package com.zelo.internal.zelo.model;

import com.zelo.internal.downloadmanager.core.DownloadStatus;

import static com.zelo.internal.downloadmanager.core.DownloadStatus.STATUS_CONNECTED;

/**
 * Created by mohan on 26/12/16.
 */

public class DownloadInfo {

    private String URL;

    private String name;
    private String packageName;
    private String id;
    private String image;
    private String url;
    private int progress;
    private String downloadPerSize;
    private int status;
    private int length;


    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public DownloadInfo(String id, String URL, String name) {
        this.url = URL;
        this.id=id;
        this.name=name;


    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public String getDownloadPerSize() {
        return downloadPerSize;
    }

    public void setDownloadPerSize(String downloadPerSize) {
        this.downloadPerSize = downloadPerSize;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getButtonText() {
        switch (status) {
            case STATUS_CONNECTED:
                return "pause";
            case DownloadStatus.STATUS_PROGRESS:
                return "Pause";
            case DownloadStatus.STATUS_PAUSED:
                return "Resume";
            case DownloadStatus.STATUS_FAILED:
                return "Retry";
            case DownloadStatus.STATUS_COMPLETED:
                return "View";
            default:
                return "Download";
        }
    }
}
