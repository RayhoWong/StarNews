package com.rayho.tsxiu.utils;

import android.util.Log;

/**
 * Created by Rayho on 2019/1/27
 * 解决message过多log显示不全的问题
 **/
public class LogUtil {
    //可以全局控制是否打印log日志
    private static boolean isPrintLog = true;
    private static int LOG_MAXLENGTH = 2000;


    public static void v(String msg) {
        v("LogUtil", msg);
    }

    public static void v(String tagName, String msg) {
        if (isPrintLog) {
            int strLength = msg.length();
            int start = 0;
            int end = LOG_MAXLENGTH;
            for (int i = 0; i < 100; i++) {
                if (strLength > end) {
                    Log.v(tagName + i, msg.substring(start, end));
                    start = end;
                    end = end + LOG_MAXLENGTH;
                } else {
                    Log.v(tagName + i, msg.substring(start, strLength));
                    break;
                }
            }
        }
    }

    public static void d(String msg) {
        d("LogUtil", msg);
    }
    public static void d(String tagName, String msg) {
        if (isPrintLog) {
            int strLength = msg.length();
            int start = 0;
            int end = LOG_MAXLENGTH;
            for (int i = 0; i < 100; i++) {
                if (strLength > end) {
                    Log.d(tagName + i, msg.substring(start, end));
                    start = end;
                    end = end + LOG_MAXLENGTH;
                } else {
                    Log.d(tagName + i, msg.substring(start, strLength));
                    break;
                }
            }
        }
    }

    public static void i(String msg) {
        i("LogUtil", msg);
    }

    public static void i(String tagName, String msg) {
        if (isPrintLog) {
            int strLength = msg.length();
            int start = 0;
            int end = LOG_MAXLENGTH;
            for (int i = 0; i < 100; i++) {
                if (strLength > end) {
                    Log.i(tagName + i, msg.substring(start, end));
                    start = end;
                    end = end + LOG_MAXLENGTH;
                } else {
                    Log.i(tagName + i, msg.substring(start, strLength));
                    break;
                }
            }
        }
    }

    public static void w(String msg) {
        w("LogUtil", msg);
    }

    public static void w(String tagName, String msg) {
        if (isPrintLog) {
            int strLength = msg.length();
            int start = 0;
            int end = LOG_MAXLENGTH;
            for (int i = 0; i < 100; i++) {
                if (strLength > end) {
                    Log.w(tagName + i, msg.substring(start, end));
                    start = end;
                    end = end + LOG_MAXLENGTH;
                } else {
                    Log.w(tagName + i, msg.substring(start, strLength));
                    break;
                }
            }
        }
    }

    public static void e(String msg) {
        e("LogUtil", msg);
    }
    public static void e(String tagName, String msg) {
        if (isPrintLog) {
            int strLength = msg.length();
            int start = 0;
            int end = LOG_MAXLENGTH;
            for (int i = 0; i < 100; i++) {
                if (strLength > end) {
                    Log.e(tagName + i, msg.substring(start, end));
                    start = end;
                    end = end + LOG_MAXLENGTH;
                } else {
                    Log.e(tagName + i, msg.substring(start, strLength));
                    break;
                }
            }
        }
    }
}
