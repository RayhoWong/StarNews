package com.rayho.tsxiu.module_video.retrofit;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.rayho.tsxiu.base.Constant;
import com.rayho.tsxiu.http.Intercepter.HttpCommonInterceptor;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author:Rayho 提供一个retrofit实例，并且配置了okhttpclient和一些公共参数
 */
public class VideosServiceHelper {
    //连接请求超时的时间s
    private static final int DEFAULT_CONNECT_TIME_OUT = 5;
    //读操作超时时间
    private static final int DEFAULT_READ_TIME_OUT = 10;
    //写操作超时时间
    private static final int DEFAULT_WRITE_TIME_OUT = 10;
    //retrofit实例
    private Retrofit mRetrofit;

    //私有构造函数 外部类中无法实例化
    private VideosServiceHelper() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //设置请求超时的时间
        builder.connectTimeout(DEFAULT_CONNECT_TIME_OUT, TimeUnit.SECONDS);
        //设置读操作超时时间
        builder.readTimeout(DEFAULT_READ_TIME_OUT, TimeUnit.SECONDS);
        //设置写操作超时的时间
        builder.writeTimeout(DEFAULT_WRITE_TIME_OUT, TimeUnit.SECONDS);

        //方式一
        //统一添加Header参数拦截器
        //为梨视频网络请求添加Header
     /*   HttpCommonInterceptor interceptor = new HttpCommonInterceptor.Builder()
                .addHeaderParams("X-Channel-Code", "official")
                .addHeaderParams("X-Client-Agent", "htc_HTC 2Q4D200_8.0.0")
                .addHeaderParams("X-Client-Hash", "ecad6cc25c3157ac31b30d49a7d6d89a")
                .addHeaderParams("X-Client-ID", "359984080315932")
                .addHeaderParams("X-Client-Version", "5.3.1")
                .addHeaderParams("X-IMSI", "46007")
                .addHeaderParams("X-Location", "")
                .addHeaderParams("X-Long-Token", "")
                .addHeaderParams("X-Platform-Type", "2")
                .addHeaderParams("X-Platform-Version", "8.0.0")
                .addHeaderParams("X-Serial-Num", "")
                .addHeaderParams("X-User-ID", "")
                .build();
        builder.addInterceptor(interceptor);*/

        //方式二
        //统一添加Header参数拦截器
        //为梨视频网络请求添加Header
        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {
                Request original = chain.request();
                Request request = original.newBuilder()
                        .header("X-Channel-Code", "official")
                        .header("X-Client-Agent", "htc_HTC 2Q4D200_8.0.0")
                        .header("X-Client-Hash", "ecad6cc25c3157ac31b30d49a7d6d89a")
                        .header("X-Client-ID", "359984080315932")
                        .header("X-Client-Version", "5.3.1")
                        .header("X-IMSI", "46007")
                        .header("X-Location", "")
                        .header("X-Long-Token", "")
                        .header("X-Platform-Type", "2")
                        .header("X-Platform-Version", "8.0.0")
                        .header("X-Serial-Num", "")
                        .header("X-User-ID", "")
                        .method(original.method(), original.body())
                        .build();

                return chain.proceed(request);
            }
        });

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
                .baseUrl(Constant.PEAR_VIDEO_URL)
                .client(builder.build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private static class SingleHolder {
        public static VideosServiceHelper instance = new VideosServiceHelper();
    }

    public static VideosServiceHelper getInstance() {
        return SingleHolder.instance;
    }

    /**
     * 获取对应的Service
     *
     * @param service Service 的 class
     * @param <T>
     * @return
     */
    public <T> T create(Class<T> service) {
        return mRetrofit.create(service);
    }
}
