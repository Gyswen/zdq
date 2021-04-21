package com.sameal.dd.ui.ddActivity;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.sameal.dd.R;
import com.sameal.dd.aop.SingleClick;
import com.sameal.dd.common.MyActivity;
import com.sameal.dd.helper.Identifier;
import com.sameal.dd.http.model.HttpData;
import com.sameal.dd.http.response.LoginBean;
import com.sameal.dd.other.AppConfig;

/**
 *
 * 账号与安全
 *
 */

public class AccountSafetyActivity extends MyActivity {

    LinearLayout llLoginPassword;
    TextView tvPhone;
    TextView tvRealStatus;
    LinearLayout llPhone;
    LinearLayout llVerified;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_account_safety;
    }

    @Override
    protected void initView() {
        llLoginPassword = (LinearLayout) findViewById(R.id.ll_login_password);
        tvPhone = (TextView) findViewById(R.id.tv_phone);
        tvRealStatus = (TextView) findViewById(R.id.tv_real_status);
        llPhone = (LinearLayout) findViewById(R.id.ll_phone);
        llVerified = (LinearLayout) findViewById(R.id.ll_verified);
        setOnClickListener(R.id.ll_login_password, R.id.ll_phone, R.id.ll_verified);
    }

    @Override
    protected void initData() {

    }

    @SingleClick
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_login_password:
                startActivity(UpPasswordActivity.class);
                break;
            case R.id.ll_phone:
                break;
            case R.id.ll_verified:
                if (AppConfig.getLoginBean().getReal_status() == 0) {
                    startActivity(VerifiedActivity.class);
                } else if (AppConfig.getLoginBean().getReal_status() == 1) {
                    toast("已认证，请等待审核");
                } else {
                    toast("已认证，无需重复认证");
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserInfo();
    }

    /**
     * 获取用户信息
     */
    private void getUserInfo() {
        LoginBean loginBean = AppConfig.getLoginBean();
        EasyHttp.get(this)
                .api("api/sists/getUserinfo?uid=" + loginBean.getId() + "&client_id=" + util.MD5.md5Str(Identifier.getSN()))
                .request(new HttpCallback<HttpData<LoginBean>>(this) {
                    @Override
                    public void onSucceed(HttpData<LoginBean> result) {
                        super.onSucceed(result);
                        if (result.getCode() == 1) {
                            AppConfig.setLoginBean(result.getData());
                            tvPhone.setText(AppConfig.getLoginBean().getMobile());
                            tvRealStatus.setText(AppConfig.getLoginBean().getReal_status() == 0 ? "未认证" :
                                    (AppConfig.getLoginBean().getReal_status() == 1 ? "等待审核" : "已认证"));
                        }
                    }
                });
    }
}