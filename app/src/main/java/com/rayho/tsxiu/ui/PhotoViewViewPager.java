package com.rayho.tsxiu.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

/**
 * Created by Rayho on 2019/4/3
 * 解决viewpager和photoview的手势冲突
 * 程序就会崩掉，并且抛出java.lang.IllegalArgumentException: pointerIndex out of range
 **/
public class PhotoViewViewPager extends ViewPager {

    public PhotoViewViewPager(@NonNull Context context) {
        super(context);
    }

    public PhotoViewViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        return false;
    }
}
