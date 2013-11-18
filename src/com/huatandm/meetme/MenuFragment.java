package com.huatandm.meetme;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.huatandm.meetme.fragment.DiscussListFragment;
import com.huatandm.meetme.fragment.DocsListFragment;
import com.huatandm.meetme.fragment.FriendsListFragment;
import com.huatandm.meetme.fragment.HomeListFragment;
import com.huatandm.meetme.fragment.InvestigateListFragment;
import com.huatandm.meetme.fragment.MessagesListFragment;
import com.huatandm.meetme.fragment.MySchedulesListFragment;
import com.huatandm.meetme.fragment.NewsListFragment;
import com.huatandm.meetme.fragment.PartivipantsListFragment;
import com.huatandm.meetme.fragment.SchedulesListFragment;
import com.huatandm.meetme.fragment.SpeakersListFragment;
import com.huatandm.meetme.fragment.SponsorsListFragment;
import com.huatandm.meetme.fragment.detail.WebDetailFragment;
import com.umeng.analytics.MobclickAgent;

/**
 * @ClassName: MenuFragment
 * @Description: 菜单界面
 * @author tlq
 * @date 2013-9-9 下午5:17:10
 * 
 */
public class MenuFragment extends ListFragment {

	// icons and title
	private String[] mTitles;
	static final int resource[] = { 
		R.drawable.icon_backhome, 
		0,
		R.drawable.icon_forum, 
		R.drawable.icon_friend,
		R.drawable.icon_messages, 
		R.drawable.icon_survey,
		R.drawable.icon_my_agenda, 
		0, 
		R.drawable.icon_agenda,
		R.drawable.icon_about,
		R.drawable.icon_speaker, 
		R.drawable.icon_sponsor, 
		R.drawable.icon_delegation,
		R.drawable.icon_news,
		R.drawable.icon_document,//文档图标
		R.drawable.icon_document,//文档图标
		R.drawable.icon_document,//文档图标
		R.drawable.icon_document 
		};

	private MyAdapter adapter;

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(this.getClass().getName());
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(this.getClass().getName());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.menu_list, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initdata();
	}

	public void initdata() {
		// TODO Auto-generated method stub
		mTitles = getResources().getStringArray(R.array.menu_names);
		adapter = new MyAdapter(getActivity());
		setListAdapter(adapter);
	}

	public class MyAdapter extends BaseAdapter {

		private LayoutInflater mInflater;
		TextView title;
		TextView mun;
		ImageView imageview;
		View v0;
		View v1;
		View v2;

		public MyAdapter(Context context) {
			this.mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mTitles.length;
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int getViewTypeCount() {
			// TODO Auto-generated method stub
			return 2;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			if (position == 1 || position == 7) {
				convertView = mInflater.inflate(
						R.layout.cell_leftmeun_list_title_item, null);
				title = (TextView) convertView
						.findViewById(R.id.cell_meun_textView2);
				title.setText(mTitles[position]);
			} else {
				 convertView = mInflater.inflate(
						 R.layout.cell_leftmeun_list_item, null);
				 title = (TextView) convertView
						 .findViewById(R.id.cell_meun_textView);
				 imageview = (ImageView) convertView
						 .findViewById(R.id.cell_meun_imageView);
				 mun = (TextView) convertView.findViewById(R.id.cell_meun_sMun);

				 imageview.setImageResource(resource[position]);
				 title.setText(mTitles[position]);

				 
				 if (position == 3 && CONTANT.un_read_msgs != null) {
					 if (CONTANT.un_read_msgs.endsWith("0")) {

					 } else {
						 mun.setVisibility(View.VISIBLE);
						 mun.setText(CONTANT.un_read_msgs);
					 }

				 }

				 if (position == 4 && CONTANT.un_read_friends != null) {
					 if (CONTANT.un_read_friends.endsWith("0")) {

					 } else {
						 mun.setVisibility(View.VISIBLE);
						 mun.setText(CONTANT.un_read_friends);
					 }
				 }
				 
			 }
			
			return convertView;
		}
	}

	@Override
	public void onListItemClick(ListView lv, View v, int position, long id) {

		Fragment newContent = null;

		if (1 < position && position < 7) {
			if (((HTMainActivity) getActivity()).isLogin()) {
			} else {
				((HTMainActivity) getActivity()).login(position);
				return;
			}
		}

		switch (position) {
		case 0:
			newContent = new HomeListFragment();
			break;
			
			
		case 2:
			newContent = new DiscussListFragment();
			break;
		case 3:
			newContent = new MessagesListFragment();
			break;
		case 4:
			newContent = new FriendsListFragment();
			break;
		case 5:
			newContent = new InvestigateListFragment();
			break;
		case 6:
			newContent = new MySchedulesListFragment();
			break;
			
			
		case 8:
			newContent = new SchedulesListFragment();
			break;
		case 9:
			Bundle b = new Bundle();
			b.putInt("index", 0);
			b.putString("fragment", "nullFragment");
			((HTMainActivity) getActivity()).setBundle_data(b);

			newContent = new WebDetailFragment();
			break;
		case 10:
			newContent = new SpeakersListFragment();
			break;
		case 11:
			newContent = new SponsorsListFragment();
			break;
		case 12:
			if (((HTMainActivity) getActivity()).isLogin()) {
				newContent = new PartivipantsListFragment();
			} else {
				((HTMainActivity) getActivity()).login(10);
				return;
			}
			break;
		case 13:
			newContent = new NewsListFragment();
			break;
		case 14:
			newContent = new DocsListFragment();
			break;
		case 15:
			newContent = new DocsListFragment();
			break;
		case 16:
			newContent = new DocsListFragment();
			break;
		case 17:
			newContent = new DocsListFragment();
			break;
		}
		if (newContent != null)
			switchFragment(newContent);
	}

	// the meat of switching the above fragment
	private void switchFragment(Fragment fragment) {
		if (getActivity() == null)
			return;

		if (getActivity() instanceof HTMainActivity) {
			HTMainActivity fca = (HTMainActivity) getActivity();
			fca.switchContent(fragment, 1);
		}
	}
}
