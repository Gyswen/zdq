package com.sameal.dd.ui.ddActivity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import com.google.android.material.tabs.TabLayout;
import com.hjq.base.BaseFragmentAdapter;
import com.hjq.widget.layout.NoScrollViewPager;
import com.sameal.dd.R;
import com.sameal.dd.aop.SingleClick;
import com.sameal.dd.common.MyActivity;
import com.sameal.dd.common.MyFragment;
import com.sameal.dd.other.IntentKey;
import com.sameal.dd.ui.ddFragment.MoreZjFragment;
import com.sameal.dd.ui.ddFragment.RecommendFragment;

/**
 * @author zhangj
 * @date 2021/1/2 16:58
 * desc 更多
 */
public class MoreZjActivity extends MyActivity {


    ImageView imgBack;
    TabLayout tlZjfa;
    NoScrollViewPager vpZjfaPager;
    private BaseFragmentAdapter<MyFragment> mPagerZjfaAdapter;
    private int code = 0;

    public static RecommendFragment newInstance() {
        return new RecommendFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_more_zj;
    }

    @Override
    protected void initView() {
        imgBack = (ImageView) findViewById(R.id.img_back);
        tlZjfa = (TabLayout) findViewById(R.id.tl_zjfa);
        vpZjfaPager = (NoScrollViewPager) findViewById(R.id.vp_zjfa_pager);
        setOnClickListener(R.id.img_back);
    }

    @Override
    protected void initData() {
        mPagerZjfaAdapter = new BaseFragmentAdapter<>(this);
        mPagerZjfaAdapter.addFragment(MoreZjFragment.newInstance(1), "全部");
        mPagerZjfaAdapter.addFragment(MoreZjFragment.newInstance(2), "命中榜");
        mPagerZjfaAdapter.addFragment(MoreZjFragment.newInstance(3), "连红榜");
        mPagerZjfaAdapter.addFragment(MoreZjFragment.newInstance(5),"财富榜");
        mPagerZjfaAdapter.addFragment(MoreZjFragment.newInstance(4), "关注");
        vpZjfaPager.setAdapter(mPagerZjfaAdapter);
        tlZjfa.setupWithViewPager(vpZjfaPager);

        code = getInt(IntentKey.CODE);
        if (code != 0) {
            vpZjfaPager.setCurrentItem(code);
        } else {
            vpZjfaPager.setCurrentItem(0);
        }
    }

    @SingleClick
    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        setResult(0,intent);
        finish();
    }
}