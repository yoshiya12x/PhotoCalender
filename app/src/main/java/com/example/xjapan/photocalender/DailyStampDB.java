package com.example.xjapan.photocalender;

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
public class DailyStampDB {

    private String tableName = "dailyStamp";
    private SQLiteHelper helper;

    public DailyStampDB(Context context) {
        helper = new SQLiteHelper(context);
    }

    public String selectStamp(int year, int month, int day) {
        String[] tableColumn = {"dailyStampId", "year", "month", "day", "stamp", "updated"};
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

    public int selectDailyStampId(int year, int month, int day) {
        String[] tableColumn = {"dailyStampId", "year", "month", "day", "stamp", "updated"};
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

    public void insertStamp(int year, int month, int day, int stamp) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues insertValues = new ContentValues();
        insertValues.put("year", year + "");
        insertValues.put("month", month + "");
        insertValues.put("day", day + "");
        insertValues.put("stamp", stamp + "");
        insertValues.put("updated", getDateTime());
        long id = db.insert(tableName, "00", insertValues);
        db.close();
    }

    public void updateStamp(int year, int month, int day, int stamp) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues updateValues = new ContentValues();
        updateValues.put("stamp", stamp + "");
        updateValues.put("updated", getDateTime());
        db.update(tableName, updateValues, "year = ? and month = ? and day = ?", new String[]{year + "", month + "", day + ""});
        db.close();
    }

    public String selectUpdated(int year, int month, int day) {
        String[] tableColumn = {"dailyStampId", "year", "month", "day", "stamp", "updated"};
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
