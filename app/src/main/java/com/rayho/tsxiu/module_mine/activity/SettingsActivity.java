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
import rx.functions.Action1;

/**
 * 设置
 */
public class SettingsActivity extends AppCompatActivity {

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
        StatusBarUtil.changeStatusBarTextColor(this);
        initView();
    }


    private void initView() {
        mToolbar.setNavigationIcon(R.mipmap.arrow_back_2);
        mToolbar.setTitle(getString(R.string.settings));
        mToolbar.setNavigationOnClickListener(v -> SettingsActivity.this.finish());

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
                    if (mVideoAutoPlay != null) {
                        mVideoAutoPlay.setAutoplay(false);
                        DaoManager.getInstance().getDaoSession().getVideoAutoPlayDao()
                                .rx()
                                .update(mVideoAutoPlay)
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe();
                    }
                } else {
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


    @OnClick({R.id.rl_clearCache})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_clearCache:

                break;
        }
    }
}
