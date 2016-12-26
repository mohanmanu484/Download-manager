package com.zelo.internal.zelo;

import com.zelo.internal.downloadmanager.core.DownloadConfiguration;
import com.zelo.internal.zelo.model.DownloadInfo;

import java.util.List;

/**
 * Created by mohan on 4/10/16.
 */

public interface ImagesContract {


    interface View extends BaseView<Presenter>{
        void showProgress();
        void hideProgress();
        void setTitle(String title);
        void showImages(List<DownloadInfo> downloadInfos);
        boolean isActive();
        void showNetworkError(String message);

        void showMessage(String message);
        void showImage(String fileLocation);
        void setStatus(DownloadInfo downloadInfo,int mPosition);
    }
    interface Presenter extends BasePresenter{
        void downloadImage(DownloadConfiguration downloadConfiguration, DownloadInfo downloadInfo,int pos);
        void pauseDownload(String tag);
        void pauseAll();
        void cancelDownload(String tag);
        void viewImage(String location);

        void deleteFiles();
    }


}
