package com.goach.tabdemo.fragment;

import android.os.Bundle;

import com.goach.tabdemo.APPConst;
import com.goach.tabdemo.R;
import com.goach.tabdemo.activity.MainActivity;
import com.goach.tabdemo.adapter.ChannelAdapter;
import com.goach.tabdemo.base.IChannelType;
import com.goach.tabdemo.bean.ChannelBean;
import com.goach.tabdemo.util.GridItemDecoration;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by 钟光新 on 2016/9/10 0010.
 */
public class ChannelActivity extends AppCompatActivity implements ChannelAdapter.ChannelItemClickListener {
    private RecyclerView mRecyclerView;
    private ChannelAdapter mRecyclerAdapter;
    private MainActivity mainActivity;
    private String[] myStrs = new String[]{"热门","关注","技术","科技","商业","互联网","涨知识","时尚"};
    private String[] recStrs = new String[]{"设计","天文","美食","星座","历史","消费维权","体育","明星八卦"};
    private List<ChannelBean> mMyChannelList;
    private List<ChannelBean> mRecChannelList ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_edit);

        initView();
        initData();

        mainActivity = new MainActivity();
        mRecyclerAdapter = new ChannelAdapter(this,mRecyclerView,mMyChannelList,mRecChannelList,1,1);
        mRecyclerAdapter.setChannelItemClickListener(this);
        mRecyclerView.setAdapter(mRecyclerAdapter);
    }

    private void initView(){
        mRecyclerView = findViewById(R.id.id_tab_recycler_view);
        GridLayoutManager gridLayout = new GridLayoutManager(this,4);
        gridLayout.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                boolean isHeader = mRecyclerAdapter.getItemViewType(position)== IChannelType.TYPE_MY_CHANNEL_HEADER||
                        mRecyclerAdapter.getItemViewType(position)== IChannelType.TYPE_REC_CHANNEL_HEADER;
                return isHeader?4:1;
            }
        });
        mRecyclerView.setLayoutManager(gridLayout);
        mRecyclerView.addItemDecoration(new GridItemDecoration(APPConst.ITEM_SPACE));
    }

    private void initData(){
        mMyChannelList = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            ChannelBean channelBean = new ChannelBean();
            channelBean.setTabName(myStrs[i]);
            channelBean.setTabType(i==0?0:i==1?1:2);
            mMyChannelList.add(channelBean);
        }
        mRecChannelList = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            ChannelBean channelBean = new ChannelBean();
            channelBean.setTabName(recStrs[i]);
            channelBean.setTabType(2);
            mRecChannelList.add(channelBean);
        }
    }

    @Override
    public void onChannelItemClick(List<ChannelBean> list,int position) {
        /*if(mainActivity!=null){
            mainActivity.notifyTabDataChange(list,position);
        }*/
    }
}
