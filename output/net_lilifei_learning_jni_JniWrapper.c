#include <jni.h>
#include "net_lilifei_learning_jni_JniWrapper.h"

JNIEXPORT jint JNICALL Java_net_lilifei_learning_jni_JniWrapper_add
  (JNIEnv *env, jobject object, jint a, jint b) {
    return a + b;
  }