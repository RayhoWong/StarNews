package com.rayho.tsxiu.module_news.viewmodel;

import com.blankj.rxbus.RxBus;
import com.rayho.tsxiu.module_news.adapter.NewsAdapter;
import com.rayho.tsxiu.module_news.bean.NewsBean;
import com.rayho.tsxiu.module_news.bean.NewsHotSearch;
import com.rayho.tsxiu.module_news.retrofit.NewsLoader;

import java.util.List;

import androidx.databinding.ObservableField;
import io.reactivex.Observable;

/**
 * Created by Rayho on 2019/3/22
 **/
public class SearchViewModel {

    public ObservableField<String> content = new ObservableField<>();

    private List<NewsBean.DataBean> mList;

    private NewsAdapter mAdapter;


    public SearchViewModel(String content,NewsAdapter adapter) {
        this.content.set(content);
        mAdapter = adapter;
    }


    /**
     * 获取热搜词条
     * @return
     */
    public Observable<NewsHotSearch> getHotSearch(){
        return NewsLoader.getInstance().getHotSearch();
    }


    /**
     * 返回NewsBean类型的Observable(已请求网络 得到数据)
     * @param kw 关键字
     * @param pageToken 分页id
     * @return
     */
    public Observable<NewsBean> getNewsObservable(String kw, String pageToken){
        return NewsLoader.getInstance().searchNews(kw,pageToken);
    }


    /**
     * 根据新闻的图片数量 设置类型
     * 为adapter设置新闻数据
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
                RxBus.getDefault().postSticky(mList.size(),"updateNums");
                mAdapter.setNews(mList);
                break;
            case 1://上拉加载
                mAdapter.addItems(mList);
                break;
        }
    }


}
