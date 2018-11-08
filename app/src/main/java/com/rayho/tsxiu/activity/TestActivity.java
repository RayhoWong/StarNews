package com.rayho.tsxiu.activity;

import android.view.View;

import com.rayho.tsxiu.R;
import com.rayho.tsxiu.base.BaseActivity;
import com.rayho.tsxiu.utils.StatusBarUtil;
import com.rayho.tsxiu.utils.ToastUtil;

public class TestActivity extends BaseActivity {

    @Override
    public int getLayoutId() {
        return R.layout.activity_second;
    }

    @Override
    public void afterSetContentView() {
        StatusBarUtil.setStatusBarTranslucent(this);



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
//                Toast.makeText(TestActivity.this,"我被收藏了",Toast.LENGTH_SHORT).show();
                ToastUtil toast = new ToastUtil(TestActivity.this,R.layout.toast_layout,"hahah");
//                toast.showAtCenter();
//                toast.showAtTop();
                toast.show(3000);
            }
        });
    }



}
