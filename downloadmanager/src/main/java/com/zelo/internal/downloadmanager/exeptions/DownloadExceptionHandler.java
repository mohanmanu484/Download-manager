package com.zelo.internal.downloadmanager.exeptions;


import com.zelo.internal.downloadmanager.core.DownloadStatus;
import com.zelo.internal.downloadmanager.listeners.DownloadResponceListener;

/**
 * Created by mohan on 23/12/16.
 */
public class DownloadExceptionHandler {

    public static void handle(DownloadException downloadException,  DownloadStatus downloadStatus) {

        DownloadResponceListener downloadResponceListener;
        if (downloadException instanceof PausedException) {
            downloadStatus.setStatus(DownloadStatus.STATUS_PAUSED);

        } else if (downloadException instanceof NetworkFailureException) {
            downloadStatus.setStatus(DownloadStatus.STATUS_NETWORK_FAIL);

        } else if (downloadException instanceof FailedException) {
            downloadStatus.setStatus(DownloadStatus.STATUS_FAILED);
        } else if (downloadException instanceof CancelledException) {
            downloadStatus.setStatus(DownloadStatus.STATUS_CANCELED);
        }
    }
}
