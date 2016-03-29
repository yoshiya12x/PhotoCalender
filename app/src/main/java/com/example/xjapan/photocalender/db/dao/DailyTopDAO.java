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

    static void inject(OrmaDatabase orma){
        dao.orma = orma;
    }

    static void leave(){
        dao.orma = null;
    }

    public static DailyTopDAO get(){
        return dao;
    }

    public DailyTop findFirst(){
        assert orma != null;
        return orma.selectFromDailyTop().getOrNull(0);
    }

    public List<DailyTop> findAll(){
        assert orma != null;
        return orma.selectFromDailyTop().toList();
    }

}