package com.sameal.dd.ui.ddFragment;

import androidx.recyclerview.widget.RecyclerView;
import com.sameal.dd.R;
import com.sameal.dd.action.StatusAction;
import com.sameal.dd.common.MyFragment;
import com.sameal.dd.widget.HintLayout;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

/**
 * @author zhangj
 * @date 2021/1/8 16:15
 * @desc 描述：分析
 */
public class AnalysisFragment extends MyFragment implements StatusAction {

    RecyclerView recy;
    HintLayout hint;
    SmartRefreshLayout refresh;

    public static AnalysisFragment newInstance() {
        return new AnalysisFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_analysis;
    }

    @Override
    protected void initView() {
        refresh = (SmartRefreshLayout) findViewById(R.id.refresh);
        hint = (HintLayout) findViewById(R.id.hint);
        recy = (RecyclerView) findViewById(R.id.recy);
    }

    @Override
    protected void initData() {
        showEmpty();
    }

    @Override
    public HintLayout getHintLayout() {
        return hint;
    }
}