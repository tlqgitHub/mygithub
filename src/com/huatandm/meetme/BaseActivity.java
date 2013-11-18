package com.huatandm.meetme;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

/**
 * @ClassName: BaseActivity
 * @Description: activity ����
 * @author tlq
 * @date 2013-9-9 ����5:14:50
 * 
 */
public class BaseActivity extends SlidingFragmentActivity {

	// Progress
	protected ProgressDialog m_dialogProgress;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// �������������
		SlidingMenu sm = getSlidingMenu();
		sm.setShadowWidthRes(R.dimen.shadow_width);
		sm.setShadowDrawable(R.drawable.shadow);
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		sm.setFadeDegree(0.35f);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
	}

	// add Dialog
	public void addProgressView(Context context) {
		m_dialogProgress = ProgressDialog.show(context, "", getResources()
				.getString(R.string.loading), true, false);
		m_dialogProgress.show();
	}

	// move Dialog
	public void removeProgressView() {
		if (m_dialogProgress != null) {
			m_dialogProgress.dismiss();
		}
	}
}
