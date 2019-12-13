#include <jni.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <fcntl.h>
#include <android/log.h>

int fd;
unsigned char direction, i;

JNIEXPORT jint JNICALL
Java_com_example_puyo_SinglePlay_button_1open(JNIEnv *env, jobject thiz)
{
    fd = open("/dev/9_button", O_RDONLY);
    if(fd < 0) {
        return -1;
    }
    return fd;
}

JNIEXPORT jint JNICALL
Java_com_example_puyo_SinglePlay_button_1read(JNIEnv *env, jobject thiz)
{
    if(read(fd, &direction, 1) != 1){
        return -1;
    } else if(direction == 1) {
        return 1; //printf("LEFT ROTATE\n");
    } else if(direction == 2) {
        return 2; //printf("LEFT MOVE\n");
    } else if(direction == 4) {
        return 4; //printf("RIGHT MOVE\n");
    } else if(direction == 8) {
        return 8; //printf("RIGHT ROTATE\n");
    } else
        return 0;
}

JNIEXPORT jint JNICALL
Java_com_example_puyo_SinglePlay_botton_1close(JNIEnv *env, jobject thiz)
{
    close(fd);
    return 0;
}

JNIEXPORT jint JNICALL
Java_com_example_puyo_Players2Activity_button_1open(JNIEnv *env, jobject thiz)
{
    fd = open("/dev/9_button", O_RDONLY);
    if(fd < 0) {
        return -1;
    }
    return fd;
}

JNIEXPORT jint JNICALL
Java_com_example_puyo_Players2Activity_button_1read(JNIEnv *env, jobject thiz)
{
    if(read(fd, &direction, 1) != 1){
        return -1;
    } else if(direction == 1) {
        return 1; //printf("LEFT ROTATE\n");
    } else if(direction == 2) {
        return 2; //printf("LEFT MOVE\n");
    } else if(direction == 4) {
        return 4; //printf("RIGHT MOVE\n");
    } else if(direction == 8) {
        return 8; //printf("RIGHT ROTATE\n");
    } else
        return 0;
}

JNIEXPORT jint JNICALL
Java_com_example_puyo_Players2Activity_botton_1close(JNIEnv *env, jobject thiz)
{
    close(fd);
    return 0;
} 
JNIEXPORT jint JNICALL
Java_com_example_puyo_MultiActivity_button_1open(JNIEnv *env, jobject thiz)
{
    fd = open("/dev/9_button", O_RDONLY);
    if(fd < 0) {
        return -1;
    }
    return fd;
}

JNIEXPORT jint JNICALL
Java_com_example_puyo_MultiActivity_button_1read(JNIEnv *env, jobject thiz)
{
    if(read(fd, &direction, 1) != 1){
        return -1;
    } else if(direction == 1) {
        return 1; //printf("LEFT ROTATE\n");
    } else if(direction == 2) {
        return 2; //printf("LEFT MOVE\n");
    } else if(direction == 4) {
        return 4; //printf("RIGHT MOVE\n");
    } else if(direction == 8) {
        return 8; //printf("RIGHT ROTATE\n");
    } else
        return 0;
}

JNIEXPORT jint JNICALL
Java_com_example_puyo_MultiActivity_botton_1close(JNIEnv *env, jobject thiz)
{
    close(fd);
    return 0;
} 