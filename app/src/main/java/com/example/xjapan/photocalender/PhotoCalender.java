package com.example.xjapan.photocalender;

import android.app.Application;

import com.example.xjapan.photocalender.db.dao.DAOInjector;

/**
 * Created by xjapan on 16/03/31.
 */
public class PhotoCalender extends Application {
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
