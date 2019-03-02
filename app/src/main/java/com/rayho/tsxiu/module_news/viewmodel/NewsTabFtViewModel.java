package com.rayho.tsxiu.module_news.viewmodel;



import com.rayho.tsxiu.module_news.bean.ChannelsBean;
import com.rayho.tsxiu.module_news.retrofit.NewsLoader;

import io.reactivex.Observable;

public class NewsTabFtViewModel {

    public NewsTabFtViewModel() {
    }

    public Observable<ChannelsBean> getChannelsObservable(){
        return NewsLoader.getInstance().getChannels();
    }

}
