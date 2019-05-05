package com.rayho.tsxiu.module_photo.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Rayho on 2019/4/10
 **/
@Entity
public class Image {

    @Id(autoincrement = true)
    private Long id;

    @NotNull
    @Unique
    private String imageId;//图片id

    @NotNull
    private String url;//图片地址


    private String date;//发布日期

    private String title;//标题

    private String favorites;//点赞数

    private String comments;//评论数


    @Generated(hash = 936323031)
    public Image(Long id, @NotNull String imageId, @NotNull String url, String date,
            String title, String favorites, String comments) {
        this.id = id;
        this.imageId = imageId;
        this.url = url;
        this.date = date;
        this.title = title;
        this.favorites = favorites;
        this.comments = comments;
    }

    @Generated(hash = 1590301345)
    public Image() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImageId() {
        return this.imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFavorites() {
        return this.favorites;
    }

    public void setFavorites(String favorites) {
        this.favorites = favorites;
    }

    public String getComments() {
        return this.comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
