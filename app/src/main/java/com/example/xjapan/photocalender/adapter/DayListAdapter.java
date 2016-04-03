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
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        int i_temp = i + 1;
        if (view == null) {
            view = inflater.inflate(R.layout.day_list_item, null);
            holder = new ViewHolder();
            holder.dayRelativelayout = (RelativeLayout) view.findViewById(R.id.day_relativelayout);
            holder.dayImageView = (ImageView) view.findViewById(R.id.day_image);
            holder.dayStampImageView = (ImageView) view.findViewById(R.id.stamp_day_image);
            holder.dayTextView = (TextView) view.findViewById(R.id.day_list_day_text);
            holder.dayTitleMemoTextView = (TextView) view.findViewById(R.id.day_title_memo);
            holder.dayMemoTextView = (TextView) view.findViewById(R.id.day_memo);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        //初期化
        holder.dayRelativelayout.setBackgroundColor(Color.WHITE);
        holder.dayImageView.setImageResource(R.drawable.noimage1);
        holder.dayStampImageView.setImageBitmap(null);
        holder.dayTextView.setText(i_temp + "");
        holder.dayTextView.setTextColor(Color.WHITE);
        holder.dayTitleMemoTextView.setText("");
        holder.dayMemoTextView.setText("");

        if (i_temp == currentDay) {
            holder.dayRelativelayout.setBackgroundColor(Color.rgb(255, 240, 245));
        }
        DailyTop dailyTop = dailyTopDAO.getItem(calenderList.year, calenderList.month, i_temp);
        if (dailyTop != null) {
            if (dailyTop.path != null) {
                File imageFile = new File(dailyTop.path);
                if (imageFile.exists()) {
                    Picasso.with(view.getContext()).load(imageFile).into(holder.dayImageView);
                }
            } else if (dailyTop.stamp != 0) {
                holder.dayStampImageView.setImageResource(dailyTop.stamp);
            }
            if (dailyTop.titleMemo != null) {
                holder.dayTitleMemoTextView.setText(dailyTop.titleMemo);
            }
        }
        if (sundayList.indexOf(i_temp) != -1) {
            holder.dayTextView.setTextColor(Color.RED);
        } else if (saturdayList.indexOf(i_temp) != -1) {
            holder.dayTextView.setTextColor(Color.BLUE);
        } else {
            for (int j = 0; j < holidayList.size(); j++) {
                if (holidayList.get(j).get(Calendar.DATE) == i_temp) {
                    holder.dayTextView.setTextColor(Color.RED);
                    break;
                }
            }
        }
        DailyMemoDAO dailyMemoDAO = DailyMemoDAO.get();
        DailyMemo dailyMemo = dailyMemoDAO.getItem(calenderList.year, calenderList.month, i_temp);
        if (dailyMemo != null) {
            holder.dayMemoTextView.setText(dailyMemo.memo);
        }

        return view;
    }

    private class ViewHolder {
        private RelativeLayout dayRelativelayout;
        private ImageView dayImageView;
        private ImageView dayStampImageView;
        private TextView dayTextView;
        private TextView dayTitleMemoTextView;
        private TextView dayMemoTextView;
    }
}
