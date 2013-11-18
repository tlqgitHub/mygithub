package com.huatandm.meetme.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huatandm.meetme.ActivityUtils;
import com.huatandm.meetme.CONTANT;
import com.huatandm.meetme.HTMainActivity;
import com.huatandm.meetme.R;
import com.huatandm.meetme.adapter.SchedulesContant;
import com.huatandm.meetme.fragment.detail.SchedulesDetailFragment;
import com.umeng.analytics.MobclickAgent;

public class SchedulesListFragment extends BaseListFragment {

	List<List<SchedulesContant>> vector = new ArrayList<List<SchedulesContant>>();
	List<List<SchedulesContant>> vector0;
	List<SchedulesContant> listSchedules = null;

	List<SchedulesContant> this_List_Schedules = null;
	private int index = 0;
	SchedulesAdapter adapter;
	Button un_but;
	Button re_but;

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setTitle(R.string.schedule);
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

		un_but = getUn_bt_a();
		un_but.setVisibility(View.VISIBLE);
		re_but = getRe_bt_a();
		re_but.setVisibility(View.VISIBLE);

		buttontype();

		re_but.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (index < vector.size() - 1) {
					index += 1;
					reinitdata();
				}
			}
		});

		un_but.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (index > 0) {
					index -= 1;
					reinitdata();
				}
			}
		});
		MobclickAgent.onPageStart(this.getClass().getName());
	}

	//
	protected void reinitdata() {
		// TODO Auto-generated method stub
		setTitle(vector.get(index).get(0).getSchedulestime().split(" ")[0]);

		listSchedules = vector.get(index);
		vector0 = new ArrayList<List<SchedulesContant>>();
		vector0 = getSampleSchedulesContantWithSchedulesshowtime(listSchedules);
		for (int i = 0; i < vector0.size(); i++) {
			if (vector0.get(i).size() > 1) {
				for (int j = 0; j < vector0.get(i).size(); j++) {
					vector0.get(i).get(j).setIsShow(false);
					vector0.get(i).get(j).setIsShowTime(false);
				}
			}
		}
		buttontype();
		adapter.notifyDataSetChanged();
	}

	// undo redo button show type
	private void buttontype() {
		// TODO Auto-generated method stub
		if (index == 0) {
			un_but.setBackgroundResource(R.drawable.icon_left_none);
		} else {
			un_but.setBackgroundResource(R.drawable.icon_left);
		}

		if (vector.size()==0) {
			re_but.setBackgroundResource(R.drawable.icon_right_none);

			return;
		}

		if (index == vector.size() - 1) {
			re_but.setBackgroundResource(R.drawable.icon_right_none);
		} else {
			re_but.setBackgroundResource(R.drawable.icon_right);
		}
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

		if (vector0.get(position).size() == 1) {
			Bundle b = new Bundle();// b.putString("object",object.toString());
			b.putString("fragment", "SchedulesListFragment");
			b.putInt("index", position);
			b.putInt("schedule_id", vector0.get(position).get(0)
					.getSchedulesid());
			b.putString("eventIds", CONTANT.eventIds);
			b.putInt("state", 0);
			mListener.onArticleSelected(b);

			((HTMainActivity) getActivity()).switchContent(
					new SchedulesDetailFragment(), 2);
		} else {
			dataAction(position);
		}
	}

	// 合并与收缩
	private void dataAction(int position) {
		// TODO Auto-generated method stub
		if (((SchedulesContant) vector0.get(position).get(0)).getIsShow()) {

			for (int i = vector0.get(position).size() - 1; i >= 0; i--) {
				vector0.remove(position + 1);
				vector0.get(position).get(i).setIsShow(false);
			}

		} else {

			for (int i = vector0.get(position).size() - 1; i >= 0; i--) {
				List<SchedulesContant> v = new ArrayList<SchedulesContant>();
				v.add(vector0.get(position).get(i));
				vector0.get(position).get(i).setIsShow(true);
				vector0.add(position + 1, v);
			}
		}

		adapter.notifyDataSetChanged();
	}

	@SuppressWarnings("unchecked")
	private void UIupdata() {
		// TODO Auto-generated method stub
		// 判断如果新闻的长度大于10，有更多，如果欣慰的长度小于10，没有更多
		if (datas == null) {
			return;
		}

		listSchedules = new ArrayList<SchedulesContant>();
		Map<String, Object> maps = (Map<String, Object>) datas;
		Object[] menu = (Object[]) maps.get("content");
		for (int i = 0; i < menu.length; i++) {
			SchedulesContant schedulesContant = new SchedulesContant();
			Map<String, Object> map = (Map<String, Object>) menu[i];
			schedulesContant.setSchedulesdate(((String) map
					.get("schedule_starttime")).split(" ")[0]);
			schedulesContant.setSchedulesshowtime(((String) map
					.get("schedule_starttime")).split(" ")[1].substring(0, 5)
					+ " - "
					+ ((String) map.get("schedule_endtime")).split(" ")[1]
							.substring(0, 5));
			schedulesContant.setSchedulestime((String) map
					.get("schedule_starttime"));
			schedulesContant
			.setSchedulestopic((String) ((Map<String, Object>) map
					.get("schedule_topic")).get(ActivityUtils.getLang()));
			schedulesContant
			.setSchedulesroom((String) ((Map<String, Object>) map
					.get("schedule_room")).get(ActivityUtils.getLang()));
			schedulesContant
			.setSchedulesplace((String) ((Map<String, Object>) map
					.get("schedule_place")).get(ActivityUtils.getLang()));
			schedulesContant.setSchedulesspeakers(map.get("schedule_speakers"));
			schedulesContant
			.setSchedulescontent((String) ((Map<String, Object>) map
					.get("schedule_desc")).get(ActivityUtils.getLang()));
			schedulesContant.setSchedulesid((Integer) map.get("schedule_ID"));
			schedulesContant.setMap((String) map.get("schedule_map"));

			schedulesContant
			.setSchedule_subschedules(((Map<String, Object>) map
					.get("schedule_subschedules")).get(ActivityUtils
							.getLang()));

			listSchedules.add(schedulesContant);
		}

		Collections.sort(listSchedules);
		// 按时间对类分组
		vector = new ArrayList<List<SchedulesContant>>();

		if(listSchedules.size()==0){

			setTitle(R.string.schedule);
			buttontype();
			removeProgressView();
		}else{
			vector = getSampleSchedulesContantWithSchedulestime(listSchedules);
			listSchedules = vector.get(index);

			vector0 = new ArrayList<List<SchedulesContant>>();
			vector0 = getSampleSchedulesContantWithSchedulesshowtime(listSchedules);

			for (int i = 0; i < vector0.size(); i++) {
				if (vector0.get(i).size() > 1) {
					for (int j = 0; j < vector0.get(i).size(); j++) {
						vector0.get(i).get(j).setIsShow(false);
						vector0.get(i).get(j).setIsShowTime(false);
					}
				}
			}

			setTitle(vector.get(index).get(0).getSchedulesdate());
			buttontype();
			removeProgressView();

			adapter = new SchedulesAdapter(getActivity(), listSchedules);
			setListAdapter(adapter);
		}
	}

	private void initData() {
		// TODO Auto-generated method stub
		datas = ActivityUtils.parse(getActivity(), CONTANT.schedules);
		h.sendEmptyMessage(0);
	}

	// 分组--- getSchedulestime
	public List<List<SchedulesContant>> getSampleSchedulesContantWithSchedulestime(
			List<SchedulesContant> list) {

		List<SchedulesContant> sampleSchedulesContants = new ArrayList<SchedulesContant>();
		List<SchedulesContant> differentSchedulesContants = new ArrayList<SchedulesContant>();
		sampleSchedulesContants.add(list.get(0));

		for (int i = 1; i < list.size(); i++) {
			if (list.get(0).getSchedulestime().split(" ")[0].equals(list.get(i)
					.getSchedulestime().split(" ")[0])) {
				// 放入到相同的数组里面
				sampleSchedulesContants.add(list.get(i));
			} else {
				// 放入到不同的数组里面
				differentSchedulesContants.add(list.get(i));
			}
		}

		vector.add(sampleSchedulesContants);
		if (!differentSchedulesContants.isEmpty()) {
			getSampleSchedulesContantWithSchedulestime(differentSchedulesContants);
		}
		return vector;
	}

	// 分组--- getSchedulesshowtime
	public List<List<SchedulesContant>> getSampleSchedulesContantWithSchedulesshowtime(
			List<SchedulesContant> list) {
		List<SchedulesContant> sampleSchedulesContants = new ArrayList<SchedulesContant>();
		List<SchedulesContant> differentSchedulesContants = new ArrayList<SchedulesContant>();
		sampleSchedulesContants.add(list.get(0));

		for (int i = 1; i < list.size(); i++) {
			if (list.get(0).getSchedulesshowtime().split(" ")[0].equals(list
					.get(i).getSchedulesshowtime().split(" ")[0])) {
				// 放入到相同的数组里面
				sampleSchedulesContants.add(list.get(i));

			} else {
				// 放入到不同的数组里面
				differentSchedulesContants.add(list.get(i));
			}

		}
		vector0.add(sampleSchedulesContants);
		if (!differentSchedulesContants.isEmpty()) {
			getSampleSchedulesContantWithSchedulesshowtime(differentSchedulesContants);
		}
		return vector0;
	}

	public class SchedulesAdapter extends BaseAdapter {

		Context context;
		List<SchedulesContant> listSchedules;

		public SchedulesAdapter(Context context,
				List<SchedulesContant> listSchedules) {
			this.context = context;
			this.listSchedules = listSchedules;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return vector0.size();
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
			if (vector0.get(position).size() > 1) {
				convertView = LayoutInflater.from(context).inflate(
						R.layout.cell_schedules_line_item, null);

				ImageView imageview = (ImageView) convertView
						.findViewById(R.id.s_icon);
				TextView time = (TextView) convertView
						.findViewById(R.id.s_time);
				TextView title = (TextView) convertView
						.findViewById(R.id.s_title);
				TextView num = (TextView) convertView.findViewById(R.id.s_num);

				if (vector0.get(position).get(0).getIsShow()) {
					// imageview.setBackground(null);
					imageview
					.setImageResource(R.drawable.agenda_section_arrow_down);
				} else {
					// imageview.setBackground(null);
					imageview
					.setImageResource(R.drawable.agenda_section_arrow_right);
				}

				time.setText(vector0.get(position).get(0)
						.getSchedulesshowtime());
				title.setText(getString(R.string.schedule_line_title));
				num.setText(String.valueOf(vector0.get(position).size()));
			} else {
				convertView = LayoutInflater.from(context).inflate(
						R.layout.cell_schedules_item, null);

				TextView time = (TextView) convertView
						.findViewById(R.id.s_time);
				TextView title = (TextView) convertView
						.findViewById(R.id.s_title);
				TextView room = (TextView) convertView.findViewById(R.id.s_map);
				TextView place = (TextView) convertView
						.findViewById(R.id.s_title0);

				time.setText(vector0.get(position).get(0)
						.getSchedulesshowtime());
				title.setText(vector0.get(position).get(0).getSchedulestopic());
				room.setText(vector0.get(position).get(0).getSchedulesroom());
				place.setText(vector0.get(position).get(0).getSchedulesplace());

				if (!vector0.get(position).get(0).getIsShowTime()) {
					time.setVisibility(View.GONE);

					RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
							RelativeLayout.LayoutParams.WRAP_CONTENT,
							RelativeLayout.LayoutParams.WRAP_CONTENT);
					layoutParams.addRule(RelativeLayout.CENTER_VERTICAL,
							RelativeLayout.TRUE);
					layoutParams.setMargins(20, 0, 0, 0);
					room.setLayoutParams(layoutParams);
				}
			}

			return convertView;
		}
	}
}
