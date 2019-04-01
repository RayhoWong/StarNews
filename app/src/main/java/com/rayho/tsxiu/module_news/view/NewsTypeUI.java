package com.rayho.tsxiu.module_news.view;

import android.view.ViewGroup;

import com.rayho.tsxiu.module_news.adapter.NewsAdapter;
import com.rayho.tsxiu.module_news.bean.NewsBean;

import androidx.annotation.NonNull;

/**
 * Created by Rayho on 2019/1/30
 * 根据新闻的类型创建不同的布局
 **/
public interface NewsTypeUI {

    NewsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType);

    void onBindViewHolder(@NonNull NewsAdapter.MyViewHolder holder, int position, NewsBean.DataBean dataBean);
}
