package com.zelo.internal.zelo;

import android.content.Context;
import android.support.annotation.NonNull;

import com.zelo.internal.downloadmanager.core.DownloadConfiguration;
import com.zelo.internal.downloadmanager.listeners.DownloadListener;
import com.zelo.internal.zelo.model.DownloadInfo;

import java.util.List;

/**
 * Created by mohan on 4/10/16.
 */

public interface ImageDataSource {

    interface LoadImagesCallback {

        void onImagesLoaded(List<DownloadInfo> images);

        void onImageNotAvailable();
    }

    public void getImages(@NonNull LoadImagesCallback callback);

    public void downLoadImage(Context context, DownloadConfiguration downloadConfiguration, DownloadListener downloadListener);

    public void pauseDownload(String tag);

    public void cancelDownload(String tag);

    public void pauseAll();

}
