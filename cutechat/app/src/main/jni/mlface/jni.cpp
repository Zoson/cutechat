#include <jni.h>
#include <cstddef>
#include "MLFace.h"
extern "C"
{
	JNIEXPORT jintArray JNICALL Java_com_druson_cycle_service_mlface_MLFace_handleFacePoint(JNIEnv* env,jobject jo,jintArray face_points)
	{
		jintArray arr = env->NewIntArray(5);
        jint *out = env->GetIntArrayElements(arr, NULL);



        env->ReleaseIntArrayElements(arr, out, 0);
        return arr;
	}
    
    JNIEXPORT jfloat JNICALL Java_com_druson_cycle_service_mlface_MLFace_testSvm(JNIEnv* env,jobject jo)
    {
        return svm();
    }




}