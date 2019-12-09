//
// Created by User on 2019-12-07.
//

#include <jni.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <sys/ioctl.h>
#include <android/log.h>

#define DRIVER_MAGIC		0xBD
#define DRIVER_SET_CURSOR_POS	_IOW(DRIVER_MAGIC, 0, int)

int fd, pos;
const char *num, *ip;

JNIEXPORT jint JNICALL
Java_com_example_puyo_MainActivity_LCD_1write(JNIEnv *env, jobject thiz, jstring game_mode, jstring address)
{
	fd = open("/dev/text_LCD", O_WRONLY);
	if(fd < 0){
		return -1;
	}

	num = (*env)->GetStringUTFChars(env, game_mode, NULL);
	ip = (*env)->GetStringUTFChars(env, address, NULL);

	pos = 0;
	ioctl(fd, DRIVER_SET_CURSOR_POS, &pos, _IOC_SIZE(DRIVER_SET_CURSOR_POS));
	write(fd, num, strlen(num));

	pos = 16;
	ioctl(fd, DRIVER_SET_CURSOR_POS, &pos, _IOC_SIZE(DRIVER_SET_CURSOR_POS));
	write(fd, ip, strlen(ip));

	close(fd);

	return 0;

}

// (*env)->ReleaseStringUTFChars();
// (*env)->NewStringUTF()