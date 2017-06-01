# StoneVoicer-Demo
防QQ变声功能（使用FMOD音频引擎）

#仿QQ语言变声功能#

这次写的是QQ语言变声功能，这个功能想必大家都使用过，那么这个功能是怎么实现的呢？
在开发中一边触及和语音，视频，算法等等都和c/c++相关，我们这里也是使用的NDK，链接第三方动态库，生成我们自己的库文件。

先来讲讲第三方的动态库，我们这里使用的FMOD音频引擎提供给android的api和动态库。我们可以到官网下载：
[FMOD](http://www.fmod.com/),因为下载需要注册，但是我天朝的。。。，需要vnp,所以这边也提供了下载地址：[FMOD android下的开发](https://pan.baidu.com/s/1mh518zI)


想看下效果图：

![](/photo/bs.png)


我么进入正题：

###1.从fmod开发包中，引入动态库和jar包,头文件


![](/photo/2.png)


###2.编写CMakeLists.txt文件

	

	cmake_minimum_required(VERSION 3.4.1)
	
	set(distribution_dir ../../../../libs )
	
	
	
	
	find_library(   log-lib
	                log )
	
	
	add_library(    fmod
	                SHARED
	                 IMPORTED )
	
	
	set_target_properties(  fmod
	                        PROPERTIES IMPORTED_LOCATION
	                        ${distribution_dir}/${ANDROID_ABI}/libfmod.so)
	
	add_library(    fmodL
	                SHARED
	                IMPORTED )
	
	set_target_properties(  fmodL
	                        PROPERTIES IMPORTED_LOCATION
	                        ${distribution_dir}/${ANDROID_ABI}/libfmodL.so)
	
	set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=gnu++11")
	
	
	
	
	add_library(    stonevoicer
	                SHARED
	                src/main/cpp/StoneVoicer.cpp )
	
	include_directories(src/main/cpp/inc)
	
	target_link_libraries(  stonevoicer
	                        fmod
	                        fmodL
	                        ${log-lib} )



 
###3.build.gradle中添加配置信息

	 ndk{
            abiFilters "armeabi","x86","armeabi-v7a","arm64-v8a"
        }
	sourceSets {
        main {
            jniLibs.srcDirs = ['libs']
        }
    }

###4.编写java代码


MainActivity:

	public class MainActivity extends AppCompatActivity {

	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_main);
	        requestPermission();
	        FMOD.init(this);
	    }
	    private void  requestPermission(){
	
	        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
	            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
	        }
	        if(ContextCompat.checkSelfPermission(this,Manifest.permission.RECORD_AUDIO)!= PackageManager.PERMISSION_GRANTED) {
	            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 200);
	        }
	    }
	
	    public void mFix(View v) {
	        //音频路径可以去获取麦克风的数据，我这里写死了
	        String path = "file:///android_asset/xiaoxin.wav";
	        switch (v.getId()) {
	            case R.id.btn_normal:
	                VoicerUtil.fix(path, VoicerUtil.MODE_NORMAL);
	                break;
	            case R.id.btn_luoli:
	                VoicerUtil.fix(path, VoicerUtil.MODE_LUOLI);
	                break;
	            case R.id.btn_dashu:
	                VoicerUtil.fix(path, VoicerUtil.MODE_DASHU);
	                break;
	            case R.id.btn_jingsong:
	                VoicerUtil.fix(path, VoicerUtil.MODE_JINGSONG);
	                break;
	            case R.id.btn_gaoguai:
	                VoicerUtil.fix(path, VoicerUtil.MODE_GAOGUAI);
	                break;
	            case R.id.btn_kongling:
	                VoicerUtil.fix(path, VoicerUtil.MODE_KONGLING);
	                break;
	            default:
	                break;
	        }
	    }
	
	    @Override
	    protected void onDestroy() {
	        super.onDestroy();
	        FMOD.close();
	    }
	}


VoicerUtil:

	public class VoicerUtil {


	    public static final int MODE_NORMAL=0;
	    public static final int MODE_LUOLI=1;
	    public static final int MODE_DASHU=2;
	    public static final int MODE_JINGSONG=3;
	    public static final int MODE_GAOGUAI=4;
	    public static final int MODE_KONGLING=5;
	    /**
	     *
	     * @param path
	     * @param type
	     */
	    public native static void fix(String path,int type);
	    static{
	        System.loadLibrary("fmod");
	        System.loadLibrary("fmodL");
	        System.loadLibrary("stonevoicer");
	    }
	}


###5.生成头文件

我这边使用的是javah来生成的：
	
	javah -classpath . -jni github.com.stonevoicer.VoicerUtil

然后将头文件移动到cpp文件夹下：

![](/photo/3.png)



###6.编写c++代码

	

	#include <jni.h>
	#include <string>
	#include <stdlib.h>
	#include "github_com_stonevoicer_VoicerUtil.h"
	#include <fmod.hpp>
	#include <unistd.h>
	
	#include <android/log.h>
	#define LOGI(FORMAT,...) __android_log_print(ANDROID_LOG_INFO,"zph",FORMAT,##__VA_ARGS__);
	#define LOGE(FORMAT,...) __android_log_print(ANDROID_LOG_ERROR,"zph",FORMAT,##__VA_ARGS__);
	
	
	#define MODE_NORMAL 0
	#define MODE_LUOLI 1
	#define MODE_DASHU 2
	#define MODE_JINGSONG 3
	#define MODE_GAOGUAI 4
	#define MODE_KONGLING 5
	
	using namespace FMOD;
	
	JNIEXPORT void JNICALL Java_github_com_stonevoicer_VoicerUtil_fix
	        (JNIEnv *env, jclass jcls, jstring path_jstr, jint mode){
	    LOGI("%s", "===>start");
	    System * system;
	    Sound *sound;
	    DSP *dsp;
	    Channel *channel;
	    float frequency;
	    bool isplaying= true;
	    System_Create(&system);
	    system->init(32,FMOD_INIT_NORMAL,NULL);
	    const char* path_cstr = env->GetStringUTFChars(path_jstr,NULL);
	    try {
	        system->createSound(path_cstr,FMOD_DEFAULT,NULL,&sound);
	        switch (mode){
	            case MODE_NORMAL:
	                LOGI("%s", path_cstr);
	                system->playSound(sound, 0, false, &channel);
	                LOGI("%s", "fix normal");
	                break;
	            case MODE_LUOLI:
	                //dsp->音效
	                //dsp 提升或者降低音调的一种音效
	                system->createDSPByType(FMOD_DSP_TYPE_PITCHSHIFT, &dsp);
	                dsp->setParameterFloat(FMOD_DSP_PITCHSHIFT_PITCH,8.0);
	                system->playSound(sound, 0, false, &channel);
	                channel->addDSP(0, dsp);
	                break;
	            case MODE_DASHU:
	                system->createDSPByType(FMOD_DSP_TYPE_PITCHSHIFT, &dsp);
	                dsp->setParameterFloat(FMOD_DSP_PITCHSHIFT_PITCH,0.8);
	                system->playSound(sound, 0, false, &channel);
	                channel->addDSP(0, dsp);
	                break;
	            case MODE_JINGSONG:
	                system->createDSPByType(FMOD_DSP_TYPE_TREMOLO, &dsp);
	                dsp->setParameterFloat(FMOD_DSP_TREMOLO_SKEW, 5);
	                system->playSound(sound, 0, false, &channel);
	                channel->addDSP(0, dsp);
	                break;
	            case MODE_GAOGUAI:
	                //提高说话的速度
	                system->playSound(sound, 0, false, &channel);
	                channel->getFrequency(&frequency);
	                frequency = frequency * 2;
	                channel->setFrequency(frequency);
	                break;
	            case MODE_KONGLING:
	                //空灵
	                system->createDSPByType(FMOD_DSP_TYPE_ECHO, &dsp);
	                dsp->setParameterFloat(FMOD_DSP_ECHO_DELAY, 300);
	                dsp->setParameterFloat(FMOD_DSP_ECHO_FEEDBACK, 20);
	                system->playSound(sound, 0, false, &channel);
	                channel->addDSP(0, dsp);
	                break;
	        }
	
	    }catch (...){
	        LOGE("%s", "发生异常");
	        goto end;
	    }
	    system->update();
	
	    while (isplaying){
	        channel->isPlaying(&isplaying);
	        usleep(1000 * 1000);
	    }
	
	
	    goto end;
	
	
	    end:
	    env->ReleaseStringUTFChars(path_jstr, path_cstr);
	    sound->release();
	    system->close();
	    system->release();
	
	}






下面给出项目地址：[StoneVoicer-Demo](https://github.com/skcodestack/StoneVoicer-Demo)
