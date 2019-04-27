package com.rayho.tsxiu.module_mine.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Rayho on 2019/4/25
 **/
@Entity
public class User {

    @Id(autoincrement = true)
    private Long id;

    @NotNull
    @Unique
    private String uid;//用户id

    @NotNull
    @Unique
    private String name;//用户名

    @NotNull
    private String imgName;//图片地址

    @Generated(hash = 256412163)
    public User(Long id, @NotNull String uid, @NotNull String name,
            @NotNull String imgName) {
        this.id = id;
        this.uid = uid;
        this.name = name;
        this.imgName = imgName;
    }

    @Generated(hash = 586692638)
    public User() {
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

    public String getImgName() {
        return this.imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    public String getUid() {
        return this.uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
