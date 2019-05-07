package com.rayho.tsxiu.ui;

import android.content.Context;
import android.util.AttributeSet;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import skin.support.widget.SkinCompatBackgroundHelper;
import skin.support.widget.SkinCompatSupportable;

/**
 * Created by Rayho on 2019/5/6
 * 适配Skin库的BottomNavigationView
 * 动态监听换肤行为 设置BottomNavigationView的background属性
 **/
public class SkinBottomNavigationView extends BottomNavigationView implements SkinCompatSupportable {

    private SkinCompatBackgroundHelper mBackgroundTintHelper;

    public SkinBottomNavigationView(Context context) {
        super(context);
    }

    public SkinBottomNavigationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mBackgroundTintHelper = new SkinCompatBackgroundHelper(this);
        mBackgroundTintHelper.loadFromAttributes(attrs, 0);
    }

    @Override
    public void applySkin() {
        if (mBackgroundTintHelper != null) {
            mBackgroundTintHelper.applySkin();
        }
    }
}
