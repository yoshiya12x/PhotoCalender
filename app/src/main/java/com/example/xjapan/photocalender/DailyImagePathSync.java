package com.example.xjapan.photocalender;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by xjapan on 16/01/16.
 */
public class DailyImagePathSync extends AsyncTaskLoader<ArrayList<String>> {

    private Context context;
    private int year;
    private int month;
    private String day;
    private ViewHolder holder;
    private DailyImageDB dailyImageDB;
    private DailyStampDB dailyStampDB;
    private DailyTitleMemoDB dailyTitleMemoDB;
    private Common common;

    public DailyImagePathSync(Context context, int year, int month, String day, ViewHolder holder, Common common) {
        super(context);
        this.context = context;
        this.year = year;
        this.month = month;
        this.day = day;
        this.holder = holder;
        this.dailyImageDB = new DailyImageDB(context);
        this.dailyStampDB = new DailyStampDB(context);
        this.dailyTitleMemoDB = new DailyTitleMemoDB(context);
        this.common = common;
    }

    @Override
    public ArrayList<String> loadInBackground() {
        ArrayList<String> resultDB = new ArrayList<>();
        String path = dailyImageDB.selectPath(year, month, Integer.parseInt(day));
        String stamp = dailyStampDB.selectStamp(year, month, Integer.parseInt(day));
        String stampDate = dailyStampDB.selectUpdated(year, month, Integer.parseInt(day));
        String titleMemo = dailyTitleMemoDB.selectTitleMemo(year, month, Integer.parseInt(day));
        String titleMemoDate = dailyTitleMemoDB.selectUpdated(year, month, Integer.parseInt(day));
        resultDB.add(path);
        resultDB.add(stamp);
        resultDB.add(stampDate);
        resultDB.add(titleMemo);
        resultDB.add(titleMemoDate);
        return resultDB;
    }

    @Override
    public void deliverResult(ArrayList<String> result) {
        File imageFile = new File(result.get(0));
        if (!imageFile.exists()) {

            if (result.get(1).equals("") && result.get(3).equals("")) {
            } else {
                if (result.get(1).equals("")) {
                    setTitleMemo(result);
                } else if (result.get(3).equals("")) {
                    setStamp(result);
                } else {
                    try {
                        Date stampDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(result.get(2));
                        Date titleMemoDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(result.get(4));
                        if (stampDate.compareTo(titleMemoDate) > 0) {
                            setStamp(result);
                        } else {
                            setTitleMemo(result);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
            holder.gridImageView.setImageResource(R.drawable.noimage1);

        } else {
            holder.gridImageView.setImageResource(R.drawable.noimage1);
//            Picasso.with(context).load(imageFile).into(imageView);
        }
    }

    public void setStamp(ArrayList<String> result) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);
        ViewGroup.LayoutParams params = holder.stampImageView.getLayoutParams();
        params.width = displayMetrics.widthPixels / 14;
        params.height = displayMetrics.widthPixels / 14;
        holder.stampImageView.setVisibility(View.VISIBLE);
        holder.stampImageView.setImageResource(Integer.parseInt(result.get(1)));
    }

    public void setTitleMemo(ArrayList<String> result) {
        holder.titleMemoTextView.setVisibility(View.VISIBLE);
        holder.titleMemoTextView.setText(result.get(3));
    }

}
