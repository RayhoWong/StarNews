package com.goach.tabdemo.fragment;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.goach.tabdemo.R;

import androidx.annotation.Nullable;

/**
 * Created by 钟光新 on 2016/9/4 0004.
 */
public class OneFragment extends LazyFragment {
    private View mContainerView;
    public  static OneFragment  newInstance(){
        OneFragment oneFragment = new OneFragment();
        return oneFragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContainerView = inflater.inflate(R.layout.fragment_one,container,false);
        return mContainerView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void initNet() {
    }
}
