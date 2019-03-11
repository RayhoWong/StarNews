package com.rayho.tsxiu.module_news;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.rxbus.RxBus;
import com.orhanobut.logger.Logger;
import com.rayho.tsxiu.R;
import com.rayho.tsxiu.activity.MainActivity;
import com.rayho.tsxiu.activity.TestActivity;
import com.rayho.tsxiu.base.Presenter;
import com.rayho.tsxiu.databinding.NewsFragmentBinding;
import com.rayho.tsxiu.module_news.dao.Channel;
import com.rayho.tsxiu.module_news.fragment.ContentFragment;
import com.rayho.tsxiu.module_news.viewmodel.NewsTabFtViewModel;
import com.rayho.tsxiu.ui.channelhelper.activity.ChannelActivity;
import com.rayho.tsxiu.ui.channelhelper.bean.ChannelBean;
import com.rayho.tsxiu.utils.DaoManager;
import com.trello.rxlifecycle3.components.support.RxFragment;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import io.reactivex.schedulers.Schedulers;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class NewsTabFragment extends RxFragment implements Presenter {

    public NewsFragmentBinding mBinding;

    private ContentFragment mFragment;//当前显示的fragment

    private MainActivity mActivity;//所依赖的activity

    public ContentAdapter mAdapter;

    private NewsTabFtViewModel mViewModel;

    public List<Fragment> mFragments;

    public List<Channel> mChannels;//本地缓存的频道列表


    public static NewsTabFragment newInstance() {
        return new NewsTabFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.news_fragment, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new NewsTabFtViewModel();
        mBinding.setPresenter(this);
        mBinding.setVm(mViewModel);
        mActivity = (MainActivity) getActivity();

        getLocalChannels();
    }


    private void initView() {
        mAdapter = new ContentAdapter(getChildFragmentManager());
        mBinding.viewpager.setAdapter(mAdapter);
        //除当前页面的预加载页面数(保证切换页面时 不会重新创建)
        mBinding.viewpager.setOffscreenPageLimit(mChannels.size() - 1);//缓存所有页面
        mBinding.tablayout.setupWithViewPager(mBinding.viewpager);
    }


    /**
     * 频道发生变化 重置viewpager
     */
    public void resetViewPager(){
        //除当前页面的预加载页面数(保证切换页面时 不会重新创建)
        mBinding.viewpager.setOffscreenPageLimit(mChannels.size() - 1);//缓存所有页面
        mBinding.tablayout.setupWithViewPager(mBinding.viewpager);
    }


    /**
     * 获取本地缓存的频道
     */
    private void getLocalChannels() {
        DaoManager.getInstance().getDaoSession().getChannelDao()
                .rx()
                .loadAll()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Channel>>() {
                    @Override
                    public void call(List<Channel> channels) {
                        mChannels = channels;
//                        Logger.d("长度："+mChannels.size());
                        if (mChannels.size() == 0) {
                            DaoManager.getInstance().getDaoSession().getChannelDao()
                                    .rx()
                                    .insertOrReplace(new Channel(null,getString(R.string.default_channel_name_1), getString(R.string.default_channel_cid_1)))
                                    .subscribe();
                            DaoManager.getInstance().getDaoSession().getChannelDao()
                                    .rx()
                                    .insertOrReplace(new Channel(null,getString(R.string.default_channel_name_2), getString(R.string.default_channel_cid_2)))
                                    .subscribe();
                            mChannels.add(new Channel(null, getString(R.string.default_channel_name_1),  getString(R.string.default_channel_cid_1)));
                            mChannels.add(new Channel(null, getString(R.string.default_channel_name_2),  getString(R.string.default_channel_cid_2)));
                        }
                        mFragments = new ArrayList<>();
                        for (int i = 0; i < mChannels.size(); i++) {
                            mFragments.add(ContentFragment.newInstance(mChannels.get(i).getCid()));
                        }
                        initView();
                    }
                });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_search:
                mActivity.startActivity(new Intent(mActivity, TestActivity.class));
                break;
            case R.id.ll_scan:
                break;
            case R.id.channel_menu:
                RxBus.getDefault().postSticky(this,"NewTabFt");
                mActivity.startActivity(new Intent(mActivity, ChannelActivity.class));
                break;
        }
    }


    /**
     * 更新首页新闻
     * 如果list有数据 删除数据库中所有频道记录 重新插入list里面的所有频道记录
     * @param list 全新的频道记录数据(必须跟数据库的不同 才插入数据库)
     */
    public void setContentFts(List<ChannelBean> list){
        if(list != null && list.size() > 0){
            mFragments.clear();
            mChannels.clear();
            mAdapter.notifyDataSetChanged();
            if (list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    mFragments.add(ContentFragment.newInstance(list.get(i).getCid()));
                    mChannels.add(new Channel(null, list.get(i).getTabName(), list.get(i).getCid()));
                }
                mAdapter.notifyDataSetChanged();
                //频道更新 重置vp
                resetViewPager();
              /*设置当前显示的fragment
                不过有bug(待解决问题)大几率会后台加载一个不显示的fragment(请求网络)
                由于接口不支持并发请求 会导致当前显示的fragment进行请求 返回网络异常
                mBinding.viewpager.setCurrentItem(mChannels.size() - 1,true);*/
                //更新数据库
                setLocalChannels();
            }
        }
    }


    /**
     * 删除数据库原有的频道记录 重新插入新的记录
     */
    private void setLocalChannels(){
        if(mChannels.size() > 0){
            //删除数据库中的所有频道记录
            DaoManager.getInstance().getDaoSession().getChannelDao()
                    .rx()
                    .deleteAll()
                    .subscribe();

            for(int i=0;i<mChannels.size();i++){
                DaoManager.getInstance().getDaoSession().getChannelDao()
                        .rx()
                        .insertOrReplace(mChannels.get(i))
                        .subscribe();
            }
        }
    }


    public class ContentAdapter extends FragmentStatePagerAdapter {
        public ContentAdapter(FragmentManager fm) {
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
            return mChannels.get(position).getName();
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

        /*在调用notifyDataSetChanged()方法后，随之会触发该方法，
        根据该方法返回的值来确定是否更新

		object对象为Fragment，具体是当前显示的Fragment和它的前一个以及后一个
        将它的返回值改为POSITION_NONE就好了（默认返回的是POSITION_UNCHANGED），
        那么调用notifyDataSetChanged()方法时，就会每次都去加载新的Fragment，而不是引用之前的*/
        @Override
        public int getItemPosition(@NonNull Object object) {
            return POSITION_NONE;// 返回发生改变，让系统重新加载
        }
    }
}
