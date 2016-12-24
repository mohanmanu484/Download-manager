package com.zelo.internal.downloadmanager.core;

import java.io.File;

/**
 * Created by mohan on 23/12/16.
 */
public class DownloadConfiguration {

    private String mURL;

    private File mFolder;

    private CharSequence mName;


    private DownloadConfiguration(String mURL, File mFolder, CharSequence mName) {
        this.mURL = mURL;
        this.mFolder = mFolder;
        this.mName = mName;
    }

    public String getmURL() {
        return mURL;
    }

    public File getmFolder() {
        return mFolder;
    }

    public CharSequence getmName() {
        return mName;
    }

    public static class Builder{
        private String mURL;

        private File mFolder;

        private CharSequence mName;

        public Builder setmURL(String mURL) {
            this.mURL = mURL;
            return this;
        }

        public Builder setmFolder(File mFolder) {
            this.mFolder = mFolder;
            return this;
        }

        public Builder setmName(CharSequence mName) {
            this.mName = mName;
            return this;
        }



        public DownloadConfiguration build(){
            return new DownloadConfiguration(mURL,mFolder,mName);
        }
    }
}
