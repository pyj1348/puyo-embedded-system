#include <jni.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <sys/ioctl.h>
#include <fcntl.h>
#include <android/log.h>

#define DRIVER_MAGIC		0xBC
#define DRIVER_SET_ALL	_IOW(DRIVER_MAGIC, 0, int)
#define DRIVER_SET_CLEAR	_IOW(DRIVER_MAGIC, 1, int)
#define DRIVER_EXPLOSION	_IOW(DRIVER_MAGIC, 6, int)
#define DRIVER_GAME_OVER	_IOW(DRIVER_MAGIC, 7, int)

int fd;

JNIEXPORT jint JNICALL
Java_com_example_puyo_MainActivity_matrix_1write(JNIEnv *env, jobject thiz, jint signal)
{
    fd = open("/dev/dot_matrix", O_WRONLY);
    if(fd < 0){
        return -1;
    }

    if(signal == 0){
        ioctl(fd, DRIVER_EXPLOSION, NULL, _IOC_SIZE(DRIVER_EXPLOSION));
    }
    else if(signal > 0 && signal < 5){
        write(fd, &signal, 1);
    }
    else if(signal == 5){
        ioctl(fd, DRIVER_GAME_OVER, NULL, _IOC_SIZE(DRIVER_GAME_OVER));
    }

    close(fd);
    return 0;
} 