package com.rayho.tsxiu.ui.channelhelper.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.blankj.rxbus.RxBus;
import com.rayho.tsxiu.R;
import com.rayho.tsxiu.http.api.NetObserver;
import com.rayho.tsxiu.http.exception.ApiException;
import com.rayho.tsxiu.http.exception.ServerStatusCode;
import com.rayho.tsxiu.module_news.NewsTabFragment;
import com.rayho.tsxiu.module_news.bean.NewsTypeBean;
import com.rayho.tsxiu.module_news.dao.Channel;
import com.rayho.tsxiu.module_news.retrofit.NewsLoader;
import com.rayho.tsxiu.ui.channelhelper.adapter.ChannelAdapter;
import com.rayho.tsxiu.ui.channelhelper.base.IChannelType;
import com.rayho.tsxiu.ui.channelhelper.bean.ChannelBean;
import com.rayho.tsxiu.utils.DaoManager;
import com.rayho.tsxiu.utils.NetworkUtils;
import com.rayho.tsxiu.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by rayho on 2019/3/2
 * 频道管理器
 */
public class ChannelActivity extends AppCompatActivity implements ChannelAdapter.ChannelItemClickListener {

    private ImageView mIvClose;

    private RecyclerView mRecyclerView;

    private ChannelAdapter mRecyclerAdapter;

    private List<Channel> mChannels;//从数据库获取的我的(本地)频道

    private List<ChannelBean> mMyChannelList;//我的(本地)频道

    //当前频道管理器显示的我的频道(从ChannelAdapter中动态获取)
    public List<ChannelBean> mMyChannelList_2 = new ArrayList<>();

    private List<ChannelBean> mRecChannelList;//推荐频道

    private List<String> mMyChannelStrs_Db;//从数据库获取的我的频道对应名称

    private List<String> mMyChannelStrs;//当前频道管理器显示的我的频道对应名称

    private NewsTabFragment mFragment;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_edit);

        getNewsTabFt();
        initView();
        getChannels();
    }


    /**
     * 获取当前NewsTabFragment引用by RxBus
     */
    private void getNewsTabFt() {
        RxBus.getDefault().subscribeSticky(this, "NewTabFt", new RxBus.Callback<NewsTabFragment>() {
            @Override
            public void onEvent(NewsTabFragment newsTabFragment) {
                mFragment = newsTabFragment;
              /*  if(mFragment != null){
                    ToastUtil util = new ToastUtil(ChannelActivity.this,"haha" );
                    util.show();
                }*/
            }
        });
    }


    private void initView() {
        mIvClose = findViewById(R.id.iv_close);
        mRecyclerView = findViewById(R.id.id_tab_recycler_view);
        mIvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        GridLayoutManager gridLayout = new GridLayoutManager(this, 4);
        gridLayout.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                /**
                 * SpanSize:控制当前item占用的列数
                 * 如果是标题就占用4列（最多）显示一行
                 * 否则只占用1列
                 */
                boolean isHeader = mRecyclerAdapter.getItemViewType(position) == IChannelType.TYPE_MY_CHANNEL_HEADER ||
                        mRecyclerAdapter.getItemViewType(position) == IChannelType.TYPE_REC_CHANNEL_HEADER;
                return isHeader ? 4 : 1;
            }
        });
        mRecyclerView.setLayoutManager(gridLayout);
    }


    private void getChannels() {
        getMyChannels();
//        getRecChannels();
    }

    /**
     * 获取我的(本地)频道
     */
    private void getMyChannels() {
        mMyChannelList = new ArrayList<>();
        DaoManager.getInstance().getDaoSession().getChannelDao()
                .rx()
                .loadAll()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Channel>>() {
                    @Override
                    public void call(List<Channel> channels) {
                        mChannels = channels;
                        for (int i = 0; i < mChannels.size(); i++) {
                            ChannelBean channelBean = new ChannelBean();
                            channelBean.setTabName(mChannels.get(i).getName());
                            channelBean.setCid(mChannels.get(i).getCid());
                            channelBean.setTabType(i == 0 ? 0 : i == 1 ? 1 : 2);
                            mMyChannelList.add(channelBean);
                        }
                        getRecChannels();
                    }
                });
    }

    /**
     * 获取推荐频道
     */
    private void getRecChannels() {
        mRecChannelList = new ArrayList<>();
        if (!NetworkUtils.isConnected(this)) {
            //无网络
            ToastUtil util = new ToastUtil(this, getString(R.string.no_network_tips));
            util.show();
            initAdapter();
        } else {
            NewsLoader.getInstance().getChannels()
                    .subscribe(new NetObserver<NewsTypeBean>() {
                        @Override
                        public void onNext(NewsTypeBean newsTypeBean) {
                            //请求成功 有数据返回
                            if (ServerStatusCode.getStatusResponse(newsTypeBean.retcode)
                                    .equals(getString(R.string.request_success))) {

                                if (newsTypeBean.data.size() > 0 && newsTypeBean.data != null) {
                                    for (int i = 0; i < newsTypeBean.data.size(); i++) {
                                        ChannelBean channelBean = new ChannelBean();
                                        channelBean.setTabName(newsTypeBean.data.get(i).value);
                                        channelBean.setCid(newsTypeBean.data.get(i).key);
                                        channelBean.setTabType(2);
                                        mRecChannelList.add(channelBean);
                                    }
                                    /*推荐频道(mRecChannelList)与我的频道(mMyChannelList)
                                       重复的频道从推荐频道里去掉
                                       前提条件是：推荐频道的频道数 >= 我的频道的频道数
                                       结果保证推荐频道不包含我的频道里面的所有频道*/
                                    if (mRecChannelList.size() >= mMyChannelList.size()) {
                                        for (int i = 0; i < mMyChannelList.size(); i++) {
                                            for (int j = 0; j < mRecChannelList.size(); j++) {
                                                if (mRecChannelList.get(j).getTabName().
                                                        equals(mMyChannelList.get(i).getTabName())) {
                                                    mRecChannelList.remove(j);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            initAdapter();
                        }

                        @Override
                        public void onError(ApiException ex) {
                            ToastUtil util = new ToastUtil(ChannelActivity.this, ex.getDisplayMessage());
                            util.show();
                            initAdapter();
                        }
                    });
        }
    }

    /**
     * 初始化adapter
     * 无论mMyChannelList(推荐频道)是否有数据 都必须调用该方法
     */
    private void initAdapter() {
        mRecyclerAdapter = new ChannelAdapter(ChannelActivity.this, mRecyclerView, mMyChannelList, mRecChannelList, 1, 1);
        mRecyclerAdapter.setChannelItemClickListener(ChannelActivity.this);
        mRecyclerView.setAdapter(mRecyclerAdapter);
    }

    /**
     * 初始化本地我的频道list和当前我的频道list
     * 用List<String> 便于两个集合对比
     */
    private void initMyChannelStrs() {
        mMyChannelStrs_Db = new ArrayList<>();//from数据库
        mMyChannelStrs = new ArrayList<>();//当前显示
        for (int i = 0; i < mChannels.size(); i++) {
            mMyChannelStrs_Db.add(mChannels.get(i).getName());
        }

        if (mMyChannelList_2 != null && mMyChannelList_2.size() > 0) {
            for (int i = 0; i < mMyChannelList_2.size(); i++) {
                mMyChannelStrs.add(mMyChannelList_2.get(i).getTabName());
            }
        } else {
            //当前我的频道没变化
            return;
        }
    }

    /**
     * 点击我的频道
     * @param list  从ChannelAdapter动态获取数据
     * @param position
     */
    @Override
    public void onChannelItemClick(List<ChannelBean> list, int position) {
        mMyChannelList_2 = list;
        initMyChannelStrs();
        //只要当前显示我的频道 与 数据库中获取我的频道 不一样(频道的数量或者顺序不同)
        //动态更新首页的新闻
        if (mMyChannelStrs.equals(mMyChannelStrs_Db)) {
            mFragment.mBinding.viewpager.setCurrentItem(position);
            finish();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消订阅 避免内存泄漏
        RxBus.getDefault().unregister(this);
        resetContentFts();
    }


    /**
     * 当频道管理器的我的频道发生变化(跟数据库存储的频道不一样)，
     * 重新设置我的频道 首页马上更新新闻频道
     * mMyChannelList_2 从ChannelAdapter动态获取数据
     */
    private void resetContentFts() {
        initMyChannelStrs();
        //只要当前显示我的频道 与 数据库中获取我的频道 不一样(频道的数量或者顺序不同)
        //动态更新首页的新闻
        if (!mMyChannelStrs.equals(mMyChannelStrs_Db)) {
            mFragment.setContentFts(mMyChannelList_2);
        }
    }
}
