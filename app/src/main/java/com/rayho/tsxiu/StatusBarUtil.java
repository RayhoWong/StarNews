package com.rayho.tsxiu;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by Rayho on 2018/11/5 0005
 * 状态栏工具类
 */
public class StatusBarUtil {

    /**
     * 状态栏透明
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

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            context.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    /**
     * 改变状态栏颜色
     * @param context
     * @param color
     */
    public static void setStatusBarColor(Activity context,int color) {
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

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            context.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    /**
     * 隐藏状态栏
     * @param context
     */
    public static void hideStatusBar(Activity context){
        View decorView = context.getWindow().getDecorView();
        int option = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(option);
    }

    /**
     * 改变状态栏字体的颜色
     * 6.0及以上版本可以更改状态栏的字体颜色，如果状态栏的背景色是浅色或者白色，
     * 那么字体图标的颜色趋向于深色或者是黑色
     * 记得根布局设置属性android:fitsSystemWindows="true"，不然toolbar会占用状态栏
     * @param context
     */
    public static void changeStatusBarTextColor(Activity context){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            context.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }


    /**
     * 为ToolBar添加一个状态栏高度的paddingTop
     * 效果等于fitsSystemWindows=true这个属性
     * @param context
     * @param toolbar
     */
    public static void setToolBarPaddingTop(Activity context,View toolbar) {

        final float scale = context.getResources().getDisplayMetrics().density;

        int statusBarHeight = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
        }
        Log.d("CompatToolbar", "状态栏高度：" + (statusBarHeight/scale) + "dp");

        int compatPadingTop = 0;
        // android 4.4以上将Toolbar添加状态栏高度的上边距，沉浸到状态栏下方
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            compatPadingTop = statusBarHeight;
        }
        toolbar.setPadding(toolbar.getPaddingLeft(), toolbar.getPaddingTop() + compatPadingTop,
                toolbar.getPaddingRight(), toolbar.getPaddingBottom());
    }

   /* public int getStatusBarHeight() {
        int statusBarHeight = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }
        Log.d("CompatToolbar", "状态栏高度：" + px2dp(statusBarHeight) + "dp");
        return statusBarHeight;
    }

    public float px2dp(float pxVal) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (pxVal / scale);
    }
*/





}
