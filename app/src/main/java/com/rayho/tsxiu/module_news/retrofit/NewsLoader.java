package com.rayho.tsxiu.module_news.retrofit;


import com.rayho.tsxiu.module_news.bean.NewsBean;
import com.rayho.tsxiu.module_news.bean.NewsHotSearch;
import com.rayho.tsxiu.module_news.bean.NewsTypeBean;
import com.rayho.tsxiu.utils.RxUtil;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class NewsLoader{
    private NewsApi helper;
    private NewsApi helper2;

    public NewsLoader(){
        helper = NewsServiceHelper.getInstance().create(NewsApi.class);
        helper2 = NewsServiceHelper2.getInstance().create(NewsApi.class);
    }

    private static class SingleHolder{
        private static NewsLoader loader = new NewsLoader();
    }

    public static NewsLoader getInstance(){
        return SingleHolder.loader;
    }

//////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * 获取某部电影的详细信息
     * @return
     */
    /*public Observable<MovieDetailBean> getMovie(){
        return helper.getSearch()
                     //进行数据类型转换 返回将RespondBody类型转成具体GSON的类对象
                     .map(RxUtil.jsonTransform(MovieDetailBean.class))
                      //捕获异常 并且分发到观察者的onError中处理
                     .onErrorResumeNext(RxUtil.<MovieDetailBean>throwableFunc())
                     //进行线程切换
                     .compose(RxUtil.<MovieDetailBean>rxSchedulerHelper());
    }*/
//////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * 获取新闻的所有频道(类型)
     * @return
     */
    public Observable<NewsTypeBean> getChannels(){
        return helper.getChannels()
                .map(RxUtil.jsonTransform(NewsTypeBean.class))
                .onErrorResumeNext(RxUtil.<NewsTypeBean>throwableFunc())
                .compose(RxUtil.<NewsTypeBean>rxSchedulerHelper());
    }

    /**
     * 获取新闻
     * @param id 新闻类型
     * @param pageToken 分页值可以为null
     * @return
     */
    public Observable<NewsBean> getNews(String id,String pageToken){
        return helper.getNews(id,pageToken)
                     .map(RxUtil.jsonTransform(NewsBean.class))
                     .onErrorResumeNext(RxUtil.<NewsBean>throwableFunc())
                     .compose(RxUtil.<NewsBean>rxSchedulerHelper());
    }


    /**
     * 获取今日头条的热搜词条
     * @return
     */
    public Observable<NewsHotSearch> getHotSearch(){
        return helper2.getNewsHotSearch()
                .map(RxUtil.jsonTransform(NewsHotSearch.class))
                .onErrorResumeNext(RxUtil.<NewsHotSearch>throwableFunc())
                .subscribeOn(Schedulers.io());
    }


    /**
     * 根据关键字获取新闻
     * @param kw 关键字
     * @param pageToken 分页值（可null）
     * @return
     */
    public Observable<NewsBean> searchNews(String kw,String pageToken){
        return helper.searchNews(kw,pageToken)
                .map(RxUtil.jsonTransform(NewsBean.class))
                .onErrorResumeNext(RxUtil.<NewsBean>throwableFunc())
                .compose(RxUtil.<NewsBean>rxSchedulerHelper());
    }
}
