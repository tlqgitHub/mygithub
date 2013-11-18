﻿package com.huatandm.meetme.helper;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.URI;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.xmlrpc.android.XMLRPCClient;
import org.xmlrpc.android.XMLRPCRedirectException;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.huatandm.meetme.R;

/*
 * Example:
 *         
 *      BxConnector o = new BxConnector ("http://192.168.1.64/d700/xmlrpc/", "hihi", "aaaaa");
 *
 *      Object[] aParams = {
 *      		"123",
 *      		"456",
 *      };      
 *
 *      o.execAsyncMethod("dolphin.concat", aParams, new BxConnector.Callback() {
 *			public void callFinished(Object result) {				
 *				t.setText (result.toString());
 *			}
 *      });
 * 
 * 
 * 	
 */
public class Connector extends Object implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final String TAG = "OO Connector";
	private static final String FILENAME = "connector.ser";

	transient protected XMLRPCClient m_oClient;
	// transient protected ProgressDialog m_dialogProgress;
	protected String m_sUrl;
	protected String m_sUsername;
	protected String m_sPwd;
	protected String m_sPwdClear;
	protected int m_iProtocolVer;
	protected int m_iUnreadLetters;
	protected int m_iFriendRequests;
	protected boolean m_bImagesReloadRequired;
	protected boolean m_bAlbumsReloadRequired;
	public static Context m_context;
	public static Timer timer;
	public static TimerTask timerTask;

	public Connector(String sUrl, String sUsername, String sPwd) {
		m_sUrl = sUrl;
		m_sUsername = sUsername;
		m_sPwdClear = sPwd;
		m_sPwd = sPwd;
		m_iProtocolVer = 2;
		m_oClient = new XMLRPCClient(URI.create(m_sUrl));
	}

	public Connector(String sUrl) {
		m_sUrl = sUrl;
		m_oClient = new XMLRPCClient(URI.create(m_sUrl));
	}

	@SuppressWarnings("static-access")
	public void execAsyncMethod(String sMethod, Object[] aParams,
			Callback oCallBack, Context context) {
		this.m_context = context;

		// 部分机型 android.view.WindowManager$BadTokenExcept ion: Unable t o add
		// window -- t oken android.os.BinderProxy@4057a570 is
		// not valid; is your act ivit y running?
		// String sLoading =
		// m_context.getResources().getString(R.string.loading);
		//
		// if (null != context)
		// m_dialogProgress = ProgressDialog.show(context, "", sLoading, true,
		// false);
		// else
		// m_dialogProgress = null;

		XMLRPCMethod method = new XMLRPCMethod(sMethod, oCallBack);
		method.call(aParams);
	}

	public static class Callback {
		public void callFinished(Object result) {

		}

		public boolean callFailed(Exception e) {
			return true;
		}
	}

	class XMLRPCMethod extends Thread {
		private int redirectsCount = 0;
		private String method;
		private Object[] params;
		private Handler handler;
		private Callback callBack;

		public XMLRPCMethod(String method, Callback callBack) {
			this.method = method;
			this.callBack = callBack;
			handler = new Handler();
		}

		public void call() {
			call(null);
		}

		public void call(Object[] params) {

			this.params = params;
			start();
		}

		@Override
		public void run() {

			try {

				boolean isRepeatLoop;
				do {

					isRepeatLoop = false;
					try {

						final long t0 = System.currentTimeMillis();

						final Object result = m_oClient.call(method, params);
						final long t1 = System.currentTimeMillis();
						handler.post(new Runnable() {
							@SuppressWarnings("unchecked")
							public void run() {
								Log.i(TAG, "XML-RPC call took " + (t1 - t0)
										+ "ms");
								// if (null != m_dialogProgress)
								// m_dialogProgress.dismiss();

								if ((result instanceof Map)
										&& null != ((Map<String, String>) result)
												.get("error")) {

									Builder builder = new AlertDialog.Builder(
											m_context);
									builder.setTitle(m_context.getResources()
											.getString(R.string.exception));
									builder.setMessage(m_context.getResources()
											.getString(R.string.error));
									builder.setNegativeButton("cancel", null);
									builder.show();

								} else {

									callBack.callFinished(result);
								}
							}
						});

					} catch (final XMLRPCRedirectException e) {
						System.out.println("555服务器返回的值是  " + e.getMessage());
						if (++redirectsCount < 4) {
							m_sUrl = e.getRedirectUrl();
							Log.i(TAG, "Redirect: " + m_sUrl);
							m_oClient = new XMLRPCClient(URI.create(m_sUrl));
							isRepeatLoop = true;
						} else {
							throw new Exception("Redirection limit exceeded");
						}

					}

				} while (isRepeatLoop);

			} catch (final Exception e) {
				// System.out.println("111服务器返回的值是  " + e.getMessage());
				handler.post(new Runnable() {
					public void run() {
						System.out.println("服务器返回的值是  " + e.getMessage());
						Log.w(TAG, "Error: " + e.getMessage());
						// if (null != m_dialogProgress)
						// m_dialogProgress.dismiss();

						if (callBack.callFailed(e)) {
							//
							// Builder builder = new
							// AlertDialog.Builder(m_context);
							// builder.setTitle(m_context.getResources()
							// .getString(R.string.exception));
							// builder.setMessage(R.string.error_connect);
							// builder.setNegativeButton(R.string.ok, null);
							// builder.show();
						}
					}
				});
			}
		}

	}

	public String md5(String s) {
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			digest.update(s.getBytes());
			byte[] hash = digest.digest();
			String sRet = "";
			for (int i = 0; i < digest.getDigestLength(); ++i)
				sRet += String.format("%02x", hash[i]);
			return sRet;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return "";
		}
	}

	public String getSiteUrl() {
		return m_sUrl;
	}

	public String getUsername() {
		return m_sUsername;
	}

	public String getPassword() {
		return md5(m_sPwd);
	}

	public String setPassword(String s) {
		return (m_sPwd = s);
	}

	public String getPasswordClear() {
		return m_sPwdClear;
	}

	public String setPasswordClear(String s) {
		return (m_sPwdClear = s);
	}

	public int getProtocolVer() {
		return m_iProtocolVer;
	}

	public int setProtocolVer(int i) {
		return (m_iProtocolVer = i);
	}

	public int getUnreadLettersNum() {
		return m_iUnreadLetters;
	}

	public int setUnreadLettersNum(int i) {
		return (m_iUnreadLetters = i);
	}

	public int getFriendRequestsNum() {
		return m_iFriendRequests;
	}

	public int setFriendRequestsNum(int i) {
		return (m_iFriendRequests = i);
	}

	public boolean setImagesReloadRequired(boolean b) {
		return (m_bImagesReloadRequired = b);
	}

	public boolean getImagesReloadRequired() {
		return m_bImagesReloadRequired;
	}

	public boolean setAlbumsReloadRequired(boolean b) {
		return (m_bAlbumsReloadRequired = b);
	}

	public boolean getAlbumsReloadRequired() {
		return m_bAlbumsReloadRequired;
	}

	private void writeObject(ObjectOutputStream out) throws IOException {
		out.defaultWriteObject();
	}

	private void readObject(ObjectInputStream in) throws IOException,
			ClassNotFoundException {
		in.defaultReadObject();
		m_oClient = new XMLRPCClient(URI.create(m_sUrl));
	}

	public static void saveConnector(Context context, Connector oConnector) {
		FileOutputStream fos = null;
		ObjectOutputStream out = null;
		try {
			fos = context.openFileOutput(FILENAME, 0);
			out = new ObjectOutputStream(fos);
			out.writeObject(oConnector);
			out.close();
		} catch (IOException e) {
			Log.e(TAG, "Error during reading from file: " + e.getMessage());
		}
	}

	public static Connector restoreConnector(Context context) {
		FileInputStream fis = null;
		ObjectInputStream in = null;
		Connector oConnector = null;
		try {
			fis = context.openFileInput(FILENAME);
			in = new ObjectInputStream(fis);
			oConnector = (Connector) in.readObject();
			in.close();
		} catch (IOException e) {
			Log.e(TAG, "Error during writing to file: " + e.getMessage());
		} catch (ClassNotFoundException e) {
			Log.e(TAG, "Error during writing to file: " + e.getMessage());
		}
		return oConnector;
	}
}
