package com.rayho.tsxiu.module_mine.fragment;


import android.app.Fragment;
import android.view.View;
import android.view.ViewStub;

import com.rayho.tsxiu.R;
import com.rayho.tsxiu.base.BaseDataBindingApater;
import com.rayho.tsxiu.base.LazyLoadFragment;
import com.rayho.tsxiu.module_mine.adapter.NewsCtAdapter;
import com.rayho.tsxiu.module_mine.viewmodel.NewsCtViewModel;
import com.rayho.tsxiu.module_news.dao.News;
import com.rayho.tsxiu.utils.DaoManager;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;

/**
 * A simple {@link Fragment} subclass.
 * 新闻的收藏
 */
public class NewsCtFragment extends LazyLoadFragment {

    @BindView(R.id.rcv)
    RecyclerView mRcv;

    @BindView(R.id.viewStub)
    public ViewStub mViewStub;


    private List<BaseDataBindingApater.Items> items = new ArrayList<>();


    private NewsCtAdapter mAdapter;


    public static NewsCtFragment newInstance() {
        NewsCtFragment fragment = new NewsCtFragment();
        return fragment;
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_news_ct;
    }


    @Override
    public void loadData() {
        getNewsFromDB();
    }


    /**
     * 从数据库获取收藏的新闻
     */
    private void getNewsFromDB() {
        DaoManager.getInstance().getDaoSession().getNewsDao()
                .rx()
                .loadAll()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(news -> {
                    if (news != null && news.size() > 0) {
                        for (News bean : news) {
                            NewsCtViewModel model = new NewsCtViewModel(bean);
                            items.add(model);
                        }
                        mAdapter = new NewsCtAdapter(this);
                        mAdapter.setItems(items);
                        mRcv.setLayoutManager(new LinearLayoutManager(getActivity()));
                        mRcv.setAdapter(mAdapter);
                    } else if (news != null && news.size() == 0) {
                        //无数据 显示无数据布局
                        mViewStub.setVisibility(View.VISIBLE);
                    }
                });
    }

}
