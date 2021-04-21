package com.sameal.dd.ui.ddReview;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.hjq.widget.view.CircleImageView;
import com.mob.secverify.datatype.VerifyResult;
import com.netease.nis.quicklogin.QuickLogin;
import com.netease.nis.quicklogin.listener.QuickLoginPreMobileListener;
import com.netease.nis.quicklogin.listener.QuickLoginTokenListener;
import com.sameal.dd.R;
import com.sameal.dd.aop.SingleClick;
import com.sameal.dd.common.MyApplication;
import com.sameal.dd.common.MyFragment;
import com.sameal.dd.helper.ActivityStackManager;
import com.sameal.dd.helper.Identifier;
import com.sameal.dd.helper.LogUtils;
import com.sameal.dd.helper.QuickLoginUiConfig;
import com.sameal.dd.helper.SpUtil;
import com.sameal.dd.http.model.HttpData;
import com.sameal.dd.http.request.AutoLoginApi;
import com.sameal.dd.http.response.AutoLoginBean;
import com.sameal.dd.http.response.LoginBean;
import com.sameal.dd.http.response.MsgBean;
import com.sameal.dd.http.response.UpLoadBean;
import com.sameal.dd.other.AppConfig;
import com.sameal.dd.ui.ddActivity.AboutMineActivity;
import com.sameal.dd.ui.ddActivity.AccountSafetyActivity;
import com.sameal.dd.ui.ddActivity.ExchangeActivity;
import com.sameal.dd.ui.ddActivity.GuessActivity;
import com.sameal.dd.ui.ddActivity.LoginActivity;
import com.sameal.dd.ui.ddActivity.MainActivity;
import com.sameal.dd.ui.ddActivity.MoneyDetailActivity;
import com.sameal.dd.ui.ddActivity.MsgCenterActivity;
import com.sameal.dd.ui.ddActivity.SettingsActivity;
import com.sameal.dd.ui.ddActivity.UserInfoActivity;
import com.sameal.dd.ui.ddActivity.VerifiedActivity;
import com.sameal.dd.ui.ddActivity.VipActivity;

import java.util.List;

import okhttp3.Call;

/**
 * @author zhangj
 * @date 2020/12/16 23:17
 * desc 个人中心（审核）
 */
public class ReviewMeFragment extends MyFragment {

    public static ReviewMeFragment newInstance() {
        return new ReviewMeFragment();
    }

    private TextView tvNum;
    private TextView tvUserName;
    private TextView tvPhone;
    private TextView tv_level;
    private CircleImageView imgAvatar;

    private String errMsg;
    QuickLogin login = QuickLogin.getInstance(MyApplication.sInstance, "7f2ed00edafe4a9a83d8d394903bc8e2");

    @Override
    protected int getLayoutId() {
        return R.layout.review_fragment_me;
    }

    @Override
    protected void initView() {
        tvNum = (TextView) findViewById(R.id.tv_num);
        tvUserName = (TextView) findViewById(R.id.tv_user_name);
        tvPhone = (TextView) findViewById(R.id.tv_phone);
        tv_level = (TextView) findViewById(R.id.tv_level);
        imgAvatar = (CircleImageView) findViewById(R.id.img_avatar);
        setOnClickListener(R.id.img_setting, R.id.rl_message, R.id.ll_user_info, R.id.ll_gywm, R.id.ll_zhyaq);
    }

    @Override
    protected void initData() {
    }

    @Override
    public void onResume() {
        super.onResume();
        if (SpUtil.getInstance().getBooleanValue(SpUtil.IS_LOGIN)) {
            getUserInfo();
            getMsgList();
        } else {
            tvUserName.setText(R.string.no_login);
            tvPhone.setText(R.string.login_hint);
        }
        prefetch();
    }

    @Override
    public void onStart(Call call) {
        super.onStart(call);
        hideDialog();
    }

    /**
     * 网易易盾-预取号
     */
    private void prefetch() {
        login.setUnifyUiConfig(QuickLoginUiConfig.getUiConfig(getActivity(), new QuickLoginUiConfig.OnListener() {
            @Override
            public void otherLogin() {
                login.quitActivity();
                startActivity(LoginActivity.class);
            }

            @Override
            public void onClickEventListener(int viewType) {

            }
        }));
        login.prefetchMobileNumber(new QuickLoginPreMobileListener() {
            @Override
            public void onGetMobileNumberSuccess(String YDToken, final String mobileNumber) {
                // 注:2.0.0及以后版本，直接在该回调中调用取号接口onePass即可
                LogUtils.d(TAG, "onGetMobileNumberSuccess: " + YDToken + ":" + mobileNumber);
                errMsg = "";
            }

            @Override
            public void onGetMobileNumberError(String YDToken, final String msg) {
                LogUtils.d(TAG, "onGetMobileNumberError: " + YDToken + ":" + msg);
                errMsg = msg;
            }
        });
    }

    /**
     * 网易易盾自动登录
     *
     * @param token
     * @param acessToken
     */
    private void autoLogin(String token, String acessToken) {
        EasyHttp.post(this)
                .api(new AutoLoginApi(token, acessToken))
                .request(new HttpCallback<HttpData<AutoLoginBean>>(this) {
                    @Override
                    public void onSucceed(HttpData<AutoLoginBean> result) {
                        super.onSucceed(result);
                        if (result.getCode() == 200) {
                            autoLoginInfo(result.getData().getPhone());
                        } else {
                            toast(result.getMessage());
                        }
                    }
                });
    }

    /**
     * 通过手机号获取用户信息
     */
    private void autoLoginInfo(String phone) {
        EasyHttp.get(this)
                .api("api/sists/autoLogininfo?mobile=" + phone)
                .request(new HttpCallback<HttpData<LoginBean>>(this) {
                    @Override
                    public void onSucceed(HttpData<LoginBean> result) {
                        super.onSucceed(result);
                        if (result.getCode() == 1) {
                            SpUtil.getInstance().setBooleanValue(SpUtil.IS_LOGIN, true);
                            SpUtil.getInstance().setStringValue(SpUtil.UID, result.getData().getId() + "");
                            AppConfig.setLoginBean(result.getData());
                            startActivity(MainActivity.class);
                            // 进行内存优化，销毁除登录页之外的所有界面
                            ActivityStackManager.getInstance().finishAllActivities(MainActivity.class);
                        } else {
                            toast(result.getMessage());
                        }
                    }
                });
    }

    @SingleClick
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_setting://设置
                startActivity(SettingsActivity.class);
                break;
            case R.id.rl_message://通知
                if (AppConfig.isLogin()) {
                    startActivity(MsgCenterActivity.class);
                } else {
                    startActivity(LoginActivity.class);
                }
                break;
            case R.id.ll_user_info://个人信息
                if (SpUtil.getInstance().getBooleanValue(SpUtil.IS_LOGIN)) {
                    startActivity(UserInfoActivity.class);
                } else {
                    if (TextUtils.isEmpty(errMsg)) {
                        login.onePass(new QuickLoginTokenListener() {
                            @Override
                            public void onGetTokenSuccess(String s, String s1) {
                                LogUtils.d(TAG, "onGetTokenSuccess: " + s + ":" + s1);
                                login.quitActivity();
                                autoLogin(s, s1);
                            }

                            @Override
                            public void onGetTokenError(String s, String s1) {
                                LogUtils.d(TAG, "onGetTokenSuccess: " + s + ":" + s1);
                                startActivity(LoginActivity.class);
                                toast(s1);
                            }
                        });
                    } else {
                        startActivity(LoginActivity.class);
                    }
                }
                break;
            case R.id.ll_gywm://关于我们
                startActivity(AboutMineActivity.class);
                break;
            case R.id.ll_zhyaq://账号与安全
                if (AppConfig.isLogin()) {
                    startActivity(AccountSafetyActivity.class);
                } else {
                    startActivity(LoginActivity.class);
                }
                break;
        }
    }

    /**
     * 获取用户信息
     */
    private void getUserInfo() {
        EasyHttp.get(this)
                .api("api/sists/getUserinfo?uid=" + AppConfig.getLoginBean().getId() + "&client_id=" + util.MD5.md5Str(Identifier.getSN()))
                .request(new HttpCallback<HttpData<LoginBean>>(this) {
                    @Override
                    public void onSucceed(HttpData<LoginBean> result) {
                        super.onSucceed(result);
                        if (result.getCode() == 1) {
                            AppConfig.setLoginBean(result.getData());
                            tvUserName.setText(AppConfig.getLoginBean().getNickname());
                            tvPhone.setText(AppConfig.getLoginBean().getMobile());
                            Glide.with(getActivity()).load(AppConfig.getLoginBean().getAvatar())
                                    .error(R.mipmap.icon_contact_avatar_default)
                                    .placeholder(R.mipmap.icon_contact_avatar_default)
                                    .into(imgAvatar);
                            tv_level.setVisibility(View.VISIBLE);
                            switch (result.getData().getLevel_status()) {
                                case 0:
                                    tv_level.setText(R.string.bronze);
                                    break;
                                case 1:
                                    tv_level.setText(R.string.silver);
                                    break;
                                case 2:
                                    tv_level.setText(R.string.gold);
                                    break;
                                case 3:
                                    tv_level.setText(R.string.platinum);
                                    break;
                                case 4:
                                    tv_level.setText(R.string.diamond);
                                    break;
                            }
                        }
                    }
                });
    }


    /**
     * 获取消息列表
     */
    private void getMsgList() {
        EasyHttp.get(this)
                .api("api/User/getMessage?code=0")
                .request(new HttpCallback<HttpData<List<MsgBean>>>(this) {
                    @Override
                    public void onSucceed(HttpData<List<MsgBean>> result) {
                        super.onSucceed(result);
                        if (result.getCode() == 0) {
                            if (result.getData() != null && result.getData().size() > 0) {
                                tvNum.setText(result.getData().size() + "");
                            } else {
                                tvNum.setVisibility(View.GONE);
                            }
                        } else {
                            tvNum.setVisibility(View.GONE);
                        }
                    }
                });
    }

    private boolean isPreVerifyDone = true;
    private boolean devMode = false;

    /**
     * 一键登录
     */
    private void mobLogin(VerifyResult data) {
        EasyHttp.get(this)
                .api("auth.php?token=" + data.getToken() + "&optoken=" + data.getOpToken() + "&operator=" + data.getOperator())
                .request(new HttpCallback<HttpData<LoginBean>>(this) {
                    @Override
                    public void onSucceed(HttpData<LoginBean> result) {
                        super.onSucceed(result);
                        if (result.getCode() == 1) {
                            SpUtil.getInstance().setBooleanValue(SpUtil.IS_LOGIN, true);
                            SpUtil.getInstance().setStringValue(SpUtil.UID, result.getData().getId() + "");
                            AppConfig.setLoginBean(result.getData());
                            getUserInfo();
                        } else {
                            toast(result.getMessage());
                        }
                    }
                });
    }
}