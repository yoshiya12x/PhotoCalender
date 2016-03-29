package com.example.xjapan.photocalender.model;

import com.github.gfx.android.orma.annotation.Column;
import com.github.gfx.android.orma.annotation.PrimaryKey;
import com.github.gfx.android.orma.annotation.Table;

/**
 * Created by xjapan on 16/03/28.
 */

@Table
public class DailyTop {
    @PrimaryKey(auto = true)
    @Column(indexed = true)
    public int dailyTopId;

    @Column(indexed = true)
    public String year;

    @Column(indexed = true)
    public String month;

    @Column(indexed = true)
    public String day;

    @Column(indexed = true)
    public String path;

    @Column(indexed = true)
    public String stamp;

    @Column(indexed = true)
    public String titleMemo;

    @Column(indexed = true)
    public String flag;
}
