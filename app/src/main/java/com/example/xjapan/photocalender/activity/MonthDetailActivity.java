package com.example.xjapan.photocalender.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.xjapan.photocalender.model.CalenderList;
import com.example.xjapan.photocalender.model.DayList;
import com.example.xjapan.photocalender.adapter.DayListAdapter;
import com.example.xjapan.photocalender.util.JapaneseHolidayUtils;
import com.example.xjapan.photocalender.util.MyCalender;
import com.example.xjapan.photocalender.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MonthDetailActivity extends AppCompatActivity {

    private CalenderList calenderList;
    private int genzaiDay;
    private ArrayList<Integer> sundayList;
    private ArrayList<Integer> saturdayList;
    private List<Calendar> holidayList;
    private String currentDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month_detail);

        Calendar genzai = Calendar.getInstance();
        genzaiDay = 0;

        currentDay = getIntent().getStringExtra("dayListDay");
        calenderList = new CalenderList();
        calenderList.month = getIntent().getIntExtra("calenderListMonth", 0);
        calenderList.year = getIntent().getIntExtra("calenderListYear", 0);
        calenderList.startDay = getIntent().getIntExtra("calenderListStartDay", 0);
        calenderList.days = getIntent().getIntExtra("calenderListDays", 0);
        if (calenderList.year == genzai.get(Calendar.YEAR) && calenderList.month == genzai.get(Calendar.MONTH) + 1) {
            genzaiDay = genzai.get(Calendar.DATE);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(calenderList.year + "年" + calenderList.month + "月");
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        MyCalender myCalender = new MyCalender();
        sundayList = myCalender.getSundayList(calenderList);
        saturdayList = myCalender.getSaturDayList(calenderList);
        holidayList = JapaneseHolidayUtils.getHolidaysOf(calenderList.year, calenderList.month);
    }

    @Override
    public void onResume() {
        super.onResume();
        ListView dayListView = (ListView) findViewById(R.id.dayListView);
        DayListAdapter dayListAdapter = new DayListAdapter(MonthDetailActivity.this, R.layout.day_list_item, calenderList, genzaiDay, sundayList, saturdayList, holidayList);
        dayListView.setAdapter(dayListAdapter);
        dayListView.setSelection(Integer.parseInt(currentDay) - 1);
        dayListView.setOnItemClickListener(createOnItemClickListener());
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

    private AdapterView.OnItemClickListener createOnItemClickListener(){
        return new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                view.getContext().startActivity(DayDetailActivity.createIntent(view.getContext(), calenderList, i, genzaiDay));
            }
        };
    }

    public static Intent createIntent(Context context, DayList dayList, CalenderList postCalenderList ){
        Intent intent = new Intent(context.getApplicationContext(), MonthDetailActivity.class);
        intent.putExtra("dayListDay", dayList.day);
        intent.putExtra("calenderListYear", postCalenderList.year);
        intent.putExtra("calenderListMonth", postCalenderList.month);
        intent.putExtra("calenderListDays", postCalenderList.days);
        intent.putExtra("calenderListStartDay", postCalenderList.startDay);
        return intent;
    }

}
