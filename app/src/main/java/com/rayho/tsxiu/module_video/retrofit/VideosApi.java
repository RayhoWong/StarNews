package com.rayho.tsxiu.module_video.retrofit;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Rayho on 2019/3/30
 **/
public interface VideosApi {

    /**]
     * link = http://app.pearvideo.com/clt/jsp/v4/home.jsp
     * 下拉刷新获取视频
     * start:分页值=(默认加载第一页为1*10 = 10 以此类推 当前页码*10)
     * @return
     */
    @GET("home.jsp?")
    Observable<ResponseBody> getVideos(@Query("start") String start);


    /**
     * link = http://app.pearvideo.com/clt/jsp/v4/home.jsp?start=10
     * 上拉加载获取视频
     * start:分页值=(默认加载第一页为1*10 = 10 以此类推 当前页码*10)
     * @return
     */
    @GET("home.jsp?")
    Observable<ResponseBody> getVideosByLoadmore(@Query("start") String start);

}
