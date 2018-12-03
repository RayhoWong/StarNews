package com.rayho.tsxiu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.rayho.tsxiu.R;
import com.rayho.tsxiu.base.BaseActivity;
import com.rayho.tsxiu.fragment.MineFragment;
import com.rayho.tsxiu.fragment.NewsFragment;
import com.rayho.tsxiu.fragment.PhotoFragment;
import com.rayho.tsxiu.fragment.VideoFragment;
import com.rayho.tsxiu.utils.SnackbarUtil;
import com.rayho.tsxiu.utils.ToastUtil;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    @BindView(R.id.toolbarMain)
    Toolbar mToolBar;
    @BindView(R.id.container)
    RelativeLayout container;
    @BindView(R.id.bottom_navigation_bar)
    BottomNavigationBar mBottomNavigationBar;

    Fragment mFragment;//当前显示的fragment
    NewsFragment newsFragment;
    PhotoFragment photoFragment;
    MineFragment mineFragment;
    VideoFragment videoFragment;

    String[] titles;
    int lastSelectedPosition = 0;


    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void afterSetContentView() {
        initToolbar();
        initBottomNavigationBar();
        initFragment();
    }

    //解决Fragment重叠的问题
    protected void onSaveInstanceState(Bundle outState) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.remove(newsFragment);
        transaction.remove(photoFragment);
        transaction.remove(videoFragment);
        transaction.remove(mineFragment);
        transaction.commitAllowingStateLoss();
        super.onSaveInstanceState(outState);
    }

    private void initToolbar() {
        hideBaseToolbar();
        mToolBar.setNavigationIcon(R.mipmap.arrow_back);
        mToolBar.setTitle("首页");
        mToolBar.setLogo(R.mipmap.ic_launcher);
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "haha", Toast.LENGTH_SHORT).show();
            }
        });
        mToolBar.inflateMenu(R.menu.setting_menu);
        mToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.search:
                        Intent intent = new Intent(MainActivity.this, TestActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.favor:
                        SnackbarUtil.showSnackbarColorAction(MainActivity.this, container,
                                "ni hao a", "press", R.color.colorAccent, 2000,
                                R.color.colorPrimary, R.color.colorPrimary,
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        ToastUtil toastUtil = new ToastUtil(MainActivity.this, "testtest");
                                        toastUtil.show();
//                                       ActivityUtils.startActivity(TestActivity.class);
                                    }
                                });
                        break;
                    case R.id.like:
                        break;
                }
                return false;
            }
        });
    }

    private void initFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        newsFragment = NewsFragment.newInstance();
        photoFragment = PhotoFragment.newInstance();
        videoFragment = VideoFragment.newInstance();
        mineFragment = MineFragment.newInstance();
        //默认显示新闻首页
        transaction.add(R.id.frame, newsFragment);
        transaction.commit();
        mFragment = newsFragment;
    }

    private void initBottomNavigationBar(){
        titles = getResources().getStringArray(R.array.BottomNavigationNames);

        mBottomNavigationBar.setActiveColor(R.color.colorAccent)
                            .setInActiveColor(R.color.grey)
                            .setBarBackgroundColor(R.color.colorPrimary)
                            .setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC)
                            .setMode(BottomNavigationBar.MODE_FIXED);

        mBottomNavigationBar.addItem(new BottomNavigationItem(R.mipmap.news,titles[0]))
                            .addItem(new BottomNavigationItem(R.mipmap.photo,titles[1]))
                            .addItem(new BottomNavigationItem(R.mipmap.video,titles[2]))
                            .addItem(new BottomNavigationItem(R.mipmap.my,titles[3]))
                            .setFirstSelectedPosition(lastSelectedPosition)//默认显示首页
                            .initialise();

        mBottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                switch (position){
                    case 0:
                        switchFragment(newsFragment);
                        break;
                    case 1:
                        switchFragment(photoFragment);
                        break;
                    case 2:
                        switchFragment(videoFragment);
                        break;
                    case 3:
                        switchFragment(mineFragment);
                        break;
                }
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {

            }
        });
    }

    private void switchFragment(Fragment fragment){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        if(mFragment != fragment){
            if(!fragment.isAdded()){
                transaction.hide(mFragment);
                transaction.add(R.id.frame,fragment);
                transaction.commit();
            }else {
                transaction.hide(mFragment);
                transaction.show(fragment);
                transaction.commit();
            }
            mFragment = fragment;
        }
    }

}
