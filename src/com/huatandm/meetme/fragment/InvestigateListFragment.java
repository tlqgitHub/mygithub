package com.huatandm.meetme.fragment;

import java.util.ArrayList;
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
import com.huatandm.meetme.adapter.InvestigateAdapter;
import com.huatandm.meetme.adapter.InvestigateContant;
import com.huatandm.meetme.fragment.detail.WebDetailFragment;
import com.huatandm.meetme.helper.Connector;
import com.umeng.analytics.MobclickAgent;

public class InvestigateListFragment extends BaseListFragment {

	List<InvestigateContant> listInvestigate = null;

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		initData();
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(this.getClass().getName());
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setTitle(R.string.surveylist);
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
		b.putString("fragment", "InvestigateListFragment");
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
		listInvestigate = new ArrayList<InvestigateContant>();
		Object[] m_aFriends;
		m_aFriends = (Object[]) datas;
		for (int i = 0; i < m_aFriends.length; i++) {
			InvestigateContant investigateContant = new InvestigateContant();
			Map<String, Object> map = (Map<String, Object>) m_aFriends[i];
			System.out.println((String) map.get("survey_url"));
			investigateContant.setSurvey_id((Integer) map.get("survey_id"));
			investigateContant
					.setSurvey_title((String) map.get("survey_title"));
			investigateContant.setSurvey_url((String) map.get("survey_url"));
			listInvestigate.add(investigateContant);
		}
		CONTANT.listinvestigate = listInvestigate;
		removeProgressView();
		setListAdapter(new InvestigateAdapter(getActivity(), listInvestigate));
	}

	public void initData() {
		// TODO Auto-generated method stub
		addProgressView(getActivity());
		Connector o = ((HTMainActivity) getActivity()).getConnector();
		Object params[] = { o.getUsername(), o.getPassword(), CONTANT.eventIds,
				ActivityUtils.getLang() };
		o.execAsyncMethod("dolphin.SurveyList", params,
				new Connector.Callback() {

					@Override
					public void callFinished(Object result) {
						// TODO Auto-generated method stub

						datas = result;
						h.sendEmptyMessage(0);
						super.callFinished(result);
					}

					@Override
					public boolean callFailed(Exception e) {
						// TODO Auto-generated method stub
						removeProgressView();
						return super.callFailed(e);
					}

				}, getActivity());
	}

}
