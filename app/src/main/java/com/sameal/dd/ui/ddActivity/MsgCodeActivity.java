package com.sameal.dd.ui.ddActivity;

import android.content.Intent;
import android.graphics.Bitmap;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import com.google.gson.Gson;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.hjq.widget.view.CountdownView;
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
import com.sameal.dd.helper.JsonConfigParser;
import com.sameal.dd.helper.LogUtils;
import com.sameal.dd.helper.MsgTimerTask;
import com.sameal.dd.helper.QuickLoginUiConfig;
import com.sameal.dd.helper.SpUtil;
import com.sameal.dd.http.model.HttpData;
import com.sameal.dd.http.request.AutoLoginApi;
import com.sameal.dd.http.response.AutoLoginBean;
import com.sameal.dd.http.response.CheckRegBean;
import com.sameal.dd.http.response.LoginBean;
import com.sameal.dd.http.response.SendSMSBean;
import com.sameal.dd.other.AppConfig;
import com.sameal.dd.other.CodeUtils;
import com.sameal.dd.other.IntentKey;
import com.sameal.dd.umeng.Platform;
import com.sameal.dd.umeng.UmengClient;
import com.sameal.dd.umeng.UmengLogin;

/**
 * @author zhangj
 * @date 2020/12/20 22:57
 * desc 短信验证码验证
 */
public class MsgCodeActivity extends MyActivity implements View.OnClickListener {

    ImageButton imgBack;
    AppCompatButton btnYjdl;
    EditText etPhone;
    EditText etCaptcha;
    ImageView img;
    EditText etVerificationCode;
    CountdownView cvCode;
    TextView tvMsgCodeLogin;
    AppCompatButton btnLoginCommit;
    ScaleImageView ivLoginWechat;
    TextView tvBottom;

    CodeUtils codeUtils;
    private String openId;
    private String errMsg;
    private static SendSMSBean sendSMSBean = new SendSMSBean();
    QuickLogin login = QuickLogin.getInstance(MyApplication.sInstance, "7f2ed00edafe4a9a83d8d394903bc8e2");

    @Override
    protected int getLayoutId() {
        return R.layout.activity_msg_vode;
    }

    @Override
    protected void initView() {
        imgBack = (ImageButton) findViewById(R.id.img_back);
        btnYjdl = (AppCompatButton) findViewById(R.id.btn_yjdl);
        etPhone = (EditText) findViewById(R.id.et_phone);
        etCaptcha = (EditText) findViewById(R.id.et_captcha);
        img = (ImageView) findViewById(R.id.img);
        etVerificationCode = (EditText) findViewById(R.id.et_verification_code);
        cvCode = (CountdownView) findViewById(R.id.cv_code);
        tvMsgCodeLogin = (TextView) findViewById(R.id.tv_msg_code_login);
        btnLoginCommit = (AppCompatButton) findViewById(R.id.btn_login_commit);
        ivLoginWechat = (ScaleImageView) findViewById(R.id.iv_login_wechat);
        tvBottom = (TextView) findViewById(R.id.tv_bottom);
        setOnClickListener(R.id.img, R.id.img_back, R.id.btn_yjdl, R.id.cv_code, R.id.tv_msg_code_login, R.id.btn_login_commit, R.id.iv_login_wechat, R.id.tv_forget_password);
    }

    @Override
    protected void initData() {
        codeUtils = CodeUtils.getInstance();
        openId = getString(IntentKey.ID);
        Bitmap bitmap = codeUtils.createBitmap();
        img.setImageBitmap(bitmap);
        setCbText();
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
                startActivity(MsgCodeActivity.class);
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

    @SingleClick
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
            case R.id.img:
                Bitmap bitmap = codeUtils.createBitmap();
                img.setImageBitmap(bitmap);
                break;
            case R.id.tv_forget_password:
                startActivity(RegisterActivity.class);
                break;
            case R.id.cv_code:
                if (TextUtils.isEmpty(etPhone.getText().toString())) {
                    toast("请输入手机号");
                    return;
                } else if (!etCaptcha.getText().toString().toLowerCase().equals(codeUtils.getCode().toLowerCase())) {
                    toast("请输入正确的图形验证码");
                    return;
                }  else if (!AppConfig.isMobileNO(etPhone.getText().toString())) {
                    toast("请输入正确的手机号");
                    return;
                }
                checkRegSMS(etPhone.getText().toString());
                break;
            case R.id.tv_msg_code_login:
                finish();
                break;
            case R.id.btn_login_commit:
                if (!etCaptcha.getText().toString().toLowerCase().equals(codeUtils.getCode().toLowerCase())) {
                    toast("请输入正确的图形验证码");
                    return;
                } else if (TextUtils.isEmpty(etVerificationCode.getText().toString())) {
                    toast("请输入验证码");
                    return;
                } else if (TextUtils.isEmpty(sendSMSBean.getCode())) {
                    toast("请先获取验证码");
                    return;
                } else if (!sendSMSBean.getCode().equals(util.MD5.md5Str(etVerificationCode.getText().toString()))) {
                    toast("验证码错误");
                    return;
                }
                autoLoginInfo(etPhone.getText().toString());
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
        }
    }

    /**
     * 获取短信验证码（登录）
     */
    private void getSMS() {
        EasyHttp.get(this)
                .api("/api/Sists/sendSMS?phone="+ etPhone.getText().toString() +"&is_reg=0")
                .request(new HttpCallback<HttpData<SendSMSBean>>(this) {
                    @Override
                    public void onSucceed(HttpData<SendSMSBean> result) {
                        super.onSucceed(result);
                        if (result.getCode() == 1) {
                            sendSMSBean = result.getData();
                            MsgTimerTask.getInstance(cvCode);
                        } else {
                            toast(result.getMessage());
                        }
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
                                Intent intent = new Intent(MsgCodeActivity.this, RegisterActivity.class);
                                startActivity(intent);
                            }
                        } else {
                            toast(result.getMessage());
                        }
                    }
                });
   }

    /**
     * 检查账号是否注册，获取验证码
     */
    private void checkRegSMS(String phone) {
        EasyHttp.get(this)
                .api("api/sists/checkReg?mobile="+phone)
                .request(new HttpCallback<HttpData<CheckRegBean>>(this) {
                    @Override
                    public void onSucceed(HttpData<CheckRegBean> result) {
                        super.onSucceed(result);
                        if (result.getCode() == 1){
                            if (result.getData().getIs_reg() == 1){
                                getSMS();
                            } else {
                                toast(result.getMessage());
                                startActivity(RegisterActivity.class);
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
                            Intent intent = new Intent(MsgCodeActivity.this, WXBindingAcitvity.class);
                            intent.putExtra(IntentKey.ID, data.getOpenid());
                            startActivity(intent);
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
     * 账号绑定微信
     */
    private void wxLoginBing() {
        EasyHttp.get(this)
                .api("api/sists/wxLoginBing?mobile=" + etPhone.getText().toString() + "&openid=" + openId)
                .request(new HttpCallback<HttpData<Object>>(this) {
                    @Override
                    public void onSucceed(HttpData<Object> result) {
                        super.onSucceed(result);
                        if (result.getCode() == 1) {
                            autoLoginInfo(etPhone.getText().toString());
                        } else {
                            toast(result.getMessage());
                        }
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
                            finish();
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