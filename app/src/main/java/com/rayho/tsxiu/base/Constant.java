package com.rayho.tsxiu.base;

/**
 * Created by Rayho on 2019/1/24
 * 常用的常量
 **/
public class Constant {
    //第三方平台提供头条新闻的主页和APIKEY
    public static String IDATA_URL = "http://api01.idataapi.cn:8000/";
    public static String API_KEY = "UVszcjmKFuxIOuo5qngQuUA1oWwTv45XPfso6g1LO9ISf9uqjBxvQicqbXpTIY95&";
    //今日头条主页
    public static String TOUTIAO_URL = "http://lf.snssdk.com/";
    //图虫主页
    public static String TUCHONG_URL = "https://api.tuchong.com/";
    //图虫图片主页
    public static String TUCHONG_PHOTO_URL = "https://photo.tuchong.com/";
    //梨视频主页
    public static String PEAR_VIDEO_URL = "http://app.pearvideo.com/clt/jsp/v4/";
    //分类id
    public static final String TYPE_ID = "tag";
    //新闻的界面类型
    public static int TYPE_NO_PHOTO = 0;//只有文字
    public static int TYPE_SINGLE_PHOTO = 1;//只有一张图
    public static int TYPE_THREE_PHOTO = 2;//三张图
    public static int TYPE_MULTIPLE_PHOTO = 3;//多于三张图
    //获取数据方式
    public static int REFRESH_DATA = 0;//下拉刷新
    public static int LOAD_MORE_DATA = 1;//上拉加载
    //新闻缓存的间隔
    public static long CACHE_TIME = 2000;
    //缓存新闻数据的最大值
    public static int MAX_CACHE_NUMS = 10;
    //打开相机的请求码
    public static int REQUEST_CODE_SCAN = 0000;
    //图片获取的最大数量
    public static int MAX_PHOTO_NUMS = 10;
    //默认用户的id
    public static String DEFAULT_USER_ID = "100";
    //默认用户名
    public static String DEFAULT_USER_NAME = "Koo";
    //默认用户头像
    public static String DEFAULT_USER_FACE = "https://photo.tuchong.com/1673709/f/25389444.jpg";
}
