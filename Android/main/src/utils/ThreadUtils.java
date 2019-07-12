package com.example.xm.xianyc.utils;


import android.os.Handler;

public class ThreadUtils {
    /*子线程执行task*/
    public static void runInThread(Runnable task){
        new Thread(task).start();
    }

    /**
     * 主线程Handler
     */
    public static Handler mhandler = new Handler();

    /*ui线程执行task*/
    public static void runInUIThread(Runnable task){
        mhandler.post(task);
    }

}
