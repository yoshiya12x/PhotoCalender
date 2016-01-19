package com.example.xjapan.photocalender;

import android.content.Context;
import android.net.Uri;
import android.support.v4.content.AsyncTaskLoader;
import android.widget.ImageView;

import java.io.File;

/**
 * Created by xjapan on 16/01/16.
 */
public class SetMonthDetailImage extends AsyncTaskLoader<Void>{

    private ImageView imageView;
    private File file;

    public SetMonthDetailImage(Context context, ImageView imageView, File file) {
        super(context);
        this.imageView = imageView;
        this.file = file;
    }

    @Override
    public Void loadInBackground() {
        imageView.setImageURI(Uri.fromFile(file));
        return null;
    }
}
