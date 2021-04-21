package com.sameal.dd.ui.ddActivity;

import android.content.Context;
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
import com.sameal.dd.R;
import com.sameal.dd.action.StatusAction;
import com.sameal.dd.common.MyActivity;
import com.sameal.dd.common.MyAdapter;
import com.sameal.dd.http.model.HttpData;
import com.sameal.dd.http.response.HotLiveListBean;
import com.sameal.dd.other.AppConfig;
import com.sameal.dd.widget.HintLayout;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import java.util.List;

/**
 * @author zhangj
 * @date 2021/1/16 16:58
 * desc 直播列表
 */
public class LiveListActivity extends MyActivity implements StatusAction {

    RecyclerView recy;
    HintLayout hint;
    SmartRefreshLayout refresh;

    private HotLiveAdapter hotLiveAdapter;

    private int p = 1;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_live_list;
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
        hotLiveAdapter = new HotLiveAdapter(this);
        hotLiveAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView recyclerView, View itemView, int position) {
                if (AppConfig.isLogin()) {
//                    LiveActivity.start(getActivity(), hotLiveAdapter.getItem(position).getLive_address(),
//                            hotLiveAdapter.getItem(position).getTitle(), hotLiveAdapter.getItem(position).getId() + "",
//                            hotLiveAdapter.getItem(position).getRid());
                    toast("直播还未开放");
                } else {
                    startActivity(LoginActivity.class);
                }
            }
        });
        recy.setAdapter(hotLiveAdapter);
        getHotLive();
        refresh.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                p++;
                getHotLive();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                p = 1;
                hotLiveAdapter.clearData();
                getHotLive();
            }
        });
    }

    @Override
    public HintLayout getHintLayout() {
        return hint;
    }


    /**
     * 获取热门直播
     */
    private void getHotLive() {
        EasyHttp.get(this)
                .api("api/sists/getHotLives?p=" + p)
                .request(new HttpCallback<HttpData<List<HotLiveListBean>>>(this) {
                    @Override
                    public void onSucceed(HttpData<List<HotLiveListBean>> result) {
                        super.onSucceed(result);
                        if (result.getCode() == 1) {
                            hotLiveAdapter.addData(result.getData());
                        }
                        if (hotLiveAdapter.getItemCount() == 0) {
                            showEmpty();
                        } else {
                            showComplete();
                        }
                        refresh.finishRefresh();
                        refresh.finishLoadMore();
                    }
                });
    }


    private class HotLiveAdapter extends MyAdapter<HotLiveListBean> {

        public HotLiveAdapter(@NonNull Context context) {
            super(context);
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(R.layout.item_hot_lives);
        }

        class ViewHolder extends MyAdapter.ViewHolder {

            private ImageView img;
            private TextView tvName;

            public ViewHolder(int id) {
                super(id);
                img = (ImageView) findViewById(R.id.image);
                tvName = (TextView) findViewById(R.id.tv_name);
            }

            @Override
            public void onBindView(int position) {
                HotLiveListBean liveListBean = getItem(position);
                Glide.with(getActivity())
                        .load(liveListBean.getImage())
                        .into(img);
                tvName.setText(liveListBean.getAuthor());
            }
        }
    }
}