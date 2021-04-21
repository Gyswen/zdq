package com.sameal.dd.ui.ddActivity;

import androidx.recyclerview.widget.RecyclerView;
import com.hjq.bar.TitleBar;
import com.sameal.dd.R;
import com.sameal.dd.action.StatusAction;
import com.sameal.dd.common.MyActivity;
import com.sameal.dd.helper.SpUtil;
import com.sameal.dd.widget.HintLayout;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

/**
 * @author zhangj
 * @date 2021/1/10 21:39
 * desc 拼单大厅and跟单大厅
 */
public class PdAndGdActivity extends MyActivity implements StatusAction {

    RecyclerView recy;
    HintLayout hint;
    SmartRefreshLayout refresh;
    TitleBar titleBar;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_pd_and_gd;
    }

    @Override
    protected void initView() {
        refresh = (SmartRefreshLayout) findViewById(R.id.refresh);
        hint = (HintLayout) findViewById(R.id.hint);
        recy = (RecyclerView) findViewById(R.id.recy);
        titleBar = (TitleBar) findViewById(R.id.titlebar_text);
    }

    @Override
    protected void initData() {
        if (getInt(SpUtil.TYPE) == 0) {
            titleBar.setTitle(R.string.pd);
        } else if (getInt(SpUtil.TYPE) == 1) {
            titleBar.setTitle(R.string.gd);
        } else if (getInt(SpUtil.TYPE) == 2) {
            titleBar.setTitle(R.string.rv_mz);
        } else if (getInt(SpUtil.TYPE) == 3) {
            titleBar.setTitle(R.string.rv_hb);
        }
        showEmpty();
    }

    @Override
    public HintLayout getHintLayout() {
        return hint;
    }
}