package com.goach.tabdemo.activity;


import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.goach.tabdemo.R;
import com.goach.tabdemo.bean.ChannelBean;
import com.goach.tabdemo.fragment.ChannelActivity;
import com.goach.tabdemo.fragment.LazyFragment;
import com.goach.tabdemo.fragment.OneFragment;
import com.goach.tabdemo.fragment.TwoFragment;
import com.goach.tabdemo.view.ZTabLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private ZTabLayout tabLayout;
    private ViewPager mViewPager;
    private MainActivity.MyViewPager myViewPagerAdapter;
    private String[] myStrs = new String[]{"推荐","热点","军事","图片","社会","娱乐","科技","体育","深圳","财经"};
    private List<ChannelBean> mDataList ;
    private ImageView mAddChannelEntryIv;
    private ChannelActivity mAddChannelFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_tab);
        initView();
        initData();
    }
    @Override
    public void onClick(View view) {
      /*  int i = view.getId();
        if (i == R.id.id_add_channel_entry_iv) {
            if (!mAddChannelFragment.isAdded()) {
                mAddChannelFragment.show(getSupportFragmentManager(), "addChannel");
            }

        }*/
    }
    private void initView(){
        tabLayout = (ZTabLayout) findViewById(R.id.id_tab_pager_indicator);
        mViewPager = (ViewPager) findViewById(R.id.id_view_Pager);
        mAddChannelEntryIv = (ImageView)findViewById(R.id.id_add_channel_entry_iv);
        mAddChannelEntryIv.setOnClickListener(this);
    }
    private void initData(){
        mDataList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            ChannelBean channelBean = new ChannelBean();
            channelBean.setTabName(myStrs[i]);
            channelBean.setTabType(i==0?0:i==1?1:2);
            mDataList.add(channelBean);
        }
        myViewPagerAdapter = new MainActivity.MyViewPager(getSupportFragmentManager(),mDataList);
        tabLayout.setDataList(mDataList);
        mViewPager.setAdapter(myViewPagerAdapter);
        tabLayout.setupWithViewPager(mViewPager);
        mAddChannelFragment = new ChannelActivity();

        tabLayout.showMsg(3,100,0);
        tabLayout.showMsg(2,0,0);
    }
    public void notifyTabDataChange(List<ChannelBean> list,int position){
        mDataList.clear();
        mDataList.addAll(list);
        myViewPagerAdapter.notifyDataSetChanged();
        tabLayout.setDataList(mDataList);
        tabLayout.notifyDataSetChanged();
        mViewPager.setCurrentItem(position);
    }
    public class MyViewPager extends FragmentStatePagerAdapter {
        private List<ChannelBean> mDataList;
        private Map<Integer,LazyFragment> baseFragmentMap = new HashMap<>();
        public MyViewPager(FragmentManager fm, List<ChannelBean> list) {
            super(fm);
            mDataList = list ;
        }

        @Override
        public Fragment getItem(int position) {
            LazyFragment fragment = baseFragmentMap.get(position);
            if(fragment==null){
                if(position%2==0)
                    fragment =  OneFragment.newInstance();
                else
                    fragment =  TwoFragment.newInstance(mDataList.get(position).getTabName());
                baseFragmentMap.put(position,fragment);
            }
            return fragment;
        }
        @Override
        public int getCount() {
            return mDataList.size();
        }
    }
}
