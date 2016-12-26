package com.zelo.internal.zelo;

import android.content.Context;
import android.support.annotation.NonNull;

import com.zelo.internal.downloadmanager.core.DownloadConfiguration;
import com.zelo.internal.downloadmanager.core.DownloadStatus;
import com.zelo.internal.downloadmanager.listeners.DownloadListener;
import com.zelo.internal.zelo.model.DownloadInfo;
import com.zelo.internal.zelo.model.ImagesRepository;

import java.util.List;


/**
 * Created by mohan on 4/10/16.
 */

public class ImagesPresenter implements ImagesContract.Presenter {

    private final ImagesRepository mImagesRepository;


    private final ImagesContract.View mImageFragmentView;
    private Context mContext;

    public ImagesPresenter(@NonNull Context context, @NonNull ImagesRepository movieRepository, @NonNull ImagesContract.View movieView) {

        if (null == movieRepository) {
            throw new IllegalArgumentException("movieRepository cannot be null");
        }
        if (null == movieView) {
            throw new IllegalArgumentException("movieView cannot be null!");
        }
        mImagesRepository = movieRepository;
        mImageFragmentView = movieView;
        this.mContext = mContext;

        mImageFragmentView.setPresenter(this);
    }

    @Override
    public void start() {
        mImageFragmentView.setTitle("Downoad manager");
        loadImages();
    }

    private void loadImages() {
        // mImageFragmentView.showProgress();
        mImagesRepository.getImages(new ImageDataSource.LoadImagesCallback() {
            @Override
            public void onImagesLoaded(List<DownloadInfo> images) {

                mImageFragmentView.showImages(images);
            }

            @Override
            public void onImageNotAvailable() {
            }


        });
    }

    @Override
    public void downloadImage(DownloadConfiguration downloadConfiguration, DownloadInfo downloadInfo, int position) {

        mImagesRepository.downLoadImage(mContext, downloadConfiguration, new DownloadCallback(position, downloadInfo));
    }

    @Override
    public void pauseDownload(String tag) {

        mImagesRepository.pauseDownload(tag);
    }

    @Override
    public void pauseAll() {
        mImagesRepository.pauseAll();
    }

    @Override
    public void cancelDownload(String tag) {
        mImagesRepository.cancelDownload(tag);

    }

    @Override
    public void viewImage(String fileLocation) {

        mImageFragmentView.showImage(fileLocation);
    }

    @Override
    public void deleteFiles() {
        mImageFragmentView.showProgress();
        mImagesRepository.deleteFiles(new ImageDataSource.DeleteFileCallback() {
            @Override
            public void onDelete() {
                mImageFragmentView.hideProgress();
                mImageFragmentView.showMessage("Delete successful");
                loadImages();
            }
        });
    }


    class DownloadCallback implements DownloadListener {
        private int mPosition;
        private DownloadInfo downloadInfo;

        public DownloadCallback(int position, DownloadInfo downloadInfo) {
            this.downloadInfo = downloadInfo;
            mPosition = position;
        }

        @Override
        public void onConnected(long totalLength) {


            downloadInfo.setStatus(DownloadStatus.STATUS_CONNECTED);
            downloadInfo.setLength((int) totalLength);
           mImageFragmentView.setStatus(downloadInfo,mPosition);

        }

        @Override
        public void onProgress(long total, long progress) {
            downloadInfo.setStatus(DownloadStatus.STATUS_PROGRESS);
            downloadInfo.setProgress((int) progress);
            mImageFragmentView.setStatus(downloadInfo,mPosition);
        }

        @Override
        public void onDownoadComplete() {
            downloadInfo.setStatus(DownloadStatus.STATUS_COMPLETED);
            mImageFragmentView.setStatus(downloadInfo,mPosition);


        }

        @Override
        public void onDownloadPaused() {
            downloadInfo.setStatus(DownloadStatus.STATUS_PAUSED);
            mImageFragmentView.setStatus(downloadInfo,mPosition);

        }

        @Override
        public void onDownloadCancelled() {
            downloadInfo.setStatus(DownloadStatus.STATUS_CANCELED);
            mImageFragmentView.setStatus(downloadInfo,mPosition);
        }

        @Override
        public void onDownloadFailed() {
            downloadInfo.setStatus(DownloadStatus.STATUS_FAILED);
            mImageFragmentView.setStatus(downloadInfo,mPosition);
        }

    }


}
