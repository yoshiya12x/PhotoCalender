package com.example.xjapan.photocalender.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by xjapan on 16/01/19.
 */
public class DailyTitleMemoDB {

    private String tableName = "dailyTitleMemo";
    private SQLiteHelper helper;

    public DailyTitleMemoDB(Context context) {
        helper = new SQLiteHelper(context);
    }

    public String selectTitleMemo(int year, int month, int day) {
        String[] tableColumn = {"dailyTitleMemoId", "year", "month", "day", "titleMemo", "updated"};
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.query(tableName, tableColumn, "year = ? and month = ? and day = ?", new String[]{year + "", month + "", day + ""}, null, null, null);
        boolean mov = cursor.moveToFirst();
        String query = "";
        while (mov) {
            query = cursor.getString(4);
            mov = cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return query;
    }

    public int selectDailyTitleMemoId(int year, int month, int day) {
        String[] tableColumn = {"dailyTitleMemoId", "year", "month", "day", "titleMemo", "updated"};
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

    public void insertTitleMemo(int year, int month, int day, String titleMemo) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues insertValues = new ContentValues();
        insertValues.put("year", year + "");
        insertValues.put("month", month + "");
        insertValues.put("day", day + "");
        insertValues.put("titleMemo", titleMemo);
        insertValues.put("updated", getDateTime());
        long id = db.insert(tableName, "00", insertValues);
        db.close();
    }

    public void updateTitleMemo(int year, int month, int day, String titleMemo) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues updateValues = new ContentValues();
        updateValues.put("titleMemo", titleMemo);
        updateValues.put("updated", getDateTime());
        db.update(tableName, updateValues, "year = ? and month = ? and day = ?", new String[]{year + "", month + "", day + ""});
        db.close();
    }

    public String selectUpdated(int year, int month, int day) {
        String[] tableColumn = {"dailyTitleMemoId", "year", "month", "day", "titleMemo", "updated"};
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.query(tableName, tableColumn, "year = ? and month = ? and day = ?", new String[]{year + "", month + "", day + ""}, null, null, null);
        boolean mov = cursor.moveToFirst();
        String query = "";
        while (mov) {
            query = cursor.getString(5);
            mov = cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return query;
    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
}
