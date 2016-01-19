package com.example.xjapan.photocalender;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by xjapan on 16/01/08.
 */
public class DayPagerAdapter extends FragmentPagerAdapter {

    private CalenderList calenderList;
    private int selectedDay;
    private int currentDay;

    public DayPagerAdapter(FragmentManager fm, CalenderList calenderList, int selectedDay, int currentDay) {
        super(fm);
        this.calenderList = calenderList;
        this.selectedDay = selectedDay;
        this.currentDay = currentDay;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new DayPagerFragment();
        Bundle arguments = new Bundle();
        arguments.putInt("day", position+1);
        arguments.putInt("year", calenderList.year);
        arguments.putInt("month", calenderList.month);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public int getCount() {
        return calenderList.days;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String dotto = "";
        if (position + 1 == currentDay) {
            dotto = "・";
        }
        return position + 1 + "日" + dotto;
    }
}
