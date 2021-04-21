package com.sameal.dd.ui.ddReview;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.hjq.base.BaseFragmentAdapter;
import com.hjq.widget.layout.NoScrollViewPager;
import com.sameal.dd.R;
import com.sameal.dd.aop.SingleClick;
import com.sameal.dd.common.MyFragment;
import com.sameal.dd.helper.SpUtil;
import com.sameal.dd.other.AppConfig;
import com.sameal.dd.ui.ddActivity.LoginActivity;
import com.sameal.dd.ui.ddActivity.PdAndGdActivity;
import com.sameal.dd.ui.ddFragment.ZjfaFragment;
import com.sameal.dd.widget.XCollapsingToolbarLayout;

/**
 * @author zhangj
 * @date 2020/12/26 21:02
 * desc 社区 推荐（审核）
 */
public class ReviewRecommendFragment extends MyFragment {

    LinearLayout imgBuy;
    LinearLayout imgFollow;
    TabLayout tlZjfa;
    ViewPager vpZjfaPager;
    TextView tvZjfa;
    Toolbar tbHomeTitle;
    XCollapsingToolbarLayout ctlHomeBar;
    AppBarLayout appbarLayout;
    LinearLayout headLayout;
    private BaseFragmentAdapter<MyFragment> mPagerZjfaAdapter;

    public static ReviewRecommendFragment newInstance() {
        return new ReviewRecommendFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.review_fragment_recommend;
    }

    @Override
    protected void initView() {
        appbarLayout = (AppBarLayout) findViewById(R.id.appbarLayout);
        ctlHomeBar = (XCollapsingToolbarLayout) findViewById(R.id.ctl_home_bar);
        headLayout = (LinearLayout) findViewById(R.id.head_layout);
        imgBuy = (LinearLayout) findViewById(R.id.img_buy);
        imgFollow = (LinearLayout) findViewById(R.id.img_follow);
        tvZjfa = (TextView) findViewById(R.id.tv_zjfa);
        tbHomeTitle = (Toolbar) findViewById(R.id.tb_home_title);
        tlZjfa = (TabLayout) findViewById(R.id.tl_zjfa);
        vpZjfaPager = (NoScrollViewPager) findViewById(R.id.vp_zjfa_pager);
        setOnClickListener(R.id.img_buy, R.id.img_follow);
    }

    @Override
    protected void initData() {
        mPagerZjfaAdapter = new BaseFragmentAdapter<>(this);
        mPagerZjfaAdapter.addFragment(ZjfaFragment.newInstance(1), "最新");
        mPagerZjfaAdapter.addFragment(ZjfaFragment.newInstance(2), "价格");
        mPagerZjfaAdapter.addFragment(ZjfaFragment.newInstance(3), "连红");
        mPagerZjfaAdapter.addFragment(ZjfaFragment.newInstance(4), "命中");
        mPagerZjfaAdapter.addFragment(ZjfaFragment.newInstance(5), "免费");
        vpZjfaPager.setAdapter(mPagerZjfaAdapter);
        tlZjfa.setupWithViewPager(vpZjfaPager);

        setTitleToCollapsingToolbarLayout();
    }

    @SingleClick
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.img_buy:
                if (AppConfig.isLogin()) {
                    intent.setClass(getActivity(),PdAndGdActivity.class);
                    intent.putExtra(SpUtil.TYPE,2);
                    startActivity(intent);
                } else {
                    startActivity(LoginActivity.class);
                }
                break;
            case R.id.img_follow:
                if (AppConfig.isLogin()) {
                    intent.setClass(getActivity(),PdAndGdActivity.class);
                    intent.putExtra(SpUtil.TYPE,3);
                    startActivity(intent);
                } else {
                    startActivity(LoginActivity.class);
                }
                break;
        }
    }

    @Override
    public boolean isStatusBarEnabled() {
        // 使用沉浸式状态栏
        return !super.isStatusBarEnabled();
    }

    @Override
    public boolean statusBarDarkFont() {
        return ctlHomeBar.isScrimsShown();
    }


    /**
     * 使用CollapsingToolbarLayout必须把title设置到CollapsingToolbarLayout上，
     * 设置到Toolbar上则不会显示
     */
    private void setTitleToCollapsingToolbarLayout() {
        appbarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset <= -headLayout.getHeight() / 1.5) {
                    ctlHomeBar.setTitle(getString(R.string.zjfa));
                    //使用下面两个CollapsingToolbarLayout的方法设置展开透明->折叠时你想要的颜色
                    ctlHomeBar.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
                    ctlHomeBar.setCollapsedTitleTextColor(getResources().getColor(R.color.colorAccent));
                } else {
                    ctlHomeBar.setTitle("");
                }
            }
        });
    }
}