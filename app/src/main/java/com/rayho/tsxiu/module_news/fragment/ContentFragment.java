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
    private ContentViewModel mViewModel;

    NewsAdapter mAdapter;
    private List<NewsBean.DataBean> mList = new ArrayList<>();
    private List<String> mImageUrlList = new ArrayList<>();


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
//        mViewModel = ViewModelProviders.of(this).get(ContentViewModel.class);
//        mTvTag.setText(tag);
        initRefreshLayout();
        initData();
    }

    private void initData(){
        mImageUrlList.add("http://p1-tt.bytecdn.cn/large/pgc-image/RGPKNSq3mtQzNc");
        mImageUrlList.add("http://p3-tt.bytecdn.cn/large/pgc-image/RGPKNT23AJbl4b");
        mImageUrlList.add("http://p1-tt.bytecdn.cn/large/pgc-image/RGPKNTFFrcz02F");
        mImageUrlList.add("http://p1-tt.bytecdn.cn/large/pgc-image/RGPKNTgH29XGii");
        mImageUrlList.add("http://p9-tt.bytecdn.cn/large/pgc-image/RGPKNTRLV9vvu");

        for(int i=0;i<10;i++){
            NewsBean.DataBean bean = new NewsBean.DataBean();
            bean.title = "今日头条在东莞松山湖设立研发中心，大量职位开放，快点来投递简历吧。";
            bean.imageUrls = mImageUrlList;
            bean.commentCount = i+1;
            bean.publishDate = 1548925892;
            bean.type = new Random().nextInt(4);
            mList.add(bean);
        }
        mRcv.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new NewsAdapter(mList,this);
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
