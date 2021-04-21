package com.sameal.dd.ui.ddActivity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.hjq.base.BaseAdapter;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.sameal.dd.R;
import com.sameal.dd.common.MyActivity;
import com.sameal.dd.common.MyAdapter;
import com.sameal.dd.http.model.HttpData;
import com.sameal.dd.http.response.AboutMineBean;
import com.sameal.dd.other.AppConfig;
import com.sameal.dd.other.IntentKey;
import java.util.List;

/**
 * @author zhangj
 * @date 2020/12/19 15:09
 * desc 关于我们
 */
public class AboutMineActivity extends MyActivity {

    TextView tvVersion;
    RecyclerView recy;

    AboutAdapter aboutAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_about_mine;
    }

    @Override
    protected void initView() {
        tvVersion = (TextView) findViewById(R.id.tv_version);
        recy = (RecyclerView) findViewById(R.id.recy);
    }

    @Override
    protected void initData() {
        recy.setLayoutManager(new LinearLayoutManager(this));
        aboutAdapter = new AboutAdapter(this);
        aboutAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView recyclerView, View itemView, int position) {
                Intent intent = new Intent(AboutMineActivity.this, WebActivity.class);
                intent.putExtra(IntentKey.ADDRESS, aboutAdapter.getItem(position).getContent());
                intent.putExtra(IntentKey.TITLE, aboutAdapter.getItem(position).getTitle());
                startActivity(intent);
            }
        });
        recy.setAdapter(aboutAdapter);
        tvVersion.setText(AppConfig.getVersionName());
        getAbout();
    }

    private void getAbout() {
        EasyHttp.get(this)
                .api("api/User/aboutUs")
                .request(new HttpCallback<HttpData<List<AboutMineBean>>>(this) {
                    @Override
                    public void onSucceed(HttpData<List<AboutMineBean>> result) {
                        super.onSucceed(result);
                        if (result.getCode() == 1) {
                            aboutAdapter.setData(result.getData());
                        }
                    }
                });
    }

    class AboutAdapter extends MyAdapter<AboutMineBean> {

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
                AboutMineBean mineBean = getItem(position);
                tvContent.setText(mineBean.getTitle());
            }
        }
    }

}