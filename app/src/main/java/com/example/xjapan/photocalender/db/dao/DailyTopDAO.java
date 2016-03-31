package com.example.xjapan.photocalender.db.dao;

import com.example.xjapan.photocalender.model.DailyTop;
import com.example.xjapan.photocalender.model.OrmaDatabase;

/**
 * Created by xjapan on 16/03/28.
 */
public class DailyTopDAO {
    private static final DailyTopDAO dao = new DailyTopDAO();
    private OrmaDatabase orma;

    DailyTopDAO() {
    }

    static void inject(OrmaDatabase orma) {
        dao.orma = orma;
    }

    static void leave() {
        dao.orma = null;
    }

    public static DailyTopDAO get() {
        return dao;
    }

    public DailyTop getItem(int year, int month, int day) {
        assert orma != null;
        return orma.selectFromDailyTop()
                .yearEq(year)
                .monthEq(month)
                .dayEq(day)
                .getOrNull(0);
    }

    public long insertStamp(int stamp, int year, int month, int day) {
        DailyTop dailyTop = new DailyTop();
        dailyTop.year = year;
        dailyTop.month = month;
        dailyTop.day = day;
        dailyTop.stamp = stamp;
        dailyTop.flag = 0;
        return orma.insertIntoDailyTop(dailyTop);
    }

    public long updateStamp(int stamp, int year, int month, int day) {
        return orma.updateDailyTop()
                .yearEq(year)
                .month(month)
                .dayEq(day)
                .stamp(stamp)
                .flag(0)
                .execute();
    }

    public long insertTitleMemo(String titleMemo, int year, int month, int day) {
        DailyTop dailyTop = new DailyTop();
        dailyTop.year = year;
        dailyTop.month = month;
        dailyTop.day = day;
        dailyTop.titleMemo = titleMemo;
        dailyTop.flag = 1;
        return orma.insertIntoDailyTop(dailyTop);
    }

    public long updateTitleMemo(String titleMemo, int year, int month, int day) {
        return orma.updateDailyTop()
                .yearEq(year)
                .monthEq(month)
                .dayEq(day)
                .titleMemo(titleMemo)
                .execute();
    }

    public long insertPath(String path, int year, int month, int day){
        DailyTop dailyTop = new DailyTop();
        dailyTop.year = year;
        dailyTop.month = month;
        dailyTop.day = day;
        dailyTop.path = path;
        return orma.insertIntoDailyTop(dailyTop);
    }

    public long updatePath(String path, int year, int month, int day){
        return orma.updateDailyTop()
                .yearEq(year)
                .monthEq(month)
                .dayEq(day)
                .path(path)
                .execute();
    }

}
