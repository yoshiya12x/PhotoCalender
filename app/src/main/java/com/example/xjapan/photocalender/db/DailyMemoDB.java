package com.example.xjapan.photocalender.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by xjapan on 16/01/09.
 */
public class DailyMemoDB {

    private String tableName = "dailyMemo";
    private SQLiteHelper helper;

    public DailyMemoDB(Context context) {
        helper = new SQLiteHelper(context);
    }

    public String selectMemo(int year, int month, int day) {
        String[] tableColumn = {"dailyMemoId", "year", "month", "day", "memo"};
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

    public int selectDailyMemoId(int year, int month, int day) {
        String[] tableColumn = {"dailyMemoId", "year", "month", "day", "memo"};
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

    public void insertMemo(int year, int month, int day, String memo) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues insertValues = new ContentValues();
        insertValues.put("year", year + "");
        insertValues.put("month", month + "");
        insertValues.put("day", day + "");
        insertValues.put("memo", memo);
        long id = db.insert(tableName, "00", insertValues);
        db.close();
    }

    public void updateMemo(int year, int month, int day, String memo) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues updateValues = new ContentValues();
        updateValues.put("memo", memo);
        db.update(tableName, updateValues, "year = ? and month = ? and day = ?", new String[]{year + "", month + "", day + ""});
        db.close();
    }

}
