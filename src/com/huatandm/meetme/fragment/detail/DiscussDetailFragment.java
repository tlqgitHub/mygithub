package com.huatandm.meetme.fragment.detail;

import java.util.Map;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.huatandm.meetme.ActivityUtils;
import com.huatandm.meetme.CONTANT;
import com.huatandm.meetme.HTMainActivity;
import com.huatandm.meetme.R;
import com.huatandm.meetme.fragment.BaseListFragment;
import com.huatandm.meetme.helper.AsyncImageLoader;
import com.huatandm.meetme.helper.AsyncImageLoader.ImageCallback;
import com.huatandm.meetme.helper.Connector;
import com.umeng.analytics.MobclickAgent;

public class DiscussDetailFragment extends BaseListFragment {

	private Object[] m_disData;

	private int discussIds;
	private String photo, name, content, date;

	public void onSaveBundleState(Bundle outState) {
		// TODO Auto-generated method stub
		((HTMainActivity) getActivity()).getOutState().putBundle(
				this.getClass().getName(), outState);
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(this.getClass().getName());
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		getRight_view().setVisibility(View.VISIBLE);

		Button right_bt = getRight_bt();
		right_bt.setBackgroundResource(R.drawable.send);
		right_bt.setVisibility(View.VISIBLE);
		right_bt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onSaveBundleState(bundle);
				onSendMsg();
			}
		});
		MobclickAgent.onPageStart(this.getClass().getName());
	}

	public void onSendMsg() {
		// TODO Auto-generated method stub
		Bundle b = new Bundle();
		b.putString("fragment", "DiscussListFragment");
		b.putString("discussIds", String.valueOf(discussIds));
		mListener.onArticleSelected(b);
		HTMainActivity fca = (HTMainActivity) getActivity();
		fca.switchContent(new SendMsgListFragment(), 2);
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		setTitle(R.string.discussdetail);
		if (null != ((HTMainActivity) getActivity()).getOutState().getBundle(
				this.getClass().getName())) {
			bundle = ((HTMainActivity) getActivity()).getOutState().getBundle(
					this.getClass().getName());
			((HTMainActivity) getActivity()).getOutState().putBundle(
					this.getClass().getName(), null);
		}

		discussIds = bundle.getInt("discussIds");
		photo = bundle.getString("photo");
		name = bundle.getString("name");
		content = bundle.getString("content");
		date = bundle.getString("date");

		getListView().setDividerHeight(0);

		setTitle(R.string.discussdetail);
		initData();
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
		getListView().setAdapter(new MyAdapter());
	}

	public void initData() {

		Connector o = ((HTMainActivity) getActivity()).getConnector();
		Object[] aParams = { o.getUsername(), o.getPassword(),
				CONTANT.eventIds, discussIds + "", ActivityUtils.getLang() };
		o.execAsyncMethod("dolphin.DiscussionList", aParams,
				new Connector.Callback() {
					public void callFinished(Object result) {
						Log.d(" OOO ", "dolphin.getDiscusssInbox result: "
								+ result.toString());
						m_disData = (Object[]) result;
						h.sendEmptyMessage(0);
					}
				}, getActivity());
	}

	class MyAdapter extends BaseAdapter {

		AsyncImageLoader asyncImageLoader = new AsyncImageLoader();

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 3 + m_disData.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int getViewTypeCount() {
			// TODO Auto-generated method stub
			return 4;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@SuppressWarnings("unchecked")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			if (position == 0) {
				convertView = LayoutInflater.from(getActivity()).inflate(
						R.layout.cell_user_name_item, null);
				ImageView speakerimage = (ImageView) convertView
						.findViewById(R.id.speakerimage);

				TextView speakername = (TextView) convertView
						.findViewById(R.id.speakername);

				speakername.setText(name);

				Drawable cachedImage = asyncImageLoader.loadDrawable(photo,
						speakerimage, new ImageCallback() {
							@Override
							public void imageLoaded(Drawable imageDrawable,
									ImageView imageView, String imageUrl) {
								imageView.setImageDrawable(imageDrawable);
							}
						});

				if (cachedImage == null) {
					speakerimage.setImageResource(R.drawable.no_image);
				} else {
					speakerimage.setImageDrawable(cachedImage);
				}

			} else if (position == 1) {
				convertView = LayoutInflater.from(getActivity()).inflate(
						R.layout.cell_with_webview_item, null);

				WebView webview = (WebView) convertView
						.findViewById(R.id.webview);
				webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_INSET);
				webview.loadDataWithBaseURL(
						"null",
						"<html><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">"
								+ "<head><style type='text/css'>"
								+ "body {background-color:#ffffff;font-family:Î¢ÈíÑÅºÚ;}</style>"
								+ "</head>"
								+ "<body>"
								+ "<span style='color:#2d2d2d; font-size:18px;'>"
								+ content + "</span> " + "<br><br>" +

								"<span style='color:#999999; font-size:16px;'>"
								+ date + "</span> " + "</body></html>",
						"text/html", "utf-8", null);

			} else if (position == 2) {
				convertView = LayoutInflater.from(getActivity()).inflate(
						R.layout.cell_line_title_item, null);
				TextView title = (TextView) convertView
						.findViewById(R.id.cell_line_textView);
				title.setText(getString(R.string.diss_talk_it) + ":"
						+ m_disData.length);
			} else {

				Map<String, Object> map = (Map<String, Object>) m_disData[position - 3];
				convertView = LayoutInflater.from(getActivity()).inflate(
						R.layout.cell_with_head_addline_item, null);

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
				speakertitle.setText((String) map.get("discussion_date"));

				Drawable cachedImage = asyncImageLoader.loadDrawable(
						(String) map.get("discussion_photo"), speakerimage,
						new ImageCallback() {

							@Override
							public void imageLoaded(Drawable imageDrawable,
									ImageView imageView, String imageUrl) {
								// System.out.println("ÏÔÊ¾Í·Ïñ°¡");
								imageView.setImageDrawable(imageDrawable);
							}
						});
				if (cachedImage == null) {
					speakerimage.setImageResource(R.drawable.no_image);
				} else {
					// System.out.println("ÏÔÊ¾Í·Ïñ");
					speakerimage.setImageDrawable(cachedImage);
				}

			}
			return convertView;
		}
	}
}
