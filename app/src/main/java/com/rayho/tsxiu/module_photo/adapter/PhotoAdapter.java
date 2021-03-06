package com.rayho.tsxiu.module_photo.adapter;

import android.view.View;

import com.blankj.rxbus.RxBus;
import com.blankj.utilcode.util.ActivityUtils;
import com.rayho.tsxiu.R;
import com.rayho.tsxiu.base.BaseDataBindingApater;
import com.rayho.tsxiu.module_photo.activity.PhotoDetailActivity;
import com.rayho.tsxiu.module_photo.bean.PhotoBean;
import com.rayho.tsxiu.module_photo.viewmodel.PhotosViewModel;

/**
 * Created by Rayho on 2019/3/31
 * 图片展示的adapter
 **/
public class PhotoAdapter extends BaseDataBindingApater {

    public PhotoAdapter() {}

    @Override
    public void onBindViewHolder(ItemViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhotosViewModel model = (PhotosViewModel) items.get(position);
                PhotoBean.FeedListBean feedListBean = model.getPhoto();
                RxBus.getDefault().postSticky(feedListBean,"feed");
                ActivityUtils.startActivity(PhotoDetailActivity.class);
            }
        });


        holder.itemView.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeItem(position);
            }
        });

    }
}
