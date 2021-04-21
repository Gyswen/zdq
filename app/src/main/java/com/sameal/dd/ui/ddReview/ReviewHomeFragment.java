package com.sameal.dd.ui.ddReview;

import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hjq.base.BaseAdapter;
import com.hjq.base.BaseDialog;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.sameal.dd.R;
import com.sameal.dd.action.StatusAction;
import com.sameal.dd.common.MyAdapter;
import com.sameal.dd.common.MyFragment;
import com.sameal.dd.helper.ActivityStackManager;
import com.sameal.dd.helper.LogUtils;
import com.sameal.dd.helper.SpUtil;
import com.sameal.dd.helper.TimeUtil;
import com.sameal.dd.http.model.HttpData;
import com.sameal.dd.http.response.ArticleListBean;
import com.sameal.dd.http.response.BannerListBean;
import com.sameal.dd.http.response.HotLiveListBean;
import com.sameal.dd.other.AppConfig;
import com.sameal.dd.other.IntentKey;
import com.sameal.dd.ui.ddActivity.CustomerDetailActivity;
import com.sameal.dd.ui.ddActivity.LiveActivity;
import com.sameal.dd.ui.ddActivity.LoginActivity;
import com.sameal.dd.ui.ddActivity.VerifiedActivity;
import com.sameal.dd.ui.ddActivity.WebActivity;
import com.sameal.dd.ui.dialog.PrivacyDialog;
import com.sameal.dd.ui.dialog.VerifiedDialog;
import com.sameal.dd.widget.HintLayout;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.bgabanner.BGABanner;

/**
 * 推荐（审核界面）
 */
public class ReviewHomeFragment extends MyFragment implements StatusAction {
    private SmartRefreshLayout refresh;
    private BGABanner bgaBanner;
    private RecyclerView recyHotLive;
    private RecyclerView recyService;
    private HintLayout hint;

    private String channel;
    private int page = 1;

    private HotLiveAdapter hotLiveAdapter;
    private ServiceAdapter serviceAdapter;

    private List<BannerListBean> bannerListBeans;

    public static ReviewHomeFragment newInstance() {
        return new ReviewHomeFragment();
    }

    public ReviewHomeFragment() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.review_fragment_home;
    }

    @Override
    protected void initView() {
        refresh = (SmartRefreshLayout) findViewById(R.id.refresh);
        bgaBanner = (BGABanner) findViewById(R.id.banner);
        recyHotLive = (RecyclerView) findViewById(R.id.recy_hot_live);
        recyService = (RecyclerView) findViewById(R.id.recy_service);
        hint = (HintLayout) findViewById(R.id.hint);

        recyHotLive.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        hotLiveAdapter = new HotLiveAdapter(getActivity());
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
        recyHotLive.setAdapter(hotLiveAdapter);

        recyService.setLayoutManager(new LinearLayoutManager(getActivity()){
            @Override
            public boolean canScrollVertically() {
                //禁止RecyclerView垂直滑动
                return false;
            }
        });
        serviceAdapter = new ServiceAdapter(getActivity());
        serviceAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView recyclerView, View itemView, int position) {
                Intent intent = new Intent(getActivity(),ReviewArticleListActivity.class);
                intent.putExtra("Article",serviceAdapter.getItem(position));
                startActivity(intent);
            }
        });
        recyService.setAdapter(serviceAdapter);
        refresh.setEnableAutoLoadMore(false);
        refresh.setEnableLoadMore(true);
    }

    @Override
    protected void initData() {
        setData();
        refresh.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page++;
                getArticle();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                serviceAdapter.clearData();
                setData();
            }
        });
        if (AppConfig.isLogin() && AppConfig.getLoginBean().getReal_status() == 0) {
            new VerifiedDialog.Builder(getActivity())
                    .setCancel("")
                    .setConfirmVisiable(View.GONE)
                    .setVisibility(R.id.tv_ui_title, View.GONE)
                    .setVisibility(R.id.ll_bottom_btn, View.GONE)
                    .setListener(new VerifiedDialog.OnListener() {
                        @Override
                        public void onConfirm(BaseDialog dialog) {
                            startActivity(VerifiedActivity.class);
                        }
                    }).show();
        }
        if (!SpUtil.getInstance().getBooleanValue(SpUtil.SHARE_APP_TAG)) {
            new PrivacyDialog.Builder(getActivity())
                    .setListener(new PrivacyDialog.Builder.OnListener() {
                        @Override
                        public void onConfirm(BaseDialog dialog, boolean content) {
                            SpUtil.getInstance().setBooleanValue(SpUtil.SHARE_APP_TAG,true);
                            dialog.dismiss();
                        }

                        @Override
                        public void onCancel(BaseDialog dialog) {
                            dialog.dismiss();
                            //销毁所有活Activity
                            ActivityStackManager.getInstance().finishAllActivities();
                        }

                        @Override
                        public void jump(String id) {
                            getCustomerDetail(id);
                        }
                    })
                    .show();
        }
    }

    private void setData() {
        bgaBanner.setAdapter(new BGABanner.Adapter<ImageView, String>() {
            @Override
            public void fillBannerItem(BGABanner banner, ImageView itemView, String model, int position) {
                Glide.with(getActivity())
                        .load(model)
                        .placeholder(R.drawable.avatar_placeholder_ic)
                        .error(R.drawable.hint_error_ic)
                        .centerCrop()
                        .dontAnimate()
                        .into(itemView);
            }

        });

        bgaBanner.setDelegate(new BGABanner.Delegate<ImageView, String>() {
            @Override
            public void onBannerItemClick(BGABanner banner, ImageView itemView, @Nullable String model, int position) {
                if (bannerListBeans != null && bannerListBeans.size() > position) {
                    if (!TextUtils.isEmpty(bannerListBeans.get(position).getLinks()) && bannerListBeans.get(position).getLinks().startsWith("http")) {
                        Intent intent = new Intent(getActivity(), WebActivity.class);
                        intent.putExtra(IntentKey.TITLE, " ");
                        intent.putExtra(IntentKey.ADDRESS, bannerListBeans.get(position).getLinks());
                        startActivity(intent);
                    }
                }
            }
        });
        getBannerList();
        getHotLive();
        getArticle();

        refresh.finishRefresh(1500);
    }

    /**
     * 获取问题详情
     */
    private void getCustomerDetail(String id) {
        EasyHttp.get(this)
                .api("api/User/customerDetail?id=" + id)
                .request(new HttpCallback<HttpData<CustomerDetailActivity.CustomerDetailBean>>(this) {
                    @Override
                    public void onSucceed(HttpData<CustomerDetailActivity.CustomerDetailBean> result) {
                        super.onSucceed(result);
                        if (result.getCode() == 1) {
                            Intent intent = new Intent(getActivity(), WebActivity.class);
                            intent.putExtra(IntentKey.TITLE, result.getData().getProblem().getTitle());
                            intent.putExtra(IntentKey.ADDRESS, result.getData().getProblem().getContent());
                            startActivity(intent);
                        }
                    }
                });
    }

    /**
     * 获取轮播图列表
     */
    private void getBannerList() {
        EasyHttp.get(this)
                .api("api/sists/getBannerList")
                .request(new HttpCallback<HttpData<List<BannerListBean>>>(this) {
                    @Override
                    public void onSucceed(HttpData<List<BannerListBean>> result) {
                        super.onSucceed(result);
                        if (result.getCode() == 1) {
                            bannerListBeans = result.getData();
                            List<String> imgs = new ArrayList<>();
                            for (BannerListBean listBean : result.getData()) {
                                imgs.add(listBean.getImage());
                            }
                            bgaBanner.setData(imgs, imgs);
                        }
                    }
                });
    }

    /**
     * 获取热门直播
     */
    private void getHotLive() {
        EasyHttp.get(this)
                .api("api/sists/getHotLive?type=1&p=1&limit=10")
                .request(new HttpCallback<HttpData<List<HotLiveListBean>>>(this) {
                    @Override
                    public void onSucceed(HttpData<List<HotLiveListBean>> result) {
                        super.onSucceed(result);
                        if (result.getCode() == 1) {
                            hotLiveAdapter.setData(result.getData());
                        }
                    }
                });
    }

    /**
     * 获取新闻咨询
     */
    private void getArticle() {
        EasyHttp.get(this)
                .api("api/sists/getArticleList?p=" + page + "&limit=" + 10)
                .request(new HttpCallback<HttpData<List<ArticleListBean>>>(this) {
                    @Override
                    public void onSucceed(HttpData<List<ArticleListBean>> result) {
                        super.onSucceed(result);
                        if (result.getCode() == 1) {
                            if (page == 1){
                                serviceAdapter.setData(result.getData());
                            } else {
                                serviceAdapter.addData(result.getData());
                            }
                        } else {
                            toast(result.getMessage());
                        }
                        if (serviceAdapter.getItemCount() == 0) {
                            showEmpty();
                        } else {
                            showComplete();
                        }
                        refresh.finishRefresh();
                        refresh.finishLoadMore();
                    }
                });
    }

    @Override
    public HintLayout getHintLayout() {
        return hint;
    }

    private class HotLiveAdapter extends MyAdapter<HotLiveListBean> {

        public HotLiveAdapter(@NonNull Context context) {
            super(context);
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(R.layout.item_home_hot_live);
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

    private class ServiceAdapter extends MyAdapter<ArticleListBean> {

        public ServiceAdapter(@NonNull Context context) {
            super(context);
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(R.layout.item_home_article_live);
        }

        class ViewHolder extends MyAdapter.ViewHolder {
            private TextView tvTitle;
            private TextView tvVirtual;
            private TextView tvTime;
            private ImageView imgTp;

            public ViewHolder(int id) {
                super(id);
                tvTitle = (TextView) findViewById(R.id.tv_title);
                tvVirtual = (TextView) findViewById(R.id.tv_virtual);
                tvTime = (TextView) findViewById(R.id.tv_time);
                imgTp = (ImageView) findViewById(R.id.img_tp);
            }

            @Override
            public void onBindView(int position) {
                ArticleListBean articleListBean = getItem(position);
                tvTitle.setText(articleListBean.getArticle_title());
                tvVirtual.setText(articleListBean.getVirtual_views() + "浏览");
                tvTime.setText(TimeUtil.timeCompareYMDHMinSFigure(articleListBean.getCreate_time() * 1000));
                Glide.with(getActivity())
                        .load(articleListBean.getImage())
                        .into(imgTp);
            }
        }
    }
}
