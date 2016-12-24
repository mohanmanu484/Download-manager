package com.zelo.internal.downloadmanager.listeners;

import android.os.Handler;
import android.os.Looper;

import com.zelo.internal.downloadmanager.core.DownloadOptions;
import com.zelo.internal.downloadmanager.core.DownloadStatus;

/**
 * Created by mohan on 24/12/16.
 */

public class DownloadResponceListener {

    DownloadStatus mDownloadStatus;
    DownloadListener downloadListener;
    Handler handler;
    DownloadOptions downloadOptions;

    public DownloadResponceListener(DownloadListener downloadListener ,DownloadOptions downloadOptions) {
        handler = new Handler(Looper.getMainLooper());
        this.downloadListener=downloadListener;
        this.downloadOptions=downloadOptions;
    }

    public void updateStatus(DownloadStatus downloadStatus){
        handler.post(new StatusUpdater(downloadStatus,downloadListener,downloadOptions));
    }


    public static class StatusUpdater implements Runnable {

        DownloadStatus mDownloadStatus;
        DownloadListener mCallBack;
        DownloadOptions downloadOptions;

        public StatusUpdater(DownloadStatus mDownloadStatus, DownloadListener downloadListener,DownloadOptions downloadOptions) {
            this.mDownloadStatus = mDownloadStatus;
            this.mCallBack = downloadListener;
            this.downloadOptions=downloadOptions;
        }

        @Override
        public void run() {
            switch (mDownloadStatus.getStatus()) {
                case DownloadStatus.STATUS_CONNECTED:
                    mCallBack.onConnected(mDownloadStatus.getLength());
                    break;
                case DownloadStatus.STATUS_PROGRESS:
                    mCallBack.onProgress(mDownloadStatus.getLength(),mDownloadStatus.getFinished());
                    break;
                case DownloadStatus.STATUS_COMPLETED:
                    mCallBack.onDownoadComplete();
                    downloadOptions.onComplete(mDownloadStatus.getUri());
                    break;
                case DownloadStatus.STATUS_PAUSED:
                    mCallBack.onDownloadPaused();
                    break;
                case DownloadStatus.STATUS_CANCELED:
                    mCallBack.onDownloadCancelled();
                    break;
                case DownloadStatus.STATUS_FAILED:
                    mCallBack.onDownloadFailed();
                    downloadOptions.onFailed(mDownloadStatus.getUri());
                    break;
            }
        }
    }

}
