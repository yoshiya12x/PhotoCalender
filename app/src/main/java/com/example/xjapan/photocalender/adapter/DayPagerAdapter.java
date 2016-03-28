package com.example.xjapan.photocalender.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.xjapan.photocalender.model.CalenderList;
import com.example.xjapan.photocalender.fragment.DayPagerFragment;

/**
 * Created by xjapan on 16/01/08.
 */
public class DayPagerAdapter extends FragmentPagerAdapter {

    private CalenderList calenderList;
    private int currentDay;

    public DayPagerAdapter(FragmentManager fm, CalenderList calenderList, int currentDay) {
        super(fm);
        this.calenderList = calenderList;
        this.currentDay = currentDay;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new DayPagerFragment();
        fragment.setArguments(DayPagerFragment.createArguments(position, calenderList));
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
