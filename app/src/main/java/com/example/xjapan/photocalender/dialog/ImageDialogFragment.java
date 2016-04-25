package com.example.xjapan.photocalender.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.xjapan.photocalender.R;
import com.squareup.picasso.Picasso;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by xjapan on 2016/04/25.
 */
public class ImageDialogFragment extends DialogFragment {

    @Bind(R.id.dialog_image)
    ImageView dayImage;
    File imageFile;

    public ImageDialogFragment(File imageFile) {
        this.imageFile = imageFile;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        ButterKnife.bind(getActivity());
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.image_dialog_fragment);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater i, ViewGroup c, Bundle b) {
        View view = i.inflate(R.layout.image_dialog_fragment, (ViewGroup) getActivity().findViewById(R.id.dialog_image_layout));
        ImageView test = (ImageView) view.findViewById(R.id.dialog_image);
        Picasso.with(getContext()).load(imageFile).into(test);
        return view;
    }
}