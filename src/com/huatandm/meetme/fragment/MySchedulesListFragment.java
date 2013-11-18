package com.huatandm.meetme.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ListView;

import com.huatandm.meetme.CONTANT;
import com.huatandm.meetme.HTMainActivity;
import com.huatandm.meetme.R;
import com.huatandm.meetme.adapter.MySchedulesAdapter;
import com.huatandm.meetme.adapter.SchedulesContant;
import com.huatandm.meetme.fragment.detail.SchedulesDetailFragment;
import com.umeng.analytics.MobclickAgent;

public class MySchedulesListFragment extends BaseListFragment {

	SharedPreferences sp;
	List<SchedulesContant> listSchedules = null;

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		setTitle(R.string.myschedule);
		sp = getActivity().getSharedPreferences("collect", 0);
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

		Bundle b = new Bundle();// b.putString("object",object.toString());
		b.putString("fragment", "MySchedulesListFragment");
		b.putInt("schedule_id", listSchedules.get(position).getSchedulesid());
		mListener.onArticleSelected(b);
		((HTMainActivity) getActivity()).switchContent(
				new SchedulesDetailFragment(), 2);
	}

	private void UIupdata() {
		// TODO Auto-generated method stub

		listSchedules = new ArrayList<SchedulesContant>();

		for (int i = 0; i < CONTANT.listschedules.size(); i++) {
			SchedulesContant schedulesContant = CONTANT.listschedules.get(i);

			if (sp.getInt(schedulesContant.getSchedulesid() + "", 0) != schedulesContant
					.getSchedulesid()) {
				continue;
			}

			listSchedules.add(schedulesContant);
		}

		Collections.sort(listSchedules);
		removeProgressView();
		setListAdapter(new MySchedulesAdapter(getActivity(), listSchedules));
	}

	private void initData() {
		// TODO Auto-generated method stub
		h.sendEmptyMessage(0);
	}

}
