package com.sameal.dd.ui.ddFragment;

import android.view.View;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import com.hjq.base.BaseFragmentAdapter;
import com.sameal.dd.R;
import com.sameal.dd.common.MyFragment;
import com.sameal.dd.http.response.ForumCaseBean;

/**
 * @author zhangj
 * @date 2020/12/26 21:06
 * desc电竞
 */
public class GamingFragment extends MyFragment {

    TabLayout tlHomeTab;
    ViewPager vpHomePager;
    private BaseFragmentAdapter<MyFragment> mPagerAdapter;

    private ForumCaseBean forumCaseBean;

    public static GamingFragment newInstance(ForumCaseBean forumCaseBean) {
        return new GamingFragment(forumCaseBean);
    }

    public GamingFragment(ForumCaseBean forumCaseBean) {
        this.forumCaseBean = forumCaseBean;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_gaming;
    }

    @Override
    protected void initView() {
        tlHomeTab = (TabLayout) findViewById(R.id.tl_home_tab);
        vpHomePager = (ViewPager) findViewById(R.id.vp_home_pager);

        mPagerAdapter = new BaseFragmentAdapter<>(this);
        if (forumCaseBean.getSon() != null && forumCaseBean.getSon().size() > 0) {
            for (ForumCaseBean.SonBean sonBean : forumCaseBean.getSon()) {
                mPagerAdapter.addFragment(GamingListFragment.newInstance(sonBean), sonBean.getTitle());
            }
        } else {
            mPagerAdapter.addFragment(GamingListFragment.newInstance(forumCaseBean.getId()), "cc");
            tlHomeTab.setVisibility(View.GONE);
        }
        vpHomePager.setAdapter(mPagerAdapter);
        tlHomeTab.setupWithViewPager(vpHomePager);
    }

    @Override
    protected void initData() {

    }
}