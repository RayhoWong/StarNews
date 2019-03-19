package com.rayho.tsxiu.ui.sharedialog;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.view.View;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.rayho.tsxiu.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.blankj.utilcode.util.ActivityUtils.startActivity;

/**
 * Created by Rayho on 2019/3/18
 * 通过BottomSheetDialog分享链接
 **/
public class BottomShareDialog extends BottomSheetDialog {

    private ArrayList<AppInfo> mAppinfoList;

    private AppInfoAdapter mAdapter;

    private RecyclerView mRecyclerView;

    private Intent shareIntent;

    private Context mContext;

    private String mContent;//分享的内容


    public BottomShareDialog(@NonNull Context context, String content) {
        super(context);
        mContext = context;
        mContent = content;
        createView();
    }


    private void createView() {
        View bottomSheetView = getLayoutInflater().inflate(R.layout.share_dialog, null);
        setContentView(bottomSheetView);
        mRecyclerView = bottomSheetView.findViewById(R.id.rcv);
        initDialog();
    }


    /**
     * 初始化dialog
     */
    private void initDialog() {
        shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, mContent);
        mAppinfoList = AppInfoUtils.getShareAppList(mContext, shareIntent);
        mAdapter = new AppInfoAdapter(mContext, mAppinfoList);
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 4));
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickLitener(new AppInfoAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(AppInfo appInfo, View view, int position) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setComponent(new ComponentName(appInfo.getPkgName(), appInfo.getLaunchClassName()));
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, mContent);
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(AppInfo appInfo, View view, int position) {
                // 长按打开应用信息界面
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.setData(Uri.parse("package:" + appInfo.getPkgName()));
                startActivity(intent);
            }
        });
    }


    /**
     * 通过系统分享(默认底部弹出dialog)
     */
    public void shareBySystem(){
        Intent shareIntent;
        shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, mContent);
        String title = "分享文字";
        Intent chooser = Intent.createChooser(shareIntent, title);
        if (shareIntent.resolveActivity(mContext.getPackageManager()) != null) {
            startActivity(chooser);
        }
    }
}



