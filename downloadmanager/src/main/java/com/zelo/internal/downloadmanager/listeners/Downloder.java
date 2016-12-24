package com.zelo.internal.downloadmanager.listeners;

import com.zelo.internal.downloadmanager.core.DownloadStatus;
import com.zelo.internal.downloadmanager.exeptions.DownloadException;

/**
 * Created by mohan on 23/12/16.
 */
public interface Downloder {

    void download(long length) throws DownloadException;
    void setmDownloadStatus(DownloadStatus downloadStatus);
}
