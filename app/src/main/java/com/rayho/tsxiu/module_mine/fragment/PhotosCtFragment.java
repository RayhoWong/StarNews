package com.rayho.tsxiu.module_mine.fragment;


import android.view.View;
import android.view.ViewStub;

import com.rayho.tsxiu.R;
import com.rayho.tsxiu.base.BaseDataBindingApater;
import com.rayho.tsxiu.base.LazyLoadFragment;
import com.rayho.tsxiu.module_mine.adapter.PhotosCtAdapter;
import com.rayho.tsxiu.module_mine.viewmodel.PhotosCtViewModel;
import com.rayho.tsxiu.module_photo.dao.Image;
import com.rayho.tsxiu.utils.DaoManager;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;

/**
 * 图片的收藏
 */
public class PhotosCtFragment extends LazyLoadFragment {

    @BindView(R.id.rcv)
    RecyclerView mRcv;

    @BindView(R.id.viewStub)
    public ViewStub mViewStub;

    private List<BaseDataBindingApater.Items> items = new ArrayList<>();

    private PhotosCtAdapter mAdapter;



    public static PhotosCtFragment newInstance(){
        PhotosCtFragment fragment = new PhotosCtFragment();
        return fragment;
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_photos_ct;
    }


    @Override
    public void loadData() {
        getPhotosFromDB();
    }


    /**
     * 从数据库获取图片
     */
    private void getPhotosFromDB(){
        DaoManager.getInstance().getDaoSession().getImageDao()
                .rx()
                .loadAll()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(images -> {
                    if (images != null && images.size() > 0) {
                        for (Image bean : images) {
                            PhotosCtViewModel model = new PhotosCtViewModel(bean);
                            items.add(model);
                        }
                        mAdapter = new PhotosCtAdapter(this);
                        mAdapter.setItems(items);
                        mRcv.setLayoutManager(new LinearLayoutManager(getActivity()));
                        mRcv.setAdapter(mAdapter);
                    } else if (images != null && images.size() == 0) {
                        //无数据 显示无数据布局
                        mViewStub.setVisibility(View.VISIBLE);
                    }
                });
    }

}
