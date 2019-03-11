package com.rayho.tsxiu.module_news.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Rayho on 2019/3/9
 * 缓存新闻的文件名
 **/
@Entity
public class FilesName {
    @Id(autoincrement = true)
    private Long id;//主键

    @Unique
    @NotNull
    private String name;//文件名称

    @Generated(hash = 1494897053)
    public FilesName(Long id, @NotNull String name) {
        this.id = id;
        this.name = name;
    }

    @Generated(hash = 427404527)
    public FilesName() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
