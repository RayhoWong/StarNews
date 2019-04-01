package com.rayho.tsxiu.module_photo.viewmodel;

import android.text.TextUtils;

import com.rayho.tsxiu.BR;
import com.rayho.tsxiu.R;
import com.rayho.tsxiu.base.BaseDataBindingApater;
import com.rayho.tsxiu.base.Constant;
import com.rayho.tsxiu.module_photo.bean.PhotoBean;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

/**
 * Created by Rayho on 2019/3/31
 * 图片的viewmodel
 **/
public class PhotosViewModel extends BaseObservable implements BaseDataBindingApater.Items {

    private PhotoBean.FeedListBean photo;

    private String url;//图片地址

    private String date;//发布日期

    private String title;//标题

    private String favorites;//点赞数

    private String comments;//评论数


    public PhotosViewModel(PhotoBean.FeedListBean photo) {
        this.photo = photo;
        initData();
    }


    private void initData() {
        if (photo.images != null && photo.images.size() > 0) {
            PhotoBean.FeedListBean.ImagesBean image = photo.images.get(0);
            //图片地址 = https://photo.tuchong.com/ + user_id +/f/ + img_id + .jpg
            //eg: https://photo.tuchong.com/1673709/f/25389444.jpg
            url = Constant.TUCHONG_PHOTO_URL + image.user_id + "/f/" + image.img_id + ".jpg";
        }
        date = photo.passed_time;
        title = photo.excerpt;
        favorites = String.valueOf(photo.favorites);
        comments = String.valueOf(photo.comments);
    }


    @Override
    public int getType() {
        return R.layout.item_photo;
    }


    @Bindable
    public PhotoBean.FeedListBean getPhoto() {
        return photo;
    }

    public void setPhoto(PhotoBean.FeedListBean photo) {
        this.photo = photo;
        notifyPropertyChanged(BR.photo);
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
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
        notifyPropertyChanged(BR.date);
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
    public String getFavorites() {
        return favorites;
    }

    public void setFavorites(String favorites) {
        this.favorites = favorites;
        notifyPropertyChanged(BR.favorites);
    }

    @Bindable
    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
        notifyPropertyChanged(BR.comments);
    }

}
