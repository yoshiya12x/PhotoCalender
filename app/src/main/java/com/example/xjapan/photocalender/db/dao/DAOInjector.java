package com.example.xjapan.photocalender.db.dao;

import android.app.Application;

import com.example.xjapan.photocalender.model.OrmaDatabase;
import com.github.gfx.android.orma.AccessThreadConstraint;

/**
 * Created by xjapan on 16/03/29.
 */
public class DAOInjector {
    private DAOInjector() {
    }

    public static void inject(Application app) {
        OrmaDatabase orma = OrmaDatabase.builder(app).
                readOnMainThread(AccessThreadConstraint.WARNING).
                writeOnMainThread(AccessThreadConstraint.WARNING).
                build();

        DailyTopDAO.inject(orma);
    }

    public static void leave() {
        DailyTopDAO.leave();
    }
}
