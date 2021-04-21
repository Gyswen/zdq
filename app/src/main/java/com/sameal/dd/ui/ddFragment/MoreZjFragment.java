package com.sameal.dd.ui.ddFragment;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.hjq.base.BaseAdapter;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.hjq.widget.view.CircleImageView;
import com.sameal.dd.R;
import com.sameal.dd.action.StatusAction;
import com.sameal.dd.common.MyAdapter;
import com.sameal.dd.common.MyFragment;
import com.sameal.dd.helper.BigDecimalUtils;
import com.sameal.dd.helper.LogUtils;
import com.sameal.dd.http.model.HttpData;
import com.sameal.dd.http.response.WealthBean;
import com.sameal.dd.http.response.ZjBean;
import com.sameal.dd.other.AppConfig;
import com.sameal.dd.other.IntentKey;
import com.sameal.dd.ui.ddActivity.ZjDetailActivity;
import com.sameal.dd.ui.dialog.TopMoneyDialog;
import com.sameal.dd.widget.HintLayout;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import java.util.List;

/**
 * @author zhangj
 * @date 2021/1/3 15:52
 * desc 更多
 */
public class MoreZjFragment extends MyFragment implements StatusAction {

    RecyclerView recy;
    HintLayout hint;
    SmartRefreshLayout refresh;
    private int type = 1;
    private MoreZjAdapter moreZjAdapter;
    private WealthAdapter wealthAdapter;
    private int p = 1;
    private TopMoneyDialog.Builder topMoneyBuilder;

    public static MoreZjFragment newInstance(int type) {
        return new MoreZjFragment(type);
    }

    public MoreZjFragment(int type) {
        this.type = type;
        LogUtils.d(TAG, "MoreZjFragment: " + this.type);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_more_zj;
    }

    @Override
    protected void initView() {
        refresh = (SmartRefreshLayout) findViewById(R.id.refresh);
        hint = (HintLayout) findViewById(R.id.hint);
        recy = (RecyclerView) findViewById(R.id.recy);
    }

    @Override
    protected void initData() {
        recy.setLayoutManager(new LinearLayoutManager(getActivity()));
        if (type == 5) {
            wealthAdapter = new WealthAdapter(getActivity());
            topMoneyBuilder = new TopMoneyDialog.Builder(getActivity());
            wealthAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(RecyclerView recyclerView, View itemView, int position) {
                    topMoneyBuilder
                            .setData(wealthAdapter.getItem(position))
                            .show();
                }
            });
            recy.setAdapter(wealthAdapter);
            refresh.setEnableLoadMore(false);
        } else {
            moreZjAdapter = new MoreZjAdapter(getActivity(), type);
            moreZjAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(RecyclerView recyclerView, View itemView, int position) {
                    Intent intent = new Intent(getActivity(), ZjDetailActivity.class);
                    intent.putExtra(IntentKey.ID, moreZjAdapter.getItem(position).getId());
                    intent.putExtra(IntentKey.DATA, new Gson().toJson(moreZjAdapter.getItem(position)));
                    startActivity(intent);
                }
            });
            recy.setAdapter(moreZjAdapter);
            refresh.setEnableAutoLoadMore(false);
        }

        if (type == 5){
            getTopMoney();
            refresh.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
                @Override
                public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

                }

                @Override
                public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                    wealthAdapter.clearData();
                    getTopMoney();
                }
            });
        } else {
            getForumExperts();
            refresh.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
                @Override
                public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                    p++;
                    getForumExperts();
                }

                @Override
                public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                    p = 1;
                    moreZjAdapter.clearData();
                    getForumExperts();
                }
            });
        }
    }

    @Override
    public HintLayout getHintLayout() {
        return hint;
    }

    /**
     * 获取专家
     */
    private void getForumExperts() {
        EasyHttp.get(this)
                .api("api/Forum/getForumExperts?code=" + type + "&uid=" + AppConfig.getLoginBean().getId() + "&p=" + p)
                .request(new HttpCallback<HttpData<List<ZjBean>>>(this) {
                    @Override
                    public void onSucceed(HttpData<List<ZjBean>> result) {
                        super.onSucceed(result);
                        if (result.getCode() == 1) {
                            moreZjAdapter.addData(result.getData());
                        } else {
                            toast(result.getMessage());
                            p--;
                        }
                        if (moreZjAdapter.getItemCount() == 0) {
                            showEmpty();
                        } else {
                            showComplete();
                        }
                        refresh.finishRefresh();
                        refresh.finishLoadMore();
                    }
                });
    }

    /**
     * 获取财富榜
     */
    private void getTopMoney() {
        EasyHttp.get(this)
                .api("api/sists/getTopMoney")
                .request(new HttpCallback<HttpData<List<WealthBean>>>(this) {
                    @Override
                    public void onSucceed(HttpData<List<WealthBean>> result) {
                        super.onSucceed(result);
                        if (result.getCode() == 1) {
                            wealthAdapter.addData(result.getData());
                        } else {
                            toast(result.getMessage());
                        }
                        if (wealthAdapter.getItemCount() == 0) {
                            showEmpty();
                        } else {
                            showComplete();
                        }
                        refresh.finishRefresh();
                    }
                });
    }

    class MoreZjAdapter extends MyAdapter<ZjBean> {

        public int type = 1;

        public MoreZjAdapter(@NonNull Context context, int type) {
            super(context);
            this.type = type;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LogUtils.d(TAG, "onCreateViewHolder: " + type);
            if (type == 1 || type == 4) {
                return new AllViewHolder(R.layout.item_more_zj_all);
            } else {
                return new MzLhViewHolder(R.layout.item_more_zj_mz_lh);
            }
        }

        class AllViewHolder extends MyAdapter.ViewHolder {

            private CircleImageView imgAvatar;
            private TextView tvName, tvJob, tvBlue, tvRed;
            private Button btn;

            public AllViewHolder(int id) {
                super(id);
                imgAvatar = (CircleImageView) findViewById(R.id.img_avatar);
                tvName = (TextView) findViewById(R.id.tv_name);
                tvJob = (TextView) findViewById(R.id.tv_job);
                tvBlue = (TextView) findViewById(R.id.tv_blue);
                tvRed = (TextView) findViewById(R.id.tv_red);
                btn = (Button) findViewById(R.id.btn);
            }

            @Override
            public void onBindView(int position) {
                ZjBean zjBean = getItem(position);
                Glide.with(getContext())
                        .load(zjBean.getAvatar())
                        .error(R.mipmap.icon_contact_avatar_default)
                        .placeholder(R.mipmap.icon_contact_avatar_default)
                        .into(imgAvatar);
                tvName.setText(zjBean.getNickname());
                tvJob.setText(zjBean.getOccupation());
                tvBlue.setText("近" + zjBean.getFrequency() + "红" + zjBean.getFrequency_red());
                tvRed.setText(zjBean.getContinuity_red() + "连红");
                if (zjBean.getIs_follow() == 1) {
                    btn.setText(R.string.ygz);
                    btn.setBackgroundResource(R.drawable.bg_ygz);
                } else {
                    btn.setText(R.string.gz);
                    btn.setBackgroundResource(R.drawable.bg_circle_red);
                }
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MoreZjFragment.this.setFollow(zjBean.getId());
                    }
                });
                tvBlue.setTextColor(getColor(R.color.nice_blue));
                tvRed.setTextColor(getColor(R.color.nice_red));
                if (type == 1) {
                    tvBlue.setBackgroundResource(R.drawable.bg_football_blue);
                    tvRed.setBackgroundResource(R.drawable.bg_football_red);
                } else {
                    tvBlue.setBackgroundResource(R.drawable.bg_follow_blue);
                    tvRed.setBackgroundResource(R.drawable.bg_follow_red);
                }
            }
        }

        class MzLhViewHolder extends MyAdapter.ViewHolder {

            private ImageView imgNo;
            private CircleImageView imgAvatar;
            private TextView tvName, tvJob, tvNo, tvPercent, tvType;

            public MzLhViewHolder(int id) {
                super(id);
                imgAvatar = (CircleImageView) findViewById(R.id.img_avatar);
                imgNo = (ImageView) findViewById(R.id.img_no);
                tvName = (TextView) findViewById(R.id.tv_name);
                tvJob = (TextView) findViewById(R.id.tv_job);
                tvNo = (TextView) findViewById(R.id.tv_no);
                tvPercent = (TextView) findViewById(R.id.tv_percent);
                tvType = (TextView) findViewById(R.id.tv_type);
            }

            @Override
            public void onBindView(int position) {
                ZjBean zjBean = getItem(position);
                Glide.with(getContext())
                        .load(zjBean.getAvatar())
                        .error(R.mipmap.icon_contact_avatar_default)
                        .placeholder(R.mipmap.icon_contact_avatar_default)
                        .into(imgAvatar);
                tvName.setText(zjBean.getNickname());
                tvJob.setText(zjBean.getOccupation());
                if (position == 0) {
                    imgNo.setVisibility(View.VISIBLE);
                    tvNo.setVisibility(View.GONE);
                    imgNo.setImageResource(R.mipmap.top1);
                } else if (position == 1) {
                    imgNo.setVisibility(View.VISIBLE);
                    tvNo.setVisibility(View.GONE);
                    imgNo.setImageResource(R.mipmap.top2);
                } else if (position == 2) {
                    imgNo.setVisibility(View.VISIBLE);
                    tvNo.setVisibility(View.GONE);
                    imgNo.setImageResource(R.mipmap.top3);
                } else {
                    imgNo.setVisibility(View.GONE);
                    tvNo.setVisibility(View.VISIBLE);
                    tvNo.setText(position + 1 + "");
                }
                if (type == 2) {
                    tvPercent.setText(BigDecimalUtils.mul(zjBean.getHit_rate(),"100",0) + "%");
                    tvType.setText(R.string.mzl);
                } else {
                    tvPercent.setText(zjBean.getContinuity_red());
                    tvType.setText(R.string.lh);
                }
            }
        }
    }

    class WealthAdapter extends MyAdapter<WealthBean> {

        public WealthAdapter(@NonNull Context context) {
            super(context);
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LogUtils.d(TAG, "onCreateViewHolder: " + type);
            return new WealthHolder(R.layout.wealth_adapter);
        }

        class WealthHolder extends ViewHolder {

            private ImageView imgNo;
            private TextView tvNo;
            private CircleImageView imgAvatar;
            private TextView tvName;
            private TextView tvMoney;

            public WealthHolder(int id) {
                super(id);
                imgNo = (ImageView) findViewById(R.id.img_no);
                tvNo = (TextView) findViewById(R.id.tv_no);
                imgAvatar = (CircleImageView) findViewById(R.id.img_avatar);
                tvName = (TextView) findViewById(R.id.tv_name);
                tvMoney = (TextView) findViewById(R.id.tv_money);
            }

            @Override
            public void onBindView(int position) {
                WealthBean wealthBean = getItem(position);
                Glide.with(getContext())
                        .load(wealthBean.getAvatar())
                        .error(R.mipmap.icon_contact_avatar_default)
                        .placeholder(R.mipmap.icon_contact_avatar_default)
                        .into(imgAvatar);
                tvName.setText(wealthBean.getNickname());
                tvMoney.setText(ChangeMoney(wealthBean.getMoney()));
                if (position == 0) {
                    imgNo.setVisibility(View.VISIBLE);
                    tvNo.setVisibility(View.GONE);
                    imgNo.setImageResource(R.mipmap.top1);
                } else if (position == 1) {
                    imgNo.setVisibility(View.VISIBLE);
                    tvNo.setVisibility(View.GONE);
                    imgNo.setImageResource(R.mipmap.top2);
                } else if (position == 2) {
                    imgNo.setVisibility(View.VISIBLE);
                    tvNo.setVisibility(View.GONE);
                    imgNo.setImageResource(R.mipmap.top3);
                } else {
                    imgNo.setVisibility(View.GONE);
                    tvNo.setVisibility(View.VISIBLE);
                    tvNo.setText(position + 1 + "");
                }
            }
        }

        /**
         * 超过10000转换
         * @param money
         * @return
         */
        private String ChangeMoney(String money){
            double cMoneyD = Double.valueOf(money);
            if (cMoneyD > 9999){
                String cMoney = String.format("%.2f",cMoneyD / 10000);
                return cMoney+"w";
            }
            return money;
        }
    }

    /**
     * 关注取消关注专家
     *
     * @param eid
     */
    public void setFollow(int eid) {
        EasyHttp.get(this)
                .api("api/Forum/setFollow?uid=" + AppConfig.getLoginBean().getId() + "&eid=" + eid)
                .request(new HttpCallback<HttpData<String>>(this) {
                    @Override
                    public void onSucceed(HttpData<String> result) {
                        super.onSucceed(result);
                        if (result.getCode() == 1) {
                            p = 1;
                            moreZjAdapter.clearData();
                            getForumExperts();
                        }
                    }
                });
    }
}