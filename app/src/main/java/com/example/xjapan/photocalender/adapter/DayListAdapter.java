package com.example.xjapan.photocalender.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.xjapan.photocalender.R;
import com.example.xjapan.photocalender.ViewHolder;
import com.example.xjapan.photocalender.db.dao.DailyMemoDAO;
import com.example.xjapan.photocalender.db.dao.DailyTopDAO;
import com.example.xjapan.photocalender.model.CalenderList;
import com.example.xjapan.photocalender.model.DailyMemo;
import com.example.xjapan.photocalender.model.DailyTop;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by xjapan on 16/01/07.
 */
public class DayListAdapter extends ArrayAdapter {

    private Context context;
    private int resourceId;
    private LayoutInflater inflater;
    private CalenderList calenderList;
    private int currentDay;
    private ArrayList<Integer> sundayList;
    private ArrayList<Integer> saturdayList;
    private List<Calendar> holidayList;
    private DailyTopDAO dailyTopDAO = DailyTopDAO.get();

    public DayListAdapter(Context context, int resource, CalenderList calenderList, int currentDay, ArrayList<Integer> sundayList, ArrayList<Integer> saturdayList, List<Calendar> holidayList) {
        super(context, resource);
        this.context = context;
        this.resourceId = resource;
        this.inflater = LayoutInflater.from(context);
        this.calenderList = calenderList;
        this.currentDay = currentDay;
        this.sundayList = sundayList;
        this.saturdayList = saturdayList;
        this.holidayList = holidayList;
    }

    @Override
    public int getCount() {
        return calenderList.days;
    }

    @Override
    public View getView(int i, final View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        View view;
        final int i_temp = i + 1;
        if (convertView == null) {
            view = this.inflater.inflate(resourceId, null);
        } else {
            view = this.inflater.inflate(resourceId, null);
        }

        RelativeLayout dayRelativeLayout = (RelativeLayout) view.findViewById(R.id.day_relativelayout);
        if (i_temp == currentDay) {
            dayRelativeLayout.setBackgroundColor(Color.rgb(255, 240, 245));
        }

        ImageView dayImage = (ImageView) view.findViewById(R.id.day_image);
        dayImage.setImageResource(R.drawable.noimage1);
        DailyTop dailyTop = dailyTopDAO.getItem(calenderList.year, calenderList.month, i_temp);
        if (dailyTop != null) {
            if (dailyTop.path != null) {
                File imageFile = new File(dailyTop.path);
                if (imageFile.exists()) {
                    Picasso.with(view.getContext()).load(imageFile).into(dayImage);
                }
            }
        }

        TextView day = (TextView) view.findViewById(R.id.day_list_day_text);
        day.setText(i_temp + "");
        if (sundayList.indexOf(i_temp) != -1) {
            day.setTextColor(Color.RED);
        } else if (saturdayList.indexOf(i_temp) != -1) {
            day.setTextColor(Color.BLUE);
        } else {
            for (int j = 0; j < holidayList.size(); j++) {
                if (holidayList.get(j).get(Calendar.DATE) == i_temp) {
                    day.setTextColor(Color.RED);
                    break;
                }
            }
        }

        DailyMemoDAO dailyMemoDAO = DailyMemoDAO.get();
        DailyMemo dailyMemo = dailyMemoDAO.getItem(calenderList.year, calenderList.month, i_temp);
        TextView memoText = (TextView) view.findViewById(R.id.day_memo);
        if (dailyMemo != null) {
            memoText.setText(dailyMemo.memo);
        } else {
            memoText.setText("");
        }
        return view;
    }
}
