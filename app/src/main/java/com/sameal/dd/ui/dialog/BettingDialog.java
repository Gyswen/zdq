package com.sameal.dd.ui.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.hjq.base.BaseAdapter;
import com.hjq.base.BaseDialog;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.hjq.toast.ToastUtils;
import com.sameal.dd.R;
import com.sameal.dd.aop.SingleClick;
import com.sameal.dd.common.MyAdapter;
import com.sameal.dd.http.model.HttpData;
import com.sameal.dd.http.response.LoginBean;
import com.sameal.dd.http.response.SportDetailBean;
import com.sameal.dd.other.AppConfig;
import com.sameal.dd.ui.ddActivity.CustomerDetailActivity;

import java.util.Arrays;

public class BettingDialog {

    public static final class Builder extends UIDialog.Builder<Builder> implements BaseDialog.OnShowListener {

        private OnListener mListener;
        private ImageView imgClose;
        private TextView tvStartName, tvEndName, tvText1, tvText2, tvBetting, tvMoney, tvResult;
        private EditText etMoney;
        private RecyclerView recy;
        private CheckBox checkBox;
        private LinearLayout llCheck;

        private SportDetailBean.EventsBean eventsBean;

        public static int selectPosi;
        private BettingAdapter bettingAdapter;

        public Builder(Context context) {
            super(context);
            setCustomView(R.layout.betting_dialog);

            checkBox = findViewById(R.id.checkbox);
            imgClose = findViewById(R.id.img_close);
            tvStartName = findViewById(R.id.tv_start_name);
            tvEndName = findViewById(R.id.tv_end_name);
            tvText1 = findViewById(R.id.tv_text1);
            tvText2 = findViewById(R.id.tv_text2);
            tvBetting = findViewById(R.id.tv_betting);
            tvMoney = findViewById(R.id.tv_money);
            tvResult = findViewById(R.id.tv_result);
            llCheck = findViewById(R.id.ll_check);
            recy = findViewById(R.id.recy);
            recy.setLayoutManager(new GridLayoutManager(context, 3));
            bettingAdapter = new BettingAdapter(context);
            bettingAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(RecyclerView recyclerView, View itemView, int position) {
                    etMoney.setText(bettingAdapter.getItem(position) + "");
                    selectPosi = position;
                    bettingAdapter.notifyDataSetChanged();
                }
            });
            recy.setAdapter(bettingAdapter);
            bettingAdapter.addData(Arrays.asList(100, 500, 1000, 5000, 10000, 50000));
            bettingAdapter.notifyDataSetChanged();
            etMoney = findViewById(R.id.et_money);
            etMoney.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (!TextUtils.isEmpty(editable.toString()) && !TextUtils.isEmpty(AppConfig.getLoginBean().getMoney())) {
                        if (Double.valueOf(editable.toString()) > Double.valueOf(AppConfig.getLoginBean().getMoney())) {
                            setConfirm("余额不足，请充值");
                        } else if (Double.valueOf(editable.toString()) < 100){
                            setConfirm("最小单笔投注金额100");
                        } else if (Double.valueOf(editable.toString()) > 50000) {
                            setConfirm("最大单笔投注金额50000");
                        } else {
                            setConfirm("投注");
                        }
                        tvResult.setText(Html.fromHtml("猜对：<font color='#fb6d23'>" + String.format("%.2f", (eventsBean.getOdds() / 1000 * Double.valueOf(etMoney.getText().toString()))) + "</font>"));
                        boolean isHave = false;
                        for (int i = 0; i < bettingAdapter.getItemCount(); i++) {
                            if (editable.toString().equals(bettingAdapter.getItem(i) + "")) {
                                selectPosi = i;
                                isHave = true;
                            }
                        }
                        if (!isHave) {
                            selectPosi = 10;
                        }
                        bettingAdapter.notifyDataSetChanged();
                    }
                }
            });
            setOnClickListener(R.id.img_close, R.id.tv_tyxy);
        }

        public Builder setListener(OnListener listener) {
            mListener = listener;
            return this;
        }

        public Builder setData(SportDetailBean eventsBean) {
            tvStartName.setText(eventsBean.getItem1_name());
            tvEndName.setText(eventsBean.getItem2_name());
            return this;
        }

        public Builder setData(SportDetailBean.EventsBean eventsBean) {
            this.eventsBean = eventsBean;
            etMoney.setText("100");
            if (Double.valueOf(etMoney.getText().toString()) > Double.valueOf(AppConfig.getLoginBean().getMoney())) {
                setConfirm("余额不足，请充值");
            } else if (Double.valueOf(etMoney.getText().toString()) < 100){
                setConfirm("最小单笔投注金额100");
            } else if (Double.valueOf(etMoney.getText().toString()) > 50000) {
                setConfirm("最大单笔投注金额50000");
            } else {
                setConfirm("投注");
            }
            selectPosi = 0;
            bettingAdapter.notifyDataSetChanged();
            tvText1.setText(eventsBean.getG_name());
            tvText2.setText(eventsBean.getName() + "@" + String.format("%.2f", eventsBean.getOdds() / 1000));
            tvBetting.setText(Html.fromHtml("指数：<font color='#fb6d23'>" + String.format("%.2f", eventsBean.getOdds() / 1000) + "</font>"));
            tvMoney.setText(Html.fromHtml("余额：<font color='#fb6d23'>" + AppConfig.getLoginBean().getMoney() + "椰糖</font>"));
            tvResult.setText(Html.fromHtml("猜对：<font color='#fb6d23'>" + String.format("%.2f", (eventsBean.getOdds() / 1000 * Double.valueOf(etMoney.getText().toString()))) + "椰糖</font>"));
            return this;
        }

        @SuppressLint("NonConstantResourceId")
        @SingleClick
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.img_close:
                    dismiss();
                    break;
                case R.id.tv_tyxy:
                    if (mListener != null) {
                        mListener.jump();
                    }
                    break;
                case R.id.tv_ui_confirm:
                    if (TextUtils.isEmpty(etMoney.getText().toString())) {
                        ToastUtils.show("请输入投注金额");
                        return;
                    }
                    if (checkBox.isChecked()) {
                        if (mListener != null) {
                            mListener.onConfirm(getDialog(), etMoney.getText().toString());
                        }
                    } else {
                        ToastUtils.show("请同意体育游戏规则协议");
                    }
                    break;
                case R.id.tv_ui_cancel:
                    autoDismiss();
                    if (mListener != null) {
                        mListener.onCancel(getDialog());
                    }
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onShow(BaseDialog dialog) {

        }
    }

    public interface OnListener {

        /**
         * 点击确定时回调
         */
        void onConfirm(BaseDialog dialog, String content);

        /**
         * 点击取消时回调
         */
        default void onCancel(BaseDialog dialog) {
        }

        /**
         * 点击跳转协议
         */
        default void jump() {
        }
    }


    static class BettingAdapter extends MyAdapter<Integer> {

        public BettingAdapter(@NonNull Context context) {
            super(context);
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(R.layout.item_betting_money);
        }

        class ViewHolder extends MyAdapter.ViewHolder {

            TextView tvMoney;

            public ViewHolder(int id) {
                super(id);
                tvMoney = (TextView) findViewById(R.id.tv_money);
            }

            @Override
            public void onBindView(int position) {
                tvMoney.setText(getItem(position) + "椰糖");
                if (Builder.selectPosi == position) {
                    tvMoney.setTextColor(getColor(R.color.white));
                    tvMoney.setBackgroundResource(R.drawable.bg_circle_red_5);
                } else {
                    tvMoney.setTextColor(getColor(R.color.black));
                    tvMoney.setBackgroundResource(R.drawable.bg_edit);
                }
            }
        }
    }

}
