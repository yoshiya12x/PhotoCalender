package com.example.xjapan.photocalender;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.content.AsyncTaskLoader;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by xjapan on 16/01/16.
 */
public class SetDayDetailImage extends AsyncTaskLoader<String> {

    private Context context;
    private RelativeLayout relativeLayout;
    private ImageView imageView;
    private int year;
    private int month;
    private int day;
    private DailyTopDB dailyTopDB;
    private Common common;

    public SetDayDetailImage(Context context, RelativeLayout relativeLayout, ImageView imageView, int year, int month, int day, Common common) {
        super(context);
        this.context = context;
        this.relativeLayout = relativeLayout;
        this.imageView = imageView;
        this.year = year;
        this.month = month;
        this.day = day;
        this.dailyTopDB = new DailyTopDB(context);
        this.common = common;
    }

    @Override
    public String loadInBackground() {
        ArrayList<String> item = dailyTopDB.selectAll(year, month, day);
        if(item.size() == 0){
            return "";
        }
        return item.get(0);
    }

    @Override
    public void deliverResult(String result) {
        if (result.equals("")) {
        } else {
            File imageFile = new File(result);
            if (imageFile.exists()) {
                WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
                Display display = wm.getDefaultDisplay();
                DisplayMetrics displayMetrics = new DisplayMetrics();
                display.getMetrics(displayMetrics);
                BitmapUtil bitmapUtil = new BitmapUtil();
                Bitmap bitmap = bitmapUtil.createBitmap(result, displayMetrics.widthPixels * 3 / 5, displayMetrics.widthPixels * 3 / 5);
                imageView.setImageBitmap(bitmap);
                RelativeLayout.LayoutParams params = createRelativeLayoutParam(bitmap.getWidth(), bitmap.getHeight());
                params.addRule(RelativeLayout.CENTER_IN_PARENT, 1);
                params.addRule(RelativeLayout.ALIGN_PARENT_TOP, 1);
                relativeLayout.setLayoutParams(params);
            } else {
            }
        }
    }

    private RelativeLayout.LayoutParams createRelativeLayoutParam(int w, int h) {
        return new RelativeLayout.LayoutParams(w, h);
    }
}
