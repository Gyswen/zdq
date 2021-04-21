package com.sameal.dd.ui.ddFragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.hjq.base.BaseAdapter;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.sameal.dd.R;
import com.sameal.dd.action.StatusAction;
import com.sameal.dd.common.MyAdapter;
import com.sameal.dd.common.MyFragment;
import com.sameal.dd.helper.TimeUtil;
import com.sameal.dd.http.model.HttpData;
import com.sameal.dd.http.response.SportsCaseListBean;
import com.sameal.dd.other.AppConfig;
import com.sameal.dd.other.IntentKey;
import com.sameal.dd.ui.ddActivity.LoginActivity;
import com.sameal.dd.ui.ddActivity.SportsDetailActivity;
import com.sameal.dd.ui.dialog.WaitDialog;
import com.sameal.dd.widget.HintLayout;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import java.util.List;

/**
 * @author zhangj
 * @date 2020/12/22 20:35
 * desc 赛程列表
 */
public class ScheduleListFragment extends MyFragment implements StatusAction {

    ImageButton imgNavLeft;
    TextView tvDate;
    TextView tvNum;
    ImageButton imgNavRight;
    RecyclerView recy;
    HintLayout hint;
    SmartRefreshLayout refresh;

    private int currentdate = 0;
    WaitDialog.Builder waitDialog;
    private ScheduleListAdapter listAdapter;
    private int type = 0;

    private int code = 0;

    public static ScheduleListFragment newInstance(int code) {
        return new ScheduleListFragment(code);
    }

    public ScheduleListFragment(int code) {
        this.code = code;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_schedule_list;
    }

    @Override
    protected void initView() {
        imgNavLeft = (ImageButton) findViewById(R.id.img_nav_left);
        tvDate = (TextView) findViewById(R.id.tv_date);
        tvNum = (TextView) findViewById(R.id.tv_num);
        imgNavRight = (ImageButton) findViewById(R.id.img_nav_right);
        refresh = (SmartRefreshLayout) findViewById(R.id.refresh);
        hint = (HintLayout) findViewById(R.id.hint);
        recy = (RecyclerView) findViewById(R.id.recy);
        setOnClickListener(R.id.img_nav_left, R.id.img_nav_right);

        waitDialog = new WaitDialog.Builder(getActivity());
        recy.setLayoutManager(new LinearLayoutManager(getActivity()));
        listAdapter = new ScheduleListAdapter(getActivity());
        listAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView recyclerView, View itemView, int position) {
                if (!AppConfig.isCheck()) {
                    if (AppConfig.isLogin()) {
                        Intent intent = new Intent(getActivity(), SportsDetailActivity.class);
                        intent.putExtra(IntentKey.ID, listAdapter.getItem(position).getRid());
                        startActivity(intent);
                    } else {
                        startActivity(LoginActivity.class);
                    }
                }
            }
        });
        recy.setAdapter(listAdapter);
        refresh.setEnableAutoLoadMore(false);
        refresh.setEnableLoadMore(false);
    }

    @Override
    protected void initData() {
        refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                listAdapter.clearData();
                getScheduleList();
            }
        });
        tvDate.setText(TimeUtil.getTime());
        getScheduleList();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_nav_left:
                waitDialog.show();
                tvDate.setText(TimeUtil.currentDatePre(tvDate.getText().toString()));
                listAdapter.clearData();
                getScheduleList();
                CountDownTimer timer = new CountDownTimer(1500, 1500) {
                    @Override
                    public void onTick(long l) {

                    }

                    @Override
                    public void onFinish() {
                        waitDialog.dismiss();
                    }
                }.start();
                break;
            case R.id.img_nav_right:
                waitDialog.show();
                tvDate.setText(TimeUtil.currentDateNext(tvDate.getText().toString()));
                listAdapter.clearData();
                getScheduleList();
                timer = new CountDownTimer(1500, 1500) {
                    @Override
                    public void onTick(long l) {

                    }

                    @Override
                    public void onFinish() {
                        waitDialog.dismiss();
                    }
                }.start();
                break;
        }
    }

    @Override
    public HintLayout getHintLayout() {
        return hint;
    }

    public void refreshData(int type) {
        this.type = type;
        getScheduleList();
    }

    public void getScheduleList() {
        EasyHttp.get(this)
                .api("api/Sports/getScheduleList?code=" + code + "&type=" + type + "&timestamps=" +
                        TimeUtil.StringToDate(tvDate.getText().toString(), "yyyy-MM-dd").toString().substring(0, 10))
                .request(new HttpCallback<HttpData<List<SportsCaseListBean>>>(this) {
                    @Override
                    public void onSucceed(HttpData<List<SportsCaseListBean>> result) {
                        super.onSucceed(result);
                        if (result.getCode() == 1) {
                            listAdapter.setData(result.getData());
                        } else {
                            toast(result.getMessage());
                        }
                        if (listAdapter.getItemCount() == 0) {
                            showEmpty();
                        } else {
                            showComplete();
                        }
                        tvNum.setText(listAdapter.getItemCount() + "场比赛");
                        refresh.finishRefresh();
                    }
                });
    }

    class ScheduleListAdapter extends MyAdapter<SportsCaseListBean> {

        public ScheduleListAdapter(@NonNull Context context) {
            super(context);
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(R.layout.item_schedule);
        }

        class ViewHolder extends MyAdapter.ViewHolder {

            TextView tvStartName;
            TextView tvStartNum;
            ImageView imgStart;
            TextView tvEndName;
            TextView tvEndNum;
            ImageView imgEnd;
            TextView tvTime;
            TextView tvName;
            TextView tvStatus;
            TextView tvLeInfoName;
            private ImageView imgType;

            public ViewHolder(int id) {
                super(id);
                tvTime = (TextView) findViewById(R.id.tv_time);
                tvName = (TextView) findViewById(R.id.tv_name);
                tvStartName = (TextView) findViewById(R.id.tv_start_name);
                tvStartNum = (TextView) findViewById(R.id.tv_start_num);
                imgStart = (ImageView) findViewById(R.id.img_start);
                imgEnd = (ImageView) findViewById(R.id.img_end);
                tvEndName = (TextView) findViewById(R.id.tv_end_name);
                tvEndNum = (TextView) findViewById(R.id.tv_end_num);
                tvStatus = (TextView) findViewById(R.id.tv_status);
                imgType = (ImageView) findViewById(R.id.img_type);
                tvLeInfoName = (TextView) findViewById(R.id.tv_leInfo_name);
            }

            @Override
            public void onBindView(int position) {
                SportsCaseListBean data = getItem(position);
                tvTime.setText(data.getoStr());
                if (data.getStatus() == 1) {
                    Drawable drawable = getDrawable(R.drawable.bg_circle_color_fe701f);
                    tvStatus.setText("未开始");
                    tvStatus.setBackground(drawable);
                } else if (data.getStatus() == 2) {
                    Drawable drawable = getDrawable(R.drawable.bg_circle_color_fc3030);
                    tvStatus.setText("进行中");
                    tvStatus.setBackground(drawable);
                } else if (data.getStatus() == 3) {
                    Drawable drawable = getDrawable(R.drawable.bg_circle_color_dcdcdc);
                    tvStatus.setText("已结束");
                    tvStatus.setBackground(drawable);
                } else {
                    Drawable drawable = getDrawable(R.drawable.bg_circle_color_dcdcdc);
                    tvStatus.setText("已关闭");
                    tvStatus.setBackground(drawable);
                }
                tvName.setText(data.getGname());
                tvLeInfoName.setText(data.getLeagueInfo().getName());
                tvStartName.setText(data.getItem1_name());
                tvEndName.setText(data.getItem2_name());
                String score = data.getRaceResult().getScore();
                tvStartNum.setText(score.split("-").length > 0 ? score.split("-")[0] : "0");
                tvEndNum.setText(score.split("-").length > 1 ? score.split("-")[1] : "0");
                Glide.with(getContext())
                        .load(data.getItem1_logo())
                        .error(R.mipmap.icon_contact_avatar_default)
                        .placeholder(R.mipmap.icon_contact_avatar_default)
                        .into(imgStart);
                Glide.with(getContext())
                        .load(data.getItem2_logo())
                        .error(R.mipmap.icon_contact_avatar_default)
                        .placeholder(R.mipmap.icon_contact_avatar_default)
                        .into(imgEnd);
                switch (data.getGid()) {
                    case 1:
                        Glide.with(getContext())
                                .load(R.mipmap.lol_default)
                                .into(imgType);
                        break;
                    case 2:
                        Glide.with(getContext())
                                .load(R.mipmap.dota2_default)
                                .into(imgType);
                        break;
                    case 3:
                        Glide.with(getContext())
                                .load(R.mipmap.csgo_default)
                                .into(imgType);
                        break;
                    case 4:
                        Glide.with(getContext())
                                .load(R.mipmap.kog_default)
                                .into(imgType);
                        break;
                    case 5:
                        Glide.with(getContext())
                                .load(R.mipmap.xingji_default)
                                .into(imgType);
                        break;
                    case 6:
                        Glide.with(getContext())
                                .load(R.mipmap.shouwang_default)
                                .into(imgType);
                        break;
                    case 7:
                        Glide.with(getContext())
                                .load(R.mipmap.basketball_default)
                                .into(imgType);
                        break;
                    case 8:
                        Glide.with(getContext())
                                .load(R.mipmap.soccer)
                                .into(imgType);
                        break;
                }
            }
        }
    }
}