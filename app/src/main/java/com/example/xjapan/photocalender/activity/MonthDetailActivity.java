package com.example.xjapan.photocalender.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.xjapan.photocalender.R;
import com.example.xjapan.photocalender.adapter.MonthRecyclerAdapter;
import com.example.xjapan.photocalender.model.CalenderList;
import com.example.xjapan.photocalender.model.DayList;
import com.example.xjapan.photocalender.util.JapaneseHolidayUtils;
import com.example.xjapan.photocalender.util.MyCalender;

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
    private static RecyclerView.Adapter recyclerViewAdapter;

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

        setMonthRecyclerView();
    }

    public void setMonthRecyclerView() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.monthRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.scrollToPosition(Integer.parseInt(currentDay) - 1);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerViewAdapter = new MonthRecyclerAdapter(getApplicationContext(), calenderList, genzaiDay, sundayList, saturdayList, holidayList);
//        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                startActivity(DayDetailActivity.createIntent(view.getContext(), calenderList, position + 1, genzaiDay));
//            }
//        }));
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
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

    public static Intent createIntent(Context context, DayList dayList, CalenderList postCalenderList) {
        Intent intent = new Intent(context.getApplicationContext(), MonthDetailActivity.class);
        intent.putExtra("dayListDay", dayList.day);
        intent.putExtra("calenderListYear", postCalenderList.year);
        intent.putExtra("calenderListMonth", postCalenderList.month);
        intent.putExtra("calenderListDays", postCalenderList.days);
        intent.putExtra("calenderListStartDay", postCalenderList.startDay);
        return intent;
    }

    public static void reloadView(int day) {
        recyclerViewAdapter.notifyItemChanged(day - 1);
    }

}
