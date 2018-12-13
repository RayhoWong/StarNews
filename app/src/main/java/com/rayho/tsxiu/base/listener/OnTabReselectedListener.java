package com.rayho.tsxiu.base.listener;

/**
 * Created by Rayho on 2018/12/13
 * 再次点击当前的Tab 在当前页面(Fragment)重新加载数据(类似于知乎,头条的功能)
 * 等同于手动下拉加载数据
 **/
public interface OnTabReselectedListener {
    //更新数据
    void updateData();
}
