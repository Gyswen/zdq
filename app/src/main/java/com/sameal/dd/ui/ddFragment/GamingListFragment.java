package com.sameal.dd.ui.ddFragment;

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
import com.sameal.dd.common.MyAdapter;
import com.sameal.dd.common.MyFragment;
import com.sameal.dd.helper.BigDecimalUtils;
import com.sameal.dd.helper.SpUtil;
import com.sameal.dd.helper.TimeUtil;
import com.sameal.dd.http.model.HttpData;
import com.sameal.dd.http.response.ForumCaseBean;
import com.sameal.dd.http.response.ZjfaBean;
import com.sameal.dd.other.IntentKey;
import com.sameal.dd.ui.ddActivity.LoginActivity;
import com.sameal.dd.ui.ddActivity.ZjfaDetailActivity;
import com.sameal.dd.widget.HintLayout;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import java.util.List;

/**
 * @author zhangj
 * @date 2020/12/27 23:47
 * desc 电竞列表
 */
public class GamingListFragment extends MyFragment implements StatusAction {

    RecyclerView recy;
    HintLayout hint;
    SmartRefreshLayout refresh;

    private FootballAdapter footballAdapter;
    private ForumCaseBean.SonBean sonBean;
    private int id;

    public static GamingListFragment newInstance(ForumCaseBean.SonBean sonBean) {
        return new GamingListFragment(sonBean);
    }

    public static GamingListFragment newInstance(int sonBean) {
        return new GamingListFragment(sonBean);
    }

    public GamingListFragment(ForumCaseBean.SonBean sonBean) {
        this.sonBean = sonBean;
    }

    public GamingListFragment(int sonBean) {
        this.id = sonBean;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_gaming_list;
    }

    @Override
    protected void initView() {
        refresh = (SmartRefreshLayout) findViewById(R.id.refresh);
        hint = (HintLayout) findViewById(R.id.hint);
        recy = (RecyclerView) findViewById(R.id.recy);

        recy.setLayoutManager(new LinearLayoutManager(getActivity()));
        footballAdapter = new FootballAdapter(getActivity());
        footballAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView recyclerView, View itemView, int position) {
                if (SpUtil.getInstance().getBooleanValue(SpUtil.IS_LOGIN)) {
                    Intent intent = new Intent(getActivity(), ZjfaDetailActivity.class);
                    intent.putExtra(IntentKey.ID, footballAdapter.getItem(position).getId());
                    startActivity(intent);
                } else {
                    startActivity(LoginActivity.class);
                }
            }
        });
        recy.setAdapter(footballAdapter);
        refresh.setEnableLoadMore(false);
    }

    @Override
    protected void initData() {
        getForumTypeList();
        refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getForumTypeList();
            }
        });
    }

    @Override
    public HintLayout getHintLayout() {
        return hint;
    }

    /**
     * 获取子分类方案列表
     */
    private void getForumTypeList() {
        int sonId = sonBean != null ? sonBean.getId() : id;

        EasyHttp.get(this)
                .api("api/Forum/getForumTypeList?code=" + sonId)
                .request(new HttpCallback<HttpData<List<ZjfaBean>>>(this) {
                    @Override
                    public void onSucceed(HttpData<List<ZjfaBean>> result) {
                        super.onSucceed(result);
                        if (result.getCode() == 1) {
                            footballAdapter.setData(result.getData());
                        } else {
                            toast(result.getMessage());
                        }
                        if (footballAdapter.getItemCount() == 0) {
                            showEmpty();
                        } else {
                            showComplete();
                        }
                        refresh.finishRefresh();
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
            private TextView tvUserName, tvJob, tvBlue, tvRed, tvPercent, tvContent, tvStartTime, tvTeam, tvCreateTime, tvType, tv_type_name;

            public ViewHolder(int id) {
                super(id);
                imgAvatar = (CircleImageView) findViewById(R.id.img_avatar);
                tvUserName = (TextView) findViewById(R.id.tv_user_name);
                tvJob = (TextView) findViewById(R.id.tv_job);
                tvBlue = (TextView) findViewById(R.id.tv_blue);
                tvRed = (TextView) findViewById(R.id.tv_red);
                tvPercent = (TextView) findViewById(R.id.tv_percent);
                tvContent = (TextView) findViewById(R.id.tv_content);
                tvStartTime = (TextView) findViewById(R.id.tv_start_time);
                tvTeam = (TextView) findViewById(R.id.tv_team);
                tvCreateTime = (TextView) findViewById(R.id.tv_createTime);
                tvType = (TextView) findViewById(R.id.tv_type);
                tv_type_name = (TextView) findViewById(R.id.tv_type_name);
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
            }
        }
    }
}