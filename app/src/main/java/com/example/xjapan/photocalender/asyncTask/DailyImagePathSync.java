package com.example.xjapan.photocalender.asyncTask;

import android.content.Context;
import android.os.AsyncTask;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.example.xjapan.photocalender.R;
import com.example.xjapan.photocalender.ViewHolder;
import com.example.xjapan.photocalender.db.DailyTopDB;
import com.example.xjapan.photocalender.db.dao.DailyTopDAO;
import com.example.xjapan.photocalender.model.DailyTop;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xjapan on 16/01/16.
 */
public class DailyImagePathSync extends AsyncTask<Integer, Void, ArrayList<String>> {

    private Context context;
    private DailyTopDB dailyTopDB;
    private ViewHolder holder;
    private String tag;
    private DailyTopDAO dao = DailyTopDAO.get();

    public DailyImagePathSync(ViewHolder viewHolder, Context context) {
        this.context = context;
        this.dailyTopDB = new DailyTopDB(context);
        this.holder = viewHolder;
        this.tag = viewHolder.gridImageView.getTag().toString();
    }

    @Override
    protected ArrayList<String> doInBackground(Integer... ints) {
        Log.d("dao_test", "test");
        List<DailyTop> test = dao.findAll();
        Log.d("dao_test", test.get(0).day);

        return dailyTopDB.selectAll(ints[0], ints[1], ints[2]);
    }

    @Override
    protected void onPostExecute(ArrayList<String> result) {
        if (this.tag.equals(this.holder.gridImageView.getTag())) {
            if (result.size() != 0) {
                if (result.get(0) != null) {
                    File imageFile = new File(result.get(0));
                    if (imageFile.exists()) {
//                        holder.gridImageView.setImageResource(R.drawable.noimage1);
                        Picasso.with(context).load(imageFile).into(holder.gridImageView);
                    } else {
                        holder.gridImageView.setImageResource(R.drawable.noimage1);
                    }
                } else {
                    holder.gridImageView.setImageResource(R.drawable.noimage1);

                    if (result.get(3).equals("0")) {
                        setStamp(result);
                    } else if (result.get(3).equals("1")) {
                        setTitleMemo(result);
                    }
                }
            } else {
                holder.gridImageView.setImageResource(R.drawable.noimage1);
            }
        } else {
            Log.d("onPostExecute", "はいらない");
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
        holder.titleMemoTextView.setText(result.get(2));
    }
}