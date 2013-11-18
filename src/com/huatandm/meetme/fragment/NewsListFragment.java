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
import com.huatandm.meetme.adapter.NewsAdapter;
import com.huatandm.meetme.adapter.NewsContant;
import com.huatandm.meetme.fragment.detail.WebDetailFragment;
import com.umeng.analytics.MobclickAgent;

public class NewsListFragment extends BaseListFragment {

	List<NewsContant> listnews = null;

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

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
		setTitle(R.string.listnews);
		MobclickAgent.onPageStart(this.getClass().getName());
	}

	public void beginthread() {
		new Thread() {
			public void run() {
				initData();
			}
		}.start();
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
		b.putString("fragment", "NewsListFragment");
		b.putString("title", listnews.get(position).getNewsname());
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
		listnews = new ArrayList<NewsContant>();
		Map<String, Object> maps = (Map<String, Object>) datas;
		Object[] menu = (Object[]) maps.get("content");
		for (int i = 0; i < menu.length; i++) {
			NewsContant newsContant = new NewsContant();
			Map<String, Object> map = (Map<String, Object>) menu[i];
			// newsContant.setNewsimage((String)map.get(""));
			newsContant.setNewsname((String) map.get("news_title"));
			newsContant.setNewscontent((String) map.get("news_content"));
			newsContant.setNewstime((String) map.get("news_date"));
			newsContant.setNewsid((Integer) map.get("news_ID"));
			listnews.add(newsContant);
		}

		CONTANT.listnews = listnews;
		removeProgressView();
		setListAdapter(new NewsAdapter(getActivity(), listnews));
	}

	public void initData() {
		// TODO Auto-generated method stub
		datas = ActivityUtils.parse(getActivity(), CONTANT.news);
		System.out.print("datas= " + datas);
		h.sendEmptyMessage(0);
	}

}
