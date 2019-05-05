package com.rayho.tsxiu.module_mine.viewmodel;

import com.rayho.tsxiu.BR;
import com.rayho.tsxiu.R;
import com.rayho.tsxiu.base.BaseDataBindingApater;
import com.rayho.tsxiu.base.Constant;
import com.rayho.tsxiu.module_photo.bean.PhotoBean;
import com.rayho.tsxiu.module_photo.dao.Image;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

/**
 * Created by Rayho on 2019/4/31
 * 图片的viewmodel
 **/
public class PhotosCtViewModel extends BaseObservable implements BaseDataBindingApater.Items {

    private Image photo;//数据库返回的图片实例

    private String id;//图片id

    private String url;//图片地址

    private String date;//发布日期

    private String title;//标题

    private String favorites;//点赞数

    private String comments;//评论数


    public PhotosCtViewModel(Image photo) {
        this.photo = photo;
        initData();
    }


    private void initData() {
        id = photo.getImageId();
        url = photo.getUrl();
        date = photo.getDate();
        title = photo.getTitle();
        favorites = photo.getFavorites();
        comments = photo.getComments();
    }


    @Override
    public int getLayoutType() {
        return R.layout.item_photo2;
    }


    @Bindable
    public Image getPhoto() {
        return photo;
    }

    public void setPhoto(Image photo) {
        this.photo = photo;
        notifyPropertyChanged(BR.photo);
    }



    @Bindable
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
        notifyPropertyChanged(BR.id);
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
