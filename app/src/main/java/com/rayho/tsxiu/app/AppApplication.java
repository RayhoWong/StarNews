package com.rayho.tsxiu.app;

import android.app.Application;

import com.blankj.utilcode.util.Utils;

/**
 * Created by Rayho on 2018/11/8 0008
 */
public class AppApplication extends Application {

    private static class AppApplicationHolder{
        public static AppApplication application = new AppApplication();
    }

    public static AppApplication getAppApplication(){
        return AppApplicationHolder.application;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        //AndroidUtilCode的初始化
        Utils.init(this);
    }
}
