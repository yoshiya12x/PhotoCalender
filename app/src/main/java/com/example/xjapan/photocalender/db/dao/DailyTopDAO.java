package com.example.xjapan.photocalender.db.dao;

import com.example.xjapan.photocalender.model.DailyTop;
import com.example.xjapan.photocalender.model.OrmaDatabase;

import java.util.List;

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

    public DailyTop findFirst() {
        assert orma != null;
        return orma.selectFromDailyTop().getOrNull(0);
    }

    public List<DailyTop> findAll() {
        assert orma != null;
        return orma.selectFromDailyTop().toList();
    }

}
