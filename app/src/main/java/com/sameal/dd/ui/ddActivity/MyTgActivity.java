package com.sameal.dd.ui.ddActivity;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.sameal.dd.R;
import com.sameal.dd.action.StatusAction;
import com.sameal.dd.common.MyActivity;
import com.sameal.dd.common.MyAdapter;
import com.sameal.dd.http.model.HttpData;
import com.sameal.dd.http.response.TgUserBean;
import com.sameal.dd.other.AppConfig;
import com.sameal.dd.widget.HintLayout;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

/**
 * @author zhangj
 * @date 2021/1/16 15:16
 * desc 我的推广
 */
public class MyTgActivity extends MyActivity implements StatusAction {

    TextView tvUserNum;
    TextView tvMoney;
    Button btnStartTime;
    Button btnEndTime;
    Button btnSearch;
    RecyclerView recy;
    HintLayout hint;
    SmartRefreshLayout refresh;

    private TgAdapter tgAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_tg;
    }

    @Override
    protected void initView() {
        tvUserNum = (TextView) findViewById(R.id.tv_user_num);
        tvMoney = (TextView) findViewById(R.id.tv_money);
        btnStartTime = (Button) findViewById(R.id.btn_start_time);
        btnEndTime = (Button) findViewById(R.id.btn_end_time);
        btnSearch = (Button) findViewById(R.id.btn_search);
        refresh = (SmartRefreshLayout) findViewById(R.id.refresh);
        hint = (HintLayout) findViewById(R.id.hint);
        recy = (RecyclerView) findViewById(R.id.recy);

    }

    @Override
    protected void initData() {
        recy.setLayoutManager(new LinearLayoutManager(this));
        tgAdapter = new TgAdapter(this);
        recy.setAdapter(tgAdapter);
        getTgList();
    }

    @Override
    public void onRightClick(View v) {
        startActivity(ShareActivity.class);
    }

    /**
     * 获取推广列表
     */
    private void getTgList() {
        EasyHttp.get(this)
                .api("api/User/myShareList?uid=" + AppConfig.getLoginBean().getId())
                .request(new HttpCallback<HttpData<TgUserBean>>(this) {
                    @Override
                    public void onSucceed(HttpData<TgUserBean> result) {
                        super.onSucceed(result);
                        if (result.getCode() == 1) {
                            tgAdapter.setData(result.getData().getInfo());
                            tvUserNum.setText(result.getData().getCount() + "");
                            tvMoney.setText(result.getData().getSumcoin() + "");
                        }
                        if (tgAdapter.getItemCount() == 0) {
                            showEmpty();
                        } else {
                            showComplete();
                        }
                        refresh.finishRefresh();
                    }
                });
    }

    @Override
    public HintLayout getHintLayout() {
        return hint;
    }

    class TgAdapter extends MyAdapter<TgUserBean.InfoBean> {

        public TgAdapter(@NonNull Context context) {
            super(context);
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(R.layout.item_tg);
        }

        class ViewHolder extends MyAdapter.ViewHolder {

            private TextView tvUserName;
            private TextView tvMoney;

            public ViewHolder(int id) {
                super(id);
                tvUserName = (TextView) findViewById(R.id.tv_user_name);
                tvMoney = (TextView) findViewById(R.id.tv_money);
            }

            @Override
            public void onBindView(int position) {
                TgUserBean.InfoBean tgUserBean = getItem(position);
                tvUserName.setText(tgUserBean.getNickname());
                tvMoney.setText(tgUserBean.getCoin() + "");
            }
        }
    }

}