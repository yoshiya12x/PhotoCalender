package com.example.xjapan.photocalender;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

/**
 * Created by xjapan on 16/01/17.
 */
public class BitmapUtil {
    public static Bitmap createBitmap(String path, int width, int height) {

        BitmapFactory.Options option = new BitmapFactory.Options();

        option.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, option);

        if (option.outWidth < width || option.outHeight < height) {
            return BitmapFactory.decodeFile(path);
        }

        float scaleWidth = ((float) width) / option.outWidth;
        float scaleHeight = ((float) height) / option.outHeight;

        int newSize = 0;
        int oldSize = 0;
        if (scaleWidth > scaleHeight) {
            newSize = width;
            oldSize = option.outWidth;
        } else {
            newSize = height;
            oldSize = option.outHeight;
        }

        int sampleSize = 1;
        int tmpSize = oldSize;
        while (tmpSize > newSize) {
            sampleSize = sampleSize * 2;
            tmpSize = oldSize / sampleSize;
        }
        if (sampleSize != 1) {
            sampleSize = sampleSize / 2;
        }

        option.inJustDecodeBounds = false;
        option.inSampleSize = sampleSize;

        return BitmapFactory.decodeFile(path, option);
    }

    public static Bitmap resize(Bitmap bitmap, int newWidth, int newHeight) {

        if (bitmap == null) {
            return null;
        }

        int oldWidth = bitmap.getWidth();
        int oldHeight = bitmap.getHeight();

        if (oldWidth < newWidth && oldHeight < newHeight) {
            return bitmap;
        }

        float scaleWidth = ((float) newWidth) / oldWidth;
        float scaleHeight = ((float) newHeight) / oldHeight;
        float scaleFactor = Math.min(scaleWidth, scaleHeight);

        Matrix scale = new Matrix();
        scale.postScale(scaleFactor, scaleFactor);

        Bitmap resizeBitmap = Bitmap.createBitmap(bitmap, 0, 0, oldWidth, oldHeight, scale, false);
        bitmap.recycle();

        return resizeBitmap;

    }
}
