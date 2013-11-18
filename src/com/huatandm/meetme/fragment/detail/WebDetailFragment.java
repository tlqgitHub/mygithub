package com.huatandm.meetme.fragment.detail;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.huatandm.meetme.ActivityUtils;
import com.huatandm.meetme.CONTANT;
import com.huatandm.meetme.HTMainActivity;
import com.huatandm.meetme.R;
import com.huatandm.meetme.fragment.BaseListFragment;
import com.umeng.analytics.MobclickAgent;

@SuppressLint("SetJavaScriptEnabled")
public class WebDetailFragment extends BaseListFragment implements
		OnTouchListener {

	String eventIds;
	int index;
	WebView webview;
	String content;
	String path;
	String fragmentName;

	protected ProgressDialog m_dialogProgress;
	String sLoading;
	Button undo_bt;
	Button redo_bt;

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

		if (fragmentName.equals("ExhibitorsListFragment")) {
			setTitle(R.string.exhibitorscomplaydetail);
		} else if (fragmentName.equals("NewsListFragment")) {
			setTitle(R.string.newsdetail);
		} else if (fragmentName.equals("SponsorsListFragment")) {
			setTitle(R.string.sponsorsdetail);
		} else if (fragmentName.equals("InvestigateListFragment")) {
			setTitle(R.string.questionnaire);
		} else if (fragmentName.equals("FriendsListFragment")) {
			setTitle(R.string.friends_detail);
		} else if (fragmentName.equals("HomeListFragment")) {
			setTitle(R.string.info);
		} else if (fragmentName.equals("nullFragment")) {
			setTitle(R.string.forum_info);
		}

		if (fragmentName.equals("NewsListFragment")) {

			Button left_bt = getLeft_bt();
			left_bt.setBackgroundResource(R.drawable.back);
			left_bt.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					HTMainActivity fca = (HTMainActivity) getActivity();
					fca.back();
				}
			});

			undo_bt = getUn_bt_n();
			redo_bt = getRe_bt_n();
			undo_bt.setVisibility(View.VISIBLE);
			redo_bt.setVisibility(View.VISIBLE);

			updataButtonStatus();

			undo_bt.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					preview();
					updataButtonStatus();
				}
			});

			redo_bt.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					next();
					updataButtonStatus();
				}
			});

		} else if (fragmentName.equals("FriendsListFragment")) {

			getRight_view().setVisibility(View.VISIBLE);
			Button right_bt = getRight_bt();
			right_bt.setVisibility(View.VISIBLE);
			right_bt.setBackgroundResource(R.drawable.send);
			right_bt.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					onSaveBundleState(bundle);
					onSendMsg();
				}
			});

		}
		MobclickAgent.onPageStart(this.getClass().getName());
	}

	protected void onSendMsg() {
		// TODO Auto-generated method stub
		Bundle b = new Bundle();
		b.putString("fragment", "WebDetailFragment");
		b.putString("recipient", bundle.getString("recipient"));
		b.putString("account", bundle.getString("account"));
		mListener.onArticleSelected(b);
		HTMainActivity fca = (HTMainActivity) getActivity();
		fca.switchContent(new SendMsgListFragment(), 2);
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

		index = bundle.getInt("index", 0);
		fragmentName = bundle.getString("fragment");
		sLoading = getActivity().getResources().getString(R.string.loading);

		getListView().setDividerHeight(0);
		initData();
	}

	class WebPageViewClient extends WebViewClient {

		Context context;

		public WebPageViewClient(Context context) {
			this.context = context;
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			// TODO Auto-generated method stub
			return super.shouldOverrideUrlLoading(view, url);
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			// TODO Auto-generated method stub
			super.onPageFinished(view, url);
			removeProgressView();
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			// TODO Auto-generated method stub
			super.onPageStarted(view, url, favicon);
			addProgressView();
		}
	}

	class WebPageChromeClient extends WebChromeClient {
		Context context;

		public WebPageChromeClient(Context context) {
			this.context = context;
		}

		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			// TODO Auto-generated method stub
			super.onProgressChanged(view, newProgress);
		}
	}

	public void addProgressView() {

		if (null != m_dialogProgress && m_dialogProgress.isShowing()) {
			return;
		} else {
			m_dialogProgress = ProgressDialog.show(getActivity(), "", sLoading,
					true, false);
			m_dialogProgress.show();
		}
	}

	public void removeProgressView() {
		if (m_dialogProgress != null) {
			if (m_dialogProgress.isShowing()) {
				m_dialogProgress.dismiss();
			}
		}
	}

	public class MyAdapter extends BaseAdapter {

		Context context;

		public MyAdapter(Context context) {
			this.context = context;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 1;
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

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			if (convertView == null)
				convertView = LayoutInflater.from(context).inflate(
						R.layout.webview, null);

			webview = (WebView) convertView.findViewById(R.id.webview);
			webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_INSET);
			webview.setHorizontalScrollBarEnabled(false);
			webview.setVerticalScrollBarEnabled(false);
			webview.getSettings().setJavaScriptEnabled(true);
			webview.setWebViewClient(new WebPageViewClient(getActivity()));
			webview.setWebChromeClient(new WebPageChromeClient(getActivity()));
			webview.setLongClickable(true);

			if (fragmentName.equals("ExhibitorsListFragment")) {
				content = String.valueOf(CONTANT.listexhibitors.get(index)
						.getId()) + "_" + "android.html";
				path = "file://" + "/" + CONTANT.mobilepath + "/"
						+ CONTANT.totalfolder + "/" + CONTANT.eventIds + "/"
						+ CONTANT.exhibitors + "/" + CONTANT.info + "/"
						+ content;
				webview.loadUrl(path);
			} else if (fragmentName.equals("NewsListFragment")) {
				content = CONTANT.listnews.get(index).getNewscontent();
				path = "file://" + "/" + CONTANT.mobilepath + "/"
						+ CONTANT.totalfolder + "/" + CONTANT.eventIds + "/"
						+ CONTANT.news + "/" + content;
				webview.loadUrl(path);
			} else if (fragmentName.equals("SponsorsListFragment")) {
				content = String.valueOf(CONTANT.listsponsors.get(index)
						.getId())
						+ "_"
						+ "android_"
						+ ActivityUtils.getLang()
						+ ".html";
				path = "file://" + "/" + CONTANT.mobilepath + "/"
						+ CONTANT.totalfolder + "/" + CONTANT.eventIds + "/"
						+ CONTANT.sponsors + "/" + CONTANT.info + "/" + content;
				webview.loadUrl(path);
			} else if (fragmentName.equals("InvestigateListFragment")) {
				content = String.valueOf(CONTANT.listinvestigate.get(index)
						.getSurvey_url());
				path = content + CONTANT.member_id;
				webview.loadUrl(path);
			} else if (fragmentName.equals("FriendsListFragment")) {
				path = CONTANT.web_info_url + "/userinfo/userinfo.php?nickname="
						+ bundle.getString("username") + "&lang ="
						+ ActivityUtils.getLang();
				webview.loadUrl(path);
			} else if (fragmentName.equals("HomeListFragment")) {
				path = CONTANT.web_info_url + "/userinfo/userinfo.php?nickname="
						+ bundle.getString("username") + "&lang ="
						+ ActivityUtils.getLang();
				webview.loadUrl(path);
			} else {
				webview.loadDataWithBaseURL("about:blank",
						CONTANT.event_description, "text/html", "utf-8", null);
			}

			return convertView;
		}
	}

	public void initData() {
		// TODO Auto-generated method stub
		getListView().setAdapter(new MyAdapter(getActivity()));
	}

	private void updataButtonStatus() {
		// TODO Auto-generated method stub
		if (index == 0) {
			undo_bt.setBackgroundResource(R.drawable.icon_left_none);
		} else {
			undo_bt.setBackgroundResource(R.drawable.icon_left);
		}

		if (index == CONTANT.listnews.size() - 1) {
			redo_bt.setBackgroundResource(R.drawable.icon_right_none);
		} else {
			redo_bt.setBackgroundResource(R.drawable.icon_right);
		}
	}

	public void next() {
		if (index < CONTANT.listnews.size() - 1) {
			index++;
			content = CONTANT.listnews.get(index).getNewscontent();
			String path = "/" + CONTANT.mobilepath + "/" + CONTANT.totalfolder
					+ "/" + CONTANT.eventIds + "/" + CONTANT.news + "/"
					+ content;
			webview.loadUrl("file://" + path);
		} else {
			Toast.makeText(getActivity(), getString(R.string.lastpage),
					Toast.LENGTH_SHORT).show();
		}
	}

	public void preview() {
		if (index > 0) {
			index--;
			content = CONTANT.listnews.get(index).getNewscontent();
			String path = "/" + CONTANT.mobilepath + "/" + CONTANT.totalfolder
					+ "/" + CONTANT.eventIds + "/" + CONTANT.news + "/"
					+ content;
			webview.loadUrl("file://" + path);
		} else {
			Toast.makeText(getActivity(), getString(R.string.firstpage),
					Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		return false;
	}
}
