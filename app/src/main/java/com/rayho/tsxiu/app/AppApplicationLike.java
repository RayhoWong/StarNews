package com.rayho.tsxiu.app;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.blankj.utilcode.util.Utils;
import com.facebook.stetho.Stetho;
import com.meituan.android.walle.WalleChannelReader;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;
import com.rayho.tsxiu.utils.DaoManager;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.beta.interfaces.BetaPatchListener;
import com.tencent.tinker.entry.DefaultApplicationLike;

import java.io.IOException;
import java.util.Locale;

import androidx.multidex.MultiDex;
import io.reactivex.exceptions.UndeliverableException;
import io.reactivex.functions.Consumer;
import io.reactivex.plugins.RxJavaPlugins;
import skin.support.SkinCompatManager;
import skin.support.app.SkinAppCompatViewInflater;
import skin.support.app.SkinCardViewInflater;
import skin.support.design.app.SkinMaterialViewInflater;


/**
 * Created by Rayho on 2019/5/21
 * 该类是AppApplication的代理类
 * 所有的初始化操作在该类进行
 **/
public class AppApplicationLike extends DefaultApplicationLike {

    public static final String TAG = "Tinker.SampleApplicationLike";


    public AppApplicationLike(Application application, int tinkerFlags, boolean tinkerLoadVerifyFlag, long applicationStartElapsedTime, long applicationStartMillisTime, Intent tinkerResultIntent) {
        super(application, tinkerFlags, tinkerLoadVerifyFlag, applicationStartElapsedTime, applicationStartMillisTime, tinkerResultIntent);
    }


    @Override
    public void onCreate() {
        super.onCreate();

        //初始化Logger
        initLogger();

        initBugly();

        //AndroidUtilCode的初始化
        Utils.init(getApplication());

        //初始化greenDao数据库
        DaoManager.getInstance().init(getApplication());
        DaoManager.getInstance().getDaoMaster();

        //初始化Stetho 在chrome可以查看app的数据库
        Stetho.initializeWithDefaults(getApplication());

        //初始化Skin库
        SkinCompatManager.withoutActivity(getApplication())
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
    private void initBugly() {
        // 设置是否开启热更新能力，默认为true
        Beta.enableHotfix = true;
        // 设置是否自动下载补丁，默认为true
        Beta.canAutoDownloadPatch = true;
        // 设置是否自动合成补丁，默认为true
        Beta.canAutoPatch = true;
        // 设置是否提示用户重启，默认为false
        Beta.canNotifyUserRestart = true;
        // 补丁回调接口
        Beta.betaPatchListener = new BetaPatchListener() {
            @Override
            public void onPatchReceived(String patchFile) {
                Logger.t("Bugly-Tinker");
                Logger.d("onPatchReceived:   补丁下载地址" + patchFile);
            }

            @Override
            public void onDownloadReceived(long savedLength, long totalLength) {
                Logger.t("Bugly-Tinker");
                Logger.d("onDownloadReceived:  补丁下载地址" + String.format(Locale.getDefault(), "%s %d%%",
                        Beta.strNotificationDownloading,
                        (int) (totalLength == 0 ? 0 : savedLength * 100 / totalLength)));
            }

            @Override
            public void onDownloadSuccess(String msg) {
                Logger.t("Bugly-Tinker");
                Logger.d("onDownloadSuccess:   补丁下载成功");
            }

            @Override
            public void onDownloadFailure(String msg) {
                Logger.t("Bugly-Tinker");
                Logger.d("onDownloadFailure:   补丁下载失败");

            }

            @Override
            public void onApplySuccess(String msg) {
                Logger.t("Bugly-Tinker");
                Logger.d("onApplySuccess:   补丁应用成功");
            }

            @Override
            public void onApplyFailure(String msg) {
                Logger.t("Bugly-Tinker");
                Logger.d("onApplyFailure:   补丁应用失败");
            }

            @Override
            public void onPatchRollback() {
                Logger.t("Bugly-Tinker");
                Logger.d("onPatchRollback:   补丁应用失败");
            }
        };

        // 设置开发设备，默认为false，上传补丁如果下发范围指定为“开发设备”，需要调用此接口来标识开发设备
//        Bugly.setIsDevelopmentDevice(getApplication(), true);

        // 多渠道信息塞入
        String channel = WalleChannelReader.getChannel(getApplication());
        Bugly.setAppChannel(getApplication(), channel);

        /*
          这里实现SDK初始化，appId替换成你的在Bugly平台申请的appId
            第三个参数为SDK调试模式开关，调试模式的行为特性如下：
            输出详细的Bugly SDK的Log；
            每一条Crash都会被立即上报；
            自定义日志将会在Logcat中输出。
            建议在测试阶段建议设置成true，发布时设置为false。*/
        Bugly.init(getApplication(), "9b842bc8ea", true);

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


    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void onBaseContextAttached(Context base) {
        super.onBaseContextAttached(base);
        // you must install multiDex whatever tinker is installed!
        MultiDex.install(base);

        // 安装tinker
        // TinkerManager.installTinker(this); 替换成下面Bugly提供的方法
        Beta.installTinker(this);
    }


    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void registerActivityLifecycleCallback(Application.ActivityLifecycleCallbacks callbacks) {
        getApplication().registerActivityLifecycleCallbacks(callbacks);
    }

}
