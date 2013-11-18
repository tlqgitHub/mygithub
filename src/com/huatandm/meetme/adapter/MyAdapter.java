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
import android.widget.TextView;

import com.huatandm.meetme.CONTANT;
import com.huatandm.meetme.R;
import com.huatandm.meetme.helper.FileUtils;

public class MyAdapter extends BaseAdapter {

	List<MainContant> listmaincontant;
	Context context;
	FileUtils fileUtils;

	public MyAdapter(Context context, List<MainContant> listmaincontant) {
		this.listmaincontant = listmaincontant;
		this.context = context;
		fileUtils = new FileUtils(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listmaincontant.size();
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
					R.layout.eventview, null);
		ImageView eventimage = (ImageView) convertView
				.findViewById(R.id.eventimage);
		TextView eventname = (TextView) convertView
				.findViewById(R.id.eventname);
		TextView eventaddress = (TextView) convertView
				.findViewById(R.id.eventaddress);
		eventname.setText(listmaincontant.get(position).getEventtitle());
		eventaddress.setText(listmaincontant.get(position).getEventplace());
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 2;
		Bitmap bm = BitmapFactory.decodeFile(fileUtils.getSDPATH() + "/"
				+ CONTANT.totalfolder + "/" + CONTANT.event + "/"
				+ listmaincontant.get(position).getEventimage(), options);
		eventimage.setImageBitmap(bm);
		return convertView;
	}

}
