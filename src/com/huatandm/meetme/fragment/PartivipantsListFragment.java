package com.huatandm.meetme.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huatandm.meetme.ActivityUtils;
import com.huatandm.meetme.CONTANT;
import com.huatandm.meetme.HTMainActivity;
import com.huatandm.meetme.R;
import com.huatandm.meetme.adapter.SpeakersAdapter;
import com.huatandm.meetme.adapter.SpeakersContant;
import com.huatandm.meetme.fragment.detail.UserDetailListFragment;
import com.umeng.analytics.MobclickAgent;

public class PartivipantsListFragment extends BaseListFragment {

	private String py[] = { "#", "A", "B", "C", "D", "E", "F", "G", "H", "I",
			"J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
			"W", "X", "Y", "Z" };

	List<SpeakersContant> listparters;
	List<SpeakersContant> listSearch;
	List<String[]> list = null;

	private ListView speedListView;
	private EditText search;
	private SpeakersAdapter adapter;

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		addProgressView(getActivity());
		speedListView = (ListView) getActivity().findViewById(R.id.listview);
		speedListView.setVisibility(View.VISIBLE);
		speedListView.setDivider(null);
		speedListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				String s = ((TextView) arg1).getText().toString();

				int localPosition = binSearch(list, s); // 接收返回值

				if (localPosition != -1) {
					// 防止点击出现的txtOverlay与滚动出现的txtOverlay冲突
					getListView().setSelection(localPosition); // 让List指向对应位置的Item
				}
			}
		});

		RelativeLayout convertView = (RelativeLayout) getActivity()
				.findViewById(R.id.layout2);
		convertView.setVisibility(View.VISIBLE);

		search = (EditText) getActivity().findViewById(R.id.search);
		search.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				final String txt = s.toString()
						.toLowerCase(Locale.getDefault());

				if (listSearch != null) {
					listSearch = new ArrayList<SpeakersContant>();
					if (listparters != null && listparters.size() > 0) {

						for (int i = 0; i < listparters.size(); i++) {
							String string = listparters.get(i)
									.getSpeaker_name();

							if (string.startsWith(txt)
									|| string.startsWith(txt.toLowerCase(Locale
											.getDefault()))
									|| string.endsWith(txt.toUpperCase(Locale
											.getDefault()))
									|| string.indexOf(txt) != -1) {

								listSearch.add(listparters.get(i));
							}
						}
					}

					if ("".equals(txt)) {
						listSearch = listparters;
					}
					// getDatalist();
					setListAdapter(new SpeakersAdapter(getActivity(),
							CONTANT.participants, listSearch));
				}
			}
		});
		beginthread();
	}

	protected int binSearch(List<String[]> list2, String s) {
		// TODO Auto-generated method stub
		for (int i = 0; i < list.size(); i++) {
			if (s.endsWith(list.get(i)[0])) {
				return Integer.parseInt(list.get(i)[1]);
			}
		}
		return -1;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setTitle(R.string.meetpeople);
		search.setText("");
		getLeft_bt().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				hide();
				((HTMainActivity) getActivity()).back();
			}
		});
		MobclickAgent.onPageStart(this.getClass().getName());
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		hide();
		MobclickAgent.onPageEnd(this.getClass().getName());
	}

	public void beginthread() {
		new Thread() {
			public void run() {
				initData();
			}
		}.start();
	}

	@Override
	public void onListItemClick(ListView lv, View v, int position, long id) {

		Bundle b = new Bundle();
		b.putString("fragment", "PartivipantsListFragment");
		;
		b.putInt("speaker_id", CONTANT.listspart.get(position).getSpeakerid());
		mListener.onArticleSelected(b);

		((HTMainActivity) getActivity()).switchContent(
				new UserDetailListFragment(), 2);
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
	private void UIupdata() {
		// TODO Auto-generated method stub
		// 判断如果新闻的长度大于10，有更多，如果欣慰的长度小于10，没有更多
		if (datas == null) {
			return;
		}
		listparters = new ArrayList<SpeakersContant>();
		listSearch = new ArrayList<SpeakersContant>();
		Map<String, Object> maps = (Map<String, Object>) datas;
		// System.out.println("内容是   "+map.get("content"));
		Object[] menu = (Object[]) maps.get("content");

		SpeakersContant speakersContant;

		for (int i = 0; i < menu.length; i++) {
			speakersContant = new SpeakersContant();
			Map<String, Object> map = (Map<String, Object>) menu[i];

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

			listparters.add(speakersContant);
			listSearch.add(speakersContant);
		}

		Collections.sort(listparters);
		Collections.sort(listSearch);
		CONTANT.listspart = null;
		CONTANT.listspart = listparters;

		getDatalist();
		adapter = new SpeakersAdapter(getActivity(), CONTANT.participants,
				listSearch);

		removeProgressView();
		setListAdapter(adapter);

		ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity(),
				R.layout.textview, py);
		speedListView.setAdapter(adapter1);
	}

	private void getDatalist() {
		// TODO Auto-generated method stub
		list = new ArrayList<String[]>();
		for (int i = 0; i < listparters.size(); i++) {

			int idx = i - 1;
			// 判断前后Item是否匹配，如果不匹配则设置并显示，匹配则取消
			char previewChar;
			char currentChar;
			if (ActivityUtils.getLang().equals("zh")) {
				previewChar = idx >= 0 ? ActivityUtils.getPinYinHeadChar(
						listSearch.get(idx).getSpeaker_name()).charAt(0) : ' ';
				currentChar = ActivityUtils.getPinYinHeadChar(
						listSearch.get(i).getSpeaker_name()).charAt(0);
			} else {

				previewChar = idx >= 0 ? listSearch.get(idx).getSpeaker_name()
						.charAt(0) : ' ';
				currentChar = listSearch.get(i).getSpeaker_name().charAt(0);
			}
			// 将小写字符转换为大写字符
			char newPreviewChar = Character.toUpperCase(previewChar);
			char newCurrentChar = Character.toUpperCase(currentChar);

			if (newCurrentChar != newPreviewChar) {
				String[] arr = { String.valueOf(newCurrentChar), i + "" };
				list.add(arr);
			}
		}

	}

	private void initData() {
		// TODO Auto-generated method stub
		datas = ActivityUtils.parse(getActivity(), CONTANT.participants);
		System.out.print("datas= " + datas);
		h.sendEmptyMessage(0);
	}

	@SuppressWarnings("static-access")
	private void hide() {
		InputMethodManager imm = (InputMethodManager) getActivity()
				.getSystemService(getActivity().INPUT_METHOD_SERVICE);
		boolean isOpen = imm.isActive();

		if (isOpen) {
			imm.hideSoftInputFromWindow(getActivity().getCurrentFocus()
					.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}
}
