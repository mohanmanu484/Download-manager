package com.zelo.internal.downloadmanager.model;


import com.zelo.internal.downloadmanager.core.DownloadConfiguration;

/**
 * Created by mohan on 23/12/16.
 */
public class DownloadFile {

    private int id;
    private String tag;
    private String uri;
    private long start;
    private long end;
    private long finished;
    private int status;
    private DownloadConfiguration downloadConfiguration;

    public DownloadFile( String tag, String uri, long start, long end, long finished) {
        this.tag = tag;
        this.uri = uri;
        this.start = start;
        this.end = end;
        this.finished = finished;
    }

    public DownloadFile(int id, String tag, String uri, long start, long end, long finished) {
        this.id=id;
        this.tag = tag;
        this.uri = uri;
        this.start = start;
        this.end = end;
        this.finished = finished;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public DownloadConfiguration getDownloadConfiguration() {
        return downloadConfiguration;
    }

    public void setDownloadConfiguration(DownloadConfiguration downloadConfiguration) {
        this.downloadConfiguration = downloadConfiguration;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public long getFinished() {
        return finished;
    }

    public void setFinished(long finished) {
        this.finished = finished;
    }
}
