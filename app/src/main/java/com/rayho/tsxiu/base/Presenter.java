package com.rayho.tsxiu.base;

import android.view.View;

/**
 * Created by Rayho on 2019/2/23
 * 该接口用于处理各种点击事件(根据需求自定义)
 **/
public interface Presenter extends View.OnClickListener {
    @Override
    void onClick(View v);
}
