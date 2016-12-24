package com.zelo.internal.downloadmanager.test;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by mohan on 24/12/16.
 */

public class TestMainThread implements SecodCallback {


    private static TestMainThread testMainThread;
    private MainCallback mainCallback;
    public static TestMainThread getInstance(){

        if(testMainThread==null){
            testMainThread=new TestMainThread();
        }


        return testMainThread;
    }


    public void createSecondThread(MainCallback mainCallback){
        this.mainCallback=mainCallback;
      ExecutorService mExecutorService = Executors.newFixedThreadPool(1);

        mExecutorService.execute(new SecondThread(this));
    }

    @Override
    public void callback() {
        Handler handler=new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                mainCallback.mainCallback();
            }
        });

    }
}
