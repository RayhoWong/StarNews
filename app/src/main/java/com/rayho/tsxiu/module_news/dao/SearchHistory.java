package com.rayho.tsxiu.module_news.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Rayho on 2019/3/26
 **/
@Entity
public class SearchHistory {
    @Id(autoincrement = true)
    Long id;

    @Unique
    @NotNull
    String record;

    @Generated(hash = 1824068001)
    public SearchHistory(Long id, @NotNull String record) {
        this.id = id;
        this.record = record;
    }

    @Generated(hash = 1905904755)
    public SearchHistory() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRecord() {
        return this.record;
    }

    public void setRecord(String record) {
        this.record = record;
    }
}
