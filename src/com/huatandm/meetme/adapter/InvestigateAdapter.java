package com.huatandm.meetme.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huatandm.meetme.R;

public class InvestigateAdapter extends BaseAdapter {

	Context context;
	List<InvestigateContant> listInvestigateContants;

	public InvestigateAdapter(Context context,
			List<InvestigateContant> listInvestigateContants) {

		this.context = context;
		this.listInvestigateContants = listInvestigateContants;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listInvestigateContants.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return listInvestigateContants.get(position);
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
					R.layout.invesitigate, null);

		TextView eventname = (TextView) convertView
				.findViewById(R.id.invesitigatename);
		eventname.setText(listInvestigateContants.get(position)
				.getSurvey_title());
		return convertView;
	}
}
