package com.zelo.internal.downloadmanager.core;


import android.content.Context;
import android.os.Handler;

import com.zelo.internal.downloadmanager.listeners.DownloadListener;
import com.zelo.internal.downloadmanager.listeners.DownloadResponceListener;
import com.zelo.internal.downloadmanager.listeners.Downloder;
import com.zelo.internal.downloadmanager.model.DownloadFile;
import com.zelo.internal.downloadmanager.model.DownloadFileDb;
import com.zelo.internal.downloadmanager.receivers.ConnectivityReceiver;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Created by mohan on 23/12/16.
 */
public class ZeloDownloadManager implements DownloadOptions, ConnectivityReceiver.ConnectivityReceiverListener {

    private static ZeloDownloadManager sDownloadManager;
    private ExecutorService mExecutorService;
    private Map<String, DownloadTask> mDownloaderMap;
    private Handler mHandler;
    private static DownloadFileDb downloadFileDb;
    private static Context mcontext;
    private  DownloadListener mDownloadListener;


    public static ZeloDownloadManager getInstance(Context context) {
        mcontext=context;
        if (sDownloadManager == null) {
            synchronized (ZeloDownloadManager.class) {
                if (sDownloadManager == null) {
                    sDownloadManager = new ZeloDownloadManager();
                    if(downloadFileDb==null){
                        downloadFileDb=DownloadFileDb.getInstance(context) ;
                    }
                }
            }
        }
        return sDownloadManager;
    }

    public static DownloadFileDb getDownloadFileDb() {
        if(downloadFileDb==null){
            downloadFileDb=DownloadFileDb.getInstance(mcontext) ;
        }

        return downloadFileDb;
    }

    public ZeloDownloadManager() {
        mDownloaderMap = new LinkedHashMap<>();
        ConnectivityReceiver.setConnectivityReceiverListener(this);
        mHandler=new Handler();
    }

    public void add(DownloadConfiguration downloadConfiguration, DownloadListener downloadListener) {

        this.mDownloadListener=downloadListener;
        mExecutorService = Executors.newFixedThreadPool(1);
        final String key = createKey(downloadConfiguration.getmURL());
        if (check(key)) {

            DownloadResponceListener downloadResponceListener=new DownloadResponceListener(downloadListener,this);
            Downloder  downloder=new DownloaderImpl(downloadConfiguration,key,downloadResponceListener,downloadFileDb,this);
            DownloadTask downloadTask = new DownloadTask(downloadResponceListener,downloder, downloadConfiguration.getmURL(),this);
            mExecutorService.execute(downloadTask);
            mDownloaderMap.put(key,downloadTask);
        }


    }

    private boolean check(String key) {
        if (mDownloaderMap.containsKey(key)) {
            DownloadTask downloadTask = mDownloaderMap.get(key);
            if (downloadTask != null) {

                int status=downloadTask.getmDownloadStatus().getStatus();
                throw new IllegalStateException("DownloaderImpl instance with same tag has not been destroyed!");
            }
        }
        return true;
    }

    private static String createKey(String tag) {
        if (tag == null) {
            throw new NullPointerException("Tag can't be null!");
        }
        return String.valueOf(tag.hashCode());
    }

    @Override
    public void pause(String tag) {
        String key = createKey(tag);
        if (mDownloaderMap.containsKey(key)) {
            DownloadTask downloader = mDownloaderMap.get(key);
            if (downloader != null) {
                int status=downloader.getmDownloadStatus().getStatus();
                if (status==DownloadStatus.STATUS_STARTED||
                        status==DownloadStatus.STATUS_CONNECTED||
                        status==DownloadStatus.STATUS_PROGRESS) {
                    downloader.pause();
                }
            }
            mDownloaderMap.remove(key);
        }
    }

    @Override
    public void cancel(String tag) {
        String key = createKey(tag);
        if (mDownloaderMap.containsKey(key)) {
            DownloadTask downloader = mDownloaderMap.get(key);
            if (downloader != null) {
                downloader.cancel();
                delete(key);
            }
            mDownloaderMap.remove(key);
        }
    }

    @Override
    public void pauseAll() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                for (DownloadTask downloader : mDownloaderMap.values()) {
                    if (downloader != null) {

                        int status=downloader.getmDownloadStatus().getStatus();
                        if (status==DownloadStatus.STATUS_STARTED||
                                status==DownloadStatus.STATUS_CONNECTED||
                                status==DownloadStatus.STATUS_PROGRESS) {
                            downloader.pause();
                        }
                    }
                }
            }
        });
    }
    public DownloadFile getDownloadInfo(String tag) {
        if(tag==null || downloadFileDb==null){
            return null;
        }
        return downloadFileDb.getDownloadFile(tag);
    }

    public void startFailedProceses(){
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                for (DownloadTask downloader : mDownloaderMap.values()) {
                    if (downloader != null) {

                        int status=downloader.getmDownloadStatus().getStatus();
                        if (status==DownloadStatus.STATUS_FAILED) {
                            mExecutorService.execute(downloader);
                        }
                    }
                }
            }
        });
    }

    @Override
    public void cancelAll() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                for (DownloadTask downloader : mDownloaderMap.values()) {
                    if (downloader != null) {

                        int status=downloader.getmDownloadStatus().getStatus();
                        if (status==DownloadStatus.STATUS_STARTED||
                                status==DownloadStatus.STATUS_CONNECTED||
                                status==DownloadStatus.STATUS_PROGRESS) {
                            downloader.cancel();
                        }
                    }
                }
            }
        });
    }

    public void delete(String key) {
        downloadFileDb.delete(key);
    }

    public void removeKey(String tag){
      //  String key = createKey(tag);
        if (mDownloaderMap.containsKey(tag)) {
            //DownloadTask downloader = mDownloaderMap.get(key);
            mDownloaderMap.remove(tag);
        }
    }

    @Override
    public void onComplete(String tag) {
        removeKey(tag);
    }

    @Override
    public void onFailed(String tag) {
        removeKey(createKey(tag));
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if(isConnected){
            startFailedProceses();
        }
    }
}
