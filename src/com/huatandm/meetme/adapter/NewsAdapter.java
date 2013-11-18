package com.huatandm.meetme.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huatandm.meetme.R;

public class NewsAdapter extends BaseAdapter {

	Context context;
	List<NewsContant> listnewscontant;
	int page;

	public NewsAdapter(Context context, List<NewsContant> listnewscontant) {
		this.context = context;
		this.listnewscontant = listnewscontant;

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listnewscontant.size();
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
					R.layout.cell_title_add_subtitle_item, null);

		TextView title = (TextView) convertView
				.findViewById(R.id.cell_title_textView);
		TextView subtitle = (TextView) convertView
				.findViewById(R.id.cell_sub_textView);

		title.setText(listnewscontant.get(position).getNewsname().trim());
		subtitle.setText(listnewscontant.get(position).getNewstime());
		return convertView;
	}
}
