package com.rayho.tsxiu.module_mine.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;

import com.blankj.utilcode.util.SPUtils;
import com.rayho.tsxiu.R;
import com.rayho.tsxiu.base.Constant;
import com.rayho.tsxiu.greendao.VideoAutoPlayDao;
import com.rayho.tsxiu.module_video.dao.VideoAutoPlay;
import com.rayho.tsxiu.utils.DaoManager;
import com.rayho.tsxiu.utils.StatusBarUtil;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import skin.support.SkinCompatManager;
import skin.support.widget.SkinCompatSupportable;

/**
 * 设置
 */
public class SettingsActivity extends AppCompatActivity implements SkinCompatSupportable {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.switch_autoplay)
    SwitchCompat mSwitchAutoplay;

    @BindView(R.id.switch_nightmode)
    SwitchCompat mSwitchNightmode;

    @BindView(R.id.rl_clearCache)
    RelativeLayout mRlClearCache;

    private VideoAutoPlay mVideoAutoPlay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ButterKnife.bind(this);
        initView();
    }


    private void initView() {
        //根据夜间模式的设置 设置状态栏的颜色
        if (SPUtils.getInstance(Constant.SP_SETTINGS).getBoolean(getString(R.string.sp_nightmode))) {
            //夜间模式true 黑色
            StatusBarUtil.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark_night), this);
        } else {
            //夜间模式false 白色
            StatusBarUtil.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark), this);
        }

        mToolbar.setNavigationIcon(R.mipmap.arrow_back_2);
        mToolbar.setTitle(getString(R.string.settings));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        initAutoPlay();
        initNightMode();
    }


    /**
     * 视频自动播放
     */
    private void initAutoPlay() {
        DaoManager.getInstance().getDaoSession().getVideoAutoPlayDao()
                .queryBuilder()
                .where(VideoAutoPlayDao.Properties.Vid.eq(Constant.AUTO_PLAY_ID))
                .rx()
                .unique()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(videoAutoPlay -> {
                    boolean autoplay;//自动播放标记
                    if (videoAutoPlay != null) {
                        mVideoAutoPlay = videoAutoPlay;
                        autoplay = videoAutoPlay.getAutoplay();
                        mSwitchAutoplay.setChecked(autoplay);
                    }
                });

        mSwitchAutoplay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    //关闭
                    if (mVideoAutoPlay != null) {
                        mVideoAutoPlay.setAutoplay(false);
                        DaoManager.getInstance().getDaoSession().getVideoAutoPlayDao()
                                .rx()
                                .update(mVideoAutoPlay)
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe();
                    }
                } else {
                    //开启
                    if (mVideoAutoPlay != null) {
                        mVideoAutoPlay.setAutoplay(true);
                        DaoManager.getInstance().getDaoSession().getVideoAutoPlayDao()
                                .rx()
                                .update(mVideoAutoPlay)
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe();
                    }
                }

            }
        });
    }


    /**
     * 夜间模式切换
     */
    private void initNightMode() {
        mSwitchNightmode.setChecked(SPUtils.getInstance(Constant.SP_SETTINGS).getBoolean(getString(R.string.sp_nightmode)));
        mSwitchNightmode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                SkinCompatManager.getInstance().loadSkin(Constant.SKIN_NIGHT, SkinCompatManager.SKIN_LOADER_STRATEGY_BUILD_IN); // 后缀加载
                SPUtils.getInstance(Constant.SP_SETTINGS).put(getString(R.string.sp_nightmode), true);
            } else {
                SkinCompatManager.getInstance().restoreDefaultTheme();
                SPUtils.getInstance(Constant.SP_SETTINGS).put(getString(R.string.sp_nightmode), false);
            }
        });
    }


    @OnClick({R.id.rl_clearCache})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_clearCache:

                break;
        }
    }


    /**
     * 动态监听换肤行为 设置状态栏颜色
     */
    @Override
    public void applySkin() {
        if (SPUtils.getInstance(Constant.SP_SETTINGS).getBoolean(getString(R.string.sp_nightmode))) {
            //夜间模式true 黑色
            StatusBarUtil.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark_night), this);
        } else {
            //夜间模式false 白色
            StatusBarUtil.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark), this);
        }
    }
}
