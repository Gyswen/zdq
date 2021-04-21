package com.sameal.dd.ui.ddActivity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.hjq.base.BaseAdapter;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.sameal.dd.R;
import com.sameal.dd.aop.SingleClick;
import com.sameal.dd.common.MyActivity;
import com.sameal.dd.common.MyAdapter;
import com.sameal.dd.http.model.HttpData;
import com.sameal.dd.http.response.CustomerMainBean;
import com.sameal.dd.other.IntentKey;

/**
 * @author zhangj
 * @date 2020/12/19 19:50
 * desc 帮助与客服
 */
public class ServiceActivity extends MyActivity {

    LinearLayout llKfrx;
    LinearLayout llSwhz;
    LinearLayout llYjfk;
    RecyclerView recy;

    private CustomerMainBean mainBean;
    private AboutAdapter aboutAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_service;
    }

    @Override
    protected void initView() {
        llKfrx = (LinearLayout) findViewById(R.id.ll_kfrx);
        llSwhz = (LinearLayout) findViewById(R.id.ll_swhz);
        llYjfk = (LinearLayout) findViewById(R.id.ll_yjfk);
        recy = (RecyclerView) findViewById(R.id.recy);
        setOnClickListener(R.id.ll_kfrx, R.id.ll_swhz, R.id.ll_yjfk);
    }

    @Override
    protected void initData() {
        recy.setLayoutManager(new LinearLayoutManager(this));
        aboutAdapter = new AboutAdapter(this);
        aboutAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView recyclerView, View itemView, int position) {
                Intent intent = new Intent(ServiceActivity.this, CustomerDetailActivity.class);
                intent.putExtra(IntentKey.TITLE, aboutAdapter.getItem(position).getTitle());
                intent.putExtra(IntentKey.ID, aboutAdapter.getItem(position).getId());
                startActivity(intent);
            }
        });
        recy.setAdapter(aboutAdapter);
        getCustomer();
    }

    @SingleClick
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_kfrx:
                if (mainBean != null && mainBean.getCustomer() != null) {
                    if (mainBean.getCustomer().size() > 0 && mainBean.getCustomer().get(0).getPhone() != null) {
                        callPhone(mainBean.getCustomer().get(0).getPhone());
                    } else {
                        toast("未获取到客服热线");
                    }
                } else {
                    toast("未获取到客服热线");
                }
                break;
            case R.id.ll_swhz:
                if (mainBean != null && mainBean.getCustomer() != null) {
                    if (mainBean.getCustomer().size() > 0 && mainBean.getCustomer().get(1).getPhone() != null) {
//                        callPhone(mainBean.getCustomer().get(1).getPhone());
                        Intent intent = new Intent(ServiceActivity.this, WebActivity.class);
                        intent.putExtra(IntentKey.TITLE, "在线客服");
                        intent.putExtra(IntentKey.ADDRESS, mainBean.getCustomer().get(1).getPhone());
                        startActivity(intent);
                    } else {
                        toast("未获取到商务合作热线");
                    }
                } else {
                    toast("未获取到商务合作热线");
                }
                break;
            case R.id.ll_yjfk:
                startActivity(FeedBackActivity.class);
                break;
        }
    }

    /**
     * 获取客服信息
     */
    private void getCustomer() {
        EasyHttp.get(this)
                .api("api/User/getCustomer")
                .request(new HttpCallback<HttpData<CustomerMainBean>>(this) {
                    @Override
                    public void onSucceed(HttpData<CustomerMainBean> result) {
                        super.onSucceed(result);
                        if (result.getCode() == 1) {
                            mainBean = result.getData();
                            aboutAdapter.setData(mainBean.getProblem());
                        }
                    }
                });
    }


    class AboutAdapter extends MyAdapter<CustomerMainBean.ProblemBean> {

        public AboutAdapter(@NonNull Context context) {
            super(context);
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(R.layout.item_about_mine);
        }

        class ViewHolder extends MyAdapter.ViewHolder {

            private TextView tvContent;

            public ViewHolder(int id) {
                super(id);
                tvContent = (TextView) findViewById(R.id.tv_content);
            }

            @Override
            public void onBindView(int position) {
                CustomerMainBean.ProblemBean mineBean = getItem(position);
                tvContent.setText(mineBean.getTitle());
            }
        }
    }
}