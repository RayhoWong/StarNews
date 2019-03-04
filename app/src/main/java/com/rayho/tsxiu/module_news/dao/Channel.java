package com.rayho.tsxiu.module_news.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Rayho on 2019/3/4
 * dao数据库的频道bean
 **/
@Entity
public class Channel {
    @Id(autoincrement = true)
    private Long id;//主键

    @Unique
    @NotNull
    private String name;//频道名称

    @Unique
    @NotNull
    private String cid;//频道id

    @Generated(hash = 1773821558)
    public Channel(Long id, @NotNull String name, @NotNull String cid) {
        this.id = id;
        this.name = name;
        this.cid = cid;
    }

    @Generated(hash = 459652974)
    public Channel() {
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

    public String getCid() {
        return this.cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }
}
