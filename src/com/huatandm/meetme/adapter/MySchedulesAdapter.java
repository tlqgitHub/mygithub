package com.huatandm.meetme.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huatandm.meetme.R;

public class MySchedulesAdapter extends BaseAdapter {
	Context context;
	List<SchedulesContant> listSchedules;

	public MySchedulesAdapter(Context context,
			List<SchedulesContant> listSchedules) {
		this.context = context;
		this.listSchedules = listSchedules;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listSchedules.size();
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
					R.layout.cell_schedules_item, null);

		TextView time = (TextView) convertView.findViewById(R.id.s_time);
		TextView title = (TextView) convertView.findViewById(R.id.s_title);
		TextView map = (TextView) convertView.findViewById(R.id.s_map);
		TextView room = (TextView) convertView.findViewById(R.id.s_title0);

		time.setText(listSchedules.get(position).getSchedulestime().split(" ")[0]);
		title.setText(listSchedules.get(position).getSchedulestopic());
		map.setText(listSchedules.get(position).getSchedulesroom());
		room.setText(listSchedules.get(position).getSchedulesplace());
		return convertView;
	}
}
