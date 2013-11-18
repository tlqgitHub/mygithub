package com.huatandm.meetme.adapter;

import java.util.List;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huatandm.meetme.R;

public class ExhibitorsAdapter extends BaseAdapter {

	Context context;
	List<ExhibitorsContant> listExhibitorsContants;

	public ExhibitorsAdapter(Context context,
			List<ExhibitorsContant> listExhibitorsContants) {
		this.context = context;
		this.listExhibitorsContants = listExhibitorsContants;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listExhibitorsContants.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return listExhibitorsContants.get(position);
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
					R.layout.exhibitors, null);
		/*
		 * ImageView eventflag = (ImageView) convertView
		 * .findViewById(R.id.eventflag);
		 */
		TextView nameindex = (TextView) convertView
				.findViewById(R.id.exhibitorsnameindex);

		TextView exhibitorsname = (TextView) convertView
				.findViewById(R.id.exhibitorsname);
		TextView exhibitorsnumber = (TextView) convertView
				.findViewById(R.id.exhibitorsnumber);
		exhibitorsname.setGravity(Gravity.CENTER_VERTICAL);
		exhibitorsname.setText(listExhibitorsContants.get(position).getName()
				.split("/")[1]);
		exhibitorsnumber.setText(listExhibitorsContants.get(position)
				.getNumber());
		int idx = position - 1;
		// 判断前后Item是否匹配，如果不匹配则设置并显示，匹配则取消
		char previewChar = idx >= 0 ? listExhibitorsContants.get(idx).getName()
				.charAt(0) : ' ';
		char currentChar = listExhibitorsContants.get(position).getName()
				.charAt(0);
		// 将小写字符转换为大写字符
		char newPreviewChar = Character.toUpperCase(previewChar);
		char newCurrentChar = Character.toUpperCase(currentChar);

		if (newCurrentChar != newPreviewChar) {
			nameindex.setVisibility(View.VISIBLE);
			nameindex.setText(String.valueOf(newCurrentChar));
		} else {
			// 此段代码不可缺：实例化一个CurrentView后，会被多次赋值并且只有最后一次赋值的position是正确
			nameindex.setVisibility(View.GONE);
		}
		return convertView;
	}
}
