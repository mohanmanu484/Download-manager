package com.zelo.internal.zelo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zelo.internal.R;
import com.zelo.internal.downloadmanager.core.DownloadStatus;
import com.zelo.internal.zelo.model.DownloadInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mohan on 26/12/16.
 */

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.ImagesHoler> {


    public List<DownloadInfo> downloadInfos;
    private Context context;
    private OnItemClickListener<DownloadInfo> mListener;

    public ImagesAdapter(Context context,ArrayList<DownloadInfo> downloadInfos) {
        this.downloadInfos = downloadInfos;
        this.context=context;
    }

    public void setmListener(OnItemClickListener<DownloadInfo> mListener) {
        this.mListener = mListener;
    }

    @Override
    public ImagesHoler onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.image_single_item,parent,false);
        return new ImagesHoler(view);
    }

    @Override
    public void onBindViewHolder(ImagesHoler holder, final int position) {

        DownloadInfo downloadInfo=downloadInfos.get(position);
        holder.downloadStatus.setText(downloadInfo.getButtonText());
        holder.imageName.setText(downloadInfo.getName());
        holder.downloadStatus.setEnabled(downloadInfo.getStatus()==DownloadStatus.STATUS_STARTED?false:true);
        if(downloadInfo.getStatus()== DownloadStatus.STATUS_PROGRESS){
            holder.progressBar.setVisibility(View.VISIBLE);
            holder.progressBar.setMax(downloadInfo.getLength());
            holder.progressBar.setProgress(downloadInfo.getProgress());
            holder.progressView.setVisibility(View.VISIBLE);
            holder.progressView.setText(downloadInfo.getProgress()+"/"+downloadInfo.getLength());
        }else {
            holder.progressView.setVisibility(View.GONE);
            holder.progressBar.setVisibility(View.GONE);
        }


        holder.downloadStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick(v, position, downloadInfos.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return downloadInfos.size();
    }

    public void update(List<DownloadInfo> downloadInfos) {
        this.downloadInfos.clear();
        this.downloadInfos = downloadInfos;
        notifyDataSetChanged();
    }

    public void update(DownloadInfo downloadInfo,int position){
        downloadInfos.set(position,downloadInfo);
        notifyDataSetChanged();
    }

    public class ImagesHoler extends RecyclerView.ViewHolder{

        ImageView downloadImage;
        TextView imageName;
        TextView downloadStatus;
        ProgressBar progressBar;
        TextView progressView;
        public ImagesHoler(View itemView) {
            super(itemView);
            downloadImage= (ImageView) itemView.findViewById(R.id.ivDownload);
            imageName= (TextView) itemView.findViewById(R.id.tvDownloadName);
            downloadStatus= (TextView) itemView.findViewById(R.id.downloadStatus);
            progressBar= (ProgressBar) itemView.findViewById(R.id.progressbar);
            progressView= (TextView) itemView.findViewById(R.id.tvProgress);
        }
    }


}
