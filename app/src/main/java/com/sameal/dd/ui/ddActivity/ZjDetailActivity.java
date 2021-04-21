package com.sameal.dd.ui.ddActivity;

import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import com.bumptech.glide.Glide;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.gyf.immersionbar.ImmersionBar;
import com.hjq.base.BaseFragmentAdapter;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.hjq.widget.view.CircleImageView;
import com.sameal.dd.R;
import com.sameal.dd.aop.SingleClick;
import com.sameal.dd.common.MyActivity;
import com.sameal.dd.common.MyFragment;
import com.sameal.dd.helper.BigDecimalUtils;
import com.sameal.dd.http.model.HttpData;
import com.sameal.dd.http.response.ZjBean;
import com.sameal.dd.other.AppConfig;
import com.sameal.dd.other.IntentKey;
import com.sameal.dd.ui.ddFragment.ZjfaFragment;
import com.sameal.dd.widget.XCollapsingToolbarLayout;

/**
 * @author zhangj
 * @date 2021/1/3 17:39
 * desc 专家详情
 */
public class ZjDetailActivity extends MyActivity {

    Toolbar tbHomeTitle;
    //    @BindView(R.id.tv_title)
//    TextView tvTitle;
//    @BindView(R.id.title)
//    TitleBar title;
    ImageView imgBack;
    CircleImageView imgAvatar;
    TextView tvName;
    TextView tvJob;
    TextView tvNum;
    ImageView imgAdd;
    LinearLayout llFollow;
    TextView tvContent;
    TextView tvPercent;
    TextView tvJqlh;
    TextView tvZglh;
    XCollapsingToolbarLayout ctlHomeBar;
    TabLayout tlHomeTab;
    ViewPager vpHomePager;
    AppBarLayout appBarLayout;
    LinearLayout headLayout;
    private BaseFragmentAdapter<MyFragment> mPagerAdapter;

    private ZjBean zjBean;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_zj_detail;
    }

    @Override
    protected void initView() {
        ctlHomeBar = (XCollapsingToolbarLayout) findViewById(R.id.ctl_home_bar);
        headLayout = (LinearLayout) findViewById(R.id.head_layout);
        imgBack = (ImageView) findViewById(R.id.img_back);
        imgAvatar = (CircleImageView) findViewById(R.id.img_avatar);
        tvName = (TextView) findViewById(R.id.tv_name);
        tvJob = (TextView) findViewById(R.id.tv_job);
        tvNum = (TextView) findViewById(R.id.tv_num);
        llFollow = (LinearLayout) findViewById(R.id.ll_follow);
        imgAdd = (ImageView) findViewById(R.id.img_add);
        tvContent = (TextView) findViewById(R.id.tv_content);
        tvPercent = (TextView) findViewById(R.id.tv_percent);
        tvJqlh = (TextView) findViewById(R.id.tv_jqlh);
        tvZglh = (TextView) findViewById(R.id.tv_zglh);
        tbHomeTitle = (Toolbar) findViewById(R.id.tb_home_title);
        tlHomeTab = (TabLayout) findViewById(R.id.tl_home_tab);
        vpHomePager = (ViewPager) findViewById(R.id.vp_home_pager);
        appBarLayout = (AppBarLayout) findViewById(R.id.appbarLayout);

    }

    @Override
    protected void initData() {
        // 给这个 ToolBar 设置顶部内边距，才能和 TitleBar 进行对齐
        ImmersionBar.setTitleBar(this, tbHomeTitle);
        zjBean = new Gson().fromJson(getString(IntentKey.DATA), ZjBean.class);

        mPagerAdapter = new BaseFragmentAdapter<>(this);
        mPagerAdapter.addFragment(ZjfaFragment.newInstance(1,zjBean.getId()), "最新方案");
        mPagerAdapter.addFragment(ZjfaFragment.newInstance(0,zjBean.getId()), "历史方案");
        vpHomePager.setAdapter(mPagerAdapter);
        tlHomeTab.setupWithViewPager(vpHomePager);

        setToolBarReplaceActionBar();
        setTitleToCollapsingToolbarLayout();
        setData();
        setOnClickListener(R.id.ll_follow);
//        CollapsingToolbarLayout.LayoutParams params = new CollapsingToolbarLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        params.setMargins(0, tbHomeTitle.getHeight(), 0, 0);
//        LogUtils.d(TAG, "initData: " + tbHomeTitle.getMeasuredHeight());
//        headLayout.setPadding(0, tbHomeTitle.getMeasuredHeight(), 0, 0);
//        headLayout.setLayoutParams(params);
    }

    @SingleClick
    @Override
    public void onClick(View v) {
        setFollow();
    }

    @Override
    public boolean isStatusBarEnabled() {
        // 使用沉浸式状态栏
        return !super.isStatusBarEnabled();
    }

    @Override
    public boolean isStatusBarDarkFont() {
        return ctlHomeBar.isScrimsShown();
    }

    private void setData() {
        Glide.with(this)
                .load(zjBean.getAvatar())
                .error(R.mipmap.icon_contact_avatar_default)
                .placeholder(R.mipmap.icon_contact_avatar_default)
                .into(imgAvatar);
        tvName.setText(zjBean.getNickname());
        tvJob.setText(zjBean.getOccupation());
        tvNum.setText(zjBean.getFans()+"");
        if (zjBean.getIs_follow() == 1) {
            imgAdd.setImageResource(R.mipmap.icon_selected);
        } else {
            imgAdd.setImageResource(R.mipmap.gift_add);
        }
        tvContent.setText(zjBean.getDescribes());
        tvPercent.setText(BigDecimalUtils.mul(zjBean.getHit_rate(),"100",0) + "%");
        tvJqlh.setText(Html.fromHtml(zjBean.getFrequency_red() + "连红"));
        tvZglh.setText(Html.fromHtml(zjBean.getContinuity_red() + "连红"));
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
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset <= -headLayout.getHeight() / 2) {
                    ctlHomeBar.setTitle(getString(R.string.zj_detail));
                    //使用下面两个CollapsingToolbarLayout的方法设置展开透明->折叠时你想要的颜色
                    ctlHomeBar.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
                    ctlHomeBar.setCollapsedTitleTextColor(getResources().getColor(R.color.white));
                } else {
                    ctlHomeBar.setTitle("");
                }
            }
        });
    }

    /**
     * 关注取消关注专家
     *
     * @param
     */
    public void setFollow() {
        EasyHttp.get(this)
                .api("api/Forum/setFollow?uid=" + AppConfig.getLoginBean().getId() + "&eid=" + zjBean.getId())
                .request(new HttpCallback<HttpData<String>>(this) {
                    @Override
                    public void onSucceed(HttpData<String> result) {
                        super.onSucceed(result);
                        if (result.getCode() == 1) {
                            if (zjBean.getIs_follow() == 1) {
                                zjBean.setIs_follow(0);
                            } else {
                                zjBean.setIs_follow(1);
                            }
                            setData();
                        }
                    }
                });
    }
}