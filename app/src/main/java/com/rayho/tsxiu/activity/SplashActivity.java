package com.rayho.tsxiu.activity;

import androidx.appcompat.app.AppCompatActivity;
import rx.android.schedulers.AndroidSchedulers;

import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.blankj.utilcode.util.ActivityUtils;
import com.rayho.tsxiu.R;
import com.rayho.tsxiu.base.Constant;
import com.rayho.tsxiu.greendao.VideoAutoPlayDao;
import com.rayho.tsxiu.module_news.activity.SearchActivity;
import com.rayho.tsxiu.module_video.dao.VideoAutoPlay;
import com.rayho.tsxiu.utils.DaoManager;
import com.rayho.tsxiu.utils.RxTimer;
import com.rayho.tsxiu.utils.RxUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 启动页
 * 不设置任何界面 直接由闪屏页(冷启动过程打开的窗口)背景过渡到主界面
 */
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //将闪屏页的背景图片设置为null 便于回收
        getWindow().setBackgroundDrawable(null);
//        setContentView(R.layout.activity_splash);

        RxTimer.timer(1000, new RxTimer.RxAction() {
            @Override
            public void action() {
                setupShortcuts();
                setAutoplayFlag();
                ActivityUtils.startActivity(MainActivity.class);
                finish();
            }
        });
    }


    /**
     * 设置应用的快捷方式(仅支持25及以上)
     */
    private void setupShortcuts() {
        if (Build.VERSION.SDK_INT >= 25) {
            ShortcutManager shortcutManager = getSystemService(ShortcutManager.class);

            List<ShortcutInfo> infos = new ArrayList<>();
            //扫描
            Intent intentScan = new Intent(this, MainActivity.class);
            intentScan.setAction(Intent.ACTION_VIEW);
            ShortcutInfo scanInfo = new ShortcutInfo.Builder(this, "shortcut_id_scan")
                    .setLongLabel(getResources().getString(R.string.shortcut_scan_long))
                    .setShortLabel(getResources().getString(R.string.shortcut_scan_short))
                    .setIcon(Icon.createWithResource(this, R.drawable.ic_scan_st))
                    .setIntent(intentScan)
                    .build();
            infos.add(scanInfo);
            //搜索
            Intent intentSearch = new Intent(this, SearchActivity.class);
            intentSearch.setAction(Intent.ACTION_VIEW);
            ShortcutInfo searchInfo = new ShortcutInfo.Builder(this, "shortcut_id_search")
                    .setLongLabel(getResources().getString(R.string.shortcut_search_long))
                    .setShortLabel(getResources().getString(R.string.shortcut_search_short))
                    .setIcon(Icon.createWithResource(this, R.drawable.ic_search_st))
                    .setIntents(new Intent[]{
                            new Intent(Intent.ACTION_MAIN, Uri.EMPTY, this, MainActivity.class)
                                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK),
                            intentSearch
                    })
                    .build();
            infos.add(searchInfo);
            //这样就可以通过长按图标显示出快捷方式了
            shortcutManager.setDynamicShortcuts(infos);
        }
    }


    /**
     * 设置视频自动播放的标记
     */
    private void setAutoplayFlag(){
        DaoManager.getInstance().getDaoSession().getVideoAutoPlayDao()
                .queryBuilder()
                .where(VideoAutoPlayDao.Properties.Vid.eq(Constant.AUTO_PLAY_ID))
                .rx()
                .unique()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(videoAutoPlay -> {
                    if (videoAutoPlay == null) {
                        DaoManager.getInstance().getDaoSession().getVideoAutoPlayDao()
                                .rx()
                                .insert(new VideoAutoPlay(null, Constant.AUTO_PLAY_ID, false))
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe();
                    }
                });
    }
}
