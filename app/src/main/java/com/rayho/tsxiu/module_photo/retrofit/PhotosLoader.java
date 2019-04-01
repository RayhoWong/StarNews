package com.rayho.tsxiu.module_photo.retrofit;


import com.rayho.tsxiu.module_photo.bean.PhotoBean;
import com.rayho.tsxiu.utils.RxUtil;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class PhotosLoader {
    private PhotosApi helper;

    public PhotosLoader(){
        helper = PhotosServiceHelper.getInstance().create(PhotosApi.class);
    }

    private static class SingleHolder{
        private static PhotosLoader loader = new PhotosLoader();
    }

    public static PhotosLoader getInstance(){
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
     * 获取图虫的图片
     * @return
     */
    public Observable<PhotoBean> getPhotos(){
        return helper.getPhotos()
                .map(RxUtil.jsonTransform(PhotoBean.class))
                .onErrorResumeNext(RxUtil.<PhotoBean>throwableFunc())
                .compose(RxUtil.<PhotoBean>rxSchedulerHelper());
    }

}
