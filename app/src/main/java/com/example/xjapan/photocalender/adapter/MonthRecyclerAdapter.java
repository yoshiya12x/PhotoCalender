package com.example.xjapan.photocalender.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
 * Created by xjapan on 16/04/06.
 */
public class MonthRecyclerAdapter extends RecyclerView.Adapter {

    private Context context;
    private LayoutInflater inflater;
    private CalenderList calenderList;
    private int currentDay;
    private ArrayList<Integer> sundayList;
    private ArrayList<Integer> saturdayList;
    private List<Calendar> holidayList;
    private DailyTopDAO dailyTopDAO = DailyTopDAO.get();

    public MonthRecyclerAdapter(Context context, CalenderList calenderList, int currentDay, ArrayList<Integer> sundayList, ArrayList<Integer> saturdayList, List<Calendar> holidayList) {
        super();
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.calenderList = calenderList;
        this.currentDay = currentDay;
        this.sundayList = sundayList;
        this.saturdayList = saturdayList;
        this.holidayList = holidayList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(inflater.inflate(R.layout.day_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemViewHolder viewHolder = (ItemViewHolder) holder;
        int position_tmp = position + 1;

        //初期化
        viewHolder.dayRelativelayout.setBackgroundColor(Color.WHITE);
        viewHolder.dayImageView.setImageResource(R.drawable.noimage1);
        viewHolder.dayStampImageView.setImageBitmap(null);
        viewHolder.dayTextView.setText(position_tmp + "");
        viewHolder.dayTextView.setTextColor(Color.WHITE);
        viewHolder.dayTitleMemoTextView.setText("");
        viewHolder.dayMemoTextView.setText("");

        if (position_tmp == currentDay) {
            viewHolder.dayRelativelayout.setBackgroundColor(Color.rgb(255, 240, 245));
        }
        DailyTop dailyTop = dailyTopDAO.getItem(calenderList.year, calenderList.month, position_tmp);
        if (dailyTop != null) {
            if (dailyTop.path != null) {
                File imageFile = new File(dailyTop.path);
                if (imageFile.exists()) {
                    Picasso.with(context).load(imageFile).into(viewHolder.dayImageView);
                }
            } else if (dailyTop.stamp != 0) {
                viewHolder.dayStampImageView.setImageResource(dailyTop.stamp);
            }
            if (dailyTop.titleMemo != null) {
                viewHolder.dayTitleMemoTextView.setText(dailyTop.titleMemo);
            }
        }
        if (sundayList.indexOf(position_tmp) != -1) {
            viewHolder.dayTextView.setTextColor(Color.RED);
        } else if (saturdayList.indexOf(position_tmp) != -1) {
            viewHolder.dayTextView.setTextColor(Color.BLUE);
        } else {
            for (int j = 0; j < holidayList.size(); j++) {
                if (holidayList.get(j).get(Calendar.DATE) == position_tmp) {
                    viewHolder.dayTextView.setTextColor(Color.RED);
                    break;
                }
            }
        }
        DailyMemoDAO dailyMemoDAO = DailyMemoDAO.get();
        DailyMemo dailyMemo = dailyMemoDAO.getItem(calenderList.year, calenderList.month, position_tmp);
        if (dailyMemo != null) {
            viewHolder.dayMemoTextView.setText(dailyMemo.memo);
        }
    }

    @Override
    public int getItemCount() {
        return calenderList.days;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout dayRelativelayout;
        private ImageView dayImageView;
        private ImageView dayStampImageView;
        private TextView dayTextView;
        private TextView dayTitleMemoTextView;
        private TextView dayMemoTextView;

        public ItemViewHolder(View view) {
            super(view);
            this.dayRelativelayout = (RelativeLayout) view.findViewById(R.id.day_relativelayout);
            this.dayImageView = (ImageView) view.findViewById(R.id.day_image);
            this.dayStampImageView = (ImageView) view.findViewById(R.id.stamp_day_image);
            this.dayTextView = (TextView) view.findViewById(R.id.day_list_day_text);
            this.dayTitleMemoTextView = (TextView) view.findViewById(R.id.day_title_memo);
            this.dayMemoTextView = (TextView) view.findViewById(R.id.day_memo);
        }
    }
}