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
unsigned char bytevalues[4], ret;

JNIEXPORT jint JNICALL
Java_com_example_puyo_MainActivity_segment_1write(JNIEnv *env, jobject thiz, jint score)
{
    fd = open("/dev/7_segment", O_WRONLY);
    if(fd < 0){
        return -1;
    }

    bytevalues[0] = score / 1000;
    score = score % 1000;
    bytevalues[1] = score / 100;
    score = score % 100;
    bytevalues[2] = score / 10;
    score = score % 10;
    bytevalues[3] = score;

    ret = write(fd, bytevalues, 4);
    if(ret < 0){
        return -1;
    }

    close(fd);

    return 0;
}