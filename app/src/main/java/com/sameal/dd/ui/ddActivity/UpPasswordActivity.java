package com.sameal.dd.ui.ddActivity;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.widget.AppCompatButton;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.hjq.widget.view.CountdownView;
import com.sameal.dd.R;
import com.sameal.dd.aop.SingleClick;
import com.sameal.dd.common.MyActivity;
import com.sameal.dd.helper.MsgTimerTask;
import com.sameal.dd.http.model.HttpData;
import com.sameal.dd.http.response.SendSMSBean;
import com.sameal.dd.other.AppConfig;
import com.sameal.dd.other.CodeUtils;

/**
 * @author zhangj
 * @date 2020/12/19 13:15
 * desc 修改登录密码
 */
public class UpPasswordActivity extends MyActivity {

    TextView etPhone;
    EditText etVerificationCode;
    CountdownView cvCode;
    EditText etCaptcha;
    ImageView img;
    EditText etNewPassword;
    EditText etConfirmNewPassword;
    AppCompatButton btnCommonConfirm;

    CodeUtils codeUtils;
    private static SendSMSBean sendSMSBean = new SendSMSBean();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_up_password;
    }

    @Override
    protected void initView() {
        etPhone = (EditText) findViewById(R.id.et_phone);
        etCaptcha = (EditText) findViewById(R.id.et_captcha);
        img = (ImageView) findViewById(R.id.img);
        etVerificationCode = (EditText) findViewById(R.id.et_verification_code);
        cvCode = (CountdownView) findViewById(R.id.cv_code);
        etNewPassword = (EditText) findViewById(R.id.et_new_password);
        etConfirmNewPassword = (EditText) findViewById(R.id.et_confirm_new_password);
        btnCommonConfirm = (AppCompatButton) findViewById(R.id.btn_common_confirm);
        setOnClickListener(R.id.cv_code, R.id.btn_common_confirm, R.id.img);
    }

    @Override
    protected void initData() {
        codeUtils = CodeUtils.getInstance();
        Bitmap bitmap = codeUtils.createBitmap();
        img.setImageBitmap(bitmap);
        if (AppConfig.isLogin()) {
            etPhone.setText(AppConfig.getLoginBean().getMobile());
            etPhone.setEnabled(false);
            setTitle("修改密码");
        } else {
            etPhone.setEnabled(true);
            setTitle("忘记密码");
        }
    }

    @SingleClick
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cv_code:
                if (TextUtils.isEmpty(etPhone.getText().toString())) {
                    toast("请输入手机号");
                    return;
                } else if (!etCaptcha.getText().toString().toLowerCase().equals(codeUtils.getCode().toLowerCase())) {
                    toast("请输入正确的图形验证码");
                    return;
                }
                getSMS();
                break;
            case R.id.img:
                Bitmap bitmap = codeUtils.createBitmap();
                img.setImageBitmap(bitmap);
                break;
            case R.id.btn_common_confirm:
                if (TextUtils.isEmpty(etPhone.getText().toString())) {
                    toast("请输入手机号");
                    return;
                } else if (TextUtils.isEmpty(etVerificationCode.getText().toString().toLowerCase())) {
                    toast("请输入验证码");
                    return;
                } else if (TextUtils.isEmpty(etNewPassword.getText().toString()) || etNewPassword.getText().toString().length() < 6) {
                    toast("请输入6-20位新密码");
                    return;
                } else if (TextUtils.isEmpty(etConfirmNewPassword.getText().toString())) {
                    toast("请再次输入新密码");
                    return;
                } else if (!etNewPassword.getText().toString().equals(etConfirmNewPassword.getText().toString())) {
                    toast("两次密码输入不一致");
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
                upPassword();
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
     * 修改密码
     */
    private void upPassword() {
        EasyHttp.get(this)
                .api("api/User/updatePassword?mobile=" + etPhone.getText().toString() + "&password=" + etNewPassword.getText().toString())
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