LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := bitmaputils

LOCAL_SRC_FILES := jni.cpp bitmaputils.cpp

LOCAL_LDLIBS +=  -llog -ldl
                                            
include $(BUILD_SHARED_LIBRARY)