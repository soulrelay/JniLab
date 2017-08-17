更多精彩请关注[https://github.com/soulrelay/InterviewMemoirs/issues](https://github.com/soulrelay/InterviewMemoirs/issues)
# 环境搭建
## 开发环境
>* MacBook Pro(macOS Sierra10.12.6)
>* Android Studio2.3.3
>* Gradle 2.3.3
## NDK install
1、这里我是采用Android Studio自行安装的
打开AndroidStudio，选择顶部工具条，Tools->Android->SDK Manager->SDK Tools->NDK 点击install
![NDK安装路径](http://upload-images.jianshu.io/upload_images/1814304-c319d861eba7dd78.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

![设置NDK路径](http://upload-images.jianshu.io/upload_images/1814304-e82eb5b296e03cef.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
2、也可以自行下载ndk包（去AndroidDevTools或者谷歌官方网站下载），下载ndk包后解析到某个路径，打开Project Structure->设置 NDK location，同上1设置NDK路径
3、NDK环境变量配置，我们需要使用到ndk-build命令
打开终端 -> 输入 ：vim ~/.bash_profile -> 加入NDK 包的路径，具体怎么使用vim进行编辑请自行百度
![配置NDK环境变量](http://upload-images.jianshu.io/upload_images/1814304-6d44f903f6c4ba87.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

![配置NDK环境变量](http://upload-images.jianshu.io/upload_images/1814304-243b593ae661aeff.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

4、保存文件，关闭.bash_profile，输入source .bash_profile或者重新开启一个terminal ，当前配置才会生效。 命令行输入ndk-build验证配置是否成功

总之：整个配置过程同Android SDK的配置一样

## NDK开发实战
1、创建Android项目JniLab
2、查看项目local.properties中加入ndk和sdk的路径是否正确
``` java
ndk.dir=/Users/didi/Library/Android/sdk/ndk-bundle
sdk.dir=/Users/didi/Library/Android/sdk
```
3、配置项目下的gradle.properties文件，表示我们要使用NDK进行开发(缺少这步会导致后续无法通过alt+enter快捷键生成jni文件夹)
```java
android.useDeprecatedNdk=true
```
4、在moudle根目录下的的build.gradle中的defaultConfig标签内部里加入如下代码
```java
ndk{    
 // 生成的so文件名字，调用C程序的代码中会用到该名字,需要保持一致    
moduleName "algorithm"     
// 输出指定三种平台下的so库
// 还可以添加 'x86_64', 'mips', 'mips64'
abiFilters "armeabi", "armeabi-v7a", "x86" 
}

```
5、编写jni代码
通过System.loadLibrary加载的库名要和上述4的moduleName一致，否则会出现java.lang.UnsatisfiedLinkError问题，找不到so库
![JniManager](http://upload-images.jianshu.io/upload_images/1814304-02d78faa0b73af3b.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
6、执行第5步的时候，如上图所示，对应native方法（getInfo）会提示找不到对应方法，快捷键 alt+enter 会生成对应jni文件夹，包含algorithm.c文件，此处的native方法还是会显示红色，但是不影响编译

![jni文件夹生成](http://upload-images.jianshu.io/upload_images/1814304-540912a349ea375f.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

7、编译项目后会发现app/build中已经生成so文件，并且已经对应的cpu包就是我们在gradle中已经配置的,并且已经调用成功
![build中生成对应so文件](http://upload-images.jianshu.io/upload_images/1814304-9edf6fef9f874869.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

PS：编译时可能碰到NDK_PROJECT_PATH = null问题

![Messages Gradle Build](http://upload-images.jianshu.io/upload_images/1814304-0c9285b2aa656d49.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
暂时的解决方法：将app module的compileSdkVersion与targetSdkVersion由之前的25改成24（可能也跟最新的NDK版本有关系）
![成功调用native方法](http://upload-images.jianshu.io/upload_images/1814304-f5b254c6168ca723.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

## 打包出动态so文件，在其它项目中使用
有时候我们的需求是这样的，我们把一些比较重要的业务逻辑封装到ndk内部，对java层只暴露接口。我们就需要打包出so文件，并且可能需要在其他项目中使用，下面将介绍so（符合JNI标准）文件的打包，以及在其他项目中如何正确的调用
>* 编写Android.mk文件，放到jni文件夹根目录，与.c文件同级
PS:注意中文注释或者中文空格带来的意外麻烦
```java
LOCAL_PATH := $(call my-dir)
 include $(CLEAR_VARS)
LOCAL_MODULE := algorithm
LOCAL_SRC_FILES := /Users/sus/example/JniLab/app/src/main/jni/algorithm.c
 include $(BUILD_SHARED_LIBRARY)
```

>* 使用ndk-build命令（需要配置NDK环境变量，参照上文NDK环境变量配置），生成so文件

>* 编写Application.mk文件，放到jni文件夹根目录，与.c文件同级
```java
APP_PLATFORM := android-14
APP_ABI :=all //打包出所有cpu平台so文件
```
进入到main目录后在terminal中输入命令，ndk-build工具便会帮我们打包出所有cpu平台so文件
![ndk-build](http://upload-images.jianshu.io/upload_images/1814304-f56a862e6e58fa61.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

![libs目录](http://upload-images.jianshu.io/upload_images/1814304-c340ed34e5266040.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
### 其它项目使用该so文件
>* 拷贝so文件到项目的main/**jniLibs**目录
>* 新建package，包名与类名以及方法名必须与生成so文件的类保持一致！
>* 使用loadLibrary加载动态库，声明native方法

PS:这里如果你不想新建项目测试，你可以在main下新建jniLibs文件夹，把libs里的so放到jniLibs中，删除libs文件夹，然后删除jni文件夹运行也会起到类似在新项目中使用so文件的作用
>* 对于上面说的【包名与类名以及方法名必须与生成so文件的类保持一致！】这个规范，读者可能有疑惑，这样的约束太死板不够灵活，我们在使用一些包含so库的第三方SDK的时候并不记得有这么多限制
>* 的确如此，我们看下第三方SDK是怎么搞的，以Umeng Push SDK为参考来看一下，我们发现第三方库都会带有jar包，然后通过包里面去调用so文件，我们只需要使用jar包中暴露的接口方法即可，而上述的规范可能更适合内部人之间开发和使用so

![Umeng Push SDK](http://upload-images.jianshu.io/upload_images/1814304-e69df73d576805c7.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
