package com.sameal.dd.ui.popup;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hjq.base.BaseAdapter;
import com.hjq.base.BaseDialog;
import com.hjq.base.BasePopupWindow;
import com.sameal.dd.R;
import com.sameal.dd.aop.SingleClick;
import com.sameal.dd.common.MyAdapter;
import com.sameal.dd.ui.dialog.BettingDialog;

import java.util.Arrays;

public class FifiterPopup {

    public static final class Builder
            extends BasePopupWindow.Builder<CopyPopup.Builder> {

        private RecyclerView recy;
        private OnListener mListener;
        private FifiterAdapter fifiterAdapter;
        public static int selectPosi = 0;

        public Builder(Context context) {
            super(context);
            setContentView(R.layout.fifiter_popup);

            recy = (RecyclerView) findViewById(R.id.recy);
            recy.setLayoutManager(new GridLayoutManager(context, 3));
            fifiterAdapter = new FifiterAdapter(context);
            fifiterAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(RecyclerView recyclerView, View itemView, int position) {
                    selectPosi = position;
                    fifiterAdapter.notifyDataSetChanged();
                }
            });
            fifiterAdapter.setData(Arrays.asList("lol", "dota2", "csgo", "王者荣耀", "星际争霸", "守望先锋"));
            recy.setAdapter(fifiterAdapter);

            setOnClickListener(R.id.tv_ui_cancel, R.id.tv_ui_confirm);
        }

        public Builder setListener(OnListener listener) {
            mListener = listener;
            return this;
        }

        @SingleClick
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.tv_ui_cancel) {
                dismiss();
                if (mListener != null) {
                    mListener.onCancel();
                }
            } else if (v.getId() == R.id.tv_ui_confirm) {
                dismiss();
                if (mListener != null) {
                    mListener.onConfirm(selectPosi + 1);
                }
            }
        }
    }

    public interface OnListener {

        /**
         * 点击确定时回调
         */
        void onConfirm(int content);

        /**
         * 点击取消时回调
         */
        default void onCancel() {
        }
    }

    static class FifiterAdapter extends MyAdapter<String> {

        public FifiterAdapter(@NonNull Context context) {
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
                tvMoney.setText(getItem(position));
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
