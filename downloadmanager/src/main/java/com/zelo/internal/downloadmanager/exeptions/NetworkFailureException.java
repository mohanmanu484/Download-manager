package com.zelo.internal.downloadmanager.exeptions;

/**
 * Created by mohan on 22/12/16.
 */
public class NetworkFailureException extends DownloadException {

    private String errorMessage;
    private int errorCode;

    public NetworkFailureException(String detailMessage) {
        super(detailMessage);
        this.errorMessage = detailMessage;
    }

    public NetworkFailureException(int errorCode, String detailMessage) {
        super(detailMessage);
        this.errorCode = errorCode;
        this.errorMessage = detailMessage;
    }

    public NetworkFailureException(int errorCode, Throwable throwable) {
        super(throwable);
        this.errorCode = errorCode;
    }

    public NetworkFailureException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
        this.errorMessage = detailMessage;
    }

    public NetworkFailureException(int errorCode, String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
        this.errorCode = errorCode;
        this.errorMessage = detailMessage;
    }


    @Override
    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public void setErrorMessage(String errorMessage) {
        this.errorMessage=errorMessage;

    }

    @Override
    public int getErrorCode() {
        return errorCode;
    }

    @Override
    public void setErrorCode(int errorCode) {
        this.errorCode=errorCode;
    }
}
