package com.sameal.dd.ui.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hjq.base.BaseDialog;
import com.hjq.widget.view.CircleImageView;
import com.sameal.dd.R;
import com.sameal.dd.http.response.WealthBean;
import com.sameal.dd.other.AppConfig;

public class TopMoneyDialog {

    public static final class Builder extends UIDialogMax.Builder<TopMoneyDialog.Builder>
            implements BaseDialog.OnShowListener {

        private ImageView imgClose;
        private CircleImageView imgAvatar;
        private TextView tvName;
        private TextView tvSex;
        private TextView tvBio;

        public Builder(Context context) {
            super(context);
            setCustomView(R.layout.top_money_dialog);

            setGravity(Gravity.BOTTOM);
            imgClose = (ImageView) findViewById(R.id.img_close);
            imgAvatar = (CircleImageView) findViewById(R.id.img_avatar);
            tvName = (TextView) findViewById(R.id.tv_name);
            tvSex = (TextView) findViewById(R.id.tv_sex);
            tvBio = (TextView) findViewById(R.id.tv_bio);

            setOnClickListener(R.id.img_close);
        }

        @Override
        public void onShow(BaseDialog dialog) {

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.img_close:
                    dismiss();
                    break;
            }
        }

        public Builder setData(WealthBean wealthBean){
            Glide.with(getActivity()).load(wealthBean.getAvatar())
                    .error(R.mipmap.icon_contact_avatar_default)
                    .placeholder(R.mipmap.icon_contact_avatar_default)
                    .into(imgAvatar);
            tvName.setText(wealthBean.getNickname());
            tvSex.setText(wealthBean.getGender() == 0 ?"男" : "女");
            tvBio.setText(wealthBean.getBio()+"");
            return this;
        }

    }
}
