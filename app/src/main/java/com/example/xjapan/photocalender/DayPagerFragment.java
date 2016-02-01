package com.example.xjapan.photocalender;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.SpannableStringBuilder;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by xjapan on 16/01/08.
 */
public class DayPagerFragment extends Fragment {

    private EditText editText;
    private Button memoButton;
    private DailyMemoDB dailyMemoDB;
    private int year;
    private int month;
    private int day;
    private Button memoConfirmText;
    private Button changeImageButton;
    private RelativeLayout imageRelativeLayout;
    private ImageView dayImage;
    public Common common;
    private DisplayMetrics displayMetrics;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.date, null);
        common = (Common) getActivity().getApplication();
        dayImage = (ImageView) view.findViewById(R.id.day_detail_image);

        imageRelativeLayout = (RelativeLayout) view.findViewById(R.id.imageRelativeLayout);

        year = getArguments().getInt("year");
        month = getArguments().getInt("month");
        day = getArguments().getInt("day");

        dailyMemoDB = new DailyMemoDB(getActivity());
        String memo = dailyMemoDB.selectMemo(year, month, day);

        memoConfirmText = (Button) view.findViewById(R.id.memoConfirm);
        editText = (EditText) view.findViewById(R.id.dateEditText);
        editText.setText(memo, TextView.BufferType.NORMAL);
        memoButton = (Button) view.findViewById(R.id.memoButton);
        memoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SpannableStringBuilder sb = (SpannableStringBuilder) editText.getText();
                int id = dailyMemoDB.selectDailyMemoId(year, month, day);
                if (id == 0) {
                    dailyMemoDB.insertMemo(year, month, day, sb.toString());
                } else {
                    dailyMemoDB.updateMemo(year, month, day, sb.toString());
                }
                memoConfirmText.setVisibility(View.VISIBLE);
                animateAlpha1(memoConfirmText);
            }
        });

        if (memo.equals("")) {
            memoButton.setEnabled(false);
            memoButton.setBackground(getResources().getDrawable(R.drawable.memo_send_button));
            memoButton.setTextColor(getResources().getColor(R.color.colorDarkGray));
        } else {
            memoButton.setEnabled(true);
            memoButton.setBackground(getResources().getDrawable(R.drawable.memo_send_button_true));
            memoButton.setTextColor(getResources().getColor(R.color.colorAccent));
        }

        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_ENTER) {
                    memoButton.setEnabled(true);
                    memoButton.setBackground(getResources().getDrawable(R.drawable.memo_send_button_true));
                    memoButton.setTextColor(getResources().getColor(R.color.colorAccent));
                }
                return false;
            }
        });

        changeImageButton = (Button) view.findViewById(R.id.changeImageButton);
        changeImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                common.year = year;
                common.month = month;
                common.day = day;
                DialogFragment newFragment = new ContactUsDialogFragment();
                newFragment.show(getFragmentManager(), "contact_us");
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        //ここではRalativeLayoutに縦幅横幅を設定するため，Picassoを使わない．
        SetDayDetailImage setDayDetailImage = new SetDayDetailImage(getContext(), imageRelativeLayout, dayImage, year, month, day, common);
        setDayDetailImage.forceLoad();
        memoConfirmText.setVisibility(View.GONE);
    }

    public void animateAlpha1(Button target) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(target, "alpha", 0f, 1f);
        objectAnimator.setDuration(800);
        objectAnimator.start();
    }

    public class ContactUsDialogFragment extends DialogFragment {

        private Uri fileUri;
        private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
        private static final int GALLERY_IMAGE_ACTIVITY_REQUEST_CODE = 10;
        public static final int MEDIA_TYPE_IMAGE = 1;
        public static final int MEDIA_TYPE_VIDEO = 2;
        public Common common;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            CharSequence[] items = {"ギャラリーから選択", "写真を撮る", "キャンセル"};
            common = (Common) getActivity().getApplication();

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setItems(items, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case 0:
                            gallery();
                            common.isCamera = false;
                            common.isGallery = true;
                            break;
                        case 1:
                            common.isGallery = false;
                            common.isCamera = true;
                            camera();
                            break;
                        case 2:

                            break;
                        default:
                            break;
                    }
                }
            });
            return builder.create();
        }

        public void camera() {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
            common.m_uri = fileUri;
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }

        public void gallery() {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, GALLERY_IMAGE_ACTIVITY_REQUEST_CODE);
        }

        public Uri getOutputMediaFileUri(int type) {
            return Uri.fromFile(getOutputMediaFile(type));
        }

        public File getOutputMediaFile(int type) {
            File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES), "MyCameraApp");

            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    Log.d("MyCameraApp", "failed to create directory");
                    return null;
                }
            }

            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            File mediaFile;
            if (type == MEDIA_TYPE_IMAGE) {
                mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                        "IMG_" + timeStamp + ".jpg");
            } else if (type == MEDIA_TYPE_VIDEO) {
                mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                        "VID_" + timeStamp + ".mp4");
            } else {
                return null;
            }

            return mediaFile;
        }
    }

    private RelativeLayout.LayoutParams createRelativeLayoutParam(int w, int h) {
        return new RelativeLayout.LayoutParams(w, h);
    }
}
