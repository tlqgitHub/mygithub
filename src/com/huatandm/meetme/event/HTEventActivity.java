package com.huatandm.meetme.event;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.huatandm.meetme.ActivityUtils;
import com.huatandm.meetme.CONTANT;
import com.huatandm.meetme.HTMainActivity;
import com.huatandm.meetme.R;
import com.huatandm.meetme.helper.Connector;
import com.huatandm.meetme.helper.FileUtils;
import com.umeng.analytics.MobclickAgent;

/**
 * @ClassName: HTEventActivity
 * @Description: �����б� �� ѡ�����
 * @author tlq
 * @date 2013-9-9 ����5:17:38
 * 
 */
@SuppressLint("HandlerLeak")
public class HTEventActivity extends Activity {

	// defult time
	String event_time = "2012-01-01 20:15:00 +0800";

	String speakerstime = "2012-03-22 15:58:09 +0800";
	String schedulestime = "2012-03-22 15:58:09 +0800";
	String newstime = "2012-03-22 15:58:09 +0800";
	String sponsorstime = "2012-03-22 15:58:09 +0800";
	String exhibitorstime = "2012-03-22 15:58:09 +0800";
	String docstime = "2012-03-22 15:58:09 +0800";
	String participants = "2012-03-22 15:58:09 +0800";

	// �����������
	Object object = null;
	FileUtils fileUtils;// ������
	// �����б��ļ���
	Object eventlist = null;

	// ���ش洢״̬���� ��¼
	SharedPreferences homesp, login, autologin, home;

	// ״̬
	boolean degreeflag = true;
	boolean homeflag = true;

	// �����ļ���Ŀͳ��
	int jj = 0;

	// �����м�ת������
	Map<String, Object> map;

	ProgressBar mainpProgressBar;
	TextView mainTextView;
	RelativeLayout progressview;

	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		MobclickAgent.onPageStart(this.getClass().getName());
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		MobclickAgent.onPageEnd(this.getClass().getName());
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_event_list);

		autologin = getSharedPreferences("autologin", MODE_PRIVATE);
		login = getSharedPreferences("login", MODE_PRIVATE);
		homesp = getSharedPreferences("start", MODE_PRIVATE);
		home = getSharedPreferences("home", MODE_PRIVATE);
		// �����������״̬
		initApptype();

		//����Ϊ����״̬
		getSharedPreferences("login", MODE_PRIVATE).edit()
				.putBoolean("logintype", true).commit();

		init();// ��ʼ��
		bindEvent();// �¼�
	}

	private void initApptype() {
		// TODO Auto-generated method stub
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		CONTANT.widthPixels = dm.widthPixels;
		CONTANT.heightPixels = dm.heightPixels;
		ActivityUtils.getSDPath();
		// ����ͳ������
		MobclickAgent.openActivityDurationTrack(false);
		// MobclickAgent.setDebugMode(true);
		// MobclickAgent.onError(this);
		// MobclickAgent.updateOnlineConfig(this);
		//����δ��¼
		login.edit().putBoolean("login", false).putBoolean("logintype", false)
				.commit();
	}

	public void init() {
		mainpProgressBar = (ProgressBar) findViewById(R.id.mainprogressBar);
		mainTextView = (TextView) findViewById(R.id.mainprogresstextiew);
		progressview = (RelativeLayout) findViewById(R.id.progressview);
	}

	public void bindEvent() {

		fileUtils = new FileUtils(HTEventActivity.this);

		if (!ActivityUtils.isConnectInternet(this)) {
			// �ж��Ƿ��һ������app
			if (homesp.getBoolean("start", true)) {
				Toast.makeText(getApplication(), R.string.error_net,
						Toast.LENGTH_LONG).show();
				System.exit(0);
			} else {
				// ֱ�ӽ�����ҳ
				h.sendEmptyMessage(111);
			}
			return;
		}

		// �����б�ļ�������
		EvenListDownload();
	}

	Handler h = new Handler() {
		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 111:

				parse(CONTANT.totalfolder + "/" + CONTANT.event + "/list.xml");

				Map<String, Object> maps1 = (Map<String, Object>) object;
				Object[] menu1 = (Object[]) maps1.get("content");
				for (int i = 0; i < menu1.length; i++) {
					Map<String, Object> map = (Map<String, Object>) menu1[i];

					if ((Integer)map.get("event_ID")==CONTANT.event_Id) {
						
						CONTANT.event_description = (String) (((Map<String, Object>) map
								.get("event_description")).get(ActivityUtils
								.getLang()));
					}
				}

				mainTextView.setVisibility(View.GONE);
				progressview.setVisibility(View.GONE);

				Intent in = new Intent(HTEventActivity.this,
						HTMainActivity.class);
				Bundle b;
				b = new Bundle();
				b.putString("eventIds", CONTANT.eventIds);
				b.putString("eventname", getString(R.string.event_name));
				in.putExtras(b);
				startActivity(in);
				login.edit().remove("login").commit();
				finish();

				break;
			case 11:

				mainTextView.setVisibility(View.GONE);
				progressview.setVisibility(View.GONE);
				// parse(); //����

				map = (Map<String, Object>) object;
				if (map == null) {

					return;
				}
				// System.out.println("������  "+map.size()+""+","+i+"");
				if (map.size() == jj) {
					Intent it = new Intent(HTEventActivity.this,
							HTMainActivity.class);
					Bundle bundle;

					bundle = new Bundle();
					bundle.putString("eventIds", CONTANT.eventIds);
					bundle.putString("eventname",
							getString(R.string.event_name));
					it.putExtras(bundle);
					startActivity(it);
					login.edit().remove("login").commit();

					finish();
				} else if (map.containsKey("faultString")
						|| map.containsKey("faultCode")) {
					CharSequence warmtips = getText(R.string.warmtips);
					AlertDialog alertDialog = new AlertDialog.Builder(
							HTEventActivity.this)
							.setTitle(warmtips)
							.setMessage(R.string.msg_home)
							.setPositiveButton(R.string.ok,
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											// TODO Auto-generated method stub
											finish();
										}
									}).create();
					alertDialog.show();
				}

				break;
			case 1:
				progressview.setVisibility(View.VISIBLE);
				mainTextView.setVisibility(View.VISIBLE);
				down();
				break;
			case 2:
				progressview.setVisibility(View.VISIBLE);
				mainTextView.setVisibility(View.VISIBLE);
				down(eventlist);
				break;
			case 3:

				Map<String, Object> maps = (Map<String, Object>) object;
				Object[] menu = (Object[]) maps.get("content");
				for (int i = 0; i < menu.length; i++) {
					Map<String, Object> map = (Map<String, Object>) menu[i];

					if ((Integer)map.get("event_ID")==CONTANT.event_Id) {
						CONTANT.event_description = (String) (((Map<String, Object>) map
								.get("event_description")).get(ActivityUtils
								.getLang()));
					}
				}
				EventDataLoad();
				break;
			default:
				break;

			}
			super.handleMessage(msg);
		}

	};

	// �����б�
	private void EvenListDownload() {
		// TODO Auto-generated method stub

		if (homesp.getBoolean("start", true)) {
			secondfromComputer(event_time);
		} else {
			secondfromComputer(getupdatetime(parse(CONTANT.totalfolder + "/"
					+ CONTANT.event + "/list.xml"), event_time));
		}
	}

	// �����б�file ����
	public void down(Object eventlist) {

		String event_package;
		@SuppressWarnings("unchecked")
		Map<String, Object> map = (Map<String, Object>) eventlist;
		event_package = (String) map.get("event_package");
		if (event_package != null && !event_package.equals("")) {
			new MyASyncTask().execute(event_package);// �첽����
		}
	}

	// ������
	public void secondfromComputer(String updatetime) {

		System.out.println(" ����ʱ��     " + updatetime);
		Connector o = new Connector(CONTANT.offlineurl);

		Map<String, String> secondmap = new LinkedHashMap<String, String>();
		secondmap.put("update_time", updatetime);// �ճ̵�ʱ��
		Object[] params = { secondmap };
		o.execAsyncMethod("dolphin.getEventPackage", params,
				new Connector.Callback() {

					@SuppressWarnings("unchecked")
					@Override
					public void callFinished(Object result) {
						// TODO Auto-generated method stub
						// objects=(Object[])result;
						eventlist = (Object) result;

						System.out.println("getEventPackage ���ص�--------  "
								+ result.toString());

						Editor editor = homesp.edit();
						editor.putBoolean("flag", false);
						editor.putBoolean("start", false);
						editor.commit();

						if (((Map<String, Object>) eventlist).size() == 0) {

							parse(CONTANT.totalfolder + "/" + CONTANT.event
									+ "/list.xml");
							h.sendEmptyMessage(3);
						} else {
							h.sendEmptyMessage(2);
						}
						super.callFinished(result);
					}

					@Override
					public boolean callFailed(Exception e) {
						// TODO Auto-generated method stub
						FileUtils fileUtils = new FileUtils(
								HTEventActivity.this);

						if (fileUtils.isFileExist(CONTANT.totalfolder + "/"
								+ CONTANT.event)) {
							parse(CONTANT.totalfolder + "/" + CONTANT.event
									+ "/list.xml");
							h.sendEmptyMessage(3);
						} else {
							Toast.makeText(HTEventActivity.this,
									R.string.nonetwork_1, Toast.LENGTH_LONG)
									.show();
						}
						return super.callFailed(e);
					}

				}, this);
	}

	private void EventDataLoad() {
		// TODO Auto-generated method stub

		if (home.getBoolean("degreeflag", degreeflag)) {

			secondfromComputer(schedulestime, speakerstime, newstime,
					sponsorstime, exhibitorstime, docstime, participants);
			Editor editor = home.edit();
			homeflag = false;
			degreeflag = false;
			editor.putBoolean("degreeflag", degreeflag);
			editor.putBoolean("flag", homeflag);
			editor.commit();
		} else {

			secondfromComputer(
					getupdatetime(parse(CONTANT.totalfolder + "/"
							+ CONTANT.eventIds + "/" + CONTANT.schedules
							+ "/list.xml"), schedulestime),
					getupdatetime(parse(CONTANT.totalfolder + "/"
							+ CONTANT.eventIds + "/" + CONTANT.speakers
							+ "/list.xml"), speakerstime),
					getupdatetime(parse(CONTANT.totalfolder + "/"
							+ CONTANT.eventIds + "/" + CONTANT.news
							+ "/list.xml"), newstime),
					getupdatetime(parse(CONTANT.totalfolder + "/"
							+ CONTANT.eventIds + "/" + CONTANT.sponsors
							+ "/list.xml"), sponsorstime),
					getupdatetime(parse(CONTANT.totalfolder + "/"
							+ CONTANT.eventIds + "/" + CONTANT.exhibitors
							+ "/list.xml"), exhibitorstime),
					getupdatetime(parse(CONTANT.totalfolder + "/"
							+ CONTANT.eventIds + "/" + CONTANT.docs
							+ "/list.xml"), docstime),
					getupdatetime(parse(CONTANT.totalfolder + "/"
							+ CONTANT.eventIds + "/" + CONTANT.participants
							+ "/list.xml"), participants));

		}

	}

	public void secondfromComputer(String schedulestime, String speakerstime,
			String newstime, String sponsorstime, String exhibitorstime,
			String docstime, String participants) {

		// ��sd�����ʱ�䣬���sd������Ŀ¼
		System.out.println("����ʱ��  " + schedulestime + "," + schedulestime + ","
				+ newstime + "," + sponsorstime + "," + exhibitorstime + ","
				+ docstime + "," + participants);

		Connector o = new Connector(CONTANT.offlineurl);
		Map<String, String> secondmap = new LinkedHashMap<String, String>();
		secondmap.put("schedules", schedulestime);// �ճ̵�ʱ��
		secondmap.put("speakers", speakerstime);// �ݽ��α���ʱ��
		secondmap.put("news", newstime);// ���ŵ�ʱ��
		secondmap.put("sponsors", sponsorstime);// �����̵�ʱ��
		secondmap.put("exhibitors", exhibitorstime);// ���͵�ʱ��
		secondmap.put("docs", docstime);// ���ϵ�ʱ��
		secondmap.put("participants", participants);// �λ�α�ʱ��
		Object[] params = { Integer.valueOf(CONTANT.eventIds), secondmap };
		o.execAsyncMethod("dolphin.checkEventUpdate", params,
				new Connector.Callback() {

					@SuppressWarnings("unchecked")
					@Override
					public void callFinished(Object result) {
						// TODO Auto-generated method stub
						// objects=(Object[])result;
						object = (Object) result;
						System.out.println("���������ص�ֵ��  " + result.toString());
						if (((Map<String, Object>) object).size() == 0) {
							h.sendEmptyMessage(11);
						} else {
							h.sendEmptyMessage(1);
						}
						super.callFinished(result);
					}

					@Override
					public boolean callFailed(Exception e) {
						// TODO Auto-generated method stub
						FileUtils fileUtils = new FileUtils(
								HTEventActivity.this);

						if (fileUtils.isFileExist(CONTANT.totalfolder + "/"
								+ CONTANT.eventIds)) {
							h.sendEmptyMessage(11);
						} else {
							Toast.makeText(HTEventActivity.this,
									R.string.nonetwork_1, Toast.LENGTH_LONG)
									.show();
						}

						return super.callFailed(e);
					}

				}, this);
	}

	// ����xml�ļ�
	public Object parse(String path) {
		FileUtils fileUtils = new FileUtils(this);
		object = fileUtils.parse(path);
		System.out.println("1111 parse path-------------------------" + path);
		return object;

	}

	// ���ػ�������
	@SuppressWarnings("unchecked")
	public void down() {
		String speakersurl, schedulesurl, newsurl, sponsorsurl, exhibitorsurl, docsurl, participantsurl;

		Map<String, Object> map = (Map<String, Object>) object;
		speakersurl = (String) map.get("speakers");
		schedulesurl = (String) map.get("schedules");
		newsurl = (String) map.get("news");
		sponsorsurl = (String) map.get("sponsors");
		exhibitorsurl = (String) map.get("exhibitors");
		docsurl = (String) map.get("docs");
		participantsurl = (String) map.get("participants");
		System.out.println("�ݽ�" + speakersurl + "," + "�ճ�" + schedulesurl + ","
				+ "����" + newsurl + "," + "������" + sponsorsurl + "," + "չ��"
				+ exhibitorsurl + "," + "����" + docsurl);

		List<String[]> list = new ArrayList<String[]>();

		if (speakersurl != null && !speakersurl.equals("")) {
			getName(speakersurl);// �õ������ݽ��α���zip��
			list.add(new String[] { speakersurl,
					(String) getText(R.string.speakersurlzip) });
		}
		if (schedulesurl != null && !schedulesurl.equals("")) {
			getName(schedulesurl);// �õ������ճ̵�zip��
			list.add(new String[] { schedulesurl,
					(String) getText(R.string.schedulesurlzip) });
		}
		if (newsurl != null && !newsurl.equals("")) {
			getName(newsurl);// �õ��������ŵ�zip��
			list.add(new String[] { newsurl,
					(String) getText(R.string.newsurlzip) });
		}
		if (sponsorsurl != null && !sponsorsurl.equals("")) {
			getName(sponsorsurl);// �õ������ݽ��α���zip��
			list.add(new String[] { sponsorsurl,
					(String) getText(R.string.sponsorsurlzip) });
		}
		if (exhibitorsurl != null && !exhibitorsurl.equals("")) {
			getName(exhibitorsurl);// �õ����������̵�zip��
			list.add(new String[] { exhibitorsurl,
					(String) getText(R.string.exhibitorsurlzip) });
		}
		if (docsurl != null && !docsurl.equals("")) {
			getName(docsurl);// �õ��������ϵ�zip��
			list.add(new String[] { docsurl,
					(String) getText(R.string.docsurlzip) });
		}
		if (participantsurl != null && !participantsurl.equals("")) {
			getName(participantsurl);// �õ����ǲλ���Ա��zip��
			list.add(new String[] { participantsurl,
					(String) getText(R.string.participantsurlzip) });
		}

		String[][] str = new String[list.size()][2];
		for (int i = 0; i < list.size(); i++) {
			str[i] = list.get(i);
		}
		new DownLoadASyncTask().execute(str);
	}

	// �첽�����б�����
	public class MyASyncTask extends AsyncTask<String, Integer, Object> {

		@Override
		protected Object doInBackground(String... params) {
			// TODO Auto-generated method stub

			FileOutputStream f = null;

			try {
				// ���ӵ�ַ
				URL u = new URL(params[0]);
				HttpURLConnection c = (HttpURLConnection) u.openConnection();
				c.setRequestMethod("GET");
				c.setDoOutput(true);
				c.connect();

				// �����ļ�����
				int lenghtOfFile = c.getContentLength();

				File f0 = new File(CONTANT.mobilepath + "/"
						+ CONTANT.totalfolder);
				if (!f0.exists()) {
					FileUtils.creatSDDir(CONTANT.totalfolder);
				}
				System.out.println("�����б��ļ���ַ------------��" + CONTANT.mobilepath
						+ "/" + CONTANT.totalfolder + "/" + getName(params[0]));

				f = new FileOutputStream(new File(CONTANT.mobilepath + "/"
						+ CONTANT.totalfolder, getName(params[0])));
				InputStream in = c.getInputStream();

				// ���صĴ���
				byte[] buffer = new byte[1024];
				int len1 = 0;
				long total = 0;

				while ((len1 = in.read(buffer)) > 0) {
					total += len1; // total = total + len1
					f.write(buffer, 0, len1);
					publishProgress((int) ((total * 100) / lenghtOfFile));
				}

			} catch (Exception e) {

			} finally {
				try {
					f.flush();
					f.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			//
			upzip(0, params[0]);
			// ����
			return parse(CONTANT.totalfolder + "/" + CONTANT.event
					+ "/list.xml");
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub
			if (values[0] == 100) {
				mainTextView.setText(getString(R.string.eventslistzip) + ".. "
						+ getString(R.string.unzip));
			} else {
				mainTextView.setText(getString(R.string.eventslistzip) + ".. "
						+ values[0] + "%");
			}
			mainpProgressBar.setProgress(values[0]);
		}

		@Override
		protected void onPostExecute(Object result) {
			// TODO Auto-generated method stub
			if (object != null) {
				h.sendEmptyMessage(3);
			}
		}
	}

	public class DownLoadASyncTask extends AsyncTask<String[], String, String> {

		int count;

		@Override
		protected String doInBackground(String[]... params) {
			// TODO Auto-generated method stub

			File f0 = new File(CONTANT.mobilepath + "/" + CONTANT.totalfolder
					+ "/" + CONTANT.eventIds);
			if (!f0.exists()) {
				FileUtils.creatSDDir("/" + CONTANT.totalfolder + "/"
						+ CONTANT.eventIds);
			}

			count = params.length;

			for (int i = 0; i < params.length; i++) {

				FileOutputStream f = null;
				try {
					// ���ӵ�ַ
					URL u = new URL(params[i][0]);
					HttpURLConnection c = (HttpURLConnection) u
							.openConnection();
					c.setRequestMethod("GET");
					c.setDoOutput(true);
					c.connect();

					// �����ļ�����
					long lenghtOfFile = c.getContentLength();

					f = new FileOutputStream(new File(CONTANT.mobilepath + "/"
							+ CONTANT.totalfolder + "/" + CONTANT.eventIds + ""
							+ "/" + getName(params[i][0])));
					InputStream in = c.getInputStream();
					System.out.println("���ص��ļ�------------��"
							+ CONTANT.mobilepath + "/" + CONTANT.totalfolder
							+ "/" + CONTANT.eventIds + "" + "/"
							+ getName(params[i][0]));
					// ���صĴ���
					byte[] buffer = new byte[1024];
					int len1 = 0;
					long total = 0;

					while ((len1 = in.read(buffer)) > 0) {
						total += len1; // total = total + len1
						publishProgress(new String[] {
								String.valueOf((int) ((total * 100) / lenghtOfFile)),
								params[i][1] });
						f.write(buffer, 0, len1);
					}

				} catch (Exception e) {

				} finally {
					try {
						f.flush();
						f.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				upzip(1, params[i][0]);

				jj++;
				System.out.println("���ص��ļ�����" + jj + "");
			}

			params = null;
			return "�������";
		}

		@Override
		protected void onProgressUpdate(String... values) {
			// TODO Auto-generated method stub

			if (values[0].equals("100")) {
				mainTextView.setText("" + values[1] + ".. "
						+ getString(R.string.unzip));
			} else {
				mainTextView.setText("" + values[1] + ".. " + values[0] + "%");
			}
			mainpProgressBar.setProgress((100 / count) * jj
					+ Integer.parseInt(values[0]) / count);
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			h.sendEmptyMessage(11);
		}
	}

	public static String getName(String homeurl) {
		String[] str = homeurl.split("/");
		return str[str.length - 1];
	}

	public void upzip(int index, String homeurl) {

		try {

			if (index == 0) {
				fileUtils.upZipFile(fileUtils.getSDPATH() + "/"
						+ CONTANT.totalfolder + "/"

						+ getName(homeurl), fileUtils.getSDPATH() + "/"
						+ CONTANT.totalfolder + "/" + CONTANT.event);
			} else {
				fileUtils.upZipFile(
						fileUtils.getSDPATH() + "/" + CONTANT.totalfolder + "/"
								+ CONTANT.eventIds + "" + "/"
								+ getName(homeurl),

						fileUtils.getSDPATH()
								+ "/"
								+ CONTANT.totalfolder
								+ "/"
								+ CONTANT.eventIds
								+ ""
								+ "/"
								+ getName(homeurl).substring(0,
										getName(homeurl).indexOf(".")).split(
										"_")[0]);
			}

		} catch (ZipException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		File f;
		if (index == 0) {
			f = new File(fileUtils.getSDPATH() + "/" + CONTANT.totalfolder
					+ "/" + getName(homeurl));
		} else {
			f = new File(fileUtils.getSDPATH() + "/" + CONTANT.totalfolder
					+ "/" + CONTANT.eventIds + "" + "/" + getName(homeurl));
		}

		f.delete();
	}

	@SuppressWarnings("unchecked")
	public String getupdatetime(Object updatetime, String previewtime) {
		if (updatetime != null) {
			Map<String, Object> map = (Map<String, Object>) updatetime;
			System.out.println("����ʱ��" + map.get("create_time") + "");
			return map.get("create_time") + "";
		} else {
			return previewtime;
		}
	}
}
