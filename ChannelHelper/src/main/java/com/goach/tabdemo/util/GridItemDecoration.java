package com.goach.tabdemo.util;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by goach on 2016/9/28.
 */

public class GridItemDecoration extends RecyclerView.ItemDecoration{
    private int spacing;

    public GridItemDecoration(int spacing) {
        this.spacing = spacing;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        if (position >= 0) {
            outRect.left = spacing;
            outRect.right = spacing;
            outRect.top = spacing;
        } else {
            outRect.left = 0;
            outRect.right = 0;
            outRect.top = 0;
            outRect.bottom = 0;
        }
    }
}
