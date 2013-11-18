package com.huatandm.meetme.fragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.huatandm.meetme.ActivityUtils;
import com.huatandm.meetme.CONTANT;
import com.huatandm.meetme.HTMainActivity;
import com.huatandm.meetme.R;
import com.huatandm.meetme.adapter.MapsContant;
import com.huatandm.meetme.helper.Connector;
import com.huatandm.meetme.helper.FileUtils;
import com.umeng.analytics.MobclickAgent;

public class MapsListFragment extends BaseListFragment {

	List<MapsContant> listMapsContants = null;
	protected Object[] m_aMaps;

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setTitle(R.string.event_map);
		initData();
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(this.getClass().getName());
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart(this.getClass().getName());
	}

	public void beginthread() {
		new Thread() {
			public void run() {
				initData();
			}
		}.start();
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
			case 1:
				removeProgressView();
				break;
			default:
				break;
			}

			super.handleMessage(msg);
		}
	};

	@Override
	public void onListItemClick(ListView lv, View v, int position, long id) {

		if (listMapsContants.get(position).getVenue_map() == null) {
			return;
		}

		String path = getMapWithName(listMapsContants.get(position)
				.getVenue_map());

		if (path.contains("http:")) {
			Uri uri = Uri.parse(listMapsContants.get(position).getVenue_map());
			Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			startActivity(intent);
		} else {

			Intent intent = new Intent();
			intent.setAction(android.content.Intent.ACTION_VIEW);
			File file = new File(path);
			intent.setDataAndType(Uri.fromFile(file), "image/*");
			startActivity(intent);
		}
	}

	public String getName(String homeurl) {
		String[] str = homeurl.split("/");
		return str[str.length - 1];
	}

	private String getMapWithName(String string) {
		// TODO Auto-generated method stub

		String name = getName(string);
		String path = CONTANT.mobilepath + "/" + CONTANT.totalfolder + "/"
				+ CONTANT.eventIds + "/" + CONTANT.maps + "/" + name;

		File file = new File(path);

		if (file.exists()) {
			return path;
		} else {
			String[] params = { string, name };// url,path,filename
			new MyASyncTask().execute(params);
			return string;
		}
	}

	public class MyASyncTask extends AsyncTask<String, Integer, Object> {

		@Override
		protected Object doInBackground(String... params) {
			// TODO Auto-generated method stub

			FileOutputStream f = null;

			try {
				// 连接地址
				URL u = new URL(params[0]);
				HttpURLConnection c = (HttpURLConnection) u.openConnection();
				c.setRequestMethod("GET");
				c.setDoOutput(true);
				c.connect();

				File f0 = new File(CONTANT.mobilepath + "/"
						+ CONTANT.totalfolder + "/" + CONTANT.eventIds + "/"
						+ CONTANT.maps);
				if (!f0.exists()) {
					FileUtils.creatSDDir("/" + CONTANT.totalfolder + "/"
							+ CONTANT.eventIds + "/" + CONTANT.maps);
				}
				f = new FileOutputStream(new File(CONTANT.mobilepath + "/"
						+ CONTANT.totalfolder + "/" + CONTANT.eventIds + "/"
						+ CONTANT.maps + "/" + params[1]));
				InputStream in = c.getInputStream();

				// 下载的代码
				byte[] buffer = new byte[1024];
				int len1 = 0;

				while ((len1 = in.read(buffer)) > 0) {
					f.write(buffer, 0, len1);
				}

			} catch (Exception e) {

				return null;
			} finally {
				try {
					f.flush();
					f.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			return "ok";
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub
		}

		@Override
		protected void onPostExecute(Object result) {
			// TODO Auto-generated method stub
			if (null != result) {
				// h.sendEmptyMessage(1);
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void UIupdata() {
		// TODO Auto-generated method stub

		if (m_aMaps == null)
			return;

		Log.d("lodi", m_aMaps.toString());
		listMapsContants = new ArrayList<MapsContant>();
		for (int i = 0; i < m_aMaps.length; i++) {

			MapsContant mapContant = new MapsContant();
			Map<String, Object> map = (Map<String, Object>) m_aMaps[i];

			mapContant.setVenue_id((Integer) map.get("venue_id"));

			mapContant.setVenue_name((String) ((Map<String, Object>) map
					.get("venue_name")).get(ActivityUtils.getLang()));

			mapContant.setVenue_address((String) ((Map<String, Object>) map
					.get("venue_address")).get(ActivityUtils.getLang()));

			mapContant
					.setVenue_introduction((String) ((Map<String, Object>) map
							.get("venue_introduction")).get(ActivityUtils
							.getLang()));

			mapContant.setVenue_map((String) map.get("venue_map"));

			listMapsContants.add(mapContant);
		}

		removeProgressView();
		setListAdapter(new MapsAdapter(getActivity(), listMapsContants));
	}

	class MapsAdapter extends BaseAdapter {
		Context m_context;
		List<MapsContant> m_listMapsContants;

		public MapsAdapter(Context context, List<MapsContant> listMapsContants) {
			this.m_context = context;
			this.m_listMapsContants = listMapsContants;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return m_listMapsContants.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int arg0, View convertView, ViewGroup arg2) {
			// TODO Auto-generated method stub
			if (convertView == null)
				convertView = LayoutInflater.from(m_context).inflate(
						R.layout.invesitigate, null);

			TextView eventname = (TextView) convertView
					.findViewById(R.id.invesitigatename);
			eventname.setText(m_listMapsContants.get(arg0).getVenue_name());
			return convertView;
		}
	}

	private void initData() {
		// TODO Auto-generated method stub
		if (!ActivityUtils.isConnectInternet(getActivity())) {
			Toast.makeText(getActivity(), R.string.error_net,
					Toast.LENGTH_SHORT).show();
			return;
		}

		addProgressView(getActivity());

		Connector o = ((HTMainActivity) getActivity()).getConnector();
		Object params[] = { CONTANT.eventIds, ActivityUtils.getLang() };
		o.execAsyncMethod("dolphin.VenuesList", params,
				new Connector.Callback() {
					@Override
					public void callFinished(Object result) {
						// TODO Auto-generated method stub
						m_aMaps = (Object[]) result;
						h.sendEmptyMessage(0);
						super.callFinished(result);
					}

					@Override
					public boolean callFailed(Exception e) {
						// TODO Auto-generated method stub
						h.sendEmptyMessage(1);
						return super.callFailed(e);
					}
				}, getActivity());
		h.sendEmptyMessage(0);
	}
}
