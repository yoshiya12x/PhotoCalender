package com.example.xjapan.photocalender.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.xjapan.photocalender.db.SQLiteHelper;

import java.util.ArrayList;

/**
 * Created by xjapan on 16/01/31.
 */
public class DailyTopDB {

    private String tableName = "dailyTop";
    private SQLiteHelper helper;

    public DailyTopDB(Context context) {
        helper = new SQLiteHelper(context);
    }

    public ArrayList<String> selectAll(int year, int month, int day) {
        String[] tableColumn = {"dailyTopId", "year", "month", "day", "path", "stamp", "titleMemo", "flag"};
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.query(tableName, tableColumn, "year = ? and month = ? and day = ?", new String[]{year + "", month + "", day + ""}, null, null, null);
        boolean mov = cursor.moveToFirst();
        ArrayList<String> query = new ArrayList<>();
        while (mov) {
            query.add(cursor.getString(4));
            query.add(cursor.getString(5));
            query.add(cursor.getString(6));
            query.add(cursor.getString(7));
            mov = cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return query;
    }

    public int selectId(int year, int month, int day) {
        String[] tableColumn = {"dailyTopId", "year", "month", "day", "path", "stamp", "titleMemo", "flag"};
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.query(tableName, tableColumn, "year = ? and month = ? and day = ?", new String[]{year + "", month + "", day + ""}, null, null, null);
        boolean mov = cursor.moveToFirst();
        int query = 0;
        while (mov) {
            query = cursor.getInt(0);
            mov = cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return query;
    }

    public void insertPath(int year, int month, int day, String path) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues insertValues = new ContentValues();
        insertValues.put("year", year + "");
        insertValues.put("month", month + "");
        insertValues.put("day", day + "");
        insertValues.put("path", path);
        long id = db.insert(tableName, "00", insertValues);
        db.close();
    }

    public void insertStamp(int year, int month, int day, int stamp) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues insertValues = new ContentValues();
        insertValues.put("year", year + "");
        insertValues.put("month", month + "");
        insertValues.put("day", day + "");
        insertValues.put("stamp", stamp + "");
        insertValues.put("flag", "0");
        long id = db.insert(tableName, "00", insertValues);
        db.close();
    }

    public void insertTitleMemo(int year, int month, int day, String titleMemo) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues insertValues = new ContentValues();
        insertValues.put("year", year + "");
        insertValues.put("month", month + "");
        insertValues.put("day", day + "");
        insertValues.put("titleMemo", titleMemo);
        insertValues.put("flag", "1");
        long id = db.insert(tableName, "00", insertValues);
        db.close();
    }

    public void updatePath(int year, int month, int day, String path) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues updateValues = new ContentValues();
        updateValues.put("path", path);
        db.update(tableName, updateValues, "year = ? and month = ? and day = ?", new String[]{year + "", month + "", day + ""});
        db.close();
    }

    public void updateStamp(int year, int month, int day, int stamp) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues updateValues = new ContentValues();
        updateValues.put("stamp", stamp + "");
        updateValues.put("flag", "0");
        db.update(tableName, updateValues, "year = ? and month = ? and day = ?", new String[]{year + "", month + "", day + ""});
        db.close();
    }

    public void updateTitleMemo(int year, int month, int day, String titleMemo) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues updateValues = new ContentValues();
        updateValues.put("titleMemo", titleMemo);
        updateValues.put("flag", "1");
        db.update(tableName, updateValues, "year = ? and month = ? and day = ?", new String[]{year + "", month + "", day + ""});
        db.close();
    }

}
