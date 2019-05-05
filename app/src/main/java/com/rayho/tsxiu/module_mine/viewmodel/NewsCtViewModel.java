package com.rayho.tsxiu.module_mine.viewmodel;

import com.rayho.tsxiu.BR;
import com.rayho.tsxiu.R;
import com.rayho.tsxiu.base.BaseDataBindingApater;
import com.rayho.tsxiu.module_news.dao.News;
import com.rayho.tsxiu.utils.DateUtils;

import java.util.List;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

/**
 * Created by Rayho on 2019/4/28
 **/
public class NewsCtViewModel extends BaseObservable implements BaseDataBindingApater.Items {

    private News news;

    private String nid;
    private String url;
    private String title;
    private String user;
    private String commentCounts;
    private String time;
    private String nums;
    private List<String> imageUrls;
    //新闻的类型(只有文字 单图 3图 组图)
    private int type;


    public NewsCtViewModel(News news) {
        this.news = news;
        initData();
    }


    private void initData() {
        type = news.getType();
        nid = news.getNid();
        title = news.getTitle();
        user = news.getPosterScreenName();
        commentCounts = String.valueOf(news.getCommentCount()) + "评论";
        time = DateUtils.convertTimeToFormat(news.getPublishDate());
        imageUrls = news.getImageUrls();
        if (imageUrls == null || imageUrls.size() == 0) {
            nums = "";
        } else {
            nums = String.valueOf(news.getImageUrls().size()) + " 图";
        }
        url = news.getUrl();
    }


    /**
     * 根据type返回item的
     *
     * @return
     */
    @Override
    public int getLayoutType() {
        switch (type) {
            case 0://无图
                return R.layout.item_news_no_photo2;
            case 1://单图
                return R.layout.item_news_single_photo2;
            case 2://三图
                return R.layout.item_news_three_photo2;
            case 3://组图
                return R.layout.item_news_multiple_photo2;
        }
        return R.layout.item_news_no_photo2;
    }


    @Bindable
    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
        notifyPropertyChanged(BR.nid);
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

    @Bindable
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
        notifyPropertyChanged(BR.type);
    }


    @Bindable
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
        notifyPropertyChanged(BR.url);
    }

}
