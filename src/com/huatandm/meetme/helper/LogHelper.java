package com.huatandm.meetme.helper;

import android.annotation.SuppressLint;
import android.util.Log;

/**
 * @ClassName: LogHelper
 * @Description: debug ����
 * @author tlq
 * @date 2013-9-12 ����8:57:26
 * 
 */
public class LogHelper {

	private static boolean mIsDebugMode = false;// ��ȡ��ջ��Ϣ��Ӱ�����ܣ�����Ӧ��ʱ�ǵùر�DebugMode
	private static String mLogTag = "LogHelper";

	private static final String CLASS_METHOD_LINE_FORMAT = "%s.%s()  Line:%d  (%s)";

	@SuppressLint("DefaultLocale")
	public static void trace() {
		if (mIsDebugMode) {
			StackTraceElement traceElement = Thread.currentThread()
					.getStackTrace()[3];// �Ӷ�ջ��Ϣ�л�ȡ��ǰ�����õķ�����Ϣ
			String logText = String.format(CLASS_METHOD_LINE_FORMAT,
					traceElement.getClassName(), traceElement.getMethodName(),
					traceElement.getLineNumber(), traceElement.getFileName());
			Log.d(mLogTag, logText);// ��ӡLog
		}
	}
}
