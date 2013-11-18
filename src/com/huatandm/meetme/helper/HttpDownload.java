package com.huatandm.meetme.helper;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.content.Context;

public class HttpDownload {
	private URL url = null;
	private int size = 1;
	private int hasRead = 0;

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getHasRead() {
		return hasRead;
	}

	public void setHasRead(int hasRead) {
		this.hasRead = hasRead;
	}

	public String download(String str) {
		StringBuffer sb = new StringBuffer();
		String line = null;
		BufferedReader br = null;
		URL url = null;
		try {
			url = new URL(str);

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			br = new BufferedReader(new InputStreamReader(
					((URLConnection) conn).getInputStream()));

			while ((line = br.readLine()) != null) {
				sb.append(line);

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		finally {
			try {
				br.close();
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
		return sb.toString();
	}

	public int downFile(String urlStr, String pujiang, String fileName,
			Context context) {
		InputStream inputStream = null;
		try {
			FileUtils fileUtils = new FileUtils(context);

			// if (fileUtils.isFileExist(pujiang+fileName)) {
			// return 1;
			// } else {
			inputStream = getInputStreamFromUrl(urlStr);
			File resultFile = fileUtils.write2SDFromInput(hasRead, size,
					pujiang, fileName, inputStream, context);
			if (resultFile == null) {
				return -1;
			}
			// }
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		} finally {
			try {
				inputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return 0;

	}

	public InputStream getInputStreamFromUrl(String urlStr)
			throws MalformedURLException, IOException {
		url = new URL(urlStr);

		HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();

		size = urlConn.getContentLength();

		InputStream inputStream = urlConn.getInputStream();

		return inputStream;
	}

}
