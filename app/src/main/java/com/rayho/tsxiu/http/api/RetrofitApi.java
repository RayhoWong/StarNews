package com.rayho.tsxiu.http.api;



import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;

/**
 * 获取某部电影的详细信息
 */
public interface RetrofitApi {

    @GET("subject/24773958")
    Observable<ResponseBody> getMovie();





}
