package com.rayho.tsxiu.module_photo.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;

import com.blankj.rxbus.RxBus;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.rayho.tsxiu.R;
import com.rayho.tsxiu.base.Constant;
import com.rayho.tsxiu.base.Presenter;
import com.rayho.tsxiu.databinding.ActivityPhotoDetailBinding;
import com.rayho.tsxiu.greendao.ImageDao;
import com.rayho.tsxiu.module_photo.bean.PhotoBean;
import com.rayho.tsxiu.module_photo.dao.Image;
import com.rayho.tsxiu.module_photo.fragment.ImageFragment;
import com.rayho.tsxiu.module_photo.viewmodel.PhotoDetailViewModel;
import com.rayho.tsxiu.ui.SlideCloseLayout;
import com.rayho.tsxiu.utils.DaoManager;
import com.rayho.tsxiu.utils.FileUtil;
import com.rayho.tsxiu.utils.NetworkUtils;
import com.rayho.tsxiu.utils.PermissionSettingPage;
import com.rayho.tsxiu.utils.RxUtil;
import com.rayho.tsxiu.utils.ToastUtil;
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;


public class PhotoDetailActivity extends RxAppCompatActivity implements Presenter {

    public ActivityPhotoDetailBinding mBinding;

    private PhotoDetailViewModel mViewModel;

    private List<Fragment> mFragments = new ArrayList<>();

    private List<String> mUrls = new ArrayList<>();

    private List<String> mIds = new ArrayList<>();

    private String mNumber;// 当前的页数/总页数

    private String mUrl;//当前显示图片的URL

    private String mId;//当前显示图片的id


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_photo_detail);
        mBinding.setPresenter(this);
        mBinding.setVm(mViewModel = new PhotoDetailViewModel(mNumber));

        getUrls();
        initSlideCloseLayout();
        initViewPager();
    }


    private void getUrls() {
        RxBus.getDefault().subscribeSticky(this, "feed", new RxBus.Callback<PhotoBean.FeedListBean>() {
            @Override
            public void onEvent(PhotoBean.FeedListBean feedListBean) {
                if (feedListBean != null) {
                    if (feedListBean.images != null && feedListBean.images.size() > 0) {
                        for (PhotoBean.FeedListBean.ImagesBean bean : feedListBean.images) {
                            //图片地址 = https://photo.tuchong.com/ + user_id +/f/ + img_id + .jpg
                            //eg: https://photo.tuchong.com/1673709/f/25389444.jpg
                            mUrls.add(Constant.TUCHONG_PHOTO_URL + bean.user_id + "/f/" + bean.img_id + ".jpg");
                            mIds.add(String.valueOf(bean.img_id));
                        }
                    }
                }
            }
        });
    }


    private void initSlideCloseLayout() {
        //设置activity的背景为黑色
        getWindow().getDecorView().setBackgroundColor(Color.BLACK);
        //给控件设置需要渐变的背景。如果没有设置这个，则背景不会变化
        mBinding.containerScl.setGradualBackground(getWindow().getDecorView().getBackground());
        //设置监听，滑动一定距离后让Activity结束
        mBinding.containerScl.setLayoutScrollListener(new SlideCloseLayout.LayoutScrollListener() {
            @Override
            public void onLayoutClosed() {
                onBackPressed();
            }
        });
    }


    private void initViewPager() {
        for (String mUrl : mUrls) {
            mFragments.add(ImageFragment.newInstance(mUrl));
        }
        ImageAdapter adapter = new ImageAdapter(getSupportFragmentManager());
        mBinding.viewpager.setAdapter(adapter);
        //默认第一页选中 设值
        mUrl = mUrls.get(0);
        mId = mIds.get(0);
        mViewModel.number.set(1 + "/" + mUrls.size());
        mBinding.viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //动态监听页面的变化
                mViewModel.number.set(String.valueOf(position + 1) + "/" + mUrls.size());
                mUrl = mUrls.get(position);
                mId = mIds.get(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_close:
                finish();
                break;
            case R.id.iv_download:
                checkPermission();
                break;
            case R.id.iv_share:
                shareImage();
                break;
            case R.id.iv_collect:
                collectImage();
                break;
        }
    }


    /**
     * 分享图片的链接
     */
    private void shareImage() {
        if (TextUtils.isEmpty(mUrl)) {
            return;
        } else {
            Intent textIntent = new Intent(Intent.ACTION_SEND);
            textIntent.setType("text/plain");
            textIntent.putExtra(Intent.EXTRA_TEXT, mUrl);
            startActivity(Intent.createChooser(textIntent, "分享到..."));
        }
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
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        //已获取权限
                        saveImage();
                    }
                })
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        //判断用户是不是选中不再显示权限弹窗了，若不再显示的话提醒进入权限设置页
                        if (AndPermission.hasAlwaysDeniedPermission(PhotoDetailActivity.this, data)) {
                            //提醒打开权限设置页
                            ToastUtil toast = new ToastUtil(PhotoDetailActivity.this, getString(R.string.permission_request_tip));
                            toast.show();
                            PermissionSettingPage.start(PhotoDetailActivity.this, false);
                        } else {
                            ToastUtil toast = new ToastUtil(PhotoDetailActivity.this, "你拒绝了该权限");
                            toast.show();
                        }
                    }
                }).start();
    }


    /**
     * 保存图片
     * 从glide中获取缓存图片 复制图片保存到本地
     */
    @SuppressLint("CheckResult")
    private void saveImage() {
        if (!NetworkUtils.isConnected(PhotoDetailActivity.this)) {
            ToastUtil toast = new ToastUtil(PhotoDetailActivity.this, "网络不可用");
            toast.show();
            return;
        }
        Observable
                .create(new ObservableOnSubscribe<File>() {
                    @Override
                    public void subscribe(ObservableEmitter<File> emitter) throws Exception {
                        //glide缓存获取的原文件
                        File sourceFile = Glide.with(PhotoDetailActivity.this)
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
                                //创建文件目录
                                appDir.mkdirs();
                            }
                            String fileName = System.currentTimeMillis() + ".jpg";
                            destFile = new File(appDir, fileName);
                            //把glide下载得到图片复制到定义好的目录中去
                            FileUtil.copyFile(sourceFile, destFile);
                        }
                        emitter.onNext(destFile);
                    }
                })
                .compose(RxUtil.<File>rxSchedulerHelper())
                .compose(this.<File>bindToLifecycle())
                .subscribe(new Consumer<File>() {
                    @Override
                    public void accept(File destFile) throws Exception {
                        if (destFile != null) {
                            ToastUtil toast = new ToastUtil(PhotoDetailActivity.this, "图片保存至" + destFile.getPath());
                            toast.show();
                            // 最后通知图库更新
                            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                                    Uri.fromFile(new File(destFile.getPath()))));
                        } else {
                            ToastUtil toast = new ToastUtil(PhotoDetailActivity.this, "图片保存失败");
                            toast.show();
                        }
                    }
                });
    }


    /**
     * 收藏图片(保存到数据库)
     */
    private void collectImage() {
        if (TextUtils.isEmpty(mId) || TextUtils.isEmpty(mUrl)) {
            ToastUtil toast = new ToastUtil(this, "收藏失败");
            toast.show();
            return;
        }

        DaoManager.getInstance().getDaoSession().getImageDao()
                .queryBuilder()
                .where(ImageDao.Properties.ImageId.eq(mId))
                .rx()
                .unique()
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                .subscribe(new Action1<Image>() {
                    @Override
                    public void call(Image image) {
                        if (image != null) {
                            //图片存在
                            ToastUtil toast = new ToastUtil(PhotoDetailActivity.this, "图片已收藏过");
                            toast.show();
                        } else {
                            //图片不存在 插入图片
                            DaoManager.getInstance().getDaoSession().getImageDao()
                                    .rx()
                                    .insertOrReplace(new Image(null, mId, mUrl))
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Action1<Image>() {
                                        @Override
                                        public void call(Image image) {
                                            if (image != null) {
                                                ToastUtil toast = new ToastUtil(PhotoDetailActivity.this, "图片收藏成功");
                                                toast.show();
                                            }else {
                                                ToastUtil toast = new ToastUtil(PhotoDetailActivity.this, "图片收藏失败");
                                                toast.show();
                                            }
                                        }
                                    });
                        }
                    }
                });
    }


    private class ImageAdapter extends FragmentStatePagerAdapter {

        public ImageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.getDefault().unregister(this);
    }
}
