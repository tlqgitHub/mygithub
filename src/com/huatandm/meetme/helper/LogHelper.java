package com.huatandm.meetme.helper;

import android.annotation.SuppressLint;
import android.util.Log;

/**
 * @ClassName: LogHelper
 * @Description: debug 辅助
 * @author tlq
 * @date 2013-9-12 上午8:57:26
 * 
 */
public class LogHelper {

	private static boolean mIsDebugMode = false;// 获取堆栈信息会影响性能，发布应用时记得关闭DebugMode
	private static String mLogTag = "LogHelper";

	private static final String CLASS_METHOD_LINE_FORMAT = "%s.%s()  Line:%d  (%s)";

	@SuppressLint("DefaultLocale")
	public static void trace() {
		if (mIsDebugMode) {
			StackTraceElement traceElement = Thread.currentThread()
					.getStackTrace()[3];// 从堆栈信息中获取当前被调用的方法信息
			String logText = String.format(CLASS_METHOD_LINE_FORMAT,
					traceElement.getClassName(), traceElement.getMethodName(),
					traceElement.getLineNumber(), traceElement.getFileName());
			Log.d(mLogTag, logText);// 打印Log
		}
	}
}
