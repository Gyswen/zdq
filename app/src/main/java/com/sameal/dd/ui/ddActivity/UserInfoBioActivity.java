package com.sameal.dd.ui.ddActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.hjq.base.action.BundleAction;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.sameal.dd.R;
import com.sameal.dd.common.MyActivity;
import com.sameal.dd.http.model.HttpData;
import com.sameal.dd.other.AppConfig;

/**
 * 修改个性签名
 */

public class UserInfoBioActivity extends MyActivity {

    private android.widget.EditText etFeedbackBio;
    private android.widget.TextView tvTextNum;
    private androidx.appcompat.widget.AppCompatButton btnSubmit;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_info_bio;
    }

    @Override
    protected void initView() {
        etFeedbackBio = (EditText) findViewById(R.id.et_feedback_bio);
        tvTextNum = (TextView) findViewById(R.id.tv_text_num);
        btnSubmit = (AppCompatButton) findViewById(R.id.btn_submit);

        setOnClickListener(R.id.btn_submit);
    }

    @Override
    protected void initData() {
        if (!TextUtils.isEmpty(AppConfig.getLoginBean().getBio())){
            etFeedbackBio.setText(AppConfig.getLoginBean().getBio());
            tvTextNum.setText(AppConfig.getLoginBean().getBio().length() + "/60");
        }
        etFeedbackBio.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                tvTextNum.setText(etFeedbackBio.getText().length() + "/60");
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_submit:
                if (TextUtils.isEmpty(etFeedbackBio.getText().toString())) {
                    etFeedbackBio.setText(R.string.bio);
                    return;
                }
                feedbackBio();
                break;
        }
    }

    /**
     * 修改个性签名
     */
    private void feedbackBio() {
        EasyHttp.get(this)
                .api("api/User/updateBio?uid=" + AppConfig.getLoginBean().getId() + "&bio=" + etFeedbackBio.getText().toString())
                .request(new HttpCallback<HttpData<String>>(this) {
                    @Override
                    public void onSucceed(HttpData<String> result) {
                        super.onSucceed(result);
                        if (result.getCode() == 1) {
                            setResult(0);
                            finish();
                        }
                        toast(result.getMessage());
                    }
                });
    }
}