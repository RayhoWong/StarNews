package com.rayho.tsxiu.module_news.view;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rayho.tsxiu.R;
import com.rayho.tsxiu.module_news.adapter.NewsAdapter;
import com.rayho.tsxiu.module_news.bean.NewsBean;
import com.rayho.tsxiu.module_news.fragment.ContentFragment;
import com.rayho.tsxiu.utils.DateUtils;

import androidx.annotation.NonNull;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Rayho on 2019/1/30
 * 新闻无图片
 * 2.24更新 此类只做参考 无引用
 **/
public class NewsNoPhoto implements NewsTypeUI {
    private ContentFragment mContext;

    public NewsNoPhoto(ContentFragment mContext) {
        this.mContext = mContext;
    }

    @Override
    public NewsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news_no_photo, parent, false);
        NoPhotoViewHolder holder = new NoPhotoViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull NewsAdapter.MyViewHolder holder, int position, NewsBean.DataBean dataBean) {
        NoPhotoViewHolder noPhotoViewHolder;
        if (holder instanceof NoPhotoViewHolder) {
            noPhotoViewHolder = (NoPhotoViewHolder) holder;
        }else {
            return;
        }
        if (!TextUtils.isEmpty(dataBean.title)) {
            noPhotoViewHolder.mTvTitle.setText(dataBean.title);
        }
        noPhotoViewHolder.mTvComments.setText(String.valueOf(dataBean.commentCount) + "评论");
        noPhotoViewHolder.mTvTime.setText(DateUtils.convertTimeToFormat(dataBean.publishDate));
        noPhotoViewHolder.mIvMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public static class NoPhotoViewHolder extends NewsAdapter.MyViewHolder {
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

        public NoPhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
