package com.rayho.tsxiu.module_news.bean;

import java.util.List;

/**
 * Created by Rayho on 2019/3/22
 **/
public class NewsHotSearch {

    /**
     * data : {"call_per_refresh":2,"homepage_search_suggest":"罗斯右手肘骨折 | 郑爽发布会扇嘴巴 | 江西结婚习俗视频","suggest_words":[{"id":"6671003404044604683","or":"qc:317","recommend_reason":"","word":"罗斯右手肘骨折"},{"id":"6670772719069435147","or":"qc:29","recommend_reason":"","word":"郑爽发布会扇嘴巴"},{"id":"6580531342621021453","or":"qc:208","recommend_reason":"","word":"江西结婚习俗视频"}]}
     * message : success
     */
    public DataBean data;
    public String message;


    public static class DataBean {
        /**
         * call_per_refresh : 2
         * homepage_search_suggest : 罗斯右手肘骨折 | 郑爽发布会扇嘴巴 | 江西结婚习俗视频
         * suggest_words : [{"id":"6671003404044604683","or":"qc:317","recommend_reason":"","word":"罗斯右手肘骨折"},{"id":"6670772719069435147","or":"qc:29","recommend_reason":"","word":"郑爽发布会扇嘴巴"},{"id":"6580531342621021453","or":"qc:208","recommend_reason":"","word":"江西结婚习俗视频"}]
         */
        public int call_per_refresh;
        public String homepage_search_suggest;
        public List<SuggestWordsBean> suggest_words;


        public static class SuggestWordsBean {
            /**
             * id : 6671003404044604683
             * or : qc:317
             * recommend_reason :
             * word : 罗斯右手肘骨折
             */
            public String id;
            public String or;
            public String recommend_reason;
            public String word;
        }
    }
}
