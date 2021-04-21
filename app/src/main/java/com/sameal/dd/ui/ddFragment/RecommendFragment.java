package com.sameal.dd.ui.ddFragment;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
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
import com.sameal.dd.ui.ddActivity.MoreZjActivity;
import com.sameal.dd.ui.ddActivity.PdAndGdActivity;
import com.sameal.dd.widget.XCollapsingToolbarLayout;

/**
 * @author zhangj
 * @date 2020/12/26 21:02
 * desc 社区 推荐
 */
public class RecommendFragment extends MyFragment {

    ImageView imgBuy;
    ImageView imgFollow;
    TabLayout tlZj;
    RelativeLayout rlMoreZj;
    ViewPager vpZjPager;
    TabLayout tlZjfa;
    ViewPager vpZjfaPager;
    TextView tvZjfa;
    Toolbar tbHomeTitle;
    XCollapsingToolbarLayout ctlHomeBar;
    AppBarLayout appbarLayout;
    LinearLayout headLayout;
    private BaseFragmentAdapter<MyFragment> mPagerZjAdapter;
    private BaseFragmentAdapter<MyFragment> mPagerZjfaAdapter;

    public static RecommendFragment newInstance() {
        return new RecommendFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_recommend;
    }

    @Override
    protected void initView() {
        appbarLayout = (AppBarLayout) findViewById(R.id.appbarLayout);
        ctlHomeBar = (XCollapsingToolbarLayout) findViewById(R.id.ctl_home_bar);
        headLayout = (LinearLayout) findViewById(R.id.head_layout);
        imgBuy = (ImageView) findViewById(R.id.img_buy);
        imgFollow = (ImageView) findViewById(R.id.img_follow);
        tlZj = (TabLayout) findViewById(R.id.tl_zj);
        rlMoreZj = (RelativeLayout) findViewById(R.id.rl_more_zj);
        vpZjPager = (ViewPager) findViewById(R.id.vp_zj_pager);
        tvZjfa = (TextView) findViewById(R.id.tv_zjfa);
        tbHomeTitle = (Toolbar) findViewById(R.id.tb_home_title);
        tlZjfa = (TabLayout) findViewById(R.id.tl_zjfa);
        vpZjfaPager = (NoScrollViewPager) findViewById(R.id.vp_zjfa_pager);
        setOnClickListener(R.id.img_buy, R.id.img_follow, R.id.rl_more_zj);
    }

    @Override
    protected void initData() {
        mPagerZjAdapter = new BaseFragmentAdapter<>(this);
        mPagerZjAdapter.addFragment(ZjFragment.newInstance(1), "命中榜");
        mPagerZjAdapter.addFragment(ZjFragment.newInstance(2), "连红榜");
        vpZjPager.setAdapter(mPagerZjAdapter);
        tlZj.setupWithViewPager(vpZjPager);
        tlZj.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mPagerZjAdapter.getShowFragment().setIsVisible(true,tab.getPosition()+1);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        mPagerZjfaAdapter = new BaseFragmentAdapter<>(this);
        mPagerZjfaAdapter.addFragment(ZjfaFragment.newInstance(1), "最新");
        mPagerZjfaAdapter.addFragment(ZjfaFragment.newInstance(2), "价格");
        mPagerZjfaAdapter.addFragment(ZjfaFragment.newInstance(3), "连红");
        mPagerZjfaAdapter.addFragment(ZjfaFragment.newInstance(4), "命中");
        mPagerZjfaAdapter.addFragment(ZjfaFragment.newInstance(5), "免费");
        vpZjfaPager.setAdapter(mPagerZjfaAdapter);
        tlZjfa.setupWithViewPager(vpZjfaPager);

        setTitleToCollapsingToolbarLayout();
        addOnVisible(new OnVisible() {
            @Override
            public void isUsetVisible(boolean isVisible) {
                if (isVisible) {
                    mPagerZjAdapter.getShowFragment().setIsVisible(isVisible);
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0) {
            mPagerZjAdapter.getShowFragment().setIsVisible(true);
        }

    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.img_buy:
                if (AppConfig.isLogin()) {
                    intent.setClass(getActivity(),PdAndGdActivity.class);
                    intent.putExtra(SpUtil.TYPE,0);
                    startActivity(intent);
                } else {
                    startActivity(LoginActivity.class);
                }
                break;
            case R.id.img_follow:
                if (AppConfig.isLogin()) {
                    intent.setClass(getActivity(),PdAndGdActivity.class);
                    intent.putExtra(SpUtil.TYPE,1);
                    startActivity(intent);
                } else {
                    startActivity(LoginActivity.class);
                }
                break;
            case R.id.rl_more_zj:
                if (AppConfig.isLogin()) {
                    intent.setClass(getActivity(),MoreZjActivity.class);
                    startActivityForResult(intent,0);
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