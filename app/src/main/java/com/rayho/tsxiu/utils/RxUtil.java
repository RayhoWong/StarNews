package com.rayho.tsxiu.utils;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.rayho.tsxiu.http.exception.ExceptionEngine;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;


/**
 * rxjava的工具类
 */
public class RxUtil {

    private static Gson GSON = new GsonBuilder().serializeNulls().create();
    /**
     * 统一线程处理
     * @param <T>
     * @return
     */
    public static <T> ObservableTransformer<T, T> rxSchedulerHelper() {    //compose简化线程
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /**
     * 获取需要的数据实体类
     * @param <T>
     * @return
     */
    public static <T> Function<ResponseBody, T> jsonTransform(final Class<T> clazz) {
        return new Function<ResponseBody, T>() {
            @Override
            public T apply(@NonNull ResponseBody responseBody) throws Exception {
                String content = responseBody.string();
                //打印返回的json数据
                Logger.json(content);
                //                JSONObject jsonObject = new JSONObject(content);
                //                int code = jsonObject.getInt("code");
                //                String msg = jsonObject.getString("msg");
                //                if (code == 1) {//code=1(需要与后台约定) 证明请求成功 返回数据
                //                    //String data = jsonObject.getString("data");
                return GSON.fromJson(content, clazz);
                //                } else {
                //                    throw new ServerException(code, msg);
                //                }
            }
        };
    }

    /**
     * 将异常发送到最终Oberver的onError
     *
     * @param <T>
     */
    public static <T> Function<Throwable, Observable<T>> throwableFunc() {
        return new Function<Throwable, Observable<T>>() {
            @Override
            public Observable<T> apply(@NonNull Throwable throwable) throws Exception {
                return Observable.error(ExceptionEngine.handleException(throwable));
            }
        };
    }
}
