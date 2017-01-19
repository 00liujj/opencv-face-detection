package com.example.camera;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

/**
 * Created by cpxiao on 15/9/19.
 */
public class MySurfaceView extends SurfaceView {

    //SurfaceHolder用于控制SurfaceView的大小、格式等，用于监听SurfaceView的状态。
    private SurfaceHolder mSurfaceHolder;
    private Paint mPaint;

    //初始化文本坐标
    private int textX = 100;
    private int textY = 100;

    public MySurfaceView(Context context) {
        super(context);
        init();
    }

    public MySurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MySurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        //实例SurfaceHolder
        Log.d(MainActivity.TAG, "Init My surface view");
        mSurfaceHolder = getHolder();
        //为SurfaceView添加状态监听
        mSurfaceHolder.addCallback(new MyCallback2());
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        //实例一个画笔
        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setTextSize(30);
    }

    class MyCallback implements SurfaceHolder.Callback {
        /**
         * 重写SurfaceHolder.Callback接口的三个方法surfaceCreated()、surfaceChanged()、surfaceDestroyed()
         */

        /**
         * 当SurfaceView被创建完成后响应的方法
         */
        @Override
        public void surfaceCreated(SurfaceHolder surfaceHolder) {
            myDraw();
        }

        /**
         * 当SurfaceView状态发生改变时响应的方法
         */
        @Override
        public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

        }

        /**
         * 当SurfaceView状态Destroyed时响应的方法
         */
        @Override
        public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

        }
    }

    class MyCallback2 implements SurfaceHolder.Callback {

        Camera camera;

        @Override
        public void surfaceCreated(SurfaceHolder surfaceHolder) {
            camera = Camera.open();
            if (camera == null) {
                Log.d(MainActivity.TAG, "the camera is null");
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
            Camera.Parameters param = camera.getParameters();
            param.setPreviewSize(i1, i2);
            //camera.setParameters(param);
            try {
                camera.setPreviewDisplay(mSurfaceHolder);
            } catch (IOException e) {
                camera.release();
                camera = null;
            }
            camera.startPreview();
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
            camera.release();
        }
    }

    /**
     * 自定义绘图方法
     */
    private void myDraw() {
        //使用SurfaceHolder.lockCanvas()获取SurfaceView的Canvas对象，并对画布加锁.
        Canvas canvas = mSurfaceHolder.lockCanvas();
        //得到自定义大小的画布，因为局部绘制，效率更高
        //      Canvas canvas = mSurfaceHolder.lockCanvas(new Rect(0,0,200,200));

        /**
         * 在绘制之前需要将画布清空，否则画布上会显示之前绘制的内容,以下三种方法效果一致*/
        canvas.drawRect(0,0,getWidth(),getHeight(),new Paint());
        canvas.drawColor(Color.WHITE);
        canvas.drawRGB(255, 255, 255);

        //通过在Canvas上绘制内容来修改SurfaceView中的数据
        canvas.drawText("mySurfaceView", textX, textY, mPaint);
        //canvas.setBitmap();
        //用于解锁画布和提交
        mSurfaceHolder.unlockCanvasAndPost(canvas);
    }

    /**
     * 重写触屏监听方法
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        textX = (int) event.getX();
        textY = (int) event.getY();
        myDraw();
        return true;
//      return super.onTouchEvent(event);
    }
}

