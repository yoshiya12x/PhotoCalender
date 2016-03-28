package com.example.xjapan.photocalender.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.xjapan.photocalender.R;

/**
 * Created by xjapan on 16/01/14.
 */
public class PhotoFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.testtab, null);
        return view;
    }
}