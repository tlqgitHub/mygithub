package com.poqop.document;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.huatandm.meetme.R;
import com.poqop.document.events.CurrentPageListener;
import com.poqop.document.events.DecodingProgressListener;
import com.poqop.document.models.CurrentPageModel;
import com.poqop.document.models.DecodingProgressModel;
import com.poqop.document.models.ZoomModel;
import com.poqop.document.views.PageViewZoomControls;

public abstract class BaseViewerActivity extends Activity implements
		DecodingProgressListener, CurrentPageListener {
	private static final int MENU_EXIT = 0;// �˳�
	private static final int MENU_GOTO = 1;// ��ת
	private static final int MENU_FULL_SCREEN = 2;// ȫ��
	private static final int DIALOG_GOTO = 0;// ��ת
	private static final String DOCUMENT_VIEW_STATE_PREFERENCES = "DjvuDocumentViewState";
	private DecodeService decodeService;
	private DocumentView documentView;
	private ViewerPreferences viewerPreferences;
	private Toast pageNumberToast;// �Զ���Toast
	private CurrentPageModel currentPageModel;

	private String pdf_name;

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initDecodeService();

		pdf_name = getIntent().getStringExtra("pdf_name");
		final ZoomModel zoomModel = new ZoomModel();
		final DecodingProgressModel progressModel = new DecodingProgressModel();
		progressModel.addEventListener(this);
		currentPageModel = new CurrentPageModel();
		currentPageModel.addEventListener(this);
		documentView = new DocumentView(this, zoomModel, progressModel,
				currentPageModel);
		zoomModel.addEventListener(documentView);
		documentView.setLayoutParams(new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT,
				ViewGroup.LayoutParams.FILL_PARENT));
		decodeService.setContentResolver(getContentResolver());
		decodeService.setContainerView(documentView);
		documentView.setDecodeService(decodeService);
		decodeService.open(getIntent().getData());

		viewerPreferences = new ViewerPreferences(this);

		final FrameLayout frameLayout = createMainContainer();
		frameLayout.addView(documentView);
		frameLayout.addView(createZoomControls(zoomModel));
		setFullScreen();
		setContentView(frameLayout);

		final SharedPreferences sharedPreferences = getSharedPreferences(
				DOCUMENT_VIEW_STATE_PREFERENCES, 0);
		documentView.goToPage(sharedPreferences.getInt(getIntent().getData()
				.toString(), 0));
		documentView.showDocument();

		viewerPreferences.addRecent(getIntent().getData());
	}

	/**
	 * �������
	 */
	public void decodingProgressChanged(final int currentlyDecoding) {
		runOnUiThread(new Runnable() {
			public void run() {
				// getWindow().setFeatureInt(Window.FEATURE_INDETERMINATE_PROGRESS,
				// currentlyDecoding == 0 ? 10000 : currentlyDecoding);
			}
		});
	}

	/**
	 * ��ҳ�滬����һҳʱToast��ʾ��ǰҳ��
	 */
	@SuppressLint("ShowToast")
	public void currentPageChanged(int pageIndex) {
		final String pageText = (pageIndex + 1) + "/"
				+ decodeService.getPageCount();
		if (pageNumberToast != null) {
			pageNumberToast.setText(pageText);
		} else {
			pageNumberToast = Toast.makeText(this, pageText, 150);
		}
		// pageNumberToast.setGravity(Gravity.TOP | Gravity.LEFT,0,0);
		pageNumberToast.show();
		saveCurrentPage();
	}

	/**
	 * ����Title��ʾ���ݵ�ֵ
	 */
	private void setWindowTitle() {
		// final String name = getIntent().getData().getLastPathSegment();

		getWindow().setTitle(pdf_name);
	}

	/**
	 * ��ʾTitle
	 */
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		setWindowTitle();
	}

	/**
	 * ����ȫ��
	 */
	private void setFullScreen() {
		if (viewerPreferences.isFullScreen()) {
			getWindow().requestFeature(Window.FEATURE_NO_TITLE);
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
					WindowManager.LayoutParams.FLAG_FULLSCREEN);
		} else {
			getWindow().requestFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		}
	}

	/**
	 * ��������ͼƬ��С������
	 * 
	 * @param zoomModel
	 * @return
	 */
	private PageViewZoomControls createZoomControls(ZoomModel zoomModel) {
		final PageViewZoomControls controls = new PageViewZoomControls(this,
				zoomModel);
		controls.setGravity(Gravity.RIGHT | Gravity.BOTTOM);
		zoomModel.addEventListener(controls);
		return controls;
	}

	/**
	 * ����һ�����ý����ͼƬ������
	 * 
	 * @return
	 */
	private FrameLayout createMainContainer() {
		return new FrameLayout(this);
	}

	/**
	 * ��ʼ���������
	 */
	private void initDecodeService() {
		if (decodeService == null) {
			decodeService = createDecodeService();
		}
	}

	protected abstract DecodeService createDecodeService();

	/**
	 * ��Activityֹͣʱ
	 */
	@Override
	protected void onStop() {
		super.onStop();
	}

	/**
	 * Activity����ʱ
	 */
	@Override
	protected void onDestroy() {
		decodeService.recycle();
		decodeService = null;
		super.onDestroy();
	}

	/**
	 * ���浱ǰҳ
	 */
	private void saveCurrentPage() {
		final SharedPreferences sharedPreferences = getSharedPreferences(
				DOCUMENT_VIEW_STATE_PREFERENCES, 0);
		final SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putInt(getIntent().getData().toString(),
				documentView.getCurrentPage());
		editor.commit();
	}

	/**
	 * ѡ�ť
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
//		menu.add(0, MENU_EXIT, 0, "�˳�");
//		menu.add(0, MENU_GOTO, 0, "��תҳ��");
		final MenuItem menuItem = menu
				.add(0, MENU_FULL_SCREEN, 0, "Full screen").setCheckable(true)
				.setChecked(viewerPreferences.isFullScreen());
		setFullScreenMenuItemText(menuItem);
		return true;
	}

	private void setFullScreenMenuItemText(MenuItem menuItem) {
		menuItem.setTitle(getString(R.string.isFullScreen) + (menuItem.isChecked() ? "("+getString(R.string.yesfullScreen)+")" : "("+getString(R.string.noFullScreen)+")"));
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU_EXIT:
			finish();
			return true;
		case MENU_GOTO:
			showDialog(DIALOG_GOTO);
			return true;
		case MENU_FULL_SCREEN:
			item.setChecked(!item.isChecked());
			setFullScreenMenuItemText(item);
			viewerPreferences.setFullScreen(item.isChecked());

			finish();
			startActivity(getIntent());
			return true;
		}
		return false;
	}

	/**
	 * ��תҳ��
	 */
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_GOTO:
			return new GoToPageDialog(this, documentView, decodeService);
		}
		return null;
	}

	/**
	 * ����HOME��
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub

		if (KeyEvent.KEYCODE_HOME == keyCode) {

			/*
			 * String path = Global.DATABASE_PATH + "/" +
			 * Global.DATABASE_FILENAME_JM; if(new File(path).exists()){ new
			 * File(path).delete(); } ExitApplication.getInstance().exit();
			 */
			return false;

		}

		return super.onKeyDown(keyCode, event);
	}

	/*
	 * public void onAttachedToWindow() {
	 * this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD);
	 * super.onAttachedToWindow(); }
	 */
}