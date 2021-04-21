package com.sameal.dd.ui.ddActivity;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.sameal.dd.R;
import com.sameal.dd.aop.SingleClick;
import com.sameal.dd.common.MyActivity;
import com.sameal.dd.common.MyAdapter;
import com.sameal.dd.http.model.HttpData;
import com.sameal.dd.http.request.UpLoadApi;
import com.sameal.dd.http.response.UpLoadBean;
import com.sameal.dd.http.server.ReleaseServer;
import com.sameal.dd.other.AppConfig;
import com.sameal.dd.ui.activity.ImageSelectActivity;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangj
 * @date 2020/12/20 11:31
 * desc 意见反馈
 */
public class FeedBackActivity extends MyActivity {

    TextView tvTextNum;
    EditText etFeedback;
    TextView tvFileNum;
    RecyclerView recy;
    RelativeLayout rlSelectPhoto;
    AppCompatButton btnSubmit;

    private List<String> uploadImg = new ArrayList<>();
    private ImgAdapter imgAdapter;
    private int maxSelect = 4;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_feed_back;
    }

    @Override
    protected void initView() {
        tvTextNum = (TextView) findViewById(R.id.tv_text_num);
        etFeedback = (EditText) findViewById(R.id.et_feedback);
        tvFileNum = (TextView) findViewById(R.id.tv_file_num);
        rlSelectPhoto = (RelativeLayout) findViewById(R.id.rl_select_photo);
        recy = (RecyclerView) findViewById(R.id.recy);
        btnSubmit = (AppCompatButton) findViewById(R.id.btn_submit);
        setOnClickListener(R.id.btn_submit, R.id.rl_select_photo);
    }

    @Override
    protected void initData() {
        recy.setLayoutManager(new GridLayoutManager(this, 4));
        imgAdapter = new ImgAdapter(this);
        recy.setAdapter(imgAdapter);
        etFeedback.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                tvTextNum.setText(etFeedback.getText().length() + "/200");
            }
        });
        tvFileNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().equals("4/4")) {
                    rlSelectPhoto.setVisibility(View.GONE);
                } else {
                    rlSelectPhoto.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @SingleClick
    public void onClick(View view) {
        if (view.getId() == R.id.btn_submit) {
            if (TextUtils.isEmpty(etFeedback.getText().toString())) {
                toast("请输入反馈内容");
                return;
            }
            feedBack();
        } else if (view.getId() == R.id.rl_select_photo) {
            ImageSelectActivity.start(this, maxSelect, data -> {
                for (String s : data) {
                    upload(s);
                }
            });
        }
    }

    /**
     * 上传图片
     */
    private void upload(String path) {
        EasyHttp.post(this)
                .api(new UpLoadApi()
                        .setFile(new File(path)))
                .request(new HttpCallback<HttpData<UpLoadBean>>(this) {
                    @Override
                    public void onSucceed(HttpData<UpLoadBean> result) {
                        super.onSucceed(result);
                        if (result.getCode() == 1) {
                            if (uploadImg.size() < 4) {
                                imgAdapter.addItem(result.getData().getUrl());
                                maxSelect --;
                                tvFileNum.setText(imgAdapter.getItemCount() + "/4");
                            }
                        }
                    }
                });
    }

    /**
     * 意见反馈
     */
    private void feedBack() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < imgAdapter.getItemCount(); i++) {
            if (i != imgAdapter.getItemCount() - 1) {
                builder.append(imgAdapter.getItem(i)).append(",");
            } else {
                builder.append(imgAdapter.getItem(i));
            }
        }
        EasyHttp.get(this)
                .api("api/User/setFeedback?uid=" + AppConfig.getLoginBean().getId() +
                        "&content=" + etFeedback.getText().toString() + "&pic=" + builder.toString())
                .request(new HttpCallback<HttpData<String>>(this) {
                    @Override
                    public void onSucceed(HttpData<String> result) {
                        super.onSucceed(result);
                        toast(result.getMessage());
                        if (result.getCode() == 1) {
                            finish();
                        }
                    }
                });
    }

    private class ImgAdapter extends MyAdapter<String> {

        public ImgAdapter(@NonNull Context context) {
            super(context);
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(R.layout.item_img);
        }

        class ViewHolder extends MyAdapter.ViewHolder {

            private ImageView imageView;
            private ImageView imgDelete;

            public ViewHolder(int id) {
                super(id);
                imageView = (ImageView) findViewById(R.id.img);
                imgDelete = (ImageView) findViewById(R.id.img_delete);
            }

            @Override
            public void onBindView(int position) {
                Glide.with(getContext())
                        .load(new ReleaseServer().getHost() + getItem(position))
                        .into(imageView);
                imgDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getData().remove(position);
                        maxSelect++;
                        notifyDataSetChanged();
                        tvFileNum.setText(getItemCount() + "/4");
                    }
                });
            }
        }
    }
}