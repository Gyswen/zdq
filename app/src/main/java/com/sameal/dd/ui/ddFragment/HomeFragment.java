package com.sameal.dd.ui.ddFragment;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hjq.base.BaseAdapter;
import com.hjq.base.BaseDialog;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.sameal.dd.R;
import com.sameal.dd.aop.SingleClick;
import com.sameal.dd.common.MyAdapter;
import com.sameal.dd.common.MyFragment;
import com.sameal.dd.helper.ActivityStackManager;
import com.sameal.dd.helper.LogUtils;
import com.sameal.dd.helper.SpUtil;
import com.sameal.dd.http.model.HttpData;
import com.sameal.dd.http.response.BannerListBean;
import com.sameal.dd.http.response.HotLiveListBean;
import com.sameal.dd.http.response.MessageEvent;
import com.sameal.dd.http.response.UpLoadBean;
import com.sameal.dd.other.AppConfig;
import com.sameal.dd.other.IntentKey;
import com.sameal.dd.ui.ddActivity.CustomerDetailActivity;
import com.sameal.dd.ui.ddActivity.HotActivity;
import com.sameal.dd.ui.ddActivity.LiveActivity;
import com.sameal.dd.ui.ddActivity.LiveListActivity;
import com.sameal.dd.ui.ddActivity.LoginActivity;
import com.sameal.dd.ui.ddActivity.ShareActivity;
import com.sameal.dd.ui.ddActivity.TaskSignActivity;
import com.sameal.dd.ui.ddActivity.VerifiedActivity;
import com.sameal.dd.ui.ddActivity.WebActivity;
import com.sameal.dd.ui.dialog.PrivacyDialog;
import com.sameal.dd.ui.dialog.VerifiedDialog;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.bgabanner.BGABanner;

/**
 * @author zhangj
 * @date 2020/12/16 22:50
 * desc 首页
 */
public class HomeFragment extends MyFragment {
    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    private BGABanner bgaBanner;
    private RecyclerView recyType;
    private RecyclerView recyHotLive;
    private ImageView imgActivity1, imgActivity2, imgActivity3;
    private SmartRefreshLayout refresh;
    private TypeAdapter typeAdapter;
    private HotLiveAdapter hotLiveAdapter;

    private PrivacyDialog.Builder privacyDialog;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView() {
        bgaBanner = (BGABanner) findViewById(R.id.banner);
        recyType = (RecyclerView) findViewById(R.id.recy_type);
        recyHotLive = (RecyclerView) findViewById(R.id.recy_hot_live);
        refresh = (SmartRefreshLayout) findViewById(R.id.refresh);
        imgActivity1 = (ImageView) findViewById(R.id.img_1);
        imgActivity3 = (ImageView) findViewById(R.id.img_3);
        imgActivity2 = (ImageView) findViewById(R.id.img_2);
        refresh.setEnableAutoLoadMore(false);

        recyType.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        typeAdapter = new TypeAdapter(getActivity());
        typeAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView recyclerView, View itemView, int position) {
                if (position != 3) {
                    EventBus.getDefault().post(new MessageEvent(1, position + 1));
                } else {
                    //热门活动
                    startActivity(HotActivity.class);
                }
            }
        });
        recyType.setAdapter(typeAdapter);
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
        refresh.setEnableLoadMore(false);

        setOnClickListener(R.id.img_1, R.id.img_2, R.id.img_3);
    }

    @Override
    protected void initData() {
        setData();
        getBottomPic();
        refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                setData();
                getBottomPic();
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
        LogUtils.d(TAG, "initData: "+SpUtil.getInstance().getBooleanValue(SpUtil.SHARE_APP_TAG) );
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

    @SingleClick
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_1:
                startActivity(ShareActivity.class);
                break;
            case R.id.img_2:
                if (AppConfig.isLogin()) {
                    startActivity(TaskSignActivity.class);
                } else {
                    startActivity(LoginActivity.class);
                }
                break;
            case R.id.img_3:
                startActivity(LiveListActivity.class);
                break;
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
                    if (!TextUtils.isEmpty(bannerListBeans.get(position).getLinks()) &&  bannerListBeans.get(position).getLinks().startsWith("http")){
                        Intent intent = new Intent(getActivity(), WebActivity.class);
                        intent.putExtra(IntentKey.TITLE, " ");
                        intent.putExtra(IntentKey.ADDRESS, bannerListBeans.get(position).getLinks());
                        startActivity(intent);
                    }
                }
            }
        });

        List<String> data = new ArrayList<>();
        data.add("电竞竞猜");
        data.add("体育竞猜");
        data.add("智力竞技");
        data.add("热门活动");
        typeAdapter.setData(data);
        getBannerList();
        getHotLive();
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

    private List<BannerListBean> bannerListBeans;

    /**
     * 获取轮播图列表
     */
    private void getBannerList() {
        EasyHttp.get(this)
                .api("api/sists/getBannerList?code=1")
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
     * 获取底部图
     */
    private void getBottomPic() {
        EasyHttp.get(this)
                .api("api/sists/getHomepic")
                .request(new HttpCallback<HttpData<UpLoadBean>>(this) {
                    @Override
                    public void onSucceed(HttpData<UpLoadBean> result) {
                        super.onSucceed(result);
                        if (result.getCode() == 1) {
                            Glide.with(getActivity())
                                    .load(result.getData().getShare())
                                    .into(imgActivity1);
                            Glide.with(getActivity())
                                    .load(result.getData().getZuo())
                                    .into(imgActivity2);
                            Glide.with(getActivity())
                                    .load(result.getData().getYou())
                                    .into(imgActivity3);
                        }
                    }
                });
    }

    private class TypeAdapter extends MyAdapter<String> {

        public TypeAdapter(@NonNull Context context) {
            super(context);
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(R.layout.item_home_type);
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
                tvName.setText(getItem(position));
                int imgSrc = R.mipmap.icon_gaming;
                if (position == 1) {
                    imgSrc = R.mipmap.icon_sports;
                } else if (position == 2) {
                    imgSrc = R.mipmap.icon_zljj;
                } else if (position == 3) {
                    imgSrc = R.mipmap.icon_hot_activity;
                }
                Glide.with(getActivity()).load(imgSrc)
                        .into(img);
            }
        }
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
}