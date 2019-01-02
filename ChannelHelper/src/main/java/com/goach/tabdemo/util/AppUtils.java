package com.goach.tabdemo.util;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.WindowManager;

import com.goach.tabdemo.other.TabApplication;

/**
 * Created by 钟光新 on 2016/9/10 0010.
 */
public class AppUtils {
    public static int getScreenWidth(Context context){
        WindowManager windowManager = (WindowManager) context.getSystemService(Activity.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }
    public static int dp2px(int value){
        return (int) (TabApplication.getContext().getResources().getDisplayMetrics().density*value);
    }
    public static int sp2px(int value){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,value,TabApplication.getContext().getResources().getDisplayMetrics());
    }
}
