package com.rayho.tsxiu.module_news.view;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rayho.tsxiu.R;
import com.rayho.tsxiu.module_news.NewsTypeUI;
import com.rayho.tsxiu.module_news.adapter.NewsAdapter;
import com.rayho.tsxiu.module_news.bean.NewsBean;
import com.rayho.tsxiu.module_news.fragment.ContentFragment;
import com.rayho.tsxiu.utils.DateUtils;
import com.rayho.tsxiu.utils.GlideUtils;

import java.util.Random;

import androidx.annotation.NonNull;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Rayho on 2019/1/30
 * 新闻组图(中间显示一张大图)
 **/
public class NewsMultiplePhoto implements NewsTypeUI {
    private ContentFragment mContext;

    public NewsMultiplePhoto(ContentFragment mContext) {
        this.mContext = mContext;
    }

    @Override
    public NewsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news_multiple_photo, parent, false);
        MultiplePhotoViewHolder holder = new MultiplePhotoViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull NewsAdapter.MyViewHolder holder, int position, NewsBean.DataBean dataBean) {
        MultiplePhotoViewHolder multiplePhotoViewHolder;
        if (holder instanceof MultiplePhotoViewHolder) {
            multiplePhotoViewHolder = (MultiplePhotoViewHolder) holder;
        } else {
            return;
        }
        if (!TextUtils.isEmpty(dataBean.title)) {
            multiplePhotoViewHolder.mTvTitle.setText(dataBean.title);
        }
        multiplePhotoViewHolder.mTvComments.setText(String.valueOf(dataBean.commentCount) + "评论");
        multiplePhotoViewHolder.mTvTime.setText(DateUtils.convertTimeToFormat(dataBean.publishDate));
        if (dataBean.imageUrls != null && dataBean.imageUrls.size() > 3) {
            multiplePhotoViewHolder.mIvImageNums.setText(String.valueOf(dataBean.imageUrls.size()) + "图");
            GlideUtils.loadImage(
                    mContext.getActivity(), dataBean.imageUrls.get(0), multiplePhotoViewHolder.mIvMiddle);
        }
        multiplePhotoViewHolder.mIvMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public static class MultiplePhotoViewHolder extends NewsAdapter.MyViewHolder {
        @BindView(R.id.tv_title)
        TextView mTvTitle;
        @BindView(R.id.tv_user)
        TextView mTvUser;
        @BindView(R.id.tv_comments)
        TextView mTvComments;
        @BindView(R.id.tv_time)
        TextView mTvTime;
        @BindView(R.id.iv_more)
        ImageView mIvMore;
        @BindView(R.id.iv_middle)
        ImageView mIvMiddle;
        @BindView(R.id.tv_image_nums)
        TextView mIvImageNums;

        public MultiplePhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
