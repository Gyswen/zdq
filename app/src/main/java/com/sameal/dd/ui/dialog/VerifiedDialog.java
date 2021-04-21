package com.sameal.dd.ui.dialog;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import com.hjq.base.BaseDialog;
import com.sameal.dd.R;
import com.sameal.dd.aop.SingleClick;

public class VerifiedDialog {

    public static final class Builder
            extends UIDialog.Builder<Builder>
            implements BaseDialog.OnShowListener {

        private OnListener mListener;
        private ImageView imgClose;


        public Builder(Context context) {
            super(context);
            setCustomView(R.layout.verified_dialog);

            setOnClickListener(R.id.img_close, R.id.btn_real);
        }

        public Builder setListener(OnListener listener) {
            mListener = listener;
            return this;
        }

        @SingleClick
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.img_close:
                    dismiss();
                    break;
                case R.id.btn_real:
                    autoDismiss();
                    if (mListener != null) {
                        mListener.onConfirm(getDialog());
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
        void onConfirm(BaseDialog dialog);

        /**
         * 点击取消时回调
         */
        default void onCancel(BaseDialog dialog) {
        }
    }

}
