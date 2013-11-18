package com.huatandm.meetme.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.huatandm.meetme.ActivityUtils;
import com.huatandm.meetme.R;
import com.huatandm.meetme.helper.AsyncImageLoader;
import com.huatandm.meetme.helper.AsyncImageLoader.ImageCallback;

public class MessageAdapter extends BaseAdapter {

	Context context;

	List<Map<String, Object>> tempfriendlist;
	AsyncImageLoader asyncImageLoader = new AsyncImageLoader();
	String txt = "";
	boolean isbox;

	public MessageAdapter(Context context, boolean isbox,
			List<Map<String, Object>> tempfriendlist) {
		this.context = context;
		this.tempfriendlist = tempfriendlist;
		this.isbox = isbox;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return tempfriendlist.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return tempfriendlist.get(position);
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
					R.layout.cell_with_head_item, null);
		ImageView speakerimage = (ImageView) convertView
				.findViewById(R.id.speakerimage);

		TextView speakername = (TextView) convertView
				.findViewById(R.id.speakername);
		TextView speakercomplay = (TextView) convertView
				.findViewById(R.id.speakercomplay);
		TextView speakertitle = (TextView) convertView
				.findViewById(R.id.speakertitle);

		speakername.setText((String) tempfriendlist.get(position)
				.get("Subject"));
		speakercomplay.setText(isbox ? "From :"
				+ tempfriendlist.get(position).get("Nick") : "To :"
				+ tempfriendlist.get(position).get("Nick"));

		if ((boolean) tempfriendlist.get(position).get("New").equals("1")) {
			txt = ActivityUtils.getLang().equals("en") ? "New" : "新";
			txt = "     " + txt;
		} else {
			txt = "     ";
		}

		speakertitle.setText((String) tempfriendlist.get(position).get("Date")
				+ txt);
		Drawable cachedImage = asyncImageLoader.loadDrawable(
				(String) tempfriendlist.get(position).get("Thumb"),
				speakerimage, new ImageCallback() {

					@Override
					public void imageLoaded(Drawable imageDrawable,
							ImageView imageView, String imageUrl) {
						// System.out.println("显示头像啊");
						imageView.setImageDrawable(imageDrawable);
					}
				});

		if (cachedImage == null) {
			speakerimage.setImageResource(R.drawable.no_image);
		} else {
			speakerimage.setImageDrawable(cachedImage);
		}
		return convertView;
	}
}
