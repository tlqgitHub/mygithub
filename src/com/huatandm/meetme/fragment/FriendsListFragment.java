package com.huatandm.meetme.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.huatandm.meetme.ActivityUtils;
import com.huatandm.meetme.HTMainActivity;
import com.huatandm.meetme.R;
import com.huatandm.meetme.fragment.detail.WebDetailFragment;
import com.huatandm.meetme.helper.AsyncImageLoader;
import com.huatandm.meetme.helper.AsyncImageLoader.ImageCallback;
import com.huatandm.meetme.helper.Connector;
import com.umeng.analytics.MobclickAgent;

public class FriendsListFragment extends BaseListFragment {

	private List<Map<String, Object>> friendlist = null;
	private Object m_aFriends[];

	private Boolean bool;
	private Button ch1;
	private Button ch2;
	FriendsAdapter adapter;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.msg_list, null);
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(this.getClass().getName());
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart(this.getClass().getName());
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setTitle(R.string.friendlist);

		ch1 = (Button) getActivity().findViewById(R.id.chioce1);
		ch2 = (Button) getActivity().findViewById(R.id.chioce2);
		ch1.setText(R.string.friendlist);
		ch2.setText(R.string.friendrequest);

		ch1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				bool = false;
				setChoice(0);
			}
		});

		ch2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				bool = true;
				setChoice(1);
			}
		});

		// defult
		bool = false;
		setChoice(0);

		// 为 ListView 的所有 item 注册 ContextMenu
		registerForContextMenu(getListView());
	}

	// 上下文菜单，本例会通过长按条目激活上下文菜单
	@Override
	public void onCreateContextMenu(ContextMenu menu, View view,
			ContextMenuInfo menuInfo) {
		menu.setHeaderTitle(getText(R.string.menu_friend_oper));
		// 添加菜单项
		menu.add(0, 101, 0, getText(R.string.menu_friend_dele));
		menu.add(0, 102, 0, getText(R.string.menu_friend_clen));
	}

	// 菜单单击响应
	@SuppressWarnings("unchecked")
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// 获取当前被选择的菜单项的信息
		// 关键代码在这里
		AdapterView.AdapterContextMenuInfo menuInfo;
		menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

		switch (item.getItemId()) {
		case 101:
			// 在这里添加处理代码
			Map<String, Object> map = (Map<String, Object>) m_aFriends[menuInfo.position];
			onRemoveFriend((String) map.get("Nick"));
			break;
		case 102:
			// 在这里添加处理代码
			// return false;
			break;
		}
		return true;
	}

	private void setChoice(int i) {
		// TODO Auto-generated method stub

		if (i == 0) {
			ch1.setBackgroundResource(R.drawable.button_hover_bg);
			;
			ch2.setBackgroundResource(R.drawable.button_bg_right);
			initData();
		} else {
			ch2.setBackgroundResource(R.drawable.button_hover_right_bg);
			;
			ch1.setBackgroundResource(R.drawable.button_bg);
			initData0();
		}
	}

	public void beginthread() {
		new Thread() {
			public void run() {
				initData();
			}
		}.start();
	}

	@SuppressLint("HandlerLeak")
	Handler h = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 0:
				UIupdata();
				break;
			default:
				break;
			}

			super.handleMessage(msg);
		}
	};

	@SuppressWarnings("unchecked")
	@Override
	public void onListItemClick(ListView lv, View v, int position, long id) {

		Map<String, Object> map = (Map<String, Object>) m_aFriends[position];

		String name = ActivityUtils.getNickWithuserId(getActivity(),
				Integer.parseInt((String) map.get("ID")));
		name = name.equals("") ? (String) map.get("Nick") : name;

		Bundle b = new Bundle();
		b.putString("fragment", "FriendsListFragment");
		b.putString("username", (String) map.get("Nick"));
		b.putString("recipient", name);
		b.putString("account", (String) map.get("Nick"));
		mListener.onArticleSelected(b);
		((HTMainActivity) getActivity()).switchContent(new WebDetailFragment(),
				2);
	}

	@SuppressWarnings("unchecked")
	private void UIupdata() {
		// TODO Auto-generated method stub
		friendlist = null;
		friendlist = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < m_aFriends.length; i++) {
			Map<String, Object> map = (Map<String, Object>) m_aFriends[i];
			friendlist.add(map);
		}
		removeProgressView();
		setListAdapter(new FriendsAdapter(getActivity(), bool, friendlist));
	}

	public void initData() {
		// TODO Auto-generated method stub
		addProgressView(getActivity());
		Connector o = ((HTMainActivity) getActivity()).getConnector();
		Object params[] = { o.getUsername(), o.getPassword(), o.getUsername(),
				ActivityUtils.getLang() };
		o.execAsyncMethod("dolphin.getFriends", params,
				new Connector.Callback() {
					@Override
					public void callFinished(Object result) {
						// TODO Auto-generated method stub
						m_aFriends = null;
						m_aFriends = (Object[]) result;
						h.sendEmptyMessage(0);
						super.callFinished(result);
					}

					@Override
					public boolean callFailed(Exception e) {
						// TODO Auto-generated method stub
						removeProgressView();
						return super.callFailed(e);
					}
				}, getActivity());

	}

	protected void initData0() {
		addProgressView(getActivity());
		Connector o = ((HTMainActivity) getActivity()).getConnector();
		Object params[] = { o.getUsername(), o.getPassword(),
				ActivityUtils.getLang() };

		o.execAsyncMethod("dolphin.getFriendRequests", params,
				new Connector.Callback() {
					@Override
					public void callFinished(Object result) {
						m_aFriends = null;
						m_aFriends = (Object[]) result;
						h.sendEmptyMessage(0);
						super.callFinished(result);
					}

					@Override
					public boolean callFailed(Exception e) {
						// TODO Auto-generated method stub
						removeProgressView();
						return super.callFailed(e);
					}
				}, getActivity());
	}

	public void onRemoveFriend(final String userName) {

		addProgressView(getActivity());
		Connector o = ((HTMainActivity) getActivity()).getConnector();
		Object[] aParams = { o.getUsername(), o.getPassword(), userName };

		o.execAsyncMethod("dolphin.removeFriend", aParams,
				new Connector.Callback() {
					@Override
					public void callFinished(Object result) {
						if (result.toString().equals("ok")) {
							m_aFriends = null;
							removeProgressView();
							initData();
						}
						super.callFinished(result);
					}

					@Override
					public boolean callFailed(Exception e) {
						// TODO Auto-generated method stub
						return super.callFailed(e);
					}
				}, getActivity());
	}

	public void onAcceptFriend(String s) {

		addProgressView(getActivity());
		Connector o = ((HTMainActivity) getActivity()).getConnector();

		Object[] aParams = { o.getUsername(), o.getPassword(), s };

		o.execAsyncMethod("dolphin.acceptFriendRequest", aParams,
				new Connector.Callback() {
					@Override
					public void callFinished(Object result) {
						if (result.toString().equals("ok")) {
							m_aFriends = null;
							removeProgressView();
							initData0();
						}
						super.callFinished(result);
					}

					@Override
					public boolean callFailed(Exception e) {
						// TODO Auto-generated method stub
						return super.callFailed(e);
					}
				}, getActivity());
	}

	public void onRejectFriend(String s) {

		addProgressView(getActivity());
		Connector o = ((HTMainActivity) getActivity()).getConnector();

		Object[] aParams = { o.getUsername(), o.getPassword(), s };

		o.execAsyncMethod("dolphin.declineFriendRequest", aParams,
				new Connector.Callback() {
					@Override
					public void callFinished(Object result) {
						if (result.toString().equals("ok")) {
							m_aFriends = null;
							removeProgressView();
							initData0();
						}
						super.callFinished(result);
					}

					@Override
					public boolean callFailed(Exception e) {
						// TODO Auto-generated method stub
						return super.callFailed(e);
					}
				}, getActivity());

	}

	public class FriendsAdapter extends BaseAdapter {

		Context context;
		List<Map<String, Object>> friendlist;
		AsyncImageLoader asyncImageLoader = new AsyncImageLoader();
		boolean b;

		public FriendsAdapter(Context context, boolean bool,
				List<Map<String, Object>> tempfriendlist) {
			this.context = context;
			friendlist = tempfriendlist;
			this.b = bool;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub

			return friendlist.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return friendlist.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
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

			Button chioc1 = (Button) convertView.findViewById(R.id.chioce3);
			Button chioc2 = (Button) convertView.findViewById(R.id.chioce4);

			chioc1.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					onRejectFriend((String) friendlist.get(position)
							.get("Nick"));
				}
			});

			chioc2.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					onAcceptFriend((String) friendlist.get(position)
							.get("Nick"));
				}
			});

			if (b) {
				chioc1.setVisibility(View.VISIBLE);
				chioc2.setVisibility(View.VISIBLE);
			} else {
				chioc1.setVisibility(View.GONE);
				chioc2.setVisibility(View.GONE);
			}

			if (!b) {
				speakercomplay.setVisibility(View.VISIBLE);
				speakertitle.setVisibility(View.VISIBLE);
			} else {
				speakercomplay.setVisibility(View.GONE);
				speakertitle.setVisibility(View.GONE);
			}

			String speakname = ActivityUtils.getNickWithuserId(getActivity(),
					Integer.parseInt((String) friendlist.get(position)
							.get("ID")));
			speakname = speakname.equals("") ? (String) friendlist
					.get(position).get("Nick") : speakname;

			speakername.setText(speakname);
			speakercomplay
					.setText((String) friendlist.get(position).get("Sex"));
			speakertitle.setText((String) friendlist.get(position).get("City"));

			Drawable cachedImage = asyncImageLoader.loadDrawable(
					(String) friendlist.get(position).get("Thumb"),
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
}
