package com.rayho.tsxiu.module_news.viewmodel;


import com.rayho.tsxiu.module_news.bean.NewsHotSearch;
import com.rayho.tsxiu.module_news.retrofit.NewsLoader;

import io.reactivex.Observable;


public class NewsTabViewModel {

    public NewsTabViewModel() {}


    /**
     * 获取热搜词条
     * @return
     */
    public Observable<NewsHotSearch> getHotSearch(){
        return NewsLoader.getInstance().getHotSearch();
    }

}
