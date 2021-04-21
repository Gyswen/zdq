package com.sameal.dd.ui.ddActivity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hjq.base.BaseDialog;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.hjq.widget.view.CircleImageView;
import com.sameal.dd.R;
import com.sameal.dd.aop.SingleClick;
import com.sameal.dd.common.MyActivity;
import com.sameal.dd.helper.Identifier;
import com.sameal.dd.http.model.HttpData;
import com.sameal.dd.http.request.UpLoadApi;
import com.sameal.dd.http.response.LoginBean;
import com.sameal.dd.http.response.UpLoadBean;
import com.sameal.dd.other.AppConfig;
import com.sameal.dd.ui.activity.ImageSelectActivity;
import com.sameal.dd.ui.dialog.InputDialog;
import com.sameal.dd.ui.dialog.SelectDialog;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhangj
 * @date 2020/12/17 22:00
 * desc 个人资料
 */
public class UserInfoActivity extends MyActivity {

    private CircleImageView imgAvatar;
    private TextView tvNickName;
    private TextView tvPhone;
    private TextView tvSex;
    private TextView tvBio;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_info;
    }

    @Override
    protected void initView() {
        imgAvatar = findViewById(R.id.img_avatar);
        tvNickName = findViewById(R.id.tv_nick_name);
        tvPhone = findViewById(R.id.tv_phone);
        tvSex = findViewById(R.id.tv_sex);
        tvBio = findViewById(R.id.tv_bio);
        setOnClickListener(R.id.ll_avatar, R.id.ll_nick_name, R.id.ll_sex,R.id.ll_bio);
    }

    @Override
    protected void initData() {
        tvNickName.setText(AppConfig.getLoginBean().getNickname());
        tvPhone.setText(AppConfig.getLoginBean().getMobile());
        if (!TextUtils.isEmpty(AppConfig.getLoginBean().getBio())){
            tvBio.setText(AppConfig.getLoginBean().getBio());
        }
        Glide.with(getActivity()).load(AppConfig.getLoginBean().getAvatar())
                .error(R.mipmap.icon_contact_avatar_default)
                .placeholder(R.mipmap.icon_contact_avatar_default)
                .into(imgAvatar);
        tvSex.setText(AppConfig.getLoginBean().getGender() == 0 ? "男" : "女");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0){
            getUserInfo();
        }
    }

    @SingleClick
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_avatar:
                ImageSelectActivity.start(this, data -> {
                    upload(data.get(0));
                });
                break;
            case R.id.ll_nick_name:
                new InputDialog.Builder(this)
                        .setHint("请输入昵称")
                        .setListener(new InputDialog.OnListener() {
                            @Override
                            public void onConfirm(BaseDialog dialog, String content) {
                                upNickName(content);
                            }
                        }).show();
                break;
            case R.id.ll_sex:
                // 菜单弹窗
                new SelectDialog.Builder(this)
                        .setSingleSelect()
                        .setList(Arrays.asList("男", "女"))
                        .setListener(new SelectDialog.OnListener<String>() {
                            @Override
                            public void onSelected(BaseDialog dialog, HashMap<Integer, String> data) {
                                for (Map.Entry<Integer, String> entry : data.entrySet()) {
                                    tvSex.setText(entry.getValue());
                                    upGender(entry.getKey());
                                }
                            }
                        }).show();
                break;
            case R.id.ll_bio:
                Intent intent = new Intent(this,UserInfoBioActivity.class);
                startActivityForResult(intent,0);
                break;
            default:
                break;
        }
    }

    /**
     * 修改性别
     */
    private void upGender(int gender) {
        EasyHttp.get(this)
                .api("api/User/updateGender?uid=" + AppConfig.getLoginBean().getId() + "&gender=" + gender)
                .request(new HttpCallback<HttpData<String>>(this) {
                    @Override
                    public void onSucceed(HttpData<String> result) {
                        super.onSucceed(result);
                        if (result.getCode() == 1) {
                            getUserInfo();
                        }
                        toast(result.getMessage());
                    }
                });
    }

    /**
     * 修改昵称
     */
    private void upNickName(String nickName) {
        EasyHttp.get(this)
                .api("api/User/updateNickname?uid=" + AppConfig.getLoginBean().getId() + "&nickname=" + nickName)
                .request(new HttpCallback<HttpData<String>>(this) {
                    @Override
                    public void onSucceed(HttpData<String> result) {
                        super.onSucceed(result);
                        if (result.getCode() == 1) {
                            getUserInfo();
                        }
                        toast(result.getMessage());
                    }
                });
    }

    /**
     * 上传图片
     */
    private void upload(String path) {
        EasyHttp.post(this)
                .api(new UpLoadApi()
                        .setFile(new File(path)))
                .request(new HttpCallback<HttpData<UpLoadBean>>(this) {
                    @Override
                    public void onSucceed(HttpData<UpLoadBean> result) {
                        super.onSucceed(result);
                        if (result.getCode() == 1) {
                            updateAvatar(result.getData().getUrl());
                        }
                    }
                });
    }

    /**
     * 修改昵称
     */
    private void updateAvatar(String url) {
        EasyHttp.get(this)
                .api("api/User/updateAvatar?uid=" + AppConfig.getLoginBean().getId() + "&avatar=" + url)
                .request(new HttpCallback<HttpData<String>>(this) {
                    @Override
                    public void onSucceed(HttpData<String> result) {
                        super.onSucceed(result);
                        if (result.getCode() == 1) {
                            getUserInfo();
                        }
                        toast(result.getMessage());
                    }
                });
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
                            tvNickName.setText(AppConfig.getLoginBean().getNickname());
                            tvPhone.setText(AppConfig.getLoginBean().getMobile());
                            if (!TextUtils.isEmpty(AppConfig.getLoginBean().getBio())){
                                tvBio.setText(AppConfig.getLoginBean().getBio());
                            }
                            Glide.with(getActivity()).load(AppConfig.getLoginBean().getAvatar())
                                    .error(R.mipmap.icon_contact_avatar_default)
                                    .placeholder(R.mipmap.icon_contact_avatar_default)
                                    .into(imgAvatar);
                        }
                    }
                });
    }
}