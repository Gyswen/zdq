package com.sameal.dd.ui.ddActivity;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.hjq.base.BaseFragmentAdapter;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.hjq.permissions.OnPermission;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.hjq.toast.ToastUtils;
import com.mob.MobSDK;
import com.mob.OperationCallback;
import com.sameal.dd.BuildConfig;
import com.sameal.dd.R;
import com.sameal.dd.common.MyActivity;
import com.sameal.dd.common.MyFragment;
import com.sameal.dd.helper.ActivityStackManager;
import com.sameal.dd.helper.DoubleClickHelper;
import com.sameal.dd.helper.LogUtils;
import com.sameal.dd.helper.SpUtil;
import com.sameal.dd.http.model.HttpData;
import com.sameal.dd.http.response.AppStoreBean;
import com.sameal.dd.http.response.MessageEvent;
import com.sameal.dd.http.response.VersionBean;
import com.sameal.dd.other.AppConfig;
import com.sameal.dd.other.KeyboardWatcher;
import com.sameal.dd.ui.ddFragment.CommunityFragment;
import com.sameal.dd.ui.ddFragment.HomeFragment;
import com.sameal.dd.ui.ddFragment.MeFragment;
import com.sameal.dd.ui.ddFragment.ScheduleFragment;
import com.sameal.dd.ui.ddFragment.SportsFragment;
import com.sameal.dd.ui.ddReview.ReviewCommunityFragment;
import com.sameal.dd.ui.ddReview.ReviewHomeFragment;
import com.sameal.dd.ui.ddReview.ReviewMeFragment;
import com.sameal.dd.ui.ddReview.ReviewVideoFragment;
import com.sameal.dd.ui.dialog.UpdateDialog;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import java.util.List;

/**
 * @author zhangj
 * @date 2020/12/16 21:12
 * desc 主页
 */
public class MainActivity extends MyActivity
        implements KeyboardWatcher.SoftKeyboardStateListener,
        BottomNavigationView.OnNavigationItemSelectedListener {

    private ViewPager mViewPager;
    private BottomNavigationView mBottomNavigationView;

    private BaseFragmentAdapter<MyFragment> mPagerAdapter;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMoonEvent(MessageEvent messageEvent) {
        if (messageEvent.getType() == 1) {
            AppConfig.setIsClick(true);
            AppConfig.setClickPosi(messageEvent.getPosi());
            mViewPager.setCurrentItem(2);
            mBottomNavigationView.setSelectedItemId(R.id.home_jj);
//            EventBus.getDefault().post(messageEvent);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        mViewPager = findViewById(R.id.vp_home_pager);
        mBottomNavigationView = findViewById(R.id.bv_home_navigation);

        // 不使用图标默认变色
        mBottomNavigationView.setItemIconTintList(null);
        mBottomNavigationView.setOnNavigationItemSelectedListener(this);

        KeyboardWatcher.with(this)
                .setListener(this);

        //一键登录
        MobSDK.submitPolicyGrantResult(false, new OperationCallback<Void>() {
            @Override
            public void onComplete(Void aVoid) {
                LogUtils.d(TAG, "onComplete: " + aVoid);
            }

            @Override
            public void onFailure(Throwable throwable) {
                LogUtils.d(TAG, "onFailure: " + throwable.toString());
            }
        });
        EventBus.getDefault().register(this);
    }

    @Override
    protected void initData() {
        mPagerAdapter = new BaseFragmentAdapter<>(this);
        AppStore();

        setPermission();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_tj://推荐
                mViewPager.setCurrentItem(0);
                return true;
            case R.id.home_sq://社区
                mViewPager.setCurrentItem(1);
                return true;
            case R.id.home_jj://竞技
                mViewPager.setCurrentItem(2);
                return true;
            case R.id.home_sc://赛程
                mViewPager.setCurrentItem(3);
                return true;
            case R.id.home_me://我的
                mViewPager.setCurrentItem(4);
                return true;
            default:
                break;
        }
        return false;
    }

    @Override
    public void onSoftKeyboardOpened(int keyboardHeight) {
        mBottomNavigationView.setVisibility(View.GONE);
    }

    @Override
    public void onSoftKeyboardClosed() {
        mBottomNavigationView.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 回调当前 Fragment 的 onKeyDown 方法
        if (mPagerAdapter.getShowFragment().onKeyDown(keyCode, event)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onBackPressed() {
        if (DoubleClickHelper.isOnDoubleClick()) {
            // 移动到上一个任务栈，避免侧滑引起的不良反应
            moveTaskToBack(false);
            postDelayed(() -> {

                // 进行内存优化，销毁掉所有的界面
                ActivityStackManager.getInstance().finishAllActivities();
                // 销毁进程（注意：调用此 API 可能导致当前 Activity onDestroy 方法无法正常回调）
                // System.exit(0);

            }, 300);
        } else {
            toast(R.string.home_exit_hint);
        }
    }

    @Override
    protected void onDestroy() {
        mViewPager.setAdapter(null);
        mBottomNavigationView.setOnNavigationItemSelectedListener(null);
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public boolean isSwipeEnable() {
        return false;
    }

    /**
     * 检查更新
     */
    private void checkUpdate() {
        EasyHttp.get(this)
                .api("api/User/checkUpdate")
                .request(new HttpCallback<HttpData<VersionBean>>(this) {
                    @Override
                    public void onSucceed(HttpData<VersionBean> result) {
                        super.onSucceed(result);
                        if (result.getCode() == 1) {
                            String[] mvers = AppConfig.getVersionName().split("[.]");
                            String[] yvers = result.getData().getAndroid_version().split("[.]");

                            if (Integer.valueOf(yvers[0]).intValue() > Integer.valueOf(mvers[0]).intValue()) {
                                starUpdateDialog(result);
                            } else if (Integer.valueOf(yvers[0]).intValue() == Integer.valueOf(mvers[0]).intValue()) {
                                if (Integer.valueOf(yvers[1]).intValue() > Integer.valueOf(mvers[1]).intValue()) {
                                    starUpdateDialog(result);
                                } else if (Integer.valueOf(yvers[1]).intValue() == Integer.valueOf(mvers[1]).intValue()) {
                                    if (Integer.valueOf(yvers[2]).intValue() > Integer.valueOf(mvers[2]).intValue()) {
                                        starUpdateDialog(result);
                                    }
                                }
                            }
                        }
                    }
                });
    }

    private void starUpdateDialog(HttpData<VersionBean> result) {
        new UpdateDialog.Builder(MainActivity.this)
                .setDownloadUrl(result.getData().getAndroid_href())
                .setVersionName(result.getData().getAndroid_version())
                .setUpdateLog(result.getData().getAndroid_content())
                .setForceUpdate(result.getData().getAndroid_isforce().equals("1"))
                .show();
    }

    /**
     * 检查是否审核
     */
    private void AppStore(){
        EasyHttp.get(this)
                .api("api/sists/checkAppStore?app_store="+ BuildConfig.FLAVOR + "&android_version="+ AppConfig.getVersionName())
                .request(new HttpCallback<HttpData<AppStoreBean>>(this) {
                    @Override
                    public void onSucceed(HttpData<AppStoreBean> result) {
                        super.onSucceed(result);
                        if (result.getCode() == 1){
                            if (result.getData().getIs_check() == 1){
                                setReviewPagerAdapter();
                            }else {
                                setmPagerAdapter();
                            }
                        }else {
                            setmPagerAdapter();
                        }
                    }

                    @Override
                    public void onFail(Exception e) {
                        super.onFail(e);
                        setmPagerAdapter();
                    }
                });
    }

    private void setReviewPagerAdapter(){
        SpUtil.getInstance().setBooleanValue(SpUtil.IS_CHECK,true);
        mPagerAdapter.addFragment(ReviewHomeFragment.newInstance());
        mPagerAdapter.addFragment(ReviewCommunityFragment.newInstance());
        mPagerAdapter.addFragment(ReviewVideoFragment.newInstance());
        mPagerAdapter.addFragment(ScheduleFragment.newInstance());
        mPagerAdapter.addFragment(ReviewMeFragment.newInstance());
        mBottomNavigationView.getMenu().getItem(1).setVisible(false);
        mBottomNavigationView.getMenu()
                .getItem(2)
                .setIcon(R.drawable.home_video_selector)
                .setTitle(R.string.video);
        // 设置成懒加载模式
        mPagerAdapter.setLazyMode(true);
        mViewPager.setAdapter(mPagerAdapter);
    }

    private void setmPagerAdapter(){
        SpUtil.getInstance().setBooleanValue(SpUtil.IS_CHECK,false);
        mPagerAdapter.addFragment(HomeFragment.newInstance());
        mPagerAdapter.addFragment(CommunityFragment.newInstance());
        mPagerAdapter.addFragment(SportsFragment.newInstance());
        mPagerAdapter.addFragment(ScheduleFragment.newInstance());
        mPagerAdapter.addFragment(MeFragment.newInstance());
        // 设置成懒加载模式
        mPagerAdapter.setLazyMode(true);
        mViewPager.setAdapter(mPagerAdapter);
        checkUpdate();
    }

    private void setPermission() {
        XXPermissions.with(this)
                // 申请安装包权限
                .permission(Permission.READ_EXTERNAL_STORAGE)
                .permission(Permission.READ_PHONE_STATE)
                // 申请通知栏权限
                .permission(Permission.ACCESS_COARSE_LOCATION)
                .permission(Permission.ACCESS_FINE_LOCATION)
                // 申请系统设置权限
                .permission(Permission.WRITE_EXTERNAL_STORAGE)
                .permission(Permission.CAMERA)
                // 申请单个权限
//                .permission(Permission.WRITE_SETTINGS)
                // 申请多个权限
//                .permission(Permission.Group.CALENDAR)
                .request(new OnPermission() {

                    @Override
                    public void hasPermission(List<String> granted, boolean all) {
                        if (all) {

                        } else {
                            ToastUtils.show("获取权限成功，部分权限未正常授予");
                        }
                    }

                    @Override
                    public void noPermission(List<String> denied, boolean never) {
                        if (never) {
                            ToastUtils.show("被永久拒绝授权，请手动授予存储和拍照权限");
                            // 如果是被永久拒绝就跳转到应用权限系统设置页面
                            XXPermissions.startPermissionActivity(MainActivity.this);
                        } else {
                            ToastUtils.show("获取存储和拍照权限失败");
                        }
                    }
                });
    }
}