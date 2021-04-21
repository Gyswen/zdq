package com.sameal.dd.ui.ddActivity;

import android.widget.TextView;
import com.google.gson.Gson;
import com.sameal.dd.R;
import com.sameal.dd.common.MyActivity;
import com.sameal.dd.helper.TimeUtil;
import com.sameal.dd.http.response.MsgBean;
import com.sameal.dd.other.IntentKey;

/**
 * @author zhangj
 * @date 2021/1/30 20:03
 * @描述 消息详情
 */
public class MsgDetailActivity extends MyActivity {

    TextView tvTitle;
    TextView tvTime;
    TextView tvContent;

    MsgBean msgBean;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_msg_detail;
    }

    @Override
    protected void initView() {
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvTime = (TextView) findViewById(R.id.tv_time);
        tvContent = (TextView) findViewById(R.id.tv_content);
    }

    @Override
    protected void initData() {
        msgBean = new Gson().fromJson(getString(IntentKey.DATA), MsgBean.class);
        tvTitle.setText(msgBean.getTitle());
        tvTime.setText(TimeUtil.timeCompareYMDHMinSFigure(Long.valueOf(msgBean.getAddtime() + "000")));
        tvContent.setText(msgBean.getContent());
    }
}