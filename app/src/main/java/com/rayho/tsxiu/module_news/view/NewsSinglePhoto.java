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
 * 新闻单图
 **/
public class NewsSinglePhoto implements NewsTypeUI {
    private ContentFragment mContext;

    public NewsSinglePhoto(ContentFragment mContext) {
        this.mContext = mContext;
    }

    @Override
    public NewsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news_single_photo,parent,false);
        SinglePhotoViewHolder holder = new SinglePhotoViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull NewsAdapter.MyViewHolder holder, int position, NewsBean.DataBean dataBean) {
        SinglePhotoViewHolder singlePhotoViewHolder;
        if(holder instanceof SinglePhotoViewHolder){
            singlePhotoViewHolder = (SinglePhotoViewHolder) holder;
        }else {
            return;
        }
        if (!TextUtils.isEmpty(dataBean.title)) {
            singlePhotoViewHolder.mTvTitle.setText(dataBean.title);
        }
        singlePhotoViewHolder.mTvComments.setText(String.valueOf(dataBean.commentCount) + "评论");
        singlePhotoViewHolder.mTvTime.setText(DateUtils.convertTimeToFormat(dataBean.publishDate));
        if(dataBean.imageUrls != null && dataBean.imageUrls.size() > 0)
        GlideUtils.loadRoundCircleImage(
                mContext.getActivity(),dataBean.imageUrls.get(0),singlePhotoViewHolder.mIvCover);
        singlePhotoViewHolder.mIvMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

       /* private void bindSinglePhoto(MyViewHolder holder, int position) {
        NewsBean.DataBean bean = mList.get(position);
        holder.mTvTitle.setText(bean.title);
        holder.mTvComments.setText(String.valueOf(bean.commentCount) + "评论");
        holder.mTvTime.setText(bean.publishDateStr);
        GlideUtils.loadRoundCircleImage(
                mContext.getContext(),bean.imageUrls.get(position),holder.mIvCover);
    }*/

    public static class SinglePhotoViewHolder extends NewsAdapter.MyViewHolder{
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

        @BindView(R.id.iv_cover)
        ImageView mIvCover;

        public SinglePhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
