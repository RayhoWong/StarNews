package com.rayho.tsxiu.base;

import androidx.annotation.DrawableRes;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import android.os.PersistableBundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.rayho.tsxiu.R;
import com.rayho.tsxiu.app.AppApplication;
import com.rayho.tsxiu.utils.StatusBarUtil;

import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private TextView mTvTitle;//中间的主标题
    private TextView mTvSubTitle;//toolbar右边的标题
    private ImageView mIvRightImage;//toolbar右边的图标
    private View mRootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRootView = View.inflate(this, R.layout.activity_base, null);
        addContent(getLayoutId());
        setContentView(mRootView);

        initToolBar();
        //默认6.0及以上改字体颜色(白色背景->深色字体)
        StatusBarUtil.changeStatusBarTextColor(this);
        ButterKnife.bind(this);
        afterSetContentView();
    }


    @Override
    protected void onStart() {
        super.onStart();
        //默认设置左上角返回
        showBack();
    }

    /**
     * 把子类Activity的布局加载到Base布局的FrameLayout里面,activity无须在xml引用toolbar布局
     * @param layoutId
     */
    private void addContent(int layoutId) {
        FrameLayout contentView = mRootView.findViewById(R.id.fl_content);
        View content = View.inflate(this, layoutId, null);
        if (content != null) {
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT);
            contentView.addView(content, params);
        }
    }

    /**
     * 获取子类布局的id
     * @return
     */
    public abstract int getLayoutId();

    /**
     * 加载完布局的后续操作
     */
    public abstract void afterSetContentView();

    private void initToolBar() {
        mToolbar = findViewById(R.id.toolbar);
        mTvTitle = findViewById(R.id.title);
        mTvSubTitle = findViewById(R.id.subTitle);
        mIvRightImage = findViewById(R.id.rightImage);

       /* if (mToolbar != null) {
            //将Toolbar显示到界面
            setSupportActionBar(mToolbar);
        }*/

        if (mTvTitle != null) {
            mTvTitle.setText(getTitle());
        }
    }


    public Toolbar getToolbar() {
        return mToolbar;
    }

    /**
     * 某些界面可能需要定制toolbar 那么就隐藏默认Base的toolbar
     */
    public void hideBaseToolbar(){
        if(mToolbar.getVisibility() == View.VISIBLE){
            mToolbar.setVisibility(View.GONE);
        }
    }

    public TextView getToolbarTitle() {
        return mTvTitle;
    }

    public void setToolbarTitle(String title) {
        if (!TextUtils.isEmpty(title)) {
            mTvTitle.setText(title);
        }
    }

    public TextView getToolbarSubTitle() {
        return mTvSubTitle;
    }

    public void setToolbarSubTitle(String title) {
        if (mIvRightImage.getVisibility() == View.VISIBLE || mTvSubTitle.getVisibility() == View.GONE
                && !TextUtils.isEmpty(title)) {
            mIvRightImage.setVisibility(View.GONE);
            mTvSubTitle.setVisibility(View.VISIBLE);
            mTvSubTitle.setText(title);
        }
    }

    /**
     * 返回右上角图标
     *
     * @return
     */
    public ImageView getRightImage() {
        return mIvRightImage;
    }

    /**
     * 设置右上角的图标
     * @param image
     */
    public void setRightImage(@DrawableRes int image) {
        if (mTvSubTitle.getVisibility() == View.VISIBLE || mIvRightImage.getVisibility() == View.GONE
                && image != 0) {
            mTvSubTitle.setVisibility(View.GONE);
            mIvRightImage.setVisibility(View.VISIBLE);
            mIvRightImage.setImageResource(image);
        }
    }

    /**
     * toolbar返回键的设置
     */
    private void showBack() {
        if (mToolbar != null && isShowBacking()) {
            mToolbar.setNavigationIcon(R.mipmap.arrow_back);
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }
    }

    /**
     * 是否设置返回(子类可以重写)
     * @return
     */
    public boolean isShowBacking() {
        return true;
    }

}
