package com.zelo.internal.downloadmanager.exeptions;

/**
 * Created by mohan on 22/12/16.
 */
public abstract class DownloadException extends Exception{

    public DownloadException(String message){
        super(message);
    }

    public DownloadException(Throwable throwable) {
        super(throwable);
    }

    public DownloadException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);;
    }

    public abstract String getErrorMessage();

    public abstract void setErrorMessage(String errorMessage);

    public abstract int getErrorCode();

    public abstract void setErrorCode(int errorCode);
}
