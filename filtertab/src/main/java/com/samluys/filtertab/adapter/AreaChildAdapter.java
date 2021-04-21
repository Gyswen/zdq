package com.samluys.filtertab.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.samluys.filtertab.R;
import com.samluys.filtertab.base.BaseFilterBean;
import com.samluys.filtertab.util.SpUtils;

import java.util.ArrayList;
import java.util.List;


public class AreaChildAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<BaseFilterBean> mList;
    private OnItemClickListener onItemClickListener;

    public AreaChildAdapter(Context context) {
        mContext = context;
        mList = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_area_child, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {

        try {
            ViewHolder viewHolder = (ViewHolder) holder;
            BaseFilterBean bean = mList.get(position);
            viewHolder.tv_content.setText(bean.getItemName());

            // 是否设置“不限”为选中
            boolean isSelectFirst = true;
            for (int i = 0; i < mList.size(); i++) {
                BaseFilterBean entity = mList.get(i);
                if (entity.getSelecteStatus() == 1) {
                    isSelectFirst = false;
                    break;
                }
            }

            if (isSelectFirst) {
                mList.get(0).setSelecteStatus(1);
            }

            TextPaint textPaint = viewHolder.tv_content.getPaint();
            if (bean.getSelecteStatus() == 0) {
                textPaint.setFakeBoldText(false);
                viewHolder.tv_content.setTextColor(Color.parseColor("#333333"));
                viewHolder.img_select.setVisibility(View.GONE);
            } else {
                if (SpUtils.getInstance(mContext).getTextStyle() == 1) {
                    textPaint.setFakeBoldText(true);
                }
                viewHolder.tv_content.setTextColor(Color.parseColor("#fb6d23"));
                viewHolder.img_select.setVisibility(View.VISIBLE);
            }

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for (int i = 0; i < mList.size(); i++) {
                        if (i == position) {
                            mList.get(position).setSelecteStatus(1);
                        } else {
                            mList.get(i).setSelecteStatus(0);
                        }
                    }
                    notifyDataSetChanged();
                    onItemClickListener.onItemClick(position);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addData(List<BaseFilterBean> list) {
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public void cleanData() {
        mList.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_content;
        ImageView img_select;

        public ViewHolder(View itemView) {
            super(itemView);

            tv_content = itemView.findViewById(R.id.tv_content);
            img_select = itemView.findViewById(R.id.img_select);
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
