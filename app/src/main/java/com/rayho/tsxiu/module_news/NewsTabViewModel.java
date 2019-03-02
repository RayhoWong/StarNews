package com.rayho.tsxiu.module_news;



import com.rayho.tsxiu.module_news.bean.ChannelsBean;
import com.rayho.tsxiu.module_news.retrofit.NewsLoader;

import io.reactivex.Observable;

public class NewsTabViewModel{

    public NewsTabViewModel() {
    }

    public Observable<ChannelsBean> getChannelsObservable(){
        return NewsLoader.getInstance().getChannels();
    }

}
