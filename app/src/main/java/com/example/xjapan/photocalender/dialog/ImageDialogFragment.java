package com.example.xjapan.photocalender.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.xjapan.photocalender.R;
import com.squareup.picasso.Picasso;

import java.io.File;

import butterknife.ButterKnife;
import uk.co.senab.photoview.PhotoView;

/**
 * Created by xjapan on 2016/04/25.
 */
public class ImageDialogFragment extends DialogFragment {

    private File imageFile;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        ButterKnife.bind(getActivity());
        imageFile = (File) getArguments().getSerializable("imageFile");
        int year = getArguments().getInt("year");
        int month = getArguments().getInt("month");
        int day = getArguments().getInt("day");
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.image_dialog_fragment);
        dialog.setTitle(Integer.toString(year) + "年" + Integer.toString(month) + "月" + Integer.toString(day) + "日");
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater i, ViewGroup c, Bundle b) {
        View view = i.inflate(R.layout.image_dialog_fragment, (ViewGroup) getActivity().findViewById(R.id.dialog_image_layout));
        PhotoView dayImage = (PhotoView) view.findViewById(R.id.dialog_image);
        Picasso.with(getContext()).load(imageFile).into(dayImage);
        return view;
    }

    public static Bundle createArguments(File imageFile, int year, int month, int day) {
        Bundle arguments = new Bundle();
        arguments.putSerializable("imageFile", imageFile);
        arguments.putInt("year", year);
        arguments.putInt("month", month);
        arguments.putInt("day", day);
        return arguments;
    }
}