package com.rayho.tsxiu.ui.behavior;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 根据子view滑动方向 底部导航栏隐藏与显示的行为
 * Created by Rayho on 2018/12/5
 **/
public class BottomNavigationBehavior extends CoordinatorLayout.Behavior<View> {
    private ObjectAnimator outAnimator, inAnimator;
    public BottomNavigationBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    //在CoordinatorLayout中，
    //当嵌套的子view(必须实现NestedScrollingChild接口，如Recylerview等)正在滑动的时候回调
    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int nestedScrollAxes) {
        //判断是垂直方向滑动的时候，返回ture回调onNestedPreScroll，代表消耗事件
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    //在子view消耗滑动事件之前调用，实时的监听滑动状态，根据状态从而对child做出特定的响应
    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dx, int dy, int[] consumed) {
        if (dy > 0) {// 上滑隐藏
            if (outAnimator == null) {
                outAnimator = ObjectAnimator.ofFloat(child, "translationY", 0, child.getHeight());
                outAnimator.setDuration(200);
            }
            if (!outAnimator.isRunning() && child.getTranslationY() <= 0) {
                outAnimator.start();
            }
        } else if (dy < 0) {// 下滑显示
            if (inAnimator == null) {
                inAnimator = ObjectAnimator.ofFloat(child, "translationY", child.getHeight(), 0);
                inAnimator.setDuration(200);
            }
            if (!inAnimator.isRunning() && child.getTranslationY() >= child.getHeight()) {
                inAnimator.start();
            }
        }
    }
}
