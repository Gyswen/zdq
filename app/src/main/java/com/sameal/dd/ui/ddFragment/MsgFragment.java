package com.sameal.dd.ui.ddFragment;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.Gson;
import com.hjq.base.BaseAdapter;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.sameal.dd.R;
import com.sameal.dd.action.StatusAction;
import com.sameal.dd.common.MyAdapter;
import com.sameal.dd.common.MyFragment;
import com.sameal.dd.helper.TimeUtil;
import com.sameal.dd.http.model.HttpData;
import com.sameal.dd.http.response.MsgBean;
import com.sameal.dd.other.AppConfig;
import com.sameal.dd.other.IntentKey;
import com.sameal.dd.ui.ddActivity.MsgDetailActivity;
import com.sameal.dd.widget.HintLayout;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import java.util.List;

/**
 * @author zhangj
 * @date 2020/12/18 21:20
 * desc 消息列表
 */
public class MsgFragment extends MyFragment implements StatusAction {

    RecyclerView recy;
    HintLayout hint;
    SmartRefreshLayout refresh;
    private int type;

    private MsgAdapter msgAdapter;

    public static MsgFragment newInstance(int label) {
        return new MsgFragment(label);
    }

    public MsgFragment(int type) {
        this.type = type;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_msg;
    }

    @Override
    protected void initView() {
        refresh = (SmartRefreshLayout) findViewById(R.id.refresh);
        hint = (HintLayout) findViewById(R.id.hint);
        recy = (RecyclerView) findViewById(R.id.recy);

        recy.setLayoutManager(new LinearLayoutManager(getActivity()));
        msgAdapter = new MsgAdapter(getActivity());
        msgAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView recyclerView, View itemView, int position) {
                Intent intent = new Intent(getActivity(), MsgDetailActivity.class);
                intent.putExtra(IntentKey.DATA, new Gson().toJson(msgAdapter.getItem(position)));
                startActivity(intent);
            }
        });
        recy.setAdapter(msgAdapter);
        refresh.setEnableLoadMore(false);
    }

    @Override
    protected void initData() {
        getMsgList();
        refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getMsgList();
            }
        });
    }

    /**
     * 获取消息列表
     */
    private void getMsgList() {
        EasyHttp.get(this)
                .api("api/User/getMessage?type=" + type + "&uid=" + AppConfig.getLoginBean().getId())
                .request(new HttpCallback<HttpData<List<MsgBean>>>(this) {
                    @Override
                    public void onSucceed(HttpData<List<MsgBean>> result) {
                        super.onSucceed(result);
                        msgAdapter.clearData();
                        if (result.getCode() == 1) {
                            msgAdapter.addData(result.getData());
                        }
                        if (msgAdapter.getItemCount() == 0) {
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

    class MsgAdapter extends MyAdapter<MsgBean> {

        public MsgAdapter(@NonNull Context context) {
            super(context);
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(R.layout.item_msg);
        }

        class ViewHolder extends MyAdapter.ViewHolder {

            private TextView tvTitle, tvContent, tvTime, tvType;

            public ViewHolder(int id) {
                super(id);
                tvTitle = (TextView) findViewById(R.id.tv_title);
                tvContent = (TextView) findViewById(R.id.tv_content);
                tvTime = (TextView) findViewById(R.id.tv_time);
                tvType = (TextView) findViewById(R.id.tv_type);
            }

            @Override
            public void onBindView(int position) {
                MsgBean bean = getItem(position);

                tvTitle.setText(bean.getTitle());
                tvContent.setText(Html.fromHtml(bean.getContent()));
                tvTime.setText(TimeUtil.timeCompareYMDHMinSFigure(Long.valueOf(bean.getAddtime() + "000")));
                if (bean.getType() == 1) {
                    tvType.setText(R.string.notice);
                } else {
                    tvType.setText(R.string.order);
                }
            }
        }
    }
}