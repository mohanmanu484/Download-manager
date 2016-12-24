package com.zelo.internal.downloadmanager.core;

public class DownloadStatus {
    public static final int STATUS_STARTED = 101;
    public static final int STATUS_CONNECTING = 102;
    public static final int STATUS_CONNECTED = 103;
    public static final int STATUS_PROGRESS = 104;
    public static final int STATUS_COMPLETED = 105;
    public static final int STATUS_PAUSED = 106;
    public static final int STATUS_CANCELED = 107;
    public static final int STATUS_FAILED = 108;

    public static final int STATUS_NETWORK_FAIL=110;

    private  int status;
    private  long time;
    private  long length;
    private  long finished;
    private  int percent;
    private String Uri;


    public DownloadStatus(String uri) {
        Uri = uri;
    }

    public String getUri() {
        return Uri;
    }

    public void setUri(String uri) {
        Uri = uri;
    }

    public  int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public long getFinished() {
        return finished;
    }

    public void setFinished(long finished) {
        this.finished = finished;
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }

    public interface DownloadStatusListener{
        void onStatuusChange(DownloadStatus downloadStatus);
    }



}