package com.example.xjapan.photocalender.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.xjapan.photocalender.R;
import com.example.xjapan.photocalender.ViewHolder;
import com.example.xjapan.photocalender.asyncTask.DailyImagePathSync;
import com.example.xjapan.photocalender.model.CalenderList;
import com.example.xjapan.photocalender.model.DayList;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersBaseAdapter;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by xjapan on 15/12/21.
 */
public class StickyAdapter extends BaseAdapter implements StickyGridHeadersBaseAdapter {

    private LayoutInflater inflater;
    private ArrayList<CalenderList> allList;
    private ArrayList<DayList> allDays;

    public StickyAdapter(Context context, ArrayList<CalenderList> allList, ArrayList<DayList> allDays) {
        super();
        this.inflater = LayoutInflater.from(context);
        this.allList = allList;
        this.allDays = allDays;
    }

    @Override
    public int getCount() {
        return allList.size();
    }

    @Override
    public Object getItem(int i) {
        return allList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolder holder;
        final DayList dayList = allDays.get(i);

        if (view == null) {
//            view = inflater.inflate(R.layout.grid_image, viewGroup, false);
            view = inflater.inflate(R.layout.grid_image, null);
            holder = new ViewHolder();
            holder.gridImageView = (ImageView) view.findViewById(R.id.item_imageview);
            holder.gridTextView = (TextView) view.findViewById(R.id.day_text);
            holder.stampImageView = (ImageView) view.findViewById(R.id.stamp_imageview);
            holder.titleMemoTextView = (TextView) view.findViewById(R.id.title_memo);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        if (dayList.day.isEmpty()) {
            holder.gridImageView.setImageBitmap(null);
        } else {
            holder.gridImageView.setTag(i + "");
            DailyImagePathSync dailyImagePathSync = new DailyImagePathSync(holder, view.getContext());
            dailyImagePathSync.execute(dayList.year, dayList.month, Integer.parseInt(dayList.day));
//            DailyImagePathSync dailyImagePathSync = new DailyImagePathSync(context, dayList.year, dayList.month, dayList.day, holder, common);
//            dailyImagePathSync.forceLoad();
        }

        holder.gridTextView.setText(dayList.day);

        Calendar genzai = Calendar.getInstance();
        if (dayList.isSunday || dayList.isHoliday) {
            holder.gridTextView.setTextColor(Color.RED);
        } else if (dayList.isSaturday) {
            holder.gridTextView.setTextColor(Color.BLUE);
        } else if (!dayList.day.isEmpty() && dayList.year == genzai.get(Calendar.YEAR) && dayList.month == genzai.get(Calendar.MONTH) + 1 && Integer.parseInt(dayList.day) == genzai.get(Calendar.DATE)) {
            holder.gridTextView.setTextColor(view.getContext().getResources().getColor(R.color.colorDarkGray));
        } else {
            holder.gridTextView.setTextColor(Color.WHITE);
        }
        return view;
    }

    //コンテンツの数
    @Override
    public int getCountForHeader(int i) {
        return allList.get(i).days + allList.get(i).startDay - 1;
    }

    //ヘッダの数
    @Override
    public int getNumHeaders() {
        return allList.size();
    }

    @Override
    public View getHeaderView(int i, View view, ViewGroup viewGroup) {
        RelativeLayout holder;
        CalenderList calenderList = allList.get(i);

        if (view == null) {
            holder = (RelativeLayout) inflater.inflate(R.layout.header_text, viewGroup, false);
        } else {
            holder = (RelativeLayout) view;
        }

        TextView monthText = (TextView) holder.getChildAt(0);
        monthText.setText(calenderList.year + "年" + calenderList.month + "月");

        LinearLayout dayNameLinearLayout = (LinearLayout) holder.getChildAt(1);
        TextView sundayText = (TextView) dayNameLinearLayout.getChildAt(0);
        TextView mondayText = (TextView) dayNameLinearLayout.getChildAt(1);
        TextView tuesdayText = (TextView) dayNameLinearLayout.getChildAt(2);
        TextView wednesdayText = (TextView) dayNameLinearLayout.getChildAt(3);
        TextView thursdayText = (TextView) dayNameLinearLayout.getChildAt(4);
        TextView fridayText = (TextView) dayNameLinearLayout.getChildAt(5);
        TextView saturdayText = (TextView) dayNameLinearLayout.getChildAt(6);
        sundayText.setText("日");
        mondayText.setText("月");
        tuesdayText.setText("火");
        wednesdayText.setText("水");
        thursdayText.setText("木");
        fridayText.setText("金");
        saturdayText.setText("土");

        return holder;
    }
}