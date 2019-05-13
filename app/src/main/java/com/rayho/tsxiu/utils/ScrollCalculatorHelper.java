package com.rayho.tsxiu.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.os.Handler;
import android.widget.Toast;

import com.blankj.utilcode.util.SPUtils;
import com.orhanobut.logger.Logger;
import com.rayho.tsxiu.base.Constant;
import com.rayho.tsxiu.greendao.VideoAutoPlayDao;
import com.rayho.tsxiu.module_video.adapter.VideoAdapter;
import com.rayho.tsxiu.module_video.dao.VideoAutoPlay;
import com.shuyu.gsyvideoplayer.utils.NetworkUtils;
import com.shuyu.gsyvideoplayer.video.base.GSYBaseVideoPlayer;

import androidx.recyclerview.widget.RecyclerView;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * 计算视频列表滑动，自动播放的帮助类
 * 作者真的很牛逼
 * Created by guoshuyu on 2017/11/2.
 */

public class ScrollCalculatorHelper {

    private int firstVisible = 0;//第一个可见item的位置
    private int lastVisible = 0;//最后一个可见item的位置
    private int visibleCount = 0;//当前可见item的数量
    private int playId;
    private int rangeTop;//可播放区域的顶部高度
    private int rangeBottom;//可播放区域的底部高度
    private PlayRunnable runnable;

    private Handler playHandler = new Handler();


    private boolean autoPlay;//视频自动播放标记

    private boolean secondPlay;//第二个视频是否已经自动播放的标记

    private VideoAdapter adapter;//视频adapter

    public GSYBaseVideoPlayer mGsyBaseVideoPlayer;//当前符合播放条件的player


    public ScrollCalculatorHelper(int playId, int rangeTop, int rangeBottom, VideoAdapter adapter) {
        this.playId = playId;
        this.rangeTop = rangeTop;
        this.rangeBottom = rangeBottom;
        this.adapter = adapter;

    }

    public void onScrollStateChanged(RecyclerView view, int scrollState) {
        switch (scrollState) {
            case RecyclerView.SCROLL_STATE_IDLE:
                playVideo(view);
                break;
        }
    }

    public void onScroll(RecyclerView view, int firstVisibleItem, int lastVisibleItem, int visibleItemCount) {
        if (firstVisible == firstVisibleItem) {
            return;
        }
        firstVisible = firstVisibleItem;
        lastVisible = lastVisibleItem;
        visibleCount = visibleItemCount;
    }


    /**
     * 设置标记
     * @param autoPlay 自动播放标记
     */
    public void setAutoPlay(boolean autoPlay) {
        this.autoPlay = autoPlay;
    }


    private void playVideo(RecyclerView view) {

        if (view == null) {
            return;
        }

        RecyclerView.LayoutManager layoutManager = view.getLayoutManager();

        GSYBaseVideoPlayer gsyBaseVideoPlayer = null;

        boolean needPlay = false;

        Logger.d("可见item的数目：" + visibleCount);

        //当第一次 第二个视频滑动到播放区域(一般是中间位置)时自动播放
        if (secondPlay == false && visibleCount == 0) {
            visibleCount = 2;
            secondPlay = true;
        }

        /**
         * 当找到符合播放条件的item时 跳出循环 保证列表只播放一个视频
         */
        for (int i = 0; i < visibleCount; i++) {
            if (layoutManager.getChildAt(i) != null && layoutManager.getChildAt(i).findViewById(playId) != null) {
                GSYBaseVideoPlayer player = (GSYBaseVideoPlayer) layoutManager.getChildAt(i).findViewById(playId);
                Rect rect = new Rect();
                player.getLocalVisibleRect(rect);
                int height = player.getHeight();
                //说明该视频完全显示
                if (rect.top == 0 && rect.bottom == height) {
                    gsyBaseVideoPlayer = player;
                    if ((gsyBaseVideoPlayer.getCurrentPlayer().getCurrentState() == GSYBaseVideoPlayer.CURRENT_STATE_NORMAL
                            || gsyBaseVideoPlayer.getCurrentPlayer().getCurrentState() == GSYBaseVideoPlayer.CURRENT_STATE_ERROR)) {

                        needPlay = true;
                    }
                    break;
                }

            }
        }

        if (gsyBaseVideoPlayer != null && needPlay) {
            if (runnable != null) {
                GSYBaseVideoPlayer tmpPlayer = runnable.gsyBaseVideoPlayer;
                //handler清空消息队列
                playHandler.removeCallbacks(runnable);
                runnable = null;
                if (tmpPlayer == gsyBaseVideoPlayer) {
                    return;
                }
            }
            runnable = new PlayRunnable(gsyBaseVideoPlayer);
            //降低频率
            playHandler.postDelayed(runnable, 400);

        }


    }

    /**
     * 视频播放条件:
     * 1.视频item完全显示
     * 2.视频的中心点位于播放区域
     * <p>
     * 注意:
     * 1.当前视频播放时 会停止上一个视频的播放
     * 2.当滑动到第一个视频完全显示时 会默认播放第一个视频
     */
    private class PlayRunnable implements Runnable {

        GSYBaseVideoPlayer gsyBaseVideoPlayer;

        public PlayRunnable(GSYBaseVideoPlayer gsyBaseVideoPlayer) {
            this.gsyBaseVideoPlayer = gsyBaseVideoPlayer;
        }

        @Override
        public void run() {
            //可播放的标记
            boolean inPosition = false;
            //如果未播放，需要播放
            if (gsyBaseVideoPlayer != null) {
                int[] screenPosition = new int[2];
                //获取视频在屏幕中的x,y坐标
                //screenPosition[1]代表view的y  screenPosition[0]代表view的x
                gsyBaseVideoPlayer.getLocationOnScreen(screenPosition);
                int halfHeight = gsyBaseVideoPlayer.getHeight() / 2;
                //中心点
                int rangePosition = screenPosition[1] + halfHeight;
                //中心点在播放区域内
                if (rangePosition >= rangeBottom && rangePosition <= rangeTop) {
                    inPosition = true;
                }

                if (inPosition){
                    //将符合播放条件的item(player)传给VideoTabFragment
                    mGsyBaseVideoPlayer = gsyBaseVideoPlayer;
                }

                if (inPosition && autoPlay) {
                    //自动播放标记=ture 开启播放
                    startPlayLogic(gsyBaseVideoPlayer, gsyBaseVideoPlayer.getContext());
                    //gsyBaseVideoPlayer.startPlayLogic();
                }
            }
        }
    }


    /***************************************自动播放的点击播放确认******************************************/
    private void startPlayLogic(GSYBaseVideoPlayer gsyBaseVideoPlayer, Context context) {
        if (!com.shuyu.gsyvideoplayer.utils.CommonUtil.isWifiConnected(context)) {
            //这里判断是否wifi
            showWifiDialog(gsyBaseVideoPlayer, context);
            return;
        }
        gsyBaseVideoPlayer.startPlayLogic();
    }


    private void showWifiDialog(final GSYBaseVideoPlayer gsyBaseVideoPlayer, Context context) {
        if (!NetworkUtils.isAvailable(context)) {
            Toast.makeText(context, context.getResources().getString(com.shuyu.gsyvideoplayer.R.string.no_net), Toast.LENGTH_LONG).show();
            return;
        }
        //暂时忽略移动网络情况下的视频播放
       /* AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(context.getResources().getString(com.shuyu.gsyvideoplayer.R.string.tips_not_wifi));
        builder.setPositiveButton(context.getResources().getString(com.shuyu.gsyvideoplayer.R.string.tips_not_wifi_confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                gsyBaseVideoPlayer.startPlayLogic();
            }
        });
        builder.setNegativeButton(context.getResources().getString(com.shuyu.gsyvideoplayer.R.string.tips_not_wifi_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();*/
    }

}
