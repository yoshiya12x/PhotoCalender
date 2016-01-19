package com.example.xjapan.photocalender;

import android.app.AlertDialog;
import android.app.Application;
import android.graphics.Bitmap;
import android.net.Uri;

/**
 * Created by xjapan on 16/01/10.
 */
public class Common extends Application{

    int year;
    int month;
    int day;
    Uri m_uri;
    Bitmap bm;
    boolean isCamera = false;
    boolean isGallery = false;
    boolean isStamp = false;
    boolean isPencil = false;
    AlertDialog alertDialog;

}
