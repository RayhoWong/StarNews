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

import androidx.annotation.NonNull;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Rayho on 2019/1/30
 * 新闻三张图片
 * 2.24更新 此类只做参考 无引用
 **/
public class NewsThreePhoto implements NewsTypeUI {
    private ContentFragment mContext;

    public NewsThreePhoto(ContentFragment mContext) {
        this.mContext = mContext;
    }

    @Override
    public NewsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news_three_photo, parent, false);
        ThreePhotoViewHolder holder = new ThreePhotoViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull NewsAdapter.MyViewHolder holder, int position, NewsBean.DataBean dataBean) {
        ThreePhotoViewHolder threePhotoViewHolder;
        if (holder instanceof ThreePhotoViewHolder) {
            threePhotoViewHolder = (ThreePhotoViewHolder) holder;
        } else {
            return;
        }
        if (!TextUtils.isEmpty(dataBean.title)) {
            threePhotoViewHolder.mTvTitle.setText(dataBean.title);
        }
        threePhotoViewHolder.mTvComments.setText(String.valueOf(dataBean.commentCount) + "评论");
        threePhotoViewHolder.mTvTime.setText(DateUtils.convertTimeToFormat(dataBean.publishDate));
        if(dataBean.imageUrls != null && dataBean.imageUrls.size() >= 3){
            GlideUtils.loadRoundCircleImage(
                    mContext.getActivity(), dataBean.imageUrls.get(0), threePhotoViewHolder.mIv_1);
            GlideUtils.loadRoundCircleImage(
                    mContext.getActivity(), dataBean.imageUrls.get(1), threePhotoViewHolder.mIv_2);
            GlideUtils.loadRoundCircleImage(
                    mContext.getActivity(), dataBean.imageUrls.get(3), threePhotoViewHolder.mIv_3);
        }
        threePhotoViewHolder.mIvMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public static class ThreePhotoViewHolder extends NewsAdapter.MyViewHolder {
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

        @BindView(R.id.image_1)
        ImageView mIv_1;
        @BindView(R.id.image_2)
        ImageView mIv_2;
        @BindView(R.id.image_3)
        ImageView mIv_3;

        public ThreePhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
