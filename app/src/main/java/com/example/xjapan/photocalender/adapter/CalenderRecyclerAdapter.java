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
import com.example.xjapan.photocalender.model.DailyTop;
import com.example.xjapan.photocalender.model.DayList;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by xjapan on 16/04/04.
 */
public class CalenderRecyclerAdapter extends RecyclerView.Adapter {

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<DayList> allDays;
    private ArrayList<Integer> headerCountList;
    private DailyTopDAO dao;
    private static final int HEADER = 0;
    private static final int ITEM = 1;

    public CalenderRecyclerAdapter(Context context, ArrayList<DayList> allDays, ArrayList<Integer> headerCountList, DailyTopDAO dao) {
        super();
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.allDays = allDays;
        this.headerCountList = headerCountList;
        this.dao = dao;
    }

    @Override
    public int getItemViewType(int position) {
        if (headerCountList.indexOf(position) != -1) {
            return HEADER;
        }
        return ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == HEADER) {
            return new HeaderDayViewHolder(inflater.inflate(R.layout.header_text, parent, false));
        }
        return new ItemViewHolder(inflater.inflate(R.layout.grid_image, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderDayViewHolder) {
            HeaderDayViewHolder headerDayViewHolder = (HeaderDayViewHolder) holder;
            DayList dayList = allDays.get(position);
            headerDayViewHolder.headerTextView.setText(dayList.year + "年" + dayList.month + "月");
        } else if (holder instanceof ItemViewHolder) {
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            final DayList dayList = allDays.get(position);

            //初期化
            itemViewHolder.stampImageView.setImageBitmap(null);
            itemViewHolder.titleMemoTextView.setText("");

            //カレンダー上に日付があるかどうかの判定
            if (dayList.day.isEmpty()) {
                itemViewHolder.gridImageView.setImageBitmap(null);
            } else {
                DailyTop dailyTop = dao.getItem(dayList.year, dayList.month, Integer.parseInt(dayList.day));
                //端末内に各日付に写真、スタンプ、メモが保存されているか判定
                if (dailyTop != null) {
                    //写真が保存されているか判定
                    if (dailyTop.path != null) {
                        File imageFile = new File(dailyTop.path);
                        if (imageFile.exists()) {
                            Picasso.with(context).load(imageFile).into(itemViewHolder.gridImageView);
                        } else {
                            itemViewHolder.gridImageView.setImageResource(R.drawable.noimage1);
                            setStamp(dailyTop.stamp, context, itemViewHolder);
                            setTitleMemo(dailyTop.titleMemo, itemViewHolder);
                        }
                    } else {
                        itemViewHolder.gridImageView.setImageResource(R.drawable.noimage1);
                        setStamp(dailyTop.stamp, context, itemViewHolder);
                        setTitleMemo(dailyTop.titleMemo, itemViewHolder);
                    }
                } else {
                    itemViewHolder.gridImageView.setImageResource(R.drawable.noimage1);
                }
            }

            itemViewHolder.gridTextView.setText(dayList.day);
            Calendar genzai = Calendar.getInstance();
            if (!dayList.day.isEmpty() && dayList.year == genzai.get(Calendar.YEAR) && dayList.month == genzai.get(Calendar.MONTH) + 1 && Integer.parseInt(dayList.day) == genzai.get(Calendar.DATE)) {
                itemViewHolder.gridTextView.setTextColor(Color.BLACK);
                itemViewHolder.gridTextView.setBackgroundColor(context.getResources().getColor(R.color.colorHalfWhite));
            } else if (dayList.isSunday || dayList.isHoliday) {
                itemViewHolder.gridTextView.setTextColor(Color.RED);
            } else if (dayList.isSaturday) {
                itemViewHolder.gridTextView.setTextColor(Color.BLUE);
            } else {
                itemViewHolder.gridTextView.setTextColor(Color.WHITE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return allDays.size();
    }

    public void setStamp(int stamp, Context context, ItemViewHolder holder) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);
        ViewGroup.LayoutParams params = holder.stampImageView.getLayoutParams();
        params.width = displayMetrics.widthPixels / 14;
        params.height = displayMetrics.widthPixels / 14;
        holder.stampImageView.setVisibility(View.VISIBLE);
        holder.stampImageView.setImageResource(stamp);
    }

    public void setTitleMemo(String titleMemo, ItemViewHolder holder) {
        holder.titleMemoTextView.setVisibility(View.VISIBLE);
        holder.titleMemoTextView.setText(titleMemo);
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.item_imageview)
        ImageView gridImageView;
        @Bind(R.id.day_text)
        TextView gridTextView;
        @Bind(R.id.stamp_imageview)
        ImageView stampImageView;
        @Bind(R.id.title_memo)
        TextView titleMemoTextView;

        public ItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public class HeaderDayViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.header_textview)
        TextView headerTextView;

        public HeaderDayViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
