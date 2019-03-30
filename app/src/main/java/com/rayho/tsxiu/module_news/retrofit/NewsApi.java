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


    /**
     * 获取今日头条的热搜词条
     * @return
     */
    @GET("search/suggest/homepage_suggest/")
    Observable<ResponseBody> getNewsHotSearch();


    /**
     * 根据关键字kw获取新闻
     * @param kw 关键字
     * @return
     */
    @GET("news/toutiao?apikey=UVszcjmKFuxIOuo5qngQuUA1oWwTv45XPfso6g1LO9ISf9uqjBxvQicqbXpTIY95&")
    Observable<ResponseBody> searchNews(@Query("kw") String kw , @Query("pageToken") String pageToken);
}
