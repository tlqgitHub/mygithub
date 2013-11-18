package com.huatandm.meetme.fragment.detail;

import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.huatandm.meetme.ActivityUtils;
import com.huatandm.meetme.CONTANT;
import com.huatandm.meetme.HTMainActivity;
import com.huatandm.meetme.R;
import com.huatandm.meetme.fragment.BaseListFragment;
import com.huatandm.meetme.helper.Connector;
import com.umeng.analytics.MobclickAgent;

public class SendMsgListFragment extends BaseListFragment {

	List<Map<String, Object>> messagelist = null;
	Object m_aMessages[];
	String fragmentName;

	String discussIds;
	String sRecipient;
	String account;
	EditText content;
	EditText theme;
	EditText anyone;

	String con;
	String txt;

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setTitle(R.string.mailcompose);

		Bundle bundle = ((HTMainActivity) getActivity()).getBundle_data();
		fragmentName = bundle.getString("fragment");

		if (fragmentName.equals("DiscussListFragment")) {
			discussIds = bundle.getString("discussIds");
		} else {
			sRecipient = bundle.getString("recipient");
			account = bundle.getString("account");
		}

		getListView().setDividerHeight(0);
		setListAdapter(new MyAdapter(getActivity()));
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// show();
		getLeft_bt().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				((HTMainActivity) getActivity()).back();
			}
		});
		getRight_view().setVisibility(View.VISIBLE);
		Button right_bt = getRight_bt();
		right_bt.setBackgroundResource(R.drawable.send);
		right_bt.setVisibility(View.VISIBLE);
		right_bt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (fragmentName.equals("DiscussListFragment")) {
					reloadRemoteData(content.getText().toString());
				} else if (fragmentName.equals("DiscussDetailFragment")) {
					reloadRemoteData(content.getText().toString());
				} else {
					sendMsg(content.getText().toString());
				}
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

	@SuppressWarnings({ "static-access", "unused" })
	private void show() {
		// 界面加载后弹出软键盘 --- 不能弹出软键盘的主要原因是Android程序未将屏幕绘制完成,所以延迟一定时间，弹出软键盘。
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				InputMethodManager imm = (InputMethodManager) getActivity()
						.getSystemService(getActivity().INPUT_METHOD_SERVICE);
				boolean isOpen = imm.isActive();

				if (!isOpen) {
					imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
				}
			}
		}, 1000);
	}

	protected void sendMsg(String string) {
		// TODO Auto-generated method stub

		if (0 == theme.getText().length() || 0 == content.getText().length()) {
			AlertDialog dialog = new AlertDialog.Builder(getActivity())
					.create();
			dialog.setTitle(getString(R.string.msg_error));
			dialog.setMessage(getString(R.string.mail_text_error));
			dialog.setButton(DialogInterface.BUTTON_NEUTRAL,
					getString(R.string.close),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							dialog.dismiss();
						}
					});
			dialog.show();
			return;
		}

		addProgressView(getActivity());
		Connector o = ((HTMainActivity) getActivity()).getConnector();
		String sOptions = "";
		// if (m_radioOptionsMe.isChecked())
		// sOptions = "me";
		// else if (m_radioOptionsRecipient.isChecked())
		// sOptions = "recipient";
		// else if (m_radioOptionsBoth.isChecked())
		// sOptions = "both";

		Object[] aParams = { o.getUsername(), o.getPassword(), account,
				theme.getText().toString(), content.getText().toString(),
				sOptions };

		o.execAsyncMethod("dolphin.sendMessage", aParams,
				new Connector.Callback() {
					@SuppressLint("UseValueOf")
					public void callFinished(Object result) {

						Integer iResult = new Integer(result.toString());
						System.out.println("-------------------------------"
								+ iResult + "");
						String sErrorMsg = "";
						String sTitle = getString(R.string.mail_error);
						switch (iResult) {
						case 1:
							sErrorMsg = getString(R.string.mail_msg_send_failed);
							break;
						case 3:
							sErrorMsg = getString(R.string.mail_wait_before_sendin_another_msg);
							break;
						case 5:
							sErrorMsg = getString(R.string.mail_you_are_blocked);
							break;
						case 10:
							sErrorMsg = getString(R.string.mail_recipient_is_inactive);
							break;
						case 1000:
							sErrorMsg = getString(R.string.mail_unknown_recipient);
							break;
						default:
							sTitle = getString(R.string.mail_success);
							sErrorMsg = getString(R.string.mail_msg_successfully_sent);
						}

						AlertDialog dialog = new AlertDialog.Builder(
								getActivity()).create();
						dialog.setTitle(sTitle);
						dialog.setMessage(sErrorMsg);
						dialog.setButton(DialogInterface.BUTTON_NEUTRAL,
								getString(R.string.close),
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {
										dialog.dismiss();
										h.sendEmptyMessage(0);
									}
								});
						dialog.show();

					}

					@Override
					public boolean callFailed(Exception e) {
						// TODO Auto-generated method stub
						String sTitle = getString(R.string.mail_error);
						String sErrorMsg = getString(R.string.mail_msg_send_failed)
								+ "error:";
						AlertDialog dialog = new AlertDialog.Builder(
								getActivity()).create();
						dialog.setTitle(sTitle);
						dialog.setMessage(sErrorMsg);
						dialog.setButton(DialogInterface.BUTTON_NEUTRAL,
								getString(R.string.close),
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {
										dialog.dismiss();
										h.sendEmptyMessage(0);
									}
								});
						dialog.show();
						return super.callFailed(e);
					}
				}, getActivity());
	}

	public void beginthread() {
		new Thread() {
			public void run() {

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

	protected void reloadRemoteData(String replyText) {

		if (0 == content.getText().length()) {
			AlertDialog dialog = new AlertDialog.Builder(getActivity())
					.create();
			dialog.setTitle(getString(R.string.mail_error));
			dialog.setMessage(getString(R.string.mail_form_error));
			dialog.setButton(DialogInterface.BUTTON_NEUTRAL,
					getString(R.string.close),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							dialog.dismiss();
						}
					});
			dialog.show();
			return;
		}

		addProgressView(getActivity());

		Connector o = ((HTMainActivity) getActivity()).getConnector();
		Object[] aParams = { o.getUsername(), o.getPassword(),
				CONTANT.eventIds, discussIds, replyText,
				ActivityUtils.getLang() };
		o.execAsyncMethod("dolphin.AddDiscussion", aParams,
				new Connector.Callback() {
					public void callFinished(Object result) {

						System.out.println("-------------------------------"
								+ result.toString() + "");

						if (result.toString().contains("failed")
								&& result.toString().contains("0")) {

							AlertDialog dialog = new AlertDialog.Builder(
									getActivity()).create();
							dialog.setTitle(getString(R.string.mail_error));
							dialog.setMessage(getString(R.string.mail_identity_error));
							dialog.setButton(DialogInterface.BUTTON_NEUTRAL,
									getString(R.string.close),
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int whichButton) {
											dialog.dismiss();
											h.sendEmptyMessage(0);
										}
									});
							dialog.show();
						} else {
							h.sendEmptyMessage(0);
						}
					}

					@Override
					public boolean callFailed(Exception e) {
						// TODO Auto-generated method stub
						h.sendEmptyMessage(0);
						return super.callFailed(e);
					}

				}, getActivity());
	}

	private void UIupdata() {
		// TODO Auto-generated method stub
		removeProgressView();
		((HTMainActivity) getActivity()).back();
	}

	public class MyAdapter extends BaseAdapter {

		private LayoutInflater mInflater;
		TextView text1 = null;
		TextView text3 = null;

		View v1;
		View v2;

		public MyAdapter(Context context) {
			this.mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if (fragmentName.equals("DiscussListFragment")) {
				return 1;
			} else if (fragmentName.equals("DiscussDetailFragment")) {
				return 1;
			}
			return 2;
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

			if (theme != null)
				con = theme.getText().toString();
			if (content != null)
				txt = content.getText().toString();

			if (position == 0) {

				if (fragmentName.equals("DiscussListFragment")
						|| fragmentName.equals("DiscussDetailFragment")) {
					if (v1 == null) {
						v1 = mInflater.inflate(
								R.layout.cell_re_msg_body_list_item, null);
						content = (EditText) v1.findViewById(R.id.editText);
					}

					convertView = v1;

					content.requestFocus();

				} else {

					if (v1 == null) {
						v1 = mInflater.inflate(
								R.layout.cell_line_two_title_item, null);
						text1 = (TextView) v1
								.findViewById(R.id.cell_line_textView1);
						anyone = (EditText) v1
								.findViewById(R.id.cell_line_textView2);
						text3 = (TextView) v1
								.findViewById(R.id.cell_line_textView3);
						theme = (EditText) v1
								.findViewById(R.id.cell_line_textView4);
					}
					convertView = v1;
					text3.setText(getText(R.string.mail_compose_subject) + ":");
					anyone.setText(sRecipient);
					theme.setHint(R.string.mail_compose_subject);

					theme.setFocusable(true);
					theme.setFocusableInTouchMode(true);
					if (con != null)
						theme.setText(con);
				}

			} else if (position == 1) {
				if (v2 == null) {
					v2 = mInflater.inflate(R.layout.cell_re_msg_body_list_item,
							null);
					content = (EditText) v2.findViewById(R.id.editText);
				}

				convertView = v2;
				content.requestFocus();

				if (txt != null)
					content.setText(txt);
			}
			return convertView;
		}
	}
}
