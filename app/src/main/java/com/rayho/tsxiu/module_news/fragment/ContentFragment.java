package com.rayho.tsxiu.module_news.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.RelativeLayout;

import com.blankj.rxbus.RxBus;
import com.google.android.material.button.MaterialButton;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.rayho.tsxiu.R;
import com.rayho.tsxiu.base.Constant;
import com.rayho.tsxiu.base.LazyLoadFragment;
import com.rayho.tsxiu.base.Presenter;
import com.rayho.tsxiu.base.listener.OnTabReselectedListener;
import com.rayho.tsxiu.http.api.NetObserver;
import com.rayho.tsxiu.http.exception.ApiException;
import com.rayho.tsxiu.http.exception.ServerStatusCode;
import com.rayho.tsxiu.module_news.adapter.NewsAdapter;
import com.rayho.tsxiu.module_news.bean.NewsBean;
import com.rayho.tsxiu.module_news.viewmodel.ContentFtViewModel;
import com.rayho.tsxiu.ui.MyRefreshLottieFooter;
import com.rayho.tsxiu.ui.MyRefreshLottieHeader;
import com.rayho.tsxiu.utils.NetworkUtils;
import com.rayho.tsxiu.utils.RxTimer;
import com.rayho.tsxiu.utils.ToastUtil;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

import static com.rayho.tsxiu.base.Constant.TYPE_ID;

/**
 * 新闻列表界面(根据分类id，显示不同的新闻)
 */
public class ContentFragment extends LazyLoadFragment implements OnTabReselectedListener, Presenter {
    /* @BindView(R.id.tv_tag)
     TextView mTvTag;*/
    @BindView(R.id.rcv)
    RecyclerView mRcv;
    @BindView(R.id.twi_refreshlayout)
    TwinklingRefreshLayout mTwiRefreshlayout;
    @BindView(R.id.viewStub)
    ViewStub mViewStub;
    @BindView(R.id.rl)
    RelativeLayout mRl;

    private MaterialButton mBtReload;

    private MyRefreshLottieHeader mHeader;

    private MyRefreshLottieFooter mFooter;

    private ContentFragment mContentFragment;

    private String cid = "news_sports";//新闻的类型id

    private String pageToken;//分页值id

    private boolean flag = false;//是否有新闻缓存

    private int updateNums;//更新成功 返回的数据数量

    private NewsAdapter mAdapter;

    private ContentFtViewModel contentFtViewModel;


    public static ContentFragment newInstance(String cid) {
        ContentFragment fragment = new ContentFragment();
        //通过bundle保存NewTabFragment传过来的分类Id
        Bundle args = new Bundle();
        args.putString(TYPE_ID, cid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContentFragment = this;
        RxBus.getDefault().subscribe(this, "updateNums", new RxBus.Callback<Integer>() {
            @Override
            public void onEvent(Integer integer) {
                updateNums = integer;
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //取消订阅 避免内存泄漏
        RxBus.getDefault().unregister(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        Timber.tag("Contentfrg");
        //获取分类的id
        cid = getArguments().getString(TYPE_ID);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public int getLayoutId() {
        return R.layout.content_fragment;
    }

    @Override
    public void loadData() {
//        mTvTag.setText(tag);
        initRefreshLayout();
        initRcv();
        getData();
    }

    private void initRcv() {
        mRcv.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new NewsAdapter(this);
        contentFtViewModel = new ContentFtViewModel(mAdapter);
    }

    private void initRefreshLayout() {
        mHeader = new MyRefreshLottieHeader(getActivity());
        mFooter = new MyRefreshLottieFooter(getActivity());
        mTwiRefreshlayout.setHeaderHeight(60);
        mTwiRefreshlayout.setBottomHeight(45);
        mTwiRefreshlayout.setHeaderView(mHeader);
        mTwiRefreshlayout.setBottomView(mFooter);
        mTwiRefreshlayout.setEnableRefresh(true);
        mTwiRefreshlayout.setEnableLoadmore(true);
        //越界回弹
        mTwiRefreshlayout.setOverScrollRefreshShow(false);//禁止header回弹
        mTwiRefreshlayout.setOverScrollBottomShow(true);//允许footer回弹
        mTwiRefreshlayout.startRefresh(); //自动刷新
        mTwiRefreshlayout.setAutoLoadMore(true); //自动加载
    }


    public void getData() {
        mTwiRefreshlayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                super.onRefresh(refreshLayout);
                if (!NetworkUtils.isConnected(getActivity())) {
                    //判断是否有新闻缓存
                    if (!flag) {
                        //无缓存 显示网络错误（支持重新加载数据）界面
                        RxTimer.timer(2000, new RxTimer.RxAction() {
                            @Override
                            public void action() {
                                isShowNetWorkErrorLayout(flag);
                                mHeader.setMsg(getString(R.string.network_error));
                                refreshLayout.finishRefreshing();
                            }
                        });
                    } else {
                        //有缓存 隐藏网络错误界面 读取新闻缓存 显示在列表上
                        RxTimer.timer(2000, new RxTimer.RxAction() {
                            @Override
                            public void action() {
                                isShowNetWorkErrorLayout(flag);
                                mHeader.setMsg(getString(R.string.network_error));
                                refreshLayout.finishRefreshing();
                                //to do getNewsCache(读取缓存)
                            }
                        });
                    }
                } else {
                    //有网络 开启上拉加载和越界回弹
                    refreshLayout.setEnableLoadmore(true);
                    mTwiRefreshlayout.setEnableOverScroll(true);
                    if (mViewStub.getVisibility() == View.VISIBLE) {
                        mViewStub.setVisibility(View.GONE);
                    }
                    getNewsByRefresh();
                }
            }


            @Override
            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);
                if (!NetworkUtils.isConnected(getActivity())) {
                    RxTimer.timer(1500, new RxTimer.RxAction() {
                        @Override
                        public void action() {
                            ToastUtil util = new ToastUtil(getActivity(), getString(R.string.network_error_tips2));
                            util.show();
                            refreshLayout.finishLoadmore();
                            //无网络 关闭上拉加载和越界回弹
                            refreshLayout.setEnableLoadmore(false);
                            refreshLayout.setEnableOverScroll(false);
                        }
                    });
                } else {
                    getNewsByLoadMore();
                }
            }
        });
    }

    /**
     * 显示网络错误布局 支持重连
     *
     * @param flag 是否有缓存 true:有  false:无
     */
    private void isShowNetWorkErrorLayout(boolean flag) {
        if (!flag) {
            if (mViewStub.getVisibility() == View.GONE) {
/*               * 当mViewStub.setVisibility(View.VISIBLE)后 得到加载进来的布局
                 * 然后通过根布局mRl.findViewById()找到已加载布局的子控件view
                 * 该做法避免找不到控件的空指针异常
                 * 如果直接 布局子控件 = mViewStub.findViewById 报空指针异常*/
                mViewStub.setVisibility(View.VISIBLE);//或者mViewStub.inflate();
                mBtReload = mRl.findViewById(R.id.bt_reload);
                mBtReload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mTwiRefreshlayout.startRefresh();
                    }
                });
            } else {
                return;
            }
        } else {
            if (mViewStub.getVisibility() == View.VISIBLE) {
                mViewStub.setVisibility(View.GONE);
            }
            ToastUtil util = new ToastUtil(getActivity(), getString(R.string.no_network_tips));
            util.show();
        }
    }


    /**
     * 通过下拉刷新 获取新闻
     */
    private void getNewsByRefresh() {
        contentFtViewModel
                .getNewsObservable(cid, null)
                //自动在onDestroy中取消订阅 避免内存泄漏  一定要在subscribeOn方法之后调用
                .compose(mContentFragment.<NewsBean>bindToLifecycle())
                .subscribe(new NetObserver<NewsBean>() {
                    @Override
                    public void onNext(NewsBean newsBean) {
                        //请求成功 retcode:000000
                        if (ServerStatusCode.getStatusResponse(newsBean.retcode)
                                .equals(getString(R.string.request_success))) {
                            if (newsBean.data != null) {
                                List<NewsBean.DataBean> list = newsBean.data;
                                contentFtViewModel.setNews(list, Constant.REFRESH_DATA);
                                if (!TextUtils.isEmpty(newsBean.pageToken)) {
                                    pageToken = newsBean.pageToken;
//                                    Logger.d("=pageToken:"+pageToken);
                                }
                                mRcv.setAdapter(mAdapter);
                                mHeader.setMsg("星头条推荐引擎有" + String.valueOf(updateNums) + "条更新");
                                flag = true;
                                //如果更新的数据少于5条 禁止上拉加载
                                if(list.size() < 5){
                                    mTwiRefreshlayout.setEnableLoadmore(false);
                                    mTwiRefreshlayout.setOverScrollBottomShow(false);//允许footer回弹
                                }else {
                                    mTwiRefreshlayout.setEnableLoadmore(true);
                                    mTwiRefreshlayout.setOverScrollBottomShow(true);//允许footer回弹
                                }
                                mTwiRefreshlayout.finishRefreshing();
                            }

                        } else {
                            mHeader.setMsg(ServerStatusCode.getStatusResponse(newsBean.retcode));
                            ToastUtil toastUtil = new ToastUtil(getActivity(),
                                    ServerStatusCode.getStatusResponse(newsBean.retcode));
                            toastUtil.show();
                            mTwiRefreshlayout.finishRefreshing();
                        }
                    }

                    @Override
                    public void onError(ApiException ex) {
                        mHeader.setMsg(ex.getDisplayMessage());
                        ToastUtil toastUtil = new ToastUtil(getActivity(), ex.getDisplayMessage());
                        toastUtil.show();
                        mTwiRefreshlayout.finishRefreshing();
                    }
                });
    }

    private void getNewsByLoadMore() {
        contentFtViewModel
                .getNewsObservable(cid, pageToken)
                //自动在onDestroy中取消订阅 避免内存泄漏  一定要在subscribeOn方法之后调用
                .compose(mContentFragment.<NewsBean>bindToLifecycle())
                .subscribe(new NetObserver<NewsBean>() {
                    @Override
                    public void onNext(NewsBean newsBean) {
                        if (ServerStatusCode.getStatusResponse(newsBean.retcode)
                                .equals(getString(R.string.request_success))) {
                            if (newsBean.data != null) {
                                List<NewsBean.DataBean> list = newsBean.data;
                                contentFtViewModel.setNews(list, Constant.LOAD_MORE_DATA);
                                if (!TextUtils.isEmpty(newsBean.pageToken)) {
                                    pageToken = newsBean.pageToken;
//                                    Logger.d("=====pageToken:"+pageToken);
                                } else {
                                    pageToken = null;
                                }
                                mTwiRefreshlayout.finishLoadmore();
                            }
                        } else {
                            ToastUtil toastUtil = new ToastUtil(getActivity(),
                                    ServerStatusCode.getStatusResponse(newsBean.retcode));
                            toastUtil.show();
                            mTwiRefreshlayout.finishLoadmore();
                        }
                    }

                    @Override
                    public void onError(ApiException ex) {
                        ToastUtil toastUtil = new ToastUtil(getActivity(), ex.getDisplayMessage());
                        toastUtil.show();
                        mTwiRefreshlayout.finishLoadmore();
                    }
                });
    }

    public void updateData() {
       /* if(mTvTag != null){
            mTvTag.setText("updateData!!!");
        }else {

        }*/
        mRcv.smoothScrollToPosition(0);
        mTwiRefreshlayout.startRefresh();
    }

    @Override
    public void onClick(View v) {

    }
}
