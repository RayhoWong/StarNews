package com.rayho.tsxiu.module_news.viewmodel;

import com.rayho.tsxiu.BR;
import com.rayho.tsxiu.module_news.bean.NewsBean;
import com.rayho.tsxiu.utils.DateUtils;

import java.util.List;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

/**
 * Created by Rayho on 2019/2/22
 **/
public class NewsViewModel extends BaseObservable {

    private NewsBean.DataBean news;
    private String title;
    private String user;
    private String commentCounts;
    private String time;
    private String nums;
    private List<String> imageUrls;


    public NewsViewModel(NewsBean.DataBean news) {
        this.news = news;
        initData();
    }

    private void initData() {
        title = news.title;
        user = news.posterScreenName;
        commentCounts = String.valueOf(news.commentCount) + "评论";
        time = DateUtils.convertTimeToFormat(news.publishDate);
        imageUrls = news.imageUrls;
        if (imageUrls == null || imageUrls.size() == 0) {
            nums = "";
        } else {
            nums = String.valueOf(news.imageUrls.size()) + " 图";
        }
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
    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
        notifyPropertyChanged(BR.user);
    }

    @Bindable
    public String getCommentCounts() {
        return commentCounts;
    }

    public void setCommentCounts(String commentCounts) {
        this.commentCounts = commentCounts;
        notifyPropertyChanged(BR.commentCounts);
    }

    @Bindable
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
        notifyPropertyChanged(BR.time);
    }

    @Bindable
    public String getNums() {
        return nums;
    }

    public void setNums(String nums) {
        this.nums = nums;
        notifyPropertyChanged(BR.nums);
    }

    @Bindable
    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
        notifyPropertyChanged(BR.imageUrls);
    }

}
