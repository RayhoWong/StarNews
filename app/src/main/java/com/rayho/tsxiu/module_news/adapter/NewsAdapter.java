package com.rayho.tsxiu.module_news.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rayho.tsxiu.R;
import com.rayho.tsxiu.base.Constant;
import com.rayho.tsxiu.module_news.bean.NewsBean;
import com.rayho.tsxiu.module_news.viewmodel.NewsViewModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.databinding.library.baseAdapters.BR;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Rayho on 2019/1/28
 * 新闻界面
 **/
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyViewHolder> {
    private List<NewsBean.DataBean> list;

   /* private NewsNoPhoto mNewsNoPhoto;//无图
    private NewsSinglePhoto mNewsSinglePhoto;//单图
    private NewsThreePhoto mNewsThreePhoto;//3张图
    private NewsMultiplePhoto mNewsMultiplePhoto;//组图*/

    public NewsAdapter() {}

    public void setNews(List<NewsBean.DataBean> list){
        this.list = list;
    }

    /**
     * 判断每个item的类型（加载不同类型布局的方法）
     * 根据新闻的type属性 返回不同的viewType
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        switch (list.get(position).type){
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
        //////////////////////////普通写法(非databinding)//////////////////////////////////
        /*if(viewType == Constant.TYPE_NO_PHOTO){
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
        }*/
        ////////////////////////////////////////////////////////////////////
        ViewDataBinding binding;
        MyViewHolder holder = null;
        switch (viewType){
            case 0:
                binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),R.layout.item_news_no_photo,parent,false);
                holder = new MyViewHolder(binding.getRoot());
                holder.setBinding(binding);
                return holder;
            case 1:
                binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),R.layout.item_news_single_photo,parent,false);
                holder = new MyViewHolder(binding.getRoot());
                holder.setBinding(binding);
                return holder;
            case 2:
                binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),R.layout.item_news_three_photo,parent,false);
                holder = new MyViewHolder(binding.getRoot());
                holder.setBinding(binding);
                return holder;
            case 3:
                binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),R.layout.item_news_multiple_photo,parent,false);
                holder = new MyViewHolder(binding.getRoot());
                holder.setBinding(binding);
                return holder;
        }
        return holder;
    }

    /**
     * 根据返回holder的类型 绑定数据
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //////////////////////////普通写法(非databinding)//////////////////////////////////
       /* if(holder instanceof NewsNoPhoto.NoPhotoViewHolder){
            mNewsNoPhoto.onBindViewHolder(holder,position,mList.get(position));
        }else if(holder instanceof NewsSinglePhoto.SinglePhotoViewHolder){
            mNewsSinglePhoto.onBindViewHolder(holder,position,mList.get(position));
        }else if(holder instanceof NewsThreePhoto.ThreePhotoViewHolder){
            mNewsThreePhoto.onBindViewHolder(holder,position,mList.get(position));
        }else if(holder instanceof NewsMultiplePhoto.MultiplePhotoViewHolder){
            mNewsMultiplePhoto.onBindViewHolder(holder,position,mList.get(position));
        }*/
        ///////////////////////////////////////////////////////////////////////////////////
        NewsViewModel model = new NewsViewModel(list.get(position));
        holder.getBinding().setVariable(BR.item,model);
        holder.getBinding().executePendingBindings();//防止画面闪烁
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private ViewDataBinding binding;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public ViewDataBinding getBinding() {
            return binding;
        }

        public void setBinding(ViewDataBinding binding) {
            this.binding = binding;
        }
    }
}
