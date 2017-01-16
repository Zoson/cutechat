#include "jni.h"
#include <android/log.h>
#include "bitmaputils.h"
#include <cstddef>
#include "time.h"
#define APP_NAME "cutechat"

extern "C"
{
    JNIEXPORT jintArray JNICALL Java_com_druson_cycle_utils_BitmapUtils_stackBlur(JNIEnv *env,jclass jclazz,jintArray arr,jint w,jint h,jint radius)
    {
    	clock_t StartTime = clock();
        jint *out = env->GetIntArrayElements(arr, NULL);
        stackBlur(out,w,h,radius);
        double TotalAsmTime = double(clock() - StartTime) / CLOCKS_PER_SEC;
    	__android_log_print(ANDROID_LOG_ERROR, APP_NAME,
    		"bitmaputils use time  Img w and h :%dx%d ---> Time:%.3f secs.", w, h, TotalAsmTime);
        return arr;
    }
}