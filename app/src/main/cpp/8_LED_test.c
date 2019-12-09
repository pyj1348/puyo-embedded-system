//
// Created by User on 2019-12-07.
//

#include <jni.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <fcntl.h>
#include <android/log.h>

int fd;
unsigned char bytevalue, ret;

JNIEXPORT jint JNICALL
Java_com_example_puyo_MainActivity_LED_1write(JNIEnv *env, jobject thiz, jint combo)
{
    fd = open("/dev/8_LED", O_WRONLY);
    if(fd < 0){
        return -1;
    }

    ret = write(fd, &bytevalue, 1);
    if(ret < 0){
        return -1;
    }

    close(fd);

    return 0;
}