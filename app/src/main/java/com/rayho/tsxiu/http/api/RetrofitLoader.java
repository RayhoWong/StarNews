package com.rayho.tsxiu.http.api;


public class RetrofitLoader{
    private RetrofitApi helper;

    public RetrofitLoader(){
        helper = RetrofitServiceHelper.getInstance().create(RetrofitApi.class);
    }

    private static class SingleHolder{
        private static RetrofitLoader loader = new RetrofitLoader();
    }

    public static RetrofitLoader getInstance(){
        return SingleHolder.loader;
    }

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

   /* public Observable<NewsBean> getNews(){
        return helper.getNews()
                     .map(RxUtil.jsonTransform(NewsBean.class))
                     .onErrorResumeNext(RxUtil.<NewsBean>throwableFunc())
                     .compose(RxUtil.<NewsBean>rxSchedulerHelper());
    }*/
}
