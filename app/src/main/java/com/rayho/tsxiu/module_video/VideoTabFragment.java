package com.rayho.tsxiu.module_video;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.rxbus.RxBus;
import com.google.android.material.button.MaterialButton;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.orhanobut.logger.Logger;
import com.rayho.tsxiu.R;
import com.rayho.tsxiu.base.Constant;
import com.rayho.tsxiu.databinding.VideoFragmentBinding;
import com.rayho.tsxiu.http.api.NetObserver;
import com.rayho.tsxiu.http.exception.ApiException;
import com.rayho.tsxiu.module_video.adapter.VideoAdapter;
import com.rayho.tsxiu.module_video.bean.VideoBean;
import com.rayho.tsxiu.module_video.viewmodel.VideoTabViewModel;
import com.rayho.tsxiu.ui.refreshlayout.MyRefreshLottieFooter;
import com.rayho.tsxiu.ui.refreshlayout.MyRefreshLottieHeader;
import com.rayho.tsxiu.ui.refreshlayout.MyRefreshLottieHeader2;
import com.rayho.tsxiu.utils.NetworkUtils;
import com.rayho.tsxiu.utils.RxTimer;
import com.rayho.tsxiu.utils.ScrollCalculatorHelper;
import com.rayho.tsxiu.utils.ToastUtil;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.utils.CommonUtil;
import com.trello.rxlifecycle3.components.support.RxFragment;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 视频模块
 */
public class VideoTabFragment extends RxFragment {

    private Activity mActivity;

    private VideoFragmentBinding mBinding;

    private MyRefreshLottieHeader2 mHeader;

    private MyRefreshLottieFooter mFooter;

    private View mViewStub;

    private MaterialButton mBtReload;

    private VideoTabViewModel mViewModel;

    private VideoAdapter mAdapter;

    private int updateNums;//更新成功 返回的数据数量

    private ScrollCalculatorHelper mScrollCalculatorHelper;//自动播放帮助类

    private LinearLayoutManager mLinearLayoutManager;

    private boolean mFull = false;//视频全屏标记

    private int start = 0;//上拉加载自增标记

    private List<VideoBean.DataListBean.ContListBean> mData = new ArrayList<>();


    public static VideoTabFragment newInstance() {
        return new VideoTabFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        mActivity = getActivity();
        // 设置一个exit transition 要在activity的setContentView前调用才生效
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            mActivity.getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
//            mActivity.getWindow().setEnterTransition(new Explode());
//            mActivity.getWindow().setExitTransition(new Explode());
//        }
        super.onCreate(savedInstanceState);
        getUpdateNums();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.video_fragment, container, false);
        mViewStub = mBinding.viewStub.getViewStub();
        return mBinding.getRoot();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initRefreshLayout();
        initVideoPlayer();
        getData();
    }


    /**
     * 获取更新数据的数量
     */
    private void getUpdateNums() {
        RxBus.getDefault().subscribeSticky(this, "updateNums", new RxBus.Callback<Integer>() {
            @Override
            public void onEvent(Integer integer) {
                updateNums = integer;
            }
        });
    }


    private void initRefreshLayout() {
        mHeader = new MyRefreshLottieHeader2(mActivity);
        mFooter = new MyRefreshLottieFooter(mActivity);
        mBinding.twiRefreshlayout.setHeaderHeight(60);
        mBinding.twiRefreshlayout.setBottomHeight(45);
        mBinding.twiRefreshlayout.setHeaderView(mHeader);
        mBinding.twiRefreshlayout.setBottomView(mFooter);
        mBinding.twiRefreshlayout.setEnableRefresh(true);
        mBinding.twiRefreshlayout.setEnableLoadmore(true);
        mBinding.twiRefreshlayout.setEnableOverScroll(true);
        mBinding.twiRefreshlayout.startRefresh();//自动刷新
        mBinding.twiRefreshlayout.setAutoLoadMore(true); //自动加载
    }


    /**
     * 初始化播放器
     */
    private void initVideoPlayer() {
        //限定范围为屏幕一半的上下偏移180
        int playTop = CommonUtil.getScreenHeight(mActivity) / 2 + CommonUtil.dip2px(mActivity, 130);
        int playBottom = CommonUtil.getScreenHeight(mActivity) / 2 - CommonUtil.dip2px(mActivity, 130);
        //自定播放帮助类
        mAdapter = new VideoAdapter(mActivity);
        mScrollCalculatorHelper = new ScrollCalculatorHelper(R.id.video_item_player, playTop, playBottom,mAdapter);
        mLinearLayoutManager = new LinearLayoutManager(mActivity);
        mBinding.rcv.setLayoutManager(mLinearLayoutManager);
        mViewModel = new VideoTabViewModel(mAdapter);

        //监听rcv的滑动事件
        mBinding.rcv.addOnScrollListener(new RecyclerView.OnScrollListener() {

            int firstVisibleItem, lastVisibleItem;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                mScrollCalculatorHelper.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();
                lastVisibleItem = mLinearLayoutManager.findLastVisibleItemPosition();

                //这是滑动自动播放的代码
                if (!mFull) {
                    mScrollCalculatorHelper.onScroll(recyclerView, firstVisibleItem, lastVisibleItem, lastVisibleItem - firstVisibleItem);
                }
            }
        });
    }


    /**
     * 1.RefreshLayout设置监听器
     * 2.加载数据
     */
    private void getData() {
        mBinding.twiRefreshlayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                super.onRefresh(refreshLayout);
                if (!NetworkUtils.isConnected(mActivity)) {
                    refreshLayout.setEnableLoadmore(false);
                    refreshLayout.setEnableOverScroll(false);
                    //无网络 显示错误布局
                    RxTimer.timer(1500, new RxTimer.RxAction() {
                        @Override
                        public void action() {
                            isShowNetWorkErrorLayout(true);
                            mHeader.setMsg(getString(R.string.network_error));
                            refreshLayout.finishRefreshing();
                        }
                    });

                } else {
                    //隐藏网络重连布局
                    if (mViewStub.getVisibility() == View.VISIBLE) {
                        mViewStub.setVisibility(View.GONE);
                    }
                    refreshLayout.setEnableLoadmore(true);
                    refreshLayout.setEnableOverScroll(true);
                    //下拉刷新获取数据
                    getVideosByRefresh();
                }
            }

            @Override
            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);
                if (!NetworkUtils.isConnected(mActivity)) {
                    //无网络
                    RxTimer.timer(1500, new RxTimer.RxAction() {
                        @Override
                        public void action() {
                            ToastUtil util = new ToastUtil(mActivity, getString(R.string.network_error_tips2));
                            util.show();
                            refreshLayout.finishLoadmore();
                            refreshLayout.setEnableLoadmore(false);
                            refreshLayout.setEnableOverScroll(false);
                        }
                    });
                } else {
                    //有网络 上拉加载更多获取数据
                    getVideosByLoadMore();
                }
            }
        });
    }


    /**
     * 显示网络错误布局 支持重连
     *
     * @param flag 是显示布局 true:显示  false:隐藏
     */
    private void isShowNetWorkErrorLayout(boolean flag) {
        if (flag) {
            if (mData.size() > 0) {
                //已显示图片列表 不显示错误布局
                return;
            }
            //无网络 显示布局
            /* 当mViewStub.setVisibility(View.VISIBLE)后 得到加载进来的布局
             * 然后通过根布局ll_root.findViewById()找到已加载布局的子控件view
             * 该做法避免找不到控件的空指针异常
             * 如果直接 布局子控件 = mViewStub.findViewById 报空指针异常*/
            mViewStub.setVisibility(View.VISIBLE);//或者mViewStub.inflate();
            mBtReload = mBinding.container.findViewById(R.id.bt_reload);
            mBtReload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mBinding.twiRefreshlayout.startRefresh();
                }
            });

        } else {
            //有网络 隐藏布局
            if (mViewStub.getVisibility() == View.VISIBLE) {
                mViewStub.setVisibility(View.GONE);
            }
        }
    }


    /**
     * 通过下拉刷新获取
     */
    private void getVideosByRefresh() {
        //每次刷新重置分页值=0
        start = 0;
        mViewModel
                .getVideosObservable("10")
                //自动在onDestroy中取消订阅 避免内存泄漏  一定要在subscribeOn方法之后调用
                .compose(this.<VideoBean>bindToLifecycle())
                .subscribe(new NetObserver<VideoBean>() {
                    @Override
                    public void onNext(VideoBean videoBean) {
                        //请求成功"resultCode": "1","resultMsg": "success"
                        if (videoBean.resultCode.equals("1") && videoBean.resultMsg.equals("success")) {
                            if (videoBean.dataList != null && videoBean.dataList.size() > 0) {
                                List<VideoBean.DataListBean> datas = new ArrayList<>();
                                for (VideoBean.DataListBean bean : videoBean.dataList) {
                                    //nodeType=13才返回视频数据
                                    if (bean.nodeType.equals("13")) {
                                        datas.add(bean);
                                    }
                                }
                                mData.clear();
                                for (VideoBean.DataListBean bean : datas) {
                                    if (bean.contList != null && bean.contList.size() > 0) {
                                        mData.addAll(bean.contList);
                                    }
                                }
                                mViewModel.setVideos(mData, Constant.REFRESH_DATA);
                                mBinding.rcv.setAdapter(mAdapter);
                                mHeader.setMsg("星头条推荐引擎有" + String.valueOf(updateNums) + "条更新");
                                //如果更新的数据少于3条 禁止上拉加载
                                if (mData.size() < 3) {
                                    mBinding.twiRefreshlayout.setEnableLoadmore(false);
                                    mBinding.twiRefreshlayout.setEnableOverScroll(false);//禁止回弹
                                } else {
                                    mBinding.twiRefreshlayout.setEnableLoadmore(true);
                                    mBinding.twiRefreshlayout.setEnableOverScroll(true);//允许回弹
                                }
                                //禁止header的越界回弹 显示控件
                                mBinding.twiRefreshlayout.setOverScrollRefreshShow(false);
                                mBinding.twiRefreshlayout.finishRefreshing();
                            }

                        } else {
                            mHeader.setMsg(videoBean.resultMsg);
                            ToastUtil toastUtil = new ToastUtil(mActivity, videoBean.resultMsg);
                            toastUtil.show();
                            mBinding.twiRefreshlayout.finishRefreshing();
                        }
                    }

                    @Override
                    public void onError(ApiException ex) {
                        mHeader.setMsg(ex.getDisplayMessage());
                        ToastUtil toastUtil = new ToastUtil(mActivity, ex.getDisplayMessage());
                        toastUtil.show();
                        Logger.e(ex.getDisplayMessage());
                        mBinding.twiRefreshlayout.finishRefreshing();
                    }
                });
    }


    /**
     * 通过上拉加载更多 获取数据
     */
    private void getVideosByLoadMore() {
        start += 10;
        mViewModel
                .getVideosObservable(String.valueOf(start))
                //自动在onDestroy中取消订阅 避免内存泄漏  一定要在subscribeOn方法之后调用
                .compose(this.<VideoBean>bindToLifecycle())
                .subscribe(new NetObserver<VideoBean>() {
                    @Override
                    public void onNext(VideoBean videoBean) {
                        //请求成功"resultCode": "1","resultMsg": "success"
                        if (videoBean.resultCode.equals("1") && videoBean.resultMsg.equals("success")) {
                            if (videoBean.dataList != null && videoBean.dataList.size() > 0) {
                                List<VideoBean.DataListBean> datas = new ArrayList<>();
                                for (VideoBean.DataListBean bean : videoBean.dataList) {
                                    //nodeType=13才返回视频数据
                                    if (bean.nodeType.equals("13")) {
                                        datas.add(bean);
                                    }
                                }
                                mData.clear();
                                for (VideoBean.DataListBean bean : datas) {
                                    if (bean.contList != null && bean.contList.size() > 0) {
                                        mData.addAll(bean.contList);
                                    }
                                }
                                mViewModel.setVideos(mData, Constant.LOAD_MORE_DATA);
                                mBinding.twiRefreshlayout.finishLoadmore();
                            }
                        } else {
                            ToastUtil toastUtil = new ToastUtil(mActivity, videoBean.resultMsg);
                            toastUtil.show();
                            mBinding.twiRefreshlayout.finishLoadmore();
                        }
                    }

                    @Override
                    public void onError(ApiException ex) {
                        ToastUtil toastUtil = new ToastUtil(mActivity, ex.getDisplayMessage());
                        toastUtil.show();
                        mBinding.twiRefreshlayout.finishLoadmore();
                    }
                });
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //如果旋转了就全屏
        if (newConfig.orientation != ActivityInfo.SCREEN_ORIENTATION_USER) {
            mFull = false;
        } else {
            mFull = true;
        }
    }


    /*  按返回键
        如果当前是全屏模式true -> 退出全屏
        不是全屏false -> 退出app*/
    public boolean onBackPressed() {
        if(mActivity == null){
            return false;
        }
        if (GSYVideoManager.backFromWindowFull(mActivity)) {
            return true;
        }else {
            return false;
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        GSYVideoManager.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        GSYVideoManager.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        GSYVideoManager.releaseAllVideos();
        RxBus.getDefault().unregister(this);
    }

}
