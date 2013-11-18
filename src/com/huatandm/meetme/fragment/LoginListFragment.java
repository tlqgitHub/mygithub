package com.huatandm.meetme.fragment;

import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.huatandm.meetme.ActivityUtils;
import com.huatandm.meetme.CONTANT;
import com.huatandm.meetme.HTMainActivity;
import com.huatandm.meetme.R;
import com.huatandm.meetme.helper.Connector;
import com.umeng.analytics.MobclickAgent;

public class LoginListFragment extends BaseListFragment {

	private String mEmail;
	private String mPassword;
	// UI references.
	private EditText mEmailView;
	private EditText mPasswordView;

	private int lFromFraId = 0;// 返回参数
	int user_iIdProfile;// 接口版本号
	SharedPreferences login;

	@SuppressWarnings("static-access")
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		Bundle b = ((HTMainActivity) getActivity()).getBundle_data();
		login = getActivity().getSharedPreferences("login",
				getActivity().MODE_PRIVATE);
		lFromFraId = b.getInt("fromkey", 0);

		View lView = LayoutInflater.from(getActivity()).inflate(
				R.layout.cell_login_item2, null);

		getListView().addHeaderView(lView, null, false);
		getListView().setDividerHeight(0);
		getListView().setAdapter(getListAdapter());

		mEmailView = (EditText) lView.findViewById(R.id.user_name);
		mPasswordView = (EditText) lView.findViewById(R.id.pass_word);
		mEmailView.setText(login.getString("username", ""));
		mPasswordView.setText(login.getString("password", ""));

		getActivity().findViewById(R.id.sign_in_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						attemptLogin();
					}
				});
		getActivity().findViewById(R.id.fotget_key).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						forget_key();
					}
				});
	}

	protected void forget_key() {
		// TODO Auto-generated method stub
		startActivity(new Intent(Intent.ACTION_VIEW,
				Uri.parse("http://events.huatandm.com:8001/forgot.php")));
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setTitle(R.string.login);

		getLeft_bt().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				((HTMainActivity) getActivity()).back();
			}
		});
		MobclickAgent.onPageStart(this.getClass().getName());
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		hide();
		MobclickAgent.onPageEnd(this.getClass().getName());
	}

	@SuppressLint("HandlerLeak")
	Handler h = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 0:
				UIupdata();
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
	};

	private void UIupdata() {
		// TODO Auto-generated method stub
		removeProgressView();
		if (user_iIdProfile > 0) {

			login.edit().putBoolean("login", true)
					.putBoolean("logintype", true)
					.putString("username", mEmail)
					.putString("password", mPassword)
					.putInt("member_id", user_iIdProfile).commit();

			((HTMainActivity) getActivity()).loginOk(lFromFraId);

		} else {
			mPasswordView.setError(getString(R.string.msg_login_incorrect));
			mPasswordView.requestFocus();
		}
	}

	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptLogin() {
		// Reset errors.
		mEmailView.setError(null);
		mPasswordView.setError(null);

		// Store values at the time of the login attempt.
		mEmail = mEmailView.getText().toString();
		mPassword = mPasswordView.getText().toString();

		boolean cancel = false;
		View focusView = null;

		// Check for a valid password.
		if (TextUtils.isEmpty(mPassword)) {
			mPasswordView.setError(getString(R.string.error_field_required));
			focusView = mPasswordView;
			cancel = true;
		} else if (mPassword.length() < 4) {
			mPasswordView.setError(getString(R.string.error_invalid_password));
			focusView = mPasswordView;
			cancel = true;
		}

		// Check for a valid email address.
		if (TextUtils.isEmpty(mEmail)) {
			mEmailView.setError(getString(R.string.error_field_required));
			focusView = mEmailView;
			cancel = true;
		}/*
		 * else if (!mEmail.contains("@")) {
		 * mEmailView.setError(getString(R.string.error_invalid_email));
		 * focusView = mEmailView; cancel = true; }
		 */

		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			addProgressView(getActivity());
			login();
		}
	}

	private void login() {
		// TODO Auto-generated method stub

		Connector o = new Connector(CONTANT.url, mEmailView.getText()
				.toString(), mPasswordView.getText().toString());
		Object[] aParams = { mEmailView.getText().toString(), o.getPassword() };

		if (!ActivityUtils.isConnectInternet(getActivity())) {
			removeProgressView();
			Toast.makeText(getActivity(), R.string.error_net,
					Toast.LENGTH_SHORT).show();

		} else {

			LoginActionCallback oCallback = new LoginActionCallback() {

				public boolean callFailed(Exception e) {

					if (e.getMessage().endsWith("[code 1]")
							&& "dolphin.login2" == sMethod) { // method
						setMethod("dolphin.login");
						return false;
					} else {
						return true;
					}
				}

				public void callFinished(Object result) {
					int iIdProfile;
					if ("dolphin.login2" == sMethod) {
						@SuppressWarnings("unchecked")
						Map<String, Integer> map = (Map<String, Integer>) result;
						iIdProfile = map.get("member_id");
					} else {
						iIdProfile = Integer.valueOf(result.toString());
					}
					CONTANT.member_id = iIdProfile;
					user_iIdProfile = iIdProfile;
					h.sendEmptyMessage(0);
				}
			};
			oCallback.setMethod("dolphin.login2");
			oCallback.setParams(aParams);
			oCallback.setConnector(o);
			o.execAsyncMethod("dolphin.login2", aParams, oCallback,
					getActivity());
		}
	}

	class LoginActionCallback extends Connector.Callback {
		protected String sMethod;
		protected Object[] aParams;
		protected Connector oConnector;

		public void setMethod(String s) {
			sMethod = s;
		}

		public void setParams(Object[] a) {
			aParams = a;
		}

		public void setConnector(Connector o) {
			oConnector = o;
		}
	}

	private void hide() {
		@SuppressWarnings("static-access")
		InputMethodManager imm = (InputMethodManager) getActivity()
				.getSystemService(getActivity().INPUT_METHOD_SERVICE);
		boolean isOpen = imm.isActive();

		if (isOpen) {
			imm.hideSoftInputFromWindow(getActivity().getCurrentFocus()
					.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}
}
