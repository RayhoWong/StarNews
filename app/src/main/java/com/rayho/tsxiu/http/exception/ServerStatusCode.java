package com.rayho.tsxiu.http.exception;

/**
 * Created by Rayho on 2019/3/1
 * 服务器返回的状态码
 **/
public class ServerStatusCode {

    /**
     * 根据状态码返回相应的信息
     * @param code 状态码
     * @return
     */
    public static String getStatusResponse(String code){
        switch (code){
            case "000000":
                return "请求成功";
            case "100002":
                return "目标参数搜索没结果";
            case "100000":
                return "服务器内部错误";
            case "100001":
                return "网络错误";
            case "100004":
                return "目标服务器错误";
            case "100005":
                return "用户输入参数错误";
            case "100301":
                return "用户帐号不存在";
            case "100700":
                return "授权失败";
            case "100701":
                return "您的当前API已停用";
            case "100702":
                return "您的账户已停用";
            case "100703":
                return "并发已达上限";
            case "100704":
                return "API维护中";
            case "100705":
                return "API不存在";
            case "100706":
                return "请先添加api";
            case "100707":
                return "调用次数超限";
            case "100802":
                return "请求路径错误或者缺少\"time\"参数";
            case "100803":
                return "参数pageToken有误";
        }
        return "后台服务器异常";
    }
}
