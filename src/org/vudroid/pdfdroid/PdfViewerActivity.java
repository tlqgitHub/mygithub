package org.vudroid.pdfdroid;

import com.poqop.document.BaseViewerActivity;
import com.poqop.document.DecodeService;
import com.poqop.document.DecodeServiceBase;
import com.umeng.analytics.MobclickAgent;

import org.vudroid.pdfdroid.codec.PdfContext;

public class PdfViewerActivity extends BaseViewerActivity {
	@Override
	protected DecodeService createDecodeService() {
		return new DecodeServiceBase(new PdfContext());
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
}
