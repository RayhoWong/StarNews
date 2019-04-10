package com.rayho.tsxiu.module_photo.fragment;


import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.chrisbanes.photoview.OnViewTapListener;
import com.github.chrisbanes.photoview.PhotoView;
import com.rayho.tsxiu.R;
import com.rayho.tsxiu.module_photo.activity.PhotoDetailActivity;
import com.rayho.tsxiu.utils.GlideUtils;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * 图片查看的fragment
 */
public class ImageFragment extends Fragment {

    private PhotoView mPhotoView;

    private String url;//展示图片的url

    private PhotoDetailActivity mActivity;


    //接收PhotoDetailActivity传进来的url参数
    public static ImageFragment newInstance(String url) {
        ImageFragment fragment = new ImageFragment();
        Bundle args = new Bundle();
        args.putString("url", url);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof  PhotoDetailActivity){
            mActivity = (PhotoDetailActivity) context;
        }
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        url = getArguments().getString("url");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_image, container, false);
        mPhotoView = view.findViewById(R.id.photoview);
        initPhotoView();
        return view;
    }


    private void initPhotoView() {
        if (!TextUtils.isEmpty(url)) {
            GlideUtils.loadImageInOriginalSize(getActivity(), url, mPhotoView);
        } else {
            return;
        }

        mPhotoView.setOnViewTapListener(new OnViewTapListener() {
            @Override
            public void onViewTap(View view, float x, float y) {
                if (mActivity.mBinding == null) {
                    return;
                }
                if (mActivity.mBinding.rlToolbar.getVisibility() == View.VISIBLE) {
                    mActivity.mBinding.rlToolbar.setVisibility(View.INVISIBLE);
                } else {
                    mActivity.mBinding.rlToolbar.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
