package com.rayho.tsxiu.app;

import android.app.Application;

import com.blankj.utilcode.util.Utils;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;

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

        initLogger();
    }

    /**
     * Logger配置
     */
    private void initLogger(){
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
//                .showThreadInfo(false)  // (Optional) Whether to show thread info or not. Default true
//                .methodCount(0)         // (Optional) How many method line to show. Default 2
//                .methodOffset(7)        // (Optional) Hides internal method calls up to offset. Default 5
                .tag("Logger")   // (Optional) Global tag for every log. Default PRETTY_LOGGER
                .build();
        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy));
    }

}
