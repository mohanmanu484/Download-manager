package com.zelo.internal.downloadmanager.core;


import android.os.Process;
import android.text.TextUtils;

import com.zelo.internal.downloadmanager.exeptions.CancelledException;
import com.zelo.internal.downloadmanager.exeptions.DownloadException;
import com.zelo.internal.downloadmanager.exeptions.DownloadExceptionHandler;
import com.zelo.internal.downloadmanager.exeptions.FailedException;
import com.zelo.internal.downloadmanager.exeptions.PausedException;
import com.zelo.internal.downloadmanager.listeners.Downloder;
import com.zelo.internal.downloadmanager.listeners.DownloadResponceListener;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by mohan on 23/12/16.
 */
public class DownloadTask implements Runnable {

    private DownloadStatus mDownloadStatus;
    private Downloder mDownloder;
    private DownloadResponceListener mDownloadResponceListener;
    private String URL;
    private DownloadOptions downloadOptions;

    public DownloadStatus getmDownloadStatus() {
        return mDownloadStatus;
    }

    public DownloadTask( DownloadResponceListener downloadResponceListener,Downloder mDownloder,String URL,DownloadOptions downloadOptions) {
        mDownloadStatus = new DownloadStatus(URL);
        this.mDownloadResponceListener=downloadResponceListener;
        this.mDownloder=mDownloder;
        mDownloder.setmDownloadStatus(mDownloadStatus);
        this.URL=URL;
        this.downloadOptions=downloadOptions;
    }

    public String getURL() {
        return URL;
    }

    private void executeConnection() throws DownloadException {
        HttpURLConnection httpConnection = null;
        final URL url;
        try {
            url = new URL(URL);
        } catch (MalformedURLException e) {
            throw new FailedException(DownloadStatus.STATUS_FAILED, "Bad url.", e);
        }
        try {
            httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.setConnectTimeout(Constants.HTTP.CONNECT_TIME_OUT);
            httpConnection.setReadTimeout(Constants.HTTP.READ_TIME_OUT);
            httpConnection.setRequestMethod(Constants.HTTP.GET);
            httpConnection.setRequestProperty("Range", "bytes=" + 0 + "-");
            final int responseCode = httpConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_PARTIAL) {
                parseResponse(httpConnection);
            } else {
                throw new FailedException(DownloadStatus.STATUS_FAILED, "UnSupported response code:" + responseCode);
            }
        } catch (ProtocolException e) {
            throw new FailedException(DownloadStatus.STATUS_FAILED, "Protocol error", e);
        } catch (IOException e) {
            throw new FailedException(DownloadStatus.STATUS_FAILED, "IO error", e);
        } finally {
            if (httpConnection != null) {
                httpConnection.disconnect();
            }
        }
    }

    private void parseResponse(HttpURLConnection httpConnection) throws DownloadException {

        final long length;
        String contentLength = httpConnection.getHeaderField("Content-Length");
        if (TextUtils.isEmpty(contentLength) || contentLength.equals("0") || contentLength.equals("-1")) {
            length = httpConnection.getContentLength();
        } else {
            length = Long.parseLong(contentLength);
        }

        if (length <= 0) {
            throw new FailedException(DownloadStatus.STATUS_FAILED, "length <= 0");
        }

        checkCanceledOrPaused();

        mDownloadStatus.setStatus(DownloadStatus.STATUS_CONNECTED);
        mDownloder.download(length);


    }

    public void pause(){
        mDownloadStatus.setStatus(DownloadStatus.STATUS_PAUSED);
    }

    public void cancel(){
        mDownloadStatus.setStatus(DownloadStatus.STATUS_CANCELED);
    }

    private void checkCanceledOrPaused() throws DownloadException {
        int status = mDownloadStatus.getStatus();
        if (status == DownloadStatus.STATUS_PAUSED) {
            throw new PausedException(DownloadStatus.STATUS_PAUSED, "download Paused");
        } else if (status == DownloadStatus.STATUS_CANCELED) {
            throw new CancelledException(DownloadStatus.STATUS_CANCELED, "download cancelled");
        }
    }

    private static String createKey(String tag) {
        if (tag == null) {
            throw new NullPointerException("Tag can't be null!");
        }
        return String.valueOf(tag.hashCode());
    }

    @Override
    public void run() {
        try {
            Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
            executeConnection();
        } catch (DownloadException e) {
            DownloadExceptionHandler.handle(e,mDownloadStatus);
            mDownloadResponceListener.updateStatus(mDownloadStatus);
            e.printStackTrace();
        }
    }
}
