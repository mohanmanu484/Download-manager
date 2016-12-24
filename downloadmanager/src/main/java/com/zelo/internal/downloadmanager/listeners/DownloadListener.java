package com.zelo.internal.downloadmanager.listeners;

/**
 * Created by mohan on 22/12/16.
 */
public interface DownloadListener {

    void onConnected(long totalLength);
    void onProgress(long total, long progress);
    void onDownoadComplete();
    void onDownloadPaused();
    void onDownloadCancelled();
    void onDownloadFailed();
}
