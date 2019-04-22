package com.rayho.tsxiu.module_video.viewmodel;


import com.blankj.rxbus.RxBus;
import com.rayho.tsxiu.base.BaseDataBindingApater;
import com.rayho.tsxiu.base.Constant;
import com.rayho.tsxiu.module_video.adapter.VideoAdapter;
import com.rayho.tsxiu.module_video.bean.VideoBean;
import com.rayho.tsxiu.module_video.retrofit.VideosLoader;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

public class VideoTabViewModel {

    private VideoAdapter mAdapter;

    public VideoTabViewModel(VideoAdapter adapter) {
        mAdapter = adapter;
    }


    /**
     * 获取视频
     * start:分页值 下拉刷新=默认为0 上拉加载=pageNumber*10
     *
     * @return 返回一个Observable
     */
    public Observable<VideoBean> getVideosObservable(String start) {
        return VideosLoader.getInstance().getVideos(start);
    }


    /**
     * 设置视频列表
     *
     * @param list        视频数据
     * @param refreshType 获取数据的方式（下拉/上拉）
     */
    public void setVideos(List<VideoBean.DataListBean.ContListBean> list, int refreshType) {
        List<BaseDataBindingApater.Items> mItems = new ArrayList<>();

        if (list != null && list.size() >= 0) {
            for (VideoBean.DataListBean.ContListBean bean : list) {
                mItems.add(new VideosViewModel(bean));
            }
            if (refreshType == Constant.REFRESH_DATA) {
                //下拉刷新
                //提醒用户更新数据的数量
                RxBus.getDefault().postSticky(mItems.size(), "updateNums");
                mAdapter.setItems(mItems);
            } else if (refreshType == Constant.LOAD_MORE_DATA) {
                //加载更多
                mAdapter.addItems(mItems);
            }
        }

    }


}
