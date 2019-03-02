package com.rayho.tsxiu.module_news;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rayho.tsxiu.R;
import com.rayho.tsxiu.activity.MainActivity;
import com.rayho.tsxiu.activity.TestActivity;
import com.rayho.tsxiu.base.Presenter;
import com.rayho.tsxiu.databinding.NewsFragmentBinding;
import com.rayho.tsxiu.module_news.fragment.ContentFragment;
import com.rayho.tsxiu.ui.channelhelper.activity.ChannelActivity;
import com.rayho.tsxiu.utils.NetworkUtils;
import com.rayho.tsxiu.utils.ToastUtil;
import com.trello.rxlifecycle2.components.support.RxFragment;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class NewsTabFragment extends RxFragment implements Presenter {

    private NewsFragmentBinding mBinding;

    private ContentFragment mFragment;//当前显示的fragment

    private MainActivity mActivity;//所依赖的activity

    private NewsTabViewModel mViewModel;

    private List<Fragment> fragments;

    private String[] titles;


    public static NewsTabFragment newInstance() {
        return new NewsTabFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater,R.layout.news_fragment,container,false);
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new NewsTabViewModel();
        mBinding.setPresenter(this);
        mBinding.setVm(mViewModel);
        mActivity = (MainActivity) getActivity();
        initView();
    }

    private void initView() {
        titles = new String[]{"社会", "科技", "娱乐", "体育", "文化", "视频", "金融"};
        fragments = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            fragments.add(ContentFragment.newInstance(titles[i]));
        }
        mBinding.viewpager.setAdapter(new ContentAdapter(getChildFragmentManager()));
        //除当前页面的预加载页面数(保证切换页面时 不会重新创建)
        mBinding.viewpager.setOffscreenPageLimit(titles.length - 1);//缓存所有页面
        mBinding.tablayout.setupWithViewPager(mBinding.viewpager);
    }


    private void getChannels(){
        //无网络
        if( ! NetworkUtils.isConnected(getActivity())){
            ToastUtil toast = new ToastUtil(getActivity(),"请检查网络,无法获取");
        }
        else {

        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_search:
                mActivity.startActivity(new Intent(mActivity,TestActivity.class));
                break;
            case R.id.ll_scan:
                break;
            case R.id.channel_menu:
                mActivity.startActivity(new Intent(mActivity, ChannelActivity.class));
                break;
        }
    }


    private class ContentAdapter extends FragmentPagerAdapter {
        public ContentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        /**
         * 滑动后当前显示的Fragment
         *
         * @param container
         * @param position
         * @param object    当前显示的Fragment实例
         */
        @Override
        public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            super.setPrimaryItem(container, position, object);
            //得到当前显示的fragment
            mFragment = (ContentFragment) object;
            mActivity.setOnTabReselectedListener(mFragment);
        }
    }


}
