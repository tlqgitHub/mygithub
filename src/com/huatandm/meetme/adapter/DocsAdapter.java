package com.huatandm.meetme.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huatandm.meetme.R;

public class DocsAdapter extends BaseAdapter {
	Context context;
	List<DocsContant> listDocsContants;

	public DocsAdapter(Context context, List<DocsContant> listDocsContants) {
		this.context = context;
		this.listDocsContants = listDocsContants;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listDocsContants.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return listDocsContants.get(position);
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

		title.setText(listDocsContants.get(position).getDoc_title());
		subtitle.setText(String.valueOf(context.getText(R.string.size))
				+ listDocsContants.get(position).getDoc_size());

		return convertView;
	}
}
