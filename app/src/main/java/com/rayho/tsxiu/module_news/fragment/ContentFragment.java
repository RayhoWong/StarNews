package com.rayho.tsxiu.module_news.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.RelativeLayout;

import com.blankj.rxbus.RxBus;
import com.blankj.utilcode.util.SPUtils;
import com.google.android.material.button.MaterialButton;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.rayho.tsxiu.R;
import com.rayho.tsxiu.base.Constant;
import com.rayho.tsxiu.base.LazyLoadFragment;
import com.rayho.tsxiu.base.Presenter;
import com.rayho.tsxiu.base.listener.OnTabReselectedListener;
import com.rayho.tsxiu.greendao.NewsCacheDao;
import com.rayho.tsxiu.http.api.NetObserver;
import com.rayho.tsxiu.http.exception.ApiException;
import com.rayho.tsxiu.http.exception.ServerStatusCode;
import com.rayho.tsxiu.module_news.adapter.NewsAdapter;
import com.rayho.tsxiu.module_news.bean.NewsBean;
import com.rayho.tsxiu.module_news.dao.NewsCache;
import com.rayho.tsxiu.module_news.viewmodel.ContentFtViewModel;
import com.rayho.tsxiu.ui.MyRefreshLottieFooter;
import com.rayho.tsxiu.ui.MyRefreshLottieHeader;
import com.rayho.tsxiu.utils.CacheUtil;
import com.rayho.tsxiu.utils.DaoManager;
import com.rayho.tsxiu.utils.NetworkUtils;
import com.rayho.tsxiu.utils.RxTimer;
import com.rayho.tsxiu.utils.RxUtil;
import com.rayho.tsxiu.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

import static com.rayho.tsxiu.base.Constant.TYPE_ID;

/**
 * 新闻列表界面(根据分类id，显示不同的新闻)
 */
public class ContentFragment extends LazyLoadFragment implements OnTabReselectedListener, Presenter {
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

    private List<NewsBean.DataBean> mData = new ArrayList<>();//返回的新闻列表数据

    private List<NewsCache> mNewsCaches = new ArrayList<>();//数据库返回新闻缓存文件的文件名

    private String cid = "news_sports";//新闻的类型id

    private String pageToken;//分页值id

    private boolean cacheFlag = false;//是否有新闻缓存

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
        RxBus.getDefault().subscribeSticky(this, "updateNums", new RxBus.Callback<Integer>() {
            @Override
            public void onEvent(Integer integer) {
                updateNums = integer;
            }
        });
//        cacheFlag = SPUtils.getInstance("NewsCache").getBoolean("cacheFlag", false);
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
        initRefreshLayout();
        initRcv();
        setNewsCacheFlag();
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


    /**
     * 设置缓存
     */
    private void setNewsCache() {
        //缓存文章到本地(文件缓存)
        Observable
                .create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(ObservableEmitter<String> emitter) {
                        //删除之前同类型的新闻缓存 保证新闻缓存是最后一次刷新的数据
                        DaoManager.getInstance().getDaoSession().getNewsCacheDao()
                                .queryBuilder()
                                .where(NewsCacheDao.Properties.Cid.eq(cid))
                                .buildDelete()
                                .executeDeleteWithoutDetachingEntities();

                        if (mData.size() > Constant.MAX_CACHE_NUMS) {
                            for (int i = 0; i < Constant.MAX_CACHE_NUMS; i++) {
                                CacheUtil.saveObjectByFile(getActivity(), mData.get(i), mData.get(i).publishDateStr + "_" + mData.get(i).id);
                                //新闻的文件名保存到数据库
                                DaoManager.getInstance().getDaoSession().getNewsCacheDao()
                                        .insertOrReplace(new NewsCache(null, mData.get(i).publishDateStr + "_" + mData.get(i).id, cid));
                            }
                        } else {
                            for (NewsBean.DataBean bean : mData) {
                                //新闻写入文件
                                CacheUtil.saveObjectByFile(getActivity(), bean, bean.publishDateStr + "_" + bean.id);
                                //新闻的文件名保存到数据库
                                DaoManager.getInstance().getDaoSession().getNewsCacheDao()
                                        .insertOrReplace(new NewsCache(null, bean.publishDateStr + "_" + bean.id, cid));
                            }
                        }
                        emitter.onNext(getString(R.string.success));
                    }
                })
                .compose(RxUtil.<String>rxSchedulerHelper())
                .compose(this.<String>bindToLifecycle())
                .subscribe();
    }


    /**
     * 判断是否有缓存 设置cacheFlag标记
     * cacheFlag=true:有
     * cacheFlag=false:无
     */
    private void setNewsCacheFlag() {
        mData = new ArrayList<>();
        //条件查询 获取数据库中属于该类型的新闻缓存
        DaoManager.getInstance().getDaoSession().getNewsCacheDao()
                .queryBuilder()
                .where(NewsCacheDao.Properties.Cid.eq(cid))
                .rx()
                .list()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<NewsCache>>() {
                    @Override
                    public void call(List<NewsCache> newsCaches) {
                        mNewsCaches = newsCaches;
//                        ToastUtil toast = new ToastUtil(getActivity(),"长度为："+String.valueOf(mNewsCaches.size()));
//                        toast.show();
                        if (mNewsCaches.size() > 0) {
                            cacheFlag = true;
                        }
                        //设置cacheFlag后 立刻获取数据
                        getData();
                    }
                });
    }


    /**
     * 获取缓存
     */
    private void getNewsCache() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) {
                if (mNewsCaches != null && mNewsCaches.size() > 0) {
                    for (NewsCache news : mNewsCaches) {
                        mData.add((NewsBean.DataBean) CacheUtil.readObjectByFile(getActivity(), news.getFileName()));
                    }
                }
                //操作执行完毕 发送数据 触发观察者的onNext方法
                emitter.onNext(getString(R.string.success));
            }
        })
                .compose(RxUtil.<String>rxSchedulerHelper())
                .compose(this.<String>bindToLifecycle())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String str) {
                        if (str.equals(getString(R.string.success))) {
                            contentFtViewModel.setNews(mData, Constant.REFRESH_DATA);
                            mRcv.setAdapter(mAdapter);
                            isShowNetWorkErrorLayout(cacheFlag);
                            mHeader.setMsg(getString(R.string.network_error));
                            mTwiRefreshlayout.finishRefreshing();
                        }
                    }
                });
    }


    /**
     * 1.RefreshLayout设置监听器
     * 2.加载数据
     */
    public void getData() {
        mTwiRefreshlayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                super.onRefresh(refreshLayout);
                if (!NetworkUtils.isConnected(getActivity())) {
                    refreshLayout.setEnableLoadmore(false);
                    refreshLayout.setEnableOverScroll(false);
                    //判断是否有新闻缓存
                    if (!cacheFlag) {
                        //无缓存 显示网络错误（支持重新加载数据）界面
                        RxTimer.timer(1000, new RxTimer.RxAction() {
                            @Override
                            public void action() {
                                isShowNetWorkErrorLayout(cacheFlag);
                                mHeader.setMsg(getString(R.string.network_error));
                                refreshLayout.finishRefreshing();
                            }
                        });
                    } else {
                        //有缓存 隐藏网络错误界面 读取新闻缓存 显示在列表上
                        RxTimer.timer(1000, new RxTimer.RxAction() {
                            @Override
                            public void action() {
                                //to do getNewsCache(读取缓存)
                                if (mData.size() == 0) {
                                    getNewsCache();
                                } else {
                                    isShowNetWorkErrorLayout(cacheFlag);
                                    mHeader.setMsg(getString(R.string.network_error));
                                    refreshLayout.finishRefreshing();
                                }
                            }
                        });
                    }
                } else {
                    //有网络 开启上拉加载和越界回弹
                    refreshLayout.setEnableLoadmore(true);
                    refreshLayout.setEnableOverScroll(true);
                    //隐藏网络重连布局
                    if (mViewStub.getVisibility() == View.VISIBLE) {
                        mViewStub.setVisibility(View.GONE);
                    }
                    //下拉刷新获取数据
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
                    //有网络 上拉加载更多获取数据
                    getNewsByLoadMore();
                }
            }
        });
    }


    /**
     * 显示网络错误布局 支持重连
     *
     * @param cacheFlag 是否有缓存 true:有  false:无
     */
    private void isShowNetWorkErrorLayout(boolean cacheFlag) {
        if (!cacheFlag) {
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
                                mData = newsBean.data;
                                contentFtViewModel.setNews(mData, Constant.REFRESH_DATA);
                                if (!TextUtils.isEmpty(newsBean.pageToken)) {
                                    pageToken = newsBean.pageToken;
//                                    Logger.d("=pageToken:"+pageToken);
                                }
                                mRcv.setAdapter(mAdapter);
                                mHeader.setMsg("星头条推荐引擎有" + String.valueOf(updateNums) + "条更新");
                                //缓存新闻
                                setNewsCache();
                                //如果更新的数据少于3条 禁止上拉加载
                                if (mData.size() < 3) {
                                    mTwiRefreshlayout.setEnableLoadmore(false);
                                    mTwiRefreshlayout.setOverScrollBottomShow(false);//禁止footer回弹
                                } else {
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


    /**
     * 通过上拉加载更多 获取新闻
     */
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
                                mData = newsBean.data;
                                contentFtViewModel.setNews(mData, Constant.LOAD_MORE_DATA);
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


    /**
     * 点击首页tab刷新数据
     */
    public void updateData() {
        //recyclerView滑动到顶部
        mRcv.smoothScrollToPosition(0);
        mTwiRefreshlayout.startRefresh();
    }

    @Override
    public void onClick(View v) {
    }
}
