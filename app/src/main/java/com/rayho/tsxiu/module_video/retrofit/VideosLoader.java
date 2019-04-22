package com.rayho.tsxiu.module_video.retrofit;


import com.rayho.tsxiu.module_video.bean.VideoBean;
import com.rayho.tsxiu.utils.RxUtil;

import io.reactivex.Observable;

public class VideosLoader {
    private VideosApi helper;

    public VideosLoader() {
        helper = VideosServiceHelper.getInstance().create(VideosApi.class);
    }

    private static class SingleHolder {
        private static VideosLoader loader = new VideosLoader();
    }

    public static VideosLoader getInstance() {
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
     * 获取视频
     *start:分页值 首页刷新默认为10
     * @return
     */
    public Observable<VideoBean> getVideos(String start) {
        return helper.getVideos(start)
                .map(RxUtil.jsonTransform(VideoBean.class))
                .onErrorResumeNext(RxUtil.<VideoBean>throwableFunc())
                .compose(RxUtil.<VideoBean>rxSchedulerHelper());
    }



}


