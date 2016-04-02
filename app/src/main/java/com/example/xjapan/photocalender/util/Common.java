package com.example.xjapan.photocalender.util;

import android.app.AlertDialog;
import android.app.Application;
import android.graphics.Bitmap;
import android.net.Uri;

import com.example.xjapan.photocalender.adapter.StickyAdapter;
import com.example.xjapan.photocalender.db.dao.DAOInjector;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersGridView;

/**
 * Created by xjapan on 16/01/10.
 */
public class Common extends Application {

    public int year;
    public int month;
    public int day;
    public Uri m_uri;
    public Bitmap bm;
    public boolean isCamera = false;
    public boolean isGallery = false;
    public boolean isStamp = false;
    public boolean isPencil = false;
    public AlertDialog alertDialog;
    public StickyGridHeadersGridView stickyGridHeadersGridView;
    public StickyAdapter stickyAdapter;

    @Override
    public void onCreate() {
        super.onCreate();
        DAOInjector.inject(this);
    }

    @Override
    public void onTerminate() {
        DAOInjector.leave();
        super.onTerminate();
    }
}
