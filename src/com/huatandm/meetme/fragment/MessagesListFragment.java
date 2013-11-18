package com.huatandm.meetme.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.huatandm.meetme.HTMainActivity;
import com.huatandm.meetme.R;
import com.huatandm.meetme.adapter.MessageAdapter;
import com.huatandm.meetme.fragment.detail.MessageDetailFragment;
import com.huatandm.meetme.helper.Connector;
import com.umeng.analytics.MobclickAgent;

public class MessagesListFragment extends BaseListFragment {

	List<Map<String, Object>> messagelist = null;
	Object m_aMessages[];
	boolean inbox;

	private Button ch1;
	private Button ch2;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.msg_list, null);
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

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		if (inbox) {
			getActivity().setTitle(R.string.theinbox);
		} else {
			getActivity().setTitle(R.string.thesent);
		}

		ch1 = (Button) getActivity().findViewById(R.id.chioce1);
		ch2 = (Button) getActivity().findViewById(R.id.chioce2);
		ch1.setText(R.string.theinbox);
		ch2.setText(R.string.thesent);

		ch1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setChoice(0);
			}
		});

		ch2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setChoice(1);
			}
		});

		setChoice(0);
	}

	private void setChoice(int i) {
		// TODO Auto-generated method stub

		if (i == 0) {
			ch1.setBackgroundResource(R.drawable.button_hover_bg);
			;
			ch2.setBackgroundResource(R.drawable.button_bg_right);
			inbox = true;
			setTitle(R.string.theinbox);
		} else {
			ch2.setBackgroundResource(R.drawable.button_hover_right_bg);
			ch1.setBackgroundResource(R.drawable.button_bg);
			inbox = false;
			setTitle(R.string.thesent);
		}
		initData();
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
			default:
				break;
			}

			super.handleMessage(msg);
		}
	};

	@SuppressWarnings("unchecked")
	@SuppressLint("UseValueOf")
	@Override
	public void onListItemClick(ListView lv, View v, int position, long id) {

		Map<String, String> mapMessage = (Map<String, String>) m_aMessages[position];
		Bundle b = new Bundle();
		b.putBoolean("inbox", inbox);
		b.putInt("msg_id", new Integer(mapMessage.get("ID")));
		mListener.onArticleSelected(b);
		((HTMainActivity) getActivity()).switchContent(
				new MessageDetailFragment(), 2);
	}

	@SuppressWarnings("unchecked")
	private void UIupdata() {
		// TODO Auto-generated method stub
		messagelist = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < m_aMessages.length; i++) {
			Map<String, Object> map = (Map<String, Object>) m_aMessages[i];
			messagelist.add(map);
		}
		removeProgressView();
		setListAdapter(new MessageAdapter(getActivity(), inbox, messagelist));
	}

	public void initData() {
		// TODO Auto-generated method stub
		addProgressView(getActivity());
		Connector o = ((HTMainActivity) getActivity()).getConnector();
		Object[] aParams = { o.getUsername(), o.getPassword() };

		System.out.println(o.getUsername() + "," + o.getPassword());

		o.execAsyncMethod("dolphin."
				+ (inbox ? "getMessagesInbox" : "getMessagesSent"), aParams,
				new Connector.Callback() {

					@Override
					public boolean callFailed(Exception e) {
						// TODO Auto-generated method stub
						removeProgressView();
						return super.callFailed(e);
					}

					public void callFinished(Object result) {
						m_aMessages = null;
						m_aMessages = (Object[]) result;
						h.sendEmptyMessage(0);
					}
				}, getActivity());

	}
}
