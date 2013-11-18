package com.poqop.document;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextPaint;

class Page {
	final int index;
	RectF bounds;
	private PageTreeNode node;// 页面节点
	private DocumentView documentView;// 文档视图
	private final TextPaint textPaint = textPaint();// 文字画笔
	private final Paint fillPaint = fillPaint();// 全屏画笔
	private final Paint strokePaint = strokePaint();// 划动时画笔

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
	 * 绘制页面
	 * 
	 * @param canvas
	 */
	public void draw(Canvas canvas) {
		if (!isVisible()) {
			return;
		}
		canvas.drawRect(bounds, fillPaint);

		canvas.drawText((index + 1) + "页", bounds.centerX(), bounds.centerY(),
				textPaint);
		node.draw(canvas);
		canvas.drawLine(bounds.left, bounds.top, bounds.right, bounds.top,
				strokePaint);
		canvas.drawLine(bounds.left, bounds.bottom, bounds.right,
				bounds.bottom, strokePaint);
	}

	/**
	 * 设置划动画笔
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
	 * 设置全屏画笔
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
	 * 设置文字画笔
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
	 * 是否显示
	 * 
	 * @return
	 */
	public boolean isVisible() {
		return RectF.intersects(documentView.getViewRect(), bounds);
	}

	/**
	 * 设置长宽比
	 * 
	 * @param width
	 * @param height
	 */
	public void setAspectRatio(int width, int height) {
		setAspectRatio(width * 1.0f / height);
	}

	/**
	 * 设置界限
	 * 
	 * @param pageBounds
	 */
	void setBounds(RectF pageBounds) {
		bounds = pageBounds;
		node.invalidateNodeBounds();
	}

	/**
	 * 更新视图
	 */
	public void updateVisibility() {
		node.updateVisibility();
	}

	/**
	 * 停止时调用方法
	 */
	public void invalidate() {
		node.invalidate();
	}
}
