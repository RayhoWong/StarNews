package com.rayho.tsxiu.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.ColorInt;
import androidx.core.graphics.ColorUtils;

/**
 * Created by Rayho on 2018/11/5 0005
 * 状态栏工具类
 */
public class StatusBarUtil {

    /**
     * 状态栏透明
     *
     * @param context
     */
    public static void setStatusBarTranslucent(Activity context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0及以上
            View decorView = context.getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            //状态栏设置透明
            context.getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4到5.0
            WindowManager.LayoutParams localLayoutParams = context.getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            context.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    /**
     * 改变状态栏颜色
     *
     * @param context
     * @param color
     */
    public static void setStatusBarColor(Activity context, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0及以上
            View decorView = context.getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            //状态栏单独设置颜色
            context.getWindow().setStatusBarColor(context.getResources().getColor(color));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4到5.0
            WindowManager.LayoutParams localLayoutParams = context.getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            context.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    /**
     * 隐藏状态栏
     *
     * @param context
     */
    public static void hideStatusBar(Activity context) {
        View decorView = context.getWindow().getDecorView();
        int option = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(option);
    }


    /**
     * 改变状态栏字体的颜色
     * 记得根布局设置属性android:fitsSystemWindows="true"，不然toolbar会占用状态栏
     *
     * @param context
     */
    public static void changeStatusBarTextColor(Activity context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            context.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }


    /**
     * Android 6.0 及以上根据状态栏颜色亮度 设置状态栏文字的颜色(黑/白)
     * 一般应用于夜间模式切换
     */
    public static void setStatusBarTextColor(@ColorInt int color, Activity context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            /**判断颜色是不是亮色
             如果亮色，设置状态栏文字为黑色 否则为白色*/
            if (ColorUtils.calculateLuminance(color) >= 0.5) {
                //黑色
                context.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            } else {
                //白色
                context.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            }
        }
    }


    /**
     * Android 6.0 及以上设置状态栏颜色 以及根据状态栏颜色亮度 设置状态栏文字的颜色(黑/白)
     */
    public static void setStatusBarColor(@ColorInt int color, Activity context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            // 设置状态栏底色颜色
            context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            context.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            context.getWindow().setStatusBarColor(color);

            /**判断颜色是不是亮色
             如果亮色，设置状态栏文字为黑色 否则为白色*/
            if (ColorUtils.calculateLuminance(color) >= 0.5) {
                //黑色
                context.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            } else {
                //白色
                context.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            }
        }
    }


    /**
     * 为ToolBar添加一个状态栏高度的paddingTop
     * 效果等于fitsSystemWindows=true这个属性
     *
     * @param context
     * @param toolbar
     */
    public static void setToolBarPaddingTop(Activity context, View toolbar) {

        final float scale = context.getResources().getDisplayMetrics().density;

        int statusBarHeight = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
        }
        Log.d("CompatToolbar", "状态栏高度：" + (statusBarHeight / scale) + "dp");

        int compatPadingTop = 0;
        // android 4.4以上将Toolbar添加状态栏高度的上边距，沉浸到状态栏下方
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            compatPadingTop = statusBarHeight;
        }
        toolbar.setPadding(toolbar.getPaddingLeft(), toolbar.getPaddingTop() + compatPadingTop,
                toolbar.getPaddingRight(), toolbar.getPaddingBottom());
    }
}
