package com.rayho.tsxiu.module_photo.retrofit;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;

/**
 * Created by Rayho on 2019/3/30
 **/
public interface PhotosApi {

    /**
     * 获取图片
     * @return
     */
    @GET("feed-app")
    Observable<ResponseBody> getPhotos();

}
