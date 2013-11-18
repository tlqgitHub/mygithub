package com.huatandm.meetme.fragment.detail;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.huatandm.meetme.ActivityUtils;
import com.huatandm.meetme.CONTANT;
import com.huatandm.meetme.HTMainActivity;
import com.huatandm.meetme.R;
import com.huatandm.meetme.adapter.SchedulesContant;
import com.huatandm.meetme.adapter.SpeakersContant;
import com.huatandm.meetme.fragment.BaseListFragment;
import com.huatandm.meetme.helper.AsyncImageLoader;
import com.huatandm.meetme.helper.AsyncImageLoader.ImageCallback;
import com.huatandm.meetme.helper.Connector;
import com.umeng.analytics.MobclickAgent;

public class SchedulesDetailFragment extends BaseListFragment {

	private int scheduleid;
	private SharedPreferences collect;
	private ArrayList<SpeakersContant> listSpeakers;
	private Object[] schespeakers;

	String fragmentName;
	SchedulesContant thisSchedules;
	EditText edtext = null;
	// 璇勮鍒楄〃
	private String m_text_comment = "";
	private Object[] m_aComments;

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

		setTitle(R.string.schedulesdetail);

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

		scheduleid = bundle.getInt("schedule_id");
		fragmentName = bundle.getString("fragment");
		collect = getActivity().getSharedPreferences("collect", 0);
		initData();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
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

	protected void UIupdata() {
		removeProgressView();
		getListView().setAdapter(new MyAdapter());
	}

	@Override
	public void onListItemClick(ListView lv, View v, int position, long id) {

		if (position < 4 + listSpeakers.size() && position > 3) {

			Bundle b = new Bundle();// b.putString("object",object.toString());
			b.putInt("speaker_id", listSpeakers.get(position - 4)
					.getSpeakerid());
			b.putString("fragment", "SchedulesDetailFragment");
			;
			mListener.onArticleSelected(b);

			onSaveBundleState(bundle);
			((HTMainActivity) getActivity()).switchContent(
					new UserDetailListFragment(), 2);
		}
	}

	public void initData() {

		addProgressView(getActivity());
		thisSchedules = getSchedulesContantofid(scheduleid);
		Object schespeak = null;

		schespeak = thisSchedules.getSchedulesspeakers();
		schespeakers = (Object[]) schespeak;// 鏃ョ▼鍢夊鐨勪俊鎭�

		listSpeakers = new ArrayList<SpeakersContant>();

		for (int i = 0; i < schespeakers.length; i++) {

			for (int j = 0; j < CONTANT.listspeakers.size(); j++) {

				if (String.valueOf(schespeakers[i]).equals(
						String.valueOf(CONTANT.listspeakers.get(j)
								.getSpeakerid()))) {
					listSpeakers.add(CONTANT.listspeakers.get(j));
					break;
				}
			}
		}
		getCommentList();
	}

	// 获取讨论区列表
	private void getCommentList() {
		// TODO Auto-generated method stub
		if (!ActivityUtils.isConnectInternet(getActivity())) {
			Toast.makeText(getActivity(), R.string.error_net,
					Toast.LENGTH_SHORT).show();
			m_aComments = new Object[0];
			h.sendEmptyMessage(0);
			return;
		}

		Connector o = ((HTMainActivity) getActivity()).getConnector();
		Object[] aParams = { CONTANT.eventIds, String.valueOf(scheduleid), "0",
				ActivityUtils.getLang() };
		o.execAsyncMethod("dolphin.ScheduleDiscussionList", aParams,
				new Connector.Callback() {

					@Override
					public boolean callFailed(Exception e) {
						// TODO Auto-generated method stub
						m_aComments = new Object[0];
						h.sendEmptyMessage(0);
						return super.callFailed(e);
					}

					public void callFinished(Object result) {
						m_aComments = (Object[]) result;
						h.sendEmptyMessage(0);
					}
				}, getActivity());
	}

	private SchedulesContant getSchedulesContantofid(int scheduleid2) {
		// TODO Auto-generated method stub
		for (int i = 0; i < CONTANT.listschedules.size(); i++) {
			if (CONTANT.listschedules.get(i).getSchedulesid() == scheduleid2) {
				return CONTANT.listschedules.get(i);
			}
		}
		return null;
	}

	class MyAdapter extends BaseAdapter {
		int lines = 0;
		AsyncImageLoader asyncImageLoader = new AsyncImageLoader();

		@Override
		public int getCount() {
			// TODO Auto-generated method stub

			if (listSpeakers.size() == 0) {
				lines = 0;
			} else {
				lines = 1 + listSpeakers.size();
			}

			return 3 + lines + 1 + m_aComments.length + 1;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@SuppressWarnings("unchecked")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub

			if (position == 0) {

				convertView = LayoutInflater.from(getActivity()).inflate(
						R.layout.cell_schedules_head_item, null);

				TextView time = (TextView) convertView
						.findViewById(R.id.s_time);
				TextView map = (TextView) convertView.findViewById(R.id.s_map);
				TextView title = (TextView) convertView
						.findViewById(R.id.s_title);

				time.setText(thisSchedules.getSchedulesshowtime());
				map.setText(thisSchedules.getSchedulesroom());
				title.setText(thisSchedules.getSchedulestopic());

			} else if (position == 1) {
				convertView = LayoutInflater.from(getActivity()).inflate(
						R.layout.cell_with_add_send_item, null);

				Button but1 = (Button) convertView.findViewById(R.id.button1);
				but1.setBackgroundResource(R.drawable.icon_map_big);
				but1.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						showMap();
					}
				});
				Button but2 = (Button) convertView.findViewById(R.id.button2);

				if (collect.getInt(thisSchedules.getSchedulesid() + "", 0) != thisSchedules
						.getSchedulesid()) {

					but2.setBackgroundResource(R.drawable.icon_favorites);
				} else {

					but2.setBackgroundResource(R.drawable.icon_favorites_hover);
				}

				but2.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (collect.getInt(thisSchedules.getSchedulesid() + "",
								0) != thisSchedules.getSchedulesid()) {

							collect.edit()
									.putInt(thisSchedules.getSchedulesid() + "",
											thisSchedules.getSchedulesid())
									.commit();
							v.setBackgroundResource(R.drawable.icon_favorites_hover);
							Toast.makeText(getActivity(),
									getString(R.string.collectschedules),
									Toast.LENGTH_SHORT).show();
						} else {

							collect.edit()
									.remove(thisSchedules.getSchedulesid() + "")
									.commit();
							v.setBackgroundResource(R.drawable.icon_favorites);
							Toast.makeText(getActivity(),
									getString(R.string.cancelschedules),
									Toast.LENGTH_SHORT).show();
						}
					}
				});
			} else if (position == 2) {

				String content;
				content = (String) thisSchedules.getSchedule_subschedules();

				convertView = LayoutInflater.from(getActivity()).inflate(
						R.layout.cell_with_webview_item, null);
				WebView webview = (WebView) convertView
						.findViewById(R.id.webview);

				if (content.equals("")) {
					webview.setVisibility(View.GONE);
				}
				webview.loadDataWithBaseURL("about:blank", content,
						"text/html", "utf-8", null);
			} else if (position > 2 && position < 3 + lines) {

				if (position == 3) {

					convertView = LayoutInflater.from(getActivity()).inflate(
							R.layout.cell_line_title_item, null);
					TextView title = (TextView) convertView
							.findViewById(R.id.cell_line_textView);
					title.setText(getString(R.string.speakerspeople));
				} else {

					convertView = LayoutInflater.from(getActivity()).inflate(
							R.layout.cell_user_item, null);

					ImageView speakerimage = (ImageView) convertView
							.findViewById(R.id.speakerimage);
					TextView speakername = (TextView) convertView
							.findViewById(R.id.speakername);
					TextView speakercomplay = (TextView) convertView
							.findViewById(R.id.speakercomplay);
					TextView speakertitle = (TextView) convertView
							.findViewById(R.id.speakertitle);

					speakername.setText(listSpeakers.get(position - 4)
							.getSpeaker_name());
					speakercomplay.setText(listSpeakers.get(position - 4)
							.getSpeaker_complay());
					speakertitle.setText(listSpeakers.get(position - 4)
							.getSpeaker_title());

					BitmapFactory.Options options = new BitmapFactory.Options();
					options.inSampleSize = 1;
					Bitmap bm = BitmapFactory
							.decodeFile(CONTANT.mobilepath
									+ "/"
									+ CONTANT.totalfolder
									+ "/"
									+ CONTANT.eventIds
									+ "/"
									+ CONTANT.speakers
									+ "/"
									+ listSpeakers.get(position - 4)
											.getSpeaker_image());
					speakerimage.setImageBitmap(bm);
				}

			} else if (position == 3 + lines) {
				convertView = LayoutInflater.from(getActivity()).inflate(
						R.layout.cell_line_title_item, null);
				TextView title = (TextView) convertView
						.findViewById(R.id.cell_line_textView);
				title.setText(getString(R.string.diss_talk_it) + ":"
						+ m_aComments.length);

			} else if (position > 3 + lines
					&& position < 4 + lines + m_aComments.length) {

				Map<String, Object> map = (Map<String, Object>) m_aComments[position
						- 4 - lines];
				convertView = LayoutInflater.from(getActivity()).inflate(
						R.layout.cell_with_comment_item, null);

				ImageView speakerimage = (ImageView) convertView
						.findViewById(R.id.speakerimage);
				TextView speakername = (TextView) convertView
						.findViewById(R.id.speakername);
				TextView speakercomplay = (TextView) convertView
						.findViewById(R.id.speakercomplay);
				speakercomplay.setSingleLine(false);
				TextView speakertitle = (TextView) convertView
						.findViewById(R.id.speakertitle);

				speakername.setText((String) map.get("discussion_name"));
				speakercomplay.setText((String) map.get("discussion_content"));
				speakertitle.setText(((String) map.get("discussion_date"))
						.substring(5, 16));

				Drawable cachedImage = asyncImageLoader.loadDrawable(
						(String) map.get("discussion_photo"), speakerimage,
						new ImageCallback() {

							@Override
							public void imageLoaded(Drawable imageDrawable,
									ImageView imageView, String imageUrl) {
								// System.out.println("鏄剧ず澶村儚鍟�);
								imageView.setImageDrawable(imageDrawable);
							}
						});
				if (cachedImage == null) {
					speakerimage.setImageResource(R.drawable.no_image);
				} else {
					// System.out.println("鏄剧ず澶村儚");
					speakerimage.setImageDrawable(cachedImage);
				}
			} else if (position == 4 + lines + m_aComments.length) {

				convertView = LayoutInflater.from(getActivity()).inflate(
						R.layout.cell_schedules_talk_item, null);

				edtext = (EditText) convertView.findViewById(R.id.edit_text);
				edtext.setText(m_text_comment);
				edtext.requestFocus();
				edtext.addTextChangedListener(new TextWatcher() {

					@Override
					public void onTextChanged(CharSequence s, int start,
							int before, int count) {
						// TODO Auto-generated method stub
					}

					@Override
					public void beforeTextChanged(CharSequence s, int start,
							int count, int after) {
						// TODO Auto-generated method stub
						edtext.setFocusable(true);
					}

					@Override
					public void afterTextChanged(Editable s) {
						// TODO Auto-generated method stub
						m_text_comment = s.toString();
					}
				});

				Button send_but = (Button) convertView
						.findViewById(R.id.send_but);

				send_but.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						HTMainActivity c = (HTMainActivity) getActivity();
						if (c.isLogin()) {
							SendComments(m_text_comment);
						} else {
							onSaveBundleState(bundle);
							c.login(-1);
						}
					}
				});

			}
			return convertView;
		}

		// send cpmment
		protected void SendComments(String text) {
			// TODO Auto-generated method stub
			if (text.length() == 0) {

				AlertDialog dialog = new AlertDialog.Builder(getActivity())
						.create();
				dialog.setTitle(getString(R.string.msg_error));
				dialog.setMessage(getString(R.string.null_content));
				dialog.setButton(DialogInterface.BUTTON_NEUTRAL,
						getString(R.string.close),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								dialog.dismiss();
							}
						});
				dialog.show();

				return;
			}

			addProgressView(getActivity());
			Connector o = ((HTMainActivity) getActivity()).getConnector();
			Object[] aParams = { o.getUsername(), o.getPassword(),
					CONTANT.eventIds, String.valueOf(scheduleid), "0", text,
					ActivityUtils.getLang() };

			o.execAsyncMethod("dolphin.AddScheduleDiscussion", aParams,
					new Connector.Callback() {

						@Override
						public boolean callFailed(Exception e) {
							// TODO Auto-generated method stub
							return super.callFailed(e);
						}

						public void callFinished(Object result) {
							// Log.d("lodi", (String) result);

							if (result.toString().contains("failed")
									&& result.toString().contains("0")) {

								AlertDialog dialog = new AlertDialog.Builder(
										getActivity()).create();
								dialog.setTitle(getString(R.string.mail_error));
								dialog.setMessage(getString(R.string.mail_identity_error));
								dialog.setButton(
										DialogInterface.BUTTON_NEUTRAL,
										getString(R.string.close),
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int whichButton) {
												dialog.dismiss();
												m_text_comment = "";
												return;
											}
										});
								dialog.show();
							}
							hide();
							m_text_comment = "";
							getCommentList();
						}
					}, getActivity());
		}

		// show map
		@SuppressLint("ShowToast")
		protected void showMap() {
			// TODO Auto-generated method stub
			if (thisSchedules.getMap() == null
					&& thisSchedules.getMap().endsWith("")) {
				Toast.makeText(getActivity(), getText(R.string.no_map), 1000)
						.show();
				return;
			}

			Intent intent = new Intent();
			intent.setAction(android.content.Intent.ACTION_VIEW);
			File file = new File(CONTANT.mobilepath + "/" + CONTANT.totalfolder
					+ "/" + CONTANT.eventIds + "/" + CONTANT.schedules + "/"
					+ thisSchedules.getMap());
			intent.setDataAndType(Uri.fromFile(file), "image/*");
			startActivity(intent);
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
