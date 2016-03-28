package com.example.xjapan.photocalender.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.xjapan.photocalender.fragment.PagerFragment;
import com.example.xjapan.photocalender.fragment.PhotoFragment;

/**
 * Created by xjapan on 15/11/24.
 */
public class MonthPagerAdapter extends FragmentPagerAdapter {

    public MonthPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                Fragment pagerFragment = new PagerFragment();
                Bundle arguments = new Bundle();
                pagerFragment.setArguments(arguments);
                return pagerFragment;
            case 1:
                Fragment photoFragment = new PhotoFragment();
                Bundle arguments2 = new Bundle();
                photoFragment.setArguments(arguments2);
                return photoFragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "カレンダー";
            case 1:
                return "過去の写真";
        }
        return null;
    }
}
