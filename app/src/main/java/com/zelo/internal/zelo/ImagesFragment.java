package com.zelo.internal.zelo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.zelo.internal.R;
import com.zelo.internal.downloadmanager.core.DownloadConfiguration;
import com.zelo.internal.downloadmanager.core.DownloadStatus;
import com.zelo.internal.zelo.model.DownloadInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mohan on 4/10/16.
 */

public class ImagesFragment extends BaseFragment implements ImagesContract.View, OnItemClickListener<DownloadInfo> {

    private ImagesContract.Presenter mPresenter;
    private ActionBar mActionBar;
    private ImagesAdapter imagesAdapter;
    private static final String TAG = "ImagesFragment";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imagesAdapter = new ImagesAdapter(getContext(), new ArrayList<DownloadInfo>(0));
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.pauseAll();
    }

    RecyclerView imagesList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.image_list_fragment, container, false);
        imagesList = (RecyclerView) view.findViewById(R.id.rvImagesList);
        imagesList.setHasFixedSize(true);
        imagesList.setLayoutManager(new LinearLayoutManager(getContext()));
        imagesAdapter = new ImagesAdapter(getContext(), new ArrayList<DownloadInfo>(0));
        imagesAdapter.setmListener(this);
        imagesList.setAdapter(imagesAdapter);
        //imagesAdapter.update(Constants.getImagesList());
        return view;
    }

    private ImagesAdapter.ImagesHoler getViewHolder(int position) {
        return (ImagesAdapter.ImagesHoler) imagesList.findViewHolderForLayoutPosition(position);
    }

    @Override
    public void showProgress() {
        super.showProgress();
    }

    @Override
    public void hideProgress() {
        super.hideProgress();
    }

    @Override
    public void setTitle(String title) {
        ((AppCompatActivity) getContext()).getSupportActionBar().setTitle(title);
    }

    @Override
    public void showImages(List<DownloadInfo> downloadInfos) {

        imagesAdapter.update(downloadInfos);

    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void showNetworkError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showImage(String fileLocation) {

        Intent intent = new Intent(getContext(), ImageViewActivity.class);
        intent.putExtra("File", fileLocation);
        startActivity(intent);

    }

    @Override
    public void setPresenter(@NonNull ImagesContract.Presenter presenter) {
        mPresenter = presenter;
    }

    public static ImagesFragment newInstance() {
        return new ImagesFragment();
    }


    @Override
    public void onItemClick(View v, int position, DownloadInfo downloadInfo) {
        if (downloadInfo.getStatus() == DownloadStatus.STATUS_CONNECTED || downloadInfo.getStatus() == DownloadStatus.STATUS_PROGRESS) {
            pauseDownload(downloadInfo.getUrl());
        } else if (downloadInfo.getStatus() == DownloadStatus.STATUS_COMPLETED) {
            viewImage(downloadInfo);
        } else {
            downloadImage(position, downloadInfo.getUrl(), downloadInfo);
        }
    }


    private void downloadImage(final int position, String tag, final DownloadInfo downloadInfo) {

        ImagesAdapter.ImagesHoler holder = getViewHolder(position);
        holder.downloadStatus.setEnabled(false);
        downloadInfo.setStatus(DownloadStatus.STATUS_STARTED);
        mPresenter.downloadImage(new DownloadConfiguration.Builder()
                .setmFolder(new File(Environment.getExternalStorageDirectory(), "Download"))
                .setmName(downloadInfo.getName() + ".jpg")
                .setmURL(downloadInfo.getUrl())
                .build(), downloadInfo, position);
    }

    private void pauseDownload(String tag) {
        mPresenter.pauseDownload(tag);
    }

    private void viewImage(DownloadInfo downloadInfo) {
        Log.d(TAG, "viewImage: " + new File(Environment.getExternalStorageDirectory(), "Download").getAbsolutePath() + "/" + downloadInfo.getName());
        ;
        mPresenter.viewImage(new File(Environment.getExternalStorageDirectory(), "Download").getAbsolutePath() + "/" + downloadInfo.getName());
    }


    private boolean isCurrentListViewItemVisible(int position) {
        LinearLayoutManager layoutManager = (LinearLayoutManager) imagesList.getLayoutManager();
        int first = layoutManager.findFirstVisibleItemPosition();
        int last = layoutManager.findLastVisibleItemPosition();
        return first <= position && position <= last;
    }

    public void setStatus(DownloadInfo downloadInfo, int mPosition) {
        ImagesAdapter.ImagesHoler holder = getViewHolder(mPosition);

        //downloadInfo.setStatus(DownloadStatus.STATUS_CONNECTED);


        switch (downloadInfo.getStatus()) {


            case DownloadStatus.STATUS_CONNECTED:


                downloadInfo.setLength((int) downloadInfo.getLength());
                if (isCurrentListViewItemVisible(mPosition)) {
                    holder.downloadStatus.setEnabled(true);
                    holder.downloadStatus.setText(downloadInfo.getButtonText());
                    holder.progressBar.setVisibility(View.VISIBLE);
                    holder.progressBar.setMax(downloadInfo.getLength());
                }

                break;
            case DownloadStatus.STATUS_PROGRESS:

                if (isCurrentListViewItemVisible(mPosition)) {
                  //  holder.downloadStatus.setEnabled(holder.downloadStatus.isEnabled()?f);
                    holder.downloadStatus.setEnabled(true);
                    holder.progressBar.setProgress(downloadInfo.getProgress());
                    holder.progressView.setText(downloadInfo.getProgress()+"/"+downloadInfo.getLength());

                }


                break;
            case DownloadStatus.STATUS_PAUSED:
                if (isCurrentListViewItemVisible(mPosition)) {
                    holder.downloadStatus.setEnabled(true);
                    holder.progressBar.setVisibility(View.VISIBLE);
                    holder.downloadStatus.setText(downloadInfo.getButtonText());
                }


                break;
            case DownloadStatus.STATUS_FAILED:
                if (isCurrentListViewItemVisible(mPosition)) {
                    holder.downloadStatus.setEnabled(true);
                    holder.progressBar.setVisibility(View.GONE);
                    holder.downloadStatus.setText(downloadInfo.getButtonText());
                }
                imagesAdapter.update(downloadInfo, mPosition);


                break;
            case DownloadStatus.STATUS_COMPLETED:
                if (isCurrentListViewItemVisible(mPosition)) {
                    holder.downloadStatus.setEnabled(true);
                    holder.progressBar.setVisibility(View.GONE);
                    holder.progressView.setVisibility(View.GONE);
                    holder.downloadStatus.setText(downloadInfo.getButtonText());
                }
                imagesAdapter.update(downloadInfo, mPosition);


                break;
            case DownloadStatus.STATUS_CANCELED:
                if (isCurrentListViewItemVisible(mPosition)) {
                    holder.downloadStatus.setEnabled(true);
                    holder.progressBar.setVisibility(View.GONE);
                    holder.downloadStatus.setText(downloadInfo.getButtonText());
                }


                break;


        }
    }


}
