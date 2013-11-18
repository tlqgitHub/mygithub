package com.poqop.document;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextPaint;

class Page {
	final int index;
	RectF bounds;
	private PageTreeNode node;// ҳ��ڵ�
	private DocumentView documentView;// �ĵ���ͼ
	private final TextPaint textPaint = textPaint();// ���ֻ���
	private final Paint fillPaint = fillPaint();// ȫ������
	private final Paint strokePaint = strokePaint();// ����ʱ����

	Page(DocumentView documentView, int index) {
		this.documentView = documentView;
		this.index = index;
		node = new PageTreeNode(documentView, new RectF(0, 0, 1, 1), this, 1,
				null);
	}

	private float aspectRatio;

	float getPageHeight(int mainWidth, float zoom) {
		return mainWidth / getAspectRatio() * zoom;
	}

	public int getTop() {
		return Math.round(bounds.top);
	}

	/**
	 * ����ҳ��
	 * 
	 * @param canvas
	 */
	public void draw(Canvas canvas) {
		if (!isVisible()) {
			return;
		}
		canvas.drawRect(bounds, fillPaint);

		canvas.drawText((index + 1) + "ҳ", bounds.centerX(), bounds.centerY(),
				textPaint);
		node.draw(canvas);
		canvas.drawLine(bounds.left, bounds.top, bounds.right, bounds.top,
				strokePaint);
		canvas.drawLine(bounds.left, bounds.bottom, bounds.right,
				bounds.bottom, strokePaint);
	}

	/**
	 * ���û�������
	 * 
	 * @return
	 */
	private Paint strokePaint() {
		final Paint strokePaint = new Paint();
		strokePaint.setColor(Color.BLACK);
		strokePaint.setStyle(Paint.Style.STROKE);
		strokePaint.setStrokeWidth(2);
		return strokePaint;
	}

	/**
	 * ����ȫ������
	 * 
	 * @return
	 */
	private Paint fillPaint() {
		final Paint fillPaint = new Paint();
		fillPaint.setColor(Color.GRAY);
		fillPaint.setStyle(Paint.Style.FILL);
		return fillPaint;
	}

	/**
	 * �������ֻ���
	 * 
	 * @return
	 */
	private TextPaint textPaint() {
		final TextPaint paint = new TextPaint();
		paint.setColor(Color.BLACK);
		paint.setAntiAlias(true);
		paint.setTextSize(24);
		paint.setTextAlign(Paint.Align.CENTER);
		return paint;
	}

	public float getAspectRatio() {
		return aspectRatio;
	}

	public void setAspectRatio(float aspectRatio) {
		if (this.aspectRatio != aspectRatio) {
			this.aspectRatio = aspectRatio;
			documentView.invalidatePageSizes();
		}
	}

	/**
	 * �Ƿ���ʾ
	 * 
	 * @return
	 */
	public boolean isVisible() {
		return RectF.intersects(documentView.getViewRect(), bounds);
	}

	/**
	 * ���ó����
	 * 
	 * @param width
	 * @param height
	 */
	public void setAspectRatio(int width, int height) {
		setAspectRatio(width * 1.0f / height);
	}

	/**
	 * ���ý���
	 * 
	 * @param pageBounds
	 */
	void setBounds(RectF pageBounds) {
		bounds = pageBounds;
		node.invalidateNodeBounds();
	}

	/**
	 * ������ͼ
	 */
	public void updateVisibility() {
		node.updateVisibility();
	}

	/**
	 * ֹͣʱ���÷���
	 */
	public void invalidate() {
		node.invalidate();
	}
}
