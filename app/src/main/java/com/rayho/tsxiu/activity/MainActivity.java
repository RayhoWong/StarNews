package com.rayho.tsxiu.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.rayho.tsxiu.R;
import com.rayho.tsxiu.base.BaseActivity;
import com.rayho.tsxiu.base.listener.OnTabReselectedListener;
import com.rayho.tsxiu.module_mine.MineTabFragment;
import com.rayho.tsxiu.module_news.NewsTabFragment;
import com.rayho.tsxiu.module_photo.PhotoTabFragment;
import com.rayho.tsxiu.module_video.VideoTabFragment;
import com.rayho.tsxiu.ui.SkinBottomNavigationView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import butterknife.BindView;

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
        hideBaseToolbar();
        initBottomNavigationBar();
        initFragment();
    }


    @SuppressLint("MissingSuperCall")
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //如果用以下这种做法则不保存状态，再次进来的话会显示默认tab
        //总是执行这句代码来调用父类去保存视图层的状态
        //解决Fragment重叠的问题
        //super.onSaveInstanceState(outState);
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
