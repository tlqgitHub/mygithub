package com.huatandm.meetme.fragment.detail;

import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
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

import com.huatandm.meetme.HTMainActivity;
import com.huatandm.meetme.R;
import com.huatandm.meetme.fragment.BaseListFragment;
import com.huatandm.meetme.helper.AsyncImageLoader;
import com.huatandm.meetme.helper.AsyncImageLoader.ImageCallback;
import com.huatandm.meetme.helper.Connector;
import com.umeng.analytics.MobclickAgent;

@SuppressLint("SetJavaScriptEnabled")
public class MessageDetailFragment extends BaseListFragment {

	Map<String, String> m_Messages = null;
	private String m_sRecipient;
	private boolean isInbox;
	private int m_iMsgId;

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

		Button right_bt = getRight_bt();
		right_bt.setBackgroundResource(R.drawable.addcomment);
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

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		if (null != ((HTMainActivity) getActivity()).getOutState().getBundle(
				this.getClass().getName())) {
			bundle = ((HTMainActivity) getActivity()).getOutState().getBundle(
					this.getClass().getName());
			((HTMainActivity) getActivity()).getOutState().putBundle(
					this.getClass().getName(), null);
		}

		isInbox = bundle.getBoolean("inbox", true);
		m_iMsgId = bundle.getInt("msg_id", 0);

		if (isInbox) {
			getActivity().setTitle(R.string.messagedetailfrom);
		} else {
			getActivity().setTitle(R.string.messagedetailto);
		}

		if (isInbox) {
			setTitle(R.string.messagedetailfrom);
		} else {
			setTitle(R.string.messagedetailto);
		}

		getListView().setDividerHeight(0);
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
		setM_sRecipient(m_Messages.get("Nick"));
		removeProgressView();
		setListAdapter(new MyAdapter(getActivity(), isInbox, m_Messages));
	}

	public void initData() {
		addProgressView(getActivity());
		Connector o = ((HTMainActivity) getActivity()).getConnector();
		Object[] aParams = { o.getUsername(), o.getPassword(),
				String.valueOf(m_iMsgId) };
		o.execAsyncMethod("dolphin."
				+ (isInbox ? "getMessageInbox" : "getMessageSent"), aParams,
				new Connector.Callback() {
					@SuppressWarnings("unchecked")
					@Override
					public void callFinished(Object result) {
						// TODO Auto-generated method stub
						m_Messages = (Map<String, String>) result;
						h.sendEmptyMessage(0);
						super.callFinished(result);
					}

					@Override
					public boolean callFailed(Exception e) {
						// TODO Auto-generated method stub
						Log.d("e", e.getMessage());
						removeProgressView();
						return super.callFailed(e);
					}
				}, getActivity());
	}

	protected void onSendMsg() {
		// TODO Auto-generated method stub
		Bundle b = new Bundle();
		b.putString("fragment", "MessageDetailFragment");
		b.putString("recipient", m_Messages.get("Nick"));
		b.putString("account", m_Messages.get("Nick"));

		mListener.onArticleSelected(b);
		HTMainActivity fca = (HTMainActivity) getActivity();
		fca.switchContent(new SendMsgListFragment(), 2);
	}

	public String getM_sRecipient() {
		return m_sRecipient;
	}

	public void setM_sRecipient(String m_sRecipient) {
		this.m_sRecipient = m_sRecipient;
	}

	class MyAdapter extends BaseAdapter {
		AsyncImageLoader asyncImageLoader = new AsyncImageLoader();

		Context context;
		Map<String, String> map;
		boolean isbox;
		private String txt;

		public MyAdapter(Context context, boolean isbox, Map<String, String> map) {
			this.context = context;
			this.map = map;
			this.isbox = isbox;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 2;
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

			if (arg0 == 0) {
				convertView = LayoutInflater.from(context).inflate(
						R.layout.cell_with_head_item, null);
				ImageView speakerimage = (ImageView) convertView
						.findViewById(R.id.speakerimage);

				// TextView nameindex = (TextView) convertView
				// .findViewById(R.id.nameindex);
				TextView speakername = (TextView) convertView
						.findViewById(R.id.speakername);
				TextView speakercomplay = (TextView) convertView
						.findViewById(R.id.speakercomplay);
				TextView speakertitle = (TextView) convertView
						.findViewById(R.id.speakertitle);

				speakername.setText(map.get("Subject"));
				speakercomplay.setText(isbox ? "From :" + map.get("Nick")
						: "To :" + map.get("Nick"));
				speakertitle.setText(map.get("Date"));

				Drawable cachedImage = asyncImageLoader.loadDrawable(
						map.get("Thumb"), speakerimage, new ImageCallback() {

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
					speakerimage.setImageDrawable(cachedImage);
				}

			} else {

				convertView = LayoutInflater.from(context).inflate(
						R.layout.cell_msg_with_webview_item, null);

				WebView webview = (WebView) convertView
						.findViewById(R.id.webview);
				webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_INSET);
				txt = map.get("Text");
				Log.d("tet", txt);
				if (txt.contains("color:#000000")) {
					txt = txt.replaceAll("color:#000000", "color:#f5f5f5");
				}
				webview.loadDataWithBaseURL(
						"null",
						"<html><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"><head><style type='text/css'>body {color:#2d2d2d; background-color:#ffffff;font-family:Î¢ÈíÑÅºÚ; font-size:14px;}</style></head><body>"
								+ txt + "</body></html>", "text/html", "utf-8",
						null);
			}

			return convertView;
		}
	}

}
