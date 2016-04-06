package com.example.xjapan.photocalender.activity;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.astuetz.PagerSlidingTabStrip;
import com.example.xjapan.photocalender.R;
import com.example.xjapan.photocalender.adapter.DayPagerAdapter;
import com.example.xjapan.photocalender.asyncTask.SetDialogImage;
import com.example.xjapan.photocalender.db.dao.DailyTopDAO;
import com.example.xjapan.photocalender.model.CalenderList;
import com.example.xjapan.photocalender.model.DailyTop;
import com.example.xjapan.photocalender.util.Common;

public class DayDetailActivity extends AppCompatActivity {

    private Common common;
    private CalenderList calenderList;
    private int currentDay;
    private DailyTopDAO dao = DailyTopDAO.get();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_detail);
        calenderList = new CalenderList();
        calenderList.year = getIntent().getIntExtra("calenderListYear", 0);
        calenderList.month = getIntent().getIntExtra("calenderListMonth", 0);
        calenderList.days = getIntent().getIntExtra("calenderListDays", 0);
        calenderList.startDay = getIntent().getIntExtra("calenderListStartDay", 0);
        int selectedDay = getIntent().getIntExtra("selectedDay", 0);
        currentDay = getIntent().getIntExtra("currentDay", 0);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(calenderList.year + "年" + calenderList.month + "月");
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        setViewPager(selectedDay);
    }

    public void setViewPager(int selectedDay) {
        ViewPager viewPager = (ViewPager) findViewById(R.id.day_pager);
        viewPager.setAdapter(new DayPagerAdapter(getSupportFragmentManager(), calenderList, currentDay));
        viewPager.setCurrentItem(selectedDay - 1);
        PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip) findViewById(R.id.day_tabs);
        tabStrip.setViewPager(viewPager);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        boolean result = true;
        switch (id) {
            case android.R.id.home:
                finish();
                break;
            default:
                result = super.onOptionsItemSelected(item);
        }

        return result;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        common = (Common) getApplication();

        if (common.isCamera && resultCode == RESULT_OK) {
            showDialog(common.m_uri.getPath());
        } else if (common.isGallery && resultCode == RESULT_OK) {
            ContentResolver cr = getContentResolver();
            String[] columns = {
                    MediaStore.Images.Media.DATA
            };
            Cursor c = cr.query(data.getData(), columns, null, null, null);
            assert c != null;
            int column_index = c.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            c.moveToFirst();
            String imagePath = c.getString(column_index);
            showDialog(imagePath);
        }

    }

    private void showDialog(final String path) {
        if (null != common.bm) {
            common.bm.recycle();
        }
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.image_change_confirm, (ViewGroup) findViewById(R.id.alertdialog_layout));
        ImageView takeImageView = (ImageView) layout.findViewById(R.id.take_image);
        ImageView preImageView = (ImageView) layout.findViewById(R.id.pre_image);
        SetDialogImage setDialogImage = new SetDialogImage(this, takeImageView, preImageView, path, common.year, common.month, common.day);
        setDialogImage.forceLoad();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(common.year + "年" + common.month + "月" + common.day + "日の画像");
        builder.setView(layout);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                DailyTop dailyTop = dao.getItem(common.year, common.month, common.day);
                if (dailyTop == null) {
                    dao.insertPath(path, common.year, common.month, common.day);
                } else {
                    dao.updatePath(path, common.year, common.month, common.day);
                }
                //
                setViewPager(common.day);
            }
        });
        builder.setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.create().show();
    }

    public static Intent createIntent(Context context, CalenderList calenderList, int selectedDay, int currentDay) {
        Intent intent = new Intent(context.getApplicationContext(), DayDetailActivity.class);
        intent.putExtra("calenderListYear", calenderList.year);
        intent.putExtra("calenderListMonth", calenderList.month);
        intent.putExtra("calenderListDays", calenderList.days);
        intent.putExtra("calenderListStartDay", calenderList.startDay);
        intent.putExtra("selectedDay", selectedDay);
        intent.putExtra("currentDay", currentDay);
        return intent;
    }

}
