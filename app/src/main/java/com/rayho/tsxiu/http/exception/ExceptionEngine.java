package com.rayho.tsxiu.http.exception;

import com.google.gson.JsonParseException;
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;
import com.rayho.tsxiu.app.AppApplication;
import com.rayho.tsxiu.utils.NetworkUtils;

import org.json.JSONException;

import java.net.ConnectException;
import java.text.ParseException;


/**
 * 规定所有异常的类型
 * 最终返回一个自定义异常ApiException
 */
public class ExceptionEngine {

    //对应HTTP的状态码
    private static final int UNAUTHORIZED = 401;
    private static final int FORBIDDEN = 403;
    private static final int NOT_FOUND = 404;
    private static final int REQUEST_TIMEOUT = 408;
    private static final int INTERNAL_SERVER_ERROR = 500;
    private static final int BAD_GATEWAY = 502;
    private static final int SERVICE_UNAVAILABLE = 503;
    private static final int GATEWAY_TIMEOUT = 504;

    public static ApiException handleException(Throwable e) {
        ApiException ex;
        if (e instanceof HttpException) {             //HTTP错误
            HttpException httpException = (HttpException) e;
            ex = new ApiException(e, ERROR.HTTP_ERROR);
            switch (httpException.code()) {
                case UNAUTHORIZED:
                case FORBIDDEN:
                case NOT_FOUND:
                case REQUEST_TIMEOUT:
                case GATEWAY_TIMEOUT:
                case INTERNAL_SERVER_ERROR:
                case BAD_GATEWAY:
                case SERVICE_UNAVAILABLE:
                default:
                    ex.setDisplayMessage("网络错误: " + e.toString());  //均视为网络错误
                    break;
            }
            return ex;
        } else if (e instanceof ServerException) {    //服务器返回的错误
            ServerException resultException = (ServerException) e;
            ex = new ApiException(resultException, resultException.getCode());
            ex.setDisplayMessage(resultException.getMsg());
            return ex;
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {
            ex = new ApiException(e, ERROR.PARSE_ERROR);
            ex.setDisplayMessage("JASON解析错误: " + e.toString());//均视为解析错误
            return ex;
        } else if (e instanceof ConnectException) {
            ex = new ApiException(e, ERROR.NETWORD_ERROR);
            ex.setDisplayMessage("连接失败");  //均视为网络错误
            return ex;
        } else if (e instanceof javax.net.ssl.SSLHandshakeException) {
            ex = new ApiException(e, ERROR.SSL_ERROR);
            ex.setDisplayMessage("证书验证失败");
            return ex;
        } else if (!NetworkUtils.isAvailable(AppApplication.getAppApplication())) {
            ex = new ApiException(e, ERROR.HTTP_ERROR);
            ex.setDisplayMessage("网络未连接");//
            return ex;
        } else {
            ex = new ApiException(e, ERROR.UNKNOWN);
            ex.setDisplayMessage("网络错误");//将所有的未知的错误归于网络错误
            return ex;
        }
    }
}
