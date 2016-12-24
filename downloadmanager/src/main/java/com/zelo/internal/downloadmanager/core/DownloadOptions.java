package com.zelo.internal.downloadmanager.core;

/**
 * Created by mohan on 23/12/16.
 */
public interface DownloadOptions {

    void pause(String tag);

    void cancel(String tag);

    void pauseAll();

    void cancelAll();

     void onComplete(String tag);

    void onFailed(String tag);

}
