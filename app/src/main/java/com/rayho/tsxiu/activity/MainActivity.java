package com.rayho.tsxiu.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.orhanobut.logger.Logger;
import com.rayho.tsxiu.R;
import com.rayho.tsxiu.base.BaseActivity;
import com.rayho.tsxiu.base.Constant;
import com.rayho.tsxiu.base.listener.OnTabReselectedListener;
import com.rayho.tsxiu.greendao.VideoAutoPlayDao;
import com.rayho.tsxiu.module_mine.MineTabFragment;
import com.rayho.tsxiu.module_news.NewsTabFragment;
import com.rayho.tsxiu.module_news.activity.ScannerResultActivity;
import com.rayho.tsxiu.module_news.activity.SearchActivity;
import com.rayho.tsxiu.module_photo.PhotoTabFragment;
import com.rayho.tsxiu.module_video.VideoTabFragment;
import com.rayho.tsxiu.module_video.dao.VideoAutoPlay;
import com.rayho.tsxiu.ui.SkinBottomNavigationView;
import com.rayho.tsxiu.utils.DaoManager;
import com.yzq.zxinglibrary.android.CaptureActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;

public class MainActivity extends BaseActivity {


    @BindView(R.id.bottom_navigation_bar)
    SkinBottomNavigationView mBottomNavigationBar;

    private Fragment mFragment;//当前显示的fragment
    private Fragment mNewsTabFragment;
    private Fragment mPhotoTabFragment;
    private Fragment mMineTabFragment;
    private Fragment mVideoTabFragment;

    private OnTabReselectedListener mListener;


    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }


    @Override
    public void afterSetContentView() {
        setupShortcuts();

        hideBaseToolbar();
        initBottomNavigationBar();
        initFragment();

        //设置视频自动播放的标记
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


    @SuppressLint("MissingSuperCall")
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //如果用以下这种做法则不保存状态，再次进来的话会显示默认tab
        //总是执行这句代码来调用父类去保存视图层的状态
        //解决Fragment重叠的问题
        //super.onSaveInstanceState(outState);
    }


    /**
     * 设置应用的快捷方式(仅支持25及以上)
     *
     */
    private void setupShortcuts(){
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



    private void initFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        mNewsTabFragment = NewsTabFragment.newInstance();
        mPhotoTabFragment = PhotoTabFragment.newInstance();
        mVideoTabFragment = VideoTabFragment.newInstance();
        mMineTabFragment = MineTabFragment.newInstance();
        //默认显示新闻首页
        transaction.add(R.id.frame, mNewsTabFragment);
        transaction.commit();
        mFragment = mNewsTabFragment;
    }


    /**
     * 初始化底部导航栏
     */
    private void initBottomNavigationBar() {

        mBottomNavigationBar.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    switchFragment(mNewsTabFragment, "news");
                    return true;
                case R.id.navigation_photos:
                    switchFragment(mPhotoTabFragment, "photo");
                    return true;
                case R.id.navigation_videos:
                    switchFragment(mVideoTabFragment, "video");
                    return true;
                case R.id.navigation_person:
                    switchFragment(mMineTabFragment, "mine");
                    return true;
            }
            return false;
        });

        //监听Tab被重新选中
        mBottomNavigationBar.setOnNavigationItemReselectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    //再次点击首页Tab时，当前Fragment重新加载数据
                    if (mListener != null)
                        mListener.updateData();
                    break;
                default:
                    break;
            }
        });
    }

    /**
     * 获取当前显示的ContentFragment实例
     *
     * @param listener
     */
    public void setOnTabReselectedListener(OnTabReselectedListener listener) {
        mListener = listener;
    }


    private void switchFragment(Fragment fragment, String tag) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        if (mFragment != fragment) {
            if (!fragment.isAdded()) {
                transaction.hide(mFragment);
                transaction.add(R.id.frame, fragment, tag);
                transaction.commit();
            } else {
                transaction.hide(mFragment);
                transaction.show(fragment);
                transaction.commit();
            }
            mFragment = fragment;
        }
    }


    @Override
    public void onBackPressed() {
        VideoTabFragment fragment = null;
        if (mVideoTabFragment instanceof VideoTabFragment) {
            fragment = (VideoTabFragment) mVideoTabFragment;
        }
        //如果当前视频是全屏模式->退出全屏
        if (fragment.onBackPressed()) {
            return;
        }
        //否则退出app
        finish();
    }

}
