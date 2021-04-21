package com.sameal.dd.ui.ddActivity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.hjq.base.BaseAdapter;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.hjq.widget.view.CircleImageView;
import com.sameal.dd.R;
import com.sameal.dd.action.StatusAction;
import com.sameal.dd.common.MyActivity;
import com.sameal.dd.common.MyAdapter;
import com.sameal.dd.helper.BigDecimalUtils;
import com.sameal.dd.helper.GridSpacingItemDecoration;
import com.sameal.dd.helper.TimeUtil;
import com.sameal.dd.http.model.HttpData;
import com.sameal.dd.http.response.ZjfaBean;
import com.sameal.dd.other.AppConfig;
import com.sameal.dd.other.IntentKey;
import com.sameal.dd.widget.HintLayout;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import java.util.List;

/**
 * @author zhangj
 * @date 2020/12/19 11:57
 * desc 我的方案
 */
public class ProgramActivity extends MyActivity implements StatusAction {

    RecyclerView recy;
    HintLayout hint;
    SmartRefreshLayout refresh;

    private int p = 1;
    private FootballAdapter footballAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_program;
    }

    @Override
    protected void initView() {
        refresh = (SmartRefreshLayout) findViewById(R.id.refresh);
        hint = (HintLayout) findViewById(R.id.hint);
        recy = (RecyclerView) findViewById(R.id.recy);
    }

    @Override
    protected void initData() {
        recy.setLayoutManager(new LinearLayoutManager(this));
        footballAdapter = new FootballAdapter(this);
        footballAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView recyclerView, View itemView, int position) {
                Intent intent = new Intent(getActivity(), ZjfaDetailActivity.class);
                intent.putExtra(IntentKey.ID, footballAdapter.getItem(position).getId());
                startActivity(intent);
            }
        });

        recy.addItemDecoration(new GridSpacingItemDecoration(1,20,true));
        recy.setAdapter(footballAdapter);
        getMyForumList();
        refresh.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                p++;
                getMyForumList();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                p = 1;
                footballAdapter.clearData();
                getMyForumList();
            }
        });
    }

    @Override
    public HintLayout getHintLayout() {
        return hint;
    }

    /**
     * 获取方案
     */
    private void getMyForumList() {
        EasyHttp.get(this)
                .api("api/Forum/getMyForumList?uid=" + AppConfig.getLoginBean().getId() + "&code=1" + "&p=" + p)
                .request(new HttpCallback<HttpData<List<ZjfaBean>>>(this) {
                    @Override
                    public void onSucceed(HttpData<List<ZjfaBean>> result) {
                        super.onSucceed(result);
                        if (result.getCode() == 1) {
                            footballAdapter.addData(result.getData());
                        } else {
//                            toast(result.getMessage());
                        }
                        if (footballAdapter.getItemCount() == 0) {
                            showEmpty();
                        } else {
                            showComplete();
                        }
                        refresh.finishRefresh();
                        refresh.finishLoadMore();
                    }
                });
    }

    public static class FootballAdapter extends MyAdapter<ZjfaBean> {

        public FootballAdapter(@NonNull Context context) {
            super(context);
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(R.layout.item_football);
        }

        class ViewHolder extends MyAdapter.ViewHolder {

            private CircleImageView imgAvatar;
            private TextView tvMoney, tvUserName, tvJob, tvBlue, tvRed, tvPercent, tvContent, tv_type_name, tvStartTime, tvTeam, tvCreateTime, tvType;

            public ViewHolder(int id) {
                super(id);
                imgAvatar = (CircleImageView) findViewById(R.id.img_avatar);
                tvUserName = (TextView) findViewById(R.id.tv_user_name);
                tvJob = (TextView) findViewById(R.id.tv_job);
                tvBlue = (TextView) findViewById(R.id.tv_blue);
                tvRed = (TextView) findViewById(R.id.tv_red);
                tvPercent = (TextView) findViewById(R.id.tv_percent);
                tvContent = (TextView) findViewById(R.id.tv_content);
                tv_type_name = (TextView) findViewById(R.id.tv_type_name);
                tvStartTime = (TextView) findViewById(R.id.tv_start_time);
                tvTeam = (TextView) findViewById(R.id.tv_team);
                tvCreateTime = (TextView) findViewById(R.id.tv_createTime);
                tvType = (TextView) findViewById(R.id.tv_type);
                tvMoney = (TextView) findViewById(R.id.tv_money);
            }

            @Override
            public void onBindView(int position) {
                ZjfaBean zjfaBean = getItem(position);
                Glide.with(getContext())
                        .load(zjfaBean.getUserinfo().getAvatar())
                        .error(R.mipmap.icon_contact_avatar_default)
                        .placeholder(R.mipmap.icon_contact_avatar_default)
                        .into(imgAvatar);
                tvUserName.setText(zjfaBean.getUserinfo().getNickname());
                tvJob.setText(zjfaBean.getUserinfo().getOccupation());
                tvBlue.setText("近" + zjfaBean.getUserinfo().getFrequency() + "红" + zjfaBean.getUserinfo().getFrequency_red());
                tvRed.setText(zjfaBean.getUserinfo().getContinuity_red() + "连红");
                tvPercent.setText(BigDecimalUtils.mul(zjfaBean.getUserinfo().getHit_rate(),"100",0) + "%");
                tvContent.setText(zjfaBean.getContent());
                tv_type_name.setText(zjfaBean.getGname());
                tvStartTime.setText(TimeUtil.getDateToString(Long.valueOf(zjfaBean.getStart_time())));
                tvTeam.setText(zjfaBean.getItem1_name() + "  vs  " + zjfaBean.getItem2_name());
                tvCreateTime.setText(TimeUtil.getDateToString(Long.valueOf(zjfaBean.getCreate_time())));
                if (zjfaBean.getMoney() == 0) {
                    tvType.setText("免费");
                    tvMoney.setText("免费");
                } else {
                    tvType.setText("收费");
                    tvMoney.setText(zjfaBean.getMoney() + "");
                }
            }
        }
    }
}