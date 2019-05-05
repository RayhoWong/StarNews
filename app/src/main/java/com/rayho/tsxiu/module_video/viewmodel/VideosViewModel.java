package com.rayho.tsxiu.module_video.viewmodel;

import com.rayho.tsxiu.BR;
import com.rayho.tsxiu.R;
import com.rayho.tsxiu.base.BaseDataBindingApater;
import com.rayho.tsxiu.module_video.bean.VideoBean;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

/**
 * Created by Rayho on 2019/4/13
 **/
public class VideosViewModel extends BaseObservable implements BaseDataBindingApater.Items {

    private VideoBean.DataListBean.ContListBean mData;

    private String title;//标题

    private String duration;//时长

    private String cover;//封面

    private String url;//视频地址

    private String commentTimes;//评论数

    private String praiseTimes;//点赞数

    private String avator;//作者头像

    private String nickname;//作者名字


    public VideosViewModel(VideoBean.DataListBean.ContListBean mData) {
        this.mData = mData;
        initData();
    }


    private void initData(){
        title = mData.name;
        duration = mData.duration;
        cover = mData.pic;
        if(mData.videos.get(0) != null){
            url = mData.videos.get(0).url;
        }
        commentTimes = mData.commentTimes;
        praiseTimes = mData.praiseTimes;
        avator = mData.userInfo.pic;
        nickname = mData.userInfo.nickname;
    }


    @Bindable
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        notifyPropertyChanged(BR.title);
    }

    @Bindable
    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
        notifyPropertyChanged(BR.duration);
    }

    @Bindable
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
        notifyPropertyChanged(BR.url);
    }

    @Bindable
    public String getCommentTimes() {
        return commentTimes;
    }

    public void setCommentTimes(String commentTimes) {
        this.commentTimes = commentTimes;
        notifyPropertyChanged(BR.commentTimes);
    }

    @Bindable
    public String getPraiseTimes() {
        return praiseTimes;
    }

    public void setPraiseTimes(String praiseTimes) {
        this.praiseTimes = praiseTimes;
        notifyPropertyChanged(BR.praiseTimes);
    }

    @Bindable
    public String getAvator() {
        return avator;
    }

    public void setAvator(String avator) {
        this.avator = avator;
        notifyPropertyChanged(BR.avator);
    }

    @Bindable
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
        notifyPropertyChanged(BR.nickname);
    }

    @Bindable
    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
        notifyPropertyChanged(BR.cover);
    }


    @Override
    public int getLayoutType() {
        return R.layout.item_video;
    }
}
