package com.sameal.dd.ui.ddActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.netease.nis.alivedetected.ActionType;
import com.netease.nis.alivedetected.AliveDetector;
import com.netease.nis.alivedetected.DetectedListener;
import com.netease.nis.alivedetected.NISCameraPreview;
import com.sameal.dd.R;
import com.sameal.dd.common.MyActivity;
import com.sameal.dd.helper.ActivityStackManager;
import com.sameal.dd.helper.Identifier;
import com.sameal.dd.helper.LogUtils;
import com.sameal.dd.helper.SpUtil;
import com.sameal.dd.helper.Util;
import com.sameal.dd.http.model.HttpData;
import com.sameal.dd.http.request.CopyApi;
import com.sameal.dd.http.request.OcrZApi;
import com.sameal.dd.http.response.LoginBean;
import com.sameal.dd.http.response.OcrInfoBean;
import com.sameal.dd.other.AppConfig;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import pl.droidsonroids.gif.GifImageView;

public class AliveActivity extends MyActivity {

    private FrameLayout cameraFrameLayout;
    private NISCameraPreview mCameraPreview;
    private RelativeLayout viewTipBackground;
    private TextView tvErrorTip, tvTip, tvStep1, tvStep2, tvStep3, tvStep4;
    private GifImageView gifAction;
    private ViewStub vsStep2, vsStep3, vsStep4;

    private RelativeLayout layout1;
    private LinearLayout layout2;
    private ImageView imageResult;
    private TextView tvTips;
    private TextView tvResult;
    private TextView butTv;

    private AliveDetector mAliveDetector;
    private Map<String, String> stateTipMap = new HashMap<>();

    private static final String KEY_STRAIGHT_AHEAD = "straight_ahead";
    private static final String KEY_OPEN_MOUTH = "open_mouth";
    private static final String KEY_TURN_HEAD_TO_LEFT = "turn_head_to_left";
    private static final String KEY_TURN_HEAD_TO_RIGHT = "turn_head_to_right";
    private static final String KEY_BLINK_EYES = "blink_eyes";
    private static final String FRONT_ERROR_TIP = "???????????????????????????????????????";
    private static final String TIP_STRAIGHT_AHEAD = "?????????????????????\n" +
            "?????????????????????";//"???????????????????????????????????????";
    private static final String TIP_OPEN_MOUTH = "?????????";
    private static final String TIP_TURN_HEAD_TO_LEFT = "???????????????";
    private static final String TIP_TURN_HEAD_TO_RIGHT = "???????????????";
    private static final String TIP_BLINK_EYES = "?????????";

    private static String BUSINESS_ID = "dade89ce1b574f1dab832b57eb2637e8";
    private int mCurrentCheckStepIndex = 0;
    private ActionType mCurrentActionType = ActionType.ACTION_STRAIGHT_AHEAD;
    private ActionType[] mActions;
    private boolean isUsedCustomStateTip = true; // ???????????????????????????????????????
    private boolean isOpenVoice = true;
    private MediaPlayer mPlayer = new MediaPlayer();

    private String etRealName,etIdCard,token;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_alive;
    }

    @Override
    protected void initView() {

        cameraFrameLayout = (FrameLayout) findViewById(R.id.cameraFrameLayout);
        mCameraPreview = (NISCameraPreview) findViewById(R.id.surface_view);
        viewTipBackground = (RelativeLayout) findViewById(R.id.view_tip_background);
        tvErrorTip = (TextView) findViewById(R.id.tv_error_tip);
        tvTip = (TextView) findViewById(R.id.tv_tip);
        gifAction = (GifImageView) findViewById(R.id.gif_action);
        tvStep1 = (TextView) findViewById(R.id.tv_step_1);
        vsStep2 = (ViewStub) findViewById(R.id.vs_step_2);
        vsStep3 = (ViewStub) findViewById(R.id.vs_step_3);
        vsStep4 = (ViewStub) findViewById(R.id.vs_step_4);
        layout1 = (RelativeLayout) findViewById(R.id.layout1);
        layout2 = (LinearLayout) findViewById(R.id.layout2);
        imageResult = (ImageView) findViewById(R.id.image_result);
        tvTips = (TextView) findViewById(R.id.tv_tips);
        tvResult = (TextView) findViewById(R.id.tv_result);
        butTv = (TextView) findViewById(R.id.but_tv);

        setOnClickListener(R.id.but_tv);
    }

    @Override
    protected void initData() {
        etRealName = getString(SpUtil.NAME);
        etIdCard = getString(SpUtil.TYPE);
        Util.setWindowBrightness(this, WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_FULL);
        stateTipMap.put(KEY_STRAIGHT_AHEAD, TIP_STRAIGHT_AHEAD);
        stateTipMap.put(KEY_TURN_HEAD_TO_LEFT, TIP_TURN_HEAD_TO_LEFT);
        stateTipMap.put(KEY_TURN_HEAD_TO_RIGHT, TIP_TURN_HEAD_TO_RIGHT);
        stateTipMap.put(KEY_OPEN_MOUTH, TIP_OPEN_MOUTH);
        stateTipMap.put(KEY_BLINK_EYES, TIP_BLINK_EYES);

        mAliveDetector = AliveDetector.getInstance();
        mAliveDetector.init(this, mCameraPreview, BUSINESS_ID);
        mAliveDetector.setDetectedListener(new DetectedListener() {
            @Override
            public void onReady(boolean b) {
                if (b) {
                    LogUtils.d(TAG, "?????????????????????????????????");
                } else {
                    //  mAliveDetector.startDetect();
                    LogUtils.e(TAG, "?????????????????????????????????");
                }
            }

            @Override
            public void onActionCommands(ActionType[] actionTypes) {
                mActions = actionTypes;
                String commands = buildActionCommand(actionTypes);
                showIndicatorOnUiThread(commands.length() - 1);
//                showToast("???????????????????????????:" + commands);
                LogUtils.d(TAG, "???????????????????????????:" + commands);
            }

            @Override
            public void onStateTipChanged(ActionType actionType, String s) {
                LogUtils.d(TAG, "actionType:" + actionType.getActionTip() + " stateTip:" + actionType + " CurrentCheckStepIndex:" + mCurrentCheckStepIndex);
                if (actionType == ActionType.ACTION_PASSED && actionType.getActionID() != mCurrentActionType.getActionID()) {
                    mCurrentCheckStepIndex++;
                    if (mCurrentCheckStepIndex < mActions.length) {
                        updateIndicatorOnUiThread(mCurrentCheckStepIndex);
                        if (isOpenVoice) {
                            playSounds(mCurrentCheckStepIndex);
                        }
                        mCurrentActionType = mActions[mCurrentCheckStepIndex];
                    }
                }
                if (isUsedCustomStateTip) {
                    switch (actionType) {
                        case ACTION_STRAIGHT_AHEAD:
                            setTipText("", false);
                            break;
                        case ACTION_OPEN_MOUTH:
                            setTipText(stateTipMap.get(KEY_OPEN_MOUTH), false);
                            break;
                        case ACTION_TURN_HEAD_TO_LEFT:
                            setTipText(stateTipMap.get(KEY_TURN_HEAD_TO_LEFT), false);
                            break;
                        case ACTION_TURN_HEAD_TO_RIGHT:
                            setTipText(stateTipMap.get(KEY_TURN_HEAD_TO_RIGHT), false);
                            break;
                        case ACTION_BLINK_EYES:
                            setTipText(stateTipMap.get(KEY_BLINK_EYES), false);
                            break;
                        case ACTION_ERROR:
                            setTipText(s, true);
                            break;
//                        case ACTION_PASSED:
//                            setTipText(stateTip);
//                            break;
                    }
                } else {
                    if (actionType == ActionType.ACTION_ERROR) {
                        setTipText(s, true);
                    } else {
                        setTipText(s, false);
                    }
                }
            }

            @Override
            public void onPassed(boolean b, String s) {
                if (b) {
                    LogUtils.d(TAG, "??????????????????,token is:" + s);
                } else {
                    LogUtils.e(TAG, "?????????????????????,token is:" + s);
                }
                ocrCheck(s);
            }

            @Override
            public void onError(int i, String s, String s1) {
                LogUtils.e(TAG, "listener [onError] ??????????????????,??????:" + s + " token:" + s1);
                jump2FailureActivity(s);
            }

            @Override
            public void onOverTime() {
                Util.showDialog(AliveActivity.this, "????????????", "?????????????????????????????????",
                        "??????", "????????????", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                resetIndicator();
                                resetGif();
                                mAliveDetector.startDetect();
                            }
                        }, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(AliveActivity.this, VerifiedActivity.class);
                                startActivity(intent);
                            }
                        });
            }
        });
        mAliveDetector.setSensitivity(AliveDetector.SENSITIVITY_NORMAL);
        mAliveDetector.setTimeOut(1000 * 30);
        mAliveDetector.startDetect();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.but_tv:
                if (butTv.getText().toString().equals(getString(R.string.confirm_resun))) {
                    ActivityStackManager.getInstance().destoryActivity(VerifiedActivity.class);
                    finish();
                } else if (butTv.getText().toString().equals(getString(R.string.again))) {
                    finish();
                }
                break;
        }
    }

    @Override
    public void onLeftClick(View v) {
        if (butTv.getText().toString().equals(getString(R.string.confirm_resun))) {
            ActivityStackManager.getInstance().destoryActivity(VerifiedActivity.class);
            finish();
        }
        super.onLeftClick(v);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Util.setWindowBrightness(this, WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE);
        if (mAliveDetector != null) {
            mAliveDetector.stopDetect();
        }
    }

    private void resetIndicator() {
        mCurrentCheckStepIndex = 0;
        mCurrentActionType = ActionType.ACTION_STRAIGHT_AHEAD;
        tvStep1.setText("1");
        tvStep2.setText("");
        tvStep3.setText("");
        tvStep4.setText("");
        setTextViewUnFocus(tvStep2);
        setTextViewUnFocus(tvStep3);
        setTextViewUnFocus(tvStep4);
    }

    private void resetGif() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                gifAction.setImageResource(R.mipmap.pic_front_2x);
            }
        });
    }

    private void showIndicatorOnUiThread(final int commandLength) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showViewStub(commandLength);
                showIndicator(commandLength);
            }
        });
    }

    private void setLayouThreadt(final boolean is) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setLayout(is);
            }
        });
    }

    private void setLayout(boolean is) {
        layout1.setVisibility(View.GONE);
        layout2.setVisibility(View.VISIBLE);
        if (is) {
            imageResult.setImageResource(R.mipmap.alive_result_su);
            tvTips.setText(R.string.blessing);
            tvResult.setText(R.string.identity_success);
            butTv.setText(R.string.confirm_resun);
        } else {
            imageResult.setImageResource(R.mipmap.alive_result_el);
            tvTips.setText(R.string.apologize);
            tvResult.setText(R.string.identity_fail);
            butTv.setText(R.string.again);
        }

    }

    private void setTipText(final String tip, final boolean isErrorType) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isErrorType) {
                    if (FRONT_ERROR_TIP.equals(tip)) {
                        tvErrorTip.setText(TIP_STRAIGHT_AHEAD);
                    } else {
                        tvErrorTip.setText(tip);
                    }
                    viewTipBackground.setVisibility(View.VISIBLE);

                } else {
                    viewTipBackground.setVisibility(View.INVISIBLE);
                    tvTip.setText(tip);
                    tvErrorTip.setText("");
                }
            }
        });
    }

    public static String buildActionCommand(ActionType[] actionCommands) {
        StringBuilder commands = new StringBuilder();
        for (ActionType actionType : actionCommands) {
            commands.append(actionType.getActionID());
        }
        return commands == null ? "" : commands.toString();
    }

    private void showViewStub(int commandLength) {
        switch (commandLength) {
            case 2:
                vsStep2.setVisibility(View.VISIBLE);
                tvStep2 = findViewById(R.id.tv_step_2);
                break;
            case 3:
                vsStep2.setVisibility(View.VISIBLE);
                tvStep2 = findViewById(R.id.tv_step_2);
                vsStep3.setVisibility(View.VISIBLE);
                tvStep3 = findViewById(R.id.tv_step_3);
                break;
            case 4:
                vsStep2.setVisibility(View.VISIBLE);
                tvStep2 = findViewById(R.id.tv_step_2);
                vsStep3.setVisibility(View.VISIBLE);
                tvStep3 = findViewById(R.id.tv_step_3);
                vsStep4.setVisibility(View.VISIBLE);
                tvStep4 = findViewById(R.id.tv_step_4);
                break;
        }
    }

    private void showIndicator(int commandLength) {
        switch (commandLength) {
            case 2:
                vsStep2.setVisibility(View.VISIBLE);
                tvStep2.setVisibility(View.VISIBLE);
                break;
            case 3:
                tvStep2.setVisibility(View.VISIBLE);
                tvStep3.setVisibility(View.VISIBLE);
                break;
            case 4:
                tvStep2.setVisibility(View.VISIBLE);
                tvStep3.setVisibility(View.VISIBLE);
                tvStep4.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void updateIndicatorOnUiThread(final int currentActionIndex) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                updateIndicator(currentActionIndex);
                updateGif(currentActionIndex);
            }
        });
    }

    private void updateIndicator(int currentActionPassedCount) {
        switch (currentActionPassedCount) {
            case 2:
                tvStep1.setText("");
                tvStep2.setText("2");
                setTextViewFocus(tvStep2);
                break;
            case 3:
                tvStep1.setText("");
                tvStep2.setText("");
                setTextViewFocus(tvStep2);
                tvStep3.setText("3");
                setTextViewFocus(tvStep3);
                break;
            case 4:
                tvStep1.setText("");
                tvStep2.setText("");
                setTextViewFocus(tvStep2);
                tvStep3.setText("");
                setTextViewFocus(tvStep3);
                tvStep4.setText("4");
                setTextViewFocus(tvStep4);
                break;
        }
    }

    private void updateGif(int currentActionIndex) {
        switch (mActions[currentActionIndex]) {
            case ACTION_TURN_HEAD_TO_LEFT:
                gifAction.setImageResource(R.drawable.turn_left);
                break;
            case ACTION_TURN_HEAD_TO_RIGHT:
                gifAction.setImageResource(R.drawable.turn_right);
                break;
            case ACTION_OPEN_MOUTH:
                gifAction.setImageResource(R.drawable.open_mouth);
                break;
            case ACTION_BLINK_EYES:
                gifAction.setImageResource(R.drawable.open_eyes);
                break;
        }
    }

    private void setTextViewFocus(TextView tv) {
        tv.setBackgroundDrawable(ContextCompat.getDrawable(getApplication(), R.drawable.circle_tv_focus));
    }

    private void setTextViewUnFocus(TextView tv) {
        tv.setBackgroundDrawable(ContextCompat.getDrawable(getApplication(), R.drawable.circle_tv_un_focus));
    }

    private void playSounds(int currentActionIndex) {
        switch (mActions[currentActionIndex]) {
            case ACTION_TURN_HEAD_TO_LEFT:
                playSound(getAssetFileDescriptor("turn_head_to_left.wav"));
                break;
            case ACTION_TURN_HEAD_TO_RIGHT:
                playSound(getAssetFileDescriptor("turn_head_to_right.wav"));
                break;
            case ACTION_OPEN_MOUTH:
                playSound(getAssetFileDescriptor("open_mouth.wav"));
                break;
            case ACTION_BLINK_EYES:
                playSound(getAssetFileDescriptor("blink_eyes.wav"));
                break;
        }
    }

    private void playSound(AssetFileDescriptor fileDescriptor) {
        try {
            mPlayer.reset();
            mPlayer.setDataSource(fileDescriptor.getFileDescriptor(), fileDescriptor.getStartOffset(), fileDescriptor.getLength());
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
            LogUtils.e(TAG, "playSound error" + e.toString());
        }
    }

    private AssetFileDescriptor getAssetFileDescriptor(String assetName) {
        try {
            AssetFileDescriptor fileDescriptor = getApplication().getAssets().openFd(assetName);
            return fileDescriptor;
        } catch (IOException e) {
            e.printStackTrace();
            LogUtils.e(TAG, "getAssetFileDescriptor error" + e.toString());
        }
        return null;
    }

    private void jump2FailureActivity(String token) {
        finish();
        Intent intent = new Intent(AliveActivity.this, VerifiedActivity.class);
        intent.putExtra("token", token);
        startActivity(intent);
    }

    //ocr.php
    private void ocrCheck(String token) {
        EasyHttp.post(this)
                .api(new OcrZApi(etIdCard,etRealName,token))
                .request(new HttpCallback<OcrInfoBean>(this) {
                    @Override
                    public void onSucceed(OcrInfoBean result) {
                        super.onSucceed(result);
                        if (result.getCode() == 200) {
                            if (result.getResult().getStatus() == 1) {
                                verified(result.getResult().getAvatar());
                            } else {
                                if (result.getResult().getReasonType() == 2) {
                                    toast("??????????????????????????????????????????????????????????????????");
                                } else if (result.getResult().getReasonType() == 3) {
                                    toast("??????????????????????????????????????????");
                                } else if (result.getResult().getReasonType() == 4) {
                                    toast("???????????????");
                                } else if (result.getResult().getReasonType() == 5) {
                                    toast("?????????????????????????????????");
                                } else if (result.getResult().getReasonType() == 6) {
                                    toast("?????????????????????????????????");
                                } else if (result.getResult().getReasonType() == 7) {
                                    toast("??????????????????????????????????????????");
                                } else if (result.getResult().getReasonType() == 8) {
                                    toast("??????????????????????????????");
                                } else if (result.getResult().getReasonType() == 9) {
                                    toast("????????????????????????????????????????????????");
                                }
                                setLayouThreadt(false);
                            }
                        } else {
                            toast(result.getMsg());
                            setLayouThreadt(false);
                        }
                    }
                });
    }

    /**
     * ????????????
     */
    private void verified(String avatar) {
        EasyHttp.get(this)
                .api("api/User/realName?uid=" + AppConfig.getLoginBean().getId() + "&real_name=" + etRealName
                        + "&id_card=" + etIdCard + "&card_front=" + avatar + "&card_back=" + null)
                .request(new HttpCallback<HttpData<String>>(this) {
                    @Override
                    public void onSucceed(HttpData<String> result) {
                        super.onSucceed(result);
                        if (result.getCode() == 1) {
                            getUserInfo();
                        } else {
                            toast(result.getMessage());
                        }
                    }
                });
    }

    /**
     * ??????????????????
     */
    private void getUserInfo() {
        EasyHttp.get(this)
                .api("api/sists/getUserinfo?uid=" + AppConfig.getLoginBean().getId() + "&client_id=" + util.MD5.md5Str(Identifier.getSN()))
                .request(new HttpCallback<HttpData<LoginBean>>(this) {
                    @Override
                    public void onSucceed(HttpData<LoginBean> result) {
                        super.onSucceed(result);
                        if (result.getCode() == 1) {
                            AppConfig.setLoginBean(result.getData());
                            setLayouThreadt(true);
                        }
                    }
                });
    }
}