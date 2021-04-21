package com.sameal.dd.ui.ddFragment;

import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import com.hjq.base.BaseFragmentAdapter;
import com.sameal.dd.R;
import com.sameal.dd.common.MyFragment;
import com.sameal.dd.helper.LogUtils;
import com.sameal.dd.http.response.SportDetailBean;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangj
 * @date 2021/1/11 21:53
 * desc 竞猜分类
 */
public class QuizTypeFragment extends MyFragment {

    TabLayout tlHomeTab;
    ViewPager vpHomePager;
    private SportDetailBean detailBean;
    private BaseFragmentAdapter<MyFragment> mPagerAdapter;
    private List<QuizBean> quizBeans;

    public static QuizTypeFragment newInstance(SportDetailBean eventsBeans) {
        return new QuizTypeFragment(eventsBeans);
    }

    public QuizTypeFragment(SportDetailBean detailBean) {
        this.detailBean = detailBean;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_quiz_type;
    }

    @Override
    protected void initView() {
        tlHomeTab = (TabLayout) findViewById(R.id.tl_home_tab);
        vpHomePager = (ViewPager) findViewById(R.id.vp_home_pager);

        quizBeans = new ArrayList<>();
        mPagerAdapter = new BaseFragmentAdapter<>(this);
    }

    @Override
    protected void initData() {
        setData();
    }

    private void setData() {
        for (SportDetailBean.EventsBean eventsBean : detailBean.getEvents()) {
            if (quizBeans.size() == 0) {
                List<SportDetailBean.EventsBean> events = new ArrayList<>();
                events.add(eventsBean);
                quizBeans.add(new QuizBean(eventsBean.getRound(), events));
            } else {
                boolean isHave = false;
                for (QuizBean quizBean : quizBeans) {
                    if (quizBean.getRound() == eventsBean.getRound()) {
                        quizBean.getEventsBeans().add(eventsBean);
                        isHave = true;
                    }
                }
                if (!isHave) {
                    List<SportDetailBean.EventsBean> events = new ArrayList<>();
                    events.add(eventsBean);
                    quizBeans.add(new QuizBean(eventsBean.getRound(), events));
                }
            }
        }

        for (int i = 0; i < quizBeans.size(); i++) {
            if (i == 0) {
                mPagerAdapter.addFragment(QuizFragment.newInstance(detailBean, quizBeans.get(i)), "全场");
            } else {
                mPagerAdapter.addFragment(QuizFragment.newInstance(detailBean, quizBeans.get(i)), "第" + i + "场");
            }
        }
        vpHomePager.setAdapter(mPagerAdapter);
        tlHomeTab.setupWithViewPager(vpHomePager);
        tlHomeTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                LogUtils.d(TAG, "onTabSelected: ");
                mPagerAdapter.getShowFragment().setIsVisible(true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                LogUtils.d(TAG, "onTabUnselected: ");
                mPagerAdapter.getShowFragment().setIsVisible(false);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public class QuizBean {
        private int round;
        private List<SportDetailBean.EventsBean> eventsBeans;

        public QuizBean(int round, List<SportDetailBean.EventsBean> eventsBeans) {
            this.round = round;
            this.eventsBeans = eventsBeans;
        }

        public int getRound() {
            return round;
        }

        public void setRound(int round) {
            this.round = round;
        }

        public List<SportDetailBean.EventsBean> getEventsBeans() {
            if (eventsBeans == null) {
                return new ArrayList<>();
            }
            return eventsBeans;
        }

        public void setEventsBeans(List<SportDetailBean.EventsBean> eventsBeans) {
            this.eventsBeans = eventsBeans;
        }
    }
}