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
import android.widget.ImageView;
import android.widget.TextView;

import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.hjq.widget.view.CountdownView;
import com.sameal.dd.R;
import com.sameal.dd.common.MyActivity;
import com.sameal.dd.helper.ActivityStackManager;
import com.sameal.dd.helper.MsgTimerTask;
import com.sameal.dd.helper.SpUtil;
import com.sameal.dd.http.model.HttpData;
import com.sameal.dd.http.response.CheckRegBean;
import com.sameal.dd.http.response.LoginBean;
import com.sameal.dd.http.response.SendSMSBean;
import com.sameal.dd.other.AppConfig;
import com.sameal.dd.other.CodeUtils;
import com.sameal.dd.other.IntentKey;

public class WXBindingAcitvity extends MyActivity {

    private android.widget.EditText etPhone;
    private android.widget.EditText etVerificationCode;
    private com.hjq.widget.view.CountdownView cvCode;
    private android.widget.TextView tvForgetPassword;
    private androidx.appcompat.widget.AppCompatButton btnLoginCommit;
    private android.widget.TextView tvBottom;

    CodeUtils codeUtils;
    private String openId;
    private static SendSMSBean sendSMSBean = new SendSMSBean();
    private EditText etCaptcha;
    private ImageView img;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_w_x_binding_acitvity;
    }

    @Override
    protected void initView() {
        etPhone = (EditText) findViewById(R.id.et_phone);
        etVerificationCode = (EditText) findViewById(R.id.et_verification_code);
        cvCode = (CountdownView) findViewById(R.id.cv_code);
        tvForgetPassword = (TextView) findViewById(R.id.tv_forget_password);
        btnLoginCommit = (AppCompatButton) findViewById(R.id.btn_login_commit);
        tvBottom = (TextView) findViewById(R.id.tv_bottom);
        etCaptcha = (EditText) findViewById(R.id.et_captcha);
        img = (ImageView) findViewById(R.id.img);
        setOnClickListener(R.id.btn_login_commit,R.id.cv_code,R.id.tv_forget_password,R.id.img);

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
        switch (v.getId()){
            case R.id.img:
                Bitmap bitmap = codeUtils.createBitmap();
                img.setImageBitmap(bitmap);
                break;
            case R.id.cv_code:
                if (TextUtils.isEmpty(etPhone.getText().toString())) {
                    toast("??????????????????");
                    return;
                } else if (!etCaptcha.getText().toString().toLowerCase().equals(codeUtils.getCode().toLowerCase())) {
                    toast("?????????????????????????????????");
                    return;
                } else if (!AppConfig.isMobileNO(etPhone.getText().toString())) {
                    toast("???????????????????????????");
                    return;
                }
                checkReg();
                break;
            case R.id.btn_login_commit:
                if (TextUtils.isEmpty(etPhone.getText().toString())) {
                    toast("?????????????????????");
                    return;
                } else if (!etCaptcha.getText().toString().toLowerCase().equals(codeUtils.getCode().toLowerCase())) {
                    toast("?????????????????????????????????");
                    return;
                } else if (TextUtils.isEmpty(etVerificationCode.getText().toString())) {
                    toast("??????????????????");
                    return;
                } else if (TextUtils.isEmpty(sendSMSBean.getCode())) {
                    toast("?????????????????????");
                    return;
                } else if (!sendSMSBean.getCode().equals(util.MD5.md5Str(etVerificationCode.getText().toString()))){
                    toast("??????????????????");
                    return;
                }
                wxLoginBing();
                break;
            case R.id.tv_forget_password://??????
                Intent intent = new Intent(WXBindingAcitvity.this, RegisterActivity.class);
                intent.putExtra(IntentKey.ID, openId);
                startActivity(intent);
                break;
        }
    }

    /**
     * ?????????????????????????????????
     */
    private void getSMS() {
        EasyHttp.get(this)
                .api("api/Sists/sendSMS?phone="+ etPhone.getText().toString() +"&is_reg=0")
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
     * ????????????????????????
     */
    private void checkReg() {
        EasyHttp.get(this)
                .api("api/sists/checkReg?mobile="+etPhone.getText().toString())
                .request(new HttpCallback<HttpData<CheckRegBean>>(this) {
                    @Override
                    public void onSucceed(HttpData<CheckRegBean> result) {
                        super.onSucceed(result);
                        if (result.getCode() == 1){
                            if (result.getData().getIs_reg() == 1){
                                getSMS();
                            } else {
                                toast(result.getMessage());
                                Intent intent = new Intent(WXBindingAcitvity.this, RegisterActivity.class);
                                intent.putExtra(IntentKey.ID, openId);
                                startActivity(intent);
                            }
                        } else {
                            toast(result.getMessage());
                        }
                    }
                });
    }

    /**
     * ??????????????????
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

    /**
     * ?????????????????????????????????
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
                            // ????????????????????????????????????????????????????????????
                            ActivityStackManager.getInstance().finishAllActivities(MainActivity.class);
                        } else {
                            toast(result.getMessage());
                        }
                    }
                });
    }

    private void setCbText() {
        String string = "??????????????????????????????????????????????????????????????????????????????";
        String key1 = "??????????????????";
        String key2 = "??????????????????";
        int index1 = string.indexOf(key1);
        int index2 = string.indexOf(key2);

        //?????????????????????
        SpannableString spannedString = new SpannableString(string);
        //????????????????????????
        ForegroundColorSpan colorSpan1 = new ForegroundColorSpan(getResources().getColor(R.color.colorConfirm));
        spannedString.setSpan(colorSpan1, index1, index1 + key1.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        ForegroundColorSpan colorSpan2 = new ForegroundColorSpan(getResources().getColor(R.color.colorConfirm));
        spannedString.setSpan(colorSpan2, index2, index2 + key2.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        //????????????????????????
        AbsoluteSizeSpan sizeSpan1 = new AbsoluteSizeSpan(13, true);
        spannedString.setSpan(sizeSpan1, index1, index1 + key1.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        AbsoluteSizeSpan sizeSpan2 = new AbsoluteSizeSpan(13, true);
        spannedString.setSpan(sizeSpan2, index2, index2 + key2.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        //??????????????????
        ClickableSpan clickableSpan1 = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                getCustomerDetail("8");
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                //???????????????????????????
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
                //???????????????????????????
                ds.setUnderlineText(false);
            }
        };
        spannedString.setSpan(clickableSpan2, index2, index2 + key2.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        tvBottom.setText(spannedString);
        tvBottom.setMovementMethod(LinkMovementMethod.getInstance());
    }


    /**
     * ??????????????????
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