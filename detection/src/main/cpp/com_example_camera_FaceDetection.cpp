#include "com_example_camera_FaceDetection.h"
#include "bitmap_utils.h"
#include <opencv2/core/core.hpp>
#include <opencv2/imgproc/imgproc.hpp>
#include <opencv2/objdetect/objdetect.hpp>
#include <opencv2/highgui/highgui.hpp>

void Java_com_example_camera_FaceDetection_detectFace(
        JNIEnv *env, jclass cls, jstring path, jobject bitmapIn, jobject bitmapOut)
{
    cv::Mat matIn;
    Java_org_opencv_android_Utils_nBitmapToMat(env, cls, bitmapIn, jlong(&matIn));
    LOGD("the size of the matrix is w=%d, h=%d", matIn.cols, matIn.rows);

    //cv::cvtColor(matIn, matIn, CV_RGBA2BGR);

    cv::CascadeClassifier classifier;
    const char *str = env->GetStringUTFChars(path, 0);
    LOGD("the file path is %s", str);
    classifier.load(str);
    if (classifier.empty()) {
        LOGD("cannot read xml file");
    }
    env->ReleaseStringUTFChars(path, str);
    std::vector<cv::Rect> rects;

    cv::Mat grayMatIn;
    cv::cvtColor(matIn, grayMatIn, CV_RGB2GRAY);

    //float ratio = 480./grayMatIn.cols;
    //cv::Size size(grayMatIn.cols*ratio, grayMatIn.rows*ratio);
    //cv::resize(grayMatIn, grayMatIn, size);

    classifier.detectMultiScale(grayMatIn, rects);

    LOGD("the detector found %d faces", rects.size());

    cv::Mat matOut = matIn.clone();

    for (int i=0; i<rects.size(); i++) {
        cv::rectangle(matOut, rects[i], CV_RGB(255, 0, 0));
    }

    cv::imwrite("/sdcard/test-facedetection.jpg", matOut);
    Java_org_opencv_android_Utils_nMatToBitmap(env, cls, jlong(&matOut), bitmapOut);
}
