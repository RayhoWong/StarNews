package com.rayho.tsxiu.app;

import android.app.Application;
import android.util.Log;

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
        //初始化greenDao数据库
        DaoManager.getInstance().init(this);
        DaoManager.getInstance().getDaoMaster();
        //初始化Stetho 在chrome可以查看app的数据库
        Stetho.initializeWithDefaults(this);
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
