package com.zelo.internal;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import com.zelo.internal.zelo.ImagesFragment;
import com.zelo.internal.zelo.ImagesPresenter;
import com.zelo.internal.zelo.model.ImagesRepository;

public class ZeloActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zelo);
        Toolbar toolbar= (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.download);
        setSupportActionBar(toolbar);

        ImagesFragment moviesFragment =
                (ImagesFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (moviesFragment == null) {
            // Create the fragment
            moviesFragment = ImagesFragment.newInstance();
           addFragmentToActivity(
                    getSupportFragmentManager(), moviesFragment, R.id.contentFrame);
        }

       new ImagesPresenter(this,
                new ImagesRepository(this), moviesFragment);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
       // EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    public static void addFragmentToActivity (@NonNull FragmentManager fragmentManager,
                                              @NonNull Fragment fragment, int frameId) {

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(frameId, fragment);
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.optons_menu, menu);
        return true;
    }
}
