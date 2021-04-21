package com.sameal.dd.ui.ddActivity;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hjq.widget.view.SwitchButton;
import com.sameal.dd.R;
import com.sameal.dd.aop.SingleClick;
import com.sameal.dd.common.MyActivity;
import com.sameal.dd.helper.ActivityStackManager;
import com.sameal.dd.helper.CacheDataManager;
import com.sameal.dd.helper.SpUtil;
import com.sameal.dd.other.AppConfig;
import com.sameal.dd.ui.ddActivity.LoginActivity;

/**
 * @author zhangj
 * @date 2020/12/17 21:04
 * desc 设置
 */
public class SettingsActivity extends MyActivity {

    private TextView tvCacheSize;
    private SwitchButton sbBackPlay;
    private SwitchButton sbHoverPlay;
    private Button btnLoginExit;

    @Override
    protected int getLayoutId() {
        return R.layout.settings_activity;
    }

    @Override
    protected void initView() {
        tvCacheSize = findViewById(R.id.tv_cache_size);
        sbBackPlay = findViewById(R.id.sb_background_play);
        sbHoverPlay = findViewById(R.id.sb_hover_play);
        btnLoginExit = findViewById(R.id.btn_login_exit);
        setOnClickListener(R.id.tv_cache_size, R.id.btn_login_exit);
    }

    @Override
    protected void initData() {
        // 获取应用缓存大小
        tvCacheSize.setText(CacheDataManager.getTotalCacheSize(this));
        if (AppConfig.isLogin()) {
            btnLoginExit.setVisibility(View.VISIBLE);
        } else {
            btnLoginExit.setVisibility(View.GONE);
        }
    }

    @SingleClick
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cache_size:
                // 清除内存缓存（必须在主线程）
                Glide.get(getActivity()).clearMemory();
                new Thread(() -> {
                    CacheDataManager.clearAllCache(this);
                    // 清除本地缓存（必须在子线程）
                    Glide.get(getActivity()).clearDiskCache();
                    post(() -> {
                        // 重新获取应用缓存大小
                        tvCacheSize.setText(CacheDataManager.getTotalCacheSize(getActivity()));
                    });
                }).start();
                break;
            case R.id.btn_login_exit:
                SpUtil.getInstance().setBooleanValue(SpUtil.IS_LOGIN, false);
                startActivity(MainActivity.class);
                // 进行内存优化，销毁除登录页之外的所有界面
                ActivityStackManager.getInstance().finishAllActivities(MainActivity.class);
                break;
            default:
                break;
        }
    }
}