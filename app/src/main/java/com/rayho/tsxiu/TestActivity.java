package com.rayho.tsxiu;

import android.view.View;
import android.widget.Toast;

public class TestActivity extends BaseActivity {

    @Override
    public int getLayoutId() {
        return R.layout.activity_second;
    }

    @Override
    public void afterSetContentView() {
        setToolbarTitle("设置");
//        setToolbarSubTitle("哈哈哈");
        setRightImage(R.mipmap.favor);
        /*getToolbarSubTitle().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TestActivity.this,"我被艹了",Toast.LENGTH_SHORT).show();
            }
        });*/
        getRightImage().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TestActivity.this,"我被收藏了",Toast.LENGTH_SHORT).show();
            }
        });
    }



}
