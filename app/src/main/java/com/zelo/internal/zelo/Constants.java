package com.zelo.internal.zelo;

import com.zelo.internal.zelo.model.DownloadInfo;

import java.util.ArrayList;

/**
 * Created by mohan on 26/12/16.
 */

public class Constants {


    public static final String END_POINTS[] = new String[]{"https://upload.wikimedia.org/wikipedia/commons/2/2d/Snake_River_(5mb).jpg",
            "http://www.kenrockwell.com/nikon/d750/sample-images/750_0141-00100.JPG",
            "http://www.letsgodigital.org/images/artikelen/39/k20d-image.jpg",
            "https://gallery01.digitalrev.com/2042916/cover_bg/d0e4749d7d24477e804422beff834b02.jpg",
            "http://www.gamepur.com/files/images/2012/Watch-Dogs-Game-Wallpaper.jpg", "http://www.theluckywonders.com/uploads/6/3/1/0/6310870/tlwjumping2.7mb.jpeg",
            "http://www.trazeetravel.com/wp-content/uploads/2016/04/Daintree-National-Park-%C2%A9-Dirk-Ercken-Dreamstime-8mb-e1461353658953.jpg",
            "https://pacificnorthwesttravelerdotcom.files.wordpress.com/2013/02/pink-flower-21.jpg",
            "http://www.casualconnect.org/content/Magazine/CC_Cover_JoeKresoja.jpg",
            "http://imagescdn.tweaktown.com/news/4/7/47445_4_metal-gear-solid-pc-disc-contains-8mb-install-file.jpg"

    };


    public static ArrayList<DownloadInfo> getImagesList() {

        ArrayList<DownloadInfo> downloadInfos = new ArrayList<>();

        for (int i = 0; i < END_POINTS.length; i++) {
            downloadInfos.add(new DownloadInfo(""+i, END_POINTS[i], "Image "+i));
        }

        return downloadInfos;
    }
}
