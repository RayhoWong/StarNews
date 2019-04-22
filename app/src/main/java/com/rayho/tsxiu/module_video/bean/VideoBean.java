package com.rayho.tsxiu.module_video.bean;

import java.util.List;

/**
 * Created by Rayho on 2019/4/11
 * 视频数据的bean
 **/
public class VideoBean {

    public String resultCode;
    public String resultMsg;
    public String reqId;
    public String systemTime;
    public String nextUrl;
    public List<AreaListBean> areaList;
    public List<DataListBean> dataList;

    public static class AreaListBean {
        public String area_id;
        public ExpInfoBean expInfo;

        public static class ExpInfoBean {
            public String algorighm_exp_id;
            public String front_exp_id;
            public String s_value;
        }
    }

    public static class DataListBean {
        public String nodeType;
        public String nodeName;
        public String moreId;
        public List<ContListBean> contList;

        public static class ContListBean {
            public String contId;
            public String name;
            public String pic;
            public UserInfoBean userInfo;
            public String link;
            public String linkType;
            public String isVr;
            public String aspectRatio;
            public String cornerLabel;
            public String cornerLabelDesc;
            public String forwordType;
            public String videoType;
            public String duration;
            public String liveStatus;
            public String postHtml;
            public String postId;
            public String commentTimes;
            public String summary;
            public String sharePic;
            public String shareUrl;
            public List<TagsBean> tags;
            public List<VideosBean> videos;
            public String praiseTimes;
            public String isFavorited;
            public String adExpMonitorUrl;
            public GeoBean geo;
            public String isVideoPlus;
            public String isDownload;
            public String adName;
            public String adLogo;



            public static class UserInfoBean {
                public String userId;
                public String nickname;
                public String pic;
                public String level;
                public String isFollow;
            }

            public static class GeoBean {
                public String namePath;
                public String showName;
                public String address;
                public String loc;
                public String placeName;
                public Double longitude;
                public Double latitude;
            }

            public static class TagsBean {
                public String tagId;
                public String name;
            }

            public static class VideosBean {
                public String videoId;
                public String url;
                public String name;
                public String desc;
                public String tag;
                public String format;
                public String fileSize;
                public String duration;
            }
        }
    }
}
