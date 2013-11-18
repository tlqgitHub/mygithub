package com.huatandm.meetme;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

import net.sourceforge.pinyin4j.PinyinHelper;
import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;

import com.huatandm.meetme.adapter.SpeakersContant;
import com.huatandm.meetme.helper.FileUtils;

/**
 * @ClassName: ActivityUtils
 * @Description: ��̬���÷���
 * @author tlq
 * @date 2013-9-9 ����5:15:36
 * 
 */
public class ActivityUtils {

	/**
	 * �����û�id�����û�����
	 * 
	 * @return ·�����
	 */
	@SuppressWarnings("unchecked")
	public static String getNickWithuserId(Context context, int user_id) {

		ArrayList<SpeakersContant> listdata = new ArrayList<SpeakersContant>();

		// ����
		Object datas1 = ActivityUtils.parse(context, CONTANT.participants);
		Map<String, Object> maps1 = (Map<String, Object>) datas1;
		Object[] menu1 = (Object[]) maps1.get("content");

		for (int i = 0; i < menu1.length; i++) {

			SpeakersContant speakersContant = new SpeakersContant();
			Map<String, Object> map = (Map<String, Object>) menu1[i];

			speakersContant
					.setSpeaker_image((String) map.get("speaker_avatar"));
			;

			speakersContant
					.setSpeaker_compare(ActivityUtils
							.getPinYinHeadChar((String) ((Map<String, Object>) map
									.get("speaker_name")).get(ActivityUtils
									.getLang())));

			speakersContant.setSpeaker_name((String) ((Map<String, Object>) map
					.get("speaker_name")).get(ActivityUtils.getLang()));

			speakersContant
					.setSpeaker_complay((String) ((Map<String, Object>) map
							.get("speaker_company")).get(ActivityUtils
							.getLang()));

			speakersContant
					.setSpeaker_title((String) ((Map<String, Object>) map
							.get("speaker_title")).get(ActivityUtils.getLang()));

			speakersContant
					.setSpeaker_introduction((String) ((Map<String, Object>) map
							.get("speaker_introduction")).get(ActivityUtils
							.getLang()));

			speakersContant.setSpeakerid((Integer) map.get("speaker_ID"));
			speakersContant.setSpeaker_username((String) map
					.get("speaker_username"));
			speakersContant.setSpeaker_schedule(map.get("speaker_schedule"));

			listdata.add(speakersContant);
		}

		// �ݽ��α�

		Object datas2 = ActivityUtils.parse(context, CONTANT.speakers);

		Map<String, Object> maps2 = (Map<String, Object>) datas2;
		Object[] menu2 = (Object[]) maps2.get("content");

		for (int i = 0; i < menu2.length; i++) {

			SpeakersContant speakersContant = new SpeakersContant();
			Map<String, Object> map = (Map<String, Object>) menu2[i];

			speakersContant
					.setSpeaker_image((String) map.get("speaker_avatar"));
			;

			speakersContant
					.setSpeaker_compare(ActivityUtils
							.getPinYinHeadChar((String) ((Map<String, Object>) map
									.get("speaker_name")).get(ActivityUtils
									.getLang())));

			speakersContant.setSpeaker_name((String) ((Map<String, Object>) map
					.get("speaker_name")).get(ActivityUtils.getLang()));

			speakersContant
					.setSpeaker_complay((String) ((Map<String, Object>) map
							.get("speaker_company")).get(ActivityUtils
							.getLang()));

			speakersContant
					.setSpeaker_title((String) ((Map<String, Object>) map
							.get("speaker_title")).get(ActivityUtils.getLang()));

			speakersContant
					.setSpeaker_introduction((String) ((Map<String, Object>) map
							.get("speaker_introduction")).get(ActivityUtils
							.getLang()));

			speakersContant.setSpeakerid((Integer) map.get("speaker_ID"));
			speakersContant.setSpeaker_username((String) map
					.get("speaker_username"));
			speakersContant.setSpeaker_schedule(map.get("speaker_schedule"));

			listdata.add(speakersContant);
		}

		for (int i = 0; i < listdata.size(); i++) {
			if (listdata.get(i).getSpeakerid() == user_id) {
				return listdata.get(i).getSpeaker_name();
			}
		}

		return "";
	}

	/**
	 * �ж�sdcard״̬
	 * 
	 * @return ·�����
	 */
	@SuppressLint("SdCardPath")
	public static void getSDPath() {

		String path = null;
		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED); // �ж�sd���Ƿ����
		if (sdCardExist) {
			sdDir = Environment.getExternalStorageDirectory();// ��ȡ��Ŀ¼
			path = sdDir.toString();
		}

		CONTANT.mobilepath = (path == null) ? "/data/data/com.huatandm.meetmeevent/files"
				: path + "/data/com.huatandm.meetmeevent/files";

	}

	/**
	 * �ж�����״̬�Ƿ����
	 * 
	 * @return true: ������� ; false: ���粻����
	 */
	public static boolean isConnectInternet(Context context) {

		ConnectivityManager conManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = conManager.getActiveNetworkInfo();
		if (networkInfo != null) { // ע�⣬����ж�һ��Ҫ��Ŷ��Ҫ��Ȼ�����
			return networkInfo.isAvailable();
		}
		return false;
	}

	/**
	 * ����SD���ϵ�xml�ļ�
	 * 
	 * @param
	 */
	public static Object parse(Context context, String path) {
		FileUtils fileUtils = new FileUtils(context);
		Object object = fileUtils.parse(CONTANT.totalfolder + "/"
				+ CONTANT.eventIds + "/" + path + "/list.xml");
		System.out.println("parse path-------------------------"
				+ CONTANT.totalfolder + "/" + CONTANT.eventIds + "/" + path
				+ "/list.xml");
		return object;
	}

	/**
	 * ��ȡ����ƴ������ĸ
	 * 
	 * @param
	 */
	public static String getPinYinHeadChar(String str) {
		String convert = "";
		for (int j = 0; j < str.length(); j++) {
			char word = str.charAt(j);
			// ��ȡ���ֵ�����ĸ
			String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
			if (pinyinArray != null) {
				convert += pinyinArray[0].charAt(0);
			} else {
				convert += word;
			}
		}
		return convert;
	}

	/**
	 * ��ȡϵͳ��ǰ��������
	 * 
	 * @param
	 */
	public static String getLang() {
		String s = Locale.getDefault().getLanguage();
		// Log.d("TAG", "language: "+s);
		return s;
	}

	/**
	 * �޸�apkȨ��
	 * 
	 * @param permission
	 * @param path
	 *            apk���ڵ�cache�����·��
	 */
	public static void chmod(String permission, String path) {
		try {
			String command = "chmod " + permission + " " + path;
			Runtime runtime = Runtime.getRuntime();
			runtime.exec(command);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
