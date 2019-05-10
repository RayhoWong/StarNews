package com.rayho.tsxiu.activity;

import com.rayho.tsxiu.R;
import com.rayho.tsxiu.base.BaseActivity;

public class TestActivity extends BaseActivity {


    @Override
    public int getLayoutId() {
        return R.layout.activity_second;
    }

    @Override
    public void afterSetContentView() {
        //tag为类名
        setToolbarTitle("设置");
//        setToolbarSubTitle("哈哈哈");
        setRightImage(R.mipmap.favor);
    }

}
