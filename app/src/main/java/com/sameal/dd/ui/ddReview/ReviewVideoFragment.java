package com.sameal.dd.ui.ddReview;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hjq.base.BaseAdapter;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.sameal.dd.R;
import com.sameal.dd.action.StatusAction;
import com.sameal.dd.common.MyAdapter;
import com.sameal.dd.common.MyFragment;
import com.sameal.dd.helper.GridSpacingItemDecoration;
import com.sameal.dd.http.model.HttpData;
import com.sameal.dd.http.response.HotLiveListBean;
import com.sameal.dd.other.IntentKey;
import com.sameal.dd.widget.HintLayout;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.List;

/**
 * 视频（审核）
 */

public class ReviewVideoFragment extends MyFragment implements StatusAction {
    private SmartRefreshLayout refresh;
    private HintLayout hint;
    private RecyclerView recyVideo;
    private int page = 1;
    private int screenWidth;

    private VideoAdapter videoAdapter;

    public static ReviewVideoFragment newInstance() {
        return new ReviewVideoFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.review_video_fragment;
    }

    @Override
    protected void initView() {
        recyVideo = (RecyclerView) findViewById(R.id.recy_video);
        refresh = (SmartRefreshLayout) findViewById(R.id.refresh);
        hint = (HintLayout) findViewById(R.id.hint);

        screenWidth = getActivity().getWindowManager().getDefaultDisplay().getWidth();

        refresh.setEnableAutoLoadMore(false);
        refresh.setEnableLoadMore(true);
    }

    @Override
    protected void initData() {
        videoAdapter = new VideoAdapter(getActivity());
        recyVideo.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyVideo.addItemDecoration(new GridSpacingItemDecoration(2,10,true));
        videoAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView recyclerView, View itemView, int position) {
                Intent intent = new Intent();
                intent.setClass(getActivity(),ReviewVideoPlayActivity.class);
                intent.putExtra(IntentKey.VIDEO, videoAdapter.getItem(position).getLive_address());
                intent.putExtra(IntentKey.TITLE, videoAdapter.getItem(position).getTitle());
                intent.putExtra(IntentKey.ID,videoAdapter.getItem(position).getId());
                startActivity(intent);
            }
        });
        recyVideo.setAdapter(videoAdapter);
        refresh.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page++;
                getHotLive();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                videoAdapter.clearData();
                getHotLive();
            }
        });
        getHotLive();
    }

    @Override
    public HintLayout getHintLayout() {
        return hint;
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
                            videoAdapter.addData(result.getData());
                        } else {
                            toast(result.getMessage());
                            page--;
                        }
                        if (videoAdapter.getItemCount() == 0) {
                            showEmpty();
                        } else {
                            showComplete();
                        }
                        refresh.finishRefresh();
                        refresh.finishLoadMore();
                    }
                });
    }

    class VideoAdapter extends MyAdapter<HotLiveListBean> {

        public VideoAdapter(@NonNull Context context) {
            super(context);
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(R.layout.item_review_video);
        }

        class ViewHolder extends BaseAdapter.ViewHolder {
            private ImageView imgTp;
            private TextView tvTitle;

            public ViewHolder(int id) {
                super(id);
                imgTp = (ImageView) findViewById(R.id.img_tp);
                tvTitle = (TextView) findViewById(R.id.tv_title);
                CardView.LayoutParams params = (CardView.LayoutParams) imgTp.getLayoutParams();
                params.width = screenWidth/2;
                params.height = (screenWidth/3)/5*4;
                imgTp.setLayoutParams(params);
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
