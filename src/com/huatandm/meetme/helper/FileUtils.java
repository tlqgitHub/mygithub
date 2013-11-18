package com.huatandm.meetme.helper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlrpc.android.XMLRPCSerializer;

import com.huatandm.meetme.CONTANT;

import android.content.Context;
import android.os.Environment;

public class FileUtils {
	private String SDPATH;
	Context context;
	private Object object;
	private File desFile;
	public static List<String> list;

	public String getSDPATH() {
		return SDPATH;
	}

	public FileUtils(Context context) {
		// 得到当前外部存储设备的目录
		// /SDCARD
		this.context = context;
		SDPATH = CONTANT.mobilepath;
	}

	public static boolean ishasSD() {
		if (Environment.getExternalStorageDirectory().equals(
				Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}

	}

	/**
	 * 在SD卡上创建文件
	 * 
	 * @throws IOException
	 */
	public File creatSDFile(String fileName) throws IOException {
		File file = new File(SDPATH + fileName);
		// System.out.println("文件"+SDPATH+fileName);
		file.createNewFile();
		return file;
	}

	/**
	 * 在SD卡上创建目录
	 * 
	 * @param dirName
	 */
	public static File creatSDDir(String dirName) {
		File dir = new File(CONTANT.mobilepath + "/" + dirName);
		dir.mkdirs();
		return dir;
	}

	/**
	 * 判断SD卡上的文件夹是否存在
	 */
	public boolean isFileExist(String fileName) {
		File file = new File(SDPATH + "/" + fileName);
		return file.exists();
	}

	/**
	 * 将一个InputStream里面的数据写入到SD卡中
	 */
	public File write2SDFromInput(int hasRead, int size, String path,
			String fileName, InputStream input, Context context) {
		File file = null;
		OutputStream output = null;

		try {
			creatSDDir(path);
			file = creatSDFile(path + "/" + fileName);
			output = new FileOutputStream(file);
			byte buffer[] = new byte[1024];
			int length = 0;
			while ((length = input.read(buffer)) != -1) {
				output.write(buffer, 0, length);
				hasRead += length;
			}
			output.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				output.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return file;
	}

	/*
	 * 
	 * zipFile要解压的zip压缩包 folderPath解压的路径
	 */
	@SuppressWarnings("unused")
	public void upZipFile(String zipFile, String folderPath)
			throws ZipException, IOException {
		list = new ArrayList<String>();
		File desDir = new File(getSDPATH() + "/" + CONTANT.totalfolder);
		if (!desDir.exists()) {
			desDir.mkdirs();
		}
		ZipFile zf = new ZipFile(zipFile);

		for (Enumeration<?> entries = zf.entries(); entries.hasMoreElements();) {

			ZipEntry entry = ((ZipEntry) entries.nextElement());
			String szName = entry.getName();
			InputStream in = zf.getInputStream(entry);
			if (entry.isDirectory()) {
				szName = szName.substring(0, szName.length() - 1);
				java.io.File folder = new java.io.File(folderPath
						+ File.separator + szName);
				System.out.println("文件夹名" + szName);
			} else {
				java.io.File file = new java.io.File(szName);

				list.add(szName);
				String str = folderPath + File.separator + entry.getName();
				// System.out.println("处理前  "+str);
				str = new String(str.getBytes("8859_1"), "utf-8");
				// System.out.println("处理后  "+str);

				String avatr[] = str.split("/");
				System.out.println(avatr[avatr.length - 2] + ","
						+ avatr[avatr.length - 1]);
				if (avatr[avatr.length - 2].equals("info")) {
					if (avatr[avatr.length - 1].split("_")[1]
							.equals("iphone.html")
							|| avatr[avatr.length - 1].split("_")[1]
									.equals("ipad.html")) {
						// System.out.println("要删除的后罪名"+str);
						// deleteiphonehtml(getFileName(str));
					} else {
						desFile = new File(str);
					}
				} else {
					desFile = new File(str);
				}
				if (!desFile.exists()) {
					File fileParentDir = desFile.getParentFile();
					if (!fileParentDir.exists()) {
						fileParentDir.mkdirs();
					}
					desFile.createNewFile();
				}

				OutputStream out = new FileOutputStream(desFile);
				byte buffer[] = new byte[1024];
				int realLength;
				while ((realLength = in.read(buffer)) > 0) {
					out.write(buffer, 0, realLength);
				}
				in.close();
				out.close();
			}
		}
	}

	// 得到某路径文件的流
	public BufferedReader getSDXml(String path) {
		path = getSDPATH() + "/" + path;
		// String data = "";

		BufferedReader br = null;
		// StringBuffer sb = new StringBuffer();
		File file = new File(path);

		FileInputStream stream = null;
		try {
			stream = new FileInputStream(file);
			br = new BufferedReader(new InputStreamReader(stream, "utf-8"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return br;
	}

	// 得到某路径文件的内容
	@SuppressWarnings("resource")
	public String get(String path) {
		// path=getSDPATH()+path;
		String data = "";
		BufferedReader br = null;
		StringBuffer sb = new StringBuffer();
		File file = new File(path);

		FileInputStream stream = null;
		try {
			stream = new FileInputStream(file);
			br = new BufferedReader(new InputStreamReader(stream, "utf-8"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		try {
			while ((data = br.readLine()) != null) {

				sb.append(data);
			}
			// stream.close();
			// br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return sb.toString();
	}

	// 对某路径文件解析
	public Object parse(String path) {

		BufferedReader br = null;

		br = getSDXml(path);
		// br=fileUtils.getSDXml(fileUtils.getSDPATH()+"/"+"event/"+"list.xml");
		try {
			object = goparse(br);
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return object;
	}

	public Object goparse(Reader br) throws XmlPullParserException, IOException {
		XmlPullParser pullParser = XmlPullParserFactory.newInstance()
				.newPullParser();
		pullParser.setInput(br);
		pullParser.nextTag();
		pullParser.getName();
		pullParser.require(XmlPullParser.START_TAG, null, "methodResponse");
		pullParser.nextTag();
		String tag = pullParser.getName();
		if (tag.equals("params")) {
			pullParser.nextTag();
			pullParser.require(XmlPullParser.START_TAG, null, "param");
			pullParser.nextTag();
			object = XMLRPCSerializer.deserialize(pullParser);
			System.out.println("这个object是   " + object.toString());
		}

		return object;
	}

	public void deleteiphonehtml(File file) {
		file.delete();
	}

	public File getFileName(String path) {

		File filename = new File(path);
		return filename;

	}
}
