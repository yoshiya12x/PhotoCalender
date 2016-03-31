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

    public DailyTop getItem(int year, int month, int day){
        assert orma != null;
        return orma.selectFromDailyTop()
                .yearEq(year)
                .monthEq(month)
                .dayEq(day)
                .getOrNull(0);
    }

    public long insertStamp(int stamp){
        DailyTop dailyTop = new DailyTop();
        dailyTop.stamp = stamp;
        return orma.insertIntoDailyTop(dailyTop);
    }

    public long updateStamp(int stamp, int year, int month, int day){
        return orma.updateDailyTop()
                .yearEq(year)
                .month(month)
                .dayEq(day)
                .stamp(stamp)
                .execute();
    }
}
