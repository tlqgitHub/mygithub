package com.huatandm.meetme.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.huatandm.meetme.R;
import com.huatandm.meetme.helper.AsyncImageLoader;
import com.huatandm.meetme.helper.AsyncImageLoader.ImageCallback;

public class DiscussAdapter extends BaseAdapter {

	Context context;
	AsyncImageLoader asyncImageLoader = new AsyncImageLoader();

	DiscussContant DiscussContant;
	List<DiscussContant> listspeaker;

	public DiscussAdapter(Context context, List<DiscussContant> listspeaker) {
		this.listspeaker = listspeaker;
		this.context = context;
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

		TextView speakername = (TextView) convertView
				.findViewById(R.id.speakername);
		TextView speakercomplay = (TextView) convertView
				.findViewById(R.id.speakercomplay);
		TextView speakertitle = (TextView) convertView
				.findViewById(R.id.speakertitle);

		speakername.setText((String) listspeaker.get(position)
				.getDiscussion_name());
		speakercomplay.setText((String) listspeaker.get(position)
				.getDiscussiton_content());

		speakertitle.setText((String) listspeaker.get(position)
				.getDiscussion_date());

		Drawable cachedImage = asyncImageLoader.loadDrawable(
				(String) listspeaker.get(position).getDiscussion_photo(),
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
			// System.out.println("显示头像");
			speakerimage.setImageDrawable(cachedImage);
			// list1.get(arg0).getUser().setProfile_image_url(cachedImage);
		}
		return convertView;
	}
}
