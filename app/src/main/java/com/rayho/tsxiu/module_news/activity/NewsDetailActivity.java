package com.rayho.tsxiu.module_news.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.blankj.rxbus.RxBus;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.SPUtils;
import com.just.agentweb.AgentWeb;
import com.just.agentweb.DefaultWebClient;
import com.rayho.tsxiu.R;
import com.rayho.tsxiu.base.Constant;
import com.rayho.tsxiu.databinding.ActivityNewsDetailBinding;
import com.rayho.tsxiu.module_news.bean.NewsBean;
import com.rayho.tsxiu.module_news.viewmodel.NewsDetailViewModel;
import com.rayho.tsxiu.utils.StatusBarUtil;
import com.rayho.tsxiu.utils.ToastUtil;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import skin.support.widget.SkinCompatSupportable;

public class NewsDetailActivity extends AppCompatActivity implements SkinCompatSupportable {

    private ActivityNewsDetailBinding mBinding;

    private NewsBean.DataBean mNews;

    private AgentWeb mAgentWeb;

    private WebViewClient mWebViewClient;

    private WebChromeClient mWebChromeClient;

    private String mUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_news_detail);
        mBinding.setVm(new NewsDetailViewModel());

        initStatusBar();
        initToolbar();

        RxBus.getDefault().subscribeSticky(this, "news", new RxBus.Callback<NewsBean.DataBean>() {
            @Override
            public void onEvent(NewsBean.DataBean dataBean) {
                mNews = dataBean;
            }
        });

        showNewsContent();
    }


    private void initStatusBar(){
        //根据夜间模式的设置 设置状态栏的颜色
        if (SPUtils.getInstance(Constant.SP_SETTINGS).getBoolean(getString(R.string.sp_nightmode))) {
            //夜间模式true 黑色
            StatusBarUtil.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark_night), this);
        } else {
            //夜间模式false 白色
            StatusBarUtil.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark), this);
        }
    }


    private void initToolbar() {
        mBinding.toolbar.setNavigationIcon(R.mipmap.arrow_back_2);
        mBinding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mBinding.toolbar.inflateMenu(R.menu.news_detail);
        mBinding.toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.search:
                        ActivityUtils.startActivity(SearchActivity.class);
                        finish();
                        return true;
                    case R.id.share:
                        shareLink(mUrl);
                        return true;
                    default:
                        return true;
                }
            }
        });
    }


    /**
     * 链接分享
     *
     * @param url
     */
    private void shareLink(String url) {
        if (TextUtils.isEmpty(url)) {
            ToastUtil toast = new ToastUtil(this, "链接不存在");
            toast.show();
            return;
        }
        Intent textIntent = new Intent(Intent.ACTION_SEND);
        textIntent.setType("text/plain");
        textIntent.putExtra(Intent.EXTRA_TEXT, url);
        startActivity(Intent.createChooser(textIntent, "分享链接"));
    }


    /**
     * 通过Agentweb显示新闻内容（加载新闻的网址）
     */
    private void showNewsContent() {
        if (mNews != null) {
            mUrl = mNews.url;
            if (TextUtils.isEmpty(mUrl)) {
                return;
            } else {
                initAgentWeb();
            }
        }
    }


    /**
     * 初始化Agentweb
     * 未适配文件下载(今日头条apk下载后 自动安装)
     * 未适配视频全屏播放
     */
    private void initAgentWeb() {
        initWebClient();
        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(mBinding.container, -1, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
                .useDefaultIndicator(getResources().getColor(R.color.transparent), 1)//设置进度条颜色与高度，-1为默认值，高度为2，单位为dp。
                .setWebChromeClient(mWebChromeClient)
                .setWebViewClient(mWebViewClient)
                .setMainFrameErrorView(R.layout.agentweb_error_page, -1)//参数1是错误显示的布局，参数2点击刷新控件ID -1表示点击整个布局都刷新， AgentWeb 3.0.0 加入。
                .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK)//严格模式 Android 4.2.2 以下会放弃注入对象 ，使用AgentWebView没影响。
                .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.DISALLOW)//打开其他应用时，弹窗咨询用户是否前往其他应用(这里选择拒绝)
                .interceptUnkownUrl()  //拦截找不到相关页面的Url AgentWeb 3.0.0 加入。
                .createAgentWeb()//创建AgentWeb。
                .ready()//设置 WebSettings。
                .go(mUrl);//WebView载入该url地址的页面并显示。
    }


    private void initWebClient() {
        mWebViewClient = new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                //do you  work
                Log.i("Info", "BaseWebActivity onPageStarted");
            }
        };

        mWebChromeClient = new WebChromeClient() {
            /**
             * 动态获取网页的标题
             * @param view
             * @param title
             */
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }
        };
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.getDefault().unregister(this);

    }

    /**
     * 动态监听换肤行为 设置状态栏颜色
     */
    @Override
    public void applySkin() {
        if (SPUtils.getInstance(Constant.SP_SETTINGS).getBoolean(getString(R.string.sp_nightmode))) {
            //夜间模式true 黑色
            StatusBarUtil.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark_night), this);
        } else {
            //夜间模式false 白色
            StatusBarUtil.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark), this);
        }
    }
}
