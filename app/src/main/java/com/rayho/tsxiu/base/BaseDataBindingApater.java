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
 * 支持多类型布局 只需要在子类实现getItemViewType方法返回布局id
 **/
public class BaseDataBindingApater extends RecyclerView.Adapter<BaseDataBindingApater.ItemViewHolder> {

    public List<Items> items = new ArrayList<>();

    //相当于BaseViewModel 子类viewmodel实现该接口
    public interface Items {
        int getType();
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getType();
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
            binding.executePendingBindings();
        }
    }

    ///////////////////////item的操作方法/////////////////////////////////////
    public List<Items> getItems() {
        return items;
    }

    public void setItem(Items item) {
        clearItems();
        addItem(item);
    }

    public void setItems(List<Items> items) {
        clearItems();
        addItems(items);
    }

    public void addItem(Items item) {
        items.add(item);
    }

    public void addItem(Items item, int index) {
        items.add(index, item);
    }

    public void addItems(List<Items> items) {
        this.items.addAll(items);
    }

    public void removeItem(Items item) {
        items.remove(item);
    }

    public void clearItems() {
        items.clear();
    }
    ///////////////////////////////////////////////////////////////////////////////////
}
