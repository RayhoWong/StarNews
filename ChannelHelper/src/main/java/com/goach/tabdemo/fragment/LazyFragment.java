package com.goach.tabdemo.fragment;

import android.view.View;

import androidx.fragment.app.Fragment;

/**
 * Created by 钟光新 on 2016/9/11 0011.
 */
public abstract class LazyFragment extends Fragment {

    public abstract void initNet();
    public View getTargetView(){
        return getView() ;
    }

}
