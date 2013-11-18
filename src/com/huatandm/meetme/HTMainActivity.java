package com.huatandm.meetme;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.view.KeyEvent;
import android.widget.Toast;

import com.huatandm.meetme.adapter.SchedulesContant;
import com.huatandm.meetme.adapter.SpeakersContant;
import com.huatandm.meetme.fragment.BaseListFragment.OnArticleSelectedListener;
import com.huatandm.meetme.fragment.DiscussListFragment;
import com.huatandm.meetme.fragment.FriendsListFragment;
import com.huatandm.meetme.fragment.HomeListFragment;
import com.huatandm.meetme.fragment.InvestigateListFragment;
import com.huatandm.meetme.fragment.LoginListFragment;
import com.huatandm.meetme.fragment.MessagesListFragment;
import com.huatandm.meetme.fragment.MySchedulesListFragment;
import com.huatandm.meetme.fragment.PartivipantsListFragment;
import com.huatandm.meetme.helper.Connector;
import com.umeng.analytics.MobclickAgent;

/**
 * @ClassName: HTMainActivity
 * @Description: 主界面Activity
 * @author tlq
 * @date 2013-9-9 下午5:16:47
 * 
 */

public class HTMainActivity extends BaseActivity implements
		OnArticleSelectedListener {

	private Fragment mContent;// 当前fragment
	private ArrayList<Fragment> listFragment;// 存储fragment list
	private MenuFragment meun_fragment;// 隐藏菜单frament

	private Connector connector;// 网络操作class

	private SharedPreferences login;// 登录状态、用户、密码存储

	private Bundle bundle_data;// Fragment直接 数据传递

	private Bundle outState;// Fragment back data save

	private boolean isBack = false;// 返回状态标志

	// 显示隐藏菜单
	@Override
	public void toggle() {
		// TODO Auto-generated method stub
		if (isLogin()) {
			// 获取未读消息和未查看好友请求
			reloadRemoteData();
		}
		super.toggle();
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		MobclickAgent.onPageStart(this.getClass().getName());
	}

	public void onPause() {
 		super.onPause();
		MobclickAgent.onPause(this);
		MobclickAgent.onPageEnd(this.getClass().getName());
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// set the Above View
		setContentView(R.layout.content_frame);
		// set the Behind View
		setBehindContentView(R.layout.menu_frame);

		// set the Above View
		if (mContent == null)
			mContent = new HomeListFragment();
		switchContent(mContent, 1);
		// set the Behind View

		meun_fragment = new MenuFragment();
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.menu_frame, meun_fragment).commit();

		// init
		login = getSharedPreferences("login", MODE_PRIVATE);
		connector = new Connector(CONTANT.url);
		outState = new Bundle();
		initdata();

		// Fragment ChangedListener
		getSupportFragmentManager().addOnBackStackChangedListener(
				new OnBackStackChangedListener() {

					@Override
					public void onBackStackChanged() {
						// TODO Auto-generated method stub
					}
				});
		getSupportFragmentManager().removeOnBackStackChangedListener(
				new OnBackStackChangedListener() {

					@Override
					public void onBackStackChanged() {
						// TODO Auto-generated method stub
					}
				});
	}

	// 读取会议和嘉宾资料
	@SuppressWarnings("unchecked")
	private void initdata() {
		// TODO Auto-generated method stub
		// 会议
		Object datas1 = ActivityUtils.parse(this, CONTANT.schedules);
		ArrayList<SchedulesContant> listdata1 = new ArrayList<SchedulesContant>();

		Map<String, Object> maps1 = (Map<String, Object>) datas1;
		Object[] menu1 = (Object[]) maps1.get("content");
		for (int i = 0; i < menu1.length; i++) {

			SchedulesContant schedulesContant = new SchedulesContant();
			Map<String, Object> map = (Map<String, Object>) menu1[i];

			schedulesContant.setSchedulesdate(((String) map
					.get("schedule_starttime")).split(" ")[0]);
			schedulesContant.setSchedulesshowtime(((String) map
					.get("schedule_starttime")).split(" ")[1].substring(0, 5)
					+ " - "
					+ ((String) map.get("schedule_endtime")).split(" ")[1]
							.substring(0, 5));
			schedulesContant.setSchedulestime((String) map
					.get("schedule_starttime"));
			schedulesContant
					.setSchedulestopic((String) ((Map<String, Object>) map
							.get("schedule_topic")).get(ActivityUtils.getLang()));
			schedulesContant
					.setSchedulesroom((String) ((Map<String, Object>) map
							.get("schedule_room")).get(ActivityUtils.getLang()));
			schedulesContant
					.setSchedulesplace((String) ((Map<String, Object>) map
							.get("schedule_place")).get(ActivityUtils.getLang()));
			schedulesContant.setSchedulesspeakers(map.get("schedule_speakers"));
			schedulesContant
					.setSchedulescontent((String) ((Map<String, Object>) map
							.get("schedule_desc")).get(ActivityUtils.getLang()));
			schedulesContant.setSchedulesid((Integer) map.get("schedule_ID"));
			schedulesContant.setMap((String) map.get("schedule_map"));
			schedulesContant
					.setSchedule_subschedules(((Map<String, Object>) map
							.get("schedule_subschedules")).get(ActivityUtils
							.getLang()));

			listdata1.add(schedulesContant);
		}

		Collections.sort(listdata1);
		CONTANT.listschedules = listdata1;

		// 演讲嘉宾

		Object datas2 = ActivityUtils.parse(this, CONTANT.speakers);
		ArrayList<SpeakersContant> listdata2 = new ArrayList<SpeakersContant>();

		Map<String, Object> maps2 = (Map<String, Object>) datas2;
		Object[] menu2 = (Object[]) maps2.get("content");

		for (int i = 0; i < menu2.length; i++) {

			SpeakersContant speakersContant = new SpeakersContant();
			Map<String, Object> map = (Map<String, Object>) menu2[i];

			speakersContant
					.setSpeaker_image((String) map.get("speaker_avatar"));
			;

			speakersContant
					.setSpeaker_compare(ActivityUtils
							.getPinYinHeadChar((String) ((Map<String, Object>) map
									.get("speaker_name")).get(ActivityUtils
									.getLang())));

			speakersContant.setSpeaker_name((String) ((Map<String, Object>) map
					.get("speaker_name")).get(ActivityUtils.getLang()));

			speakersContant
					.setSpeaker_complay((String) ((Map<String, Object>) map
							.get("speaker_company")).get(ActivityUtils
							.getLang()));

			speakersContant
					.setSpeaker_title((String) ((Map<String, Object>) map
							.get("speaker_title")).get(ActivityUtils.getLang()));

			speakersContant
					.setSpeaker_introduction((String) ((Map<String, Object>) map
							.get("speaker_introduction")).get(ActivityUtils
							.getLang()));

			speakersContant.setSpeakerid((Integer) map.get("speaker_ID"));
			speakersContant.setSpeaker_username((String) map
					.get("speaker_username"));
			speakersContant.setSpeaker_schedule(map.get("speaker_schedule"));

			listdata2.add(speakersContant);
		}

		Collections.sort(listdata2);
		CONTANT.listspeakers = listdata2;
	}

	// 登录状态检查
	public boolean isLogin() {
		// TODO Auto-generated method stub
		return login.getBoolean("login", false);
	}

	// login
	public void login(int key) {
		// TODO Auto-generated method stub

		Bundle b1 = new Bundle();
		b1.putInt("fromkey", key);
		setBundle_data(b1);

		if (key == -1) {
			switchContent(new LoginListFragment(), 2);
		} else {
			switchContent(new LoginListFragment(), 1);
		}
	}

	// logout
	public void logout() {
		// TODO Auto-generated method stub
		dialog();
	}

	// 界面跳转
	public void switchContent(Fragment fragment, int index_type) {

		mContent = fragment;
		isBack = false;

		if (index_type == 1) {
			removeFragment();
			listFragment = new ArrayList<Fragment>();
			getSupportFragmentManager().beginTransaction()
			// .setCustomAnimations(R.anim.right_to_left, 0)
					.replace(R.id.content_frame, fragment).commit();
		} else if (index_type == 2) {
			listFragment.add(mContent);
			getSupportFragmentManager().beginTransaction()
					// .setCustomAnimations(R.anim.right_to_left, 0)
					.replace(R.id.content_frame, fragment).addToBackStack(null)
					.commit();
		}

		getSlidingMenu().showContent();
	}

	// 回收数据
	private void removeFragment() {
		// TODO Auto-generated method stub
		if (null == listFragment) {
			return;
		}
		if (listFragment.size() > 0) {

			for (int i = 0; i < listFragment.size(); i++) {
				getSupportFragmentManager().beginTransaction()
						.remove(listFragment.get(i)).commit();
			}
		}
	}

	// 注销提示
	protected void dialog() {

		AlertDialog.Builder builder = new Builder(this);
		builder.setMessage(R.string.are_you_sure);
		builder.setPositiveButton(getText(R.string.ok),
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();

						login.edit().putBoolean("login", false)
								.putBoolean("logintype", false)
								// .putString("username","")
								// .putString("password", "")
								.commit();

						((HomeListFragment) mContent).logout();
					}
				});

		builder.setNegativeButton(getText(R.string.cancel),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});

		builder.create().show();
	}

	// 登录成功
	public void loginOk(int lFromFraId) {
		// TODO Auto-generated method stub
		connector = new Connector(CONTANT.url, login.getString("username", ""),
				login.getString("password", ""));
		connector.setPassword(login.getString("password", ""));

		gotoFragment(lFromFraId);
	}

	// 登录后界面的跳转
	public void gotoFragment(int position) {
		// TODO Auto-generated method stub
		Fragment newContent = null;

		switch (position) {
		case -1:
			back();
			break;
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
		case 10:
			newContent = new PartivipantsListFragment();
			break;
		}

		if (newContent != null)
			switchContent(newContent, 1);
	}

	/**
	 * 返回键响应
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return back();
		}
		return false;
	}

	public boolean back() {
		// TODO Auto-generated method stub

		if (getSlidingMenu().isMenuShowing()) {
			getSlidingMenu().showContent();
		} else {

			if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
				goHome();
			} else {
				isBack = true;
				getSupportFragmentManager().popBackStack();
			}
		}
		return true;
	}

	// 返回主页
	private void goHome() {
		// TODO Auto-generated method stub
		if (mContent instanceof HomeListFragment) {
			exitBy2Click(); // 调用双击退出函数
		} else {
			mContent = new HomeListFragment();
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.content_frame, mContent).commit();
		}
	}

	/**
	 * 双击退出函数
	 */
	private static Boolean isExit = false;

	private void exitBy2Click() {
		Timer tExit = null;
		if (isExit == false) {
			isExit = true; // 准备退出
			Toast.makeText(this, getText(R.string.hit_two_exit),
					Toast.LENGTH_SHORT).show();
			tExit = new Timer();
			tExit.schedule(new TimerTask() {
				@Override
				public void run() {
					isExit = false; // 取消退出
				}
			}, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

		} else {
			finish();
			System.exit(0);
		}
	}

	// get unread friend_count message_count
	protected void reloadRemoteData() {

		addProgressView(this);
		Connector o = getConnector();
		Object[] aParams = { o.getUsername(), o.getPassword(),
				ActivityUtils.getLang() };
		o.execAsyncMethod("dolphin.Count", aParams, new Connector.Callback() {

			@SuppressWarnings("unchecked")
			public void callFinished(Object result) {
				Map<String, Object> map = (Map<String, Object>) result;
				String friend_count = (String) map.get("Friend_Count");
				String message_count = (String) map.get("Message_Count");

				if (friend_count != null) {
					CONTANT.un_read_friends = friend_count;
				}

				if (message_count != null) {
					CONTANT.un_read_msgs = message_count;
				}

				removeProgressView();
				meun_fragment.initdata();
			}

			@Override
			public boolean callFailed(Exception e) {
				// TODO Auto-generated method stub

				removeProgressView();
				return super.callFailed(e);
			}

		}, this);
	}

	public Connector getConnector() {
		return connector;
	}

	public void setConnector(Connector connector) {
		this.connector = connector;
	}

	@Override
	public void onArticleSelected(Object object) {
		// TODO Auto-generated method stub
		setBundle_data((Bundle) object);
	}

	// 反馈接口
	@Override
	public void onBack(Object object) {
		// TODO Auto-generated method stub
	}

	public boolean isBack() {
		return isBack;
	}

	public void setBack(boolean isBack) {
		this.isBack = isBack;
	}

	public Bundle getOutState() {
		return outState;
	}

	public void setOutState(Bundle outState) {
		this.outState = outState;
	}

	public Bundle getBundle_data() {
		return bundle_data;
	}

	public void setBundle_data(Bundle bundle_data) {
		this.bundle_data = bundle_data;
	}
}
