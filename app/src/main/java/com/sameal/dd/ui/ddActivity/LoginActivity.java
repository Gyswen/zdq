package com.sameal.dd.ui.ddActivity;

import android.content.Intent;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import com.google.gson.Gson;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.hjq.widget.view.ScaleImageView;
import com.mob.secverify.datatype.VerifyResult;
import com.netease.nis.quicklogin.QuickLogin;
import com.netease.nis.quicklogin.listener.QuickLoginPreMobileListener;
import com.netease.nis.quicklogin.listener.QuickLoginTokenListener;
import com.sameal.dd.R;
import com.sameal.dd.aop.SingleClick;
import com.sameal.dd.common.MyActivity;
import com.sameal.dd.common.MyApplication;
import com.sameal.dd.helper.ActivityStackManager;
import com.sameal.dd.helper.Identifier;
import com.sameal.dd.helper.InputTextHelper;
import com.sameal.dd.helper.JsonConfigParser;
import com.sameal.dd.helper.LogUtils;
import com.sameal.dd.helper.QuickLoginUiConfig;
import com.sameal.dd.helper.SpUtil;
import com.sameal.dd.http.model.HttpData;
import com.sameal.dd.http.request.AutoLoginApi;
import com.sameal.dd.http.response.AutoLoginBean;
import com.sameal.dd.http.response.CheckRegBean;
import com.sameal.dd.http.response.LoginBean;
import com.sameal.dd.other.AppConfig;
import com.sameal.dd.other.IntentKey;
import com.sameal.dd.umeng.Platform;
import com.sameal.dd.umeng.UmengClient;
import com.sameal.dd.umeng.UmengLogin;

/**
 * @author zhangj
 * @date 2020/12/20 22:00
 * desc 登录
 */
public class LoginActivity extends MyActivity implements View.OnClickListener {

    AppCompatButton btnYjdl;
    EditText etPhone;
    EditText etPassword;
    TextView tvMsgCodeLogin;
    TextView tvForgetPassword;
    AppCompatButton btnLoginCommit;
    ScaleImageView ivLoginWechat;
    TextView tvBottom;

    private String errMsg;
    QuickLogin login = QuickLogin.getInstance(MyApplication.sInstance, "7f2ed00edafe4a9a83d8d394903bc8e2");

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        btnYjdl = (AppCompatButton) findViewById(R.id.btn_yjdl);
        etPhone = (EditText) findViewById(R.id.et_phone);
        etPassword = (EditText) findViewById(R.id.et_password);
        tvMsgCodeLogin = (TextView) findViewById(R.id.tv_msg_code_login);
        tvForgetPassword = (TextView) findViewById(R.id.tv_forget_password);
        btnLoginCommit = (AppCompatButton) findViewById(R.id.btn_login_commit);
        ivLoginWechat = (ScaleImageView) findViewById(R.id.iv_login_wechat);
        tvBottom = (TextView) findViewById(R.id.tv_bottom);
        setOnClickListener(R.id.img_back, R.id.btn_yjdl, R.id.tv_msg_code_login, R.id.tv_forget_password, R.id.btn_login_commit, R.id.iv_login_wechat);
    }

    @Override
    protected void initData() {
        InputTextHelper.with(this)
                .addView(etPhone)
                .addView(etPassword)
                .setMain(btnLoginCommit)
                .build();
        setCbText();
//        preVerify();
    }

    @Override
    protected void onResume() {
        super.onResume();
        prefetch();
    }

    JsonConfigParser configParser = new JsonConfigParser();

    private void prefetch() {
        login.setUnifyUiConfig(QuickLoginUiConfig.getUiConfig(getActivity(), new QuickLoginUiConfig.OnListener() {
            @Override
            public void otherLogin() {
                login.quitActivity();
            }

            @Override
            public void onClickEventListener(int viewType) {
                //viewType为1时表示隐私协议，2表示复选框，3表示左上角返回按钮，4表示登录按钮
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
                toast(msg);
                errMsg = msg;
            }
        });
    }

    @SingleClick
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.btn_yjdl:
//                verify();
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
                            toast(s1);
                        }
                    });
                }
                break;
            case R.id.tv_msg_code_login:
                startActivity(MsgCodeActivity.class);
                break;
            case R.id.tv_forget_password:
                startActivity(RegisterActivity.class);
                break;
            case R.id.btn_login_commit:
                if (!AppConfig.isMobileNO(etPhone.getText().toString())) {
                    toast("请输入正确的手机号");
                    return;
                } else if (TextUtils.isEmpty(etPassword.getText().toString()) || etPassword.getText().toString().length() < 6) {
                    toast("请输入6-20位密码");
                    return;
                }
                login();
                break;
            case R.id.iv_login_wechat:
                UmengClient.login(this, Platform.WECHAT, new UmengLogin.OnLoginListener() {
                    @Override
                    public void onSucceed(Platform platform, UmengLogin.LoginData data) {
                        LogUtils.d("EasyHttp", "onSucceed:" + new Gson().toJson(data));
                        wxLogin(data);
                    }

                    @Override
                    public void onError(Platform platform, Throwable t) {
                        LogUtils.d("EasyHttp", "onError:" + t.toString());
                        toast("登录失败：" + t.toString());
                    }

                    @Override
                    public void onCancel(Platform platform) {
                        toast("取消登录");
                    }
                });
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + view.getId());
        }
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
                            checkReg(result.getData().getPhone());
                        } else {
                            toast(result.getMessage());
                        }
                    }
                });
    }

    /**
     * 检查账号是否注册
     */
    private void checkReg(String phone) {
        EasyHttp.get(this)
                .api("api/sists/checkReg?mobile="+phone)
                .request(new HttpCallback<HttpData<CheckRegBean>>(this) {
                    @Override
                    public void onSucceed(HttpData<CheckRegBean> result) {
                        super.onSucceed(result);
                        if (result.getCode() == 1){
                            if (result.getData().getIs_reg() == 1){
                                autoLoginInfo(phone);
                            } else {
                                toast(result.getMessage());
                                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                                startActivity(intent);
                            }
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
                .api("api/sists/autoLogininfo?mobile=" + phone + "&client_id=" + util.MD5.md5Str(Identifier.getSN()))
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
                            startActivity(RegisterActivity.class);
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 友盟登录回调
        UmengClient.onActivityResult(this, requestCode, resultCode, data);
    }

    /**
     * 微信登陆
     */
    private void wxLogin(UmengLogin.LoginData data) {
        EasyHttp.get(this)
                .api("api/sists/wxLogininfo?openid=" + data.getOpenid() + "&client_id=" + util.MD5.md5Str(Identifier.getSN()))
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
                            toast("微信未绑定账号");
                            Intent intent = new Intent(LoginActivity.this, WXBindingAcitvity.class);
                            intent.putExtra(IntentKey.ID, data.getOpenid());
                            startActivity(intent);
                        }
                    }
                });
    }

    /**
     * 登录
     */
    private void login() {
        EasyHttp.get(this)
                .api("api/sists/autoLogin?mobile=" + etPhone.getText().toString() + "&password=" + etPassword.getText().toString() + "&client_id=" + util.MD5.md5Str(Identifier.getSN()))
                .request(new HttpCallback<HttpData<LoginBean>>(this) {
                    @Override
                    public void onSucceed(HttpData<LoginBean> result) {
                        super.onSucceed(result);
                        if (result.getCode() == 1) {
                            SpUtil.getInstance().setBooleanValue(SpUtil.IS_LOGIN, true);
                            SpUtil.getInstance().setStringValue(SpUtil.UID, result.getData().getId() + "");
                            AppConfig.setLoginBean(result.getData());
                            SpUtil.getInstance().setStringValue(SpUtil.ACCOUNT, etPhone.getText().toString());
                            SpUtil.getInstance().setStringValue(SpUtil.PASSWORD, etPassword.getText().toString());
                            startActivity(MainActivity.class);
                            // 进行内存优化，销毁除登录页之外的所有界面
                            ActivityStackManager.getInstance().finishAllActivities(MainActivity.class);
                        } else if (result.getMessage().equals("密码错误")){
                            toast(result.getMessage());
                        } else {
                            toast(result.getMessage());
                            startActivity(RegisterActivity.class);
                        }
                    }

                    @Override
                    public void onFail(Exception e) {
                        super.onFail(e);
                        toast(e.toString());
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
                            startActivity(MainActivity.class);
                            // 进行内存优化，销毁除登录页之外的所有界面
                            ActivityStackManager.getInstance().finishAllActivities(MainActivity.class);
                        } else {
                            toast(result.getMessage());
                        }
                    }
                });
    }

    private void setCbText() {
        String string = "点击登录表示您已阅读并同意《用户协议》和《隐私政策》";
        String key1 = "《用户协议》";
        String key2 = "《隐私政策》";
        int index1 = string.indexOf(key1);
        int index2 = string.indexOf(key2);

        //需要显示的字串
        SpannableString spannedString = new SpannableString(string);
        //设置点击字体颜色
        ForegroundColorSpan colorSpan1 = new ForegroundColorSpan(getResources().getColor(R.color.colorConfirm));
        spannedString.setSpan(colorSpan1, index1, index1 + key1.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        ForegroundColorSpan colorSpan2 = new ForegroundColorSpan(getResources().getColor(R.color.colorConfirm));
        spannedString.setSpan(colorSpan2, index2, index2 + key2.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        //设置点击字体大小
        AbsoluteSizeSpan sizeSpan1 = new AbsoluteSizeSpan(13, true);
        spannedString.setSpan(sizeSpan1, index1, index1 + key1.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        AbsoluteSizeSpan sizeSpan2 = new AbsoluteSizeSpan(13, true);
        spannedString.setSpan(sizeSpan2, index2, index2 + key2.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        //设置点击事件
        ClickableSpan clickableSpan1 = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                getCustomerDetail("8");
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                //点击事件去掉下划线
                ds.setUnderlineText(false);
            }
        };
        spannedString.setSpan(clickableSpan1, index1, index1 + key1.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

        ClickableSpan clickableSpan2 = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                getCustomerDetail("9");
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                //点击事件去掉下划线
                ds.setUnderlineText(false);
            }
        };
        spannedString.setSpan(clickableSpan2, index2, index2 + key2.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        tvBottom.setText(spannedString);
        tvBottom.setMovementMethod(LinkMovementMethod.getInstance());
    }


    /**
     * 获取问题详情
     */
    private void getCustomerDetail(String id) {
        EasyHttp.get(this)
                .api("api/User/customerDetail?id=" + id)
                .request(new HttpCallback<HttpData<CustomerDetailActivity.CustomerDetailBean>>(this) {
                    @Override
                    public void onSucceed(HttpData<CustomerDetailActivity.CustomerDetailBean> result) {
                        super.onSucceed(result);
                        if (result.getCode() == 1) {
                            Intent intent = new Intent(getActivity(), WebActivity.class);
                            intent.putExtra(IntentKey.TITLE, result.getData().getProblem().getTitle());
                            intent.putExtra(IntentKey.ADDRESS, result.getData().getProblem().getContent());
                            startActivity(intent);
                        }
                    }
                });
    }
}