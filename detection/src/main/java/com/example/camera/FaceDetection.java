package com.example.camera;

/**
 * Created by jianjun on 1/11/17.
 */

public class FaceDetection {
    static {
        System.loadLibrary("face");
    }
    public native static void detectFace(String path, Object bitmapIn, Object bitmapOut);
}
