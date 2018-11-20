package com.rayho.tsxiu.http.api;
// Created by Rayho on 2018/5/23.





import com.rayho.tsxiu.http.exception.ApiException;
import com.rayho.tsxiu.http.exception.ERROR;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * author: Rayho
 * date:   On 2018/5/23
 * 自定义Observer 用于包装错误
 */
public abstract class NetObserver<T> implements Observer<T>{

    @Override
    public void onError(Throwable e) {
        if(e instanceof ApiException){
            onError((ApiException)e);
        }else{
            ApiException exception = new ApiException(e, ERROR.NETWORD_ERROR);
            exception.setDisplayMessage("网络错误");
            onError(exception);
        }
    }

    @Override
    public void onComplete() {
    }

    @Override
    public void onSubscribe(Disposable d) {
    }

    public abstract void onError(ApiException ex);
}
