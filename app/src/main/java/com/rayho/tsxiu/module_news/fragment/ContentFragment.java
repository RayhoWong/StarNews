package com.rayho.tsxiu.module_news.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.rxbus.RxBus;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.rayho.tsxiu.R;
import com.rayho.tsxiu.base.Constant;
import com.rayho.tsxiu.base.LazyLoadFragment;
import com.rayho.tsxiu.base.listener.OnTabReselectedListener;
import com.rayho.tsxiu.module_news.adapter.NewsAdapter;
import com.rayho.tsxiu.module_news.viewmodel.ContentFtViewModel;
import com.rayho.tsxiu.ui.MyRefreshLottieFooter;
import com.rayho.tsxiu.ui.MyRefreshLottieHeader;
import com.rayho.tsxiu.utils.RxTimer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

/**
 * 新闻列表界面(根据分类id，显示不同的新闻)
 */
public class ContentFragment extends LazyLoadFragment implements OnTabReselectedListener {
    /* @BindView(R.id.tv_tag)
     TextView mTvTag;*/
    @BindView(R.id.rcv)
    RecyclerView mRcv;

    @BindView(R.id.twi_refreshlayout)
    TwinklingRefreshLayout mTwiRefreshlayout;

    private MyRefreshLottieHeader mHeader;

    private MyRefreshLottieFooter mFooter;

    private static final String TYPE_ID = "tag";

    private String tag;

    private int updateNums;//更新成功 返回的数据数量

    private NewsAdapter mAdapter;

    private ContentFtViewModel contentFtViewModel;


    public static ContentFragment newInstance(String tag) {
        ContentFragment fragment = new ContentFragment();
        Bundle args = new Bundle();
        args.putString(TYPE_ID, tag);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        //获取分类名称
        tag = getArguments().getString(TYPE_ID);
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

    private void initRcv(){
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
        mTwiRefreshlayout.setEnableOverScroll(true);
        mTwiRefreshlayout.startRefresh(); //自动刷新
        mTwiRefreshlayout.setAutoLoadMore(true); //自动加载
    }

    public void getData(){
        mTwiRefreshlayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                super.onRefresh(refreshLayout);
                RxTimer.timer(3000, new RxTimer.RxAction() {
                    @Override
                    public void action() {
                        contentFtViewModel.getNews(Constant.REFRESH_DATA);
                        mRcv.setAdapter(mAdapter);
                        mHeader.setNums(updateNums);
                        refreshLayout.finishRefreshing();
                    }
                });
            }

            @Override
            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);
                RxTimer.timer(2000, new RxTimer.RxAction() {
                    @Override
                    public void action() {
                        contentFtViewModel.getNews(Constant.LOAD_MORE_DATA);
                        refreshLayout.finishLoadmore();
                    }
                });
            }

        });
    }

    public void updateData() {
       /* if(mTvTag != null){
            mTvTag.setText("updateData!!!");
        }else {

        }*/
    }
}
