package com.sameal.dd.ui.ddActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.hjq.base.BaseFragmentAdapter;
import com.sameal.dd.R;
import com.sameal.dd.action.StatusAction;
import com.sameal.dd.common.MyActivity;
import com.sameal.dd.common.MyFragment;
import com.sameal.dd.ui.ddFragment.MsgFragment;
import com.sameal.dd.widget.HintLayout;

/**
 * @author zhangj
 * @date 2020/12/17 22:58
 * desc 消息中心
 */
public class MsgCenterActivity extends MyActivity  {

    private TabLayout mTabLayout;

    private BaseFragmentAdapter<MyFragment> mPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_msg_center;
    }

    @Override
    protected void initView() {
        mTabLayout = findViewById(R.id.tl_home_tab);
        mViewPager = findViewById(R.id.vp_home_pager);

        mPagerAdapter = new BaseFragmentAdapter<>(this);
        mPagerAdapter.addFragment(MsgFragment.newInstance(0), getString(R.string.all));
        mPagerAdapter.addFragment(MsgFragment.newInstance(1), getString(R.string.notice));
        mPagerAdapter.addFragment(MsgFragment.newInstance(2), getString(R.string.order));
        mViewPager.setAdapter(mPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    protected void initData() {

    }
}