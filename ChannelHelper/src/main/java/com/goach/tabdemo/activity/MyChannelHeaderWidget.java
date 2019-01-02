package com.goach.tabdemo.activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.goach.tabdemo.R;
import com.goach.tabdemo.adapter.ChannelAdapter;
import com.goach.tabdemo.base.EditModeHandler;
import com.goach.tabdemo.base.IChannelType;
import com.goach.tabdemo.bean.ChannelBean;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by goach on 2016/9/28.
 */

public class MyChannelHeaderWidget implements IChannelType {
    private RecyclerView mRecyclerView;
    private EditModeHandler editModeHandler;
    public MyChannelHeaderWidget(EditModeHandler handler){
        this.editModeHandler = handler;
    }
    @Override
    public ChannelAdapter.ChannelViewHolder createViewHolder(LayoutInflater mInflater, ViewGroup parent) {
        mRecyclerView = (RecyclerView) parent;
        return new MyChannelHeaderViewHolder(mInflater.inflate(R.layout.activity_channel_my_header,parent,false));
    }

    @Override
    public void bindViewHolder(final ChannelAdapter.ChannelViewHolder holder, int position, ChannelBean data) {
       final MyChannelHeaderViewHolder viewHolder = (MyChannelHeaderViewHolder) holder;
        viewHolder.mEditModeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!viewHolder.mEditModeTv.isSelected()){
                    if(editModeHandler!=null)
                        editModeHandler.startEditMode(mRecyclerView);
                    viewHolder.mEditModeTv.setText("完成");
                }else{
                    if(editModeHandler!=null)
                        editModeHandler.cancelEditMode(mRecyclerView);
                    viewHolder.mEditModeTv.setText("编辑");
                }
                viewHolder.mEditModeTv.setSelected(!viewHolder.mEditModeTv.isSelected());
            }
        });
    }
    public class MyChannelHeaderViewHolder extends ChannelAdapter.ChannelViewHolder{
        private TextView mEditModeTv;
        public MyChannelHeaderViewHolder(View itemView) {
            super(itemView);
            mEditModeTv = itemView.findViewById(R.id.id_edit_mode);
        }
    }
}
