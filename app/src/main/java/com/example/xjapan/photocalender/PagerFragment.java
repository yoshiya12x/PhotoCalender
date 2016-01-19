package com.example.xjapan.photocalender;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tonicartos.widget.stickygridheaders.StickyGridHeadersGridView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by xjapan on 15/11/24.
 */
public class PagerFragment extends Fragment {

    private MyCalender myCalender;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myCalender = new MyCalender();
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_month, null);
        ArrayList<CalenderList> allList = myCalender.getAllList();
        CalenderList currentDate = myCalender.getCurrentDate();
        ArrayList<DayList> allDays = setDays(allList);
        StickyGridHeadersGridView stickyGridHeadersGridView = (StickyGridHeadersGridView) view.findViewById(R.id.listViewCalendar);
        stickyGridHeadersGridView.setAdapter(new StickyAdapter(getActivity(), allList, allDays, (Common) getActivity().getApplication()));
        stickyGridHeadersGridView.setNumColumns(7);
        //自動スクロール
        stickyGridHeadersGridView.setSelection(getCurrentPosition(allList, currentDate));
        return view;
    }

    public ArrayList<DayList> setDays(ArrayList<CalenderList> allList) {
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

    public int getCurrentPosition(ArrayList<CalenderList> allList, CalenderList currentDate) {
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
