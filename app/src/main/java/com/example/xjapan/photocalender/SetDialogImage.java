package com.example.xjapan.photocalender;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * Created by xjapan on 16/01/17.
 */
public class SetDialogImage extends AsyncTaskLoader<String> {

    private Context context;
    private ImageView nextImageView;
    private ImageView preImageView;
    private String nextPath;
    private int year;
    private int month;
    private int day;
    private DailyImageDB dailyImageDB;

    public SetDialogImage(Context context, ImageView nextImageView, ImageView preImageView, String nextPath, int year, int month, int day) {
        super(context);
        this.nextImageView = nextImageView;
        this.preImageView = preImageView;
        this.context = context;
        this.nextPath = nextPath;
        this.year = year;
        this.month = month;
        this.day = day;
        this.dailyImageDB = new DailyImageDB(context);
    }

    @Override
    public String loadInBackground() {
        String preImagePath = dailyImageDB.selectPath(year, month, day);
        return preImagePath;
    }

    @Override
    public void deliverResult(String preImagePath) {
        File imageFile = new File(nextPath);
        Picasso.with(context).load(imageFile).into(nextImageView);

        imageFile = new File(preImagePath);
        if(imageFile.exists()){
            Picasso.with(context).load(imageFile).into(preImageView);
        }
//        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//        Display display = wm.getDefaultDisplay();
//        DisplayMetrics displayMetrics = new DisplayMetrics();
//        display.getMetrics(displayMetrics);
//        BitmapUtil bitmapUtil = new BitmapUtil();
//        Bitmap bitmap = bitmapUtil.createBitmap(nextPath, displayMetrics.widthPixels / 2, displayMetrics.widthPixels / 2);
//        nextImageView.setImageBitmap(bitmap);
//
//        File imageFile = new File(preImagePath);
//        if(imageFile.exists()){
//            bitmap = bitmapUtil.createBitmap(preImagePath, displayMetrics.widthPixels / 2, displayMetrics.widthPixels / 2);
//            preImageView.setImageBitmap(bitmap);
//        }
    }
}
