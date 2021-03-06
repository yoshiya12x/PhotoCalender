package com.example.xjapan.photocalender.util;

import com.example.xjapan.photocalender.model.CalenderList;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by xjapan on 16/01/02.
 */
public class MyCalender {

    private Calendar calendar;
    private int currentYear;
    private int currentMonth;

    public MyCalender() {
        calendar = Calendar.getInstance();
        currentYear = calendar.get(Calendar.YEAR);
        currentMonth = calendar.get(Calendar.MONTH) + 1;
    }

    public CalenderList getCurrentDate() {
        CalenderList calenderList = new CalenderList();
        calenderList.year = currentYear;
        calenderList.month = currentMonth;
        calenderList.days = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        calendar.set(calenderList.year, calenderList.month - 1, 1);
        calenderList.startDay = calendar.get(Calendar.DAY_OF_WEEK);
        return calenderList;
    }

    public CalenderList getTargetDate(int year, int month) {
        CalenderList calenderList = new CalenderList();
        calendar.set(year, month - 1, 1);
        calenderList.year = year;
        calenderList.month = month;
        calenderList.days = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        calenderList.startDay = calendar.get(Calendar.DAY_OF_WEEK);
        return calenderList;
    }

    //去年、今年、来年のcalenderList作成
    public ArrayList<CalenderList> getAllList() {
        ArrayList<CalenderList> allList = new ArrayList();
        for (int i = -1; i < 2; i++) {
            for (int j = 1; j < 13; j++) {
                CalenderList calenderList = new CalenderList();
                calendar.set(currentYear + i, j - 1, 1);
                calenderList.year = currentYear + i;
                calenderList.month = j;
                calenderList.days = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                calenderList.startDay = calendar.get(Calendar.DAY_OF_WEEK);
                allList.add(calenderList);
            }
        }
        return allList;
    }

    public ArrayList<Integer> getSundayList(CalenderList calenderList) {
        ArrayList<Integer> sundayList = new ArrayList();
        if (calenderList.startDay == 1) {
            for (int i = 1; i <= calenderList.days; i = i + 7) {
                sundayList.add(i);
            }
        } else {
            for (int i = 7 - calenderList.startDay + 2; i <= calenderList.days; i = i + 7) {
                sundayList.add(i);
            }
        }
        return sundayList;
    }

    public ArrayList<Integer> getSaturDayList(CalenderList calenderList) {
        ArrayList<Integer> saturdayList = new ArrayList();
        if (calenderList.startDay == 7) {
            for (int i = 1; i <= calenderList.days; i = i + 7) {
                saturdayList.add(i);
            }
        } else {
            for (int i = 7 - calenderList.startDay + 1; i <= calenderList.days; i = i + 7) {
                saturdayList.add(i);
            }
        }
        return saturdayList;
    }
}
