package com.rayho.tsxiu.module_mine.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.github.chrisbanes.photoview.OnViewTapListener;
import com.github.chrisbanes.photoview.PhotoView;
import com.rayho.tsxiu.R;
import com.rayho.tsxiu.ui.SlideCloseLayout;
import com.rayho.tsxiu.utils.GlideUtils;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 头像详细查看
 */
public class FaceDetailActivity extends AppCompatActivity {

    @BindView(R.id.photoview)
    PhotoView mPhotoView;

    @BindView(R.id.container_scl)
    SlideCloseLayout mContainerScl;

    private String mPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_detail);

        ButterKnife.bind(this);

        initSlideCloseLayout();
        initFace();
    }


    private void initSlideCloseLayout() {
        //设置activity的背景为黑色
        getWindow().getDecorView().setBackgroundColor(Color.BLACK);
        //给控件设置需要渐变的背景。如果没有设置这个，则背景不会变化
        mContainerScl.setGradualBackground(getWindow().getDecorView().getBackground());
        //设置监听，滑动一定距离后让Activity结束
        mContainerScl.setLayoutScrollListener(new SlideCloseLayout.LayoutScrollListener() {
            @Override
            public void onLayoutClosed() {
                onBackPressed();
            }
        });
    }


    /**
     * 设置图片
     */
    private void initFace() {
        mPhotoPath = getIntent().getStringExtra("path");
        if (!TextUtils.isEmpty(mPhotoPath)) {
            GlideUtils.loadImageInOriginalSize(this, mPhotoPath, mPhotoView);
        }
        mPhotoView.setOnViewTapListener(new OnViewTapListener() {
            @Override
            public void onViewTap(View view, float x, float y) {
                finish();
            }
        });
    }
}
