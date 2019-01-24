package com.rayho.tsxiu.module_news.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Rayho on 2019/1/24
 **/
public class NewsBean {

    /**
     * message : success
     * data:["content":{}]
     * total_number : 19
     * has_more : true
     * login_status : 0
     * show_et_status : 0
     * post_content_hint : 分享今日新鲜事
     * has_more_to_refresh : true
     * action_to_last_stick : 0
     * feed_flag : 0
     * tips : {"type":"app","display_duration":2,"display_info":"今日头条推荐引擎有19条更新","display_template":"今日头条推荐引擎有%s条更新","open_url":"","web_url":"","download_url":"","app_name":"今日头条","package_name":""}
     * hide_topcell_count : 0
     */

    private String message;
    private int total_number;
    private boolean has_more;
    private int login_status;
    private int show_et_status;
    private String post_content_hint;
    private boolean has_more_to_refresh;
    private int action_to_last_stick;
    private int feed_flag;
    private TipsBean tips;
    private int hide_topcell_count;
    private List<DataBean> data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getTotal_number() {
        return total_number;
    }

    public void setTotal_number(int total_number) {
        this.total_number = total_number;
    }

    public boolean isHas_more() {
        return has_more;
    }

    public void setHas_more(boolean has_more) {
        this.has_more = has_more;
    }

    public int getLogin_status() {
        return login_status;
    }

    public void setLogin_status(int login_status) {
        this.login_status = login_status;
    }

    public int getShow_et_status() {
        return show_et_status;
    }

    public void setShow_et_status(int show_et_status) {
        this.show_et_status = show_et_status;
    }

    public String getPost_content_hint() {
        return post_content_hint;
    }

    public void setPost_content_hint(String post_content_hint) {
        this.post_content_hint = post_content_hint;
    }

    public boolean isHas_more_to_refresh() {
        return has_more_to_refresh;
    }

    public void setHas_more_to_refresh(boolean has_more_to_refresh) {
        this.has_more_to_refresh = has_more_to_refresh;
    }

    public int getAction_to_last_stick() {
        return action_to_last_stick;
    }

    public void setAction_to_last_stick(int action_to_last_stick) {
        this.action_to_last_stick = action_to_last_stick;
    }

    public int getFeed_flag() {
        return feed_flag;
    }

    public void setFeed_flag(int feed_flag) {
        this.feed_flag = feed_flag;
    }

    public TipsBean getTips() {
        return tips;
    }

    public void setTips(TipsBean tips) {
        this.tips = tips;
    }

    public int getHide_topcell_count() {
        return hide_topcell_count;
    }

    public void setHide_topcell_count(int hide_topcell_count) {
        this.hide_topcell_count = hide_topcell_count;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class TipsBean {
        /**
         * type : app
         * display_duration : 2
         * display_info : 今日头条推荐引擎有19条更新
         * display_template : 今日头条推荐引擎有%s条更新
         * open_url :
         * web_url :
         * download_url :
         * app_name : 今日头条
         * package_name :
         */

        private String type;
        private int display_duration;
        private String display_info;
        private String display_template;
        private String open_url;
        private String web_url;
        private String download_url;
        private String app_name;
        private String package_name;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getDisplay_duration() {
            return display_duration;
        }

        public void setDisplay_duration(int display_duration) {
            this.display_duration = display_duration;
        }

        public String getDisplay_info() {
            return display_info;
        }

        public void setDisplay_info(String display_info) {
            this.display_info = display_info;
        }

        public String getDisplay_template() {
            return display_template;
        }

        public void setDisplay_template(String display_template) {
            this.display_template = display_template;
        }

        public String getOpen_url() {
            return open_url;
        }

        public void setOpen_url(String open_url) {
            this.open_url = open_url;
        }

        public String getWeb_url() {
            return web_url;
        }

        public void setWeb_url(String web_url) {
            this.web_url = web_url;
        }

        public String getDownload_url() {
            return download_url;
        }

        public void setDownload_url(String download_url) {
            this.download_url = download_url;
        }

        public String getApp_name() {
            return app_name;
        }

        public void setApp_name(String app_name) {
            this.app_name = app_name;
        }

        public String getPackage_name() {
            return package_name;
        }

        public void setPackage_name(String package_name) {
            this.package_name = package_name;
        }
    }

    public static class DataBean {
        /**
         * content : {"abstract":"回望历史，总有一些关键节点值得铭记。5年前，党的十八届三中全会作出全面深化改革重大战略决策。5年来，以习近平同志为核心的党中央以巨大的政治勇气和智慧，更有力的措施和办法推进全面深化改革。","action_list":[{"action":1,"desc":"","extra":{}},{"action":3,"desc":"","extra":{}},{"action":7,"desc":"","extra":{}},{"action":9,"desc":"","extra":{}}],"aggr_type":1,"allow_download":false,"article_sub_type":0,"article_type":0,"article_url":"http://toutiao.com/group/6649875310391067143/","article_version":0,"ban_comment":0,"behot_time":1548310308,"bury_count":0,"cell_flag":262155,"cell_layout_style":1,"cell_type":0,"comment_count":351,"content_decoration":"","cursor":1548310308000,"digg_count":881,"display_url":"http://toutiao.com/group/6649875310391067143/","filter_words":[{"id":"8:0","is_selected":false,"name":"看过了"},{"id":"9:1","is_selected":false,"name":"内容太水"},{"id":"5:575762286","is_selected":false,"name":"拉黑作者:学习进行时"},{"id":"1:1733187203","is_selected":false,"name":"不想看:十九大"}],"forward_info":{"forward_count":46},"group_id":6649875310391067143,"has_m3u8_video":false,"has_mp4_video":0,"has_video":false,"hot":0,"ignore_web_transform":1,"interaction_data":"","is_stick":true,"is_subject":false,"item_id":6649875310391067143,"item_version":0,"keywords":"深化改革领导小组,学习进行时,华盛顿州,习近平,博鳌亚洲论坛,永无止境","label":"置顶","label_extra":{"icon_url":{},"is_redirect":false,"redirect_url":"","style_type":0},"label_style":1,"level":0,"log_pb":{"impr_id":"201901241411480100080590389837FA3","is_following":"0"},"media_info":{"avatar_url":"http://p1.pstatp.com/large/2bd5002833a717e5f769","follow":false,"is_star_user":false,"media_id":1571589787106305,"name":"学习进行时","recommend_reason":"","recommend_type":0,"user_id":62213436706,"user_verified":true,"verified_content":""},"media_name":"学习进行时","need_client_impr_recycle":1,"publish_time":1548294764,"read_count":127500,"repin_count":1076,"rid":"201901241411480100080590389837FA3","share_count":590,"share_info":{"cover_image":null,"description":null,"on_suppress":0,"share_type":{"pyq":0,"qq":0,"qzone":0,"wx":0},"share_url":"http://m.toutiao.com/a6649875310391067143/?iid=0\u0026app=news_article\u0026is_hit_share_recommend=0","title":"五年来，习近平这样论述“全面深化改革”","token_type":1,"weixin_cover_image":{"height":906,"uri":"large/tos-cn-i-0000/c08a1ac0-1f7a-11e9-aaa2-ac1f6b0aff74","url":"http://p1-tt.bytecdn.cn/large/tos-cn-i-0000/c08a1ac0-1f7a-11e9-aaa2-ac1f6b0aff74","url_list":[{"url":"http://p1-tt.bytecdn.cn/large/tos-cn-i-0000/c08a1ac0-1f7a-11e9-aaa2-ac1f6b0aff74"},{"url":"http://p3-tt.bytecdn.cn/large/tos-cn-i-0000/c08a1ac0-1f7a-11e9-aaa2-ac1f6b0aff74"},{"url":"http://p1-tt.bytecdn.cn/large/tos-cn-i-0000/c08a1ac0-1f7a-11e9-aaa2-ac1f6b0aff74"}],"width":1125}},"share_url":"http://m.toutiao.com/a6649875310391067143/?iid=0\u0026app=news_article\u0026is_hit_share_recommend=0","show_dislike":false,"show_portrait":false,"show_portrait_article":false,"source":"学习进行时","source_icon_style":4,"source_open_url":"sslocal://profile?uid=62213436706","stick_label":"置顶","stick_style":1,"tag":"news_politics","tag_id":6649875310391067143,"tip":0,"title":"五年来，习近平这样论述“全面深化改革”","ugc_recommend":{"activity":"","reason":"新华网学习进行时官方账号"},"url":"http://toutiao.com/group/6649875310391067143/","user_info":{"avatar_url":"http://p1.pstatp.com/thumb/2bd5002833a717e5f769","description":"新华社宣传报道习近平总书记治国理政新理念新思想新战略主要平台","follow":false,"follower_count":0,"live_info_type":1,"name":"学习进行时","schema":"sslocal://profile?uid=62213436706\u0026refer=all","user_auth_info":"{\"auth_type\": \"0\", \"auth_info\": \"新华网学习进行时官方账号\"}","user_id":62213436706,"user_verified":true,"verified_content":"新华网学习进行时官方账号"},"user_repin":0,"user_verified":1,"verified_content":"新华网学习进行时官方账号","video_style":0}
         * code :
         */
        private ContentBean content;
        private String code;

        public ContentBean getContent() {
            return content;
        }

        public void setContent(ContentBean content) {
            this.content = content;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public static class ContentBean {
            /**
             * abstract : 1月23日下午，习近平总书记主持召开中央全面深化改革委员会第六次会议并发表重要讲话。总书记强调，要对标到2020年在重要领域和关键环节改革上取得决定性成果，继续打硬仗，啃硬骨头，确保干一件成一件。
             * action_list : [{"action":1,"desc":"","extra":{}},{"action":3,"desc":"","extra":{}},{"action":7,"desc":"","extra":{}},{"action":9,"desc":"","extra":{}}]
             * aggr_type : 1
             * allow_download : false
             * article_sub_type : 0
             * article_type : 0
             * article_url : http://toutiao.com/group/6649730188609323534/
             * article_version : 1
             * ban_comment : 0
             * behot_time : 1548309858
             * bury_count : 0
             * cell_flag : 262155
             * cell_layout_style : 1
             * cell_type : 0
             * comment_count : 360
             * content_decoration :
             * control_panel : {"recommend_sponsor":{"icon_url":"http://p3-tt.bytecdn.cn/origin/13ef000096960314fff4","label":"帮上头条","night_icon_url":"http://p3-tt.bytecdn.cn/origin/dc1d0001ad958473e24b","target_url":"https://i.snssdk.com/ad/pgc_promotion/mobile/create/?group_id=6649730188609323534&item_id=6649730188609323534"}}
             * cursor : 1548309858000
             * digg_count : 1392
             * display_url : http://toutiao.com/group/6649730188609323534/
             * filter_words : [{"id":"8:0","is_selected":false,"name":"看过了"},{"id":"9:1","is_selected":false,"name":"内容太水"},{"id":"5:2338728378","is_selected":false,"name":"拉黑作者:央视快评"},{"id":"1:1641","is_selected":false,"name":"不想看:时政"},{"id":"6:21054","is_selected":false,"name":"不想看:cctv"}]
             * forward_info : {"forward_count":0}
             * group_id : 6649730188609323534
             * has_m3u8_video : false
             * has_mp4_video : 0
             * has_video : true
             * hot : 0
             * ignore_web_transform : 1
             * interaction_data :
             * is_stick : true
             * is_subject : false
             * item_id : 6649730188609323534
             * item_version : 0
             * keywords : 三中全会,深化改革,习近平,确保干,硬骨头
             * label : 置顶
             * label_extra : {"icon_url":{},"is_redirect":false,"redirect_url":"","style_type":0}
             * label_style : 1
             * level : 0
             * log_pb : {"impr_id":"201901241411480100080590389837FA3","is_following":"0"}
             * media_info : {"avatar_url":"http://p1.pstatp.com/large/6eed00053499a4aae20b","follow":false,"is_star_user":false,"media_id":1601422829470724,"name":"央视快评","recommend_reason":"","recommend_type":0,"user_id":99138739220,"user_verified":true,"verified_content":""}
             * media_name : 央视快评
             * need_client_impr_recycle : 1
             * publish_time : 1548260959
             * read_count : 168303
             * repin_count : 1236
             * rid : 201901241411480100080590389837FA3
             * share_count : 938
             * share_info : {"cover_image":null,"description":null,"on_suppress":0,"share_type":{"pyq":0,"qq":0,"qzone":0,"wx":0},"share_url":"http://m.toutiao.com/a6649730188609323534/?iid=0&app=news_article&is_hit_share_recommend=0","title":"「央视快评」确保干一件成一件","token_type":1,"weixin_cover_image":{"height":981,"uri":"large/tos-cn-i-0000/a8da58e2-1f2d-11e9-88d4-ac1f6b0ace98","url":"http://p1-tt.bytecdn.cn/large/tos-cn-i-0000/a8da58e2-1f2d-11e9-88d4-ac1f6b0ace98","url_list":[{"url":"http://p1-tt.bytecdn.cn/large/tos-cn-i-0000/a8da58e2-1f2d-11e9-88d4-ac1f6b0ace98"},{"url":"http://p9-tt.bytecdn.cn/large/tos-cn-i-0000/a8da58e2-1f2d-11e9-88d4-ac1f6b0ace98"},{"url":"http://p1-tt.bytecdn.cn/large/tos-cn-i-0000/a8da58e2-1f2d-11e9-88d4-ac1f6b0ace98"}],"width":800}}
             * share_type : 2
             * share_url : http://m.toutiao.com/a6649730188609323534/?iid=0&app=news_article&is_hit_share_recommend=0
             * show_dislike : false
             * show_portrait : false
             * show_portrait_article : false
             * source : 央视快评
             * source_icon_style : 1
             * source_open_url : sslocal://profile?refer=video&uid=99138739220
             * stick_label : 置顶
             * stick_style : 1
             * tag : news_politics
             * tag_id : 6649730188609323534
             * tip : 0
             * title : 「央视快评」确保干一件成一件
             * ugc_recommend : {"activity":"","reason":"央视快评官方账号"}
             * url : http://toutiao.com/group/6649730188609323534/
             * user_info : {"avatar_url":"http://p1.pstatp.com/thumb/6eed00053499a4aae20b","description":"中央电视台新闻中心精心打造的时政评论品牌。","follow":false,"follower_count":0,"live_info_type":1,"name":"央视快评","schema":"sslocal://profile?uid=99138739220&refer=video","user_auth_info":"{\"auth_type\":\"0\",\"auth_info\":\"央视快评官方账号\"}","user_id":99138739220,"user_verified":true,"verified_content":"央视快评官方账号"}
             * user_repin : 0
             * user_verified : 1
             * verified_content : 央视快评官方账号
             * video_style : 0
             */
            @SerializedName("abstract")
            private String new_abstract;
            private int aggr_type;
            private boolean allow_download;
            private int article_sub_type;
            private int article_type;
            private String article_url;
            private int article_version;
            private int ban_comment;
            private int behot_time;
            private int bury_count;
            private int cell_flag;
            private int cell_layout_style;
            private int cell_type;
            private int comment_count;
            private String content_decoration;
            private ControlPanelBean control_panel;
            private long cursor;
            private int digg_count;
            private String display_url;
            private ForwardInfoBean forward_info;
            private long group_id;
            private boolean has_image;
            private boolean has_m3u8_video;
            private int has_mp4_video;
            private boolean has_video;
            private int hot;
            private int ignore_web_transform;
            private String interaction_data;
            private boolean is_stick;
            private boolean is_subject;
            private long item_id;
            private int item_version;
            private String keywords;
            private String label;
            private LabelExtraBean label_extra;
            private int label_style;
            private int level;
            private LogPbBean log_pb;
            private MediaInfoBean media_info;
            private String media_name;
            private int need_client_impr_recycle;
            private int publish_time;
            private int read_count;
            private int repin_count;
            private String rid;
            private int share_count;
            private ShareInfoBean share_info;
            private int share_type;
            private String share_url;
            private boolean show_dislike;
            private boolean show_portrait;
            private boolean show_portrait_article;
            private String source;
            private int source_icon_style;
            private String source_open_url;
            private String stick_label;
            private int stick_style;
            private String tag;
            private long tag_id;
            private int tip;
            private String title;
            private UgcRecommendBean ugc_recommend;
            private String url;
            private UserInfoBean user_info;
            private int user_repin;
            private int user_verified;
            private String verified_content;
            private int video_style;
            private List<ActionListBean> action_list;
            private List<FilterWordsBean> filter_words;
            private List<ImageListBean> image_list;
            private MiddleImageBean middle_image;
            private List<LargeImageListBean> large_image_list;
            private ActionExtraBean action_extra;
            private int group_flags;//"group_flags": 32833,
            //"play_auth_token": "HMAC-SHA1:2.0:1548399931769433169:bab42eac5b9e4a8e
            //                     b25a91fc371ad533:Px58lDAahhvMBjx02gXYRqVokDU="
            private String play_auth_token;
            //"play_biz_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1NDgzOTk5MzEsInZlciI6InYxIiwiYWsiOiJiYWI0MmVhYzViOWU0YThlYjI1YTkxZmMzNzFhZDUzM
            //                   yIsInN1YiI6InBnY18xMDgwcCJ9.HUJmhlno1ykbyMxrFFBN8BSe91Oo8s6XiaxNpqREAj4",
            private String play_biz_token;
            private VideoDetailInfoBean video_detail_info;
            private int video_duration;
            private String video_id;
            private Double video_proportion_article;


            public int getVideo_duration() {
                return video_duration;
            }

            public void setVideo_duration(int video_duration) {
                this.video_duration = video_duration;
            }

            public String getVideo_id() {
                return video_id;
            }

            public void setVideo_id(String video_id) {
                this.video_id = video_id;
            }

            public Double getVideo_proportion_article() {
                return video_proportion_article;
            }

            public void setVideo_proportion_article(Double video_proportion_article) {
                this.video_proportion_article = video_proportion_article;
            }

            public String getPlay_auth_token() {
                return play_auth_token;
            }

            public void setPlay_auth_token(String play_auth_token) {
                this.play_auth_token = play_auth_token;
            }

            public String getPlay_biz_token() {
                return play_biz_token;
            }

            public void setPlay_biz_token(String play_biz_token) {
                this.play_biz_token = play_biz_token;
            }

            public VideoDetailInfoBean getVideo_detail_info() {
                return video_detail_info;
            }

            public void setVideo_detail_info(VideoDetailInfoBean video_detail_info) {
                this.video_detail_info = video_detail_info;
            }

            public int getGroup_flags() {
                return group_flags;
            }

            public void setGroup_flags(int group_flags) {
                this.group_flags = group_flags;
            }

            public ActionExtraBean getAction_extra() {
                return action_extra;
            }

            public void setAction_extra(ActionExtraBean action_extra) {
                this.action_extra = action_extra;
            }

            public List<LargeImageListBean> getLarge_image_list() {
                return large_image_list;
            }

            public void setLarge_image_list(List<LargeImageListBean> large_image_list) {
                this.large_image_list = large_image_list;
            }

            public List<ImageListBean> getImage_list() {
                return image_list;
            }

            public void setImage_list(List<ImageListBean> image_list) {
                this.image_list = image_list;
            }

            public MiddleImageBean getMiddle_image() {
                return middle_image;
            }

            public void setMiddle_image(MiddleImageBean middle_image) {
                this.middle_image = middle_image;
            }

            public String getAbstract() {
                return new_abstract;
            }

            public void setAbstract(String new_abstract) {
                this.new_abstract = new_abstract;
            }

            public int getAggr_type() {
                return aggr_type;
            }

            public void setAggr_type(int aggr_type) {
                this.aggr_type = aggr_type;
            }

            public boolean isAllow_download() {
                return allow_download;
            }

            public void setAllow_download(boolean allow_download) {
                this.allow_download = allow_download;
            }

            public int getArticle_sub_type() {
                return article_sub_type;
            }

            public void setArticle_sub_type(int article_sub_type) {
                this.article_sub_type = article_sub_type;
            }

            public int getArticle_type() {
                return article_type;
            }

            public void setArticle_type(int article_type) {
                this.article_type = article_type;
            }

            public String getArticle_url() {
                return article_url;
            }

            public void setArticle_url(String article_url) {
                this.article_url = article_url;
            }

            public int getArticle_version() {
                return article_version;
            }

            public void setArticle_version(int article_version) {
                this.article_version = article_version;
            }

            public int getBan_comment() {
                return ban_comment;
            }

            public void setBan_comment(int ban_comment) {
                this.ban_comment = ban_comment;
            }

            public int getBehot_time() {
                return behot_time;
            }

            public void setBehot_time(int behot_time) {
                this.behot_time = behot_time;
            }

            public int getBury_count() {
                return bury_count;
            }

            public void setBury_count(int bury_count) {
                this.bury_count = bury_count;
            }

            public int getCell_flag() {
                return cell_flag;
            }

            public void setCell_flag(int cell_flag) {
                this.cell_flag = cell_flag;
            }

            public int getCell_layout_style() {
                return cell_layout_style;
            }

            public void setCell_layout_style(int cell_layout_style) {
                this.cell_layout_style = cell_layout_style;
            }

            public int getCell_type() {
                return cell_type;
            }

            public void setCell_type(int cell_type) {
                this.cell_type = cell_type;
            }

            public int getComment_count() {
                return comment_count;
            }

            public void setComment_count(int comment_count) {
                this.comment_count = comment_count;
            }

            public String getContent_decoration() {
                return content_decoration;
            }

            public void setContent_decoration(String content_decoration) {
                this.content_decoration = content_decoration;
            }

            public ControlPanelBean getControl_panel() {
                return control_panel;
            }

            public void setControl_panel(ControlPanelBean control_panel) {
                this.control_panel = control_panel;
            }

            public long getCursor() {
                return cursor;
            }

            public void setCursor(long cursor) {
                this.cursor = cursor;
            }

            public int getDigg_count() {
                return digg_count;
            }

            public void setDigg_count(int digg_count) {
                this.digg_count = digg_count;
            }

            public String getDisplay_url() {
                return display_url;
            }

            public void setDisplay_url(String display_url) {
                this.display_url = display_url;
            }

            public ForwardInfoBean getForward_info() {
                return forward_info;
            }

            public void setForward_info(ForwardInfoBean forward_info) {
                this.forward_info = forward_info;
            }

            public long getGroup_id() {
                return group_id;
            }

            public void setGroup_id(long group_id) {
                this.group_id = group_id;
            }

            public boolean isHas_image() {
                return has_image;
            }

            public void setHas_image(boolean has_image) {
                this.has_image = has_image;
            }

            public boolean isHas_m3u8_video() {
                return has_m3u8_video;
            }

            public void setHas_m3u8_video(boolean has_m3u8_video) {
                this.has_m3u8_video = has_m3u8_video;
            }

            public int getHas_mp4_video() {
                return has_mp4_video;
            }

            public void setHas_mp4_video(int has_mp4_video) {
                this.has_mp4_video = has_mp4_video;
            }

            public boolean isHas_video() {
                return has_video;
            }

            public void setHas_video(boolean has_video) {
                this.has_video = has_video;
            }

            public int getHot() {
                return hot;
            }

            public void setHot(int hot) {
                this.hot = hot;
            }

            public int getIgnore_web_transform() {
                return ignore_web_transform;
            }

            public void setIgnore_web_transform(int ignore_web_transform) {
                this.ignore_web_transform = ignore_web_transform;
            }

            public String getInteraction_data() {
                return interaction_data;
            }

            public void setInteraction_data(String interaction_data) {
                this.interaction_data = interaction_data;
            }

            public boolean isIs_stick() {
                return is_stick;
            }

            public void setIs_stick(boolean is_stick) {
                this.is_stick = is_stick;
            }

            public boolean isIs_subject() {
                return is_subject;
            }

            public void setIs_subject(boolean is_subject) {
                this.is_subject = is_subject;
            }

            public long getItem_id() {
                return item_id;
            }

            public void setItem_id(long item_id) {
                this.item_id = item_id;
            }

            public int getItem_version() {
                return item_version;
            }

            public void setItem_version(int item_version) {
                this.item_version = item_version;
            }

            public String getKeywords() {
                return keywords;
            }

            public void setKeywords(String keywords) {
                this.keywords = keywords;
            }

            public String getLabel() {
                return label;
            }

            public void setLabel(String label) {
                this.label = label;
            }

            public LabelExtraBean getLabel_extra() {
                return label_extra;
            }

            public void setLabel_extra(LabelExtraBean label_extra) {
                this.label_extra = label_extra;
            }

            public int getLabel_style() {
                return label_style;
            }

            public void setLabel_style(int label_style) {
                this.label_style = label_style;
            }

            public int getLevel() {
                return level;
            }

            public void setLevel(int level) {
                this.level = level;
            }

            public LogPbBean getLog_pb() {
                return log_pb;
            }

            public void setLog_pb(LogPbBean log_pb) {
                this.log_pb = log_pb;
            }

            public MediaInfoBean getMedia_info() {
                return media_info;
            }

            public void setMedia_info(MediaInfoBean media_info) {
                this.media_info = media_info;
            }

            public String getMedia_name() {
                return media_name;
            }

            public void setMedia_name(String media_name) {
                this.media_name = media_name;
            }

            public int getNeed_client_impr_recycle() {
                return need_client_impr_recycle;
            }

            public void setNeed_client_impr_recycle(int need_client_impr_recycle) {
                this.need_client_impr_recycle = need_client_impr_recycle;
            }

            public int getPublish_time() {
                return publish_time;
            }

            public void setPublish_time(int publish_time) {
                this.publish_time = publish_time;
            }

            public int getRead_count() {
                return read_count;
            }

            public void setRead_count(int read_count) {
                this.read_count = read_count;
            }

            public int getRepin_count() {
                return repin_count;
            }

            public void setRepin_count(int repin_count) {
                this.repin_count = repin_count;
            }

            public String getRid() {
                return rid;
            }

            public void setRid(String rid) {
                this.rid = rid;
            }

            public int getShare_count() {
                return share_count;
            }

            public void setShare_count(int share_count) {
                this.share_count = share_count;
            }

            public ShareInfoBean getShare_info() {
                return share_info;
            }

            public void setShare_info(ShareInfoBean share_info) {
                this.share_info = share_info;
            }

            public int getShare_type() {
                return share_type;
            }

            public void setShare_type(int share_type) {
                this.share_type = share_type;
            }

            public String getShare_url() {
                return share_url;
            }

            public void setShare_url(String share_url) {
                this.share_url = share_url;
            }

            public boolean isShow_dislike() {
                return show_dislike;
            }

            public void setShow_dislike(boolean show_dislike) {
                this.show_dislike = show_dislike;
            }

            public boolean isShow_portrait() {
                return show_portrait;
            }

            public void setShow_portrait(boolean show_portrait) {
                this.show_portrait = show_portrait;
            }

            public boolean isShow_portrait_article() {
                return show_portrait_article;
            }

            public void setShow_portrait_article(boolean show_portrait_article) {
                this.show_portrait_article = show_portrait_article;
            }

            public String getSource() {
                return source;
            }

            public void setSource(String source) {
                this.source = source;
            }

            public int getSource_icon_style() {
                return source_icon_style;
            }

            public void setSource_icon_style(int source_icon_style) {
                this.source_icon_style = source_icon_style;
            }

            public String getSource_open_url() {
                return source_open_url;
            }

            public void setSource_open_url(String source_open_url) {
                this.source_open_url = source_open_url;
            }

            public String getStick_label() {
                return stick_label;
            }

            public void setStick_label(String stick_label) {
                this.stick_label = stick_label;
            }

            public int getStick_style() {
                return stick_style;
            }

            public void setStick_style(int stick_style) {
                this.stick_style = stick_style;
            }

            public String getTag() {
                return tag;
            }

            public void setTag(String tag) {
                this.tag = tag;
            }

            public long getTag_id() {
                return tag_id;
            }

            public void setTag_id(long tag_id) {
                this.tag_id = tag_id;
            }

            public int getTip() {
                return tip;
            }

            public void setTip(int tip) {
                this.tip = tip;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public UgcRecommendBean getUgc_recommend() {
                return ugc_recommend;
            }

            public void setUgc_recommend(UgcRecommendBean ugc_recommend) {
                this.ugc_recommend = ugc_recommend;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public UserInfoBean getUser_info() {
                return user_info;
            }

            public void setUser_info(UserInfoBean user_info) {
                this.user_info = user_info;
            }

            public int getUser_repin() {
                return user_repin;
            }

            public void setUser_repin(int user_repin) {
                this.user_repin = user_repin;
            }

            public int getUser_verified() {
                return user_verified;
            }

            public void setUser_verified(int user_verified) {
                this.user_verified = user_verified;
            }

            public String getVerified_content() {
                return verified_content;
            }

            public void setVerified_content(String verified_content) {
                this.verified_content = verified_content;
            }

            public int getVideo_style() {
                return video_style;
            }

            public void setVideo_style(int video_style) {
                this.video_style = video_style;
            }

            public List<ActionListBean> getAction_list() {
                return action_list;
            }

            public void setAction_list(List<ActionListBean> action_list) {
                this.action_list = action_list;
            }

            public List<FilterWordsBean> getFilter_words() {
                return filter_words;
            }

            public void setFilter_words(List<FilterWordsBean> filter_words) {
                this.filter_words = filter_words;
            }


            public static class ControlPanelBean {
                /**
                 * recommend_sponsor : {"icon_url":"http://p3-tt.bytecdn.cn/origin/13ef000096960314fff4","label":"帮上头条","night_icon_url":"http://p3-tt.bytecdn.cn/origin/dc1d0001ad958473e24b","target_url":"https://i.snssdk.com/ad/pgc_promotion/mobile/create/?group_id=6649730188609323534&item_id=6649730188609323534"}
                 */

                private RecommendSponsorBean recommend_sponsor;

                public RecommendSponsorBean getRecommend_sponsor() {
                    return recommend_sponsor;
                }

                public void setRecommend_sponsor(RecommendSponsorBean recommend_sponsor) {
                    this.recommend_sponsor = recommend_sponsor;
                }

                public static class RecommendSponsorBean {
                    /**
                     * icon_url : http://p3-tt.bytecdn.cn/origin/13ef000096960314fff4
                     * label : 帮上头条
                     * night_icon_url : http://p3-tt.bytecdn.cn/origin/dc1d0001ad958473e24b
                     * target_url : https://i.snssdk.com/ad/pgc_promotion/mobile/create/?group_id=6649730188609323534&item_id=6649730188609323534
                     */

                    private String icon_url;
                    private String label;
                    private String night_icon_url;
                    private String target_url;

                    public String getIcon_url() {
                        return icon_url;
                    }

                    public void setIcon_url(String icon_url) {
                        this.icon_url = icon_url;
                    }

                    public String getLabel() {
                        return label;
                    }

                    public void setLabel(String label) {
                        this.label = label;
                    }

                    public String getNight_icon_url() {
                        return night_icon_url;
                    }

                    public void setNight_icon_url(String night_icon_url) {
                        this.night_icon_url = night_icon_url;
                    }

                    public String getTarget_url() {
                        return target_url;
                    }

                    public void setTarget_url(String target_url) {
                        this.target_url = target_url;
                    }
                }
            }

            public static class ForwardInfoBean {
                /**
                 * forward_count : 0
                 */

                private int forward_count;

                public int getForward_count() {
                    return forward_count;
                }

                public void setForward_count(int forward_count) {
                    this.forward_count = forward_count;
                }
            }

            public static class LabelExtraBean {
                /**
                 * icon_url : {}
                 * is_redirect : false
                 * redirect_url :
                 * style_type : 0
                 */

                private IconUrlBean icon_url;
                private boolean is_redirect;
                private String redirect_url;
                private int style_type;

                public IconUrlBean getIcon_url() {
                    return icon_url;
                }

                public void setIcon_url(IconUrlBean icon_url) {
                    this.icon_url = icon_url;
                }

                public boolean isIs_redirect() {
                    return is_redirect;
                }

                public void setIs_redirect(boolean is_redirect) {
                    this.is_redirect = is_redirect;
                }

                public String getRedirect_url() {
                    return redirect_url;
                }

                public void setRedirect_url(String redirect_url) {
                    this.redirect_url = redirect_url;
                }

                public int getStyle_type() {
                    return style_type;
                }

                public void setStyle_type(int style_type) {
                    this.style_type = style_type;
                }

                public static class IconUrlBean {
                }
            }

            public static class LogPbBean {
                /**
                 * impr_id : 201901241411480100080590389837FA3
                 * is_following : 0
                 */

                private String impr_id;
                private String is_following;

                public String getImpr_id() {
                    return impr_id;
                }

                public void setImpr_id(String impr_id) {
                    this.impr_id = impr_id;
                }

                public String getIs_following() {
                    return is_following;
                }

                public void setIs_following(String is_following) {
                    this.is_following = is_following;
                }
            }

            public static class MediaInfoBean {
                /**
                 * avatar_url : http://p1.pstatp.com/large/6eed00053499a4aae20b
                 * follow : false
                 * is_star_user : false
                 * media_id : 1601422829470724
                 * name : 央视快评
                 * recommend_reason :
                 * recommend_type : 0
                 * user_id : 99138739220
                 * user_verified : true
                 * verified_content :
                 */

                private String avatar_url;
                private boolean follow;
                private boolean is_star_user;
                private long media_id;
                private String name;
                private String recommend_reason;
                private int recommend_type;
                private long user_id;
                private boolean user_verified;
                private String verified_content;

                public String getAvatar_url() {
                    return avatar_url;
                }

                public void setAvatar_url(String avatar_url) {
                    this.avatar_url = avatar_url;
                }

                public boolean isFollow() {
                    return follow;
                }

                public void setFollow(boolean follow) {
                    this.follow = follow;
                }

                public boolean isIs_star_user() {
                    return is_star_user;
                }

                public void setIs_star_user(boolean is_star_user) {
                    this.is_star_user = is_star_user;
                }

                public long getMedia_id() {
                    return media_id;
                }

                public void setMedia_id(long media_id) {
                    this.media_id = media_id;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getRecommend_reason() {
                    return recommend_reason;
                }

                public void setRecommend_reason(String recommend_reason) {
                    this.recommend_reason = recommend_reason;
                }

                public int getRecommend_type() {
                    return recommend_type;
                }

                public void setRecommend_type(int recommend_type) {
                    this.recommend_type = recommend_type;
                }

                public long getUser_id() {
                    return user_id;
                }

                public void setUser_id(long user_id) {
                    this.user_id = user_id;
                }

                public boolean isUser_verified() {
                    return user_verified;
                }

                public void setUser_verified(boolean user_verified) {
                    this.user_verified = user_verified;
                }

                public String getVerified_content() {
                    return verified_content;
                }

                public void setVerified_content(String verified_content) {
                    this.verified_content = verified_content;
                }
            }

            public static class ShareInfoBean {
                /**
                 * cover_image : null
                 * description : null
                 * on_suppress : 0
                 * share_type : {"pyq":0,"qq":0,"qzone":0,"wx":0}
                 * share_url : http://m.toutiao.com/a6649730188609323534/?iid=0&app=news_article&is_hit_share_recommend=0
                 * title : 「央视快评」确保干一件成一件
                 * token_type : 1
                 * weixin_cover_image : {"height":981,"uri":"large/tos-cn-i-0000/a8da58e2-1f2d-11e9-88d4-ac1f6b0ace98","url":"http://p1-tt.bytecdn.cn/large/tos-cn-i-0000/a8da58e2-1f2d-11e9-88d4-ac1f6b0ace98","url_list":[{"url":"http://p1-tt.bytecdn.cn/large/tos-cn-i-0000/a8da58e2-1f2d-11e9-88d4-ac1f6b0ace98"},{"url":"http://p9-tt.bytecdn.cn/large/tos-cn-i-0000/a8da58e2-1f2d-11e9-88d4-ac1f6b0ace98"},{"url":"http://p1-tt.bytecdn.cn/large/tos-cn-i-0000/a8da58e2-1f2d-11e9-88d4-ac1f6b0ace98"}],"width":800}
                 */

                private Object cover_image;
                private Object description;
                private int on_suppress;
                private ShareTypeBean share_type;
                private String share_url;
                private String title;
                private int token_type;
                private WeixinCoverImageBean weixin_cover_image;

                public Object getCover_image() {
                    return cover_image;
                }

                public void setCover_image(Object cover_image) {
                    this.cover_image = cover_image;
                }

                public Object getDescription() {
                    return description;
                }

                public void setDescription(Object description) {
                    this.description = description;
                }

                public int getOn_suppress() {
                    return on_suppress;
                }

                public void setOn_suppress(int on_suppress) {
                    this.on_suppress = on_suppress;
                }

                public ShareTypeBean getShare_type() {
                    return share_type;
                }

                public void setShare_type(ShareTypeBean share_type) {
                    this.share_type = share_type;
                }

                public String getShare_url() {
                    return share_url;
                }

                public void setShare_url(String share_url) {
                    this.share_url = share_url;
                }

                public String getTitle() {
                    return title;
                }

                public void setTitle(String title) {
                    this.title = title;
                }

                public int getToken_type() {
                    return token_type;
                }

                public void setToken_type(int token_type) {
                    this.token_type = token_type;
                }

                public WeixinCoverImageBean getWeixin_cover_image() {
                    return weixin_cover_image;
                }

                public void setWeixin_cover_image(WeixinCoverImageBean weixin_cover_image) {
                    this.weixin_cover_image = weixin_cover_image;
                }

                public static class ShareTypeBean {
                    /**
                     * pyq : 0
                     * qq : 0
                     * qzone : 0
                     * wx : 0
                     */

                    private int pyq;
                    private int qq;
                    private int qzone;
                    private int wx;

                    public int getPyq() {
                        return pyq;
                    }

                    public void setPyq(int pyq) {
                        this.pyq = pyq;
                    }

                    public int getQq() {
                        return qq;
                    }

                    public void setQq(int qq) {
                        this.qq = qq;
                    }

                    public int getQzone() {
                        return qzone;
                    }

                    public void setQzone(int qzone) {
                        this.qzone = qzone;
                    }

                    public int getWx() {
                        return wx;
                    }

                    public void setWx(int wx) {
                        this.wx = wx;
                    }
                }

                public static class WeixinCoverImageBean {
                    /**
                     * height : 981
                     * uri : large/tos-cn-i-0000/a8da58e2-1f2d-11e9-88d4-ac1f6b0ace98
                     * url : http://p1-tt.bytecdn.cn/large/tos-cn-i-0000/a8da58e2-1f2d-11e9-88d4-ac1f6b0ace98
                     * url_list : [{"url":"http://p1-tt.bytecdn.cn/large/tos-cn-i-0000/a8da58e2-1f2d-11e9-88d4-ac1f6b0ace98"},{"url":"http://p9-tt.bytecdn.cn/large/tos-cn-i-0000/a8da58e2-1f2d-11e9-88d4-ac1f6b0ace98"},{"url":"http://p1-tt.bytecdn.cn/large/tos-cn-i-0000/a8da58e2-1f2d-11e9-88d4-ac1f6b0ace98"}]
                     * width : 800
                     */

                    private int height;
                    private String uri;
                    private String url;
                    private int width;
                    private List<UrlListBean> url_list;

                    public int getHeight() {
                        return height;
                    }

                    public void setHeight(int height) {
                        this.height = height;
                    }

                    public String getUri() {
                        return uri;
                    }

                    public void setUri(String uri) {
                        this.uri = uri;
                    }

                    public String getUrl() {
                        return url;
                    }

                    public void setUrl(String url) {
                        this.url = url;
                    }

                    public int getWidth() {
                        return width;
                    }

                    public void setWidth(int width) {
                        this.width = width;
                    }

                    public List<UrlListBean> getUrl_list() {
                        return url_list;
                    }

                    public void setUrl_list(List<UrlListBean> url_list) {
                        this.url_list = url_list;
                    }

                    public static class UrlListBean {
                        /**
                         * url : http://p1-tt.bytecdn.cn/large/tos-cn-i-0000/a8da58e2-1f2d-11e9-88d4-ac1f6b0ace98
                         */

                        private String url;

                        public String getUrl() {
                            return url;
                        }

                        public void setUrl(String url) {
                            this.url = url;
                        }
                    }
                }
            }

            public static class UgcRecommendBean {
                /**
                 * activity :
                 * reason : 央视快评官方账号
                 */

                private String activity;
                private String reason;

                public String getActivity() {
                    return activity;
                }

                public void setActivity(String activity) {
                    this.activity = activity;
                }

                public String getReason() {
                    return reason;
                }

                public void setReason(String reason) {
                    this.reason = reason;
                }
            }

            public static class UserInfoBean {
                /**
                 * avatar_url : http://p1.pstatp.com/thumb/6eed00053499a4aae20b
                 * description : 中央电视台新闻中心精心打造的时政评论品牌。
                 * follow : false
                 * follower_count : 0
                 * live_info_type : 1
                 * name : 央视快评
                 * schema : sslocal://profile?uid=99138739220&refer=video
                 * user_auth_info : {"auth_type":"0","auth_info":"央视快评官方账号"}
                 * user_id : 99138739220
                 * user_verified : true
                 * verified_content : 央视快评官方账号
                 */

                private String avatar_url;
                private String description;
                private boolean follow;
                private int follower_count;
                private int live_info_type;
                private String name;
                private String schema;
                private String user_auth_info;
                private long user_id;
                private boolean user_verified;
                private String verified_content;

                public String getAvatar_url() {
                    return avatar_url;
                }

                public void setAvatar_url(String avatar_url) {
                    this.avatar_url = avatar_url;
                }

                public String getDescription() {
                    return description;
                }

                public void setDescription(String description) {
                    this.description = description;
                }

                public boolean isFollow() {
                    return follow;
                }

                public void setFollow(boolean follow) {
                    this.follow = follow;
                }

                public int getFollower_count() {
                    return follower_count;
                }

                public void setFollower_count(int follower_count) {
                    this.follower_count = follower_count;
                }

                public int getLive_info_type() {
                    return live_info_type;
                }

                public void setLive_info_type(int live_info_type) {
                    this.live_info_type = live_info_type;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getSchema() {
                    return schema;
                }

                public void setSchema(String schema) {
                    this.schema = schema;
                }

                public String getUser_auth_info() {
                    return user_auth_info;
                }

                public void setUser_auth_info(String user_auth_info) {
                    this.user_auth_info = user_auth_info;
                }

                public long getUser_id() {
                    return user_id;
                }

                public void setUser_id(long user_id) {
                    this.user_id = user_id;
                }

                public boolean isUser_verified() {
                    return user_verified;
                }

                public void setUser_verified(boolean user_verified) {
                    this.user_verified = user_verified;
                }

                public String getVerified_content() {
                    return verified_content;
                }

                public void setVerified_content(String verified_content) {
                    this.verified_content = verified_content;
                }
            }

            public static class ActionListBean {
                /**
                 * action : 1
                 * desc :
                 * extra : {}
                 */

                private int action;
                private String desc;
                private ExtraBean extra;

                public int getAction() {
                    return action;
                }

                public void setAction(int action) {
                    this.action = action;
                }

                public String getDesc() {
                    return desc;
                }

                public void setDesc(String desc) {
                    this.desc = desc;
                }

                public ExtraBean getExtra() {
                    return extra;
                }

                public void setExtra(ExtraBean extra) {
                    this.extra = extra;
                }

                public static class ExtraBean {
                }
            }

            public static class FilterWordsBean {
                /**
                 * id : 8:0
                 * is_selected : false
                 * name : 看过了
                 */

                private String id;
                private boolean is_selected;
                private String name;

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public boolean isIs_selected() {
                    return is_selected;
                }

                public void setIs_selected(boolean is_selected) {
                    this.is_selected = is_selected;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }
            }

            public static class ActionExtraBean {
                /**
                 * "channel_id": 3462388071
                 */
                private long channel_id;

                public long getChannel_id() {
                    return channel_id;
                }

                public void setChannel_id(long channel_id) {
                    this.channel_id = channel_id;
                }
            }

            public static class ImageListBean {
                /**
                 * image_list": [
                 * {
                 * "height": 356,
                 * "uri": "list/pgc-image/5b7b929935754286a5210b59a775da2e",
                 * "url": "http://p1-tt.bytecdn.cn/list/300x196/pgc-image/5b7b929935754286a5210b59a775da2e.webp",
                 * "url_list": [{
                 * "url": "http://p1-tt.bytecdn.cn/list/300x196/pgc-image/5b7b929935754286a5210b59a775da2e.webp"
                 * }, {
                 * "url": "http://p3-tt.bytecdn.cn/list/300x196/pgc-image/5b7b929935754286a5210b59a775da2e.webp"
                 * }, {
                 * "url": "http://p3-tt.bytecdn.cn/list/300x196/pgc-image/5b7b929935754286a5210b59a775da2e.webp"
                 * }],
                 * "width": 633    * 	},
                 * <p>
                 * {
                 * "height": 380,
                 * "uri": "list/pgc-image/6cbcd5c944ab4487ba3971c6cb76ac7e",
                 * "url": "http://p1-tt.bytecdn.cn/list/300x196/pgc-image/6cbcd5c944ab4487ba3971c6cb76ac7e.webp",
                 * "url_list": [{
                 * "url": "http://p1-tt.bytecdn.cn/list/300x196/pgc-image/6cbcd5c944ab4487ba3971c6cb76ac7e.webp"
                 * }, {
                 * "url": "http://p9-tt.bytecdn.cn/list/300x196/pgc-image/6cbcd5c944ab4487ba3971c6cb76ac7e.webp"
                 * }, {
                 * "url": "http://p1-tt.bytecdn.cn/list/300x196/pgc-image/6cbcd5c944ab4487ba3971c6cb76ac7e.webp"
                 * }],
                 * "width": 676 }
                 * ]
                 */
                private int height;
                private String uri;
                private String url;
                private int width;
                private List<UrlListBean> url_list;

                public int getHeight() {
                    return height;
                }

                public void setHeight(int height) {
                    this.height = height;
                }

                public String getUri() {
                    return uri;
                }

                public void setUri(String uri) {
                    this.uri = uri;
                }

                public String getUrl() {
                    return url;
                }

                public void setUrl(String url) {
                    this.url = url;
                }

                public int getWidth() {
                    return width;
                }

                public void setWidth(int width) {
                    this.width = width;
                }

                public List<UrlListBean> getUrl_list() {
                    return url_list;
                }

                public void setUrl_list(List<UrlListBean> url_list) {
                    this.url_list = url_list;
                }

                public static class UrlListBean {
                    private String url;

                    public String getUrl() {
                        return url;
                    }

                    public void setUrl(String url) {
                        this.url = url;
                    }
                }
            }

            public static class LargeImageListBean {
                /**
                 * image_list": [
                 * {
                 * "height": 356,
                 * "uri": "list/pgc-image/5b7b929935754286a5210b59a775da2e",
                 * "url": "http://p1-tt.bytecdn.cn/list/300x196/pgc-image/5b7b929935754286a5210b59a775da2e.webp",
                 * "url_list": [{
                 * "url": "http://p1-tt.bytecdn.cn/list/300x196/pgc-image/5b7b929935754286a5210b59a775da2e.webp"
                 * }, {
                 * "url": "http://p3-tt.bytecdn.cn/list/300x196/pgc-image/5b7b929935754286a5210b59a775da2e.webp"
                 * }, {
                 * "url": "http://p3-tt.bytecdn.cn/list/300x196/pgc-image/5b7b929935754286a5210b59a775da2e.webp"
                 * }],
                 * "width": 633    * 	},
                 * <p>
                 * {
                 * "height": 380,
                 * "uri": "list/pgc-image/6cbcd5c944ab4487ba3971c6cb76ac7e",
                 * "url": "http://p1-tt.bytecdn.cn/list/300x196/pgc-image/6cbcd5c944ab4487ba3971c6cb76ac7e.webp",
                 * "url_list": [{
                 * "url": "http://p1-tt.bytecdn.cn/list/300x196/pgc-image/6cbcd5c944ab4487ba3971c6cb76ac7e.webp"
                 * }, {
                 * "url": "http://p9-tt.bytecdn.cn/list/300x196/pgc-image/6cbcd5c944ab4487ba3971c6cb76ac7e.webp"
                 * }, {
                 * "url": "http://p1-tt.bytecdn.cn/list/300x196/pgc-image/6cbcd5c944ab4487ba3971c6cb76ac7e.webp"
                 * }],
                 * "width": 676 }
                 * ]
                 */
                private int height;
                private String uri;
                private String url;
                private int width;
                private List<UrlListBean> url_list;

                public int getHeight() {
                    return height;
                }

                public void setHeight(int height) {
                    this.height = height;
                }

                public String getUri() {
                    return uri;
                }

                public void setUri(String uri) {
                    this.uri = uri;
                }

                public String getUrl() {
                    return url;
                }

                public void setUrl(String url) {
                    this.url = url;
                }

                public int getWidth() {
                    return width;
                }

                public void setWidth(int width) {
                    this.width = width;
                }

                public List<UrlListBean> getUrl_list() {
                    return url_list;
                }

                public void setUrl_list(List<UrlListBean> url_list) {
                    this.url_list = url_list;
                }

                public static class UrlListBean {
                    private String url;

                    public String getUrl() {
                        return url;
                    }

                    public void setUrl(String url) {
                        this.url = url;
                    }
                }
            }

            public static class MiddleImageBean {
                /**
                 * "middle_image": {
                 * "height": 356,
                 * "uri": "list/pgc-image/5b7b929935754286a5210b59a775da2e",
                 * "url": "http://p9-tt.bytecdn.cn/list/300x196/pgc-image/5b7b929935754286a5210b59a775da2e.webp",
                 * "url_list": [{
                 * "url": "http://p9-tt.bytecdn.cn/list/300x196/pgc-image/5b7b929935754286a5210b59a775da2e.webp"
                 * }, {
                 * "url": "http://p1-tt.bytecdn.cn/list/300x196/pgc-image/5b7b929935754286a5210b59a775da2e.webp"
                 * }, {
                 * "url": "http://p1-tt.bytecdn.cn/list/300x196/pgc-image/5b7b929935754286a5210b59a775da2e.webp"
                 * }],
                 * "width": 633    * 	}
                 */
                private int height;
                private String uri;
                private String url;
                private int width;
                private List<UrlListBean> url_list;

                public int getHeight() {
                    return height;
                }

                public void setHeight(int height) {
                    this.height = height;
                }

                public String getUri() {
                    return uri;
                }

                public void setUri(String uri) {
                    this.uri = uri;
                }

                public String getUrl() {
                    return url;
                }

                public void setUrl(String url) {
                    this.url = url;
                }

                public int getWidth() {
                    return width;
                }

                public void setWidth(int width) {
                    this.width = width;
                }

                public List<UrlListBean> getUrl_list() {
                    return url_list;
                }

                public void setUrl_list(List<UrlListBean> url_list) {
                    this.url_list = url_list;
                }

                public static class UrlListBean {
                    private String url;

                    public String getUrl() {
                        return url;
                    }

                    public void setUrl(String url) {
                        this.url = url;
                    }
                }
            }

            public static class VideoDetailInfoBean {
                /*
                                "direct_play": 1,
                                "group_flags": 32832,
                                "show_pgc_subscribe": 1,
                                "video_id": "v022f4130000bh4i8ir3ft9r09emgnr0",
                                "video_preloading_flag": 1,
                                "video_type": 0,
                                "video_watch_count": 1956,
                                "video_watching_count": 0
                                "detail_video_large_image": {
			                        "height": 326,
			                        "uri": "video1609/185ff0004513e03cab63e",
			                        "url": "http://p9-tt.bytecdn.cn/video1609/185ff0004513e03cab63e",
			                        "url_list": [{
				                           "url": "http://p9-tt.bytecdn.cn/video1609/185ff0004513e03cab63e"
			                                                                }, {
				                            "url": "http://p3-tt.bytecdn.cn/video1609/185ff0004513e03cab63e"
			                                                                }, {
				                            "url": "http://p1-tt.bytecdn.cn/video1609/185ff0004513e03cab63e"
			                             }],
                                 */
                private int direct_play;
                private int group_flags;
                private int show_pgc_subscribe;
                private String video_id;
                private int video_preloading_flag;
                private int video_type;
                private int video_watch_count;
                private int video_watching_count;
                private DetailVideoLargeImageBean detail_video_large_image;

                public int getDirect_play() {
                    return direct_play;
                }

                public void setDirect_play(int direct_play) {
                    this.direct_play = direct_play;
                }

                public int getGroup_flags() {
                    return group_flags;
                }

                public void setGroup_flags(int group_flags) {
                    this.group_flags = group_flags;
                }

                public int getShow_pgc_subscribe() {
                    return show_pgc_subscribe;
                }

                public void setShow_pgc_subscribe(int show_pgc_subscribe) {
                    this.show_pgc_subscribe = show_pgc_subscribe;
                }

                public String getVideo_id() {
                    return video_id;
                }

                public void setVideo_id(String video_id) {
                    this.video_id = video_id;
                }

                public int getVideo_preloading_flag() {
                    return video_preloading_flag;
                }

                public void setVideo_preloading_flag(int video_preloading_flag) {
                    this.video_preloading_flag = video_preloading_flag;
                }

                public int getVideo_type() {
                    return video_type;
                }

                public void setVideo_type(int video_type) {
                    this.video_type = video_type;
                }

                public int getVideo_watch_count() {
                    return video_watch_count;
                }

                public void setVideo_watch_count(int video_watch_count) {
                    this.video_watch_count = video_watch_count;
                }

                public int getVideo_watching_count() {
                    return video_watching_count;
                }

                public void setVideo_watching_count(int video_watching_count) {
                    this.video_watching_count = video_watching_count;
                }

                public DetailVideoLargeImageBean getDetail_video_large_image() {
                    return detail_video_large_image;
                }

                public void setDetail_video_large_image(DetailVideoLargeImageBean detail_video_large_image) {
                    this.detail_video_large_image = detail_video_large_image;
                }

                public static class DetailVideoLargeImageBean {
                    /**
                     * "detail_video_large_image": {
                     * "height": 356,
                     * "uri": "list/pgc-image/5b7b929935754286a5210b59a775da2e",
                     * "url": "http://p9-tt.bytecdn.cn/list/300x196/pgc-image/5b7b929935754286a5210b59a775da2e.webp",
                     * "url_list": [{
                     * "url": "http://p9-tt.bytecdn.cn/list/300x196/pgc-image/5b7b929935754286a5210b59a775da2e.webp"
                     * }, {
                     * "url": "http://p1-tt.bytecdn.cn/list/300x196/pgc-image/5b7b929935754286a5210b59a775da2e.webp"
                     * }, {
                     * "url": "http://p1-tt.bytecdn.cn/list/300x196/pgc-image/5b7b929935754286a5210b59a775da2e.webp"
                     * }],
                     * "width": 633    * 	}
                     */
                    private int height;
                    private String uri;
                    private String url;
                    private int width;
                    private List<UrlListBean> url_list;

                    public int getHeight() {
                        return height;
                    }

                    public void setHeight(int height) {
                        this.height = height;
                    }

                    public String getUri() {
                        return uri;
                    }

                    public void setUri(String uri) {
                        this.uri = uri;
                    }

                    public String getUrl() {
                        return url;
                    }

                    public void setUrl(String url) {
                        this.url = url;
                    }

                    public int getWidth() {
                        return width;
                    }

                    public void setWidth(int width) {
                        this.width = width;
                    }

                    public List<UrlListBean> getUrl_list() {
                        return url_list;
                    }

                    public void setUrl_list(List<UrlListBean> url_list) {
                        this.url_list = url_list;
                    }

                    public static class UrlListBean {
                        private String url;

                        public String getUrl() {
                            return url;
                        }

                        public void setUrl(String url) {
                            this.url = url;
                        }
                    }
                }
            }
        }
    }
}
