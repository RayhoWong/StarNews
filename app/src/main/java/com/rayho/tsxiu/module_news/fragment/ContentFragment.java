package com.rayho.tsxiu.module_news.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.rayho.tsxiu.R;
import com.rayho.tsxiu.base.LazyLoadFragment;
import com.rayho.tsxiu.base.listener.OnTabReselectedListener;
import com.rayho.tsxiu.module_news.adapter.NewsAdapter;
import com.rayho.tsxiu.module_news.bean.NewsBean;
import com.rayho.tsxiu.module_news.viewmodel.ContentFtViewModel;
import com.rayho.tsxiu.ui.MyRefreshLottieFooter;
import com.rayho.tsxiu.ui.MyRefreshLottieHeader;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
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

    private static final String TYPE_ID = "tag";
    private String tag;

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
        initData();
    }

    private void initData(){
        mRcv.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new NewsAdapter();
        contentFtViewModel = new ContentFtViewModel(mAdapter);
        contentFtViewModel.getData();
        mRcv.setAdapter(mAdapter);
    }



    private void initRefreshLayout() {
        mTwiRefreshlayout.setHeaderHeight(60);
        mTwiRefreshlayout.setBottomHeight(45);
        mTwiRefreshlayout.setHeaderView(new MyRefreshLottieHeader(getActivity()));
        mTwiRefreshlayout.setBottomView(new MyRefreshLottieFooter(getActivity()));
        mTwiRefreshlayout.setEnableRefresh(false);
        mTwiRefreshlayout.setEnableLoadmore(false);
        mTwiRefreshlayout.setEnableOverScroll(false);
//        mTwiRefreshlayout.startRefresh();
//        mTwiRefreshlayout.setAutoLoadMore(true);
        /*mTwiRefreshlayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                super.onRefresh(refreshLayout);
                setTimer(3000, new RxTimer.RxAction() {
                    @Override
                    public void action() {
                        initRcv();
                        refreshLayout.finishRefreshing();
                    }
                });
            }

            @Override
            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);
                setTimer(2000, new RxTimer.RxAction() {
                    @Override
                    public void action() {
                        for (int i=0;i<3;i++){
                            data.add("NEW HELLO");
                        }
                        adapter.notifyDataSetChanged();
                        refreshLayout.finishLoadmore();
                    }
                });
            }

        });*/
    }

    public void updateData() {
       /* if(mTvTag != null){
            mTvTag.setText("updateData!!!");
        }else {

        }*/
    }


}
