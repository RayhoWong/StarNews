package com.rayho.tsxiu.module_news.activity;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.SPUtils;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.just.agentweb.AbsAgentWebSettings;
import com.just.agentweb.AgentWeb;
import com.just.agentweb.DefaultWebClient;
import com.just.agentweb.IAgentWebSettings;
import com.just.agentweb.WebListenerManager;
import com.just.agentweb.download.AgentWebDownloader;
import com.just.agentweb.download.DefaultDownloadImpl;
import com.just.agentweb.download.DownloadListenerAdapter;
import com.just.agentweb.download.DownloadingService;
import com.orhanobut.logger.Logger;
import com.rayho.tsxiu.R;
import com.rayho.tsxiu.base.Constant;
import com.rayho.tsxiu.base.Presenter;
import com.rayho.tsxiu.databinding.ActivityScannerResultBinding;
import com.rayho.tsxiu.module_news.viewmodel.ScannerResultViewModel;
import com.rayho.tsxiu.ui.sharedialog.BottomShareDialog;
import com.rayho.tsxiu.utils.StatusBarUtil;
import com.rayho.tsxiu.utils.ToastUtil;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.databinding.DataBindingUtil;
import skin.support.widget.SkinCompatSupportable;

public class ScannerResultActivity extends AppCompatActivity implements Presenter, SkinCompatSupportable {

    private ActivityScannerResultBinding mBinding;

    private ScannerResultViewModel model;

    private String mTitle = "";

    private String mUrl = "";

    private AgentWeb mAgentWeb;

    private DownloadListenerAdapter mDownloadListenerAdapter;

    private DownloadingService mDownloadingService;

    private WebViewClient mWebViewClient;

    private WebChromeClient mWebChromeClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_scanner_result);
        model = new ScannerResultViewModel(mTitle);
        mBinding.setVm(model);
        mBinding.setPresenter(this);

        initView();
        initAgentWeb();
    }


    private void initView() {
        //根据夜间模式的设置 设置状态栏的颜色
        if (SPUtils.getInstance(Constant.SP_SETTINGS).getBoolean(getString(R.string.sp_nightmode))) {
            //夜间模式true 黑色
            StatusBarUtil.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark_night), this);
        } else {
            //夜间模式false 白色
            StatusBarUtil.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark), this);
        }
        mBinding.toolbar.setNavigationIcon(R.mipmap.close_2);
        mBinding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void initAgentWeb() {
        initWebClient();
        mUrl = getIntent().getStringExtra("url");
        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(mBinding.container, -1, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
                .useDefaultIndicator(getResources().getColor(R.color.lime), 3)//设置进度条颜色与高度，-1为默认值，高度为2，单位为dp。
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
                mTitle = title;
                model.title.set(mTitle);
            }
        };
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_share:
                shareResult();
                break;
            case R.id.iv_more:
                showPopupMenu();
                break;
        }
    }


    /**
     * 返回IAgentWebSettings
     *
     * @return
     */
    private IAgentWebSettings getSettings() {
        initDownloadListener();

        return new AbsAgentWebSettings() {
            private AgentWeb mAgentWeb;

            @Override
            protected void bindAgentWebSupport(AgentWeb agentWeb) {
                this.mAgentWeb = agentWeb;
            }

            /**
             * AgentWeb 4.0.0 内部删除了 DownloadListener 监听 ，以及相关API ，将 Download 部分完全抽离出来独立一个库，
             * 如果你需要使用 AgentWeb Download 部分 ， 请依赖上 compile 'com.just.agentweb:download:4.0.0 ，
             * 如果你需要监听下载结果，请自定义 AgentWebSetting ， New 出 DefaultDownloadImpl，传入DownloadListenerAdapter
             * 实现进度或者结果监听，例如下面这个例子，如果你不需要监听进度，或者下载结果，下面 setDownloader 的例子可以忽略。
             * @param webView
             * @param downloadListener
             * @return WebListenerManager
             */
            @Override
            public WebListenerManager setDownloader(WebView webView, android.webkit.DownloadListener downloadListener) {
                return super.setDownloader(webView,
                        DefaultDownloadImpl
                                .create((Activity) webView.getContext(),
                                        webView,
                                        mDownloadListenerAdapter,
                                        mDownloadListenerAdapter,
                                        null));
            }
        };
    }


    /**
     * 文件下载监听（暂时停止调用）
     * <p>
     * 不知道什么原因导致下载失败 apk无法自动安装（点击通知也无法安装）
     * 实际上文件已下载 报错原因是：
     * error: java.lang.RuntimeException: Download failed ， cause:Download successful .
     */
    private void initDownloadListener() {
        mDownloadListenerAdapter = new DownloadListenerAdapter() {
            /**
             *
             * @param url                下载链接
             * @param userAgent          UserAgent
             * @param contentDisposition ContentDisposition
             * @param mimetype           资源的媒体类型
             * @param contentLength      文件长度
             * @param extra              下载配置 ， 用户可以通过 Extra 修改下载icon ， 关闭进度条 ， 是否强制下载。
             * @return true 表示用户处理了该下载事件 ， false 交给 AgentWeb 下载
             */
            @Override
            public boolean onStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength, AgentWebDownloader.Extra extra) {
                Logger.i("onStart:" + url);
                extra.setOpenBreakPointDownload(true) // 是否开启断点续传
                        .setIcon(R.drawable.ic_file_download_black_24dp) //下载通知的icon
                        .setConnectTimeOut(6000) // 连接最大时长
                        .setBlockMaxTime(10 * 60 * 1000)  // 以8KB位单位，默认60s ，如果60s内无法从网络流中读满8KB数据，则抛出异常
                        .setDownloadTimeOut(Long.MAX_VALUE) // 下载最大时长
                        .setParallelDownload(false)  // 串行下载更节省资源哦
                        .setEnableIndicator(true)  // false 关闭进度通知
                        .addHeader("Cookie", "xx") // 自定义请求头
                        .setAutoOpen(true) // 下载完成自动打开
                        .setForceDownload(true); // 强制下载，不管网络网络类型
                return false;
            }

            /**
             *
             * 不需要暂停或者停止下载该方法可以不必实现
             * @param url
             * @param downloadingService  用户可以通过 DownloadingService#shutdownNow 终止下载
             */
            @Override
            public void onBindService(String url, DownloadingService downloadingService) {
                super.onBindService(url, downloadingService);
                mDownloadingService = downloadingService;
                Logger.i("onBindService:" + url + "  DownloadingService:" + downloadingService);
            }

            /**
             * 回调onUnbindService方法，让用户释放掉 DownloadingService。
             * @param url
             * @param downloadingService
             */
            @Override
            public void onUnbindService(String url, DownloadingService downloadingService) {
                super.onUnbindService(url, downloadingService);
                mDownloadingService = null;
                Logger.i("onUnbindService:" + url);
            }

            /**
             *
             * @param url  下载链接
             * @param loaded  已经下载的长度
             * @param length    文件的总大小
             * @param usedTime   耗时 ，单位ms
             * 注意该方法回调在子线程 ，线程名 AsyncTask #XX 或者 AgentWeb # XX
             */
            @Override
            public void onProgress(String url, long loaded, long length, long usedTime) {
                int mProgress = (int) ((loaded) / Float.valueOf(length) * 100);
                Logger.i("onProgress:" + mProgress);
                super.onProgress(url, loaded, length, usedTime);
            }

            /**
             *
             * @param path 文件的绝对路径
             * @param url  下载地址
             * @param throwable    如果异常，返回给用户异常
             * @return true 表示用户处理了下载完成后续的事件 ，false 默认交给AgentWeb 处理
             */
            @Override
            public boolean onResult(String path, String url, Throwable throwable) {
                if (null == throwable) { //下载成功
                    //do you work
                    ToastUtil toast = new ToastUtil(ScannerResultActivity.this, "下载成功");
                    toast.show();
                    Logger.d("path: " + path);
                } else {//下载失败
                    ToastUtil toast = new ToastUtil(ScannerResultActivity.this, throwable.toString());
                    toast.show();
                    Logger.d("error: " + throwable.toString());
                    Logger.d("path: " + path);
                }
                return false; // true  不会发出下载完成的通知 , 或者打开文件
            }
        };
    }


    /**
     * 分享链接
     */
    private void shareResult() {
        BottomSheetDialog dialog = new BottomShareDialog(this,mUrl);
        dialog.show();
    }


    private void showPopupMenu() {
        PopupMenu popupMenu = new PopupMenu(this, mBinding.ivMore);
        popupMenu.inflate(R.menu.link);
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.refresh:
                        if (mAgentWeb != null) {
                            mAgentWeb.getUrlLoader().reload();//刷新
                        }
                        return true;
                    case R.id.copy:
                        if (mAgentWeb != null) {//复制链接
                            ClipboardManager mClipboardManager = (ClipboardManager) getApplication().getSystemService(Context.CLIPBOARD_SERVICE);
                            mClipboardManager.setPrimaryClip(ClipData.newPlainText(null, mAgentWeb.getWebCreator().getWebView().getUrl()));
                            ToastUtil toast = new ToastUtil(ScannerResultActivity.this, "链接已复制");
                            toast.show();
                        }
                        return true;
                    case R.id.open:
                        openInBrowser(mUrl);
                        return true;
                    default:
                        return true;
                }
            }
        });
    }


    /**
     * 使用外部浏览器打开网站
     *
     * @param targetUrl 网址
     */
    private void openInBrowser(String targetUrl) {
        if (TextUtils.isEmpty(targetUrl) || targetUrl.startsWith("file://")) {
            ToastUtil toast = new ToastUtil(this, getString(R.string.open_browser_tip));
            toast.show();
            return;
        }
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri mUri = Uri.parse(targetUrl);
        intent.setData(mUri);
        startActivity(intent);
    }


    /////////////////////////AgentWeb跟随Activity Or Fragment生命周期,释放 CPU 更省电/////////////////
    @Override
    protected void onResume() {
        super.onResume();
        mAgentWeb.getWebLifeCycle().onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAgentWeb.getWebLifeCycle().onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAgentWeb.getWebLifeCycle().onDestroy();
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * 返回事件的处理(返回上一页面 而不是退出activity)
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mAgentWeb.handleKeyEvent(keyCode, event)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
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
