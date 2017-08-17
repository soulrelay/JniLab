#include <jni.h>

JNIEXPORT jstring JNICALL
Java_com_sus_jnilab_utils_JniManager_getInfo(JNIEnv *env, jobject instance) {

    char *str = "jni lab sail";

    return (*env)->NewStringUTF(env, str);
}

