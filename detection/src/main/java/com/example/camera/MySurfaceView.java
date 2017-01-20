package com.example.camera;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Paint;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.List;

import android.os.SystemClock;

/**
 * Created by cpxiao on 15/9/19.
 */
public class MySurfaceView extends SurfaceView {
    private static final String TAG = "detection.MySurfaceView";

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
        Log.d(TAG, "Init My surface view");
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

        Camera mCamera = null;
        boolean mPreview = false;
        byte[] mBuffer = null;

        @Override
        public void surfaceCreated(SurfaceHolder surfaceHolder) {
            Log.d(TAG, "surfaceCreated");

            mCamera = Camera.open();
            if (mCamera != null) {
                Log.d(TAG, "Camera opened");
            } else {
                Log.d(TAG, "failed to open Camera");
                return;
            }
            int surfaceWidth = surfaceHolder.getSurfaceFrame().width();
            int surfaceHeight = surfaceHolder.getSurfaceFrame().height();
            Camera.Parameters params = mCamera.getParameters();
            Log.d(TAG, "getSupportedPreviewSizes()");
            List<Camera.Size> supportedSizes = params.getSupportedPreviewSizes();

            if (supportedSizes != null) {
                /* Select the size that fits surface considering maximum size allowed */
                int calcWidth = 0;
                int calcHeight = 0;

                for (Camera.Size size : supportedSizes) {
                    int width = size.width;
                    int height = size.height;

                    if (width <= surfaceWidth && height <= surfaceHeight) {
                        if (width >= calcWidth && height >= calcHeight) {
                            calcWidth = width;
                            calcHeight = height;
                        }
                    }
                }
                Log.d(TAG, "Set mPreview size to " + calcWidth + "x" + calcHeight);
                params.setPreviewSize(calcWidth, calcHeight);
            }

            List<String> focusModes = params.getSupportedFocusModes();
            if (focusModes != null && focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO)) {
                params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
            }
            params.setPreviewFormat(ImageFormat.NV21);
            //params.setPreviewFrameRate(5);

            mCamera.setParameters(params);

            mCamera.setDisplayOrientation(90);



            try {
                //mCamera.setPreviewDisplay(null);
                mCamera.setPreviewDisplay(mSurfaceHolder);
            } catch (IOException e) {
                mCamera.release();
                mCamera = null;
            }

            params = mCamera.getParameters();

            int mFrameWidth = params.getPreviewSize().width;
            int mFrameHeight = params.getPreviewSize().height;

            int size = mFrameWidth * mFrameHeight;
            size = size * ImageFormat.getBitsPerPixel(params.getPreviewFormat()) / 8;
            Log.d(TAG, String.format("need %d bytes", size));
            mBuffer = new byte[size];

            mCamera.addCallbackBuffer(mBuffer);
            mCamera.setPreviewCallbackWithBuffer(new Camera.PreviewCallback() {
            //mCamera.setPreviewCallback(new Camera.PreviewCallback() {
                @Override
                public void onPreviewFrame(byte[] bytes, Camera camera) {
                    Log.d(TAG, "onPreviewFrame received " + bytes.length);
                    Log.d(TAG, "mCamera is " + mCamera + ", camera is " + camera);
                    SystemClock.sleep(3000);
                    // 这个buffer会保留一段时间。直到下一次调用。
                    if (camera != null) {
                        camera.addCallbackBuffer(mBuffer);
                    }
                }
            });


            /* Finally we are ready to start the mPreview */
            Log.d(TAG, "startPreview");
            mCamera.startPreview();
            mPreview = true;
        }

        @Override
        public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int surfaceWidth, int surfaceHeight) {
            Log.d(TAG,"surfaceChanged");
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
            Log.d(TAG,"surfaceDestroyed");
            if (mCamera != null) {
                if (mPreview) {
                    mCamera.stopPreview();
                    mPreview = false;
                }
                mCamera.setPreviewCallback(null);
                mCamera.release();
                mCamera = null;
            }
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
        //Log.d(TAG,"onTouchEvent");
        textX = (int) event.getX();
        textY = (int) event.getY();
        //myDraw();
        return true;
//      return super.onTouchEvent(event);
    }
}

