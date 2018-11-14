package com.rayho.tsxiu.utils;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.rayho.tsxiu.R;


/**
 * Created by koo on 2016/5/28.
 * snackbar工具类
 */

public class SnackbarUtil {
    /**
     * 系统默认snackbar不带有action按钮
     *
     * @param view
     * @param message
     * @param length  :显示的时间
     */
    public static void showSnackbar(View view, String message, int length) {
        Snackbar.make(view, message, length).show();
    }

    /**
     * 系统默认snackbar带有按钮点击事件
     *
     * @param view
     * @param message
     * @param action
     */
    public static void showSnackbarWithAction(View view, String message, int length,
                                              String action, View.OnClickListener listener) {
        Snackbar.make(view, message, length)
                .setAction(action, listener)
                .show();
    }


    /**
     * 自定义修改背景,message的颜色,不实现点击方法
     * 调用snackbar相当于toast
     *
     * @param view
     */
    public static void showSnackbarColorNoAction(Context context, View view, String message,
                                                 int background, int length,
                                                 int messageColor) {

        Snackbar snackbar = Snackbar.make(view, message, length);
        //设置snackbar的背景颜色
        snackbar.getView().setBackgroundColor(context.getResources().getColor(background));
        //设置snackbar中message的颜色
        setSnackbarMessageTextColor(snackbar, context.getResources().getColor(messageColor));
        snackbar.show();
    }

    /**
     * 自定义修改背景,message和action的颜色,实现点击方法
     * 调用snackbar相当于toast
     *
     * @param view
     */
    public static void showSnackbarColorAction(Context context, View view, String message,
                                               String action, int background, int length,
                                               int messageColor, int actionColor, View.OnClickListener listener) {

        Snackbar snackbar = Snackbar.make(view, message, length)
                .setAction(action, listener)
                //设置button的颜色
                .setActionTextColor(context.getResources().getColor(actionColor));
        //设置snackbar的背景颜色
        snackbar.getView().setBackgroundColor(context.getResources().getColor(background));
        //设置snackbar中message的颜色
        setSnackbarMessageTextColor(snackbar, context.getResources().getColor(messageColor));
        snackbar.show();
    }

    /**
     * 设置snackbar中message的颜色
     *
     * @param snackbar
     * @param color
     */
    private static void setSnackbarMessageTextColor(Snackbar snackbar, int color) {
        View view = snackbar.getView();
        ((TextView) view.findViewById(R.id.snackbar_text)).setTextColor(color);
    }
}
