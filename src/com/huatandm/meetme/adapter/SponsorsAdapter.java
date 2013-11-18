package com.huatandm.meetme.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.huatandm.meetme.CONTANT;
import com.huatandm.meetme.R;

public class SponsorsAdapter extends BaseAdapter {

	Context context;
	List<SponsorsContant> listSponsorsContants;

	public SponsorsAdapter(Context context,
			List<SponsorsContant> listsponsorscontant) {
		this.context = context;
		this.listSponsorsContants = listsponsorscontant;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listSponsorsContants.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return listSponsorsContants.get(position);
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
					R.layout.sponsorslogo, null);
		ImageView sponsorslog = (ImageView) convertView.findViewById(R.id.logo);

		Bitmap bm = BitmapFactory.decodeFile(CONTANT.mobilepath + "/"
				+ CONTANT.totalfolder + "/" + CONTANT.eventIds + "/"
				+ CONTANT.sponsors + "/"
				+ listSponsorsContants.get(position).getLogo());
		sponsorslog.setImageBitmap(bm);
		return convertView;
	}

}
