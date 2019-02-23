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
 *
 **/
public class BaseDataBindingApater {}/*<T> extends RecyclerView.Adapter<BaseDataBindingApater.ItemViewHolder> {

    public List<T> items = new ArrayList<>();

    *//**
     * 根据viewType创建不同的布局
     * @param parent
     * @param layout 子类的getItemView方法直接返回布局id
     * @return
     *//*
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


    ///////////////////////item的操作方法/////////////////////////////////////
    public List<T> getItems() {
        return items;
    }

    public void setItem(T item) {
        clearItems();
        addItem(item);
    }

    public void setItems(List<T> items) {
        clearItems();
        addItems(items);
    }

    public void addItem(T item) {
        items.add(item);
    }

    public void addItem(T item, int index) {
        items.add(index, item);
    }

    public void addItems(List<T> items) {
        this.items.addAll(items);
    }

    public void removeItem(T item) {
        items.remove(item);
    }

    public void clearItems() {
        items.clear();
    }
    ///////////////////////////////////////////////////////////////////////////////////



    public class ItemViewHolder extends RecyclerView.ViewHolder {
        private final ViewDataBinding binding;

        public ItemViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindTo(T item) {
            binding.setVariable(BR.item, item);
            binding.executePendingBindings();
        }
    }*/

