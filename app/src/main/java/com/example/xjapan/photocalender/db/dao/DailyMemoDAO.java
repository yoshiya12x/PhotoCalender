package com.example.xjapan.photocalender.db.dao;

import com.example.xjapan.photocalender.model.DailyMemo;
import com.example.xjapan.photocalender.model.OrmaDatabase;

/**
 * Created by xjapan on 16/04/01.
 */
public class DailyMemoDAO {
    private static final DailyMemoDAO dao = new DailyMemoDAO();
    private OrmaDatabase orma;

    DailyMemoDAO() {
    }

    static void inject(OrmaDatabase orma) {
        dao.orma = orma;
    }

    static void leave() {
        dao.orma = null;
    }

    public static DailyMemoDAO get(){
        return dao;
    }

    public DailyMemo getItem(int year, int month, int day) {
        return orma.selectFromDailyMemo()
                .yearEq(year)
                .monthEq(month)
                .dayEq(day)
                .getOrNull(0);
    }

    public long insertMemo(String memo, int year, int month, int day) {
        DailyMemo dailyMemo = new DailyMemo();
        dailyMemo.year = year;
        dailyMemo.month = month;
        dailyMemo.day = day;
        dailyMemo.memo = memo;
        return orma.insertIntoDailyMemo(dailyMemo);
    }

    public long updateMemo(String memo, int year, int month, int day) {
        return orma.updateDailyMemo()
                .yearEq(year)
                .monthEq(month)
                .dayEq(day)
                .memo(memo)
                .execute();
    }
}
