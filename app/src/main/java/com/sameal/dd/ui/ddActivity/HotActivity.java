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
import com.sameal.dd.http.response.HotActivityBean;
import com.sameal.dd.other.AppConfig;
import com.sameal.dd.widget.HintLayout;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import java.util.List;

/**
 * @author zhangj
 * @date 2020/12/27 10:50
 * desc 热门活动
 */
public class HotActivity extends MyActivity implements StatusAction {

    RecyclerView recy;
    HintLayout hint;
    SmartRefreshLayout refresh;

    private HotAdapter hotAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_hot;
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
        hotAdapter = new HotAdapter(this);
        hotAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView recyclerView, View itemView, int position) {
                if (hotAdapter.getItem(position).getType().equals("1")) {
                    startActivity(ShareActivity.class);
                } else if (hotAdapter.getItem(position).getType().equals("2")) {
                    if (AppConfig.isLogin()) {
                        startActivity(TaskSignActivity.class);
                    } else {
                        startActivity(LoginActivity.class);
                    }
                } else {
                    //直播
                    startActivity(LiveListActivity.class);
                }
            }
        });
        recy.setAdapter(hotAdapter);
        refresh.setEnableLoadMore(false);
        getHotActivity();
        refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getHotActivity();
            }
        });
    }

    @Override
    public HintLayout getHintLayout() {
        return hint;
    }

    private void getHotActivity() {
        EasyHttp.get(this)
                .api("api/sists/getHotActivity")
                .request(new HttpCallback<HttpData<List<HotActivityBean>>>(this) {
                    @Override
                    public void onSucceed(HttpData<List<HotActivityBean>> result) {
                        super.onSucceed(result);
                        if (result.getCode() == 1) {
                            hotAdapter.setData(result.getData());
                        }
                        if (hotAdapter.getItemCount() == 0) {
                            showEmpty();
                        } else {
                            showComplete();
                        }
                        refresh.finishRefresh();
                    }
                });
    }

    class HotAdapter extends MyAdapter<HotActivityBean> {

        public HotAdapter(@NonNull Context context) {
            super(context);
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(R.layout.item_hot_activity);
        }

        class ViewHolder extends MyAdapter.ViewHolder {

            private ImageView img;
            private TextView tvTitle;
            private TextView tvDesc;

            public ViewHolder(int id) {
                super(id);
                img = (ImageView) findViewById(R.id.img);
                tvTitle = (TextView) findViewById(R.id.tv_title);
                tvDesc = (TextView) findViewById(R.id.tv_desc);
            }

            @Override
            public void onBindView(int position) {
                HotActivityBean bean = getItem(position);
                Glide.with(getContext())
                        .load(bean.getPic())
                        .into(img);
                tvDesc.setText(bean.getMsg());
                tvTitle.setText(bean.getTitle());
            }
        }
    }
}