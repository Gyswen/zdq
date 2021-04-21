package com.sameal.dd.ui.ddActivity;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.widget.AppCompatButton;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.sameal.dd.R;
import com.sameal.dd.common.MyActivity;
import com.sameal.dd.http.model.HttpData;
import com.sameal.dd.other.AppConfig;

/**
 * @author zhangj
 * @date 2020/12/18 20:01
 * desc 兑换
 */
public class ExchangeActivity extends MyActivity {

    TextView tvMoney;
    EditText etExchangeNum;
    AppCompatButton btnExchage;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_exchange;
    }

    @Override
    protected void initView() {
        tvMoney = (TextView) findViewById(R.id.tv_money);
        etExchangeNum = (EditText) findViewById(R.id.et_exchange_num);
        btnExchage = (AppCompatButton) findViewById(R.id.btn_exchage);
    }

    @Override
    protected void initData() {
        tvMoney.setText(AppConfig.getLoginBean().getIs_ex_money());
        setOnClickListener(R.id.btn_exchage);
    }

    @Override
    public void onClick(View v) {
        if (TextUtils.isEmpty(etExchangeNum.getText().toString())) {
            toast("请输入兑换金额");
            return;
        }

        double money = Double.valueOf(AppConfig.getLoginBean().getIs_ex_money());
        double exMoney = Double.valueOf(etExchangeNum.getText().toString());
        if (exMoney > money) {
            toast("兑换金额不可大于余额");
            return;
        }

        exMoney();
    }

    /**
     * 兑换
     */
    private void exMoney() {
        EasyHttp.get(this)
                .api("api/User/exMoney?uid=" + AppConfig.getLoginBean().getId() + "&coin=" + etExchangeNum.getText().toString())
                .request(new HttpCallback<HttpData<String>>(this) {
                    @Override
                    public void onSucceed(HttpData<String> result) {
                        super.onSucceed(result);
                        if (result.getCode() == 1) {
                            finish();
                        }
                        toast(result.getMessage());
                    }
                });
    }
}