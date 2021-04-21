package com.sameal.dd.helper;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.hjq.base.BaseDialog;
import com.netease.nis.quicklogin.helper.UnifyUiConfig;
import com.netease.nis.quicklogin.listener.ClickEventListener;
import com.netease.nis.quicklogin.utils.LoginUiHelper;
import com.sameal.dd.R;

/**
 * Created by hzhuqi on 2019/12/31
 */
public class QuickLoginUiConfig {
    public static UnifyUiConfig getUiConfig(final Context context, OnListener onListener) {
        LayoutInflater inflater = LayoutInflater.from(context);
        RelativeLayout otherLoginRel = (RelativeLayout) inflater.inflate(R.layout.custom_other_login, null);
        RelativeLayout.LayoutParams layoutParamsOther = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParamsOther.setMargins(0, 0, 0, Utils.dip2px(context, 130));
        layoutParamsOther.addRule(RelativeLayout.CENTER_HORIZONTAL);
        layoutParamsOther.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        otherLoginRel.setLayoutParams(layoutParamsOther);
        Button btnOther = (Button) otherLoginRel.findViewById(R.id.title);
        btnOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onListener.otherLogin();
            }
        });
        int X_OFFSET = 0;
        int BOTTOM_OFFSET = 0;
        UnifyUiConfig uiConfig = new UnifyUiConfig.Builder()
                // 状态栏
                .setStatusBarColor(Color.TRANSPARENT)
                .setStatusBarDarkColor(true)
                // 设置导航栏
                .setNavigationTitle("一键登录/注册")
                .setNavigationTitleColor(Color.BLACK)
                .setNavigationBackgroundColor(R.color.colorLine)
                .setNavigationIcon("yd_checkbox_checked2")
                .setNavigationBackIconWidth(15)
                .setNavigationBackIconHeight(15)
                .setNavigationIcon("back_black")
                .setHideNavigation(true)
        // 设置logo
                .setLogoIconName("ic_launcher")
                .setLogoWidth(70)
                .setLogoHeight(70)
                .setLogoXOffset(X_OFFSET)
                .setLogoTopYOffset(50)
                .setHideLogo(false)
                //手机掩码
                .setMaskNumberColor(Color.RED)
                .setMaskNumberSize(15)
                .setMaskNumberXOffset(X_OFFSET)
                .setMaskNumberTopYOffset(130)
                .setMaskNumberBottomYOffset(BOTTOM_OFFSET)
                // 认证品牌
                .setSloganSize(15)
                .setSloganColor(R.color.nice_blue)
                .setSloganXOffset(X_OFFSET)
                .setSloganTopYOffset(200)
                .setSloganBottomYOffset(BOTTOM_OFFSET)
                // 登录按钮
                .setLoginBtnText("一键登录")
                .setLoginBtnTextColor(R.color.nice_blue)
                .setLoginBtnBackgroundRes("bg_follow_blue")
                .setLoginBtnWidth(200)
                .setLoginBtnHeight(45)
                .setLoginBtnTextSize(15)
                .setLoginBtnXOffset(X_OFFSET)
                .setLoginBtnTopYOffset(250)
                .setLoginBtnBottomYOffset(BOTTOM_OFFSET)
                // 隐私栏
                .setPrivacyTextStart("")
                .setProtocolText("")
                .setProtocolLink("")
                .setProtocol2Text("")
                .setProtocol2Link("")
                .setPrivacyTextEnd("")
                .setPrivacyTextColor(Color.RED)
                .setPrivacyProtocolColor(Color.GREEN)
//                .setHidePrivacyCheckBox(false)
//                .setPrivacyXOffset(X_OFFSET)
                .setPrivacyState(true)
                .setPrivacySize(12)
//                .setPrivacyTopYOffset(510)
                .setPrivacyBottomYOffset(20)
                .setPrivacyTextGravityCenter(true)
                .setCheckedImageName("yd_checkbox_checked2")
                .setUnCheckedImageName("yd_checkbox_unchecked2")
                // 协议详情页导航栏
                .setProtocolPageNavTitle("易盾一键登录SDK服务条款")
                .setProtocolPageNavBackIcon("yd_checkbox_checked")
                .setProtocolPageNavColor(R.color.color_ff4545)
                .setClickEventListener(new ClickEventListener() {
                    @Override
                    public void onClick(int i, int i1) {
                        onListener.onClickEventListener(i);
                    }
                })

//                .setBackgroundImage("bg1")
                // 自定义控件
                .addCustomView(otherLoginRel, "relative", UnifyUiConfig.POSITION_IN_BODY, null)
//                .addCustomView(closeBtn, "close_btn", UnifyUiConfig.POSITION_IN_TITLE_BAR, new LoginUiHelper.CustomViewListener() {
//                    @Override
//                    public void onClick(Context context, View view) {
//
//                    }
//                })
                .build(context);
        return uiConfig;
    }

    public interface OnListener {

        /**
         * 点击确定时回调
         */
        void otherLogin();
        void onClickEventListener(int viewType);

    }

}
