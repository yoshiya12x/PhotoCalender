package com.example.xjapan.photocalender.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.xjapan.photocalender.R;
import com.example.xjapan.photocalender.db.dao.DailyTopDAO;
import com.example.xjapan.photocalender.model.CalenderList;
import com.example.xjapan.photocalender.model.DailyTop;
import com.example.xjapan.photocalender.model.DayList;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by xjapan on 16/04/04.
 */
public class CalenderRecyclerAdapter extends RecyclerView.Adapter<CalenderRecyclerAdapter.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<CalenderList> allList;
    private ArrayList<DayList> allDays;
    private DailyTopDAO dao = DailyTopDAO.get();

    public CalenderRecyclerAdapter(Context context, ArrayList<CalenderList> allList, ArrayList<DayList> allDays) {
        super();
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.allList = allList;
        this.allDays = allDays;
    }

    @Override
    public CalenderRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.grid_image, parent, false));
    }

    @Override
    public void onBindViewHolder(CalenderRecyclerAdapter.ViewHolder holder, int position) {

        final DayList dayList = allDays.get(position);

        //初期化
        holder.stampImageView.setImageBitmap(null);
        holder.titleMemoTextView.setText("");

        //カレンダー上に日付があるかどうかの判定
        if (dayList.day.isEmpty()) {
            holder.gridImageView.setImageBitmap(null);
        } else {
            holder.gridImageView.setTag(position + "");
            DailyTop dailyTop = dao.getItem(dayList.year, dayList.month, Integer.parseInt(dayList.day));
            //端末内に各日付に写真、スタンプ、メモが保存されているか判定
            if (dailyTop != null) {
                //写真が保存されているか判定
                if (dailyTop.path != null) {
                    File imageFile = new File(dailyTop.path);
                    if (imageFile.exists()) {
                        Picasso.with(context).load(imageFile).into(holder.gridImageView);
                    } else {
                        holder.gridImageView.setImageResource(R.drawable.noimage1);
                    }
                } else {
                    holder.gridImageView.setImageResource(R.drawable.noimage1);
                    setStamp(dailyTop.stamp, context, holder);
                    setTitleMemo(dailyTop.titleMemo, holder);
                }
            } else {
                holder.gridImageView.setImageResource(R.drawable.noimage1);
            }
        }

        holder.gridTextView.setText(dayList.day);
        Calendar genzai = Calendar.getInstance();
        if (!dayList.day.isEmpty() && dayList.year == genzai.get(Calendar.YEAR) && dayList.month == genzai.get(Calendar.MONTH) + 1 && Integer.parseInt(dayList.day) == genzai.get(Calendar.DATE)) {
            holder.gridTextView.setTextColor(context.getResources().getColor(R.color.colorDarkGray));
        } else if (dayList.isSunday || dayList.isHoliday) {
            holder.gridTextView.setTextColor(Color.RED);
        } else if (dayList.isSaturday) {
            holder.gridTextView.setTextColor(Color.BLUE);
        } else {
            holder.gridTextView.setTextColor(Color.WHITE);
        }
    }

    @Override
    public int getItemCount() {
        return allDays.size();
    }

    public void setStamp(int stamp, Context context, ViewHolder holder) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);
        ViewGroup.LayoutParams params = holder.stampImageView.getLayoutParams();
        params.width = displayMetrics.widthPixels / 14;
        params.height = displayMetrics.widthPixels / 14;
        holder.stampImageView.setVisibility(View.VISIBLE);
        holder.stampImageView.setImageResource(stamp);
        holder.stampImageView.setImageAlpha(100);
    }

    public void setTitleMemo(String titleMemo, ViewHolder holder) {
        holder.titleMemoTextView.setVisibility(View.VISIBLE);
        holder.titleMemoTextView.setText(titleMemo);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView gridImageView;
        private TextView gridTextView;
        private ImageView stampImageView;
        private TextView titleMemoTextView;

        public ViewHolder(View view) {
            super(view);
            this.gridImageView = (ImageView) view.findViewById(R.id.item_imageview);
            this.gridTextView = (TextView) view.findViewById(R.id.day_text);
            this.stampImageView = (ImageView) view.findViewById(R.id.stamp_imageview);
            this.titleMemoTextView = (TextView) view.findViewById(R.id.title_memo);
        }
    }
}
