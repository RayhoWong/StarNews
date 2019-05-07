package com.rayho.tsxiu.ui.refreshlayout;

import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.blankj.utilcode.util.SPUtils;
import com.lcodecore.tkrefreshlayout.IHeaderView;
import com.lcodecore.tkrefreshlayout.OnAnimEndListener;
import com.orhanobut.logger.Logger;
import com.rayho.tsxiu.R;
import com.rayho.tsxiu.base.Constant;
import com.rayho.tsxiu.utils.RxTimer;
import com.rayho.tsxiu.utils.StatusBarUtil;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import skin.support.widget.SkinCompatBackgroundHelper;
import skin.support.widget.SkinCompatSupportable;

/**
 * Created by Rayho on 2019/1/15
 * 自定义带有lottie动画的Header(用于RefreshLayout)
 **/
public class MyRefreshLottieHeader extends LinearLayout implements IHeaderView, SkinCompatSupportable {

    private Context mContext;

    private LottieAnimationView mLottieAnimationView;

    private LinearLayout mLlUpdateNumber;

    private TextView mTvUpdateNumber;

    private String msg;//HEADER的提示信息

    private SkinCompatBackgroundHelper mBackgroundTintHelper;//对应background属性


    public void setMsg(String msg) {
        this.msg = msg;
    }

    public MyRefreshLottieHeader(Context context) {
        super(context);
        mContext = context;
        initView(context);

    }

    public MyRefreshLottieHeader(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mBackgroundTintHelper = new SkinCompatBackgroundHelper(this);
        mBackgroundTintHelper.loadFromAttributes(attrs, 0);
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.refreshlayout_lottie_header, this);
        mLottieAnimationView = view.findViewById(R.id.loading_header);
        mLlUpdateNumber = view.findViewById(R.id.ll_update_number);
        mTvUpdateNumber = view.findViewById(R.id.tv_update_number);

        mLlUpdateNumber.setBackgroundResource(R.color.colorAccent);
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
    public void startAnim(float maxHeadHeight, float headHeight) {
        mLottieAnimationView.playAnimation();
    }

    /**
     * 刷新结束后 显示已更新数据的数量
     *
     * @param animEndListener
     */
    @Override
    public void onFinish(final OnAnimEndListener animEndListener) {
        mLottieAnimationView.cancelAnimation();
        mLottieAnimationView.setVisibility(GONE);
        mLlUpdateNumber.setVisibility(VISIBLE);
        mTvUpdateNumber.setText(msg);
        //刷新成功播放系统提示音
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone rt = RingtoneManager.getRingtone(mContext, uri);
        rt.play();
        //延迟2秒提醒消失
        RxTimer timer = new RxTimer();
        timer.timer(1500, new RxTimer.RxAction() {
            @Override
            public void action() {
                //Header的收缩动画
                animEndListener.onAnimEnd();
            }
        });
    }

    /**
     * 重置方法 onFinish后调用
     */
    @Override
    public void reset() {
        mLlUpdateNumber.setVisibility(GONE);
        mLottieAnimationView.setVisibility(VISIBLE);
    }

    @Override
    public void onPullingDown(float fraction, float maxHeadHeight, float headHeight) {
    }

    @Override
    public void onPullReleasing(float fraction, float maxHeadHeight, float headHeight) {
    }

    /**
     * 通过json文件设置动画
     *
     * @param name 文件名
     */
    public void setAnimationJson(String name) {
        mLottieAnimationView.setAnimation(name);
    }


    /**
     * 监听换肤行为 动态更换背景色
     */
    @Override
    public void applySkin() {
        if (mBackgroundTintHelper != null) {
            mBackgroundTintHelper.applySkin();
        }
    }
}
