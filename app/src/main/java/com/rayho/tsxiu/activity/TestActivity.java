package com.rayho.tsxiu.activity;

import android.graphics.Color;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.rayho.tsxiu.R;
import com.rayho.tsxiu.base.BaseActivity;
import com.rayho.tsxiu.utils.StatusBarUtil;
import com.rayho.tsxiu.utils.ToastUtil;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

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
//                ToastUtil toast = new ToastUtil(TestActivity.this,R.layout.toast,"hahah");
//                toast.showAtCenter();
//                toast.showAtTop();
//                toast.show(3000);
                openDialog();
            }
        });
    }

    private void openDialog(){
//        MaterialDialog(this)
//                .title(R.string.your_title)
//                .message(R.string.your_message)
//                .show();
        new MaterialDialog.Builder(TestActivity.this)
                .title("basic dialog")
                .content("一个简单的dialog,高度会随着内容改变，同时还可以嵌套RecyleView")
                .iconRes(R.mipmap.ic_launcher)
                .positiveText("同意")
                .negativeText("不同意")
                .neutralText("更多信息")
                .widgetColor(Color.BLUE)//不再提醒的checkbox 颜色
                //CheckBox
                .checkBoxPrompt("不再提醒", false, new CompoundButton.OnCheckedChangeListener(){
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (b) {
                            Toast.makeText(TestActivity.this, "不再提醒", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(TestActivity.this, "会再次提醒", Toast.LENGTH_LONG).show();
                        }
                    }
                })
                //嵌套recycleview，这个的点击事件可以先获取此Recycleview对象然后自己处理
//                .adapter(new RecycleviewAdapter(getData(), TestActivity.this), new LinearLayoutManager(TestActivity.this))
//
//
//                .itemsCallback(new MaterialDialog.ListCallback() {
//                    @Override
//                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
//                        dataChoose = "下标：" + position + " and 数据：" + mData.get(position);
//                    }
//                })
//
//                //点击事件添加 方式1
//                .onAny(new MaterialDialog.SingleButtonCallback() {
//                    @Override
//                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                        if (which == DialogAction.NEUTRAL) {
//                            Toast.makeText(TestActivity.this, "更多信息", Toast.LENGTH_LONG).show();
//                        } else if (which == DialogAction.POSITIVE) {
//                            Toast.makeText(TestActivity.this, "同意" + dataChoose, Toast.LENGTH_LONG).show();
//                        } else if (which == DialogAction.NEGATIVE) {
//                            Toast.makeText(TestActivity.this, "不同意", Toast.LENGTH_LONG).show();
//                        }
//
//                    }
//                })
                .show();
    }



}
