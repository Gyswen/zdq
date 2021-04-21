package com.sameal.dd.ui.ddFragment;

import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import com.hjq.base.BaseFragmentAdapter;
import com.sameal.dd.R;
import com.sameal.dd.common.MyFragment;
import com.sameal.dd.http.response.ClassifyChildEntry;
import com.sameal.dd.http.response.ClassifyEntry;
import com.sameal.dd.ui.popup.FifiterPopup;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangj
 * @date 2020/12/16 23:17
 * desc 赛程
 */
public class ScheduleFragment extends MyFragment {

    TabLayout tlHomeTab;
    ImageButton imgFilter;
    ViewPager mViewPager;
    LinearLayout llTop;

    private BaseFragmentAdapter<MyFragment> mPagerAdapter;
    private List<ClassifyEntry> classifyEntries = new ArrayList<>();
    private ScheduleListFragment listFragment;

    public static ScheduleFragment newInstance() {
        return new ScheduleFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_schedule;
    }

    @Override
    protected void initView() {
        listFragment = ScheduleListFragment.newInstance(1);
        tlHomeTab = (TabLayout) findViewById(R.id.tl_home_tab);
        llTop = (LinearLayout) findViewById(R.id.ll_top);
        imgFilter = (ImageButton) findViewById(R.id.img_filter);
        mViewPager = (ViewPager) findViewById(R.id.vp_home_pager);
        setOnClickListener(R.id.img_filter);
    }

    @Override
    protected void initData() {
        mPagerAdapter = new BaseFragmentAdapter<>(this);
        mPagerAdapter.addFragment(listFragment, getString(R.string.dj));
        mPagerAdapter.addFragment(ScheduleListFragment.newInstance(2), getString(R.string.zq));
        mPagerAdapter.addFragment(ScheduleListFragment.newInstance(3), getString(R.string.lq));
        mViewPager.setAdapter(mPagerAdapter);
        tlHomeTab.setupWithViewPager(mViewPager);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position == 0) {
                    imgFilter.setVisibility(View.VISIBLE);
                } else {
                    imgFilter.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        List<ClassifyChildEntry> childEntries = new ArrayList<>();
        childEntries.add(new ClassifyChildEntry(1, "英雄联盟", 1));
        childEntries.add(new ClassifyChildEntry(2, "data2", 0));
        childEntries.add(new ClassifyChildEntry(3, "王者荣耀", 0));
        childEntries.add(new ClassifyChildEntry(4, "csgo", 0));
        childEntries.add(new ClassifyChildEntry(5, "守望先锋", 0));
        childEntries.add(new ClassifyChildEntry(6, "炉石传说", 0));
        childEntries.add(new ClassifyChildEntry(7, "星际争霸2", 0));
        classifyEntries.add(new ClassifyEntry(1, "电竞", 1, childEntries));
    }

    @Override
    public void onClick(View view) {
        new FifiterPopup.Builder(getActivity())
                .setListener(new FifiterPopup.OnListener() {
                    @Override
                    public void onConfirm(int content) {
                        ((ScheduleListFragment) listFragment).refreshData(content);
                    }

                    @Override
                    public void onCancel() {
                        ((ScheduleListFragment) listFragment).refreshData(0);
                    }
                })
                .showAsDropDown(imgFilter);
    }
}