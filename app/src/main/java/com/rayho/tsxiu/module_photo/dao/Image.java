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


    @Generated(hash = 940484217)
    public Image(Long id, @NotNull String imageId, @NotNull String url) {
        this.id = id;
        this.imageId = imageId;
        this.url = url;
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
}
