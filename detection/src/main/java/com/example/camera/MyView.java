package com.example.camera;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by cpxiao on 15/9/19.
 */
public class MyView extends View {
	private Paint mPaint;

	/**
	 * 重写构造方法
	 */
	public MyView(Context context) {
		super(context);
		init();
	}

	public MyView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public MyView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	private void init() {
		//设置焦点
		setFocusable(true);
		mPaint = new Paint();
		mPaint.setColor(Color.RED);
		mPaint.setTextSize(30);
	}

	/**
	 * 重写绘图方法
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		canvas.drawText("MyView", textX, textY, mPaint);

	}

	int textX = 100;
	int textY = 100;

	/**
	 * 重写按键按下事件的方法
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		//判断按下的是否为方向键（上下左右）
		if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
			textY -= 10;
		} else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
			textY += 10;
		} else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
			textX -= 10;
		} else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
			textX += 10;
		}
		//重绘画布的函数有invalidate()和postInvalidate()，区别在于invalidate()不能在子线程中循环调用，而postInvalidate()可以。
		invalidate();
		//		postInvalidate();
		return super.onKeyDown(keyCode, event);

	}

	/**
	 * 重写按键抬起事件的方法
	 */
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		return super.onKeyUp(keyCode, event);
	}

	/**
	 * 重写触屏事件方法
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int x = (int) event.getX();
		int y = (int) event.getY();
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			textX = x;
			textY = y;
		} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
			textX = x;
			textY = y;
		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			textX = x;
			textY = y;
		}
		invalidate();
		return true;
		//		return super.onTouchEvent(event);
	}
}
