package com.rayho.tsxiu.module_mine.activity;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;

import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.orhanobut.logger.Logger;
import com.rayho.tsxiu.R;
import com.rayho.tsxiu.base.Constant;
import com.rayho.tsxiu.greendao.UserDao;
import com.rayho.tsxiu.module_mine.dao.User;
import com.rayho.tsxiu.utils.DaoManager;
import com.rayho.tsxiu.utils.GlideUtils;
import com.rayho.tsxiu.utils.PermissionSettingPage;
import com.rayho.tsxiu.utils.PhotoFromAlbumHelper;
import com.rayho.tsxiu.utils.StatusBarUtil;
import com.rayho.tsxiu.utils.ToastUtil;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;

import java.io.File;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class EditInfoActivity extends AppCompatActivity {

    @BindView(R.id.rightImage)
    ImageView mIvSave;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.iv_face)
    ImageView mIvFace;

    @BindView(R.id.et_name)
    TextInputEditText mEtName;

    @BindView(R.id.til_name)
    TextInputLayout mTilName;

    private static int CODE_PHOTO_FROM_CAMERA = 0;//拍照的请求码

    private static int CODE_PHOTO_FROM_ALBUM = 1;//相册的请求码

    private String mName;//用户名

    private File mCameraSavePath;//拍照照片路径

    private String mPhotoPath;//图片路径

    private String mNameDB;//数据库返回的用户名

    private Uri mUri;

    private User mUser;//默认用户

    private boolean mFlagName = true;//用户名是否合法的标记



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);
        ButterKnife.bind(this);
        StatusBarUtil.changeStatusBarTextColor(this);

        initView();
        initUser();
    }


    private void initView() {
        mToolbar.setTitle(getString(R.string.edit_user_info));
        mToolbar.setTitleTextColor(getResources().getColor(R.color.tab_text_nor));
        mIvSave.setVisibility(View.VISIBLE);
        mIvSave.setImageResource(R.mipmap.sure);
        mToolbar.setNavigationIcon(R.mipmap.close_2);
        mToolbar.setNavigationOnClickListener(v -> finish());
        //提示用户名的最大字数
        mTilName.setCounterEnabled(true);
        mTilName.setCounterMaxLength(10);
    }


    /**
     * 初始化用户
     */
    private void initUser() {
        String url = "https://photo.tuchong.com/1673709/f/25389444.jpg";
        GlideUtils.loadCircleImage(this, url, mIvFace);
        //初始化默认用户的信息
        DaoManager.getInstance().getDaoSession().getUserDao()
                .queryBuilder()
                .where(UserDao.Properties.Uid.eq(Constant.DEFAULT_USER_ID))
                .rx()
                .unique()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(user -> {
                    if (user != null) {
                        mEtName.setText(user.getName());
                        GlideUtils.loadCircleImage(this, user.getImgName(), mIvFace);
//                        mPhotoPathDB = user.getImgName();
                        mNameDB = user.getName();
                        mUser = user;
                        validateName();
                    }
                });
    }


    /**
     * 检查用户名
     */
    private void validateName() {
        mEtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //必须调用 否则错误信息一直不变
                mTilName.setErrorEnabled(false);

                if (TextUtils.isEmpty(s.toString())) {
                    mFlagName = false;
                    showError(mTilName, "用户名不能为空");
                } else if (s.toString().length() > 10) {
                    mFlagName = false;
                    showError(mTilName, "用户名长度须小于等于10");
                } else {
                    mFlagName = true;
                }
            }
        });
    }


    /**
     * 显示错误提示，并获取焦点
     *
     * @param textInputLayout
     * @param error
     */
    private void showError(TextInputLayout textInputLayout, String error) {
        textInputLayout.setError(error);
        textInputLayout.getEditText().setFocusable(true);
        textInputLayout.getEditText().setFocusableInTouchMode(true);
        textInputLayout.getEditText().requestFocus();
    }


    @OnClick({R.id.rightImage, R.id.iv_face})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rightImage:
                saveInfo();
                break;
            case R.id.iv_face:
                checkPhotoPermission();
                break;
        }
    }


    /**
     * 检查权限
     */
    private void checkPhotoPermission() {
        AndPermission.with(this)
                .runtime()
                .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA)
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        //已授权
                        selectPhoto();
                    }
                })
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        //判断用户是不是选中不再显示权限弹窗了，若不再显示的话提醒进入权限设置页
                        if (AndPermission.hasAlwaysDeniedPermission(EditInfoActivity.this, data)) {
                            //提醒打开权限设置页
                            ToastUtil toast = new ToastUtil(EditInfoActivity.this, "需要获取您的相册、照相使用权限");
                            toast.show();
                            PermissionSettingPage.start(EditInfoActivity.this, false);
                        } else {
                            ToastUtil toast = new ToastUtil(EditInfoActivity.this, "你拒绝了该权限");
                            toast.show();
                        }
                    }
                }).start();
    }


    /**
     * 头像获取方式
     */
    private void selectPhoto() {
        new MaterialDialog.Builder(EditInfoActivity.this)
                .title("选择头像")
                .items(new String[]{"拍照", "相册"})
                .itemsGravity(GravityEnum.CENTER)
                .itemsColor(getResources().getColor(R.color.tab_text_nor))
                .itemsCallback((dialog, itemView, position, text) -> {
                    switch (position) {
                        case 0:
                            goCamera();
                            break;
                        case 1:
                            goPhotoAlbum();
                            break;
                    }
                })
                .autoDismiss(true)
                .canceledOnTouchOutside(true)
                .show();
    }


    /**
     * 拍照获取头像
     */
    private void goCamera() {
        mCameraSavePath = new File(Environment.getExternalStorageDirectory().getPath() + "/" + System.currentTimeMillis() + ".jpg");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //第二个参数为 包名.fileprovider
            mUri = FileProvider.getUriForFile(EditInfoActivity.this, "com.rayho.tsxiu.fileprovider", mCameraSavePath);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            mUri = Uri.fromFile(mCameraSavePath);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mUri);
        startActivityForResult(intent, CODE_PHOTO_FROM_CAMERA);
    }


    /**
     * 相册选择头像
     */
    private void goPhotoAlbum() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, CODE_PHOTO_FROM_ALBUM);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CODE_PHOTO_FROM_CAMERA && resultCode == RESULT_OK) {
            //拍照
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                mPhotoPath = String.valueOf(mCameraSavePath);
            } else {
                mPhotoPath = mUri.getEncodedPath();
            }
            Logger.d("拍照返回的图片路径: " + mPhotoPath);
            GlideUtils.loadCircleImage(this, mPhotoPath, mIvFace);

        } else if (requestCode == CODE_PHOTO_FROM_ALBUM && resultCode == RESULT_OK) {
            //相册
            mPhotoPath = PhotoFromAlbumHelper.getRealPathFromUri(this, data.getData());
            Logger.d("相册选择返回的图片路径: " + mPhotoPath);
            GlideUtils.loadCircleImage(this, mPhotoPath, mIvFace);
        }
    }


    /**
     * 保存信息
     */
    private void saveInfo() {
        mName = mEtName.getText().toString().trim();
        //用户名和头像都不变 直接finish
        if (mName.equals(mNameDB) && mPhotoPath == null) {
            finish();
        }
        //更新用户名
        if (!mName.equals(mNameDB)) {
            if (mFlagName && mUser != null) {
                mUser.setName(mName);
            }
        }
        //更新头像
        if (mPhotoPath != null && mUser != null) {
            mUser.setImgName(mPhotoPath);
        }
        //更新用户
        DaoManager.getInstance().getDaoSession().getUserDao()
                .rx()
                .update(mUser)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<User>() {
                    @Override
                    public void call(User user) {
                        if (user != null) {
                            finish();
                        }
                    }
                });
    }

}
