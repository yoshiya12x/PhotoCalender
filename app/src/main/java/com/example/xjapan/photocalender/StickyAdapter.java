package com.example.xjapan.photocalender;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
    private Context context;
    private Common common;
    private int temp_i;
    private DayList dayList;

    public StickyAdapter(Context context, ArrayList<CalenderList> allList, ArrayList<DayList> allDays, Common common) {
        this.inflater = LayoutInflater.from(context);
        this.allList = allList;
        this.allDays = allDays;
        this.context = context;
        this.common = common;
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
        ViewHolder holder;
        int test = i;
        temp_i = i;
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

        dayList = allDays.get(i);

        if (dayList.day.equals("")) {
            holder.gridImageView.setImageBitmap(null);
        } else {
            DailyImagePathSync dailyImagePathSync = new DailyImagePathSync(context, dayList.year, dayList.month, dayList.day, holder, common, holder.gridImageView);
            dailyImagePathSync.forceLoad();

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (common.isStamp) {
                        //スタンプ9つに絞る
                        common.year = dayList.year;
                        common.month = dayList.month;
                        common.day = Integer.parseInt(dayList.day);
                        View layout = inflater.inflate(R.layout.select_stamp, (ViewGroup) view.findViewById(R.id.select_stamp_layout));
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle(dayList.year + "年" + dayList.month + "月" + dayList.day + "日");
                        builder.setView(layout);
                        AlertDialog alertDialog = builder.show();
                        common.alertDialog = alertDialog;
                    } else if (common.isPencil) {
                        common.year = dayList.year;
                        common.month = dayList.month;
                        common.day = Integer.parseInt(dayList.day);
                        View layout = inflater.inflate(R.layout.edit_title_memo, (ViewGroup) view.findViewById(R.id.edit_title_memo_layout));
                        final EditText editText = (EditText) layout.findViewById(R.id.edit_title_memo);
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle(dayList.year + "年" + dayList.month + "月" + dayList.day + "日");
                        builder.setView(layout);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                DailyTopDB dailyTopDB = new DailyTopDB(context);
                                SpannableStringBuilder sb = (SpannableStringBuilder) editText.getText();
                                ArrayList<String> topList = dailyTopDB.selectAll(common.year, common.month, common.day);
                                if (topList == null) {
                                    dailyTopDB.insertTitleMemo(common.year, common.month, common.day, sb.toString());
                                } else {
                                    dailyTopDB.updateTitleMemo(common.year, common.month, common.day, sb.toString());
                                }
                            }
                        });
                        builder.setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        AlertDialog alertDialog = builder.show();
                        common.alertDialog = alertDialog;
                    } else {
                        CalenderList postCalenderList = getCalenderListByDayId(temp_i);
                        Intent intent = new Intent(view.getContext(), MonthDetailActivity.class);
                        intent.putExtra("dayListDay", dayList.day);
                        intent.putExtra("calenderListYear", postCalenderList.year);
                        intent.putExtra("calenderListMonth", postCalenderList.month);
                        intent.putExtra("calenderListDays", postCalenderList.days);
                        intent.putExtra("calenderListStartDay", postCalenderList.startDay);
                        view.getContext().startActivity(intent);
                    }
                }
            });
        }

        holder.gridTextView.setText(dayList.day);

        Calendar genzai = Calendar.getInstance();
        if (dayList.isSunday || dayList.isHoliday) {
            holder.gridTextView.setTextColor(Color.RED);
        } else if (dayList.isSaturday) {
            holder.gridTextView.setTextColor(Color.BLUE);
        } else if (!dayList.day.equals("") && dayList.year == genzai.get(Calendar.YEAR) && dayList.month == genzai.get(Calendar.MONTH) + 1 && Integer.parseInt(dayList.day) == genzai.get(Calendar.DATE)) {
            holder.gridTextView.setTextColor(context.getResources().getColor(R.color.colorDarkGray));
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

    public CalenderList getCalenderListByDayId(int dayId) {
        CalenderList calenderList = new CalenderList();
        int count = 0;
        for (int i = 0; i < allList.size(); i++) {
            CalenderList object = allList.get(i);
            count = count + object.startDay + object.days - 1;
            if (count < dayId) {
            } else {
                calenderList = object;
                break;
            }
        }
        return calenderList;
    }

}
