package com.sameal.dd.ui.ddActivity;

import androidx.recyclerview.widget.RecyclerView;
import com.sameal.dd.R;
import com.sameal.dd.action.StatusAction;
import com.sameal.dd.common.MyActivity;
import com.sameal.dd.widget.HintLayout;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

/**
 * @author zhangj
 * @date 2020/12/21 23:32
 * desc我的预约
 */
public class ReservationActivity extends MyActivity implements StatusAction {

    RecyclerView recy;
    HintLayout hint;
    SmartRefreshLayout refresh;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_reservation;
    }

    @Override
    protected void initView() {
        refresh = (SmartRefreshLayout) findViewById(R.id.refresh);
        hint = (HintLayout) findViewById(R.id.hint);
        recy = (RecyclerView) findViewById(R.id.recy);
    }

    @Override
    protected void initData() {
        showLayout(R.mipmap.icon_logo, R.string.icon_logo, null);
    }

    @Override
    public HintLayout getHintLayout() {
        return hint;
    }
}