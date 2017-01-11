#!/bin/bash

SCRIPT_DIR=$(cd $(dirname $BASH_SOURCE) && pwd)

export ANDROID_NDK=/opt/android-ndk-r11b
export OpenCV_DIR=/home/jianjun/workspace/repos/face/face/opencv-android-arm/sdk/native/jni

cmake -DCMAKE_TOOLCHAIN_FILE=${OpenCV_DIR}/android.toolchain.cmake \
      -DCMAKE_C_FLAGS="-pie -fPIE" \
      -DCMAKE_CXX_FLAGS="-pie -fPIE" \
       $@

