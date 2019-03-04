package com.rayho.tsxiu.module_news.retrofit;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Rayho on 2019/1/27
 **/
public interface NewsApi {
    /**
     * 获取所有分类(频道)
     * @return
     */
    @GET("catid/toutiao?apikey=UVszcjmKFuxIOuo5qngQuUA1oWwTv45XPfso6g1LO9ISf9uqjBxvQicqbXpTIY95&")
    Observable<ResponseBody> getChannels();
    /**
     * 根据分类id获取新闻
     * @param cid 分类id
     * @return
     */
    @GET("news/toutiao?apikey=UVszcjmKFuxIOuo5qngQuUA1oWwTv45XPfso6g1LO9ISf9uqjBxvQicqbXpTIY95&")
    Observable<ResponseBody> getNews(@Query("catid") String cid , @Query("pageToken") String pageToken);


}
