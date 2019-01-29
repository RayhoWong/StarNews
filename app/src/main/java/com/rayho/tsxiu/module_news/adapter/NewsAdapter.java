package com.rayho.tsxiu.module_news.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rayho.tsxiu.R;
import com.rayho.tsxiu.module_news.bean.NewsBean;
import com.rayho.tsxiu.module_news.fragment.ContentFragment;
import com.rayho.tsxiu.utils.GlideUtils;

import java.util.List;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Rayho on 2019/1/28
 **/
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyViewHolder> {
    List<NewsBean.DataBean> mList;
    ContentFragment mContext;



    public NewsAdapter(List<NewsBean.DataBean> mList, ContentFragment mContext) {
        this.mList = mList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext.getContext()).inflate(R.layout.item_news_multiple_photo, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
//        bindNoPhoto(holder, position);
//        bindSinglePhoto(holder,position);
//        bindThreePhoto(holder, position);
        bindMultiplePhoto(holder,position);
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    /**
     * 纯文字
     *
     * @param holder
     * @param position
     */
    private void bindNoPhoto(MyViewHolder holder, int position) {
        NewsBean.DataBean bean = mList.get(position);
        holder.mTvTitle.setText(bean.title);
        holder.mTvComments.setText(String.valueOf(bean.commentCount) + "评论");
        holder.mTvTime.setText(bean.publishDateStr);
    }

    /**
     * 单图
     *
     * @param holder
     * @param position
     */
    private void bindSinglePhoto(MyViewHolder holder, int position) {
        NewsBean.DataBean bean = mList.get(position);
        holder.mTvTitle.setText(bean.title);
        holder.mTvComments.setText(String.valueOf(bean.commentCount) + "评论");
        holder.mTvTime.setText(bean.publishDateStr);
//        GlideUtils.loadRoundCircleImage(
//                mContext.getContext(),bean.imageUrls.get(position),holder.mIvCover);
    }

    /**
     * @param holder
     * @param position
     */
    private void bindThreePhoto(MyViewHolder holder, int position) {
        NewsBean.DataBean bean = mList.get(position);
        holder.mTvTitle.setText(bean.title);
        holder.mTvComments.setText(String.valueOf(bean.commentCount) + "评论");
        holder.mTvTime.setText(bean.publishDateStr);
//        GlideUtils.loadRoundCircleImage(
//                mContext.getContext(), bean.imageUrls.get(1), holder.mIv_1);
//        GlideUtils.loadRoundCircleImage(
//                mContext.getContext(), bean.imageUrls.get(2), holder.mIv_2);
//        GlideUtils.loadRoundCircleImage(
//                mContext.getContext(), bean.imageUrls.get(4), holder.mIv_3);
    }

    /**
     *
     * @param holder
     * @param position
     */
    private void bindMultiplePhoto(MyViewHolder holder, int position) {
        NewsBean.DataBean bean = mList.get(position);
        holder.mTvTitle.setText(bean.title);
        holder.mTvComments.setText(String.valueOf(bean.commentCount) + "评论");
        holder.mTvTime.setText(bean.publishDateStr);
        holder.mIvImageNums.setText(String.valueOf(bean.imageUrls.size())+"图");
        GlideUtils.loadRoundCircleImage(
                mContext.getContext(),bean.imageUrls.get(new Random().nextInt(5)),holder.mIvMiddle);
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
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

//        @BindView(R.id.iv_cover)
//        ImageView mIvCover;

       /* @BindView(R.id.image_1)
        ImageView mIv_1;
        @BindView(R.id.image_2)
        ImageView mIv_2;
        @BindView(R.id.image_3)
        ImageView mIv_3;*/

        @BindView(R.id.iv_middle)
        ImageView mIvMiddle;
        @BindView(R.id.tv_image_nums)
        TextView mIvImageNums;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
