package com.example.xjapan.photocalender.model;

import android.support.annotation.Nullable;

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
    public int year;

    @Column(indexed = true)
    public int month;

    @Column(indexed = true)
    public int day;

    @Nullable
    @Column(indexed = true)
    public String path;

    @Nullable
    @Column(indexed = true)
    public int stamp;

    @Nullable
    @Column(indexed = true)
    public String titleMemo;

    @Nullable
    @Column(indexed = true)
    public int flag;

    public DailyTop() {
    }
}
