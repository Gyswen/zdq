package com.sameal.dd.ui.ddFragment;

import android.content.Intent;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import android.view.View;
import android.widget.ImageButton;
import com.google.android.material.tabs.TabLayout;
import com.hjq.base.BaseFragmentAdapter;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.sameal.dd.R;
import com.sameal.dd.aop.SingleClick;
import com.sameal.dd.common.MyFragment;
import com.sameal.dd.http.model.HttpData;
import com.sameal.dd.http.response.ClassifyEntry;
import com.sameal.dd.http.response.ForumCaseBean;
import com.sameal.dd.other.AppConfig;
import com.sameal.dd.other.IntentKey;
import com.sameal.dd.ui.ddActivity.LoginActivity;
import com.sameal.dd.ui.ddActivity.MoreZjActivity;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangj
 * @date 2020/12/16 23:00
 * desc 社区
 */
public class CommunityFragment extends MyFragment {
    public static CommunityFragment newInstance() {
        return new CommunityFragment();
    }

    TabLayout tlHomeTab;
    ImageButton imgFilter;
    ViewPager mViewPager;

    private BaseFragmentAdapter<MyFragment> mPagerAdapter;
    private List<ClassifyEntry> classifyEntries = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_community;
    }

    @Override
    protected void initView() {
        tlHomeTab = (TabLayout) findViewById(R.id.tl_home_tab);
        imgFilter = (ImageButton) findViewById(R.id.img_filter);
        mViewPager = (ViewPager) findViewById(R.id.vp_home_pager);
        mPagerAdapter = new BaseFragmentAdapter<>(this);
        setOnClickListener(R.id.tv_attention);
    }

    @Override
    protected void initData() {
        getForumCase();
    }

    @SingleClick
    @Override
    public void onClick(View v) {
        if (AppConfig.isLogin()) {
            //更多
            Intent intent = new Intent(getActivity(), MoreZjActivity.class);
            intent.putExtra(IntentKey.CODE, 4);
            startActivityForResult(intent,0);
        } else {
            startActivity(LoginActivity.class);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0) {
            mPagerAdapter.getShowFragment().setIsVisible(true);
        }

    }

    /**
     * 获取分类
     */
    private void getForumCase() {
        EasyHttp.get(this)
                .api("api/Forum/getForumCase")
                .request(new HttpCallback<HttpData<List<ForumCaseBean>>>(this) {
                    @Override
                    public void onSucceed(HttpData<List<ForumCaseBean>> result) {
                        super.onSucceed(result);
                        if (result.getCode() == 1) {
                            mPagerAdapter.addFragment(RecommendFragment.newInstance(), getString(R.string.tj));
//                            mPagerAdapter.addFragment(GamingFragment.newInstance(), getString(R.string.dj));
//                            mPagerAdapter.addFragment(FootballFragment.newInstance(), getString(R.string.zq));
                            for (ForumCaseBean forumCaseBean : result.getData()) {
                                mPagerAdapter.addFragment(GamingFragment.newInstance(forumCaseBean), forumCaseBean.getTitle());
                            }
                            mViewPager.setAdapter(mPagerAdapter);
                            tlHomeTab.setupWithViewPager(mViewPager);
                        }
                    }
                });
    }
}