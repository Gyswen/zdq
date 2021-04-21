package com.sameal.dd.ui.ddActivity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;
import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.gyf.immersionbar.BarHide;
import com.gyf.immersionbar.ImmersionBar;
import com.hjq.base.BaseFragmentAdapter;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.sameal.dd.R;
import com.sameal.dd.aop.DebugLog;
import com.sameal.dd.common.MyActivity;
import com.sameal.dd.common.MyFragment;
import com.sameal.dd.http.model.HttpData;
import com.sameal.dd.http.response.SportDetailBean;
import com.sameal.dd.other.IntentKey;
import com.sameal.dd.ui.ddFragment.ChatFragment;
import com.sameal.dd.ui.ddFragment.QuizFragment;
import com.sameal.dd.ui.ddFragment.QuizTypeFragment;
import com.sameal.dd.widget.PlayerView;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangj
 * @date 2021/1/11 21:17
 * desc 直播
 */
public class LiveActivity extends MyActivity {

    PlayerView mPlayerView;
//    WebView webView;
    ImageView imgStart;
    TextView tvStartName;
    TextView tvEndName;
    ImageView imgEnd;
    TabLayout tlHomeTab;
    ViewPager vpHomePager;

    private String url;
    private String rid;
    private BaseFragmentAdapter<MyFragment> mPagerAdapter;

    @DebugLog
    public static void start(Context context, String url, String title, String id, String rid) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        Intent intent = new Intent(context, LiveActivity.class);
        intent.putExtra(IntentKey.VIDEO, url);
        intent.putExtra(IntentKey.TITLE, title);
        intent.putExtra(IntentKey.ID, id);
        intent.putExtra(IntentKey.RID, rid);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_live;
    }

    @Override
    protected void initView() {
        mPlayerView = (PlayerView) findViewById(R.id.player_view);
        imgStart = (ImageView) findViewById(R.id.img_start);
        tvStartName = (TextView) findViewById(R.id.tv_start_name);
        tvEndName = (TextView) findViewById(R.id.tv_end_name);
        imgEnd = (ImageView) findViewById(R.id.img_end);
        tlHomeTab = (TabLayout) findViewById(R.id.tl_home_tab);
        vpHomePager = (ViewPager) findViewById(R.id.vp_home_pager);
    }

    @Override
    protected void initData() {
        mPagerAdapter = new BaseFragmentAdapter<>(this);
        rid = getString(IntentKey.RID);
        getDetail();
//        setWebView();
//        webView.loadUrl(getString(IntentKey.VIDEO));
        mPlayerView.setOnGoBackListener(new PlayerView.onGoBackListener() {
            @Override
            public void onClickGoBack(PlayerView view) {
                onBackPressed();
            }
        });
        mPlayerView.setGestureEnabled(true);
        mPlayerView.setVideoTitle(getString(IntentKey.TITLE));
        mPlayerView.setVideoSource(getString(IntentKey.VIDEO));
        mPlayerView.start();
    }

    @Override
    protected void onResume() {
        mPlayerView.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        mPlayerView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mPlayerView.onDestroy();
        super.onDestroy();
    }

//    private void setWebView() {
//        //系统默认会通过手机浏览器打开网页，为了能够直接通过WebView显示网页，则必须设置
//        webView.setWebViewClient(new WebViewClient() {
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                //使用WebView加载显示url
//                view.loadUrl(url);
//                //返回true
//                return true;
//            }
//        });
//        //声明WebSettings子类
//        WebSettings webSettings = webView.getSettings();
//        //如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
//        webSettings.setJavaScriptEnabled(true);
//        //支持插件
////        webSettings.setPluginsEnabled(true);
//        //设置自适应屏幕，两者合用
//        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
//        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
//        //缩放操作
//        webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
//        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
//        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件
//        //其他细节操作
//        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE); //关闭webview中缓存
//        webSettings.setAllowFileAccess(true); //设置可以访问文件
//        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
//        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
//        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
//    }

    @NonNull
    @Override
    protected ImmersionBar createStatusBarConfig() {
        return super.createStatusBarConfig()
                // 隐藏状态栏和导航栏
                .hideBar(BarHide.FLAG_HIDE_BAR);
    }

    @Override
    public boolean isSwipeEnable() {
        return false;
    }

    private SportDetailBean detailBean;

    /**
     * 赛事详情
     */
    private void getDetail() {
        EasyHttp.get(this)
                .api("api/Sports/getSportDetail?rid=" + rid)
                .request(new HttpCallback<HttpData<SportDetailBean>>(this) {
                    @Override
                    public void onSucceed(HttpData<SportDetailBean> result) {
                        super.onSucceed(result);
                        if (result.getCode() == 1) {
                            detailBean = result.getData();
//                            mPagerAdapter.addFragment(AnalysisFragment.newInstance(), "分析");
                            setData();
                            tvStartName.setText(detailBean.getItem1_name());
                            tvEndName.setText(detailBean.getItem2_name());
                            Glide.with(LiveActivity.this)
                                    .load(detailBean.getItem1_logo())
                                    .error(R.mipmap.ic_launcher)
                                    .placeholder(R.mipmap.ic_launcher)
                                    .into(imgStart);
                            Glide.with(LiveActivity.this)
                                    .load(detailBean.getItem2_logo())
                                    .error(R.mipmap.ic_launcher)
                                    .placeholder(R.mipmap.ic_launcher)
                                    .into(imgEnd);
                        }
                    }
                });
    }

    private List<QuizBean> quizBeans = new ArrayList<>();

    private void setData() {
        for (SportDetailBean.EventsBean eventsBean : detailBean.getEvents()) {
            if (quizBeans.size() == 0) {
                List<SportDetailBean.EventsBean> events = new ArrayList<>();
                events.add(eventsBean);
                quizBeans.add(new QuizBean(eventsBean.getRound(), events));
            } else {
                boolean isHave = false;
                for (QuizBean quizBean : quizBeans) {
                    if (quizBean.getRound() == eventsBean.getRound()) {
                        quizBean.getEventsBeans().add(eventsBean);
                        isHave = true;
                    }
                }
                if (!isHave) {
                    List<SportDetailBean.EventsBean> events = new ArrayList<>();
                    events.add(eventsBean);
                    quizBeans.add(new QuizBean(eventsBean.getRound(), events));
                }
            }
        }
        if (quizBeans.size() == 0) {
            mPagerAdapter.addFragment(ChatFragment.newInstance(getString(IntentKey.ID)), "聊天");
            mPagerAdapter.addFragment(QuizFragment.newInstance(detailBean), "竞猜");
            vpHomePager.setAdapter(mPagerAdapter);
            tlHomeTab.setupWithViewPager(vpHomePager);
        } else {
            mPagerAdapter.addFragment(ChatFragment.newInstance(getString(IntentKey.ID)), "聊天");
            mPagerAdapter.addFragment(QuizTypeFragment.newInstance(detailBean), "竞猜");
            vpHomePager.setAdapter(mPagerAdapter);
            tlHomeTab.setupWithViewPager(vpHomePager);
        }
    }

    class QuizBean {
        private int round;
        private List<SportDetailBean.EventsBean> eventsBeans;

        public QuizBean(int round, List<SportDetailBean.EventsBean> eventsBeans) {
            this.round = round;
            this.eventsBeans = eventsBeans;
        }

        public int getRound() {
            return round;
        }

        public void setRound(int round) {
            this.round = round;
        }

        public List<SportDetailBean.EventsBean> getEventsBeans() {
            if (eventsBeans == null) {
                return new ArrayList<>();
            }
            return eventsBeans;
        }

        public void setEventsBeans(List<SportDetailBean.EventsBean> eventsBeans) {
            this.eventsBeans = eventsBeans;
        }
    }
}