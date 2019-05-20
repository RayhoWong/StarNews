package com.rayho.tsxiu.app;

import android.app.Application;

import com.blankj.utilcode.util.Utils;
import com.facebook.stetho.Stetho;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;
import com.rayho.tsxiu.utils.DaoManager;

import java.io.IOException;

import io.reactivex.exceptions.UndeliverableException;
import io.reactivex.functions.Consumer;
import io.reactivex.plugins.RxJavaPlugins;
import skin.support.SkinCompatManager;
import skin.support.app.SkinAppCompatViewInflater;
import skin.support.app.SkinCardViewInflater;
import skin.support.design.app.SkinMaterialViewInflater;

/**
 * Created by Rayho on 2018/11/8 0008
 */
public class AppApplication extends Application {

    private static class AppApplicationHolder {
        public static AppApplication application = new AppApplication();
    }

    public static AppApplication getAppApplication() {
        return AppApplicationHolder.application;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //AndroidUtilCode的初始化
        Utils.init(this);

        //初始化Logger
        initLogger();

//        initBugly();

        //初始化greenDao数据库
        DaoManager.getInstance().init(this);
        DaoManager.getInstance().getDaoMaster();

        //初始化Stetho 在chrome可以查看app的数据库
        Stetho.initializeWithDefaults(this);

        //初始化Skin库
        SkinCompatManager.withoutActivity(this)
                .addInflater(new SkinAppCompatViewInflater())           // 基础控件换肤初始化
                .addInflater(new SkinMaterialViewInflater())            // material design 控件换肤初始化[可选]
                .addInflater(new SkinCardViewInflater())                // CardView v7 控件换肤初始化[可选]
                .setSkinStatusBarColorEnable(true)                     // 关闭状态栏换肤，默认打开[可选]
                .setSkinWindowBackgroundEnable(true)    // 关闭windowBackground换肤，默认打开[可选]
                .setSkinAllActivityEnable(true)
                .loadSkin();

    }


    /**
     * Logger配置
     */
    private void initLogger() {
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
//                .showThreadInfo(false)  // (Optional) Whether to show thread info or not. Default true
//                .methodCount(0)         // (Optional) How many method line to show. Default 2
//                .methodOffset(7)        // (Optional) Hides internal method calls up to offset. Default 5
                .tag("Logger")   // (Optional) Global tag for every log. Default PRETTY_LOGGER
                .build();
        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy));
    }


    /**
     * Bugly初始化
     */
//    private void initBugly() {
//        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(getApplicationContext());
//        strategy.setAppVersion(AppUtils.getAppVersionName()); //app版本
//        strategy.setAppPackageName(AppUtils.getAppPackageName()); //app包名
////        strategy.setAppReportDelay(20000);   //Bugly会在启动20s后联网同步数据
//
///*
//          第三个参数为SDK调试模式开关，调试模式的行为特性如下：
//            输出详细的Bugly SDK的Log；
//            每一条Crash都会被立即上报；
//            自定义日志将会在Logcat中输出。
//            建议在测试阶段建议设置成true，发布时设置为false。*/
//        CrashReport.initCrashReport(getApplicationContext(), "9b842bc8ea", false ,strategy);
//    }


    /**
     * RxJava2 当取消订阅后(dispose())，RxJava抛出的异常后续无法接收(此时后台线程仍在跑，
     * 可能会抛出IO等异常),全部由RxJavaPlugin接收，需要提前设置ErrorHandler
     * 详情：https://github.com/ReactiveX/RxJava/wiki/What's-different-in-2.0#error-handling
     */
    private void setRxJavaErrorHandler() {
        RxJavaPlugins.setErrorHandler(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable e) {
                if (e instanceof UndeliverableException) {
                    e = e.getCause();
                }
                if ((e instanceof IOException)) {
                    // fine, irrelevant network problem or API that throws on cancellation
                    return;
                }
                if (e instanceof InterruptedException) {
                    // fine, some blocking code was interrupted by a dispose call
                    return;
                }
                if ((e instanceof NullPointerException) || (e instanceof IllegalArgumentException)) {
                    // that's likely a bug in the application
                    Thread.currentThread().getUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
                    return;
                }
                if (e instanceof IllegalStateException) {
                    // that's a bug in RxJava or in a custom operator
                    Thread.currentThread().getUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
                    return;
                }
                Logger.e(e.getMessage());
                Logger.e("Undeliverable exception received, not sure what to do", e);
            }
        });
    }
}
