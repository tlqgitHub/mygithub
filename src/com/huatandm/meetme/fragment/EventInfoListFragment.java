package com.huatandm.meetme.fragment;

import java.util.List;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ListView;

import com.huatandm.meetme.HTMainActivity;
import com.huatandm.meetme.R;
import com.huatandm.meetme.adapter.SponsorsContant;
import com.huatandm.meetme.fragment.detail.WebDetailFragment;
import com.umeng.analytics.MobclickAgent;

public class EventInfoListFragment extends BaseListFragment {

	List<SponsorsContant> listSponsors = null;

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		Bundle b = new Bundle();
		b.putInt("index", 0);
		b.putString("fragment", "nullFragment");
		mListener.onArticleSelected(b);
		((HTMainActivity) getActivity()).switchContent(new WebDetailFragment(),
				2);
	}

	public void beginthread() {
		new Thread() {
			public void run() {
				initData();
			}
		}.start();
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(this.getClass().getName());
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setTitle(R.string.sponsorslist);
		MobclickAgent.onPageStart(this.getClass().getName());
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

	@Override
	public void onListItemClick(ListView lv, View v, int position, long id) {

	}

	private void UIupdata() {
		// TODO Auto-generated method stub
		removeProgressView();
	}

	private void initData() {
		// TODO Auto-generated method stub
		h.sendEmptyMessage(0);
	}
}
