package com.rayho.tsxiu.module_news.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Rayho on 2019/3/11
 * 新闻缓存
 **/
@Entity
public class NewsCache {
    @Id(autoincrement = true)
    private Long id;//主键

    @Unique
    @NotNull
    private String fileName;//文件名称

    @NotNull
    private String cid;//新闻的类型id

    @Generated(hash = 1323479580)
    public NewsCache(Long id, @NotNull String fileName, @NotNull String cid) {
        this.id = id;
        this.fileName = fileName;
        this.cid = cid;
    }

    @Generated(hash = 1992898982)
    public NewsCache() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getCid() {
        return this.cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }
}
