package com.example.xjapan.photocalender.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by xjapan on 16/01/09.
 */

public class SQLiteHelper extends SQLiteOpenHelper {

    static final String DatabaseName = "PhotoCalenderDB";
    static final int DatabaseVersion = 22;

    public SQLiteHelper(Context mContext) {
        super(mContext, DatabaseName, null, DatabaseVersion);
    }

    public SQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //3step目のメモ用
        db.execSQL("create table `dailyMemo` ( `dailyMemoId` integer primary key, `year` varchar(255), `month` varchar(255), `day` varchar(255), `memo` varchar(255));");
        //top画面 flag(0はstamp,1はtitleMemo)
        db.execSQL("create table `dailyTop` ( `dailyTopId` integer primary key, `year` varchar(255), `month` varchar(255), `day` varchar(255), `path` varchar(255), `stamp` varchar(255), `titleMemo` varchar(255), `flag` varchar(255));");
//        db.execSQL("create table `dailyImage` ( `dailyImageId` integer primary key, `year` varchar(255), `month` varchar(255), `day` varchar(255), `path` varchar(255));");
//        db.execSQL("create table `dailyStamp` ( `dailyStampId` integer primary key, `year` varchar(255), `month` varchar(255), `day` varchar(255), `stamp` varchar(255), `stamp` varchar(255), `updated` TEXT DEFAULT CURRENT_TIMESTAMP);");
//        db.execSQL("create table `dailyTitleMemo` ( `dailyTitleMemoId` integer primary key, `year` varchar(255), `month` varchar(255), `day` varchar(255), `titleMemo` varchar(255), `updated` TEXT DEFAULT CURRENT_TIMESTAMP);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table `dailyMemo`;");
        db.execSQL("drop table `dailyTop`;");
        db.execSQL("create table `dailyMemo` ( `dailyMemoId` integer primary key, `year` varchar(255), `month` varchar(255), `day` varchar(255), `memo` varchar(255));");
        db.execSQL("create table `dailyTop` ( `dailyTopId` integer primary key, `year` varchar(255), `month` varchar(255), `day` varchar(255), `path` varchar(255), `stamp` varchar(255), `titleMemo` varchar(255), `flag` varchar(255));");
    }
}