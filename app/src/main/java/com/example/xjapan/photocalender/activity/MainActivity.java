package com.example.xjapan.photocalender.activity;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

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

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends FragmentActivity {

    private Common common;
    private static ArrayList<CalenderList> allList;
    private ArrayList<DayList> allDays;
    private static RecyclerView.Adapter recyclerViewAdapter;
    public ArrayList<Integer> headerCountList = new ArrayList<>();
    private DailyTopDAO dao = DailyTopDAO.get();
    private static MyCalender myCalender = new MyCalender();
    private static final int HEADER_COUNT = 7;
    private static final int ITEM_COUNT = 1;
    private int prePosition = -1;

    @Bind(R.id.drawerLinearLayout)
    LinearLayout drawerLinearLayout;
    @Bind(R.id.drawerButton)
    ImageButton drawerButton;
    @Bind(R.id.stampImageButton)
    ImageButton stampImageButton;
    @Bind(R.id.pencilImageButton)
    ImageButton pencilImageButton;
    @Bind(R.id.drawerStampLayout1)
    LinearLayout drawerStampLayout1;
    @Bind(R.id.drawerStampLayout2)
    LinearLayout drawerStampLayout2;
    @Bind(R.id.drawerTextInputLayout)
    TextInputLayout drawerTextInputLayout;
    @Bind(R.id.drawerTitleMemoSaveButton)
    Button drawerTitleMemoSaveButton;
    @Bind(R.id.drawerEditTitleMemo)
    EditText drawerEditTitleMemo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        common = (Common) getApplication();
        setRecyclerView();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @OnClick(R.id.drawerButton)
    void clickDrawerButton() {
        if (drawerLinearLayout.getVisibility() == View.GONE) {
            common.isStamp = true;
            drawerLinearLayout.setVisibility(View.VISIBLE);
            drawerStampLayout1.setVisibility(View.VISIBLE);
            drawerStampLayout2.setVisibility(View.VISIBLE);
            changeDrawerButtonImage(0);
            stampImageButton.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        } else {
            common.isStamp = false;
            common.isPencil = false;
            recyclerViewAdapter.notifyItemChanged(prePosition);
            drawerLinearLayout.setVisibility(View.GONE);
            drawerStampLayout1.setVisibility(View.GONE);
            drawerStampLayout2.setVisibility(View.GONE);
            drawerTextInputLayout.setVisibility(View.GONE);
            drawerTitleMemoSaveButton.setVisibility(View.GONE);
            changeDrawerButtonImage(1);
            stampImageButton.setBackgroundColor(getResources().getColor(R.color.colorSubImage));
            pencilImageButton.setBackgroundColor(getResources().getColor(R.color.colorSubImage));
            recyclerViewAdapter.notifyItemChanged(prePosition);
            prePosition = -1;
        }
    }

    @OnClick(R.id.stampImageButton)
    void clickStampImageButton() {
        common.isStamp = true;
        common.isPencil = false;
        drawerLinearLayout.setVisibility(View.VISIBLE);
        drawerTextInputLayout.setVisibility(View.GONE);
        drawerTitleMemoSaveButton.setVisibility(View.GONE);
        drawerStampLayout1.setVisibility(View.VISIBLE);
        drawerStampLayout2.setVisibility(View.VISIBLE);
        changeDrawerButtonImage(0);
        stampImageButton.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        pencilImageButton.setBackgroundColor(getResources().getColor(R.color.colorSubImage));
    }

    @OnClick(R.id.pencilImageButton)
    void clickPencilImageButton() {
        common.isStamp = false;
        common.isPencil = true;
        drawerLinearLayout.setVisibility(View.VISIBLE);
        drawerStampLayout1.setVisibility(View.GONE);
        drawerStampLayout2.setVisibility(View.GONE);
        drawerTextInputLayout.setVisibility(View.VISIBLE);
        drawerTitleMemoSaveButton.setVisibility(View.VISIBLE);
        changeDrawerButtonImage(0);
        stampImageButton.setBackgroundColor(getResources().getColor(R.color.colorSubImage));
        pencilImageButton.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        setDrawerEditTitleMemo();
    }

    @OnClick(R.id.drawerTitleMemoSaveButton)
    void clickDrawerTitleMemoSaveButton() {
        if (prePosition == -1) {
            makeSnackBar("日付を選択して保存ボタンを押してください");
        } else {
            DailyTop dailyTop = dao.getItem(common.year, common.month, common.day);
            if (dailyTop == null) {
                dao.insertTitleMemo(drawerEditTitleMemo.getText().toString(), common.year, common.month, common.day);
            } else {
                dao.updateTitleMemo(drawerEditTitleMemo.getText().toString(), common.year, common.month, common.day);
            }
            common.focusPosition = prePosition;
            recyclerViewAdapter.notifyItemChanged(prePosition);
        }
    }

    @OnClick(R.id.stampDelButton)
    void clickStampDelButton() {
        if (prePosition == -1) {
            makeSnackBar("日付を選択してスタンプを削除してください");
        } else {
            DailyTop dailyTop = dao.getItem(common.year, common.month, common.day);
            if (dailyTop != null) {
                if (dailyTop.stamp != -1) {
                    dao.updateStamp(-1, common.year, common.month, common.day);
                    common.focusPosition = prePosition;
                    recyclerViewAdapter.notifyItemChanged(prePosition);
                }
            }
        }
    }

    private void makeSnackBar(String snackBarText) {
        final CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.top_root_layout);
        Snackbar.make(coordinatorLayout, snackBarText, Snackbar.LENGTH_LONG).show();
    }

    private void changeDrawerButtonImage(int flag) {
        if (flag == 0) {
            drawerButton.setImageResource(R.drawable.dropdown);
        } else if (flag == 1) {
            drawerButton.setImageResource(R.drawable.dropup);
        }
    }

    private void setDrawerEditTitleMemo() {
        drawerTitleMemoSaveButton.setEnabled(false);
        drawerTitleMemoSaveButton.setBackground(getResources().getDrawable(R.drawable.memo_send_button));
        drawerTitleMemoSaveButton.setTextColor(getResources().getColor(R.color.colorDarkGray));
        drawerEditTitleMemo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    drawerTitleMemoSaveButton.setEnabled(true);
                    drawerTitleMemoSaveButton.setBackground(getResources().getDrawable(R.drawable.memo_send_button_true));
                    drawerTitleMemoSaveButton.setTextColor(getResources().getColor(R.color.colorAccent));
                } else {
                    InputMethodManager imm = (InputMethodManager) getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        });
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
                common.year = dayList.year;
                common.month = dayList.month;
                common.day = Integer.parseInt(dayList.day);
                if (common.isStamp || common.isPencil) {
                    common.focusPosition = position;
                    if (prePosition != -1) {
                        recyclerViewAdapter.notifyItemChanged(prePosition);
                    }
                    recyclerViewAdapter.notifyItemChanged(position);
                    prePosition = position;
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
        if (prePosition == -1) {
            makeSnackBar("日付を選択してスタンプを押してください");
        } else {
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
            common.focusPosition = prePosition;
            recyclerViewAdapter.notifyItemChanged(prePosition);
        }
    }

    public static void reloadView(int year, int month, int day) {
        int position = getCurrentPosition(allList, myCalender.getTargetDate(year, month), 1, day);
        recyclerViewAdapter.notifyItemChanged(position);
    }
}
