package com.goach.tabdemo.bean;

/**
 * Created by goach on 2016/9/28.
 */

public class ChannelBean {

    private int editStatus;
    private String tabName;
    private int tabType;
    public String getTabName() {
        return tabName;
    }

    public void setTabName(String tabName) {
        this.tabName = tabName;
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
