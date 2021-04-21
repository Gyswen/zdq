package com.sameal.dd.ui.ddActivity;

import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.sameal.dd.R;
import com.sameal.dd.common.MyActivity;
import com.sameal.dd.helper.LogUtils;
import com.sameal.dd.helper.MyPicJavaScript;
import com.sameal.dd.http.server.ReleaseServer;
import com.sameal.dd.other.AppConfig;
import com.sameal.dd.ui.dialog.ShareDialog;
import com.sameal.dd.umeng.Platform;
import com.sameal.dd.umeng.UmengShare;

/**
 * @author zhangj
 * @date 2021/1/7 13:06
 * @desc 描述邀请好友
 */
public class ShareActivity extends MyActivity {

    WebView webView;

    ReleaseServer releaseServer;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_share;
    }

    @Override
    protected void initView() {
        webView = findViewById(R.id.webView);
        setWebView();
    }

    @Override
    protected void initData() {
        webView.loadUrl(releaseServer.getHost() + "index/index/share?uid=" + AppConfig.getLoginBean().getId());
        webView.addJavascriptInterface(ShareActivity.this, "Android");
        webView.addJavascriptInterface(ShareActivity.this, "Android");
    }

    private void setWebView() {
        //系统默认会通过手机浏览器打开网页，为了能够直接通过WebView显示网页，则必须设置
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //使用WebView加载显示url
                view.loadUrl(url);
                //返回true
                return true;
            }
        });
        //声明WebSettings子类
        WebSettings webSettings = webView.getSettings();
        //如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        webSettings.setJavaScriptEnabled(true);
        //支持插件
//        webSettings.setPluginsEnabled(true);
        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        //缩放操作
        webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件
        //其他细节操作
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE); //关闭webview中缓存
        webSettings.setAllowFileAccess(true); //设置可以访问文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
        //优先使用缓存
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        //缓存模式如下：
        //LOAD_CACHE_ONLY: 不使用网络，只读取本地缓存数据
        //LOAD_DEFAULT: （默认）根据cache-control决定是否从网络上取数据。
        //LOAD_NO_CACHE: 不使用缓存，只从网络获取数据.
        //LOAD_CACHE_ELSE_NETWORK，只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据
        //不使用缓存
        //WebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        releaseServer = new ReleaseServer();
    }

    @Override
    public void onRightClick(View v) {
        // 分享对话框
        new ShareDialog.Builder(this)
                // 分享标题
                .setShareTitle(getString(R.string.app_name))
                // 分享描述
                .setShareDescription("鼎胜赛事APP邀请您！")
                // 分享缩略图
                .setShareLogo(R.mipmap.ic_launcher)
                // 分享链接
                .setShareUrl(new ReleaseServer().getHost() + "index/index/register?fid=" + AppConfig.getLoginBean().getId())
                .setListener(new UmengShare.OnShareListener() {

                    @Override
                    public void onSucceed(Platform platform) {
                        toast("分享成功");
                    }

                    @Override
                    public void onError(Platform platform, Throwable t) {
                        toast("分享出错");
                    }

                    @Override
                    public void onCancel(Platform platform) {
                        toast("分享取消");
                    }
                })
                .show();
    }

    @JavascriptInterface
    public void dianji(String s) {
        LogUtils.d(TAG, "dianji: " + s);

        MyPicJavaScript javaScript = new MyPicJavaScript(this);
        javaScript.savepic(s);
    }

    @JavascriptInterface
    public void fuzhi(String s) {
        LogUtils.d(TAG, "fuzhi: " + s);

        // 分享对话框
        new ShareDialog.Builder(this)
                // 分享标题
                .setShareTitle(getString(R.string.app_name))
                // 分享描述
                .setShareDescription("鼎胜赛事APP邀请您！")
                // 分享缩略图
                .setShareLogo(R.mipmap.ic_launcher)
                // 分享链接
                .setShareUrl(s)
                .setListener(new UmengShare.OnShareListener() {

                    @Override
                    public void onSucceed(Platform platform) {
                        toast("分享成功");
                    }

                    @Override
                    public void onError(Platform platform, Throwable t) {
                        toast("分享出错");
                    }

                    @Override
                    public void onCancel(Platform platform) {
                        toast("分享取消");
                    }
                })
                .show();
    }
}