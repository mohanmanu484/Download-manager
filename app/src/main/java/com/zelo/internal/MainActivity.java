package com.zelo.internal;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zelo.internal.downloadmanager.core.DownloadConfiguration;
import com.zelo.internal.downloadmanager.core.ZeloDownloadManager;
import com.zelo.internal.downloadmanager.listeners.DownloadListener;

public class MainActivity extends AppCompatActivity implements DownloadListener {

    private ImageView imageView,imageView2;
    private TextView status,status2;
    private ProgressBar progressBar,progressBar2;
    private DownloadConfiguration downloadConfiguration,downloadConfiguration2;



    private String URL="https://upload.wikimedia.org/wikipedia/commons/2/2d/Snake_River_(5mb).jpg";
    private String URL2="http://wendyloefler.com.au/wp-content/uploads/2012/12/Antarctica-7-2MB3.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*status= (TextView) findViewById(R.id.tvStatus);
        progressBar= (ProgressBar) findViewById(R.id.pbProgress);
        imageView= (ImageView) findViewById(R.id.ivImg);
        status2= (TextView) findViewById(R.id.tvStatus2);
        progressBar2= (ProgressBar) findViewById(R.id.pbProgress2);
        imageView2= (ImageView) findViewById(R.id.ivImg2);

        if(!new File(Environment.getExternalStorageDirectory()+"/zelo").isDirectory()){

                new File(Environment.getExternalStorageDirectory()+"/zelo").mkdir();

        }

        downloadConfiguration=new DownloadConfiguration.Builder()
                .setmFolder(new File(Environment.getExternalStorageDirectory(),"Download"))
                .setmName("zelo5mb.jpg")
                .setmURL(URL)
                .build();
        downloadConfiguration2=new DownloadConfiguration.Builder()
                .setmFolder(new File(Environment.getExternalStorageDirectory(),"Download"))
                .setmName("zelo2.jpg")
                .setmURL(URL2)
                .build();*/

    }


    public void pause(View view) {
        ZeloDownloadManager.getInstance(this).pause(URL);
        /*TestMainThread.getInstance().createSecondThread(new MainCallback() {
            @Override
            public void mainCallback() {

            }
        });*/
    }

    public void cancel(View view) {
        ZeloDownloadManager.getInstance(this).cancel(URL);
    }

    public void download(View view) {
        ZeloDownloadManager.getInstance(this).add(downloadConfiguration,this);
    }

    @Override
    public void onConnected(final long totalLength) {
        status.setText("Connected");
        progressBar.setMax((int) totalLength);
    }

    @Override
    public void onProgress(final long total, final long progress) {

        status.setText(progress+"/"+total);
        progressBar.setProgress((int) progress);
    }

    @Override
    public void onDownoadComplete() {


        status.setText("Complete");
        Bitmap bitmap=null;
        try {
            bitmap= BitmapFactory.decodeFile(downloadConfiguration.getmFolder()+"/"+downloadConfiguration.getmName());
        }catch (Exception e){
            e.printStackTrace();
        }
        imageView.setImageBitmap(bitmap);
       /* Looper.prepare();
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                 Bitmap bitmap=null;
                try {
                    bitmap=BitmapFactory.decodeFile(downloadConfiguration.getmFolder()+"/"+downloadConfiguration.getmName());
                }catch (Exception e){
                    e.printStackTrace();
                }

                final Bitmap finalBitmap = bitmap;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(finalBitmap !=null) {
                            status.setText("Complete");
                            imageView.setImageBitmap(finalBitmap);
                        }
                    }
                });
            }
        });*/

    }

    @Override
    public void onDownloadPaused() {

        status.setText("Paused");
        /*runOnUiThread(new Runnable() {
            @Override
            public void run() {
                status.setText("Paused");
            }
        });*/

    }

    @Override
    public void onDownloadCancelled() {
        status.setText("Cancelled");
        progressBar.setProgress(0);
        progressBar.setMax(0);

        /*runOnUiThread(new Runnable() {
            @Override
            public void run() {
                status.setText("Cancelled");
            }
        });*/

    }

    @Override
    public void onDownloadFailed() {

        status.setText("Failed");
       /* runOnUiThread(new Runnable() {
            @Override
            public void run() {
                status.setText("Failed");
               // ZeloDownloadManager.getInstance(MainActivity.this).removeKey(URL);
            }
        });*/

    }
    public void pause2(View view) {
        ZeloDownloadManager.getInstance(this).pause(URL2);
        /*TestMainThread.getInstance().createSecondThread(new MainCallback() {
            @Override
            public void mainCallback() {

            }
        });*/
    }

    public void cancel2(View view) {
        ZeloDownloadManager.getInstance(this).cancel(URL2);
    }

    public void download2(View view) {
        ZeloDownloadManager.getInstance(this).add(downloadConfiguration2, new DownloadListener() {
            @Override
            public void onConnected(final long totalLength) {
                status2.setText("Connected");
                progressBar2.setMax((int) totalLength);
            }

            @Override
            public void onProgress(final long total, final long progress) {

                status2.setText(progress+"/"+total);
                progressBar2.setProgress((int) progress);
            }

            @Override
            public void onDownoadComplete() {


                status2.setText("Complete");
                Bitmap bitmap=null;
                try {
                    bitmap= BitmapFactory.decodeFile(downloadConfiguration.getmFolder()+"/"+downloadConfiguration.getmName());
                }catch (Exception e){
                    e.printStackTrace();
                }
                imageView2.setImageBitmap(bitmap);

            }

            @Override
            public void onDownloadPaused() {

                status2.setText("Paused");

            }

            @Override
            public void onDownloadCancelled() {
                status2.setText("Cancelled");
                progressBar2.setProgress(0);
                progressBar2.setMax(0);

            }

            @Override
            public void onDownloadFailed() {

                status2.setText("Failed");

            }
        });
    }
}
