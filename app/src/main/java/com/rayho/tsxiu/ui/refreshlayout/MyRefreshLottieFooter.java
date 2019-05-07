package com.rayho.tsxiu.ui.refreshlayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.lcodecore.tkrefreshlayout.IBottomView;
import com.lcodecore.tkrefreshlayout.IHeaderView;
import com.lcodecore.tkrefreshlayout.OnAnimEndListener;
import com.orhanobut.logger.Logger;
import com.rayho.tsxiu.R;
import com.rayho.tsxiu.utils.RxTimer;

import androidx.annotation.NonNull;
import skin.support.widget.SkinCompatBackgroundHelper;
import skin.support.widget.SkinCompatSupportable;

/**
 * Created by Rayho on 2019/1/18
 * 自定义带有lottie动画的Footer(用于RefreshLayout)
 **/
public class MyRefreshLottieFooter extends LinearLayout implements IBottomView, SkinCompatSupportable {

    private LottieAnimationView mLottieAnimationView;

    private LinearLayout mLlFooter;

    private SkinCompatBackgroundHelper mBackgroundTintHelper;


    public MyRefreshLottieFooter(Context context) {
        super(context);
        initView(context);
    }

    public MyRefreshLottieFooter(Context context, AttributeSet attrs) {
        super(context, attrs);
        mBackgroundTintHelper = new SkinCompatBackgroundHelper(this);
        mBackgroundTintHelper.loadFromAttributes(attrs, 0);
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.refreshlayout_lottie_footer, this);
        mLottieAnimationView = view.findViewById(R.id.loading_footer);
        mLlFooter = view.findViewById(R.id.ll_footer);
    }
    /**
     * 返回视图，不能返回Null
     *
     * @return
     */
    @NonNull
    @Override
    public View getView() {
        return this;
    }

    @Override
    public void onPullingUp(float fraction, float maxBottomHeight, float bottomHeight) {}

    @Override
    public void startAnim(float maxHeadHeight, float headHeight) {
        mLottieAnimationView.playAnimation();
    }

    @Override
    public void onFinish() {
        mLottieAnimationView.cancelAnimation();
    }

    @Override
    public void reset() {}

    @Override
    public void onPullReleasing(float fraction, float maxHeadHeight, float headHeight) {}

    /**
     * 通过json文件设置动画
     * @param name 文件名
     */
    public void setAnimationJson(String name) {
        mLottieAnimationView.setAnimation(name);
    }


    @Override
    public void applySkin() {
        Logger.d("hahahahahaha");
        if (mBackgroundTintHelper != null) {
            mBackgroundTintHelper.applySkin();
        }
    }
}
