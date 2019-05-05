package com.rayho.tsxiu.base;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.rayho.tsxiu.BR;

import java.util.ArrayList;
import java.util.List;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Rayho on 2019/2/23
 * 使用DataBinding方式的基类adapter
 * 支持单类型/多类型布局 只需要在子类实现getItemViewType方法返回布局id
 * 子类adapter必须调用setItems方法给items赋值;
 **/
public class BaseDataBindingApater extends RecyclerView.Adapter<BaseDataBindingApater.ItemViewHolder> {
    //相当于BaseViewModel 绑定的viewmodel必须实现该接口作为子类
    public interface Items {
        //返回布局Id
        int getLayoutType();
    }

    public List<Items> items = new ArrayList<>();

    /**
     * 子类adapter必须调用 传值
     * @param items
     */
    public void setItems(List<Items> items) {
        this.items = items;
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getLayoutType();
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int layout) {
        ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                layout, parent, false);
        return new ItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        holder.bindTo(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }


    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        private final ViewDataBinding binding;

        public ItemViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindTo(Items item) {
            binding.setVariable(BR.item, item);
            //防止画面闪烁
            binding.executePendingBindings();
        }

        public ViewDataBinding getBinding(){
            return binding;
        }
    }

    ///////////////////////items的操作方法/////////////////////////////////////////////
    public List<Items> getItems() {
        return items;
    }

    public void addItem(Items item) {
        items.add(item);
        notifyDataSetChanged();
    }

    public void addItem(Items item, int position) {
        items.add(position, item);
        notifyDataSetChanged();
    }

    public void addItems(List<Items> items) {
        this.items.addAll(items);
        notifyDataSetChanged();
    }


    private long curClickTime;//当前点击删除按钮的时间
    private long lastClickTime;//上一次点击删除按钮的时间
    public void removeItem(int position) {
        //防止快速删除item 导致app崩溃
        //两次点击的间隔必须 >= 250ms
        curClickTime = System.currentTimeMillis();
        if(curClickTime - lastClickTime >= 250){
            lastClickTime = curClickTime;
            items.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position,items.size());
        }else {
            return;
        }
    }

    public void clearItems() {
        items.clear();
        notifyDataSetChanged();
    }
    ///////////////////////////////////////////////////////////////////////////////////
}
