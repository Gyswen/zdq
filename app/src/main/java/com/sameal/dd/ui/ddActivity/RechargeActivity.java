package com.sameal.dd.ui.ddActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.hjq.base.BaseAdapter;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.hjq.toast.ToastUtils;
import com.sameal.dd.R;
import com.sameal.dd.aop.SingleClick;
import com.sameal.dd.common.MyActivity;
import com.sameal.dd.common.MyAdapter;
import com.sameal.dd.helper.LogUtils;
import com.sameal.dd.http.model.HttpData;
import com.sameal.dd.http.response.PayOrederInfoBean;
import com.sameal.dd.http.response.PayResult;
import com.sameal.dd.http.response.RechargeBean;
import com.sameal.dd.other.AppConfig;
import com.sameal.dd.other.IntentKey;
import com.sameal.dd.ui.ddFragment.HomeFragment;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelpay.JumpToOfflinePay;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author zhangj
 * @date 2020/12/17 23:16
 * desc 充值
 */
public class RechargeActivity extends MyActivity {

    private TextView tvMoney;
    private TextView tvCzxy;
    private RecyclerView recyclerView;
    private RadioButton rbAlipay, rbWeChat;
    private RechargeMoneyAdapter moneyAdapter;
    private int posi = 0;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_recharge;
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
        tvMoney = findViewById(R.id.tv_money);
        recyclerView = findViewById(R.id.recy);
        rbAlipay = findViewById(R.id.rb_alipay);
        rbWeChat = findViewById(R.id.rb_wechat);
        tvCzxy = findViewById(R.id.tv_czxy);
        moneyAdapter = new RechargeMoneyAdapter(this);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        moneyAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView recyclerView, View itemView, int position) {
                posi = position;
                tvMoney.setText(moneyAdapter.getItem(position).getMoney() + "");
                moneyAdapter.notifyDataSetChanged();
            }
        });
        recyclerView.setAdapter(moneyAdapter);
        setOnClickListener(R.id.btn_confirm_recharge, R.id.tv_czxy);
    }

    @Override
    protected void initData() {
        setData();
    }

    private void setData() {
        getCointType();
    }

    @SingleClick
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_confirm_recharge) {
            if (rbAlipay.isChecked()) {
                getAlipay();
            } else if (rbWeChat.isChecked()) {
                getWechatMoney();
            } else {
                toast("请选择支付方式");
            }
        } else if (v.getId() == R.id.tv_czxy) {
            getCustomerDetail("12");
        }
    }

    /**
     * 获取充值方式
     */
    private void getCointType() {
        EasyHttp.get(this)
                .api("api/User/getCointype")
                .request(new HttpCallback<HttpData<List<RechargeBean>>>(this) {
                    @Override
                    public void onSucceed(HttpData<List<RechargeBean>> result) {
                        super.onSucceed(result);
                        if (result.getCode() == 1) {
                            moneyAdapter.setData(result.getData());
                            tvMoney.setText(result.getData().get(posi).getMoney()+"");
                        }
                    }
                });
    }

    /**
     * 调用支付宝
     */
    private void getAlipay() {
        EasyHttp.get(this)
                .api("api/Sists/getAlipay?uid=" + AppConfig.getLoginBean().getId() + "&money=" + tvMoney.getText().toString())
                .request(new HttpCallback<HttpData<String>>(this) {
                    @Override
                    public void onSucceed(HttpData<String> result) {
                        super.onSucceed(result);
                        if (result.getCode() == 200) {
                            Runnable payRunnable = new Runnable() {
                                @Override
                                public void run() {
                                    PayTask alipay = new PayTask(getActivity());
                                    Map<String, String> map = alipay.payV2(result.getData(), true);
                                    LogUtils.d("EasyHttp", map.toString());
                                    Message message = new Message();
                                    message.what = SDK_PAY_FLAG;
                                    message.obj = map;
                                    mHandler.sendMessage(message);
                                }
                            };

                            // 必须异步调用
                            Thread payThread = new Thread(payRunnable);
                            payThread.start();
                        } else {
                            toast(result.getMessage());
                        }
                    }
                });
    }

    PayOrederInfoBean payOrederInfoBean;

    private void getWechatMoney() {
        EasyHttp.get(this)
                .api("api/sists/wxpay?uid=" + AppConfig.getLoginBean().getId() + "&money=" + tvMoney.getText().toString())
                .request(new HttpCallback<HttpData<PayOrederInfoBean>>(this) {
                    @Override
                    public void onSucceed(HttpData<PayOrederInfoBean> result) {
                        super.onSucceed(result);
                        if (result.getCode() == 1) {
                            payOrederInfoBean = result.getData();
                            IWXAPI api = WXAPIFactory.createWXAPI(RechargeActivity.this, AppConfig.APP_ID, false);
                            PayReq req = new PayReq();
                            req.appId = AppConfig.APP_ID;
                            req.partnerId = result.getData().getMch_id();
                            req.prepayId = result.getData().getPrepay_id();
                            req.nonceStr = result.getData().getNonce_str();
                            req.timeStamp = result.getData().getTimes() + "";
                            req.packageValue = "Sign=WXPay";
                            req.sign = result.getData().getSigns();
                            api.sendReq(req);
                        } else {
                            toast(result.getMessage());
                        }
                    }
                });
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPayResponse(BaseResp resp) {
        LogUtils.d(TAG, "resp---微信支付回调---->" + resp.errCode);

        if (0 == resp.errCode) {//支付成功
            toast("支付成功");
            finish();
//            wxpayNotiy();
        } else {//支付失败
            toast("支付失败");
        }
    }

    /**
     * 微信回调
     */
    private void wxpayNotiy() {
        EasyHttp.get(this)
                .api("api/sists/wxpayNotiy?out_trade_no=" + payOrederInfoBean.getOut_trade_no())
                .request(new HttpCallback<HttpData<String>>(this) {
                    @Override
                    public void onSucceed(HttpData<String> result) {
                        super.onSucceed(result);
                        if (result.getCode() == 1) {
                            finish();
                        } else {
                            toast(result.getMessage());
                        }
                    }
                });
    }


    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     * 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        ToastUtils.show(getString(R.string.pay_success));
                        finish();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        ToastUtils.show(getString(R.string.pay_failed) + payResult.getMemo());
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();

        EventBus.getDefault().unregister(this);
    }

    private class RechargeMoneyAdapter extends MyAdapter<RechargeBean> {

        public RechargeMoneyAdapter(@NonNull Context context) {
            super(context);
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(R.layout.item_recharge_money);
        }

        class ViewHolder extends MyAdapter.ViewHolder {

            private TextView tvMoney;
            private TextView tvNum;
            private LinearLayout cardRecharge;

            public ViewHolder(int id) {
                super(id);
                tvMoney = (TextView) findViewById(R.id.tv_money);
                tvNum = (TextView) findViewById(R.id.tv_num);
                cardRecharge = (LinearLayout) findViewById(R.id.card_recharge);
            }

            @Override
            public void onBindView(int position) {
                RechargeBean rechargeBean = getItem(position);
                tvMoney.setText(rechargeBean.getMoney() + "元");
                tvNum.setText(rechargeBean.getCoin() + "椰糖");
                if (position == posi) {
                    cardRecharge.setBackground(getResources().getDrawable(R.drawable.bg_recharge_select));
                    tvMoney.setTextColor(getResources().getColor(R.color.white));
                    tvNum.setTextColor(getResources().getColor(R.color.white));
                } else {
                    cardRecharge.setBackground(getResources().getDrawable(R.drawable.bg_recharge_unselect));
                    tvMoney.setTextColor(getResources().getColor(R.color.black));
                    tvNum.setTextColor(getResources().getColor(R.color.gray));
                }
            }
        }
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