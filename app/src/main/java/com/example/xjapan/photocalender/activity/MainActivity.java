package com.example.xjapan.photocalender.activity;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.astuetz.PagerSlidingTabStrip;
import com.example.xjapan.photocalender.R;
import com.example.xjapan.photocalender.adapter.MonthPagerAdapter;
import com.example.xjapan.photocalender.db.dao.DailyTopDAO;
import com.example.xjapan.photocalender.model.DailyTop;
import com.example.xjapan.photocalender.util.Common;

public class MainActivity extends FragmentActivity {

    private Common common;
    private FloatingActionButton pencilButton;
    private FloatingActionButton stampButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        common = (Common) getApplication();

        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(new MonthPagerAdapter(getSupportFragmentManager()));

        PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabStrip.setViewPager(viewPager);

        pencilButton = (FloatingActionButton) findViewById(R.id.top_pencil);
        stampButton = (FloatingActionButton) findViewById(R.id.top_stamp);

        stampButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (common.isStamp) {
                    common.isStamp = false;
                    stampButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                } else {
                    common.isStamp = true;
                    common.isPencil = false;
                    stampButton.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
                    pencilButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                }
            }
        });

        pencilButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (common.isPencil) {
                    common.isPencil = false;
                    pencilButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                } else {
                    common.isPencil = true;
                    common.isStamp = false;
                    pencilButton.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
                    stampButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onStampClick(View view) {
        DailyTopDAO dao = DailyTopDAO.get();
        DailyTop dailyTop = dao.getItem(common.year, common.month, common.day);
        int updateStamp;
        switch (view.getId()) {
            case R.id.stamp_image_1:
                updateStamp = R.drawable.heart;
                break;
            case R.id.stamp_image_2:
                updateStamp = R.drawable.heart;
                break;
            case R.id.stamp_image_3:
                updateStamp = R.drawable.heart;
                break;
            case R.id.stamp_image_4:
                updateStamp = R.drawable.heart;
                break;
            case R.id.stamp_image_5:
                updateStamp = R.drawable.heart;
                break;
            case R.id.stamp_image_6:
                updateStamp = R.drawable.heart;
                break;
            case R.id.stamp_image_7:
                updateStamp = R.drawable.heart;
                break;
            case R.id.stamp_image_8:
                updateStamp = R.drawable.heart;
                break;
            case R.id.stamp_image_9:
                updateStamp = R.drawable.heart;
                break;
            default:
                updateStamp = R.drawable.heart;
                break;
        }
        if (dailyTop == null) {
            dao.insertStamp(updateStamp, common.year, common.month, common.day);
        } else {
            dao.updateStamp(updateStamp, common.year, common.month, common.day);
        }
        common.stickyAdapter.notifyDataSetChanged();
        common.stickyGridHeadersGridView.invalidateViews();
        common.alertDialog.dismiss();
    }
}
