package com.zelo.internal.zelo.model;

import android.content.Context;
import android.support.annotation.NonNull;

import com.zelo.internal.downloadmanager.core.DownloadConfiguration;
import com.zelo.internal.downloadmanager.core.ZeloDownloadManager;
import com.zelo.internal.downloadmanager.listeners.DownloadListener;
import com.zelo.internal.downloadmanager.model.DownloadFile;
import com.zelo.internal.zelo.Constants;
import com.zelo.internal.zelo.ImageDataSource;

import java.util.ArrayList;

/**
 * Created by mohan on 4/10/16.
 */

public class ImagesRepository implements ImageDataSource {

    private static final String TAG = "ImagesRepository";
    private ZeloDownloadManager zeloDownloadManager;

    public ImagesRepository(Context context) {
        zeloDownloadManager=ZeloDownloadManager.getInstance(context);
    }

    @Override
    public void getImages(@NonNull LoadImagesCallback callback) {


        ArrayList<DownloadInfo> downloadInfos= Constants.getImagesList();

        for (DownloadInfo info : downloadInfos) {
            DownloadFile downloadfile = zeloDownloadManager.getDownloadInfo(info.getUrl());
            if (downloadfile != null) {
                info.setProgress((int) downloadfile.getFinished());
                info.setLength((int) downloadfile.getEnd());
                info.setStatus(downloadfile.getStatus());
            }
        }
        if(downloadInfos.size()>0){
            callback.onImagesLoaded(downloadInfos);
        }else {
            callback.onImageNotAvailable();
        }
    }

    @Override
    public void downLoadImage(Context context, DownloadConfiguration downloadConfiguration,DownloadListener downloadListener) {
        zeloDownloadManager.add(downloadConfiguration, downloadListener);
    }

    @Override
    public void pauseDownload(String tag) {
        zeloDownloadManager.pause(tag);
    }

    @Override
    public void cancelDownload(String tag) {
        zeloDownloadManager.cancel(tag);
    }

    @Override
    public void pauseAll() {
        zeloDownloadManager.pauseAll();
    }


}
