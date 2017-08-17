package com.sus.jnilab.utils;

/**
 * Created by SuS on 2017/8/17.
 */

public class JniManager {

    //使用静态代码块，表示我们要加载的资源文件为libalgorithm.so
    static {
        System.loadLibrary("algorithm");
    }

    //声明一个本地方法，用native关键字修饰
    public static native String getInfo();

}
