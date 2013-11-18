package com.huatandm.meetme.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.huatandm.meetme.ActivityUtils;
import com.huatandm.meetme.CONTANT;
import com.huatandm.meetme.HTMainActivity;
import com.huatandm.meetme.R;
import com.huatandm.meetme.adapter.DiscussAdapter;
import com.huatandm.meetme.adapter.DiscussContant;
import com.huatandm.meetme.fragment.detail.DiscussDetailFragment;
import com.huatandm.meetme.fragment.detail.SendMsgListFragment;
import com.huatandm.meetme.helper.Connector;
import com.umeng.analytics.MobclickAgent;

/**
 * @ClassName: DiscussListFragment
 * @Description: 讨论区
 * @author tlq
 * @date 2013-9-9 下午5:18:37
 * 
 */
public class DiscussListFragment extends BaseListFragment {

	Object[] m_aDiscusss;
	List<DiscussContant> listDiscussContants = null;
	String commentIds = "0";

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		setTitle(R.string.discuss);
		initData();
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(this.getClass().getName());
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		Button but = getRight_bt();
		but.setBackgroundResource(R.drawable.addcomment);
		but.setVisibility(View.VISIBLE);
		but.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				add_comment();
			}
		});

		getRight_view().setVisibility(View.VISIBLE);
		MobclickAgent.onPageStart(this.getClass().getName());
	}

	// 新建话题
	public void add_comment() {
		// TODO Auto-generated method stub
		Bundle b = new Bundle();
		b.putString("fragment", "DiscussListFragment");
		b.putString("discussIds", "0");
		mListener.onArticleSelected(b);
		HTMainActivity fca = (HTMainActivity) getActivity();
		fca.switchContent(new SendMsgListFragment(), 2);
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

	@Override
	public void onListItemClick(ListView lv, View v, int position, long id) {

		Bundle b = new Bundle();
		b.putInt("discussIds", listDiscussContants.get(position)
				.getDiscussion_ID());
		b.putString("photo", listDiscussContants.get(position)
				.getDiscussion_photo());
		b.putString("name", listDiscussContants.get(position)
				.getDiscussion_name());
		b.putString("content", listDiscussContants.get(position)
				.getDiscussiton_content());
		b.putString("date", listDiscussContants.get(position)
				.getDiscussion_date());
		mListener.onArticleSelected(b);

		((HTMainActivity) getActivity()).switchContent(
				new DiscussDetailFragment(), 2);
	}

	@SuppressWarnings("unchecked")
	private void UIupdata() {
		// TODO Auto-generated method stub
		// 判断如果新闻的长度大于10，有更多，如果欣慰的长度小于10，没有更多
		listDiscussContants = new ArrayList<DiscussContant>();
		for (int i = 0; i < m_aDiscusss.length; i++) {

			DiscussContant discussContant = new DiscussContant();
			Map<String, Object> map = (Map<String, Object>) m_aDiscusss[i];
			discussContant.setDiscussion_ID((Integer) map.get("discussion_ID"));
			discussContant.setDiscussion_name((String) map
					.get("discussion_name"));
			discussContant.setDiscussion_photo((String) map
					.get("discussion_photo"));
			discussContant.setDiscussiton_content((String) map
					.get("discussion_content"));
			discussContant.setDiscussion_date((String) map
					.get("discussion_date"));
			listDiscussContants.add(discussContant);
		}

		removeProgressView();
		setListAdapter(new DiscussAdapter(getActivity(), listDiscussContants));
	}

	public void initData() {
		// TODO Auto-generated method stub
		addProgressView(getActivity());
		Connector o = ((HTMainActivity) getActivity()).getConnector();
		Object[] aParams = { o.getUsername(), o.getPassword(),
				CONTANT.eventIds, commentIds, ActivityUtils.getLang() };
		o.execAsyncMethod("dolphin.DiscussionList", aParams,
				new Connector.Callback() {

					@Override
					public boolean callFailed(Exception e) {
						// TODO Auto-generated method stub
						return super.callFailed(e);
					}

					public void callFinished(Object result) {
						m_aDiscusss = (Object[]) result;
						h.sendEmptyMessage(0);
					}
				}, getActivity());
	}

}
