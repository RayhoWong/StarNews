package com.rayho.tsxiu.module_video.retrofit;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

/**
 * Created by Rayho on 2019/3/30
 **/
public interface VideosApi {

    /**
     * ]
     * link = http://app.pearvideo.com/clt/jsp/v4/home.jsp
     * 下拉刷新获取视频
     * start:分页值=(默认加载第一页为1*10 = 10 以此类推 当前页码*10)
     *
     * @return
     */

    //方式三 (梨视频网络请求添加Header)
    //针对该接口添加Header参数
//    @Headers({
//            "X-Channel-Code: official",
//            "X-Client-Agent: htc_HTC 2Q4D200_8.0.0",
//            "X-Client-Hash: ecad6cc25c3157ac31b30d49a7d6d89a",
//            "X-Client-ID: 359984080315932",
//            "X-Client-Version: 5.3.1",
//            "X-IMSI: 46007",
//            "X-Location: 113.400437%2C23.121987%7C%E4%B8%AD%E5%9B%BD%2C%E5%B9%BF%E4%B8%9C%E7%9C%81%2C%E5%B9%BF%E5%B7%9E%E5%B8%82%2C%E5%A4%A9%E6%B2%B3%E5%8C%BA",
//            "X-Long-Token: ",
//            "X-Platform-Type: 2",
//            "X-Platform-Version: 8.0.0",
//            "X-Serial-Num: ",
//            "X-User-ID: "
//    })
    @GET("home.jsp?")
    Observable<ResponseBody> getVideos(@Query("start") String start);


}
