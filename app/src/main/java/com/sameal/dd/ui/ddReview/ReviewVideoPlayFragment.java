package com.sameal.dd.ui.ddReview;

import android.content.Context;
import android.content.Intent;
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
import com.sameal.dd.common.MyAdapter;
import com.sameal.dd.common.MyFragment;
import com.sameal.dd.http.model.HttpData;
import com.sameal.dd.http.response.HotLiveListBean;
import com.sameal.dd.other.IntentKey;
import com.sameal.dd.widget.HintLayout;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

public class ReviewVideoPlayFragment extends MyFragment implements StatusAction {
    private SmartRefreshLayout src;
    private RecyclerView recy;
    private HintLayout hint;

    private VideoPlayFr videoPlayFr;
    private int page = 1;
    private String vid;

    public static ReviewVideoPlayFragment newInstance(String id) {
        return new ReviewVideoPlayFragment(id);
    }

    public ReviewVideoPlayFragment(String id) {
        vid = id;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.review_video_palay_fragment;
    }

    @Override
    protected void initView() {
        src = (SmartRefreshLayout) findViewById(R.id.src);
        recy = (RecyclerView) findViewById(R.id.recy);
        hint = (HintLayout) findViewById(R.id.hint);

        src.setEnableAutoLoadMore(false);
        src.setEnableLoadMore(true);
    }

    @Override
    protected void initData() {
        videoPlayFr = new VideoPlayFr(getActivity());
        recy.setLayoutManager(new LinearLayoutManager(getActivity()));
        videoPlayFr.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView recyclerView, View itemView, int position) {
                Intent intent = new Intent();
                intent.setClass(getActivity(),ReviewVideoPlayActivity.class);
                intent.putExtra(IntentKey.VIDEO, videoPlayFr.getItem(position).getLive_address());
                intent.putExtra(IntentKey.TITLE, videoPlayFr.getItem(position).getTitle());
                intent.putExtra(IntentKey.ID,videoPlayFr.getItem(position).getId());
                startActivity(intent);
            }
        });
        recy.setAdapter(videoPlayFr);
        getHotLive();
        src.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                videoPlayFr.clearData();
                getHotLive();
            }
        });
        src.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page++;
                getHotLive();
            }
        });

    }

    /**
     * 获取热门直播（视频）
     */
    private void getHotLive() {
        EasyHttp.get(this)
                .api("api/sists/getHotLive?type=" + 2 + "&p=" + page + "&limit=10")
                .request(new HttpCallback<HttpData<List<HotLiveListBean>>>(this) {
                    @Override
                    public void onSucceed(HttpData<List<HotLiveListBean>> result) {
                        super.onSucceed(result);
                        if (result.getCode() == 1) {
                            videoPlayFr.addData(result.getData());
                        } else {
                            toast(result.getMessage());
                            page--;
                        }
                        if (videoPlayFr.getItemCount() == 0) {
                            showEmpty();
                        } else {
                            showComplete();
                        }
                        src.finishRefresh();
                        src.finishLoadMore();
                    }
                });
    }

    @Override
    public HintLayout getHintLayout() {
        return hint;
    }

    class VideoPlayFr extends MyAdapter<HotLiveListBean> {

        public VideoPlayFr(@NonNull Context context) {
            super(context);
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(R.layout.item_video_play_fr);
        }

        class ViewHolder extends BaseAdapter.ViewHolder {
            private ImageView imgTp;
            private TextView tvTitle;

            public ViewHolder(int id) {
                super(id);
                imgTp = (ImageView) findViewById(R.id.img_tp);
                tvTitle = (TextView) findViewById(R.id.tv_title);
            }

            @Override
            public void onBindView(int position) {
                HotLiveListBean liveListBean = getItem(position);
                tvTitle.setText(liveListBean.getTitle());
                Glide.with(getActivity()).load(liveListBean.getImage()).into(imgTp);
            }
        }
    }
}
