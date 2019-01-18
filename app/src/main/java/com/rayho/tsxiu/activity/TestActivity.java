package com.rayho.tsxiu.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.blankj.utilcode.util.Utils;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.rayho.tsxiu.R;
import com.rayho.tsxiu.base.BaseActivity;
import com.rayho.tsxiu.ui.MyRefreshLottieFooter;
import com.rayho.tsxiu.ui.MyRefreshLottieHeader;
import com.rayho.tsxiu.utils.RxTimer;
import com.rayho.tsxiu.utils.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class TestActivity extends BaseActivity {

    @BindView(R.id.rcv)
    RecyclerView mRcv;
    @BindView(R.id.twi_refreshlayout)
    TwinklingRefreshLayout mTwiRefreshlayout;

    private List<String> data;
    private TestAdapter adapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_second;
    }

    @Override
    public void afterSetContentView() {
        StatusBarUtil.setStatusBarTranslucent(this);
        //tag为类名
        Timber.d("hahaha");
        setToolbarTitle("设置");
//        setToolbarSubTitle("哈哈哈");
        setRightImage(R.mipmap.favor);
        /*getToolbarSubTitle().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TestActivity.this,"我被艹了",Toast.LENGTH_SHORT).show();
            }
        });*/
/*        getRightImage().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonDialog dialog = new CommonDialog(TestActivity.this);
                dialog.setTitle("test")
                        .setMessage("hahahha发生的发生的发发送到是发生的发生的发" +
                                "水电费的说法是" +
                                "胜多负少的水电费水电费是sfsfsdssfs发生放水电费水电费水电费hahahahah")
//                      .setImageResId(R.mipmap.ic_launcher)
                        .setOnClickBottomListener(new CommonDialog.OnClickBottomListener() {
                            @Override
                            public void onPositiveClick() {
                                ToastUtil util = new ToastUtil(TestActivity.this, "haha");
                                util.show();
                            }
                        })
                        .show();
            }
        });*/
        initRefreshLayout();
    }

    private void initRefreshLayout() {
        mTwiRefreshlayout.setHeaderHeight(60);
        mTwiRefreshlayout.setBottomHeight(45);
        mTwiRefreshlayout.setHeaderView(new MyRefreshLottieHeader(this));
        mTwiRefreshlayout.setBottomView(new MyRefreshLottieFooter(this));
        mTwiRefreshlayout.setEnableRefresh(true);
        mTwiRefreshlayout.setEnableLoadmore(true);
        mTwiRefreshlayout.setEnableOverScroll(true);
        mTwiRefreshlayout.startRefresh();
        mTwiRefreshlayout.setAutoLoadMore(true);
        mTwiRefreshlayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                super.onRefresh(refreshLayout);
                setTimer(3000, new RxTimer.RxAction() {
                    @Override
                    public void action() {
                        initRcv();
                        refreshLayout.finishRefreshing();
                    }
                });
            }

            @Override
            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);
                setTimer(2000, new RxTimer.RxAction() {
                    @Override
                    public void action() {
                        for (int i=0;i<3;i++){
                            data.add("NEW HELLO");
                        }
                        adapter.notifyDataSetChanged();
                        refreshLayout.finishLoadmore();
                    }
                });
            }

        });
    }

    private void initRcv() {
        initData();
        mRcv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TestAdapter(data);
        mRcv.setAdapter(adapter);
    }

    private void initData() {
        data = new ArrayList<>();
        for (int i = 0; i < 16; i++) {
            data.add("Hello " + i);
        }
    }

    private void setTimer(long milliSeconds, RxTimer.RxAction rxAction) {
        RxTimer rxTimer = new RxTimer();
        rxTimer.timer(milliSeconds, rxAction);

    }


    private void openDialog() {
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
                .checkBoxPrompt("不再提醒", false, new CompoundButton.OnCheckedChangeListener() {
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
