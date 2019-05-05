package com.rayho.tsxiu.module_mine.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.github.chrisbanes.photoview.PhotoView;
import com.rayho.tsxiu.R;
import com.rayho.tsxiu.ui.SlideCloseLayout;
import com.rayho.tsxiu.utils.FileUtil;
import com.rayho.tsxiu.utils.GlideUtils;
import com.rayho.tsxiu.utils.NetworkUtils;
import com.rayho.tsxiu.utils.PermissionSettingPage;
import com.rayho.tsxiu.utils.RxUtil;
import com.rayho.tsxiu.utils.ToastUtil;
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity;
import com.yanzhenjie.permission.AndPermission;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;

/**
 * 图片详细查看
 */
public class ImageDetailActivity extends RxAppCompatActivity {

    @BindView(R.id.photoview)
    PhotoView mPhotoView;

    @BindView(R.id.container_scl)
    SlideCloseLayout mContainerScl;

    @BindView(R.id.iv_download)
    ImageView mIvDownload;

    private String mUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);

        ButterKnife.bind(this);
        initSlideCloseLayout();
        initImage();
    }


    private void initSlideCloseLayout() {
        //设置activity的背景为黑色
        getWindow().getDecorView().setBackgroundColor(Color.BLACK);
        //给控件设置需要渐变的背景。如果没有设置这个，则背景不会变化
        mContainerScl.setGradualBackground(getWindow().getDecorView().getBackground());
        //设置监听，滑动一定距离后让Activity结束
        mContainerScl.setLayoutScrollListener(() -> onBackPressed());
    }


    private void initImage() {
        mUrl = getIntent().getStringExtra("url");
        if (!TextUtils.isEmpty(mUrl)) {
            GlideUtils.loadImageInOriginalSize(this, mUrl, mPhotoView);
        }
        mPhotoView.setOnViewTapListener((view, x, y) -> finish());
    }


    @OnClick(R.id.iv_download)
    public void onViewClicked() {
        checkPermission();
    }


    /**
     * 检查是否有sd卡读写权限
     * 有则保存图片
     */
    private void checkPermission() {
        AndPermission.with(this)
                .runtime()
                .permission(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .onGranted(data -> {
                    //已获取权限
                    saveImage();
                })
                .onDenied(data -> {
                    //判断用户是不是选中不再显示权限弹窗了，若不再显示的话提醒进入权限设置页
                    if (AndPermission.hasAlwaysDeniedPermission(ImageDetailActivity.this, data)) {
                        //提醒打开权限设置页
                        ToastUtil toast = new ToastUtil(ImageDetailActivity.this, getString(R.string.permission_request_tip));
                        toast.show();
                        PermissionSettingPage.start(ImageDetailActivity.this, false);
                    } else {
                        ToastUtil toast = new ToastUtil(ImageDetailActivity.this, "你拒绝了该权限");
                        toast.show();
                    }
                }).start();
    }


    /**
     * 保存图片
     * glide下载图片 复制图片保存到本地
     */
    @SuppressLint("CheckResult")
    private void saveImage() {
        if (!NetworkUtils.isConnected(ImageDetailActivity.this)) {
            ToastUtil toast = new ToastUtil(ImageDetailActivity.this, "网络不可用");
            toast.show();
            return;
        }
        Observable
                .create((ObservableOnSubscribe<File>) emitter -> {
                    //glide下载的图片文件
                    File sourceFile = Glide.with(ImageDetailActivity.this)
                            .load(mUrl)
                            .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                            .get();
                    //保存目录的目标文件
                    File destFile = null;
                    if (sourceFile != null) {
                        //获取到下载得到的图片，进行本地保存到sd卡
                        File pictureFolder = Environment.getExternalStorageDirectory();
                        //第二个参数为你想要保存的目录名称
                        File appDir = new File(pictureFolder, "starnews");
                        if (!appDir.exists()) {
                            //创建目标文件目录
                            appDir.mkdirs();
                        }
                        String fileName = System.currentTimeMillis() + ".jpg";
                        //创建目标文件实例
                        destFile = new File(appDir, fileName);
                        //把glide下载得到图片复制到目标文件中
                        FileUtil.copyFile(sourceFile, destFile);
                    }
                    emitter.onNext(destFile);
                })
                .compose(RxUtil.<File>rxSchedulerHelper())
                .compose(this.bindToLifecycle())
                .subscribe(destFile -> {
                    if (destFile.length() > 0) {
                        ToastUtil toast = new ToastUtil(ImageDetailActivity.this, "图片保存至" + destFile.getPath());
                        toast.show();
                        // 最后通知图库更新
                        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                                Uri.fromFile(new File(destFile.getPath()))));
                    } else {
                        ToastUtil toast = new ToastUtil(ImageDetailActivity.this, "图片保存失败");
                        toast.show();
                    }
                });
    }
}
