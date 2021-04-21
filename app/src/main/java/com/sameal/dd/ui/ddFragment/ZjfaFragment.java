package com.sameal.dd.ui.ddFragment;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.sameal.dd.helper.GridSpacingItemDecoration;
import com.sameal.dd.helper.LogUtils;
import com.sameal.dd.helper.SpUtil;
import com.sameal.dd.helper.TimeUtil;
import com.sameal.dd.http.model.HttpData;
import com.sameal.dd.http.response.ZjfaBean;
import com.sameal.dd.other.IntentKey;
import com.sameal.dd.ui.ddActivity.LoginActivity;
import com.sameal.dd.ui.ddActivity.ZjfaDetailActivity;
import com.sameal.dd.widget.HintLayout;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import java.util.List;

/**
 * @author zhangj
 * @date 2020/12/31 22:37
 * desc 专家方案
 */
public class ZjfaFragment extends MyFragment implements StatusAction {

    RecyclerView recy;
    HintLayout hint;
    SmartRefreshLayout refresh;

    private FootballAdapter footballAdapter;
    private int type;
    private String rid;
    private int author_id;
    private int page = 1;

    public static ZjfaFragment newInstance(int type) {
        return new ZjfaFragment(type);
    }

    public static ZjfaFragment newInstance(int type, String rid) {
        return new ZjfaFragment(type, rid);
    }

    public static ZjfaFragment newInstance(int type, int author_id) {
        return new ZjfaFragment(type, author_id);
    }

    public ZjfaFragment(int type) {
        this.type = type;
    }

    public ZjfaFragment(int type, String rid) {
        this.type = type;
        this.rid = rid;
    }

    public ZjfaFragment(int type, int author_id) {
        this.type = type;
        this.author_id = author_id;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_zjfa;
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
        recy.addItemDecoration(new GridSpacingItemDecoration(1,20,true));
        refresh.setEnableAutoLoadMore(false);
    }

    @Override
    protected void initData() {
        getForumList();
        refresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page++;
                getForumList();
            }
        });
        refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                footballAdapter.clearData();
                getForumList();
            }
        });
    }

    /**
     * 专家方案
     */
    private void getForumList() {
        String api = "api/forum/getForumList?code=" + type;
        if (!TextUtils.isEmpty(rid)) {
            api = "api/forum/getSportForumList?rid=" + rid;
        } else if (author_id != 0) {
            api = "api/forum/getForumList?code=" + type +"&author_id=" + author_id;
        }
        EasyHttp.get(this)
                .api(api + "&p=" + page)
                .request(new HttpCallback<HttpData<List<ZjfaBean>>>(this) {
                    @Override
                    public void onSucceed(HttpData<List<ZjfaBean>> result) {
                        super.onSucceed(result);
                        if (result.getCode() == 1) {
                            footballAdapter.addData(result.getData());
                        }
                        if (footballAdapter.getItemCount() == 0) {
                            showEmpty();
                        } else {
                            showComplete();
                        }
                        refresh.finishLoadMore();
                        refresh.finishRefresh();
                    }
                });
    }

    @Override
    public HintLayout getHintLayout() {
        return hint;
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
            private TextView tvUserName, tvJob, tvBlue, tvRed, tvPercent, tvContent, tv_type_name, tvStartTime, tvTeam, tvCreateTime, tvType, tvMoney;
            private ImageView image;

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
                image = (ImageView) findViewById(R.id.image);
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
                LogUtils.d("111",zjfaBean.getUserinfo().getHit_rate());
                tvContent.setText(zjfaBean.getContent());
                tv_type_name.setText(zjfaBean.getGname());
                tvStartTime.setText(TimeUtil.getDateToString(Long.valueOf(zjfaBean.getoTime())));
                tvTeam.setText(zjfaBean.getItem1_name() + "  vs  " + zjfaBean.getItem2_name());
                tvCreateTime.setText("发布于"+TimeUtil.timeYMDHMinSFigure(Long.valueOf(zjfaBean.getCreate_time() + "000")));
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