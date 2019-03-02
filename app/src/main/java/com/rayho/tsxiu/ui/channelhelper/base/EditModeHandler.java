package com.rayho.tsxiu.ui.channelhelper.base;

import android.view.MotionEvent;


import com.rayho.tsxiu.ui.channelhelper.adapter.ChannelAdapter;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by 钟光新 on 2016/10/15 0015.
 */

public abstract class EditModeHandler {
    public abstract void startEditMode(RecyclerView mRecyclerView);
    public abstract void cancelEditMode(RecyclerView mRecyclerView);
    public abstract void clickMyChannel(RecyclerView mRecyclerView, ChannelAdapter.ChannelViewHolder holder);
    public abstract void clickLongMyChannel(RecyclerView mRecyclerView, ChannelAdapter.ChannelViewHolder holder);
    public abstract void touchMyChannel(MotionEvent motionEvent, ChannelAdapter.ChannelViewHolder holder);
    public abstract void clickRecChannel(RecyclerView mRecyclerView, ChannelAdapter.ChannelViewHolder holder);
}
