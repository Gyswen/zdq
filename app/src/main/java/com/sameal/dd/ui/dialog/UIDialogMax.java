package com.sameal.dd.ui.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.StringRes;

import com.hjq.base.BaseDialog;
import com.sameal.dd.R;

/**
 * author : Android 轮子哥
 * github : https://github.com/getActivity/AndroidProject
 * time   : 2019/09/21
 * desc   : 项目通用 Dialog 布局封装
 */
public final class UIDialogMax {

    @SuppressWarnings("unchecked")
    public static class Builder<B extends UIDialogMax.Builder>
            extends BaseDialog.Builder<B> {

        private boolean mAutoDismiss = true;

        private final ViewGroup mContainerLayout;
        private final TextView mTitleView;

        public Builder(Context context) {
            super(context);

            setContentView(R.layout.ui_dialog_max);
            setAnimStyle(BaseDialog.ANIM_IOS);
            setGravity(Gravity.CENTER);

            mContainerLayout = findViewById(R.id.ll_ui_container);
            mTitleView = findViewById(R.id.tv_ui_title);
        }

        public B setCustomView(@LayoutRes int id) {
            return setCustomView(LayoutInflater.from(getContext()).inflate(id, mContainerLayout, false));
        }

        public B setCustomView(View view) {
            mContainerLayout.addView(view, 1);
            return (B) this;
        }

        public B setTitle(@StringRes int id) {
            return setTitle(getString(id));
        }

        public B setTitle(CharSequence text) {
            mTitleView.setText(text);
            return (B) this;
        }

        public B setAutoDismiss(boolean dismiss) {
            mAutoDismiss = dismiss;
            return (B) this;
        }

        public void autoDismiss() {
            if (mAutoDismiss) {
                dismiss();
            }
        }
    }
}