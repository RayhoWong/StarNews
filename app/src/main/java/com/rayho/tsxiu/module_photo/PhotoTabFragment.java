package com.rayho.tsxiu.module_photo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.rxbus.RxBus;
import com.google.android.material.button.MaterialButton;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.rayho.tsxiu.R;
import com.rayho.tsxiu.activity.MainActivity;
import com.rayho.tsxiu.base.Constant;
import com.rayho.tsxiu.databinding.PhotoFragmentBinding;
import com.rayho.tsxiu.http.api.NetObserver;
import com.rayho.tsxiu.http.exception.ApiException;
import com.rayho.tsxiu.module_photo.adapter.PhotoAdapter;
import com.rayho.tsxiu.module_photo.bean.PhotoBean;
import com.rayho.tsxiu.module_photo.viewmodel.PhotoTabViewModel;
import com.rayho.tsxiu.ui.refreshlayout.MyRefreshLottieFooter;
import com.rayho.tsxiu.ui.refreshlayout.MyRefreshLottieHeader;
import com.rayho.tsxiu.utils.NetworkUtils;
import com.rayho.tsxiu.utils.RxTimer;
import com.rayho.tsxiu.utils.ToastUtil;
import com.trello.rxlifecycle3.components.support.RxFragment;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

public class PhotoTabFragment extends RxFragment {

    private View mViewStub;

    private MaterialButton mBtReload;

    private PhotoFragmentBinding mBinding;

    private MyRefreshLottieHeader mHeader;

    private MyRefreshLottieFooter mFooter;

    private PhotoTabViewModel mViewModel;

    private PhotoAdapter mAdapter;

    private List<PhotoBean.FeedListBean> mData = new ArrayList<>();

    private int updateNums;//更新成功 返回的数据数量

    private MainActivity mMainActivity;


    public static PhotoTabFragment newInstance() {
        return new PhotoTabFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getUpdateNums();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.photo_fragment, container, false);
        mViewStub = mBinding.viewStub.getViewStub();
        return mBinding.getRoot();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMainActivity = (MainActivity) getActivity();
        initRefreshLayout();
        initNewsRcv();
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


    private void initNewsRcv() {
        mBinding.rcv.setLayoutManager(new LinearLayoutManager(mMainActivity));
        mAdapter = new PhotoAdapter();
        mViewModel = new PhotoTabViewModel(mAdapter);
    }


    private void initRefreshLayout() {
        mHeader = new MyRefreshLottieHeader(mMainActivity);
        mFooter = new MyRefreshLottieFooter(mMainActivity);
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
     * 1.RefreshLayout设置监听器
     * 2.加载数据
     */
    private void getData() {
        mBinding.twiRefreshlayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                super.onRefresh(refreshLayout);
                if (!NetworkUtils.isConnected(mMainActivity)) {
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
                    getPhotosByRefresh();
                }
            }

            @Override
            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);
                if (!NetworkUtils.isConnected(mMainActivity)) {
                    //无网络
                    RxTimer.timer(1500, new RxTimer.RxAction() {
                        @Override
                        public void action() {
                            ToastUtil util = new ToastUtil(mMainActivity, getString(R.string.network_error_tips2));
                            util.show();
                            refreshLayout.finishLoadmore();
                            refreshLayout.setEnableLoadmore(false);
                            refreshLayout.setEnableOverScroll(false);
                        }
                    });
                } else {
                    //有网络 上拉加载更多获取数据
                    getPhotosByLoadMore();
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
                //已显示新闻列表 不显示错误布局
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
     * 通过下拉刷新获取图片
     */
    private void getPhotosByRefresh() {
        mViewModel
                .getPhotosObservable()
                //自动在onDestroy中取消订阅 避免内存泄漏  一定要在subscribeOn方法之后调用
                .compose(this.<PhotoBean>bindToLifecycle())
                .subscribe(new NetObserver<PhotoBean>() {
                    @Override
                    public void onNext(PhotoBean photoBean) {
                        //请求成功 result:SUCCESS
                        if (photoBean.result.equals("SUCCESS")) {
                            if (photoBean.feedList != null && photoBean.feedList.size() > 0) {
                                //设置图片显示的数量必须 <= MAX_PHOTO_NUMS
                                if (photoBean.feedList.size() >= Constant.MAX_PHOTO_NUMS) {
                                    mData.clear();
                                    for (int i = 0; i < Constant.MAX_PHOTO_NUMS; i++) {
                                        mData.add(photoBean.feedList.get(i));
                                    }
                                } else {
                                    mData = photoBean.feedList;
                                }
                                mViewModel.setPhotos(mData, Constant.REFRESH_DATA);
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
                            mHeader.setMsg(photoBean.result);
                            ToastUtil toastUtil = new ToastUtil(mMainActivity, photoBean.result);
                            toastUtil.show();
                            mBinding.twiRefreshlayout.finishRefreshing();
                        }
                    }

                    @Override
                    public void onError(ApiException ex) {
                        mHeader.setMsg(ex.getDisplayMessage());
                        ToastUtil toastUtil = new ToastUtil(mMainActivity, ex.getDisplayMessage());
                        toastUtil.show();
                        mBinding.twiRefreshlayout.finishRefreshing();
                    }
                });
    }


    /**
     * 通过上拉加载更多 获取图片
     */
    private void getPhotosByLoadMore() {
        mViewModel
                .getPhotosObservable()
                //自动在onDestroy中取消订阅 避免内存泄漏  一定要在subscribeOn方法之后调用
                .compose(this.<PhotoBean>bindToLifecycle())
                .subscribe(new NetObserver<PhotoBean>() {
                    @Override
                    public void onNext(PhotoBean photoBean) {
                        //请求成功 result:SUCCESS
                        if (photoBean.result.equals("SUCCESS")) {
                            if (photoBean.feedList != null && photoBean.feedList.size() > 0) {
                                //设置图片显示的数量必须 <= MAX_PHOTO_NUMS
                                if (photoBean.feedList.size() >= Constant.MAX_PHOTO_NUMS) {
                                    mData.clear();
                                    for (int i = 0; i < Constant.MAX_PHOTO_NUMS; i++) {
                                        mData.add(photoBean.feedList.get(i));
                                    }
                                } else {
                                    mData = photoBean.feedList;
                                }
                                mViewModel.setPhotos(mData, Constant.LOAD_MORE_DATA);
                                mBinding.twiRefreshlayout.finishLoadmore();
                            }
                        } else {
                            ToastUtil toastUtil = new ToastUtil(mMainActivity, photoBean.result);
                            toastUtil.show();
                            mBinding.twiRefreshlayout.finishLoadmore();
                        }
                    }

                    @Override
                    public void onError(ApiException ex) {
                        ToastUtil toastUtil = new ToastUtil(mMainActivity, ex.getDisplayMessage());
                        toastUtil.show();
                        mBinding.twiRefreshlayout.finishLoadmore();
                    }
                });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        RxBus.getDefault().unregister(this);
    }
}
