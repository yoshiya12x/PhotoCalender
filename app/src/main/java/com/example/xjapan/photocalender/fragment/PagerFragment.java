package com.example.xjapan.photocalender.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;

import com.example.xjapan.photocalender.R;
import com.example.xjapan.photocalender.activity.MonthDetailActivity;
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

/**
 * Created by xjapan on 15/11/24.
 */
public class PagerFragment extends Fragment {

    private Common common;
    private ArrayList<CalenderList> allList;
    private StickyGridHeadersGridView stickyGridHeadersGridView;
    private StickyAdapter stickyAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MyCalender myCalender = new MyCalender();
        common = (Common) getActivity().getApplication();
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_month, null);
        allList = myCalender.getAllList();
        ArrayList<DayList> allDays = setDays(allList);
        stickyGridHeadersGridView = (StickyGridHeadersGridView) view.findViewById(R.id.listViewCalendar);
        stickyAdapter = new StickyAdapter(this.getActivity(), allList, allDays);
        stickyGridHeadersGridView.setAdapter(stickyAdapter);
        stickyGridHeadersGridView.setNumColumns(7);
        stickyGridHeadersGridView.setSelection(getCurrentPosition(allList, myCalender.getCurrentDate()));
        stickyGridHeadersGridView.setOnItemClickListener(createOnItemClickListener(allDays));
        common.stickyGridHeadersGridView = stickyGridHeadersGridView;
        common.stickyAdapter = stickyAdapter;
        return view;
    }

    private AdapterView.OnItemClickListener createOnItemClickListener(final ArrayList<DayList> allDays) {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                DayList dayList = allDays.get(i);
                if (common.isStamp) {
                    setClickStampCase(dayList);
                } else if (common.isPencil) {
                    setClickPencilCase(dayList, i);
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
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View layout = inflater.inflate(R.layout.select_stamp, (ViewGroup) this.getActivity().findViewById(R.id.select_stamp_layout));
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        builder.setTitle(dayList.year + "年" + dayList.month + "月" + dayList.day + "日");
        builder.setView(layout);
        common.alertDialog = builder.show();
    }

    private void setClickPencilCase(DayList dayList, final int position) {
        common.year = dayList.year;
        common.month = dayList.month;
        common.day = Integer.parseInt(dayList.day);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View layout = inflater.inflate(R.layout.edit_title_memo, (ViewGroup) this.getActivity().findViewById(R.id.edit_title_memo_layout));
        final EditText editText = (EditText) layout.findViewById(R.id.edit_title_memo);
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
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
        }
        return allDays;
    }

    private int getCurrentPosition(ArrayList<CalenderList> allList, CalenderList currentDate) {
        int currentPosition = 0;
        for (int i = 0; i < allList.size(); i++) {
            CalenderList calenderList = allList.get(i);
            if (calenderList.year == currentDate.year && calenderList.month == currentDate.month) {
                Calendar genzai = Calendar.getInstance();
                currentPosition = currentPosition + (i + 1) * 7 + currentDate.startDay - 1 + genzai.get(Calendar.DATE);
                break;
            } else {
                currentPosition = currentPosition + calenderList.days + calenderList.startDay - 1;
            }
        }
        return currentPosition;
    }
}
