package com.sameal.dd.ui.dialog;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import com.hjq.base.BaseDialog;
import com.sameal.dd.R;

public class PrivacyDialog {

    public static final class Builder extends UIDialog.Builder<Builder> {

        private TextView tvBottom;

        private OnListener mListener;

        public Builder(Context context) {
            super(context);
            setCustomView(R.layout.privacy_dialog);
            tvBottom = (TextView) findViewById(R.id.tv_bottom);
            setCancel("不同意并关闭");
            setConfirm("同意");
            setCanceledOnTouchOutside(false);
            setCbText(context);
        }

        private void setCbText(Context context) {
            String string = tvBottom.getText().toString();
            String key1 = "《用户协议》";
            String key2 = "《隐私政策》";
            int index1 = string.indexOf(key1);
            int index2 = string.indexOf(key2);

            //需要显示的字串
            SpannableString spannedString = new SpannableString(string);
            //设置点击字体颜色
            ForegroundColorSpan colorSpan1 = new ForegroundColorSpan(context.getResources().getColor(R.color.colorConfirm));
            spannedString.setSpan(colorSpan1, index1, index1 + key1.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            ForegroundColorSpan colorSpan2 = new ForegroundColorSpan(context.getResources().getColor(R.color.colorConfirm));
            spannedString.setSpan(colorSpan2, index2, index2 + key2.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            //设置点击字体大小
            AbsoluteSizeSpan sizeSpan1 = new AbsoluteSizeSpan(13, true);
            spannedString.setSpan(sizeSpan1, index1, index1 + key1.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            AbsoluteSizeSpan sizeSpan2 = new AbsoluteSizeSpan(13, true);
            spannedString.setSpan(sizeSpan2, index2, index2 + key2.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            //设置点击事件
            ClickableSpan clickableSpan1 = new ClickableSpan() {
                @Override
                public void onClick(View view) {
                    mListener.jump("8");
//                    getCustomerDetail("8");
                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    //点击事件去掉下划线
                    ds.setUnderlineText(false);
                }
            };
            spannedString.setSpan(clickableSpan1, index1, index1 + key1.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

            ClickableSpan clickableSpan2 = new ClickableSpan() {
                @Override
                public void onClick(View view) {
                    mListener.jump("9");
//                    getCustomerDetail("9");
                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    //点击事件去掉下划线
                    ds.setUnderlineText(false);
                }
            };
            spannedString.setSpan(clickableSpan2, index2, index2 + key2.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            tvBottom.setText(spannedString);
            tvBottom.setMovementMethod(LinkMovementMethod.getInstance());
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.tv_ui_cancel:
                    mListener.onCancel(getDialog());
                    break;
                case R.id.tv_ui_confirm:
                    mListener.onConfirm(getDialog(),false);
                    break;
            }
        }

        public Builder setListener(OnListener onListener) {
            mListener = onListener;
            return this;
        }

        public interface OnListener {

            /**
             * 点击确定时回调
             */
            void onConfirm(BaseDialog dialog, boolean content);

            /**
             * 点击取消时回调
             */
            default void onCancel(BaseDialog dialog) {
            }

            /**
             * 点击跳转协议
             */
            default void jump(String id) {
            }
        }
    }
}
