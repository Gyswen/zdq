package com.sameal.dd.ui.ddActivity;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import com.bumptech.glide.Glide;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.gyf.immersionbar.ImmersionBar;
import com.hjq.base.BaseFragmentAdapter;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.sameal.dd.R;
import com.sameal.dd.common.MyActivity;
import com.sameal.dd.common.MyFragment;
import com.sameal.dd.http.model.HttpData;
import com.sameal.dd.http.response.SportDetailBean;
import com.sameal.dd.http.response.ZjBean;
import com.sameal.dd.other.IntentKey;
import com.sameal.dd.ui.ddFragment.AnalysisFragment;
import com.sameal.dd.ui.ddFragment.QuizTypeFragment;
import com.sameal.dd.ui.ddFragment.ZjfaFragment;
import com.sameal.dd.widget.XCollapsingToolbarLayout;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangj
 * @date 2021/1/7 22:10
 * desc 竞技详情
 */
public class SportsDetailActivity extends MyActivity {

    TextView tvNo;
    TextView tvTime;
    TextView tvTitle;
    ImageView imgStart;
    TextView tvStartName;
    TextView tvResult;
    ImageView imgEnd;
    TextView tvEndName;
    LinearLayout headLayout;
    Toolbar tbHomeTitle;
    XCollapsingToolbarLayout ctlHomeBar;
    TabLayout tlHomeTab;
    AppBarLayout appbarLayout;
    ViewPager vpHomePager;
    private String rid;
    private BaseFragmentAdapter<MyFragment> mPagerAdapter;

    private ZjBean zjBean;
    private SportDetailBean detailBean;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_sports_detail;
    }

    @Override
    protected void initView() {
        appbarLayout = (AppBarLayout) findViewById(R.id.appbarLayout);
        ctlHomeBar = (XCollapsingToolbarLayout) findViewById(R.id.ctl_home_bar);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        headLayout = (LinearLayout) findViewById(R.id.head_layout);
        tvNo = (TextView) findViewById(R.id.tv_no);
        tvTime = (TextView) findViewById(R.id.tv_time);
        imgStart = (ImageView) findViewById(R.id.img_start);
        tvStartName = (TextView) findViewById(R.id.tv_start_name);
        tvResult = (TextView) findViewById(R.id.tv_result);
        imgEnd = (ImageView) findViewById(R.id.img_end);
        tvEndName = (TextView) findViewById(R.id.tv_end_name);
        tbHomeTitle = (Toolbar) findViewById(R.id.tb_home_title);
        tlHomeTab = (TabLayout) findViewById(R.id.tl_home_tab);
        vpHomePager = (ViewPager) findViewById(R.id.vp_home_pager);

    }

    @Override
    protected void initData() {
        // 给这个 ToolBar 设置顶部内边距，才能和 TitleBar 进行对齐
        ImmersionBar.setTitleBar(this, tbHomeTitle);
        mPagerAdapter = new BaseFragmentAdapter<>(this);
        rid = getString(IntentKey.ID);
        getDetail();
    }

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
                            mPagerAdapter.addFragment(AnalysisFragment.newInstance(), "分析");
                            setData();
                            setToolBarReplaceActionBar();
                            setTitleToCollapsingToolbarLayout();
                            tvTitle.setText(detailBean.getItem1_name() + " VS " + detailBean.getItem2_name());
                            tvNo.setText(detailBean.getRid());
                            tvTime.setText(detailBean.getoStr());
                            tvStartName.setText(detailBean.getItem1_name());
                            tvEndName.setText(detailBean.getItem2_name());
                            tvResult.setText(detailBean.getRaceResult().getScore());
                            Glide.with(SportsDetailActivity.this)
                                    .load(detailBean.getItem1_logo())
                                    .error(R.mipmap.ic_launcher)
                                    .placeholder(R.mipmap.ic_launcher)
                                    .into(imgStart);
                            Glide.with(SportsDetailActivity.this)
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
        if (detailBean.getEvents() == null || detailBean.getEvents().size() == 0) {
            mPagerAdapter.addFragment(QuizTypeFragment.newInstance(detailBean), "竞猜");
            mPagerAdapter.addFragment(ZjfaFragment.newInstance(10, rid), "方案");
            vpHomePager.setAdapter(mPagerAdapter);
            tlHomeTab.setupWithViewPager(vpHomePager);
            tlHomeTab.getTabAt(1).select();
            return;
        }
        if (quizBeans.size() == 1) {
            mPagerAdapter.addFragment(QuizTypeFragment.newInstance(detailBean), "竞猜");
            mPagerAdapter.addFragment(ZjfaFragment.newInstance(10, rid), "方案");
            vpHomePager.setAdapter(mPagerAdapter);
            tlHomeTab.setupWithViewPager(vpHomePager);
            tlHomeTab.getTabAt(1).select();
        } else {
            mPagerAdapter.addFragment(QuizTypeFragment.newInstance(detailBean), "竞猜");
            mPagerAdapter.addFragment(ZjfaFragment.newInstance(10, rid), "方案");
            vpHomePager.setAdapter(mPagerAdapter);
            tlHomeTab.setupWithViewPager(vpHomePager);
            tlHomeTab.getTabAt(1).select();
        }
    }

    @Override
    public boolean isStatusBarEnabled() {
        // 使用沉浸式状态栏
        return super.isStatusBarEnabled();
    }

    @Override
    public boolean isStatusBarDarkFont() {
        return false;
    }

    /**
     * 用toolBar替换ActionBar
     */
    private void setToolBarReplaceActionBar() {
        setSupportActionBar(tbHomeTitle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tbHomeTitle.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /**
     * 使用CollapsingToolbarLayout必须把title设置到CollapsingToolbarLayout上，
     * 设置到Toolbar上则不会显示
     */
    private void setTitleToCollapsingToolbarLayout() {
        appbarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset <= -headLayout.getHeight() / 2) {
                    ctlHomeBar.setTitle(detailBean.getItem1_name() + " VS " + detailBean.getItem2_name());
                    tvTitle.setVisibility(View.GONE);
                    //使用下面两个CollapsingToolbarLayout的方法设置展开透明->折叠时你想要的颜色
                    ctlHomeBar.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
                    ctlHomeBar.setCollapsedTitleTextColor(getResources().getColor(R.color.white));
                } else {
                    ctlHomeBar.setTitle("");
                    tvTitle.setVisibility(View.VISIBLE);
                }
            }
        });
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