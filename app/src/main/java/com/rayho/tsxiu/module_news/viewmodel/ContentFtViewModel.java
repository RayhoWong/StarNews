package com.rayho.tsxiu.module_news.viewmodel;

import com.blankj.rxbus.RxBus;
import com.rayho.tsxiu.module_news.adapter.NewsAdapter;
import com.rayho.tsxiu.module_news.bean.NewsBean;
import com.rayho.tsxiu.module_news.retrofit.NewsLoader;

import java.util.List;
import java.util.Random;

import io.reactivex.Observable;

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


    /**
     * 返回NewsBean类型的Observable(已请求网络 得到数据)
     * @param cid 类别id
     * @param pageToken 分页id
     * @return
     */
    public Observable<NewsBean> getNewsObservable(String cid,String pageToken){
        return NewsLoader.getInstance().getNews(cid,pageToken);
    }

    /**
     * 根据数据的获取方式 为adapter设置新闻数据
     * @param list
     * @param refreshType 获取数据的方式（下拉/上拉）
     */
    public void setNews(List<NewsBean.DataBean> list , int refreshType){
        if(list != null && list.size() >= 0) mList = list;

        for(NewsBean.DataBean news : mList){
            if(news.imageUrls == null || news.imageUrls.size() == 0){
                news.type = 0;//无图
            }else if(news.imageUrls.size() == 1 || news.imageUrls.size() == 2){
                news.type = 1;//单图
            }else if(news.imageUrls.size() == 3){
                news.type = 2;//三图
            }else {
                news.type = 3;//组图
            }
        }

        switch (refreshType){
            case 0://下拉刷新
                RxBus.getDefault().post(mList.size(),"updateNums");
                mAdapter.setNews(mList);
                break;
            case 1://上拉加载
                for(int i=0;i<5;i++){
                    mList.remove(new Random().nextInt(mList.size()));
                }
                mAdapter.addItems(mList);
                break;
        }
    }
}
