package com.rayho.tsxiu.module_news;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blankj.rxbus.RxBus;
import com.orhanobut.logger.Logger;
import com.rayho.tsxiu.R;
import com.rayho.tsxiu.activity.MainActivity;
import com.rayho.tsxiu.activity.TestActivity;
import com.rayho.tsxiu.base.Presenter;
import com.rayho.tsxiu.databinding.NewsFragmentBinding;
import com.rayho.tsxiu.http.api.NetObserver;
import com.rayho.tsxiu.http.exception.ApiException;
import com.rayho.tsxiu.module_news.activity.ScannerResultActivity;
import com.rayho.tsxiu.module_news.activity.SearchActivity;
import com.rayho.tsxiu.module_news.bean.NewsHotSearch;
import com.rayho.tsxiu.module_news.dao.Channel;
import com.rayho.tsxiu.module_news.fragment.ContentFragment;
import com.rayho.tsxiu.module_news.viewmodel.NewsTabViewModel;
import com.rayho.tsxiu.ui.channelhelper.activity.ChannelActivity;
import com.rayho.tsxiu.ui.channelhelper.bean.ChannelBean;
import com.rayho.tsxiu.utils.DaoManager;
import com.rayho.tsxiu.utils.PermissionSettingPage;
import com.rayho.tsxiu.utils.ToastUtil;
import com.sunfusheng.marqueeview.MarqueeView;
import com.trello.rxlifecycle3.components.support.RxFragment;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.bean.ZxingConfig;
import com.yzq.zxinglibrary.common.Constant;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import io.reactivex.Observable;
import io.reactivex.functions.BiFunction;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

import static android.app.Activity.RESULT_OK;
import static com.rayho.tsxiu.base.Constant.REQUEST_CODE_SCAN;

/**
 * 新闻模块
 */
public class NewsTabFragment extends RxFragment implements Presenter {

    public NewsFragmentBinding mBinding;

    private ContentFragment mFragment;//当前显示的fragment

    private MainActivity mActivity;//所依赖的activity

    public ContentAdapter mAdapter;

    private NewsTabViewModel mViewModel;

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
        mViewModel = new NewsTabViewModel();
        mBinding.setPresenter(this);
        mBinding.setVm(mViewModel);
        mActivity = (MainActivity) getActivity();

        getLocalChannels();
    }


    /**
     * 返回MainActivity更新热搜词条
     */
    @Override
    public void onResume() {
        super.onResume();
        getHotSearchList();
    }


    /**
     * 获取热搜的新闻词条
     */
    private void getHotSearchList() {
        mViewModel.getHotSearch()
                .compose(this.<NewsHotSearch>bindToLifecycle())
                .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
                .subscribe(new NetObserver<NewsHotSearch>() {
                    @Override
                    public void onNext(NewsHotSearch newsHotSearch) {
                        if (newsHotSearch != null) {
                            if (newsHotSearch.data.suggest_words != null && newsHotSearch.data.suggest_words.size() > 0) {
                                List<String> words = new ArrayList<>();
                                for (NewsHotSearch.DataBean.SuggestWordsBean bean : newsHotSearch.data.suggest_words) {
                                    words.add(bean.word);
                                }
                                mBinding.marqueeView.stopFlipping();
                                mBinding.marqueeView.startWithList(words);
                                mBinding.marqueeView.setOnItemClickListener(new MarqueeView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(int position, TextView textView) {
                                        mActivity.startActivity(new Intent(mActivity, SearchActivity.class));
                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onError(ApiException ex) {
                        Logger.d("错误信息：" + ex.getDisplayMessage());
//                        ToastUtil toast = new ToastUtil(mActivity, ex.getDisplayMessage());
//                        toast.show();
                    }
                });
    }


    /**
     * 这只是一个zip使用的例子 并不实际调用
     * 使用zip操作符合并两个或以上网络请求(传入两个或以上的Observable)返回的数据
     * zip将合并多个数据源转换成所需要的数据
     * (多个Observable合并成一个Observable,并且可以对数据源进行加工)
     */
    private void zipSample() {
//        Observable<NewsHotSearch> observable1 = mViewModel.getHotSearch();
//        Observable<NewsHotSearch> observable2 = mViewModel.getHotSearch();
//        Observable
//                .zip(observable1, observable2, new BiFunction<NewsHotSearch, NewsHotSearch, List<String>>() {
//                    @Override
//                    public List<String> apply(NewsHotSearch newsHotSearch, NewsHotSearch newsHotSearch2) throws Exception {
//                        List<NewsHotSearch.DataBean.SuggestWordsBean> list = new ArrayList<>();
//                        List<String> words = new ArrayList<>();
//                        if (newsHotSearch.data.suggest_words != null && newsHotSearch2.data.suggest_words != null) {
//                            list.addAll(newsHotSearch.data.suggest_words);
//                            list.addAll(newsHotSearch2.data.suggest_words);
//                        }
//                        for (NewsHotSearch.DataBean.SuggestWordsBean bean : list) {
//                            words.add(bean.word);
//                        }
//                        return words;
//                    }
//                })
//                .compose(this.<List<String>>bindToLifecycle())
//                .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
//                .subscribe(new NetObserver<List<String>>() {
//                    @Override
//                    public void onNext(List<String> words) {
//                        if (words != null && words.size() > 0) {
//                            mBinding.marqueeView.startWithList(words);
//                            mBinding.marqueeView.setOnItemClickListener(new MarqueeView.OnItemClickListener() {
//                                @Override
//                                public void onItemClick(int position, TextView textView) {
//                                    mActivity.startActivity(new Intent(mActivity, TestActivity.class));
//                                }
//                            });
//                        }
//                    }
//
//                    @Override
//                    public void onError(ApiException ex) {
////                        Logger.d("错误信息："+ex.getDisplayMessage());
//                        ToastUtil toast = new ToastUtil(mActivity, ex.getDisplayMessage());
//                        toast.show();
//                    }
//                });
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
    public void resetViewPager() {
        //除当前页面的预加载页面数(保证切换页面时 不会重新创建)
        mBinding.viewpager.setOffscreenPageLimit(mChannels.size() - 1);//缓存所有页面
        mBinding.tablayout.setupWithViewPager(mBinding.viewpager);
    }


    /**
     * 申请权限
     * 已获取的权限 不必申请
     */
    private void checkCameraPermission() {
        AndPermission.with(this)
                .runtime()
                .permission(
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        startScanning();
                    }
                })
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        //判断用户是不是选中不再显示权限弹窗了，若不再显示的话提醒进入权限设置页
                        if (AndPermission.hasAlwaysDeniedPermission(mActivity, data)) {
                            //提醒打开权限设置页
                            ToastUtil toast = new ToastUtil(mActivity, getString(R.string.permission_request_tip));
                            toast.show();
                            PermissionSettingPage.start(mActivity, false);
                        } else {
                            ToastUtil toast = new ToastUtil(mActivity, "你拒绝了该权限");
                            toast.show();
                        }
                    }
                }).start();
    }


    /**
     * 删除数据库原有的频道记录 重新插入新的记录
     */
    private void setLocalChannels() {
        if (mChannels.size() > 0) {
            //删除数据库中的所有频道记录
            DaoManager.getInstance().getDaoSession().getChannelDao()
                    .rx()
                    .deleteAll()
                    .subscribe(new Action1<Void>() {
                        @Override
                        public void call(Void aVoid) {
                            //插入新的频道
                            DaoManager.getInstance().getDaoSession().getChannelDao()
                                    .rx()
                                    .insertInTx(mChannels)
                                    .subscribe();
                        }
                    });
        }
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
                            //如果没有默认频道 添加两个默认频道(社会和娱乐)
                            DaoManager.getInstance().getDaoSession().getChannelDao()
                                    .rx()
                                    .insertOrReplace(new Channel(null, getString(R.string.default_channel_name_1), getString(R.string.default_channel_cid_1)))
                                    .subscribe();
                            DaoManager.getInstance().getDaoSession().getChannelDao()
                                    .rx()
                                    .insertOrReplace(new Channel(null, getString(R.string.default_channel_name_2), getString(R.string.default_channel_cid_2)))
                                    .subscribe();
                            mChannels.add(new Channel(null, getString(R.string.default_channel_name_1), getString(R.string.default_channel_cid_1)));
                            mChannels.add(new Channel(null, getString(R.string.default_channel_name_2), getString(R.string.default_channel_cid_2)));
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
            case R.id.ll_search://搜索
                mActivity.startActivity(new Intent(mActivity, SearchActivity.class));
                break;
            case R.id.ll_scan://扫描
                checkCameraPermission();
                break;
            case R.id.channel_menu://打开频道管理器
                RxBus.getDefault().postSticky(this, "NewTabFt");
                mActivity.startActivity(new Intent(mActivity, ChannelActivity.class));
                break;
        }
    }


    /**
     * 开启二维码扫描
     */
    private void startScanning() {
        Intent intent = new Intent(getActivity(), CaptureActivity.class);
        //扫描界面的配置
        ZxingConfig config = new ZxingConfig();
        config.setPlayBeep(false);//是否播放扫描声音 默认为true
        config.setShake(true);//是否震动  默认为true
        config.setFullScreenScan(false);//是否全屏扫描  默认为true  设为false则只会在扫描框中扫描
        config.setShowbottomLayout(true);//是否显示相册和闪光灯布局
        config.setDecodeBarCode(false);//是否扫描条形码 默认为true
        config.setReactColor(R.color.white);//设置扫描框四个角的颜色 默认为白色
        config.setFrameLineColor(R.color.white);//设置扫描框边框颜色 默认无色
        config.setScanLineColor(R.color.transparent);//设置扫描线的颜色 默认白色
        intent.putExtra(Constant.INTENT_ZXING_CONFIG, config);
        startActivityForResult(intent, REQUEST_CODE_SCAN);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {
                String result = data.getStringExtra(Constant.CODED_CONTENT);
//                ToastUtil toast = new ToastUtil(getActivity(), "扫描结果为：" + result);
//                toast.show();
                Intent intent = new Intent(mActivity, ScannerResultActivity.class);
                intent.putExtra("url", result);
                startActivity(intent);
            }
        }
    }


    /**
     * 更新首页新闻
     * 如果list有数据 删除数据库中所有频道记录 重新插入list里面的所有频道记录
     *
     * @param list 全新的频道记录数据(必须跟数据库的不同 才插入数据库)
     */
    public void setContentFts(List<ChannelBean> list) {
        if (list != null && list.size() > 0) {
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
