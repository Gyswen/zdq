package com.sameal.dd.ui.ddActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.util.TimeUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.sameal.dd.R;
import com.sameal.dd.action.StatusAction;
import com.sameal.dd.common.MyActivity;
import com.sameal.dd.common.MyAdapter;
import com.sameal.dd.helper.LogUtils;
import com.sameal.dd.helper.TimeUtil;
import com.sameal.dd.http.model.HttpData;
import com.sameal.dd.http.response.ClassifyChildEntry;
import com.sameal.dd.http.response.ClassifyEntry;
import com.sameal.dd.http.response.FilterInfoBean;
import com.sameal.dd.http.response.MoneyDetailBean;
import com.sameal.dd.other.AppConfig;
import com.sameal.dd.widget.HintLayout;
import com.samluys.filtertab.FilterResultBean;
import com.samluys.filtertab.FilterTabConfig;
import com.samluys.filtertab.listener.OnFilterToViewListener;
import com.samluys.filtertab.popupwindow.AreaSelectPopupWindow;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author zhangj
 * @date 2020/12/18 21:43
 * desc 我的明细
 */
public class MoneyDetailActivity extends MyActivity implements StatusAction {

    private SmartRefreshLayout mRefresh;
    private HintLayout hint;
    private RecyclerView recy;

    private DetailAdapter detailAdapter;
    List<ClassifyEntry> classifyEntries = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_money_detail;
    }

    @Override
    protected void initView() {
        mRefresh = findViewById(R.id.refresh);
        hint = findViewById(R.id.hint);
        recy = findViewById(R.id.recy);
        recy.setLayoutManager(new LinearLayoutManager(this));
        detailAdapter = new DetailAdapter(this);
        recy.setAdapter(detailAdapter);
    }

    @Override
    protected void initData() {
        getDetail();
        List<ClassifyChildEntry> childEntries = new ArrayList<>();
        childEntries.add(new ClassifyChildEntry(1, "英雄联盟", 1));
        childEntries.add(new ClassifyChildEntry(2, "data2", 0));
        childEntries.add(new ClassifyChildEntry(3, "王者荣耀", 0));
        childEntries.add(new ClassifyChildEntry(4, "csgo", 0));
        childEntries.add(new ClassifyChildEntry(5, "守望先锋", 0));
        childEntries.add(new ClassifyChildEntry(6, "炉石传说", 0));
        childEntries.add(new ClassifyChildEntry(7, "星际争霸2", 0));

        List<ClassifyChildEntry> childEntries2 = new ArrayList<>();
        childEntries2.add(new ClassifyChildEntry(1, "足球", 1));
        childEntries2.add(new ClassifyChildEntry(2, "篮球", 0));
        classifyEntries.add(new ClassifyEntry(1, "电竞", 1, childEntries));
        classifyEntries.add(new ClassifyEntry(2, "体育", 0, childEntries2));
    }

    @Override
    public void onRightClick(View v) {
        AreaSelectPopupWindow popupWindow = new AreaSelectPopupWindow(this, classifyEntries, FilterTabConfig.FILTER_TYPE_AREA
                , 0, new OnFilterToViewListener() {
            @Override
            public void onFilterToView(FilterResultBean resultBean) {
                LogUtils.d(TAG, "onFilterToView: " + resultBean.getName());
                toast(resultBean.getName());
            }

            @Override
            public void onFilterListToView(List<FilterResultBean> resultBean) {
                LogUtils.d(TAG, "onFilterListToView: " + resultBean);
            }
        });
        popupWindow.showAsDropDown(v);
    }

    /**
     * 获取明细列表
     */
    private void getDetail() {
        EasyHttp.get(this)
                .api("api/user/myDetailed?uid=" + AppConfig.getLoginBean().getId())
                .request(new HttpCallback<HttpData<List<MoneyDetailBean>>>(this) {
                    @Override
                    public void onSucceed(HttpData<List<MoneyDetailBean>> result) {
                        super.onSucceed(result);
                        if (result.getCode() == 1) {
                            detailAdapter.setData(result.getData());
                        } else {
                            toast(result.getMessage());
                        }
                        if (detailAdapter.getItemCount() == 0) {
                            showEmpty();
                        } else {
                            showComplete();
                        }
                        mRefresh.finishRefresh();
                        mRefresh.finishLoadMore();
                    }
                });
    }

    @Override
    public HintLayout getHintLayout() {
        return hint;
    }

    private class DetailAdapter extends MyAdapter<MoneyDetailBean> {

        public DetailAdapter(@NonNull Context context) {
            super(context);
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(R.layout.item_money_detail);
        }

        class ViewHolder extends MyAdapter.ViewHolder {

            private TextView tvOrderNo;
            private TextView tvNum;
            private TextView tvName;
            private TextView tvTime;
            private ImageView imgType;

            public ViewHolder(int id) {
                super(id);
                tvOrderNo = (TextView) findViewById(R.id.tv_order_no);
                tvNum = (TextView) findViewById(R.id.tv_num);
                tvName = (TextView) findViewById(R.id.tv_name);
                tvTime = (TextView) findViewById(R.id.tv_time);
                imgType = (ImageView) findViewById(R.id.img_type);
            }

            @Override
            public void onBindView(int position) {
                MoneyDetailBean detailBean = getItem(position);
                String type = "-";
                tvNum.setTextColor(getResources().getColor(R.color.color_333333));
                if (detailBean.getType() == 2) {
                    tvNum.setTextColor(getResources().getColor(R.color.color_fe701f));
                    type = "+";
                }
                tvNum.setText(type + detailBean.getMoney() + "椰糖");
                tvOrderNo.setText("订单编号：" + detailBean.getOrder_num() + "");
                tvTime.setText(TimeUtil.timeYMDHMinSFigure(Long.valueOf(detailBean.getAddtime() + "000")));
                if (detailBean.getCode() == 1) {
                    tvName.setText("充值");
                    imgType.setImageResource(R.mipmap.icon_recharge);
                } else if (detailBean.getCode() == 2) {
                    tvName.setText("投注");
                    imgType.setImageResource(R.mipmap.boxing_default);
                } else if (detailBean.getCode() == 3) {
                    tvName.setText("投注中奖");
                    imgType.setImageResource(R.mipmap.gift);
                } else if (detailBean.getCode() == 4) {
                    tvName.setText("专家方案");
                    imgType.setImageResource(R.mipmap.icon_program_guess);
                } else if (detailBean.getCode() == 5) {
                    tvName.setText("兑换");
                    imgType.setImageResource(R.mipmap.xuelidao_exchange);
                } else if (detailBean.getCode() == 6) {
                    tvName.setText("邀请好友赠送");
                    imgType.setImageResource(R.mipmap.icon_gift_guess);
                } else if (detailBean.getCode() == 7) {
                    tvName.setText("投注取消返还");
                    imgType.setImageResource(R.mipmap.luck_draw);
                }else if (detailBean.getCode() == 8) {
                    tvName.setText("抽奖");
                    imgType.setImageResource(R.mipmap.luck_draw);
                } else if (detailBean.getCode() == 9) {
                    tvName.setText("抽奖中奖");
                    imgType.setImageResource(R.mipmap.yes_luck_draw);
                }
            }
        }
    }
}