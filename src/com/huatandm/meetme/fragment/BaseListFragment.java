package com.huatandm.meetme.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.huatandm.meetme.HTMainActivity;
import com.huatandm.meetme.R;

/**
 * @ClassName: BaseListFragment
 * @Description: 子Fragment 界面的基类
 * @author tlq
 * @date 2013-9-9 下午5:18:10
 * 
 */
public class BaseListFragment extends ListFragment {

	protected OnArticleSelectedListener mListener;

	protected Object datas;

	protected SharedPreferences sp;
	protected Bundle bundle;//
	// Progress
	protected ProgressDialog m_dialogProgress;
	String sLoading;

	private Button left_bt;
	private Button right_bt;
	private ImageView right_view;
	// News
	private Button un_bt_n;
	private Button re_bt_n;
	// Events
	private Button un_bt_a;
	private Button re_bt_a;
	// title
	TextView title;

	// Container Activity must implement this interface
	public interface OnArticleSelectedListener {
		public void onArticleSelected(Object object);

		public void onBack(Object object);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.i("TAG", "---------------------" + "onResume");
		getRight_view().setVisibility(View.GONE);
		getRight_bt().setVisibility(View.GONE);

		getLeft_bt().setBackgroundResource(R.drawable.back);
		left_bt.setVisibility(View.VISIBLE);
		left_bt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				((HTMainActivity) getActivity()).back();
			}
		});

		// News
		getUn_bt_n().setVisibility(View.GONE);
		getRe_bt_n().setVisibility(View.GONE);
		// Events
		getUn_bt_a().setVisibility(View.GONE);
		getRe_bt_a().setVisibility(View.GONE);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (OnArticleSelectedListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implementOnArticleSelectedListener");
		}
	}

	public void onSaveBundleState(Bundle outState) {
		// TODO Auto-generated method stub
		Log.i("TAG", "---------------------" + "onSaveInstanceState");
	}

	@SuppressWarnings("static-access")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		datas = null;
		sp = getActivity().getSharedPreferences("login",
				getActivity().MODE_PRIVATE);
		sLoading = getActivity().getResources().getString(R.string.loading);
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.i("TAG", "---------------------" + "onCreateView");
		return inflater.inflate(R.layout.list, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		bundle = ((HTMainActivity) getActivity()).getBundle_data();
		Log.i("TAG", "---------------------" + "onActivityCreated");
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		Log.i("TAG", "---------------------" + "onDestroyView");
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		Log.i("TAG", "---------------------" + "onViewCreated");
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.i("TAG", "---------------------" + "onDestroy");
	}

	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
		Log.i("TAG", "---------------------" + "onDetach");
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.i("TAG", "---------------------" + "onPause");
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Log.i("TAG", "---------------------" + "onStart");
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.i("TAG", "---------------------" + "onStop");
	}

	public void addProgressView(Context context) {
		if (null == m_dialogProgress)
			m_dialogProgress = ProgressDialog.show(context, "", sLoading, true,
					false);

		m_dialogProgress.show();
	}

	public void removeProgressView() {
		if (m_dialogProgress != null) {
			m_dialogProgress.dismiss();
		}
	}

	public TextView getTitle() {
		title = (TextView) getActivity().findViewById(R.id.text);
		return title;
	}

	public void setTitle(int resid) {
		title = (TextView) getActivity().findViewById(R.id.text);
		title.setText(resid);
	}

	public void setTitle(String resid) {
		title = (TextView) getActivity().findViewById(R.id.text);
		title.setText(resid);
	}

	public void setTitle(TextView title) {
		this.title = title;
	}

	public Button getUn_bt_n() {
		un_bt_n = (Button) getActivity().findViewById(R.id.undo_button);
		return un_bt_n;
	}

	public void setUn_bt_n(Button un_bt_n) {
		this.un_bt_n = un_bt_n;
	}

	public Button getRe_bt_n() {
		re_bt_n = (Button) getActivity().findViewById(R.id.redo_button);
		return re_bt_n;
	}

	public void setRe_bt_n(Button re_bt_n) {
		this.re_bt_n = re_bt_n;
	}

	public Button getUn_bt_a() {
		un_bt_a = (Button) getActivity().findViewById(R.id.undo_but);
		return un_bt_a;
	}

	public void setUn_bt_a(Button un_bt_a) {
		this.un_bt_a = un_bt_a;
	}

	public Button getRe_bt_a() {
		re_bt_a = (Button) getActivity().findViewById(R.id.redo_but);
		return re_bt_a;
	}

	public void setRe_bt_a(Button re_bt_a) {
		this.re_bt_a = re_bt_a;
	}

	public Button getLeft_bt() {
		left_bt = (Button) getActivity().findViewById(R.id.left_button);
		return left_bt;
	}

	public void setLeft_bt(Button left_bt) {
		this.left_bt = left_bt;
	}

	public Button getRight_bt() {
		right_bt = (Button) getActivity().findViewById(R.id.right_button);
		return right_bt;
	}

	public void setRight_bt(Button right_bt) {
		this.right_bt = right_bt;
	}

	public ImageView getRight_view() {
		right_view = (ImageView) getActivity().findViewById(
				R.id.right_imageView);
		return right_view;
	}

	public void setRight_view(ImageView right_view) {
		this.right_view = right_view;
	}
}
