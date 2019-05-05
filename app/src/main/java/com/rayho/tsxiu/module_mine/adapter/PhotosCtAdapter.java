package com.rayho.tsxiu.module_mine.adapter;

import android.content.Intent;
import android.view.View;

import com.rayho.tsxiu.R;
import com.rayho.tsxiu.base.BaseDataBindingApater;
import com.rayho.tsxiu.greendao.ImageDao;
import com.rayho.tsxiu.module_mine.activity.ImageDetailActivity;
import com.rayho.tsxiu.module_mine.fragment.PhotosCtFragment;
import com.rayho.tsxiu.module_mine.viewmodel.PhotosCtViewModel;
import com.rayho.tsxiu.utils.DaoManager;
import com.rayho.tsxiu.utils.RxUtil;
import com.rayho.tsxiu.utils.ToastUtil;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;

/**
 * Created by Rayho on 2019/4/30
 * 图片收藏
 **/
public class PhotosCtAdapter extends BaseDataBindingApater {

    private PhotosCtFragment mFragment;


    public PhotosCtAdapter(PhotosCtFragment mFragment) {
        this.mFragment = mFragment;
    }


    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);


        holder.itemView.setOnClickListener(v -> {
            PhotosCtViewModel model = (PhotosCtViewModel) items.get(position);
            Intent intent = new Intent(mFragment.getActivity(), ImageDetailActivity.class);
            intent.putExtra("url", model.getUrl());
            mFragment.startActivity(intent);
        });


        holder.itemView.findViewById(R.id.iv_close).setOnClickListener(v ->
        {
            PhotosCtViewModel model = (PhotosCtViewModel) items.get(position);
            //取消收藏(删除图片)
            Observable.create((ObservableOnSubscribe<String>) emitter -> {
                DaoManager.getInstance().getDaoSession().getImageDao()
                        .queryBuilder()
                        .where(ImageDao.Properties.ImageId.eq(model.getId()))
                        .buildDelete()
                        .executeDeleteWithoutDetachingEntities();
                emitter.onNext("success");
            })
                    .compose(RxUtil.rxSchedulerHelper())
                    .compose(mFragment.bindToLifecycle())
                    .subscribe(s -> {
                        if (s.equals("success")) {
                            removeItem(position);
                            ToastUtil toast = new ToastUtil(mFragment.getActivity(), "已取消收藏该图片");
                            toast.show();
                            if (items.size() == 0) {
                                //显示无数据布局
                                mFragment.mViewStub.setVisibility(View.VISIBLE);
                            }
                        }
                    });
        });

    }
}