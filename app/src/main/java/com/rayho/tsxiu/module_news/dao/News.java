package com.rayho.tsxiu.module_news.dao;

import com.rayho.tsxiu.utils.StringConverter;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Unique;

import java.util.List;

import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Rayho on 2019/3/14
 **/
@Entity
public class News {
    @Id(autoincrement = true)
    private Long id;//主键

    @NotNull
    @Unique
    private String nid;//新闻的id

    private int viewCount;
    private String publishDateStr;
    private String catPathKey;
    private String title;
    private long publishDate;
    private int dislikeCount;
    private int commentCount;
    private int likeCount;
    private String posterId;
    private String html;
    private String url;
    private String coverUrl;
    private String content;
    private String posterScreenName;
    //转换器 将list<T>数据存储到数据库中
    @Convert(columnType = String.class, converter = StringConverter.class)
    private List<String> imageUrls;
    @Convert(columnType = String.class, converter = StringConverter.class)
    private List<String> tags;
    //新闻的类型(只有文字 单图 3图 组图)
    private int type;

    @Generated(hash = 1088847385)
    public News(Long id, @NotNull String nid, int viewCount, String publishDateStr,
            String catPathKey, String title, long publishDate, int dislikeCount,
            int commentCount, int likeCount, String posterId, String html,
            String url, String coverUrl, String content, String posterScreenName,
            List<String> imageUrls, List<String> tags, int type) {
        this.id = id;
        this.nid = nid;
        this.viewCount = viewCount;
        this.publishDateStr = publishDateStr;
        this.catPathKey = catPathKey;
        this.title = title;
        this.publishDate = publishDate;
        this.dislikeCount = dislikeCount;
        this.commentCount = commentCount;
        this.likeCount = likeCount;
        this.posterId = posterId;
        this.html = html;
        this.url = url;
        this.coverUrl = coverUrl;
        this.content = content;
        this.posterScreenName = posterScreenName;
        this.imageUrls = imageUrls;
        this.tags = tags;
        this.type = type;
    }

    @Generated(hash = 1579685679)
    public News() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNid() {
        return this.nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    public int getViewCount() {
        return this.viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public String getPublishDateStr() {
        return this.publishDateStr;
    }

    public void setPublishDateStr(String publishDateStr) {
        this.publishDateStr = publishDateStr;
    }

    public String getCatPathKey() {
        return this.catPathKey;
    }

    public void setCatPathKey(String catPathKey) {
        this.catPathKey = catPathKey;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getPublishDate() {
        return this.publishDate;
    }

    public void setPublishDate(long publishDate) {
        this.publishDate = publishDate;
    }

    public int getDislikeCount() {
        return this.dislikeCount;
    }

    public void setDislikeCount(int dislikeCount) {
        this.dislikeCount = dislikeCount;
    }

    public int getCommentCount() {
        return this.commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public int getLikeCount() {
        return this.likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public String getPosterId() {
        return this.posterId;
    }

    public void setPosterId(String posterId) {
        this.posterId = posterId;
    }

    public String getHtml() {
        return this.html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCoverUrl() {
        return this.coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPosterScreenName() {
        return this.posterScreenName;
    }

    public void setPosterScreenName(String posterScreenName) {
        this.posterScreenName = posterScreenName;
    }

    public List<String> getImageUrls() {
        return this.imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public List<String> getTags() {
        return this.tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
