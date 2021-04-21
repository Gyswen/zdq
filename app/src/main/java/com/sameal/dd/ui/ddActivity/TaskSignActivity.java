package com.sameal.dd.ui.ddActivity;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hjq.base.BaseDialog;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.sameal.dd.R;
import com.sameal.dd.aop.SingleClick;
import com.sameal.dd.common.MyActivity;
import com.sameal.dd.common.MyAdapter;
import com.sameal.dd.http.model.HttpData;
import com.sameal.dd.http.request.PublicApi;
import com.sameal.dd.http.request.SignInApi;
import com.sameal.dd.http.response.TaskBean;
import com.sameal.dd.http.response.UserSignBean;
import com.sameal.dd.other.AppConfig;
import com.sameal.dd.ui.dialog.MessageDialog;

import java.util.Arrays;
import java.util.List;


/**
 * @author zhangj
 * @date 2020/12/30 20:39
 * desc 满周有礼
 */
public class TaskSignActivity extends MyActivity {

    RecyclerView recySign;
    Button btn;
    RecyclerView recyTask;

    private SignAdapter signAdapter;
    private TaskAdapter taskAdapter;
    private int signDay = 0;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_task_sign;
    }

    @Override
    protected void initView() {
        recySign = (RecyclerView) findViewById(R.id.recy_sign);
        btn = (Button) findViewById(R.id.btn);
        recyTask = (RecyclerView) findViewById(R.id.recy_task);
        setOnClickListener(R.id.btn);
    }

    @Override
    protected void initData() {
        recySign.setLayoutManager(new GridLayoutManager(this, 7));
        recyTask.setLayoutManager(new LinearLayoutManager(this));

        signAdapter = new SignAdapter(this);
        taskAdapter = new TaskAdapter(this);

        recySign.setAdapter(signAdapter);
        recyTask.setAdapter(taskAdapter);
        getTaskList();
        getUserSign();
        signAdapter.setData(Arrays.asList("1", "1", "1", "1", "1", "1", "1"));
    }

    @Override
    public void onRightClick(View v) {
        new MessageDialog.Builder(this)
                .setMessage("1、该活动每个用户只能参与2次;\n2、用户可在任务界面选择要挑战的任务级别，点击挑战任务后开启任务;\n" +
                        "3、如用户中途中断未投注，或投注额度小于所领取任务等级的额度，则任务挑战;失败，且用户消耗掉1次挑战任务的机会，若用户想继续挑战任务可到任务界面再次领取任务;\n" +
                        "4、系统每日给参与任务的用户发消息提示任务完成情况;\n" +
                        "5、该活动仅适用于足球和电竞竞猜;\n6、正鼎全赛事有权延长、缩短、终止，或者修改该活动!此活动最终解释权归正鼎全赛事;")
                .setListener(new MessageDialog.OnListener() {
                    @Override
                    public void onConfirm(BaseDialog dialog) {

                    }
                })
                .setCancel("")
                .show();
    }

    @SingleClick
    @Override
    public void onClick(View v) {
        sign();
    }

    /**
     * 签到
     */
    private void sign() {
        EasyHttp.post(this)
                .api(new SignInApi())
                .request(new HttpCallback<HttpData<String>>(this) {
                    @Override
                    public void onSucceed(HttpData<String> result) {
                        super.onSucceed(result);
                        if (result.getCode() == 200) {
                            getUserSign();
                        }
                    }
                });
    }

    private boolean isHaveMasking = false;

    /**
     * 获取任务列表
     */
    private void getTaskList() {
        EasyHttp.get(this)
                .api("api/sists/getTaskList?uid=" + AppConfig.getLoginBean().getId())
                .request(new HttpCallback<HttpData<List<TaskBean>>>(this) {
                    @Override
                    public void onSucceed(HttpData<List<TaskBean>> result) {
                        super.onSucceed(result);
                        if (result.getCode() == 1) {
                            taskAdapter.setData(result.getData());
                            for (TaskBean taskBean : result.getData()) {
                                if (taskBean.getIs_making() == 1) {
                                    isHaveMasking = true;
                                }
                            }
                        }
                    }
                });
    }

    /**
     * 获取签到天数
     */
    private void getUserSign() {
        EasyHttp.post(this)
                .api(new PublicApi("api/sists/getUserSign"))
                .request(new HttpCallback<HttpData<UserSignBean>>(this) {
                    @Override
                    public void onSucceed(HttpData<UserSignBean> result) {
                        super.onSucceed(result);
                        if (result.getCode() == 200) {
                            btn.setEnabled(true);
                            btn.setText("签到");
                            signDay = result.getData().getContinueX();
                        } else if (result.getCode() == 203) {
                            toast(result.getMessage());
                            btn.setEnabled(false);
                            btn.setText("已签到");
                            signDay = result.getData().getContinueX();
                        }
                        signAdapter.notifyDataSetChanged();
                    }
                });
    }

    class SignAdapter extends MyAdapter<String> {

        public SignAdapter(@NonNull Context context) {
            super(context);
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(R.layout.item_sign);
        }

        class ViewHolder extends MyAdapter.ViewHolder {

            private View viewLine, viewCircle;
            private TextView tvDay;

            public ViewHolder(int id) {
                super(id);
                viewLine = (View) findViewById(R.id.view_line);
                viewCircle = (View) findViewById(R.id.view_circle);
                tvDay = (TextView) findViewById(R.id.tv_sign_day);
            }

            @Override
            public void onBindView(int position) {
                tvDay.setText(position + 1 + "天");
                if (position < signDay) {
                    viewLine.setBackgroundColor(getColor(R.color.colorAccent));
                    viewCircle.setBackground(getDrawable(R.drawable.bg_sign_circle));
                } else {
                    viewLine.setBackgroundColor(Color.parseColor("#72a1ff"));
                    viewCircle.setBackground(getDrawable(R.drawable.bg_sign_circle_un));
                }
            }
        }
    }

    class TaskAdapter extends MyAdapter<TaskBean> {

        public TaskAdapter(@NonNull Context context) {
            super(context);
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(R.layout.item_task);
        }

        class ViewHolder extends MyAdapter.ViewHolder {

            private ImageView imgLevel;
            private TextView tvLevel, tvDesc, tvReward;
            private Button btn;
            private View viewLine;

            public ViewHolder(int id) {
                super(id);
                imgLevel = (ImageView) findViewById(R.id.img_level);
                tvLevel = (TextView) findViewById(R.id.tv_level);
                tvDesc = (TextView) findViewById(R.id.tv_desc);
                tvReward = (TextView) findViewById(R.id.tv_reward);
                btn = (Button) findViewById(R.id.btn);
                viewLine = (View) findViewById(R.id.view_line);
            }

            @Override
            public void onBindView(int position) {
                TaskBean taskBean = getItem(position);
                if (position == 0) {
                    viewLine.setVisibility(View.GONE);
                }
                int levelIcon = R.mipmap.difficulty1;
                switch (taskBean.getGrade()) {
                    case 2:
                        levelIcon = R.mipmap.difficulty2;
                        break;
                    case 3:
                        levelIcon = R.mipmap.difficulty3;
                        break;
                    case 4:
                        levelIcon = R.mipmap.difficulty4;
                        break;
                }
                Glide.with(TaskSignActivity.this)
                        .load(levelIcon)
                        .into(imgLevel);
                tvLevel.setText(taskBean.getTitle());
                tvDesc.setText(taskBean.getDescribes());
                tvReward.setText("奖励" + taskBean.getReward() + "椰糖");
                if (taskBean.getIs_making() == 1) {
                    btn.setText("进行中");
                    btn.setBackgroundResource(R.drawable.bg_gradle);
                } else {
                    btn.setText("去报名");
                    btn.setBackgroundResource(R.drawable.bg_task_default);
                }
                if (isHaveMasking) {
                    btn.setEnabled(false);
                    btn.setFocusable(false);
                }
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (taskBean.getIs_making() == 1) {
                            toast("进行中");
                        } else {
                            if (isHaveMasking) {
                                toast("请将进行中任务完成");
                            } else {
                                TaskSignActivity.this.getTask(taskBean.getId());
                            }
                        }
                    }
                });
            }
        }
    }

    /**
     * 领取任务
     */
    private void getTask(int taskId) {
        EasyHttp.get(this)
                .api("api/sists/addUserTask?uid=" + AppConfig.getLoginBean().getId() + "&task_id=" + taskId)
                .request(new HttpCallback<HttpData<String>>(this) {
                    @Override
                    public void onSucceed(HttpData<String> result) {
                        super.onSucceed(result);
                        if (result.getCode() == 1) {
                            getTaskList();
                        } else {
                            toast(result.getMessage());
                        }
                    }
                });
    }
}