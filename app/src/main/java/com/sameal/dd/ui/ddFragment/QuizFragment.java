package com.sameal.dd.ui.ddFragment;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.hjq.base.BaseDialog;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.sameal.dd.R;
import com.sameal.dd.action.StatusAction;
import com.sameal.dd.common.MyAdapter;
import com.sameal.dd.common.MyFragment;
import com.sameal.dd.helper.Identifier;
import com.sameal.dd.helper.LogUtils;
import com.sameal.dd.helper.SpUtil;
import com.sameal.dd.http.model.HttpData;
import com.sameal.dd.http.response.LoginBean;
import com.sameal.dd.http.response.SportDetailBean;
import com.sameal.dd.other.AppConfig;
import com.sameal.dd.other.IntentKey;
import com.sameal.dd.ui.ddActivity.CustomerDetailActivity;
import com.sameal.dd.ui.ddActivity.LoginActivity;
import com.sameal.dd.ui.ddActivity.RechargeActivity;
import com.sameal.dd.ui.ddActivity.ShoppActivity;
import com.sameal.dd.ui.ddActivity.VerifiedActivity;
import com.sameal.dd.ui.ddActivity.WebActivity;
import com.sameal.dd.ui.dialog.BettingDialog;
import com.sameal.dd.widget.HintLayout;
import java.util.ArrayList;
import java.util.List;
import okhttp3.Call;

/**
 * @author zhangj
 * @date 2021/1/8 16:17
 * @desc 描述:竞猜
 */
public class QuizFragment extends MyFragment implements StatusAction {

    RecyclerView recy;
    HintLayout hint;
    private SportDetailBean detailBean;
    private BettingDialog.Builder bettingDialog;
    private List<QuizBean> quizBeans;
    private QuizTypeFragment.QuizBean quizBean;
    private boolean isBetSport = false;

    public static QuizFragment newInstance(SportDetailBean eventsBeans) {
        return new QuizFragment(eventsBeans);
    }

    public static QuizFragment newInstance(SportDetailBean eventsBeans, QuizTypeFragment.QuizBean quizBeans) {
        return new QuizFragment(eventsBeans, quizBeans);
    }

    public QuizFragment(SportDetailBean detailBean) {
        this.detailBean = detailBean;
    }

    public QuizFragment(SportDetailBean detailBean, QuizTypeFragment.QuizBean quizBeans) {
        this.detailBean = detailBean;
        this.quizBean = quizBeans;
    }

    private QuizMainAdapter quizMainAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_quiz;
    }

    @Override
    protected void initView() {
        hint = (HintLayout) findViewById(R.id.hint);
        recy = (RecyclerView) findViewById(R.id.recy);

        quizBeans = new ArrayList<>();
        bettingDialog = new BettingDialog.Builder(getActivity());
        quizMainAdapter = new QuizMainAdapter(getActivity());
        recy.setLayoutManager(new LinearLayoutManager(getActivity()));
        recy.setAdapter(quizMainAdapter);
    }

    @Override
    protected void initData() {
        if (detailBean.getEvents() != null && detailBean.getEvents().size() > 0) {
            showComplete();
            setData();
        } else {
            showEmpty();
        }
        handler.post(task);
        addOnVisible(new OnVisible() {
            @Override
            public void isUsetVisible(boolean isVisible) {
                if (isVisible){
                    LogUtils.d(TAG, "isUsetVisible: "+isVisible);
                    recy.smoothScrollToPosition(0);
                }
            }
        });
    }

    @Override
    public void onStart(Call call) {
        super.onStart(call);
        hideDialog();
    }

    @Override
    public HintLayout getHintLayout() {
        return hint;
    }

    private void setData() {
        if (quizBean == null) {
            for (SportDetailBean.EventsBean eventsBean : detailBean.getEvents()) {
                if (quizBeans.size() == 0) {
                    List<SportDetailBean.EventsBean> events = new ArrayList<>();
                    events.add(eventsBean);
                    quizBeans.add(new QuizBean(eventsBean.getG_name(), eventsBean.getG_id(), events));
                } else {
                    boolean isHave = false;
                    for (QuizBean quizBean : quizBeans) {
                        if (quizBean.getId() == eventsBean.getG_id()) {
                            quizBean.getEventsBeans().add(eventsBean);
                            isHave = true;
                        }
                    }
                    if (!isHave) {
                        List<SportDetailBean.EventsBean> events = new ArrayList<>();
                        events.add(eventsBean);
                        quizBeans.add(new QuizBean(eventsBean.getG_name(), eventsBean.getG_id(), events));
                    }
                }
            }
        } else {
            for (SportDetailBean.EventsBean eventsBean : detailBean.getEvents()) {
                if (quizBeans.size() == 0 && eventsBean.getRound() == quizBean.getRound()) {
                    List<SportDetailBean.EventsBean> events = new ArrayList<>();
                    events.add(eventsBean);
                    quizBeans.add(new QuizBean(eventsBean.getG_name(), eventsBean.getG_id(), events));
                } else if (eventsBean.getRound() == quizBean.getRound()) {
                    boolean isHave = false;
                    for (QuizBean quizBean : quizBeans) {
                        if (quizBean.getId() == eventsBean.getG_id()) {
                            quizBean.getEventsBeans().add(eventsBean);
                            isHave = true;
                        }
                    }
                    if (!isHave) {
                        List<SportDetailBean.EventsBean> events = new ArrayList<>();
                        events.add(eventsBean);
                        quizBeans.add(new QuizBean(eventsBean.getG_name(), eventsBean.getG_id(), events));
                    }
                }
            }
        }
        quizMainAdapter.setData(quizBeans);

    }

    private Handler handler = new Handler();

    private Runnable task = new Runnable() {
        public void run() {
            // TODOAuto-generated method stub
            handler.postDelayed(this, 5000);//设置延迟时间，此处是5秒
            //需要执行的代码
            getDetail();
        }
    };

    /**
     * 赛事详情
     */
    private void getDetail() {
        EasyHttp.get(this)
                .api("api/Sports/getSportDetail?rid=" + detailBean.getRid())
                .request(new HttpCallback<HttpData<SportDetailBean>>(this) {
                    @Override
                    public void onSucceed(HttpData<SportDetailBean> result) {
                        super.onSucceed(result);
                        if (result.getCode() == 1) {
                            detailBean = result.getData();
                            quizBeans.clear();
                            setData();
                        }
                    }
                });
    }

    /**
     * 获取用户信息
     */
    private void getUserInfo() {
        EasyHttp.get(this)
                .api("api/sists/getUserinfo?uid=" + AppConfig.getLoginBean().getId() + "&client_id=" + util.MD5.md5Str(Identifier.getSN()))
                .request(new HttpCallback<HttpData<LoginBean>>(this) {
                    @Override
                    public void onSucceed(HttpData<LoginBean> result) {
                        super.onSucceed(result);
                        if (result.getCode() == 1) {
                            AppConfig.setLoginBean(result.getData());
                        }
                    }
                });
    }

    /**
     * 获取用户信息并拉起下单界面
     */
    private void getUserInfoBetSport(QuizAdapter quizAdapter, int position) {
        EasyHttp.get(this)
                .api("api/sists/getUserinfo?uid=" + AppConfig.getLoginBean().getId() + "&client_id=" + util.MD5.md5Str(Identifier.getSN()))
                .request(new HttpCallback<HttpData<LoginBean>>(this) {
                    @Override
                    public void onSucceed(HttpData<LoginBean> result) {
                        super.onSucceed(result);
                        if (result.getCode() == 1) {
                            AppConfig.setLoginBean(result.getData());
                            isBetSport = true;
                            UpBettingDialog(quizAdapter,position);
                        } else if (result.getMessage().equals("登录已过期")) {
                            toast(result.getMessage());
                            SpUtil.getInstance().setBooleanValue(SpUtil.IS_LOGIN, false);
                            startActivity(LoginActivity.class);
                            finish();
                        }
                    }
                });
    }

    /**
     * 投注
     */
    private void betSport(SportDetailBean.EventsBean eventsBean, String content) {
        EasyHttp.get(this)
                .api("api/Sports/betSport?rid=" + detailBean.getRid() + "&uid=" + AppConfig.getLoginBean().getId() + "&token=" + AppConfig.getLoginBean().getToken()
                        + "&eid=" + eventsBean.getEid() + "&g_id=" + eventsBean.getG_id() + "&round=" + eventsBean.getRound() + "&win=" + eventsBean.getWin()
                        + "&tag=" + eventsBean.getTag() + "&odds=" + eventsBean.getOdds() + "&bet_notes=" + eventsBean.getName() + "&money=" + content)
                .request(new HttpCallback<HttpData<String>>(this) {
                    @Override
                    public void onSucceed(HttpData<String> result) {
                        super.onSucceed(result);
                        if (result.getCode() == 1) {
                            toast("投注成功");
                            getUserInfo();
                            bettingDialog.autoDismiss();
                        } else if (result.getMessage().equals("登录已过期")) {
                            toast(result.getMessage());
                            SpUtil.getInstance().setBooleanValue(SpUtil.IS_LOGIN, false);
                            startActivity(LoginActivity.class);
                            bettingDialog.autoDismiss();
                            finish();
                        }else {
                            toast(result.getMessage());
                        }
                        getDetail();
                    }
                });
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
//                if (message.contains("type") && message.contains("odds")) {
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
//            WsPingBean pingBean = new Gson().fromJson(msg.obj.toString(), WsPingBean.class);
//            for (QuizBean quizBean : quizMainAdapter.getData()) {
//                for (SportDetailBean.EventsBean eventsBean : quizBean.getEventsBeans()) {
//                    for (WsPingBean.DataBean dataBean : pingBean.getData()) {
//                        if (dataBean.getEid() == eventsBean.getEid()) {
//                            LogUtils.d(TAG, "handleMessage: " + quizBean.getName());
//                            LogUtils.d(TAG, "handleMessage: " + eventsBean.getG_name());
//                            eventsBean.setOdds(dataBean.getOdd());
//                            eventsBean.setStatus(dataBean.getStatus());
//                        }
//                    }
//                }
//                quizMainAdapter.notifyDataSetChanged();
//            }
//        }
//    };

    @Override
    public void onDestroy() {
        handler.removeCallbacks(task);
        super.onDestroy();
    }

    class QuizBean {
        private String name;
        private int id;
        private List<SportDetailBean.EventsBean> eventsBeans;

        public QuizBean(String name, int id, List<SportDetailBean.EventsBean> eventsBeans) {
            this.name = name;
            this.id = id;
            this.eventsBeans = eventsBeans;
        }

        public String getName() {
            return name == null ? "" : name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public List<SportDetailBean.EventsBean> getEventsBeans() {
            if (eventsBeans == null) {
                return new ArrayList<>();
            }
            return eventsBeans;
        }

        public void setEventsBeans(List<SportDetailBean.EventsBean> eventsBeans) {
            this.eventsBeans = eventsBeans;
        }
    }

    class QuizMainAdapter extends MyAdapter<QuizBean> {

        public QuizMainAdapter(@NonNull Context context) {
            super(context);
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(R.layout.item_quiz);
        }

        class ViewHolder extends MyAdapter.ViewHolder {

            private TextView tvName;
            private RecyclerView recy;
            private QuizAdapter quizAdapter;

            public ViewHolder(int id) {
                super(id);
                tvName = (TextView) findViewById(R.id.tv_name);
                recy = (RecyclerView) findViewById(R.id.recy);
                recy.setLayoutManager(new GridLayoutManager(getContext(), 2));
            }

            @Override
            public void onBindView(int position) {
                QuizBean quizBean = getItem(position);
                quizAdapter = new QuizAdapter(getContext());
                tvName.setText(quizBean.name);
                quizAdapter.setData(quizBean.getEventsBeans());
                quizAdapter.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(RecyclerView recyclerView, View itemView, int position) {
                        if (quizAdapter.getItem(position).getStatus() == 1 && AppConfig.getLoginBean().getReal_status() == 2) {
                            getUserInfoBetSport(quizAdapter,position);
                        } else if (quizAdapter.getItem(position).getStatus() == 2) {
                            toast("已锁盘");
                        } else if (quizAdapter.getItem(position).getStatus() == 3) {
                            toast("已结束");
                        } else if (quizAdapter.getItem(position).getStatus() == 4) {
                            toast("已关闭");
                        } else if (quizAdapter.getItem(position).getStatus() == 5) {
                            toast("已关闭");
                        } else if (AppConfig.getLoginBean().getReal_status() == 0 || AppConfig.getLoginBean().getReal_status() == 1){
                            toast("请先实名认证");
                            startActivity(VerifiedActivity.class);
                        }
                    }
                });
                recy.setAdapter(quizAdapter);
            }
        }
    }

    private void UpBettingDialog(QuizAdapter quizAdapter, int position) {
        //正常
        bettingDialog
                .setCancel("")
                .setData(detailBean)
                .setData(quizAdapter.getItem(position))
                .setConfirmColor(getColor(R.color.white))
                .setConfirmBackground(R.color.colorAccent)
                .setListener(new BettingDialog.OnListener() {
                    @Override
                    public void onConfirm(BaseDialog dialog, String content) {
                        if (bettingDialog.getConfirm().equals("投注") && Double.valueOf(content) >= 100) {
                            betSport(quizAdapter.getItem(position), content);
                        } else if (bettingDialog.getConfirm().equals("最小单笔投注金额100")) {
                            toast("最小单笔投注金额100");
                        } else if (bettingDialog.getConfirm().equals("最大单笔投注金额50000")) {
                            toast("最大单笔投注金额50000");
                        } else {
                            //进入H5购买代金卷
//                            Intent intent = new Intent(getActivity(), ShoppActivity.class);
//                            intent.putExtra(SpUtil.TYPE,1);
//                            startActivityForResult(intent,0);
                            startActivity(RechargeActivity.class);
                        }
                    }

                    @Override
                    public void jump() {
                        getCustomerDetail();
                    }
                }).show();
    }


    /**
     * 获取问题详情
     */
    private void getCustomerDetail() {
        EasyHttp.get(this)
                .api("api/User/customerDetail?id=" + 11)
                .request(new HttpCallback<HttpData<CustomerDetailActivity.CustomerDetailBean>>(this) {
                    @Override
                    public void onSucceed(HttpData<CustomerDetailActivity.CustomerDetailBean> result) {
                        super.onSucceed(result);
                        if (result.getCode() == 1) {
                            if (result.getData().getProblem() != null){
                                Intent intent = new Intent(getActivity(), WebActivity.class);
                                intent.putExtra(IntentKey.TITLE, result.getData().getProblem().getTitle());
                                intent.putExtra(IntentKey.ADDRESS, result.getData().getProblem().getContent());
                                startActivity(intent);
                            }
                        }
                    }
                });
    }

    class QuizAdapter extends MyAdapter<SportDetailBean.EventsBean> {

        public QuizAdapter(@NonNull Context context) {
            super(context);
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(R.layout.item_betting);
        }

        class ViewHolder extends MyAdapter.ViewHolder {

            private TextView tvResult;
            private TextView tvBetting;

            public ViewHolder(int id) {
                super(id);
                tvResult = (TextView) findViewById(R.id.tv_result);
                tvBetting = (TextView) findViewById(R.id.tv_betting);
            }

            @Override
            public void onBindView(int position) {
                SportDetailBean.EventsBean eventsBean = getItem(position);
                tvResult.setText(eventsBean.getName());
                tvBetting.setText(String.format("%.2f", eventsBean.getOdds() / 1000));
                if (eventsBean.getStatus() == 2) {
                    tvBetting.setText("封盘");
                } else if (eventsBean.getStatus() == 3) {
                    tvBetting.setText("结束");
                } else if (eventsBean.getStatus() == 4) {
                    tvBetting.setText("关闭");
                } else if (eventsBean.getStatus() == 5) {
                    tvBetting.setText("关闭");
                }
            }
        }
    }
}