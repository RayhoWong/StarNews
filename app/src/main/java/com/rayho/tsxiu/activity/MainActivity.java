package com.rayho.tsxiu.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.rayho.tsxiu.R;
import com.rayho.tsxiu.base.BaseActivity;
import com.rayho.tsxiu.base.listener.OnTabReselectedListener;
import com.rayho.tsxiu.module_mine.MineTabFragment;
import com.rayho.tsxiu.module_news.NewsTabFragment;
import com.rayho.tsxiu.module_news.fragment.ContentFragment;
import com.rayho.tsxiu.module_photo.PhotoTabFragment;
import com.rayho.tsxiu.module_video.VideoTabFragment;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import butterknife.BindView;
import timber.log.Timber;

public class MainActivity extends BaseActivity {

   /* @BindView(R.id.toolbarMain)
    Toolbar mToolBar;
    @BindView(R.id.container)
    CoordinatorLayout container;*/
    @BindView(R.id.bottom_navigation_bar)
    BottomNavigationBar mBottomNavigationBar;

    private Fragment mFragment;//当前显示的fragment
    private Fragment newsTabFragment;
    private Fragment photoTabFragment;
    private Fragment mineTabFragment;
    private Fragment videoTabFragment;

    private String[] titles;
    private int lastSelectedPosition = 0;

    private OnTabReselectedListener listener;


    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }


    @Override
    public void afterSetContentView() {
        initToolbar();
        initBottomNavigationBar();
        initFragment();

        listener = new ContentFragment();
    }



    private void initToolbar() {
        hideBaseToolbar();
       /* mToolBar.setNavigationIcon(R.mipmap.arrow_back);
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
        });*/
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
        newsTabFragment = NewsTabFragment.newInstance();
        photoTabFragment = PhotoTabFragment.newInstance();
        videoTabFragment = VideoTabFragment.newInstance();
        mineTabFragment = MineTabFragment.newInstance();
        //默认显示新闻首页
        transaction.add(R.id.frame, newsTabFragment);
        transaction.commit();
        mFragment = newsTabFragment;
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

                        switchFragment(newsTabFragment,"news");
                        break;
                    case 1:
                        switchFragment(photoTabFragment,"photo");
                        break;
                    case 2:
                        switchFragment(videoTabFragment,"video");
                        break;
                    case 3:
                        switchFragment(mineTabFragment,"mine");
                        break;
                }
            }

            @Override
            public void onTabUnselected(int position) {

            }

            /**
             * 再次点击当前选中的Tab时，当前显示Fragment重新加载数据
             * @param position
             */
            @Override
            public void onTabReselected(int position) {
                if(position == 0){
                    Timber.tag("MAINMAIN");
                    Timber.d("NesTabFragment is called.............");
                    listener.updateData();
                }

            }
        });
    }

    /**
     * 获取当前显示的ContentFragment实例
     * @param listener
     */
    public void setOnTabReselectedListener(OnTabReselectedListener listener){
        this.listener = listener;
    }


    private void switchFragment(Fragment fragment,String tag){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        if(mFragment != fragment){
            if(!fragment.isAdded()){
                transaction.hide(mFragment);
                transaction.add(R.id.frame,fragment,tag);
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
