LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

OPENCV_LIB_TYPE:=STATIC
OPENCV_INSTALL_MODULES:=on

#include ../includeOpenCV.mk
ifeq ("$(wildcard $(OPENCV_MK_PATH))","")
	#try to load OpenCV.mk from default install location
	#include $(TOOLCHAIN_PREBUILT_ROOT)/user/share/OpenCV/OpenCV.mk
	include ~/Documents/workspace/OpenCV-android-sdk/sdk/native/jni/OpenCV.mk
else
	include $(OPENCV_MK_PATH)
endif

LOCAL_MODULE    := libfaceidentify
FILE_LIST := $(wildcard $(LOCAL_PATH)/stasm/*.cpp) \
 $(wildcard $(LOCAL_PATH)/stasm/MOD_1/*.cpp) \
 $(wildcard $(LOCAL_PATH)/jnipart/*.cpp)

LOCAL_SRC_FILES := $(FILE_LIST:$(LOCAL_PATH)/%=%)

LOCAL_LDLIBS +=  -llog -ldl

include $(BUILD_SHARED_LIBRARY)
