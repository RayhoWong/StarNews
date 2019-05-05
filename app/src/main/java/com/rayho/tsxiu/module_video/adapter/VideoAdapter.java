package com.rayho.tsxiu.module_video.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.blankj.utilcode.util.SPUtils;
import com.rayho.tsxiu.R;
import com.rayho.tsxiu.base.BaseDataBindingApater;
import com.rayho.tsxiu.base.Constant;
import com.rayho.tsxiu.greendao.VideoAutoPlayDao;
import com.rayho.tsxiu.module_video.dao.VideoAutoPlay;
import com.rayho.tsxiu.module_video.viewmodel.VideosViewModel;
import com.rayho.tsxiu.utils.DaoManager;
import com.rayho.tsxiu.utils.GlideUtils;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder;
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by Rayho on 2019/4/13
 * 视频adapter
 **/
public class VideoAdapter extends BaseDataBindingApater {

    private Context mContext;

    public StandardGSYVideoPlayer mGsyVideoPlayer;


    public VideoAdapter(Context mContext) {
        this.mContext = mContext;
    }


    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        initVideoPlayer(holder, position);

        //移除视频
        holder.itemView.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeItem(position);
            }
        });
    }


    //视频播放设置
    private void initVideoPlayer(ItemViewHolder holder, int position) {
        ImageView imageView = new ImageView(mContext);
        GSYVideoOptionBuilder gsyVideoOptionBuilder = new GSYVideoOptionBuilder();
        StandardGSYVideoPlayer gsyVideoPlayer = holder.itemView.findViewById(R.id.video_item_player);

        VideosViewModel model = (VideosViewModel) items.get(position);
        String cover = model.getCover();//视频封面
        String url = model.getUrl();//视频地址
        String title = model.getTitle();//标题
        GlideUtils.loadImage(mContext, cover, imageView);//加载视频封面

        gsyVideoOptionBuilder
                .setIsTouchWiget(false)
                .setThumbImageView(imageView)//视频封面
                .setUrl(url)//加载视频链接
                .setVideoTitle(title)//视频标题
                .setCacheWithPlay(false)//边播边缓存
                .setRotateViewAuto(true)
                .setLockLand(true)
                .setPlayTag("video")
                .setShowFullAnimation(true)
                .setNeedLockFull(true)
                .setPlayPosition(position)
                .setVideoAllCallBack(new GSYSampleCallBack() {
                    @Override
                    public void onPrepared(String url, Object... objects) {
                        super.onPrepared(url, objects);
                        if (!gsyVideoPlayer.isIfCurrentIsFullscreen()) {
                            //静音
                            GSYVideoManager.instance().setNeedMute(true);
                        }

                    }

                    @Override
                    public void onQuitFullscreen(String url, Object... objects) {
                        super.onQuitFullscreen(url, objects);
                        //全屏不静音
                        GSYVideoManager.instance().setNeedMute(false);
                    }

                    @Override
                    public void onEnterFullscreen(String url, Object... objects) {
                        super.onEnterFullscreen(url, objects);
                        GSYVideoManager.instance().setNeedMute(false);
                        gsyVideoPlayer.getCurrentPlayer().getTitleTextView().setText((String) objects[0]);
                    }
                }).build(gsyVideoPlayer);

        //增加title
        gsyVideoPlayer.getTitleTextView().setVisibility(View.GONE);

        //设置返回键
        gsyVideoPlayer.getBackButton().setVisibility(View.GONE);

        //设置全屏按键功能
        gsyVideoPlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resolveFullBtn(gsyVideoPlayer);
            }
        });


        DaoManager.getInstance().getDaoSession().getVideoAutoPlayDao()
                .queryBuilder()
                .where(VideoAutoPlayDao.Properties.Vid.eq(Constant.AUTO_PLAY_ID))
                .rx()
                .unique()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<VideoAutoPlay>() {
                    @Override
                    public void call(VideoAutoPlay videoAutoPlay) {
                        boolean autoplay;//自动播放标记
                        if(videoAutoPlay != null){
                            autoplay = videoAutoPlay.getAutoplay();
                            if (position == 0 && autoplay) {
                                //默认播放第一个视频
                                gsyVideoPlayer.startPlayLogic();
                            }
                        }
                    }
                });

        //将position=0的item(player)传给VideoTabFragment
        if(position == 0){
            mGsyVideoPlayer = gsyVideoPlayer;
        }

    }


    /**
     * 全屏幕按键处理
     */
    private void resolveFullBtn(final StandardGSYVideoPlayer standardGSYVideoPlayer) {
        standardGSYVideoPlayer.startWindowFullscreen(mContext, true, true);
    }
}
