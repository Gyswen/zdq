package com.sameal.dd.ui.ddActivity;

import android.util.Log;
import android.view.View;

import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.hjq.base.BaseFragmentAdapter;
import com.sameal.dd.R;
import com.sameal.dd.common.MyActivity;
import com.sameal.dd.common.MyFragment;
import com.sameal.dd.helper.LogUtils;
import com.sameal.dd.http.response.ClassifyChildEntry;
import com.sameal.dd.http.response.ClassifyEntry;
import com.sameal.dd.http.response.FilterInfoBean;
import com.sameal.dd.ui.ddFragment.GuessListFragment;
import com.samluys.filtertab.FilterResultBean;
import com.samluys.filtertab.FilterTabConfig;
import com.samluys.filtertab.listener.OnFilterToViewListener;
import com.samluys.filtertab.popupwindow.AreaSelectPopupWindow;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangj
 * @date 2020/12/18 21:30
 * desc 我的竞猜
 */
public class GuessActivity extends MyActivity {

    private TabLayout mTabLayout;

    private BaseFragmentAdapter<MyFragment> mPagerAdapter;
    private ViewPager mViewPager;
    List<ClassifyEntry> classifyEntries = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_guess;
    }

    @Override
    protected void initView() {
        mTabLayout = findViewById(R.id.tl_home_tab);
        mViewPager = findViewById(R.id.vp_home_pager);

        mPagerAdapter = new BaseFragmentAdapter<>(this);
        mPagerAdapter.addFragment(GuessListFragment.newInstance(1), getString(R.string.all));
        mPagerAdapter.addFragment(GuessListFragment.newInstance(2), getString(R.string.dgb));
        mPagerAdapter.addFragment(GuessListFragment.newInstance(3), getString(R.string.cz));
        mPagerAdapter.addFragment(GuessListFragment.newInstance(4), getString(R.string.qcz));
        mPagerAdapter.addFragment(GuessListFragment.newInstance(5), getString(R.string.ytd));
        mViewPager.setAdapter(mPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    protected void initData() {
        List<ClassifyChildEntry> childEntries = new ArrayList<>();
        childEntries.add(new ClassifyChildEntry(1, "英雄联盟", 1));
        childEntries.add(new ClassifyChildEntry(2, "data2", 0));
        childEntries.add(new ClassifyChildEntry(3, "王者荣耀", 0));
        childEntries.add(new ClassifyChildEntry(4, "csgo", 0));
        childEntries.add(new ClassifyChildEntry(5, "守望先锋", 0));
        childEntries.add(new ClassifyChildEntry(6, "炉石传说", 0));
        childEntries.add(new ClassifyChildEntry(7, "星际争霸2", 0));

        List<ClassifyChildEntry> childEntries2 = new ArrayList<>();
        childEntries2.add(new ClassifyChildEntry(1, "足球", 1));
        childEntries2.add(new ClassifyChildEntry(2, "篮球", 0));
        classifyEntries.add(new ClassifyEntry(1, "电竞", 1, childEntries));
        classifyEntries.add(new ClassifyEntry(2, "体育", 0, childEntries2));
    }

    @Override
    public void onRightClick(View v) {
        AreaSelectPopupWindow popupWindow = new AreaSelectPopupWindow(this, classifyEntries, FilterTabConfig.FILTER_TYPE_AREA
                , 0, new OnFilterToViewListener() {
            @Override
            public void onFilterToView(FilterResultBean resultBean) {
                LogUtils.d(TAG, "onFilterToView: " + resultBean.getName());
                toast(resultBean.getName());
            }

            @Override
            public void onFilterListToView(List<FilterResultBean> resultBean) {
                LogUtils.d(TAG, "onFilterListToView: " + resultBean);
            }
        });
        popupWindow.showAsDropDown(v);
    }
}