package com.sameal.dd.ui.ddActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import com.alipay.sdk.app.H5PayCallback;
import com.alipay.sdk.app.PayTask;
import com.alipay.sdk.util.H5PayResultModel;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.netease.cloud.nos.android.utils.LogUtil;
import com.sameal.dd.BuildConfig;
import com.sameal.dd.R;
import com.sameal.dd.common.MyActivity;
import com.sameal.dd.helper.LogUtils;
import com.sameal.dd.helper.SpUtil;
import com.sameal.dd.http.model.HttpData;
import com.sameal.dd.http.response.ShoppUrlBean;
import com.sameal.dd.http.server.ReleaseServer;
import com.sameal.dd.other.AppConfig;
import com.sameal.dd.ui.activity.ImageSelectActivity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * H5商城
 *
 */

public class ShoppActivity extends MyActivity {

    WebView webView;
    View view;
    private String referer;

    private ReleaseServer releaseServer;
    // 设置微信 H5 支付调用 loadDataWithBaseURL 的标记位，避免循环调用，
// 再次进入微信 H5 支付流程时记得重置此标记位状态
    private boolean firstVisitWXH5PayUrl = true;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_shopp;
    }

    @Override
    protected void initView() {

        webView = (WebView) findViewById(R.id.webview);
        view = (View) findViewById(R.id.view);
    }

    @Override
    protected void initData() {
        setWebView();
        setViewWidth();
        getUrl();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearWebViewCache();
        webView.destroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == event.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void setWebView() {
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                if (BuildConfig.DEBUG) {
                    referer = "http://shop.xilekeji.cn/h5/";
                } else {
                    referer = "http://mall.zdqyl.com";
                }

                try {
                    if (url.startsWith("weixin://wap/pay?") ) {
                        LogUtils.d(TAG,url.toString());
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        startActivity(intent);
                        LogUtil.d(TAG,"微信H5支付");
                        return true;
                    }
                } catch (Exception e) {
                    view.goBack(); // 因为会出现有一个weixin空白页面；根据需求自己处理
                    // 防止手机没有安装处理某个 scheme 开头的 url 的 APP 导致 crash
                    return true;
                }

                if(url.startsWith("alipays:") || url.startsWith("alipay")) {
                    try {
                        startActivity(new Intent("android.intent.action.VIEW", Uri.parse(url)));
                    } catch (Exception e) {
                        view.goBack(); // 因为会出现有一个weixin空白页面；根据需求自己处理
                        // 防止手机没有安装处理某个 scheme 开头的 url 的 APP 导致 crash
                        return true;
                    }
                    return true;
                }

                if (("4.4.3".equals(Build.VERSION.RELEASE)) || ("4.4.4".equals(Build.VERSION.RELEASE))) {
                    if (firstVisitWXH5PayUrl) {
                        view.loadDataWithBaseURL(referer, "<script>window.location.href=\"" + url + "\";</script>",
                                "text/html", "utf-8", null);
                        firstVisitWXH5PayUrl = false;
                    }
                    return false;
                } else {
                    HashMap<String,String> map = new HashMap<String,String>();
                    map.put("Referer",referer);
                    view.loadUrl(url,map);
                    return true;
                }
            }
        });

        //声明WebSettings子类
        WebSettings webSettings = webView.getSettings();
        //如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        webSettings.setJavaScriptEnabled(true);//与JS交互的权限
        //支持插件
//        webSettings.setPluginsEnabled(true);
        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        webSettings.setDomStorageEnabled(true);// //开启 DOM 存储功能
        //缩放操作
        webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件
        webSettings.setDatabaseEnabled(true);//开启数据库存储功能
        webSettings.setAppCacheEnabled(true);//开启应用缓存功能
        //其他细节操作
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
        //文件权限
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        webSettings.setAllowContentAccess(true);
        String cacheDirPath = getApplication().getCacheDir().getAbsolutePath()+ "/webcache";//缓存路径
        webSettings.setAppCachePath(cacheDirPath);//设置数据库缓存路径
        releaseServer = new ReleaseServer();
    }

    private void setViewWidth(){
        int heig = getStatusBarHeight(this);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
        params.height = heig;
        view.setLayoutParams(params);
    }

    /**
     * 清除WebView缓存
     */
    public void clearWebViewCache(){
        //清理Webview缓存数据库
        try {
            deleteDatabase("webview.db");
            deleteDatabase("webviewCache.db");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取商城地址并设置给WebView
     */
    private void getUrl(){
        EasyHttp.get(this)
                .api("api/sists/getMallUrl?uid="+AppConfig.getLoginBean().getId())
                .request(new HttpCallback<HttpData<ShoppUrlBean>>(this){
                    @Override
                    public void onSucceed(HttpData<ShoppUrlBean> result) {
                        super.onSucceed(result);
                        if (result.getCode() == 1){
                            if (getInt(SpUtil.TYPE) == 0) {
                                webView.loadUrl(result.getData().getUrl());
                                webView.addJavascriptInterface(ShoppActivity.this,"APP");
                            } else if (getInt(SpUtil.TYPE) == 1) {
                                webView.loadUrl(result.getData().getUrl() + "&typePay=voucher");
                                LogUtil.d(TAG,"111:" + result.getData().getUrl() + "&typePay=voucher");
                                webView.addJavascriptInterface(ShoppActivity.this,"APP");
                            }
                        }
                    }
                });
    }

    /**
     * JS交互
     */

    //将支付宝支付的结果传递给H5
    @SuppressLint("SetJavaScriptEnabled")
    private void statePay(H5PayResultModel result) {
        webView.post(new Runnable() {
            @Override
            public void run() {
                webView.evaluateJavascript("javascript:statePay(" + result.getResultCode() + ")", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String s) {
                        LogUtil.d(TAG,"JS:" + s);
                    }
                });
            }
        });
    }

    //商城返回按钮
    @JavascriptInterface
    public void goBack(){
        LogUtils.d(TAG, "goBack:JS调用了Android方法 ");
        Intent intent = new Intent();
        setResult(1,intent);
        finish();
    }

    //告知H5是否是APP打开商城
    @JavascriptInterface
    public String isApp(){
        LogUtils.d(TAG, "IsApp:JS调用了Android方法 ");
        return "1";
    }

    //拨打电话
    @JavascriptInterface
    public void jsCallPhone(String phone) {
        callPhone(phone);
    }

    //评论图片
    @JavascriptInterface
    public List<String> jsImage(int maxSelect) {
        List<String> stringList = new ArrayList<>();
        ImageSelectActivity.start(this, maxSelect, data -> {
            for (String s : data) {
                stringList.add(s);
            }
        });
        return stringList;
    }
}