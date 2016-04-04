package com.example.xjapan.photocalender.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;

import com.example.xjapan.photocalender.R;
import com.example.xjapan.photocalender.adapter.CalenderRecyclerAdapter;
import com.example.xjapan.photocalender.adapter.StickyAdapter;
import com.example.xjapan.photocalender.db.dao.DailyTopDAO;
import com.example.xjapan.photocalender.model.CalenderList;
import com.example.xjapan.photocalender.model.DailyTop;
import com.example.xjapan.photocalender.model.DayList;
import com.example.xjapan.photocalender.util.Common;
import com.example.xjapan.photocalender.util.JapaneseHolidayUtils;
import com.example.xjapan.photocalender.util.MyCalender;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersGridView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends FragmentActivity {

    private Common common;
    private FloatingActionButton pencilButton;
    private FloatingActionButton stampButton;
    private ArrayList<CalenderList> allList;
    private StickyGridHeadersGridView stickyGridHeadersGridView;
    private StickyAdapter stickyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        common = (Common) getApplication();
//        setStickyGridHeadersGridView();
        setRecyclerView();
        setButton();
    }

    private void setRecyclerView() {
        MyCalender myCalender = new MyCalender();
        allList = myCalender.getAllList();
        ArrayList<DayList> allDays = setDays(allList);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 7);
        gridLayoutManager.scrollToPosition(getCurrentPosition(allList, myCalender.getCurrentDate()));
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(new CalenderRecyclerAdapter(this.getApplicationContext(), allList, allDays));

    }

//    private void setStickyGridHeadersGridView() {
//        MyCalender myCalender = new MyCalender();
//        allList = myCalender.getAllList();
//        ArrayList<DayList> allDays = setDays(allList);
//        stickyGridHeadersGridView = (StickyGridHeadersGridView) this.findViewById(R.id.listViewCalendar);
//        stickyAdapter = new StickyAdapter(this.getApplicationContext(), allList, allDays);
//        stickyGridHeadersGridView.setAdapter(stickyAdapter);
//        stickyGridHeadersGridView.setNumColumns(7);
//        stickyGridHeadersGridView.setSelection(getCurrentPosition(allList, myCalender.getCurrentDate()));
//        stickyGridHeadersGridView.setOnItemClickListener(createOnItemClickListener(allDays));
//        common.stickyGridHeadersGridView = stickyGridHeadersGridView;
//        common.stickyAdapter = stickyAdapter;
//    }

    private ArrayList<DayList> setDays(ArrayList<CalenderList> allList) {
        MyCalender myCalender = new MyCalender();
        ArrayList<DayList> allDays = new ArrayList();
        for (int i = 0; i < allList.size(); i++) {
            CalenderList calenderList = allList.get(i);
            ArrayList<Integer> sundayList = myCalender.getSundayList(calenderList);
            ArrayList<Integer> saturdayList = myCalender.getSaturDayList(calenderList);
            List<Calendar> holidayList = JapaneseHolidayUtils.getHolidaysOf(calenderList.year, calenderList.month);
            for (int j = 1; j < calenderList.startDay; j++) {
                DayList dayList = new DayList();
                dayList.day = "";
                dayList.year = calenderList.year;
                dayList.month = calenderList.month;
                allDays.add(dayList);
            }
            for (int j = 1; j <= calenderList.days; j++) {
                DayList dayList = new DayList();
                dayList.day = j + "";
                dayList.year = calenderList.year;
                dayList.month = calenderList.month;
                if (sundayList.indexOf(j) != -1) {
                    dayList.isSunday = true;
                } else if (saturdayList.indexOf(j) != -1) {
                    dayList.isSaturday = true;
                } else {
                    for (int k = 0; k < holidayList.size(); k++) {
                        if (holidayList.get(k).get(Calendar.DATE) == j) {
                            dayList.isHoliday = true;
                            break;
                        }
                    }
                }
                allDays.add(dayList);
            }
            int diff = calenderList.startDay + (calenderList.days % 7) - 1;
            if (diff > 7) {
                for (int j = 0; j < 7 - diff % 7; j++) {
                    DayList dayList = new DayList();
                    dayList.day = "";
                    dayList.year = calenderList.year;
                    dayList.month = calenderList.month;
                    allDays.add(dayList);
                }
            } else if (diff > 0 && diff < 7) {
                for (int j = 0; j < 7 - diff; j++) {
                    DayList dayList = new DayList();
                    dayList.day = "";
                    dayList.year = calenderList.year;
                    dayList.month = calenderList.month;
                    allDays.add(dayList);
                }
            }
        }

        return allDays;
    }

    private int getCurrentPosition(ArrayList<CalenderList> allList, CalenderList currentDate) {
        int currentPosition = 0;
        for (int i = 0; i < allList.size(); i++) {
            CalenderList calenderList = allList.get(i);
            if (calenderList.year == currentDate.year && calenderList.month == currentDate.month) {
                Calendar genzai = Calendar.getInstance();
                currentPosition = currentPosition + currentDate.startDay - 1 + genzai.get(Calendar.DATE);
                break;
            } else {
                int diff = calenderList.startDay + (calenderList.days % 7) - 1;
                int spaceCount = 0;
                if (diff > 7) {
                    while (spaceCount < 7 - diff % 7) {
                        spaceCount++;
                    }
                } else if (diff > 0 && diff < 7) {
                    while (spaceCount < 7 - diff) {
                        spaceCount++;
                    }
                }
                currentPosition = currentPosition + calenderList.days + calenderList.startDay - 1 + spaceCount;
            }
        }
        return currentPosition;
    }

    private AdapterView.OnItemClickListener createOnItemClickListener(final ArrayList<DayList> allDays) {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                DayList dayList = allDays.get(i);
                if (common.isStamp) {
                    setClickStampCase(dayList);
                } else if (common.isPencil) {
                    setClickPencilCase(dayList);
                } else {
                    CalenderList postCalenderList = getCalenderListByDayId(i);
                    view.getContext().startActivity(MonthDetailActivity.createIntent(view.getContext(), dayList, postCalenderList));
                }
            }
        };
    }

    private void setClickStampCase(DayList dayList) {
        common.year = dayList.year;
        common.month = dayList.month;
        common.day = Integer.parseInt(dayList.day);
        LayoutInflater inflater = LayoutInflater.from(this);
        View layout = inflater.inflate(R.layout.select_stamp, (ViewGroup) this.findViewById(R.id.select_stamp_layout));
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(dayList.year + "年" + dayList.month + "月" + dayList.day + "日");
        builder.setView(layout);
        common.alertDialog = builder.show();
    }

    private void setClickPencilCase(DayList dayList) {
        common.year = dayList.year;
        common.month = dayList.month;
        common.day = Integer.parseInt(dayList.day);
        LayoutInflater inflater = LayoutInflater.from(this);
        View layout = inflater.inflate(R.layout.edit_title_memo, (ViewGroup) this.findViewById(R.id.edit_title_memo_layout));
        final EditText editText = (EditText) layout.findViewById(R.id.edit_title_memo);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(dayList.year + "年" + dayList.month + "月" + dayList.day + "日");
        builder.setView(layout);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                DailyTopDAO dao = DailyTopDAO.get();
                DailyTop dailyTop = dao.getItem(common.year, common.month, common.day);
                if (dailyTop == null) {
                    dao.insertTitleMemo(editText.getText().toString(), common.year, common.month, common.day);
                } else {
                    dao.updateTitleMemo(editText.getText().toString(), common.year, common.month, common.day);
                }
                stickyAdapter.notifyDataSetChanged();
                stickyGridHeadersGridView.invalidateViews();
            }
        });
        builder.setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        common.alertDialog = builder.show();
    }

    private CalenderList getCalenderListByDayId(int dayId) {
        CalenderList calenderList = new CalenderList();
        int count = 0;
        for (int i = 0; i < allList.size(); i++) {
            CalenderList object = allList.get(i);
            count = count + object.startDay + object.days - 1;
            if (count >= dayId) {
                calenderList = object;
                break;
            }
        }
        return calenderList;
    }

    private void setButton() {
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
