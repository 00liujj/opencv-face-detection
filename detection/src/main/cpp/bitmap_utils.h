#pragma once

#include <jni.h>


#define LOG_TAG "FaceDetection"

#include <android/log.h>
#define LOGE(...) ((void)__android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__))
#ifndef NDEBUG
#  define LOGD(...) ((void)__android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__))
#else
#  define LOGD(...)
#endif

#ifdef __cplusplus
extern "C" {
#endif

/*
 * Class:     org_opencv_android_Utils
 * Method:    void nBitmapToMat2(Bitmap b, long m_addr, boolean unPremultiplyAlpha)
 */

JNIEXPORT void JNICALL Java_org_opencv_android_Utils_nBitmapToMat2
  (JNIEnv * env, jclass, jobject bitmap, jlong m_addr, jboolean needUnPremultiplyAlpha);


// old signature is left for binary compatibility with 2.4.0 & 2.4.1, to removed in 2.5
JNIEXPORT void JNICALL Java_org_opencv_android_Utils_nBitmapToMat
  (JNIEnv * env, jclass, jobject bitmap, jlong m_addr);

/*
 * Class:     org_opencv_android_Utils
 * Method:    void nMatToBitmap2(long m_addr, Bitmap b, boolean premultiplyAlpha)
 */

JNIEXPORT void JNICALL Java_org_opencv_android_Utils_nMatToBitmap2
  (JNIEnv * env, jclass, jlong m_addr, jobject bitmap, jboolean needPremultiplyAlpha);

// old signature is left for binary compatibility with 2.4.0 & 2.4.1, to removed in 2.5
JNIEXPORT void JNICALL Java_org_opencv_android_Utils_nMatToBitmap
  (JNIEnv * env, jclass, jlong m_addr, jobject bitmap);

#ifdef __cplusplus
}
#endif

