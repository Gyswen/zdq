package com.sameal.dd.ui.ddReview;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.gyf.immersionbar.BarHide;
import com.gyf.immersionbar.ImmersionBar;
import com.hjq.base.BaseFragmentAdapter;
import com.sameal.dd.R;
import com.sameal.dd.common.MyActivity;
import com.sameal.dd.common.MyFragment;
import com.sameal.dd.other.IntentKey;
import com.sameal.dd.widget.PlayerView;

/**
 * 视频播放界面（审核）
 */

public class ReviewVideoPlayActivity extends MyActivity {

    private com.sameal.dd.widget.PlayerView playerView;
    private com.google.android.material.tabs.TabLayout tlVideoTab;
    private androidx.viewpager.widget.ViewPager vpVideoPager;

    private BaseFragmentAdapter<MyFragment> mPagerAdapter;
    private int page = 1;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_review_video_play;
    }

    @Override
    protected void initView() {

        playerView = (PlayerView) findViewById(R.id.player_view);
        tlVideoTab = (TabLayout) findViewById(R.id.tl_video_tab);
        vpVideoPager = (ViewPager) findViewById(R.id.vp_video_pager);

        mPagerAdapter = new BaseFragmentAdapter<MyFragment>(this);
    }

    @Override
    protected void initData() {
        playerView.setOnGoBackListener(new PlayerView.onGoBackListener() {
            @Override
            public void onClickGoBack(PlayerView view) {
                onBackPressed();
            }
        });
        playerView.setGestureEnabled(true);
        playerView.setVideoTitle(getString(IntentKey.TITLE));
        playerView.setVideoSource(getString(IntentKey.VIDEO));
        playerView.start();

        mPagerAdapter.addFragment(ReviewVideoPlayFragment.newInstance(getString(IntentKey.ID)), "视频");
        vpVideoPager.setAdapter(mPagerAdapter);
        tlVideoTab.setupWithViewPager(vpVideoPager);
    }

    @Override
    protected void onResume() {
        playerView.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        playerView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        playerView.onDestroy();
        super.onDestroy();
    }

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

}