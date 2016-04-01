package com.example.xjapan.photocalender.model;

import com.github.gfx.android.orma.annotation.Column;
import com.github.gfx.android.orma.annotation.PrimaryKey;
import com.github.gfx.android.orma.annotation.Table;

/**
 * Created by xjapan on 16/04/01.
 */

@Table
public class DailyMemo {
    @PrimaryKey(auto = true)
    @Column(indexed = true)
    public int dailyMemoId;

    @Column(indexed = true)
    public int year;

    @Column(indexed = true)
    public int month;

    @Column(indexed = true)
    public int day;

    @Column(indexed = true)
    public String memo;
}
