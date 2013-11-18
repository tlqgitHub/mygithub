package com.huatandm.meetme.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ListView;

import com.huatandm.meetme.ActivityUtils;
import com.huatandm.meetme.CONTANT;
import com.huatandm.meetme.HTMainActivity;
import com.huatandm.meetme.R;
import com.huatandm.meetme.adapter.SponsorsAdapter;
import com.huatandm.meetme.adapter.SponsorsContant;
import com.huatandm.meetme.fragment.detail.WebDetailFragment;
import com.umeng.analytics.MobclickAgent;

public class SponsorsListFragment extends BaseListFragment {

	List<SponsorsContant> listSponsors = null;

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		addProgressView(getActivity());
		beginthread();
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

		Bundle b = new Bundle();
		b.putInt("index", position);
		b.putString("fragment", "SponsorsListFragment");
		mListener.onArticleSelected(b);

		((HTMainActivity) getActivity()).switchContent(new WebDetailFragment(),
				2);
	}

	@SuppressWarnings("unchecked")
	private void UIupdata() {
		// TODO Auto-generated method stub
		// 判断如果新闻的长度大于10，有更多，如果欣慰的长度小于10，没有更多
		if (datas == null) {
			return;
		}
		listSponsors = new ArrayList<SponsorsContant>();
		Map<String, Object> maps = (Map<String, Object>) datas;
		Object[] menu = (Object[]) maps.get("content");
		for (int i = 0; i < menu.length; i++) {
			SponsorsContant schedulesContant = new SponsorsContant();
			Map<String, Object> map = (Map<String, Object>) menu[i];
			// newsContant.setNewsimage((String)map.get(""));

			schedulesContant.setLogo((String) map.get("sponsor_logo"));
			schedulesContant.setId((Integer) map.get("id"));
			listSponsors.add(schedulesContant);
		}

		CONTANT.listsponsors = listSponsors;
		removeProgressView();
		setListAdapter(new SponsorsAdapter(getActivity(), listSponsors));
	}

	private void initData() {
		// TODO Auto-generated method stub
		datas = ActivityUtils.parse(getActivity(), CONTANT.sponsors);
		System.out.print("datas= " + datas);
		h.sendEmptyMessage(0);
	}

}
