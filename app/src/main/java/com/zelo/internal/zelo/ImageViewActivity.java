package com.zelo.internal.zelo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.zelo.internal.R;

import java.io.File;

public class ImageViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);
        String fileLocation=null;
        try {
            fileLocation=getIntent().getStringExtra("File")+".jpg";
        }catch (NullPointerException e){
            fileLocation=null;
        }

        ImageView imageView= (ImageView) findViewById(R.id.imageView);
       /* Uri uri = Uri.fromFile(fileLocation);*/

       /* Picasso.with(this).load(uri)
                .resize(96, 96).centerCrop().into(viewHolder.image);*/
        Picasso.with(this).load(new File(fileLocation)).placeholder(R.drawable.ic_place_holder).resize(300,300).into(imageView);
    }
}
