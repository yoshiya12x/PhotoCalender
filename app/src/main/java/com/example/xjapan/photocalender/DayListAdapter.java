package com.example.xjapan.photocalender;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
        dayRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                convertView.getContext().startActivity(DayDetailActivity.createIntent(convertView.getContext(), calenderList, i_temp, currentDay));
            }
        });

        ImageView dayImage = (ImageView) view.findViewById(R.id.day_image);
        dayImage.setImageResource(R.drawable.noimage1);
        DailyTopDB dailyTopDB = new DailyTopDB(context);
        ArrayList<String> allItem = dailyTopDB.selectAll(calenderList.year, calenderList.month, i_temp);

        if (allItem.size() != 0) {
            if (allItem.get(0) != null) {
                File imageFile = new File(allItem.get(0));
                if (imageFile.exists()) {
                    //AsyncTaskを使う場合
                    //SetMonthDetailImage setMonthDetailImage = new SetMonthDetailImage(context, dayImage, imageFile);
                    //setMonthDetailImage.forceLoad();
                    //Picassoを使う場合
                    Picasso.with(context).load(imageFile).into(dayImage);
                    //普通にセットする場合
                    //dayImage.setImageURI(Uri.fromFile(imageFile));
                }
            } else {

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

        DailyMemoDB dailyMemoDB = new DailyMemoDB(context);
        String memo = dailyMemoDB.selectMemo(calenderList.year, calenderList.month, i_temp);
        TextView memoText = (TextView) view.findViewById(R.id.day_memo);
        memoText.setText(memo);

        return view;
    }
}
