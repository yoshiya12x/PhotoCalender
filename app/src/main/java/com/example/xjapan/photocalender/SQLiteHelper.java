package com.example.xjapan.photocalender;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by xjapan on 16/01/09.
 */

public class SQLiteHelper extends SQLiteOpenHelper {

    static final String DatabaseName = "PhotoCalenderDB";
    static final int DatabaseVersion = 6;

    public SQLiteHelper(Context mContext) {
        super(mContext, DatabaseName, null, DatabaseVersion);
    }

    public SQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table `dailyMemo` ( `dailyMemoId` integer primary key, `year` varchar(255), `month` varchar(255), `day` varchar(255), `memo` varchar(255));");
        db.execSQL("create table `dailyImage` ( `dailyImageId` integer primary key, `year` varchar(255), `month` varchar(255), `day` varchar(255), `path` varchar(255));");
        db.execSQL("create table `dailyStamp` ( `dailyStampId` integer primary key, `year` varchar(255), `month` varchar(255), `day` varchar(255), `stamp` varchar(255));");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table `dailyMemo`;");
        db.execSQL("drop table `dailyImage`;");
        db.execSQL("drop table `dailyStamp`;");
        db.execSQL("create table `dailyMemo` ( `dailyMemoId` integer primary key, `year` varchar(255), `month` varchar(255), `day` varchar(255), `memo` varchar(255));");
        db.execSQL("create table `dailyImage` ( `dailyImageId` integer primary key, `year` varchar(255), `month` varchar(255), `day` varchar(255), `path` varchar(255));");
        db.execSQL("create table `dailyStamp` ( `dailyStampId` integer primary key, `year` varchar(255), `month` varchar(255), `day` varchar(255), `stamp` varchar(255));");

    }
}