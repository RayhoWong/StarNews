package com.rayho.tsxiu.module_mine.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.blankj.utilcode.util.SPUtils;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;
import com.rayho.tsxiu.R;
import com.rayho.tsxiu.base.Constant;
import com.rayho.tsxiu.greendao.UserDao;
import com.rayho.tsxiu.module_mine.fragment.NewsCtFragment;
import com.rayho.tsxiu.module_mine.fragment.PhotosCtFragment;
import com.rayho.tsxiu.ui.SkinTabLayout;
import com.rayho.tsxiu.utils.DaoManager;
import com.rayho.tsxiu.utils.GlideUtils;
import com.rayho.tsxiu.utils.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import skin.support.design.widget.SkinMaterialCollapsingToolbarLayout;
import skin.support.widget.SkinCompatSupportable;


/**
 * 我的收藏
 */
public class MyCollectionActivity extends AppCompatActivity {

    @BindView(R.id.iv_bg)
    ImageView mIvBg;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.collapsing_toolbar_layout)
    SkinMaterialCollapsingToolbarLayout mCollapsingToolbarLayout;

    @BindView(R.id.tablayout)
    SkinTabLayout mTablayout;

    @BindView(R.id.appbar)
    AppBarLayout mAppbarLayout;

    @BindView(R.id.viewpager)
    ViewPager mViewPager;

    private List<Fragment> mFragments = new ArrayList<>();

    private List<String> mTitles = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_collection);

        ButterKnife.bind(this);
        initView();
    }


    private void initView() {
        StatusBarUtil.setStatusBarTranslucent(this);

        mCollapsingToolbarLayout.setTitle("我的收藏");
        mCollapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.white));
        //根据夜间模式的设置 设置状态栏字体的颜色
        if (SPUtils.getInstance(Constant.SP_SETTINGS).getBoolean(getString(R.string.sp_nightmode))) {
            //夜间模式true 黑色
            StatusBarUtil.setStatusBarTextColor(getResources().getColor(R.color.colorPrimaryDark_night), this);
            mCollapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.white));
        } else {
            //夜间模式false 白色
            StatusBarUtil.setStatusBarTextColor(getResources().getColor(R.color.colorPrimaryDark), this);
            mCollapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.black));
        }

        mToolbar.setNavigationIcon(R.mipmap.close_2);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        initViewPager();

        DaoManager.getInstance().getDaoSession().getUserDao()
                .queryBuilder()
                .where(UserDao.Properties.Uid.eq(Constant.DEFAULT_USER_ID))
                .rx()
                .unique()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(user -> {
                    if (user != null) {
                        GlideUtils.loadImage(MyCollectionActivity.this, user.getImgName(), mIvBg);
                    }
                });
    }


    private void initViewPager() {
        mTitles.add(getString(R.string.news));
        mTitles.add(getString(R.string.photos));
        mFragments.add(NewsCtFragment.newInstance());
        mFragments.add(PhotosCtFragment.newInstance());
        MyViewPagerAdapter adapter = new MyViewPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(adapter);
        mTablayout.setupWithViewPager(mViewPager);
    }


    private class MyViewPagerAdapter extends FragmentPagerAdapter {

        public MyViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles.get(position);
        }
    }
}
