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

import com.huatandm.meetme.ActivityUtils;
import com.huatandm.meetme.CONTANT;
import com.huatandm.meetme.R;

public class SpeakersAdapter extends BaseAdapter {

	List<SpeakersContant> listspeaker;
	Context context;
	SpeakersContant speakersContant;
	String path;

	public SpeakersAdapter(Context context, String path,
			List<SpeakersContant> listspeaker) {
		this.listspeaker = listspeaker;
		this.context = context;
		this.path = path;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listspeaker.size();
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
					R.layout.cell_user_item, null);
		ImageView speakerimage = (ImageView) convertView
				.findViewById(R.id.speakerimage);

		TextView nameindex = (TextView) convertView
				.findViewById(R.id.nameindex);
		TextView speakername = (TextView) convertView
				.findViewById(R.id.speakername);
		TextView speakercomplay = (TextView) convertView
				.findViewById(R.id.speakercomplay);
		TextView speakertitle = (TextView) convertView
				.findViewById(R.id.speakertitle);

		int idx = position - 1;
		// 判断前后Item是否匹配，如果不匹配则设置并显示，匹配则取消
		char previewChar = idx >= 0 ? ActivityUtils.getPinYinHeadChar(
				listspeaker.get(idx).getSpeaker_name()).charAt(0) : ' ';
		char currentChar = ActivityUtils.getPinYinHeadChar(
				listspeaker.get(position).getSpeaker_name()).charAt(0);
		// 将小写字符转换为大写字符
		char newPreviewChar = Character.toUpperCase(previewChar);
		char newCurrentChar = Character.toUpperCase(currentChar);
		if (newCurrentChar != newPreviewChar) {
			nameindex.setVisibility(View.VISIBLE);
			nameindex.setText("  " + String.valueOf(newCurrentChar));
		} else {
			// 此段代码不可缺：实例化一个CurrentView后，会被多次赋值并且只有最后一次赋值的position是正确
			nameindex.setVisibility(View.GONE);
		}

		speakername.setText(listspeaker.get(position).getSpeaker_name());
		speakercomplay.setText(listspeaker.get(position).getSpeaker_complay());
		speakertitle.setText(listspeaker.get(position).getSpeaker_title());

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 1;
		Bitmap bm = BitmapFactory.decodeFile(CONTANT.mobilepath + "/"
				+ CONTANT.totalfolder + "/" + CONTANT.eventIds + "/" + path
				+ "/" + listspeaker.get(position).getSpeaker_image());
		speakerimage.setImageBitmap(bm);
		return convertView;
	}
}
