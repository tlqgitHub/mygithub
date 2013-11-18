package com.huatandm.meetme.fragment.detail;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.huatandm.meetme.ActivityUtils;
import com.huatandm.meetme.CONTANT;
import com.huatandm.meetme.HTMainActivity;
import com.huatandm.meetme.R;
import com.huatandm.meetme.adapter.SchedulesContant;
import com.huatandm.meetme.adapter.SpeakersContant;
import com.huatandm.meetme.fragment.BaseListFragment;
import com.huatandm.meetme.helper.Connector;
import com.umeng.analytics.MobclickAgent;

public class UserDetailListFragment extends BaseListFragment {

	Map<String, String> m_Messages = null;
	String fragmentName;
	private SpeakersContant schedulemeet;
	private int speakerid;
	List<SchedulesContant> listSchedulesContants;

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
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.list_user_info, null);
	}

	public void onSaveBundleState(Bundle outState) {
		// TODO Auto-generated method stub
		Bundle out = ((HTMainActivity) getActivity()).getOutState();
		ArrayList<Bundle> list = null;

		if (out.getParcelableArrayList(this.getClass().getName()) != null) {
			list = out.getParcelableArrayList(this.getClass().getName());
		} else {
			list = new ArrayList<Bundle>();
		}

		list.add(outState);
		out.putParcelableArrayList(this.getClass().getName(), list);
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		if (((HTMainActivity) getActivity()).isBack()) {

			Bundle out = ((HTMainActivity) getActivity()).getOutState();
			if (out.getParcelableArrayList(this.getClass().getName()) != null) {
				ArrayList<Bundle> list = out.getParcelableArrayList(this
						.getClass().getName());
				if (list.size() > 0) {

					bundle = list.get(list.size() - 1);
					list.remove(list.size() - 1);
					out.putParcelableArrayList(this.getClass().getName(), list);
				}
			}
		}

		fragmentName = bundle.getString("fragment");
		speakerid = bundle.getInt("speaker_id");

		if (fragmentName.equals("SpeakersListFragment")) {
			setTitle(R.string.speakersdetail);
		} else if (fragmentName.equals("PartivipantsListFragment")) {
			setTitle(R.string.meetpeopledetail);
		} else if (fragmentName.equals("SchedulesDetailFragment")) {
			setTitle(R.string.speakersdetail);
		}

		addProgressView(getActivity());
		initData();
	}

	public void beginthread() {
		new Thread() {
			public void run() {
				initData();
			}
		}.start();
	}

	@Override
	public void onListItemClick(ListView lv, View v, int position, long id) {

		if (position < 4) {
			return;
		} else {
			Bundle b = new Bundle();// b.putString("object",object.toString());
			b.putString("fragment", "SchedulesListFragment");
			b.putInt("index", position);
			b.putInt("schedule_id", listSchedulesContants.get(position - 4)
					.getSchedulesid());
			mListener.onArticleSelected(b);

			onSaveBundleState(bundle);
			((HTMainActivity) getActivity()).switchContent(
					new SchedulesDetailFragment(), 2);
		}
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

	protected void UIupdata() {
		// TODO Auto-generated method stub
		if (schedulemeet == null) {
			return;
		}

		removeProgressView();
		setListAdapter(new MyAdapter(getActivity(), schedulemeet));
	}

	public void initData() {
		schedulemeet = getSpeakerContantofid(speakerid);

		Object schedul;
		Object[] schesmeet;

		schedul = schedulemeet.getSpeaker_schedule();
		schesmeet = (Object[]) schedul;

		listSchedulesContants = new ArrayList<SchedulesContant>();

		for (int i = 0; i < schesmeet.length; i++) {

			for (int j = 0; j < CONTANT.listschedules.size(); j++) {

				if (String.valueOf(schesmeet[i]).equals(
						String.valueOf(CONTANT.listschedules.get(j)
								.getSchedulesid()))) {
					listSchedulesContants.add(CONTANT.listschedules.get(j));
					break;
				}
			}
		}

		h.sendEmptyMessage(0);
	}

	private SpeakersContant getSpeakerContantofid(int id) {
		// TODO Auto-generated method stub
		if (fragmentName.equals("PartivipantsListFragment")) {
			for (int i = 0; i < CONTANT.listspart.size(); i++) {
				SpeakersContant listContant = CONTANT.listspart.get(i);
				if (listContant.getSpeakerid() == id) {
					return listContant;
				}
			}
		} else {
			for (int i = 0; i < CONTANT.listspeakers.size(); i++) {
				SpeakersContant listContant = CONTANT.listspeakers.get(i);
				if (listContant.getSpeakerid() == id) {
					return listContant;
				}
			}
		}

		return null;
	}

	class MyAdapter extends BaseAdapter {
		Context context;

		public MyAdapter(Context context, Object map) {
			this.context = context;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if (fragmentName.equals("PartivipantsListFragment")) {
				return 3;
			} else {

				if (listSchedulesContants.size() == 0) {
					return 3;
				} else {
					return 4 + listSchedulesContants.size();
				}
			}
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

			switch (arg0) {
			case 0:
				convertView = LayoutInflater.from(context).inflate(
						R.layout.cell_with_head_item, null);

				ImageView speakerimage = (ImageView) convertView
						.findViewById(R.id.speakerimage);

				TextView speakername = (TextView) convertView
						.findViewById(R.id.speakername);
				TextView speakercomplay = (TextView) convertView
						.findViewById(R.id.speakercomplay);
				TextView speakertitle = (TextView) convertView
						.findViewById(R.id.speakertitle);

				speakername.setText((String) schedulemeet.getSpeaker_name());
				speakercomplay.setText((String) schedulemeet
						.getSpeaker_complay());
				speakertitle.setText((String) schedulemeet.getSpeaker_title());

				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inSampleSize = 1;
				String pathName = null;

				if (fragmentName.equals("PartivipantsListFragment")) {
					pathName = CONTANT.mobilepath + "/" + CONTANT.totalfolder
							+ "/" + CONTANT.eventIds + "/"
							+ CONTANT.participants + "/"
							+ schedulemeet.getSpeaker_image();
				} else {
					pathName = CONTANT.mobilepath + "/" + CONTANT.totalfolder
							+ "/" + CONTANT.eventIds + "/" + CONTANT.speakers
							+ "/" + schedulemeet.getSpeaker_image();
				}

				Bitmap bm = BitmapFactory.decodeFile(pathName);
				speakerimage.setImageBitmap(bm);

				break;
			case 1:
				convertView = LayoutInflater.from(context).inflate(
						R.layout.cell_with_add_send_item, null);

				Button but1 = (Button) convertView.findViewById(R.id.button1);
				but1.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						HTMainActivity c = (HTMainActivity) getActivity();
						if (c.isLogin()) {
							onAddFriend();
						} else {
							onSaveBundleState(bundle);
							c.login(-1);
						}
					}
				});
				Button but2 = (Button) convertView.findViewById(R.id.button2);
				but2.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						HTMainActivity c = (HTMainActivity) getActivity();
						if (c.isLogin()) {
							onSendMsg();
						} else {
							onSaveBundleState(bundle);
							c.login(-1);
						}
					}
				});
				break;
			case 2:
				convertView = LayoutInflater.from(context).inflate(
						R.layout.webview_user_info, null);
				WebView webview = (WebView) convertView
						.findViewById(R.id.webview);

				if (fragmentName.equals("PartivipantsListFragment")) {

					webview.setVisibility(View.GONE);
				} else {

					String content;
					String path = null;
					content = schedulemeet.getSpeaker_introduction();
					path = "<html><head><style type='text/css'>body {color:#000000; background-color:#FFFFFF;font-family:Arial; font-size:16px; padding:5px;}</style></head><body>"
							+ content + "</body></html>";

					webview.loadDataWithBaseURL(null, path, "text/html",
							"UTF-8", null);
					webview.setVisibility(View.VISIBLE);
				}

				break;
			case 3:

				convertView = LayoutInflater.from(context).inflate(
						R.layout.cell_line_title_item, null);
				TextView title = (TextView) convertView
						.findViewById(R.id.cell_line_textView);
				title.setText(R.string.alreadlymeet);

				break;
			default:

				convertView = LayoutInflater.from(context).inflate(
						R.layout.cell_schedules_item, null);

				TextView time = (TextView) convertView
						.findViewById(R.id.s_time);
				TextView titles = (TextView) convertView
						.findViewById(R.id.s_title);
				TextView map = (TextView) convertView.findViewById(R.id.s_map);
				TextView room = (TextView) convertView
						.findViewById(R.id.s_title0);

				time.setText(listSchedulesContants.get(arg0 - 4)
						.getSchedulestime().substring(5, 16));
				titles.setText(listSchedulesContants.get(arg0 - 4)
						.getSchedulestopic());
				map.setText(listSchedulesContants.get(arg0 - 4)
						.getSchedulesroom());
				room.setText(listSchedulesContants.get(arg0 - 4)
						.getSchedulesplace());
				break;
			}

			return convertView;
		}

		protected void onSendMsg() {
			// TODO Auto-generated method stub
			Bundle b = new Bundle();
			b.putString("fragment", "UserDetailListFragment");
			b.putString("recipient", schedulemeet.getSpeaker_name());
			b.putString("account", schedulemeet.getSpeaker_username());
			mListener.onArticleSelected(b);

			onSaveBundleState(bundle);
			((HTMainActivity) getActivity()).switchContent(
					new SendMsgListFragment(), 2);
		}

		protected void onAddFriend() {

			addProgressView(getActivity());
			Connector o = ((HTMainActivity) getActivity()).getConnector();
			String name = null;

			name = schedulemeet.getSpeaker_username();// 这个应该是加好友的名字

			Object[] aParams = { o.getUsername(), o.getPassword(), name,
					ActivityUtils.getLang() };
			o.execAsyncMethod("dolphin.addFriend", aParams,
					new Connector.Callback() {

						@Override
						public boolean callFailed(Exception e) {
							// TODO Auto-generated method stub

							return super.callFailed(e);
						}

						public void callFinished(Object result) {

							removeProgressView();

							AlertDialog dialog = new AlertDialog.Builder(
									getActivity()).create();
							dialog.setMessage(result.toString());
							dialog.setButton(DialogInterface.BUTTON_NEUTRAL,
									getString(R.string.close),
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int whichButton) {
											dialog.dismiss();
										}
									});
							dialog.show();
						}
					}, getActivity());
		}
	}
}
