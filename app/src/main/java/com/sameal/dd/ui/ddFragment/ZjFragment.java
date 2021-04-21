package com.sameal.dd.ui.ddFragment;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.hjq.base.BaseAdapter;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.hjq.widget.view.CircleImageView;
import com.sameal.dd.R;
import com.sameal.dd.common.MyAdapter;
import com.sameal.dd.common.MyFragment;
import com.sameal.dd.helper.BigDecimalUtils;
import com.sameal.dd.helper.SpUtil;
import com.sameal.dd.http.model.HttpData;
import com.sameal.dd.http.response.ZjBean;
import com.sameal.dd.other.AppConfig;
import com.sameal.dd.other.IntentKey;
import com.sameal.dd.ui.ddActivity.LoginActivity;
import com.sameal.dd.ui.ddActivity.ZjDetailActivity;
import java.util.List;

/**
 * @author zhangj
 * @date 2020/12/26 21:08
 * desc足球
 */
public class ZjFragment extends MyFragment /*implements StatusAction*/ {

    RecyclerView recy;
//    @BindView(R.id.hint)
//    HintLayout hint;
//    @BindView(R.id.refresh)
//    SmartRefreshLayout refresh;

    private int type;
    private ZjAdapter zjAdapter;

    public static ZjFragment newInstance(int type) {
        return new ZjFragment(type);
    }

    public ZjFragment(int type) {
        this.type = type;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_zj;
    }

    @Override
    protected void initView() {
        recy = (RecyclerView) findViewById(R.id.recy);


    }

    @Override
    protected void initData() {
        recy.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        zjAdapter = new ZjAdapter(getActivity());
        zjAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView recyclerView, View itemView, int position) {
                if (SpUtil.getInstance().getBooleanValue(SpUtil.IS_LOGIN)) {
                    Intent intent = new Intent(getActivity(), ZjDetailActivity.class);
                    intent.putExtra(IntentKey.ID, zjAdapter.getItem(position).getId());
                    intent.putExtra(IntentKey.DATA, new Gson().toJson(zjAdapter.getItem(position)));
                    startActivity(intent);
                } else {
                    startActivity(LoginActivity.class);
                }
            }
        });
        recy.setAdapter(zjAdapter);

//        showComplete();
        getForumExpert();
        addOnVisible(new OnVisible() {
            @Override
            public void isUsetVisible(boolean isVisible) {
                if (isVisible) {
                    getForumExpert();
                }
            }
        });
        addOnVisible(new OnVisibleType() {
            @Override
            public void isUsetVisibleType(boolean isVisible, int Type) {
                if (isVisible) {
                    type = Type;
                    getForumExpert();
                }
            }
        });
    }

//    @Override
//    public HintLayout getHintLayout() {
//        return hint;
//    }

    /**
     * 获取专家列表
     */
    private void getForumExpert() {
        EasyHttp.get(this)
                .api("api/Forum/getForumExpert?code=" + type + "&uid=" + AppConfig.getLoginBean().getId())
                .request(new HttpCallback<HttpData<List<ZjBean>>>(this) {
                    @Override
                    public void onSucceed(HttpData<List<ZjBean>> result) {
                        super.onSucceed(result);
                        if (result.getCode() == 1) {
                            zjAdapter.setData(result.getData());
                        }
                    }
                });
    }

    public class ZjAdapter extends MyAdapter<ZjBean> {

        public ZjAdapter(@NonNull Context context) {
            super(context);
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(R.layout.item_zj);
        }

        class ViewHolder extends MyAdapter.ViewHolder {

            private ImageView imgLevel;
            private CircleImageView imgAvatar;
            private TextView tvUserName, tvMzl;
            private Button btn;

            public ViewHolder(int id) {
                super(id);
                imgAvatar = (CircleImageView) findViewById(R.id.img_avatar);
                imgLevel = (ImageView) findViewById(R.id.img_level);
                tvUserName = (TextView) findViewById(R.id.tv_name);
                tvMzl = (TextView) findViewById(R.id.tv_mzl);
                btn = (Button) findViewById(R.id.btn);
            }

            @Override
            public void onBindView(int position) {
                ZjBean zjBean = getItem(position);
                Glide.with(getContext())
                        .load(zjBean.getAvatar())
                        .error(R.mipmap.icon_contact_avatar_default)
                        .placeholder(R.mipmap.icon_contact_avatar_default)
                        .into(imgAvatar);
                int icon = R.mipmap.expert_no1;
                switch (zjBean.getGrade()) {
                    case 2:
                        icon = R.mipmap.expert_no2;
                        break;
                    case 3:
                        icon = R.mipmap.expert_no3;
                        break;
                }
                Glide.with(getContext())
                        .load(icon)
                        .into(imgLevel);
                tvUserName.setText(zjBean.getNickname());
                tvMzl.setText(BigDecimalUtils.mul(zjBean.getHit_rate(),"100",0) + "%");
                if (zjBean.getIs_follow() == 1) {
                    btn.setText(R.string.ygz);
                    btn.setBackgroundResource(R.drawable.bg_ygz);
                } else {
                    btn.setText(R.string.gz);
                    btn.setBackgroundResource(R.drawable.bg_circle_red);
                }
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (AppConfig.isLogin()) {
                            ZjFragment.this.setFollow(zjBean.getId());
                        } else {
                            startActivity(LoginActivity.class);
                        }
                    }
                });
            }
        }
    }

    /**
     * 关注取消关注专家
     *
     * @param eid
     */
    public void setFollow(int eid) {
        EasyHttp.get(this)
                .api("api/Forum/setFollow?uid=" + AppConfig.getLoginBean().getId() + "&eid=" + eid)
                .request(new HttpCallback<HttpData<String>>(this) {
                    @Override
                    public void onSucceed(HttpData<String> result) {
                        super.onSucceed(result);
                        if (result.getCode() == 1) {
                            getForumExpert();
                        }
                    }
                });
    }

}