package com.rayho.tsxiu.module_news.viewmodel;

import com.blankj.rxbus.RxBus;
import com.rayho.tsxiu.module_news.adapter.NewsAdapter;
import com.rayho.tsxiu.module_news.bean.NewsBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Rayho on 2019/2/23
 * ContentFragmeht页面的VW 负责获取数据 处理业务逻辑
 **/
public class ContentFtViewModel {

    private List<NewsBean.DataBean> mList;

    private List<String> mImageUrlList;

    private NewsAdapter mAdapter;


    public ContentFtViewModel(NewsAdapter adapter) {
        mAdapter = adapter;
    }

    public void getNews(int refreshType){
        mList = new ArrayList<>();
        mImageUrlList = new ArrayList<>();
        mImageUrlList.add("http://p1-tt.bytecdn.cn/large/pgc-image/RGPKNSq3mtQzNc");
        mImageUrlList.add("http://p3-tt.bytecdn.cn/large/pgc-image/RGPKNT23AJbl4b");
        mImageUrlList.add("http://p1-tt.bytecdn.cn/large/pgc-image/RGPKNTFFrcz02F");
        mImageUrlList.add("http://p1-tt.bytecdn.cn/large/pgc-image/RGPKNTgH29XGii");
        mImageUrlList.add("http://p9-tt.bytecdn.cn/large/pgc-image/RGPKNTRLV9vvu");

        for(int i=0;i<10;i++){
            NewsBean.DataBean bean = new NewsBean.DataBean();
            bean.title = "今日头条在东莞松山湖设立研发中心，大量职位开放，快点来投递简历吧。";
            bean.posterScreenName = "广州日报";
            bean.imageUrls = mImageUrlList;
            bean.commentCount = i+1;
            bean.publishDate = 1548925892;
            bean.type = new Random().nextInt(4);
            mList.add(bean);
        }

        switch (refreshType){
            case 0:
                RxBus.getDefault().post(mList.size(),"updateNums");
                mAdapter.setNews(mList);
                break;
            case 1:
                for(int i=0;i<5;i++){
                    mList.remove(new Random().nextInt(mList.size()));
                }
                mAdapter.addItems(mList);
                break;
        }
    }
}
