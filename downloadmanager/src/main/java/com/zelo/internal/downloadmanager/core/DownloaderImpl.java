package com.zelo.internal.downloadmanager.core;


import com.zelo.internal.downloadmanager.exeptions.CancelledException;
import com.zelo.internal.downloadmanager.exeptions.DownloadException;
import com.zelo.internal.downloadmanager.exeptions.DownloadExceptionHandler;
import com.zelo.internal.downloadmanager.exeptions.FailedException;
import com.zelo.internal.downloadmanager.exeptions.PausedException;
import com.zelo.internal.downloadmanager.listeners.Downloder;
import com.zelo.internal.downloadmanager.model.DownloadFile;
import com.zelo.internal.downloadmanager.model.DownloadFileDb;
import com.zelo.internal.downloadmanager.listeners.DownloadResponceListener;
import com.zelo.internal.downloadmanager.utils.IOCloseUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mohan on 23/12/16.
 */
public class DownloaderImpl implements Downloder {

    private DownloadConfiguration mDownloadConfiguration;
    private String mKey;
    private DownloadFile mDownloadFile;
    private DownloadStatus mDownloadStatus;
    private DownloadFileDb mDownloadFileDb;
    private DownloadOptions mDownloadcompleteListenr;
    private DownloadResponceListener mDownloadResponceListener;

    public DownloaderImpl(DownloadConfiguration mDownloadConfiguration,
                          String mKey,
                          DownloadResponceListener mDownloadResponceListener,
                          DownloadFileDb  mDownloadFileDb,
                          DownloadOptions mDownloadcompleteListenr) {
        this.mDownloadConfiguration = mDownloadConfiguration;
        this.mKey = mKey;
        this.mDownloadFileDb=mDownloadFileDb;
        this.mDownloadcompleteListenr = mDownloadcompleteListenr;
        this.mDownloadResponceListener=mDownloadResponceListener;
    }

    public void setmDownloadStatus(DownloadStatus mDownloadStatus) {
        this.mDownloadStatus = mDownloadStatus;
    }

    @Override
    public void download(long length) {

        try {
            mDownloadStatus.setStatus(DownloadStatus.STATUS_CONNECTED);
            mDownloadStatus.setLength(length);
            mDownloadResponceListener.updateStatus(mDownloadStatus);
            executeDownload(getDownloadbleFile(length));
        } catch (DownloadException e) {
            DownloadExceptionHandler.handle(e,mDownloadStatus);
            mDownloadFile.setStatus(mDownloadStatus.getStatus());
            mDownloadFileDb.update(mDownloadFile,mDownloadFile.getTag());
            mDownloadResponceListener.updateStatus(mDownloadStatus);
            e.printStackTrace();
        }

    }

    private DownloadFile getDownloadbleFile(long length) {


        // // TODO: 23/12/16 need to add db

        DownloadFile downloadFile=mDownloadFileDb.getDownloadFile(mKey);

        if(downloadFile!=null){
            mDownloadFileDb.update(downloadFile,downloadFile.getTag());
            return downloadFile;
        }
        downloadFile=new DownloadFile( mKey, mDownloadConfiguration.getmURL(), 0, length, 0);
        downloadFile.setDownloadConfiguration(mDownloadConfiguration);
        mDownloadFileDb.insert(downloadFile);
        return downloadFile;
    }

    public void executeDownload(DownloadFile mDownloadFile) throws DownloadException {
        final URL url;
        this.mDownloadFile = mDownloadFile;
        mDownloadFile.setStatus(DownloadStatus.STATUS_CONNECTED);
        mDownloadFile.setDownloadConfiguration(mDownloadConfiguration);
        mDownloadFileDb.insert(mDownloadFile);
        try {
            //url = new URL("http://dldir1.qq.com/qqmi/TencentVideo_V4.1.0.8897_51.apk");//http://www.kenrockwell.com/leica/images/m9/examples/legoland-2009-10-03/L1001599.JPG
            url = new URL(mDownloadFile.getUri());
        } catch (MalformedURLException e) {
            mDownloadStatus.setStatus(DownloadStatus.STATUS_FAILED);
            throw new FailedException(DownloadStatus.STATUS_FAILED,"malformed exception");

        }

        HttpURLConnection httpConnection = null;
        try {
            httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.setConnectTimeout(Constants.HTTP.CONNECT_TIME_OUT);
            httpConnection.setReadTimeout(Constants.HTTP.READ_TIME_OUT);
            httpConnection.setRequestMethod(Constants.HTTP.GET);
            setHttpHeader(getHttpHeaders(mDownloadFile), httpConnection);
            //   final int responseCode = httpConnection.getResponseCode();

                transferData(httpConnection);

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

    private void transferData(HttpURLConnection httpConnection) throws DownloadException {
        InputStream inputStream = null;
        RandomAccessFile raf = null;
        mDownloadStatus.setStatus(DownloadStatus.STATUS_PROGRESS);
        try {
            try {
                inputStream = httpConnection.getInputStream();
            } catch (IOException e) {
                throw new FailedException(DownloadStatus.STATUS_FAILED, "http get inputStream error", e);
            }
            final long offset = mDownloadFile.getStart() + mDownloadFile.getFinished();
            try {
                raf = getFile(mDownloadConfiguration.getmFolder(), mDownloadConfiguration.getmName().toString(), offset);
            } catch (IOException e) {
                throw new FailedException(DownloadStatus.STATUS_FAILED, "File error", e);
            }
            transferData(inputStream, raf);
        } finally {
            try {
                IOCloseUtils.close(inputStream);
                IOCloseUtils.close(raf);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    protected RandomAccessFile getFile(File dir, String name, long offset) throws IOException {
        File file = new File(dir, name);
        RandomAccessFile raf = new RandomAccessFile(file, "rwd");
        raf.seek(offset);
        return raf;
    }

    private void transferData(InputStream inputStream, RandomAccessFile raf) throws DownloadException {
        final byte[] buffer = new byte[1024 * 8];

        int totlLen = 0;
        while (true) {
            checkPausedOrCanceled();
            int len = -1;
            try {
                len = inputStream.read(buffer);
                if (len == -1) {
                    break;
                }
                totlLen += len;
                System.out.println(totlLen);
                raf.write(buffer, 0, len);
                mDownloadFile.setFinished(mDownloadFile.getFinished() + len);
                synchronized (mDownloadResponceListener) {
                    mDownloadStatus.setLength(mDownloadFile.getEnd());
                    mDownloadStatus.setFinished(mDownloadFile.getFinished());
                    mDownloadResponceListener.updateStatus(mDownloadStatus);
                }

            } catch (IOException e) {
                // updateDB(mThreadInfo);
                throw new FailedException(DownloadStatus.STATUS_FAILED, e);
            }
        }
        mDownloadFile.setStatus(DownloadStatus.STATUS_COMPLETED);
        mDownloadStatus.setStatus(DownloadStatus.STATUS_COMPLETED);
        mDownloadResponceListener.updateStatus(mDownloadStatus);
        mDownloadFileDb.update(mDownloadFile,mKey);
        System.out.println("total len=" + totlLen);
    }

    private void checkPausedOrCanceled() throws DownloadException {

        int status=mDownloadStatus.getStatus();

        if (status == DownloadStatus.STATUS_CANCELED) {
            throw new CancelledException(DownloadStatus.STATUS_CANCELED, "Download canceled!");
        } else if (status == DownloadStatus.STATUS_PAUSED) {
            throw new PausedException(DownloadStatus.STATUS_PAUSED, "Download paused!");
        }
    }


    protected Map<String, String> getHttpHeaders(DownloadFile info) {
        Map<String, String> headers = new HashMap<String, String>();
        long start = info.getStart() + info.getFinished();
        long end = info.getEnd();
        headers.put("Range", "bytes=" + start + "-" + end);
        return headers;
    }

    private void setHttpHeader(Map<String, String> headers, URLConnection connection) {
        if (headers != null) {
            for (String key : headers.keySet()) {
                connection.setRequestProperty(key, headers.get(key));
            }
        }
    }
}
