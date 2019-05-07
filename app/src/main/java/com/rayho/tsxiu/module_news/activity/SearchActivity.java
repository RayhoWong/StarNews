package com.rayho.tsxiu.module_news.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.blankj.rxbus.RxBus;
import com.blankj.utilcode.util.SPUtils;
import com.google.android.material.button.MaterialButton;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.orhanobut.logger.Logger;
import com.rayho.tsxiu.R;
import com.rayho.tsxiu.base.BaseDataBindingApater;
import com.rayho.tsxiu.base.Constant;
import com.rayho.tsxiu.base.Presenter;
import com.rayho.tsxiu.databinding.ActivitySearchBinding;
import com.rayho.tsxiu.greendao.SearchHistoryDao;
import com.rayho.tsxiu.http.api.NetObserver;
import com.rayho.tsxiu.http.exception.ApiException;
import com.rayho.tsxiu.http.exception.ServerStatusCode;
import com.rayho.tsxiu.module_news.adapter.HistoryRecordAdapter;
import com.rayho.tsxiu.module_news.adapter.NewsAdapter;
import com.rayho.tsxiu.module_news.bean.NewsBean;
import com.rayho.tsxiu.module_news.bean.NewsHotSearch;
import com.rayho.tsxiu.module_news.dao.SearchHistory;
import com.rayho.tsxiu.module_news.viewmodel.HistoryRecordViewModel;
import com.rayho.tsxiu.module_news.viewmodel.SearchNewsViewModel;
import com.rayho.tsxiu.ui.refreshlayout.MyRefreshLottieFooter;
import com.rayho.tsxiu.ui.refreshlayout.MyRefreshLottieHeader;
import com.rayho.tsxiu.utils.DaoManager;
import com.rayho.tsxiu.utils.NetworkUtils;
import com.rayho.tsxiu.utils.RxTimer;
import com.rayho.tsxiu.utils.StatusBarUtil;
import com.rayho.tsxiu.utils.ToastUtil;
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import skin.support.widget.SkinCompatSupportable;

public class SearchActivity extends RxAppCompatActivity implements Presenter, SkinCompatSupportable {

    @BindView(R.id.viewStub)
    ViewStub mViewStub;

    private ActivitySearchBinding mBinding;

    private MyRefreshLottieHeader mHeader;

    private MyRefreshLottieFooter mFooter;

    private MaterialButton mBtReload;

    private SearchNewsViewModel mViewModel;

    private HistoryRecordAdapter mHistoryAdapter;

    private NewsAdapter mNewsAdapter;

    private List<BaseDataBindingApater.Items> mHistoryRecordVms = new ArrayList<>();

    private List<String> words = new ArrayList<>();//热搜词条

    private List<NewsBean.DataBean> mData = new ArrayList<>();//返回的新闻列表数据

    private String mContent;//搜索的内容(关键字)

    private String pageToken;//分页值id

    private int updateNums;//更新成功 返回的数据数量


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_search);
        mBinding.setPresenter(this);
        ButterKnife.bind(this);

        getUpdateNums();
        initStatusBar();
        initNewsRcv();
        initRefreshLayout();
        getHotSearch();
        getHistoryRecord();
        textChangedListener();
    }


    private void initStatusBar(){
        //根据夜间模式的设置 设置状态栏的颜色
        if (SPUtils.getInstance(Constant.SP_SETTINGS).getBoolean(getString(R.string.sp_nightmode))) {
            //夜间模式true 黑色
            StatusBarUtil.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark_night), this);
        } else {
            //夜间模式false 白色
            StatusBarUtil.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark), this);
        }
    }


    private void getUpdateNums() {
        RxBus.getDefault().subscribeSticky(this, "updateNums", new RxBus.Callback<Integer>() {
            @Override
            public void onEvent(Integer integer) {
                updateNums = integer;
            }
        });
    }


    private void initNewsRcv() {
        mBinding.rcvNews.setLayoutManager(new LinearLayoutManager(this));
        mNewsAdapter = new NewsAdapter(this);
        mViewModel = new SearchNewsViewModel(mContent, mNewsAdapter);
        mBinding.setVm(mViewModel);
    }


    private void initRefreshLayout() {
        mHeader = new MyRefreshLottieHeader(this);
        mFooter = new MyRefreshLottieFooter(this);
        mBinding.twiRefreshlayout.setHeaderHeight(60);
        mBinding.twiRefreshlayout.setBottomHeight(45);
        mBinding.twiRefreshlayout.setHeaderView(mHeader);
        mBinding.twiRefreshlayout.setBottomView(mFooter);
        mBinding.twiRefreshlayout.setEnableRefresh(true);
        mBinding.twiRefreshlayout.setEnableLoadmore(true);
        mBinding.twiRefreshlayout.setEnableOverScroll(true);
        mBinding.twiRefreshlayout.setAutoLoadMore(true); //自动加载
    }


    /**
     * 获取热搜词条
     */
    private void getHotSearch() {
        mViewModel.getHotSearch()
                .compose(this.<NewsHotSearch>bindToLifecycle())
                .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
                .subscribe(new NetObserver<NewsHotSearch>() {
                    @Override
                    public void onNext(NewsHotSearch newsHotSearch) {
                        if (newsHotSearch != null) {
                            if (newsHotSearch.data.suggest_words != null && newsHotSearch.data.suggest_words.size() > 0) {
                                for (NewsHotSearch.DataBean.SuggestWordsBean bean : newsHotSearch.data.suggest_words) {
                                    words.add(bean.word);
                                }
                                mBinding.flTag.setAdapter(new TagAdapter<String>(words) {
                                    @Override
                                    public View getView(FlowLayout parent, int position, String s) {
                                        TextView tag = (TextView) LayoutInflater.from(SearchActivity.this)
                                                .inflate(R.layout.search_tag, mBinding.flTag, false);
                                        tag.setText(s);
                                        return tag;
                                    }
                                });

                                mBinding.flTag.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
                                    @Override
                                    public boolean onTagClick(View view, int position, FlowLayout parent) {
                                        mBinding.etContent.setText(words.get(position));
                                        return true;
                                    }
                                });

                            } else {
                                //无法获取数据 隐藏热搜布局
                                mBinding.llHotsearch.setVisibility(View.GONE);
                            }
                        }
                    }

                    @Override
                    public void onError(ApiException ex) {
                        mBinding.llHotsearch.setVisibility(View.GONE);
                        Logger.d("错误信息：" + ex.getDisplayMessage());
//                        ToastUtil toast = new ToastUtil(mActivity, ex.getDisplayMessage());
//                        toast.show();
                    }
                });
    }


    /**
     * 获取历史记录
     */
    private void getHistoryRecord() {
        mHistoryAdapter = new HistoryRecordAdapter(SearchActivity.this, mBinding);
        DaoManager.getInstance().getDaoSession().getSearchHistoryDao()
                .rx()
                .loadAll()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<SearchHistory>>() {
                    @Override
                    public void call(List<SearchHistory> searchHistories) {
                        if (searchHistories != null && searchHistories.size() > 0) {
                            for (SearchHistory history : searchHistories) {
                                HistoryRecordViewModel vm = new HistoryRecordViewModel(history.getRecord());
                                mHistoryRecordVms.add(0, vm);
                            }
                            setHistoryRecord();
                        } else {
                            mBinding.rlHistory.setVisibility(View.GONE);
                        }
                    }
                });
    }


    /**
     * 设置搜索记录的界面
     */
    private void setHistoryRecord() {
        if (mHistoryAdapter != null && mHistoryRecordVms.size() > 0) {
            mBinding.rcv.setLayoutManager(new LinearLayoutManager(SearchActivity.this));
            mBinding.rcv.addItemDecoration(new DividerItemDecoration(SearchActivity.this, DividerItemDecoration.VERTICAL));
            mHistoryAdapter.setItems(mHistoryRecordVms);
            mBinding.rcv.setAdapter(mHistoryAdapter);
        }
    }


    /**
     * 监听搜索edittext的内容变化
     */
    private void textChangedListener() {
        mBinding.etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mBinding.etContent.getText().toString() != null &&
                        !mBinding.etContent.getText().toString().equals("")) {
                    //搜索内容不为空 显示删除button
                    mBinding.ivClose.setVisibility(View.VISIBLE);

                } else {
                    //搜索内容为空 隐藏button
                    mBinding.ivClose.setVisibility(View.INVISIBLE);
                    //隐藏新闻列表界面
                    if (mBinding.twiRefreshlayout.getVisibility() == View.VISIBLE) {
                        mBinding.twiRefreshlayout.setVisibility(View.GONE);
                    }
                    //隐藏网络错误界面
                    if (mViewStub.getVisibility() == View.VISIBLE) {
                        mViewStub.setVisibility(View.GONE);
                    }
                    //显示历史记录(记录数据大于0)
                    if (mBinding.rlHistory.getVisibility() == View.GONE && mHistoryRecordVms.size() > 0) {
                        mBinding.rlHistory.setVisibility(View.VISIBLE);
                    }
                    //显示热搜(热搜数据条数大于0)
                    if (mBinding.llHotsearch.getVisibility() == View.GONE && words.size() > 0) {
                        mBinding.llHotsearch.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_close:
                mBinding.etContent.getText().clear();
                break;
            case R.id.tv_search:
                searchNews();
                break;
            case R.id.tv_clear:
                clearHistory();
                break;
        }
    }


    /**
     * 搜索新闻 同时添加搜索记录
     */
    private void searchNews() {
        //动态获取搜索内容
        mContent = mBinding.etContent.getText().toString().trim();
        if (TextUtils.isEmpty(mContent)) {
            ToastUtil toast = new ToastUtil(SearchActivity.this, "请输入搜索内容");
            toast.show();
            return;

        } else {
            //内容不为空 隐藏历史记录和热搜
            if (mBinding.llHotsearch.getVisibility() == View.VISIBLE
                    || mBinding.rlHistory.getVisibility() == View.VISIBLE) {
                mBinding.llHotsearch.setVisibility(View.GONE);
                mBinding.rlHistory.setVisibility(View.GONE);
            }
            //显示新闻列表
            if (mBinding.twiRefreshlayout.getVisibility() == View.GONE) {
                mBinding.twiRefreshlayout.setVisibility(View.VISIBLE);
            }
            //获取新闻
            getData();
            //查询该记录是否已存在
            DaoManager.getInstance().getDaoSession().getSearchHistoryDao()
                    .queryBuilder()
                    .where(SearchHistoryDao.Properties.Record.eq(mContent))
                    .rx()
                    .unique()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<SearchHistory>() {
                        @Override
                        public void call(SearchHistory searchHistory) {
                            //记录不存在 添加记录
                            if (searchHistory == null) {
                                SearchHistory history = new SearchHistory(null, mContent);
                                DaoManager.getInstance().getDaoSession().getSearchHistoryDao()
                                        .rx()
                                        .insertOrReplace(history)
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new Action1<SearchHistory>() {
                                            @Override
                                            public void call(SearchHistory searchHistory) {
                                                if (searchHistory != null) {
                                                    //当前adapter插入新的搜索记录
                                                    if (mHistoryAdapter != null) {
                                                        if (mHistoryRecordVms.size() == 0) {
                                                            mHistoryRecordVms.add(new HistoryRecordViewModel(mContent));
                                                            setHistoryRecord();
                                                        } else if (mHistoryRecordVms.size() > 0) {
                                                            mHistoryAdapter.addItem(new HistoryRecordViewModel(mContent), 0);
                                                        }
                                                    }
                                                }
                                            }
                                        });
                            }
                        }
                    });
        }
    }


    /**
     * 1.RefreshLayout设置监听器
     * 2.加载数据
     */
    private void getData() {
        //如果当前新闻列表有数据 再次搜索时 清空界面
        if (mData.size() > 0 && mNewsAdapter != null) {
            mData.clear();
            mNewsAdapter.clearItems();
        }
        mBinding.twiRefreshlayout.startRefresh();
        mBinding.twiRefreshlayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                super.onRefresh(refreshLayout);
                if (!NetworkUtils.isConnected(SearchActivity.this)) {
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
                    getNewsByRefresh();
                }
            }

            @Override
            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);
                if (!NetworkUtils.isConnected(SearchActivity.this)) {
                    //无网络
                    RxTimer.timer(1500, new RxTimer.RxAction() {
                        @Override
                        public void action() {
                            ToastUtil util = new ToastUtil(SearchActivity.this, getString(R.string.network_error_tips2));
                            util.show();
                            refreshLayout.finishLoadmore();
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
            mBtReload = mBinding.llRoot.findViewById(R.id.bt_reload);
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
     * 通过下拉刷新获取数据
     */
    private void getNewsByRefresh() {
        mViewModel
                .getNewsObservable(mContent, null)
                //自动在onDestroy中取消订阅 避免内存泄漏  一定要在subscribeOn方法之后调用
                .compose(this.<NewsBean>bindToLifecycle())
                .subscribe(new NetObserver<NewsBean>() {
                    @Override
                    public void onNext(NewsBean newsBean) {
                        //请求成功 retcode:000000
                        if (ServerStatusCode.getStatusResponse(newsBean.retcode)
                                .equals(getString(R.string.request_success))) {
                            if (newsBean.data != null) {
                                mData = newsBean.data;
                                mViewModel.setNews(mData, Constant.REFRESH_DATA);
                                if (!TextUtils.isEmpty(newsBean.pageToken)) {
                                    pageToken = newsBean.pageToken;
//                                    Logger.d("=pageToken:"+pageToken);
                                }
                                mBinding.rcvNews.setAdapter(mNewsAdapter);
                                mHeader.setMsg("星头条推荐引擎有" + String.valueOf(updateNums) + "条更新");
                                //如果更新的数据少于3条 禁止上拉加载
                                if (mData.size() < 3) {
                                    mBinding.twiRefreshlayout.setEnableLoadmore(false);
                                    mBinding.twiRefreshlayout.setEnableOverScroll(false);//禁止footer回弹
                                } else {
                                    mBinding.twiRefreshlayout.setEnableLoadmore(true);
                                    mBinding.twiRefreshlayout.setEnableOverScroll(true);//允许footer回弹
                                }
                                mBinding.twiRefreshlayout.setOverScrollRefreshShow(false);
                                mBinding.twiRefreshlayout.finishRefreshing();
                            }

                        } else {
                            mHeader.setMsg(ServerStatusCode.getStatusResponse(newsBean.retcode));
                            ToastUtil toastUtil = new ToastUtil(SearchActivity.this,
                                    ServerStatusCode.getStatusResponse(newsBean.retcode));
                            toastUtil.show();
                            mBinding.twiRefreshlayout.finishRefreshing();
                        }
                    }

                    @Override
                    public void onError(ApiException ex) {
                        mHeader.setMsg(ex.getDisplayMessage());
                        ToastUtil toastUtil = new ToastUtil(SearchActivity.this, ex.getDisplayMessage());
                        toastUtil.show();
                        mBinding.twiRefreshlayout.finishRefreshing();
                    }
                });
    }


    /**
     * 通过上拉加载更多 获取新闻
     */
    private void getNewsByLoadMore() {
        mViewModel
                .getNewsObservable(mContent, pageToken)
                //自动在onDestroy中取消订阅 避免内存泄漏  一定要在subscribeOn方法之后调用
                .compose(this.<NewsBean>bindToLifecycle())
                .subscribe(new NetObserver<NewsBean>() {
                    @Override
                    public void onNext(NewsBean newsBean) {
                        if (ServerStatusCode.getStatusResponse(newsBean.retcode)
                                .equals(getString(R.string.request_success))) {
                            if (newsBean.data != null) {
                                mData = newsBean.data;
                                mViewModel.setNews(mData, Constant.LOAD_MORE_DATA);
                                if (!TextUtils.isEmpty(newsBean.pageToken)) {
                                    pageToken = newsBean.pageToken;
//                                    Logger.d("=====pageToken:"+pageToken);
                                } else {
                                    pageToken = null;
                                }
                                mBinding.twiRefreshlayout.finishLoadmore();
                            }
                        } else {
                            ToastUtil toastUtil = new ToastUtil(SearchActivity.this,
                                    ServerStatusCode.getStatusResponse(newsBean.retcode));
                            toastUtil.show();
                            mBinding.twiRefreshlayout.finishLoadmore();
                        }
                    }

                    @Override
                    public void onError(ApiException ex) {
                        ToastUtil toastUtil = new ToastUtil(SearchActivity.this, ex.getDisplayMessage());
                        toastUtil.show();
                        mBinding.twiRefreshlayout.finishLoadmore();
                    }
                });
    }


    /**
     * 清空历史记录
     */
    private void clearHistory() {
        new MaterialDialog.Builder(this)
                .title("清空历史记录")
                .content("确定删除所有的搜索记录吗?")
                .positiveText("确定")
                .negativeText("取消")
                //点击事件添加
                .onAny(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (which == DialogAction.POSITIVE) {
                            DaoManager.getInstance().getDaoSession().getSearchHistoryDao()
                                    .rx()
                                    .deleteAll()
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Action1<Void>() {
                                        @Override
                                        public void call(Void aVoid) {
                                            if (mHistoryAdapter != null && mHistoryRecordVms.size() > 0) {
                                                mHistoryAdapter.clearItems();
                                                mBinding.rlHistory.setVisibility(View.GONE);
                                            }
                                        }
                                    });
                        } else if (which == DialogAction.NEGATIVE) {
                            dialog.dismiss();
                        }
                    }
                }).show();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        //取消订阅 避免内存泄漏
        RxBus.getDefault().unregister(this);
    }


    /**
     * 动态监听换肤行为 设置状态栏颜色
     */
    @Override
    public void applySkin() {
        if (SPUtils.getInstance(Constant.SP_SETTINGS).getBoolean(getString(R.string.sp_nightmode))) {
            //夜间模式true 黑色
            StatusBarUtil.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark_night), this);
        } else {
            //夜间模式false 白色
            StatusBarUtil.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark), this);
        }
    }
}
