package com.huatandm.meetme.fragment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.vudroid.pdfdroid.PdfViewerActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.huatandm.meetme.ActivityUtils;
import com.huatandm.meetme.CONTANT;
import com.huatandm.meetme.R;
import com.huatandm.meetme.adapter.DocsAdapter;
import com.huatandm.meetme.adapter.DocsContant;
import com.umeng.analytics.MobclickAgent;

public class DocsListFragment extends BaseListFragment {

	List<DocsContant> listDocsContants = null;

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		setTitle(R.string.docslist);
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
		Button but = getRight_bt();
		but.setVisibility(View.GONE);
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

		Uri uri = Uri.fromFile(new File(CONTANT.mobilepath + "/"
				+ CONTANT.totalfolder + "/" + CONTANT.eventIds + "/"
				+ CONTANT.docs + "/"
				+ listDocsContants.get(position).getDoc_address()));
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		intent.setClass(getActivity(), PdfViewerActivity.class);
		startActivity(intent);
	}

	@SuppressWarnings("unchecked")
	private void UIupdata() {
		// TODO Auto-generated method stub
		// 判断如果新闻的长度大于10，有更多，如果欣慰的长度小于10，没有更多
		if (datas == null) {
			return;
		}

		listDocsContants = new ArrayList<DocsContant>();
		Map<String, Object> maps = (Map<String, Object>) datas;
		Object[] menu = (Object[]) maps.get("content");

		for (int i = 0; i < menu.length; i++) {
			DocsContant docsContant = new DocsContant();
			Map<String, Object> map = (Map<String, Object>) menu[i];
			// newsContant.setNewsimage((String)map.get(""));
			docsContant.setDoc_ID((Integer) map.get("doc_ID"));
			docsContant.setDoc_type((String) map.get("doc_type"));
			docsContant.setDoc_address((String) map.get("doc_address"));
			docsContant.setDoc_title((String) map.get("doc_title"));
			docsContant.setDoc_size((String) map.get("doc_size"));
			docsContant.setDoc_author((String) map.get("doc_author"));
			listDocsContants.add(docsContant);
		}
		CONTANT.listdocs = listDocsContants;

		removeProgressView();
		setListAdapter(new DocsAdapter(getActivity(), listDocsContants));
	}

	private void initData() {
		// TODO Auto-generated method stub
		datas = ActivityUtils.parse(getActivity(), CONTANT.docs);
		h.sendEmptyMessage(0);
	}
}
