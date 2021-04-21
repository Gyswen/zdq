package com.sameal.dd.ui.ddActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
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

import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.hjq.widget.view.CountdownView;
import com.sameal.dd.R;
import com.sameal.dd.common.MyActivity;
import com.sameal.dd.helper.ActivityStackManager;
import com.sameal.dd.helper.LogUtils;
import com.sameal.dd.helper.MsgTimerTask;
import com.sameal.dd.helper.SpUtil;
import com.sameal.dd.http.model.HttpData;
import com.sameal.dd.http.response.LoginBean;
import com.sameal.dd.http.response.RegisterBean;
import com.sameal.dd.http.response.SendSMSBean;
import com.sameal.dd.other.AppConfig;
import com.sameal.dd.other.CodeUtils;
import com.sameal.dd.other.IntentKey;

import java.util.List;

/**
 *
 * 注册界面
 *
 */

public class RegisterActivity extends MyActivity {

    private android.widget.ImageButton imgBack;
    private android.widget.EditText etPhone;
    private android.widget.EditText etPassword;
    private android.widget.EditText etPassword2;
    private android.widget.EditText inviteCode;
    private android.widget.EditText etVerificationCode;
    private com.hjq.widget.view.CountdownView cvCode;
    private android.widget.TextView tvRetuLogin;
    private androidx.appcompat.widget.AppCompatButton btnLoginCommit;
    private android.widget.TextView tvBottom;
    private EditText etCaptcha;
    private android.widget.ImageView img;

    CodeUtils codeUtils;
    private static SendSMSBean sendSMSBean = new SendSMSBean();
    private String openId;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    protected void initView() {
        imgBack = (ImageButton) findViewById(R.id.img_back);
        etPhone = (EditText) findViewById(R.id.et_phone);
        etPassword = (EditText) findViewById(R.id.et_password);
        etPassword2 = (EditText) findViewById(R.id.et_password2);
        inviteCode = (EditText) findViewById(R.id.invite_code);
        etVerificationCode = (EditText) findViewById(R.id.et_verification_code);
        cvCode = (CountdownView) findViewById(R.id.cv_code);
        tvRetuLogin = (TextView) findViewById(R.id.tv_retu_login);
        btnLoginCommit = (AppCompatButton) findViewById(R.id.btn_login_commit);
        tvBottom = (TextView) findViewById(R.id.tv_bottom);
        etCaptcha = (EditText) findViewById(R.id.et_captcha);
        img = (ImageView) findViewById(R.id.img);
        setOnClickListener(R.id.cv_code,R.id.tv_retu_login,R.id.btn_login_commit,R.id.img);
        codeUtils = CodeUtils.getInstance();
    }

    @Override
    protected void initData() {
        openId = getString(IntentKey.ID);
        Bitmap bitmap = codeUtils.createBitmap();
        img.setImageBitmap(bitmap);
        setCbText();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cv_code://获取验证码
                if (TextUtils.isEmpty(etPhone.getText().toString())) {
                    toast("请输入手机号");
                    return;
                } else if (!etCaptcha.getText().toString().toLowerCase().equals(codeUtils.getCode().toLowerCase())) {
                    toast("请输入正确的图形验证码");
                    return;
                } else if (!AppConfig.isMobileNO(etPhone.getText().toString())) {
                    toast("请输入正确的手机号");
                    return;
                }
                getSMS();
                break;
            case R.id.tv_retu_login://返回登录
                finish();
                break;
            case R.id.img:
                Bitmap bitmap = codeUtils.createBitmap();
                img.setImageBitmap(bitmap);
                break;
            case R.id.btn_login_commit://注册
                if (TextUtils.isEmpty(etPhone.getText().toString())) {
                    toast("请输入手机号");
                    return;
                } else if (!AppConfig.isMobileNO(etPhone.getText().toString())) {
                    toast("请输入正确的手机号");
                    return;
                } else if (TextUtils.isEmpty(etPassword.getText().toString())) {
                    toast("请输入密码");
                    return;
                } else if (etPassword.getText().toString().length() < 6 || etPassword.getText().toString().length() > 20) {
                    toast("请设置长度6到20的密码");
                    return;
                } else if (TextUtils.isEmpty(etPassword2.getText().toString())) {
                    toast("请输入再次确认密码");
                    return;
                } else if (!etPassword2.getText().toString().equals(etPassword.getText().toString())) {
                    toast("再次确认密码不匹配");
                    return;
                } else if (TextUtils.isEmpty(inviteCode.getText().toString())) {
                    toast("请输入邀请码");
                    return;
                } else if (!etCaptcha.getText().toString().toLowerCase().equals(codeUtils.getCode().toLowerCase())) {
                    toast("请输入正确的图形验证码");
                    return;
                } else if (TextUtils.isEmpty(etVerificationCode.getText().toString())) {
                    toast("请输入验证码");
                    return;
                } else if (TextUtils.isEmpty(sendSMSBean.getCode())) {
                    toast("请获取验证码");
                    return;
                } else if (!sendSMSBean.getCode().equals(util.MD5.md5Str(etVerificationCode.getText().toString()))){
                    toast("验证码错误");
                    return;
                }
                Register();
                break;
        }
    }

    /**
     * 获取短信验证码（登录）
     */
    private void getSMS() {
        EasyHttp.get(this)
                .api("api/Sists/sendSMS?phone="+ etPhone.getText().toString() +"&is_reg=1")
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
     * 注册
     */
    private void Register() {
        EasyHttp.get(this)
                .api("api/sists/register?mobile="+etPhone.getText().toString()+"&password="+etPassword.getText().toString()+
                        "&invite_code="+inviteCode.getText().toString()+ (!TextUtils.isEmpty(openId) ? "&openid="+openId : "&openid="))
                .request(new HttpCallback<HttpData<List<RegisterBean>>>(this) {
                    @Override
                    public void onSucceed(HttpData<List<RegisterBean>> result) {
                        super.onSucceed(result);
                        if (result.getCode() == 1){
                            autoLoginInfo(etPhone.getText().toString());
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