package com.huatandm.meetme.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ListView;

import com.huatandm.meetme.ActivityUtils;
import com.huatandm.meetme.CONTANT;
import com.huatandm.meetme.HTMainActivity;
import com.huatandm.meetme.R;
import com.huatandm.meetme.adapter.ExhibitorsAdapter;
import com.huatandm.meetme.adapter.ExhibitorsContant;
import com.huatandm.meetme.fragment.detail.WebDetailFragment;
import com.umeng.analytics.MobclickAgent;

public class ExhibitorsListFragment extends BaseListFragment {

	List<ExhibitorsContant> listexhibitors = null;

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setTitle(R.string.exhibitorscomplay);
		addProgressView(getActivity());
		beginthread();
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
		b.putString("fragment", "ExhibitorsListFragment");
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
		listexhibitors = new ArrayList<ExhibitorsContant>();
		Map<String, Object> maps = (Map<String, Object>) datas;
		// System.out.println("内容是   "+map.get("content"));
		Object[] menu = (Object[]) maps.get("content");

		for (int i = 0; i < menu.length; i++) {
			ExhibitorsContant exhibitorsContant = new ExhibitorsContant();
			Map<String, Object> map = (Map<String, Object>) menu[i];
			String name = (String) map.get("exhibitor_name");
			String number = (String) map.get("exhibitor_exhibition_number");
			int id = (Integer) map.get("id");
			exhibitorsContant.setId(id);
			exhibitorsContant.setName(ActivityUtils.getPinYinHeadChar(name)
					+ "/" + name);
			exhibitorsContant.setNumber(number);
			listexhibitors.add(exhibitorsContant);
		}
		Collections.sort(listexhibitors);
		CONTANT.listexhibitors = listexhibitors;
		removeProgressView();
		setListAdapter(new ExhibitorsAdapter(getActivity(), listexhibitors));
	}

	private void initData() {
		// TODO Auto-generated method stub
		datas = ActivityUtils.parse(getActivity(), CONTANT.exhibitors);
		System.out.print("datas= " + datas);
		h.sendEmptyMessage(0);
	}

}
