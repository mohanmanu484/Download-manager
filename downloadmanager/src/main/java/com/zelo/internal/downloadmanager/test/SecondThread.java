package com.zelo.internal.downloadmanager.test;

import android.util.Log;

/**
 * Created by mohan on 24/12/16.
 */

public class SecondThread implements Runnable {

    TestMainThread testMainThread;

    public SecondThread(TestMainThread testMainThread) {
        this.testMainThread = testMainThread;
    }

    private static final String TAG = "SecondThread";
    @Override
    public void run() {
        Log.d(TAG, "run: ");
        testMainThread.callback();
    }
}
