package com.rayho.tsxiu.base;

/**
 * Created by Rayho on 2019/1/24
 * 常用的常量
 **/
public class Constant {
    //第三方平台提供头条新闻的主页
    public static String IDATA_URL = "http://api01.idataapi.cn:8000/";

    //新闻的界面类型
    public static int TYPE_NO_PHOTO = 0;//只有文字
    public static int TYPE_SINGLE_PHOTO = 1;//只有一张图
    public static int TYPE_THREE_PHOTO = 2;//三张图
    public static int TYPE_MULTIPLE_PHOTO = 3;//多于三张图

    public static int REFRESH_DATA = 0;//下拉刷新
    public static int LOAD_MORE_DATA = 1;//上拉加载
}
