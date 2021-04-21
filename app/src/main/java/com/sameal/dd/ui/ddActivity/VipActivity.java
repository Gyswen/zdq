package com.sameal.dd.ui.ddActivity;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.sameal.dd.R;
import com.sameal.dd.aop.SingleClick;
import com.sameal.dd.common.MyActivity;
import com.sameal.dd.http.model.HttpData;
import com.sameal.dd.other.AppConfig;

/**
 * @author zhangj
 * @date 2020/12/19 12:27
 * desc VIP
 */
public class VipActivity extends MyActivity {

    LinearLayout llVip;
    LinearLayout llSvip;
    LinearLayout llOne;
    TextView tvBronze;
    TextView tvSilver;
    TextView tvGold;
    TextView tvPlatinum;
    TextView tvDiamond;
    LinearLayout llTwo;

    private int type = 0;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_vip;
    }

    @Override
    protected void initView() {
        llOne = (LinearLayout) findViewById(R.id.ll_one);
        llVip = (LinearLayout) findViewById(R.id.ll_vip);
        llSvip = (LinearLayout) findViewById(R.id.ll_svip);
        llTwo = (LinearLayout) findViewById(R.id.ll_two);
        tvBronze = (TextView) findViewById(R.id.tv_bronze);
        tvSilver = (TextView) findViewById(R.id.tv_silver);
        tvGold = (TextView) findViewById(R.id.tv_gold);
        tvPlatinum = (TextView) findViewById(R.id.tv_platinum);
        tvDiamond = (TextView) findViewById(R.id.tv_diamond);
        setOnClickListener(R.id.ll_vip, R.id.ll_svip);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onLeftClick(View v) {
        if (llTwo.getVisibility() == View.VISIBLE) {
            llOne.setVisibility(View.VISIBLE);
            llTwo.setVisibility(View.GONE);
            return;
        }
        super.onLeftClick(v);
    }

    @Override
    public void onBackPressed() {
        if (llTwo.getVisibility() == View.VISIBLE) {
            llOne.setVisibility(View.VISIBLE);
            llTwo.setVisibility(View.GONE);
            return;
        }
        super.onBackPressed();
    }

    @SingleClick
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_vip:
                type = 1;
                llOne.setVisibility(View.GONE);
                llTwo.setVisibility(View.VISIBLE);
                break;
            case R.id.ll_svip:
                applySvip();
                break;
        }
    }

    /**
     * 申请SVIP
     */
    private void applySvip() {
        EasyHttp.get(this)
                .api("api/User/svipApply?uid=" + AppConfig.getLoginBean().getId())
                .request(new HttpCallback<HttpData<String>>(this) {
                    @Override
                    public void onSucceed(HttpData<String> result) {
                        super.onSucceed(result);
                        toast(result.getMessage());
                    }
                });
    }
}