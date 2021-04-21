package com.sameal.dd.ui.ddFragment;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.sameal.dd.R;
import com.sameal.dd.action.StatusAction;
import com.sameal.dd.common.MyAdapter;
import com.sameal.dd.common.MyFragment;
import com.sameal.dd.helper.LogUtils;
import com.sameal.dd.helper.TimeUtil;
import com.sameal.dd.http.model.HttpData;
import com.sameal.dd.http.response.GuessListBean;
import com.sameal.dd.other.AppConfig;
import com.sameal.dd.widget.HintLayout;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import java.util.List;

/**
 * @author zhangj
 * @date 2020/12/18 21:39
 * desc 我的竞猜列表
 */
public class GuessListFragment extends MyFragment implements StatusAction {

    RecyclerView recy;
    HintLayout hint;
    SmartRefreshLayout refresh;

    private int type;
    private int page = 1;
    private ScheduleListAdapter scheduleListAdapter;

    public static GuessListFragment newInstance(int label) {
        return new GuessListFragment(label);
    }

    public GuessListFragment(int type) {
        this.type = type;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_guess_list;
    }

    @Override
    protected void initView() {
        refresh = (SmartRefreshLayout) findViewById(R.id.refresh);
        hint = (HintLayout) findViewById(R.id.hint);
        recy = (RecyclerView) findViewById(R.id.recy);

        recy.setLayoutManager(new LinearLayoutManager(getActivity()));
        scheduleListAdapter = new ScheduleListAdapter(getActivity());
        recy.setAdapter(scheduleListAdapter);
        refresh.setEnableLoadMore(true);
        refresh.setEnableAutoLoadMore(false);
    }

    @Override
    protected void initData() {
        getMyGuess();
        refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                scheduleListAdapter.clearData();
                getMyGuess();
            }
        });
        refresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page++;
                getMyGuess();
            }
        });
    }

    @Override
    public HintLayout getHintLayout() {
        return hint;
    }

    /**
     * 获取我的竞猜
     */
    private void getMyGuess() {
        EasyHttp.get(this)
                .api("api/User/getMybets?uid=" + AppConfig.getLoginBean().getId() + "&code=" + type + "&p=" + page)
                .request(new HttpCallback<HttpData<List<GuessListBean>>>(this) {
                    @Override
                    public void onSucceed(HttpData<List<GuessListBean>> result) {
                        super.onSucceed(result);
                        if (result.getCode() == 1) {
                            if (1 == page){
                                scheduleListAdapter.setData(result.getData());
                            }else {
                                scheduleListAdapter.addData(result.getData());
                            }
                        } else {
                            toast(result.getMessage());
                        }
                        if (scheduleListAdapter.getItemCount() == 0) {
                            showEmpty();
                        } else {
                            showComplete();
                        }
                        refresh.finishRefresh();
                        refresh.finishLoadMore();
                    }
                });
    }

    class ScheduleListAdapter extends MyAdapter<GuessListBean> {

        public ScheduleListAdapter(@NonNull Context context) {
            super(context);
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(R.layout.item_guess);
        }

        class ViewHolder extends MyAdapter.ViewHolder {

            TextView tvTitle;
            TextView tvState;
            ImageView imgAvatar;
            TextView tvTime;
            TextView tvNo;
            TextView tvWanfa;
            TextView tvWocai;
            TextView tvJingcai;
            TextView tvResult;

            public ViewHolder(int id) {
                super(id);
                imgAvatar = (ImageView) findViewById(R.id.img_avatar);
                tvTitle = (TextView) findViewById(R.id.tv_title);
                tvState = (TextView) findViewById(R.id.tv_state);
                tvTime = (TextView) findViewById(R.id.tv_time);
                tvNo = (TextView) findViewById(R.id.tv_no);
                tvWanfa = (TextView) findViewById(R.id.tv_wanfa);
                tvWocai = (TextView) findViewById(R.id.tv_wocai);
                tvJingcai = (TextView) findViewById(R.id.tv_jingcai);
                tvResult = (TextView) findViewById(R.id.tv_result);
            }

            @Override
            public void onBindView(int position) {
                GuessListBean data = getItem(position);

                tvTitle.setText(data.getItem1_name() + " VS " + data.getItem2_name());
                tvTime.setText(TimeUtil.timeYMDHMinSFigure(Long.valueOf(data.getATime() + "000")));
                tvNo.setText(data.getEid() + "");
                for (GuessListBean.EventsBean eventsBean : data.getEvents()) {
                    if (eventsBean.getEid() == data.getEid()) {
                        LogUtils.d(TAG, "onBindView: " + new Gson().toJson(eventsBean));
                        LogUtils.d(TAG, "position: "+position);
                        tvWanfa.setText("玩法：" + eventsBean.getG_name());
                        tvWocai.setText("我猜：" + eventsBean.getName());
                        tvJingcai.setText("竞猜：" + data.getMoney() + "椰糖@" + String.format("%.2f", Double.valueOf(data.getOdds() / 1000)));
                        break;
                    }else {
                        tvWanfa.setText("玩法：--");
                        tvWocai.setText("我猜：--");
                        tvJingcai.setText("竞猜：--");
                    }
                }
//                tvState.setText(eventsBean.getWin() == -1 ? "未公布" : (eventsBean.getWin() == 0 ? "未中奖" : "已中奖"));

                if (data.getWin() == -1) {
                    tvResult.setText("结果：未有结果");
                    tvState.setText("待公布");
                } else if (data.getWin() == 0) {
                    tvResult.setText("结果：未中奖");
                    tvState.setText("未中奖");
                } else if (data.getWin() == 1) {
                    double money = Double.valueOf(data.getMoney()) * data.getOdds()/1000;
                    tvResult.setText("结果：中奖+" + money + "椰糖");
                    tvState.setText("已中奖");
                } else if (data.getWin() == 2){
                    tvResult.setText("结果：已退单");
                    tvState.setText("已退单");
                }
                switch (data.getGid()) {
                    case 1:
                        Glide.with(getContext())
                                .load(R.mipmap.lol_default)
                                .into(imgAvatar);
                        break;
                    case 2:
                        Glide.with(getContext())
                                .load(R.mipmap.dota2_default)
                                .into(imgAvatar);
                        break;
                    case 3:
                        Glide.with(getContext())
                                .load(R.mipmap.csgo_default)
                                .into(imgAvatar);
                        break;
                    case 4:
                        Glide.with(getContext())
                                .load(R.mipmap.kog_default)
                                .into(imgAvatar);
                        break;
                    case 5:
                        Glide.with(getContext())
                                .load(R.mipmap.xingji_default)
                                .into(imgAvatar);
                        break;
                    case 6:
                        Glide.with(getContext())
                                .load(R.mipmap.shouwang_default)
                                .into(imgAvatar);
                        break;
                    case 7:
                        Glide.with(getContext())
                                .load(R.mipmap.basketball_default)
                                .into(imgAvatar);
                        break;
                    case 8:
                        Glide.with(getContext())
                                .load(R.mipmap.soccer)
                                .into(imgAvatar);
                        break;
                }
            }
        }
    }
}