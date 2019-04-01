package com.rayho.tsxiu.module_photo.retrofit;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.rayho.tsxiu.base.Constant;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author:Rayho
 * 提供一个retrofit实例，并且配置了okhttpclient和一些公共参数
 */
public class PhotosServiceHelper {
    //连接请求超时的时间s
    private static final int DEFAULT_CONNECT_TIME_OUT = 5;
    //读操作超时时间
    private static final int DEFAULT_READ_TIME_OUT = 10;
    //写操作超时时间
    private static final int DEFAULT_WRITE_TIME_OUT = 10;
    //retrofit实例
    private Retrofit mRetrofit;

    //私有构造函数 外部类中无法实例化
    private PhotosServiceHelper(){
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //设置请求超时的时间
        builder.connectTimeout(DEFAULT_CONNECT_TIME_OUT, TimeUnit.SECONDS);
        //设置读操作超时时间
        builder.readTimeout(DEFAULT_READ_TIME_OUT,TimeUnit.SECONDS);
        //设置写操作超时的时间
        builder.writeTimeout(DEFAULT_WRITE_TIME_OUT,TimeUnit.SECONDS);

        //添加公共参数拦截器
//        HttpCommonInterceptor interceptor = new HttpCommonInterceptor.Builder()
//                                 .addHeaderParams("token","123456kobe")
//                                 .build();
//        builder.addInterceptor(interceptor);


//         *HttpLoggingInterceptor（添加日志拦截器）
//         * 拦截器的主要作用在于显示网络传输的数据，在调试的时候，方便我们查看上传以及服务器返回的json内容
//         步骤一：添加依赖 ‘com.squareup.okhttp3:logging-interceptor:3.8.1’
//         步骤二：生成一个HttpLoggingInterceptor对象，并生成一个HttpLoggingInterceptor.Logger对象作为参数传进去；
//         步骤三：设置Level为HttpLoggingInterceptor.Level.BODY（最大权限）；
//         步骤四：重写log方法，直接把返回的String message打印出来；
        /*builder.addInterceptor(new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.d("HttpLog", message);
            }
        }).setLevel(HttpLoggingInterceptor.Level.BODY));*/

        //设置缓存路径
      /*  File cacheFile = new File(BaseApp.getContext().getCacheDir(), "caheData.txt");
        //设置缓存大小
        int cacheSize = 10*1024*1024;
        Cache cache = new Cache(cacheFile, cacheSize);*/
        //添加缓存拦截器
/*        builder.addInterceptor(new CacheIntercepter());
        builder.addNetworkInterceptor(new CacheIntercepter());
        builder.cache(cache);*/

        // 创建Retrofit
        mRetrofit = new Retrofit.Builder()
                .baseUrl(Constant.TUCHONG_URL)
                .client(builder.build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private static class SingleHolder{
        public static PhotosServiceHelper instance = new PhotosServiceHelper();
    }

    public static PhotosServiceHelper getInstance(){
        return SingleHolder.instance;
    }

    /**
     * 获取对应的Service
     * @param service Service 的 class
     * @param <T>
     * @return
     */
    public <T> T create(Class<T> service){
        return mRetrofit.create(service);
    }
}
