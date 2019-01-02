package com.goach.tabdemo;

import android.app.Application;
import android.content.Context;

/**
 * Created by 钟光新 on 2016/9/24 0024.
 */

public class TabApplication extends Application {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }
    public static Context getContext(){
        return mContext;
    }
}
