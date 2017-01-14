package com.example.camera;

/**
 * Created by jianjun on 1/11/17.
 */

import android.graphics.Bitmap;


public class FaceDetection {
    static {
        System.loadLibrary("face");
    }
    public native static void detectFace(String path, Bitmap bitmapIn, Bitmap bitmapOut);
}
