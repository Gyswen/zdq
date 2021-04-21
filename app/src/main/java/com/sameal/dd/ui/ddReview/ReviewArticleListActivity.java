package com.sameal.dd.ui.ddReview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.hjq.bar.TitleBar;
import com.sameal.dd.R;
import com.sameal.dd.common.MyActivity;
import com.sameal.dd.http.response.ArticleListBean;

/**
 *
 * 推荐新闻内容（审核）
 */

public class ReviewArticleListActivity extends MyActivity {

    private com.hjq.bar.TitleBar title;
    private android.webkit.WebView webView;

    private ArticleListBean articleListBean;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_review_article_list;
    }

    @Override
    protected void initView() {
        title = (TitleBar) findViewById(R.id.title);
        webView = (WebView) findViewById(R.id.webView);
    }

    @Override
    protected void initData() {
        articleListBean = getSerializable("Article");
        title.setTitle(articleListBean.getArticle_title());

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);//支持JS
        String js = "<script type=\"text/javascript\">"+
                "var imgs = document.getElementsByTagName('img');" + // 找到img标签
                "for(var i = 0; i<imgs.length; i++){" + // 逐个改变
                "imgs[i].style.width = '100%';" + // 宽度改为100%
                "imgs[i].style.height = 'auto';" +
                "}" +
                "</script>";
        webView.loadData(articleListBean.getArticle_content()+js,"text/html;charset=UTF-8",null);
    }
}