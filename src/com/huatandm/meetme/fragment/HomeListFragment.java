package com.huatandm.meetme.fragment;

import java.io.File;
import java.util.Map;

import org.vudroid.pdfdroid.PdfViewerActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.huatandm.meetme.ActivityUtils;
import com.huatandm.meetme.CONTANT;
import com.huatandm.meetme.HTMainActivity;
import com.huatandm.meetme.R;
import com.huatandm.meetme.fragment.detail.WebDetailFragment;
import com.umeng.analytics.MobclickAgent;

public class HomeListFragment extends BaseListFragment {

	private String[] mData;
	MyAdapter adapter = null;

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setTitle(R.string.home);
		initData();
	}

	public void initData() {
		// TODO Auto-generated method stub
		mData = getData();
		h.sendEmptyMessage(0);
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

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(this.getClass().getName());
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Button left_bt = getLeft_bt();
		left_bt.setBackgroundResource(R.drawable.icon_meun);
		left_bt.setVisibility(View.VISIBLE);
		left_bt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				((HTMainActivity) getActivity()).toggle();
			}
		});

		getRight_view().setVisibility(View.VISIBLE);

		Button right_bt = getRight_bt();
		right_bt.setVisibility(View.VISIBLE);

		if (((HTMainActivity) getActivity()).isLogin()) {
			right_bt.setBackgroundResource(R.drawable.icon_logout);
		} else {
			right_bt.setBackgroundResource(R.drawable.icon_login);
		}

		right_bt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (((HTMainActivity) getActivity()).isLogin()) {
					((HTMainActivity) getActivity()).logout();
				} else {
					((HTMainActivity) getActivity()).login(0);
				}
			}
		});
		MobclickAgent.onPageStart(this.getClass().getName());
	}

	protected void UIupdata() {
		// TODO Auto-generated method stub
		adapter = new MyAdapter(getActivity());
		setListAdapter(adapter);
	}

	@SuppressWarnings({ "unchecked", "static-access" })
	@Override
	public void onListItemClick(ListView lv, View v, int position, long id) {

		HTMainActivity c = (HTMainActivity) getActivity();
		switch (position) {
		case 0:
			if (c.isLogin()) {

				if (!ActivityUtils.isConnectInternet(getActivity())) {
					Builder builder = new AlertDialog.Builder(getActivity());
					builder.setTitle(getActivity().getResources().getString(
							R.string.exception));
					builder.setMessage(R.string.error_connect);
					builder.setNegativeButton(R.string.ok, null);
					builder.show();
					return;
				}
				SharedPreferences login = getActivity().getSharedPreferences(
						"login", getActivity().MODE_PRIVATE);
				Bundle b = new Bundle();
				b.putString("fragment", "HomeListFragment");
				b.putString("username", login.getString("username", ""));
				mListener.onArticleSelected(b);
				c.switchContent(new WebDetailFragment(), 1);

			} else {
				c.login(0);
			}
			break;
		case 1:
			if (ActivityUtils.getLang().equals("zh")) {
				startActivity(new Intent(Intent.ACTION_VIEW,
						Uri.parse(CONTANT.pif_web_url_zh)));
			} else {
				startActivity(new Intent(Intent.ACTION_VIEW,
						Uri.parse(CONTANT.pif_web_url_en)));
			}
			break;
		case 2:
			Bundle b = new Bundle();
			b.putInt("index", 0);
			b.putString("fragment", "nullFragment");
			((HTMainActivity) getActivity()).setBundle_data(b);

			c.switchContent(new WebDetailFragment(), 1);
			break;
		case 3:
			c.switchContent(new SchedulesListFragment(), 1);
			break;
		case 4:
			c.switchContent(new SpeakersListFragment(), 1);
			break;
		case 5:
			c.switchContent(new SponsorsListFragment(), 1);
			break;
		case 6:
			Map<String, Object> maps = (Map<String, Object>) ActivityUtils
					.parse(getActivity(), CONTANT.docs);
			Object[] menu = (Object[]) maps.get("content");
			Map<String, Object> map = null;
			String path = null;
			String fileName = null;
			for (int i = 0; i < menu.length; i++) {

				map = (Map<String, Object>) menu[i];
				if (map.get("doc_language").equals(ActivityUtils.getLang())) {
					path = (String) map.get("doc_address");
					fileName = (String) map.get("doc_title");
				}
			}

			if (path != null) {

				Uri uri = Uri.fromFile(new File(CONTANT.mobilepath + "/"
						+ CONTANT.totalfolder + "/" + CONTANT.eventIds + "/"
						+ CONTANT.docs + "/" + path));

				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				intent.putExtra("pdf_name", fileName);
				intent.setClass(getActivity(), PdfViewerActivity.class);
				startActivity(intent);
			}
			break;

		default:
			break;
		}
	}

	private String[] getData() {

		String[] titles = getResources()
				.getStringArray(R.array.home_list_names);
		return titles;
	}

	public class MyAdapter extends BaseAdapter {

		private LayoutInflater mInflater;
		TextView title;
		ImageView imageview;

		View v1;
		View v2;

		public MyAdapter(Context context) {
			this.mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mData.length;
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int getViewTypeCount() {
			// TODO Auto-generated method stub
			return 2;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@SuppressWarnings("static-access")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			if (position == 1) {
				convertView = mInflater.inflate(R.layout.cell_home_ad_item,
						null);
				imageview = (ImageView) convertView
						.findViewById(R.id.imageView1);
				imageview.setImageResource(R.drawable.banner);
			} else if (position == 7) {
				convertView = mInflater.inflate(R.layout.cell_home_ad_item,
						null);
				imageview = (ImageView) convertView
						.findViewById(R.id.imageView1);
				imageview.setImageResource(R.drawable.organizers);
			} else if (position == 0) {
				convertView = mInflater.inflate(R.layout.cell_home_list_item,
						null);
				title = (TextView) convertView.findViewById(R.id.textView1);
//				title.requestFocus();
				if (((HTMainActivity) getActivity()).isLogin()) {
					String name = ActivityUtils.getNickWithuserId(
							getActivity(),
							getActivity().getSharedPreferences("login",
									getActivity().MODE_PRIVATE).getInt(
									"member_id", 0));

					String userName = getActivity().getSharedPreferences(
							"login", getActivity().MODE_PRIVATE).getString(
							"username", "");
					name = name.equals("") ? userName : name;
					title.setText(name + ", " + mData[0]);
//					title.setText(name + ", " + getText(R.string.wellcome_you));
				} else {
					title.setText(mData[0]);
				}
			} else {
				convertView = mInflater.inflate(R.layout.cell_home_list_item,
						null);
				title = (TextView) convertView.findViewById(R.id.textView1);
				title.setText(mData[position]);
			}
			return convertView;
		}
	}

	public void logout() {
		// TODO Auto-generated method stub
		if (((HTMainActivity) getActivity()).isLogin()) {
			getRight_bt().setBackgroundResource(R.drawable.icon_logout);
		} else {
			getRight_bt().setBackgroundResource(R.drawable.icon_login);
		}

		adapter.notifyDataSetChanged();
	}
}
