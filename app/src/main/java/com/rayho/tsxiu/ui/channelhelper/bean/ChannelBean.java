package com.rayho.tsxiu.ui.channelhelper.bean;

/**
 * Created by goach on 2016/9/28.
 */

public class ChannelBean {

    private String tabName;//分类的名字
    private String key;//分类的id
    private int editStatus;
    private int tabType;


    public String getTabName() {
        return tabName;
    }

    public void setTabName(String tabName) {
        this.tabName = tabName;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getTabType() {
        return tabType;
    }

    public void setTabType(int tabType) {
        this.tabType = tabType;
    }

    public int getEditStatus() {
        return editStatus;
    }

    public void setEditStatus(int editStatus) {
        this.editStatus = editStatus;
    }
}
