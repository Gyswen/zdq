package com.sameal.dd.ui.ddFragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.hjq.base.BaseAdapter;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.sameal.dd.R;
import com.sameal.dd.action.StatusAction;
import com.sameal.dd.aop.SingleClick;
import com.sameal.dd.common.MyAdapter;
import com.sameal.dd.common.MyFragment;
import com.sameal.dd.http.model.HttpData;
import com.sameal.dd.http.response.ClassifyChildEntry;
import com.sameal.dd.http.response.ClassifyEntry;
import com.sameal.dd.http.response.SportsCaseListBean;
import com.sameal.dd.http.response.SportsTypeBean;
import com.sameal.dd.other.AppConfig;
import com.sameal.dd.other.IntentKey;
import com.sameal.dd.ui.ddActivity.LoginActivity;
import com.sameal.dd.ui.ddActivity.ReservationActivity;
import com.sameal.dd.ui.ddActivity.SportsDetailActivity;
import com.sameal.dd.widget.HintLayout;
import com.samluys.filtertab.FilterResultBean;
import com.samluys.filtertab.FilterTabConfig;
import com.samluys.filtertab.listener.OnFilterToViewListener;
import com.samluys.filtertab.popupwindow.MulSelectPopupwindow;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import java.util.ArrayList;
import java.util.List;
import okhttp3.Call;

/**
 * @author zhangj
 * @date 2020/12/16 23:08
 * desc 竞技
 */
public class SportsFragment extends MyFragment implements StatusAction {

    LinearLayout llOptions;
    RecyclerView recy;
    RecyclerView recyLeft;
    RecyclerView recyTwo;
    HintLayout hint;
    SmartRefreshLayout refresh;
    TextView tvFilter;
    TextView tvReservation;
    RadioButton rbRm;
    RadioButton rbJr;
    RadioButton rbGp;
    RadioButton rbSq;
    RadioGroup rgSport;
    LinearLayout llTwo;
    private SportsAdapter sportsAdapter;

    private int type = 1;
    private int sportType = 1; //体育分类 1热门 2今日 3滚盘 4赛前;

    public static SportsFragment newInstance() {
        return new SportsFragment();
    }

    private List<String> datas = new ArrayList<>();
    private List<ClassifyEntry> classifyEntries = new ArrayList<>();
    private SportsTypeAdapter sportsTypeAdapter;
    private ScheduleListAdapter scheduleListAdapter;
    int sonId;
    private boolean isSwitch = false;//true为第一次进

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_sports;
    }

    @Override
    protected void initView() {
        llOptions = (LinearLayout) findViewById(R.id.ll_options);
        tvFilter = (TextView) findViewById(R.id.tv_filter);
        tvReservation = (TextView) findViewById(R.id.tv_reservation);
        refresh = (SmartRefreshLayout) findViewById(R.id.refresh);
        recyLeft = (RecyclerView) findViewById(R.id.recy_left);
        llTwo = (LinearLayout) findViewById(R.id.ll_two);
        rgSport = (RadioGroup) findViewById(R.id.rg_sport);
        rbRm = (RadioButton) findViewById(R.id.rb_rm);
        rbJr = (RadioButton) findViewById(R.id.rb_jr);
        rbGp = (RadioButton) findViewById(R.id.rb_gp);
        rbSq = (RadioButton) findViewById(R.id.rb_sq);
        hint = (HintLayout) findViewById(R.id.hint);
        recy = (RecyclerView) findViewById(R.id.recy);
        recyTwo = (RecyclerView) findViewById(R.id.recy_two);

        setOnClickListener(R.id.tv_filter, R.id.tv_reservation, R.id.rb_rm, R.id.rb_jr, R.id.rb_gp, R.id.rb_sq);
        sportsTypeAdapter = new SportsTypeAdapter(getActivity());
        sportsTypeAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView recyclerView, View itemView, int position) {
                clickType(position + 1);
            }
        });
        recyLeft.setAdapter(sportsTypeAdapter);
        sportsAdapter = new SportsAdapter(getActivity());
        sportsAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView recyclerView, View itemView, int position) {
                sonId = sportsAdapter.getItem(position).getGid();
                isSwitch = true;
                getCaseSportList();
                recy.setVisibility(View.GONE);
                recyTwo.setVisibility(View.VISIBLE);
                if (type == 2) {
                    rgSport.setVisibility(View.VISIBLE);
                } else {
                    rgSport.setVisibility(View.GONE);
                }
            }
        });
        recy.setAdapter(sportsAdapter);
        scheduleListAdapter = new ScheduleListAdapter(getActivity());
        scheduleListAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView recyclerView, View itemView, int position) {
                if (AppConfig.isLogin()) {
                    Intent intent = new Intent(getActivity(), SportsDetailActivity.class);
                    intent.putExtra(IntentKey.ID, scheduleListAdapter.getItem(position).getRid());
                    startActivity(intent);
                } else {
                    startActivity(LoginActivity.class);
                }
            }
        });
        recyTwo.setAdapter(scheduleListAdapter);
    }

    @Override
    protected void initData() {
        getSportsCase();
        List<ClassifyChildEntry> childEntries = new ArrayList<>();
        childEntries.add(new ClassifyChildEntry(1, "英雄联盟", 1));
        childEntries.add(new ClassifyChildEntry(2, "data2", 0));
        childEntries.add(new ClassifyChildEntry(3, "王者荣耀", 0));
        childEntries.add(new ClassifyChildEntry(4, "csgo", 0));
        childEntries.add(new ClassifyChildEntry(5, "守望先锋", 0));
        childEntries.add(new ClassifyChildEntry(6, "炉石传说", 0));
        childEntries.add(new ClassifyChildEntry(7, "星际争霸2", 0));
        classifyEntries.add(new ClassifyEntry(1, "电竞", 1, childEntries));
        refresh.setEnableLoadMore(false);
        refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                if (recy.getVisibility() == View.VISIBLE) {
                    getSportsCase();
                } else {
                    getCaseSportList();
                }
            }
        });

        handler.post(task);
    }

    @Override
    public void onDestroyView() {
        handler.removeCallbacks(task);
        super.onDestroyView();
    }

    @Override
    public void onStart(Call call) {
        super.onStart(call);
        hideDialog();
    }

    @SingleClick
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_filter:
                MulSelectPopupwindow mulSelectPopupwindow = new MulSelectPopupwindow(getActivity(), classifyEntries, FilterTabConfig.FILTER_TYPE_MUL_SELECT,
                        0, new OnFilterToViewListener() {
                    @Override
                    public void onFilterToView(FilterResultBean resultBean) {
                        toast(resultBean.getName());
                    }

                    @Override
                    public void onFilterListToView(List<FilterResultBean> resultBean) {

                    }
                });
                mulSelectPopupwindow.showAsDropDown(tvFilter);
                break;
            case R.id.tv_reservation:
                if (AppConfig.isLogin()) {
                    startActivity(ReservationActivity.class);
                } else {
                    startActivity(LoginActivity.class);
                }
                break;
            case R.id.rb_rm:
                sportType = 1;
                getCaseSportList();
                break;
            case R.id.rb_jr:
                sportType = 2;
                getCaseSportList();
                break;
            case R.id.rb_gp:
                sportType = 3;
                getCaseSportList();
                break;
            case R.id.rb_sq:
                sportType = 4;
                getCaseSportList();
                break;
            default:
                break;
        }
    }

    /**
     * 点击
     *
     * @param i
     */
    private void clickType(int i) {
        type = i;
        rgSport.setVisibility(View.GONE);
        recyTwo.setVisibility(View.GONE);
        recy.setVisibility(View.VISIBLE);
        for (SportsTypeBean sportsTypeBean : sportsTypeAdapter.getData()) {
            sportsTypeBean.setCheck(false);
        }
        sportsTypeAdapter.getItem(i - 1).setCheck(true);
        sportsTypeAdapter.notifyDataSetChanged();
        sportsAdapter.setData(sportsTypeAdapter.getItem(i - 1).getSon());
        if (sportsAdapter.getItemCount() == 0) {
            showEmpty();
        } else {
            showComplete();
        }
    }

    /**
     * 获取分类
     */
    private void getSportsCase() {
        EasyHttp.get(this)
                .api("api/Sports/getSportsCase")
                .request(new HttpCallback<HttpData<List<SportsTypeBean>>>(this) {
                    @Override
                    public void onSucceed(HttpData<List<SportsTypeBean>> result) {
                        super.onSucceed(result);
                        if (result.getCode() == 1) {
                            result.getData().get(0).setCheck(true);
                            sportsTypeAdapter.setData(result.getData());
                            sportsAdapter.setData(result.getData().get(0).getSon());
                            recy.setVisibility(View.VISIBLE);
                            recyTwo.setVisibility(View.GONE);
                        }
                        if (AppConfig.isClick) {
                            clickType(AppConfig.clickPosi);
                            AppConfig.isClick = false;
                        }
                        refresh.finishRefresh();
                    }
                });
    }

    /**
     * 根据分类ID获取列表数据
     */
    private void getCaseSportList() {
        EasyHttp.get(this)
                .api("api/Sports/getCaseSportList?gid=" + sonId + "&type=" + sportType)
                .request(new HttpCallback<HttpData<List<SportsCaseListBean>>>(this) {
                    @Override
                    public void onSucceed(HttpData<List<SportsCaseListBean>> result) {
                        super.onSucceed(result);
                        scheduleListAdapter.clearData();
                        if (result.getCode() == 1) {
                            scheduleListAdapter.setData(result.getData());
                            if (isSwitch){
                                isSwitch = false;
                                recyTwo.smoothScrollToPosition(0);
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
                    }
                });
    }

    private Handler handler = new Handler();

    private Runnable task = new Runnable() {
        public void run() {
            // TODOAuto-generated method stub
            handler.postDelayed(this, 5 * 1000);//设置延迟时间，此处是5秒
            //需要执行的代码
            if (recyTwo.getVisibility() == View.VISIBLE && scheduleListAdapter != null && scheduleListAdapter.getItemCount() > 0) {
                getCaseSportList();
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        if (AppConfig.isClick) {
            if (sportsTypeAdapter.getItemCount() == 0) {
                getSportsCase();
            } else {
                clickType(AppConfig.clickPosi);
                AppConfig.isClick = false;
            }
        }
    }

//    WsAddressBean.DataBean wsAddressBean;
//
//    /**
//     * 获取socket地址
//     */
//    private void getWsAddress() {
//        EasyHttp.get(this)
//                .api("api/Sports/getWsaddress")
//                .request(new HttpCallback<HttpData<WsAddressBean>>(this) {
//                    @Override
//                    public void onSucceed(HttpData<WsAddressBean> result) {
//                        super.onSucceed(result);
//                        if (result.getCode() == 1) {
//                            wsAddressBean = result.getData().getData();
//                            initWs();
//                        }
//                    }
//                });
//    }
//
//    private JWebSocketClient webSocketClient;
//
//    /**
//     * 初始化通讯
//     */
//    private void initWs() {
//        URI uri = URI.create("ws://" + wsAddressBean.getIp() + ":" + wsAddressBean.getPort() + "?sign=" + wsAddressBean.getSign());
//        webSocketClient = new JWebSocketClient(uri) {
//            @Override
//            public void onMessage(String message) {
//                super.onMessage(message);
//                if (message.contains("type") && message.contains("match")) {
//                    Message msg = new Message();
//                    msg.what = 1;
//                    msg.obj = message;
//                    handler.sendMessage(msg);
//                }
//            }
//        };
//        try {
//            webSocketClient.connectBlocking();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
//
//    Handler handler = new Handler() {
//        @Override
//        public void handleMessage(@NonNull Message msg) {
//            super.handleMessage(msg);
//            WsMatchBean pingBean = new Gson().fromJson(msg.obj.toString(), WsMatchBean.class);
//
//            for (SportsCaseListBean caseListBean : scheduleListAdapter.getData()) {
//                if (caseListBean.getRid().equals(pingBean.getData().getRid())) {
//                    LogUtils.d(TAG, "handleMessage: " + caseListBean.getItem1_name() + ":" + caseListBean.getItem2_name());
//                    LogUtils.d(TAG, "handleMessage: " + caseListBean.getStatus());
//                    caseListBean.setStatus(pingBean.getData().getStatus());
//                }
//            }
//            scheduleListAdapter.notifyDataSetChanged();
//        }
//    };

    @Override
    public HintLayout getHintLayout() {
        return hint;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private class SportsTypeAdapter extends MyAdapter<SportsTypeBean> {

        public SportsTypeAdapter(@NonNull Context context) {
            super(context);
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(R.layout.item_sports_type);
        }

        class ViewHolder extends MyAdapter.ViewHolder {

            private LinearLayout ll;
            private ImageView img;
            private TextView tvType;
            private TextView tvNum;

            public ViewHolder(int id) {
                super(id);
                ll = (LinearLayout) findViewById(R.id.ll_gaming);
                img = (ImageView) findViewById(R.id.img_gaming);
                tvType = (TextView) findViewById(R.id.tv_dj);
                tvNum = (TextView) findViewById(R.id.tv_dj_num);
            }

            @Override
            public void onBindView(int position) {
                SportsTypeBean sportsTypeBean = getItem(position);
                tvType.setText(sportsTypeBean.getTitle());
                tvNum.setText(sportsTypeBean.getSoncount() + "");
                if (sportsTypeBean.isCheck()) {
                    ll.setBackgroundResource(R.drawable.bg_sports_select);
                    tvType.setTextColor(getColor(R.color.white));
                    tvNum.setTextColor(getColor(R.color.white));
                    if (position == 0) {
                        img.setImageResource(R.mipmap.icon_gaming_select);
                    } else if (position == 1) {
                        img.setImageResource(R.mipmap.icon_sport_select);
                    } else if (position == 2) {
                        img.setImageResource(R.mipmap.icon_game_select);
                    } else {
                        img.setImageResource(R.mipmap.icon_live_select);
                    }
                } else {
                    ll.setBackgroundResource(R.drawable.bg_sports_unselect);
                    tvType.setTextColor(getColor(R.color.textColor));
                    tvNum.setTextColor(getColor(R.color.textColor));
                    if (position == 0) {
                        img.setImageResource(R.mipmap.icon_gaming_unselect);
                    } else if (position == 1) {
                        img.setImageResource(R.mipmap.icon_sport_unselect);
                    } else if (position == 2) {
                        img.setImageResource(R.mipmap.icon_game_unselect);
                    } else {
                        img.setImageResource(R.mipmap.icon_live_unselect);
                    }
                }
            }
        }
    }

    private class SportsAdapter extends MyAdapter<SportsTypeBean.SonBean> {

        public SportsAdapter(@NonNull Context context) {
            super(context);
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(R.layout.item_sports);
        }

        class ViewHolder extends MyAdapter.ViewHolder {

            RelativeLayout rlType;
            TextView tvNum;
            CardView cardSports;
            private ImageView img;

            public ViewHolder(int id) {
                super(id);
                rlType = (RelativeLayout) findViewById(R.id.rl_type);
                tvNum = (TextView) findViewById(R.id.tv_num);
                img = (ImageView) findViewById(R.id.img);
                cardSports = (CardView) findViewById(R.id.card_sports);
            }

            @Override
            public void onBindView(int position) {
                SportsTypeBean.SonBean sonBean = getItem(position);
                Glide.with(getContext())
                        .load(sonBean.getImage())
                        .error(R.mipmap.football_bg)
                        .placeholder(R.mipmap.football_bg)
                        .into(img);
                tvNum.setText(sonBean.getList_count() + "");
//                cardSports.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        sonId = sportsAdapter.getItem(position).getGid();
//                        getCaseSportList();
//                        recy.setVisibility(View.GONE);
//                        recyTwo.setVisibility(View.VISIBLE);
//                        if (type == 2) {
//                            rgSport.setVisibility(View.VISIBLE);
//                        } else {
//                            rgSport.setVisibility(View.GONE);
//                        }
//                    }
//                });
            }
        }
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