package com.rayho.tsxiu.module_news.adapter;

import android.view.View;
import android.view.ViewGroup;

import com.rayho.tsxiu.base.Constant;
import com.rayho.tsxiu.module_news.bean.NewsBean;
import com.rayho.tsxiu.module_news.fragment.ContentFragment;
import com.rayho.tsxiu.module_news.view.NewsMultiplePhoto;
import com.rayho.tsxiu.module_news.view.NewsNoPhoto;
import com.rayho.tsxiu.module_news.view.NewsSinglePhoto;
import com.rayho.tsxiu.module_news.view.NewsThreePhoto;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.ButterKnife;

/**
 * Created by Rayho on 2019/1/28
 * 新闻界面
 **/
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyViewHolder> {
    private List<NewsBean.DataBean> mList;
    private ContentFragment mContext;

    private NewsNoPhoto mNewsNoPhoto;//无图
    private NewsSinglePhoto mNewsSinglePhoto;//单图
    private NewsThreePhoto mNewsThreePhoto;//3张图
    private NewsMultiplePhoto mNewsMultiplePhoto;//组图

    public NewsAdapter(List<NewsBean.DataBean> mList, ContentFragment mContext) {
        this.mList = mList;
        this.mContext = mContext;
    }

    /**
     * 判断每个item的类型（加载不同类型布局的方法）
     * 根据新闻的type属性 返回不同的viewType
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        switch (mList.get(position).type){
            case 0:
                return Constant.TYPE_NO_PHOTO;
            case 1:
                return Constant.TYPE_SINGLE_PHOTO;
            case 2:
                return Constant.TYPE_THREE_PHOTO;
            case 3:
                return Constant.TYPE_MULTIPLE_PHOTO;
            default:
                return super.getItemViewType(position);
        }
    }

    /**
     * 根据viewType创建不同的布局
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == Constant.TYPE_NO_PHOTO){
            mNewsNoPhoto = new NewsNoPhoto(mContext);
            return mNewsNoPhoto.onCreateViewHolder(parent,viewType);
        }else if(viewType == Constant.TYPE_SINGLE_PHOTO){
            mNewsSinglePhoto = new NewsSinglePhoto(mContext);
            return mNewsSinglePhoto.onCreateViewHolder(parent,viewType);
        }else if(viewType == Constant.TYPE_THREE_PHOTO){
            mNewsThreePhoto = new NewsThreePhoto(mContext);
            return mNewsThreePhoto.onCreateViewHolder(parent,viewType);
        }else if(viewType == Constant.TYPE_MULTIPLE_PHOTO){
            mNewsMultiplePhoto = new NewsMultiplePhoto(mContext);
            return mNewsMultiplePhoto.onCreateViewHolder(parent,viewType);
        }else {
            return null;
        }
    }

    /**
     * 根据返回holder的类型 绑定数据
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if(holder instanceof NewsNoPhoto.NoPhotoViewHolder){
            mNewsNoPhoto.onBindViewHolder(holder,position,mList.get(position));
        }else if(holder instanceof NewsSinglePhoto.SinglePhotoViewHolder){
            mNewsSinglePhoto.onBindViewHolder(holder,position,mList.get(position));
        }else if(holder instanceof NewsThreePhoto.ThreePhotoViewHolder){
            mNewsThreePhoto.onBindViewHolder(holder,position,mList.get(position));
        }else if(holder instanceof NewsMultiplePhoto.MultiplePhotoViewHolder){
            mNewsMultiplePhoto.onBindViewHolder(holder,position,mList.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
