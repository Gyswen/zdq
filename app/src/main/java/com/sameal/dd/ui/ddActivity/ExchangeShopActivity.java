package com.sameal.dd.ui.ddActivity;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.sameal.dd.R;
import com.sameal.dd.action.StatusAction;
import com.sameal.dd.common.MyActivity;
import com.sameal.dd.common.MyAdapter;
import com.sameal.dd.helper.Identifier;
import com.sameal.dd.http.model.HttpData;
import com.sameal.dd.http.response.ExchangeShopBean;
import com.sameal.dd.http.response.LoginBean;
import com.sameal.dd.other.AppConfig;
import com.sameal.dd.widget.HintLayout;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import java.util.List;
import cn.bingoogolapple.bgabanner.BGABanner;

public class ExchangeShopActivity extends MyActivity implements StatusAction {

    TextView tvExMoney;
    RecyclerView recy;
    HintLayout hint;
    SmartRefreshLayout refresh;

    private ExShopAdapter exShopAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_exchange_shop;
    }

    @Override
    protected void initView() {
        tvExMoney = (TextView) findViewById(R.id.tv_ex_money);
        refresh = (SmartRefreshLayout) findViewById(R.id.refresh);
        hint = (HintLayout) findViewById(R.id.hint);
        recy = (RecyclerView) findViewById(R.id.recy);
    }

    @Override
    protected void initData() {
        recy.setLayoutManager(new GridLayoutManager(this, 2));
        exShopAdapter = new ExShopAdapter(this);
        recy.setAdapter(exShopAdapter);
        refresh.setEnableLoadMore(false);
        getUserInfo();
        getExShopList();
        refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getExShopList();
            }
        });
    }

    @Override
    public HintLayout getHintLayout() {
        return hint;
    }

    /**
     * 获取兑换列表
     */
    private void getExShopList() {
        EasyHttp.get(this)
                .api("api/User/getExshopList")
                .request(new HttpCallback<HttpData<List<ExchangeShopBean>>>(this) {
                    @Override
                    public void onSucceed(HttpData<List<ExchangeShopBean>> result) {
                        super.onSucceed(result);
                        exShopAdapter.clearData();
                        if (result.getCode() == 1) {
                            exShopAdapter.setData(result.getData());
                        }
                        if (exShopAdapter.getItemCount() == 0) {
                            showEmpty();
                        } else {
                            showComplete();
                        }
                        refresh.finishRefresh();
                    }
                });
    }

    /**
     * 兑换商品
     */
    private void exShop(int goodId) {
        EasyHttp.get(this)
                .api("api/User/exShopOne?uid=" + AppConfig.getLoginBean().getId() + "&goodsid=" + goodId)
                .request(new HttpCallback<HttpData<String>>(this) {
                    @Override
                    public void onSucceed(HttpData<String> result) {
                        super.onSucceed(result);
                        if (result.getCode() == 1) {
                            getExShopList();
                            getUserInfo();
                        }
                        toast(result.getMessage());
                    }
                });
    }

    /**
     * 获取用户信息
     */
    private void getUserInfo() {
        LoginBean loginBean = AppConfig.getLoginBean();
        EasyHttp.get(this)
                .api("api/sists/getUserinfo?uid=" + loginBean.getId() + "&client_id=" + util.MD5.md5Str(Identifier.getSN()))
                .request(new HttpCallback<HttpData<LoginBean>>(this) {
                    @Override
                    public void onSucceed(HttpData<LoginBean> result) {
                        super.onSucceed(result);
                        if (result.getCode() == 1) {
                            AppConfig.setLoginBean(result.getData());
                            tvExMoney.setText("可兑换余额：" + result.getData().getEx_money());
                        }
                    }
                });
    }

    class ExShopAdapter extends MyAdapter<ExchangeShopBean> {

        public ExShopAdapter(@NonNull Context context) {
            super(context);
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(R.layout.item_exchange_shop);
        }

        class ViewHolder extends MyAdapter.ViewHolder {

            private BGABanner banner;
            private TextView tvTitle, tvContent, tvMoney;
            private AppCompatButton btnExchange;

            public ViewHolder(int id) {
                super(id);
                banner = (BGABanner) findViewById(R.id.banner);
                tvTitle = (TextView) findViewById(R.id.tv_title);
                tvContent = (TextView) findViewById(R.id.tv_content);
                tvMoney = (TextView) findViewById(R.id.tv_money);
                btnExchange = (AppCompatButton) findViewById(R.id.btn_exchange);
            }

            @Override
            public void onBindView(int position) {
                ExchangeShopBean bean = getItem(position);
                banner.setAdapter(new BGABanner.Adapter<ImageView, String>() {
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
                banner.setData(bean.getPics(), bean.getPics());
                tvTitle.setText(bean.getTitle());
                tvContent.setText(Html.fromHtml(bean.getContent()));
                tvMoney.setText("￥" + bean.getMoney());
                btnExchange.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        exShop(bean.getId());
                    }
                });
            }
        }
    }
}