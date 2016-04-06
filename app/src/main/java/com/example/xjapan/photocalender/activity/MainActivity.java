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
import android.widget.EditText;

import com.example.xjapan.photocalender.R;
import com.example.xjapan.photocalender.adapter.CalenderRecyclerAdapter;
import com.example.xjapan.photocalender.db.dao.DailyTopDAO;
import com.example.xjapan.photocalender.listener.RecyclerItemClickListener;
import com.example.xjapan.photocalender.model.CalenderList;
import com.example.xjapan.photocalender.model.DailyTop;
import com.example.xjapan.photocalender.model.DayList;
import com.example.xjapan.photocalender.util.Common;
import com.example.xjapan.photocalender.util.JapaneseHolidayUtils;
import com.example.xjapan.photocalender.util.MyCalender;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends FragmentActivity {

    private Common common;
    private FloatingActionButton pencilButton;
    private FloatingActionButton stampButton;
    private static ArrayList<CalenderList> allList;
    private ArrayList<DayList> allDays;
    private static RecyclerView.Adapter recyclerViewAdapter;
    public ArrayList<Integer> headerCountList = new ArrayList<>();
    private int clickPosition;
    private DailyTopDAO dao = DailyTopDAO.get();
    private static MyCalender myCalender = new MyCalender();
    private static final int HEADER_COUNT = 7;
    private static final int ITEM_COUNT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        common = (Common) getApplication();
        setRecyclerView();
        setButton();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void setRecyclerView() {
        allList = myCalender.getAllList();
        allDays = setDays(allList);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 7);
        gridLayoutManager.scrollToPosition(getCurrentPosition(allList, myCalender.getCurrentDate(), 0, 0));
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (headerCountList.indexOf(position) != -1) {
                    return HEADER_COUNT;
                }
                return ITEM_COUNT;
            }
        });
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerViewAdapter = new CalenderRecyclerAdapter(this.getApplicationContext(), allDays, headerCountList, dao);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                DayList dayList = allDays.get(position);
                if (common.isStamp) {
                    setClickStampCase(dayList);
                    clickPosition = position;
                } else if (common.isPencil) {
                    setClickPencilCase(dayList, position);
                } else {
                    if (!dayList.day.isEmpty()) {
                        CalenderList postCalenderList = getCalenderListByDayId(position);
                        startActivity(MonthDetailActivity.createIntent(view.getContext(), dayList, postCalenderList));
                    }
                }
            }
        }));
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    private ArrayList<DayList> setDays(ArrayList<CalenderList> allList) {
        MyCalender myCalender = new MyCalender();
        int headerCount = -1;
        ArrayList<DayList> allDays = new ArrayList<>();
        for (int i = 0; i < allList.size(); i++) {
            CalenderList calenderList = allList.get(i);
            ArrayList<Integer> sundayList = myCalender.getSundayList(calenderList);
            ArrayList<Integer> saturdayList = myCalender.getSaturDayList(calenderList);
            List<Calendar> holidayList = JapaneseHolidayUtils.getHolidaysOf(calenderList.year, calenderList.month);

            DayList headerTopDayList = new DayList();
            headerTopDayList.day = "";
            headerTopDayList.year = calenderList.year;
            headerTopDayList.month = calenderList.month;
            allDays.add(headerTopDayList);
            headerCount++;
            headerCountList.add(headerCount);

            for (int j = 1; j < calenderList.startDay; j++) {
                DayList dayList = new DayList();
                dayList.day = "";
                dayList.year = calenderList.year;
                dayList.month = calenderList.month;
                allDays.add(dayList);
                headerCount++;
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
                headerCount++;
            }

            int diff = calenderList.startDay + (calenderList.days % 7) - 1;
            if (diff > 7) {
                for (int j = 0; j < 7 - diff % 7; j++) {
                    DayList dayList = new DayList();
                    dayList.day = "";
                    dayList.year = calenderList.year;
                    dayList.month = calenderList.month;
                    allDays.add(dayList);
                    headerCount++;
                }
            } else if (diff > 0 && diff < 7) {
                for (int j = 0; j < 7 - diff; j++) {
                    DayList dayList = new DayList();
                    dayList.day = "";
                    dayList.year = calenderList.year;
                    dayList.month = calenderList.month;
                    allDays.add(dayList);
                    headerCount++;
                }
            }
        }

        return allDays;
    }

    private static int getCurrentPosition(ArrayList<CalenderList> allList, CalenderList currentDate, int flag, int day) {
        int currentPosition = 0;
        for (int i = 0; i < allList.size(); i++) {
            CalenderList calenderList = allList.get(i);
            if (calenderList.year == currentDate.year && calenderList.month == currentDate.month) {
                if (flag == 1) {
                    currentPosition = currentPosition + currentDate.startDay - 1 + day;
                }
                break;
            } else {
                int spaceCount = getSpaceCount(calenderList);
                currentPosition = currentPosition + 1 + calenderList.days + calenderList.startDay - 1 + spaceCount;
            }
        }
        return currentPosition;
    }

    private static int getSpaceCount(CalenderList calenderList) {
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
        return spaceCount;
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

    private void setClickPencilCase(DayList dayList, final int position) {
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
                recyclerViewAdapter.notifyItemChanged(position);
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
            int spaceCount = getSpaceCount(object);
            count = count + 1 + object.startDay + object.days - 1 + spaceCount;
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
                updateStamp = R.drawable.airplane;
                break;
            case R.id.stamp_image_2:
                updateStamp = R.drawable.music;
                break;
            case R.id.stamp_image_3:
                updateStamp = R.drawable.heart;
                break;
            case R.id.stamp_image_4:
                updateStamp = R.drawable.food;
                break;
            case R.id.stamp_image_5:
                updateStamp = R.drawable.shopping;
                break;
            case R.id.stamp_image_6:
                updateStamp = R.drawable.cake;
                break;
            case R.id.stamp_image_7:
                updateStamp = R.drawable.drink;
                break;
            case R.id.stamp_image_8:
                updateStamp = R.drawable.pen;
                break;
            case R.id.stamp_image_9:
                updateStamp = R.drawable.flag;
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
        recyclerViewAdapter.notifyItemChanged(clickPosition);
        common.alertDialog.dismiss();
    }

    public static void reloadView(int year, int month, int day) {
        int position = getCurrentPosition(allList, myCalender.getTargetDate(year, month), 1, day);
        recyclerViewAdapter.notifyItemChanged(position);
    }
}
