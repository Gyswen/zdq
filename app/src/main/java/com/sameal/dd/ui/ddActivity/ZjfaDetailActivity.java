package com.sameal.dd.ui.ddActivity;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.hjq.base.BaseDialog;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.hjq.widget.view.CircleImageView;
import com.sameal.dd.R;
import com.sameal.dd.aop.SingleClick;
import com.sameal.dd.common.MyActivity;
import com.sameal.dd.helper.LogUtils;
import com.sameal.dd.helper.TimeUtil;
import com.sameal.dd.http.model.HttpData;
import com.sameal.dd.http.response.ZjfaBean;
import com.sameal.dd.other.AppConfig;
import com.sameal.dd.other.IntentKey;
import com.sameal.dd.ui.dialog.MessageDialog;

/**
 * @author zhangj
 * @date 2021/1/4 15:36
 * @desc 描述 专家方案详情
 */
public class ZjfaDetailActivity extends MyActivity {

    TextView tvMoneyType;
    TextView tvTitle;
    TextView tvCreateTime;
    TextView tvViewNum;
    CircleImageView imgAvatar;
    TextView tvName;
    TextView tvJob;
    TextView tvBlue;
    TextView tvRed;
    ImageView imgAdd;
    TextView tvFollow;
    LinearLayout llFollow;
    TextView tvFaTitle;
    TextView tvBo;
    TextView tvFxmf;
    TextView tvScore;
    ImageView imgStart;
    TextView tvStartName;
    ImageView imgEnd;
    TextView tvEndName;
    TextView tvReason;
    TextView tvPayHint;
    TextView tvPayMoney;
    Button btn;
    RelativeLayout rlPay;
    TextView tvResult;
    private int id;
    private ZjfaBean zjfaBean;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_zjfa_detail;
    }

    @Override
    protected void initView() {
        tvMoneyType = (TextView) findViewById(R.id.tv_money_type);
        tvResult = (TextView) findViewById(R.id.tv_result);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvCreateTime = (TextView) findViewById(R.id.tv_createTime);
        tvViewNum = (TextView) findViewById(R.id.tv_view_num);
        imgAvatar = (CircleImageView) findViewById(R.id.img_avatar);
        tvName = (TextView) findViewById(R.id.tv_name);
        tvJob = (TextView) findViewById(R.id.tv_job);
        tvBlue = (TextView) findViewById(R.id.tv_blue);
        tvRed = (TextView) findViewById(R.id.tv_red);
        llFollow = (LinearLayout) findViewById(R.id.ll_follow);
        imgAdd = (ImageView) findViewById(R.id.img_add);
        tvFollow = (TextView) findViewById(R.id.tv_follow);
        tvFxmf = (TextView) findViewById(R.id.tv_fxmf);
        tvFaTitle = (TextView) findViewById(R.id.tv_fa_title);
        tvBo = (TextView) findViewById(R.id.tv_bo);
        imgStart = (ImageView) findViewById(R.id.img_start);
        tvStartName = (TextView) findViewById(R.id.tv_start_name);
        tvScore = (TextView) findViewById(R.id.tv_score);
        imgEnd = (ImageView) findViewById(R.id.img_end);
        tvEndName = (TextView) findViewById(R.id.tv_end_name);
        tvReason = (TextView) findViewById(R.id.tv_reason);
        tvPayHint = (TextView) findViewById(R.id.tv_pay_hint);
        rlPay = (RelativeLayout) findViewById(R.id.rl_pay);
        tvPayMoney = (TextView) findViewById(R.id.tv_pay_money);
        btn = (Button) findViewById(R.id.btn);
        setOnClickListener(R.id.ll_follow, R.id.btn);
    }

    @Override
    protected void initData() {
        id = getInt(IntentKey.ID);
        getForumDetail();
    }

    @Override
    public void onRightClick(View v) {
        toast("暂时无法分享");
        // 分享对话框
//        new ShareDialog.Builder(this)
//                // 分享标题
//                .setShareTitle(getString(R.string.app_name))
//                // 分享描述
//                .setShareDescription(getString(R.string.app_name))
//                // 分享缩略图
//                .setShareLogo(R.mipmap.ic_launcher)
//                // 分享链接
//                .setShareUrl(zjfaBean.getShareurl())
//                .setListener(new UmengShare.OnShareListener() {
//
//                    @Override
//                    public void onSucceed(Platform platform) {
//                        toast("分享成功");
//                    }
//
//                    @Override
//                    public void onError(Platform platform, Throwable t) {
//                        toast("分享出错");
//                    }
//
//                    @Override
//                    public void onCancel(Platform platform) {
//                        toast("分享取消");
//                    }
//                })
//                .show();
    }

    @SingleClick
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_follow:
                setFollow();
                break;
            case R.id.btn:
                //支付
                new MessageDialog.Builder(this)
                        .setTitle("提示")
                        .setMessage("是否购买该方案?")
                        .setListener(new MessageDialog.OnListener() {
                            @Override
                            public void onConfirm(BaseDialog dialog) {
                                buyFa();
                            }
                        }).show();
                break;
        }
    }

    /**
     * 获取方案详情
     */
    private void getForumDetail() {
        EasyHttp.get(this)
                .api("api/Forum/getForumDetail?tid=" + id + "&uid=" + AppConfig.getLoginBean().getId())
                .request(new HttpCallback<HttpData<ZjfaBean>>(this) {
                    @Override
                    public void onSucceed(HttpData<ZjfaBean> result) {
                        super.onSucceed(result);
                        if (result.getCode() == 1) {
                            zjfaBean = result.getData();
                            setData();
                        } else {
                            toast(result.getMessage());
                            finish();
                        }
                    }
                });
    }

    private void setData() {
        if (zjfaBean.getMoney() == 0 || zjfaBean.getIs_buy() == 1 || zjfaBean.getResult().equals(getString(R.string.red)) || zjfaBean.getResult().equals(getString(R.string.black))) {
            rlPay.setVisibility(View.GONE);
            tvFxmf.setVisibility(View.VISIBLE);
            tvPayHint.setVisibility(View.GONE);
            tvReason.setVisibility(View.VISIBLE);
        } else if (AppConfig.getLoginBean().getLevel_status() == 1 && zjfaBean.getMoney() < 28) {
            //白银
            rlPay.setVisibility(View.GONE);
            tvFxmf.setVisibility(View.VISIBLE);
            tvPayHint.setVisibility(View.GONE);
            tvReason.setVisibility(View.VISIBLE);
        } else if (AppConfig.getLoginBean().getLevel_status() == 2 && zjfaBean.getMoney() < 58) {
            //黄金
            rlPay.setVisibility(View.GONE);
            tvFxmf.setVisibility(View.VISIBLE);
            tvPayHint.setVisibility(View.GONE);
            tvReason.setVisibility(View.VISIBLE);
        } else if (AppConfig.getLoginBean().getLevel_status() == 3 && zjfaBean.getMoney() < 88) {
            //铂金
            rlPay.setVisibility(View.GONE);
            tvFxmf.setVisibility(View.VISIBLE);
            tvPayHint.setVisibility(View.GONE);
            tvReason.setVisibility(View.VISIBLE);
        } else if (AppConfig.getLoginBean().getLevel_status() == 4 && zjfaBean.getMoney() < 108) {
            //钻石
            rlPay.setVisibility(View.GONE);
            tvFxmf.setVisibility(View.VISIBLE);
            tvPayHint.setVisibility(View.GONE);
            tvReason.setVisibility(View.VISIBLE);
        } else {
            rlPay.setVisibility(View.VISIBLE);
            tvFxmf.setVisibility(View.GONE);
            tvPayHint.setVisibility(View.VISIBLE);
            tvReason.setVisibility(View.GONE);
        }
        if (zjfaBean.getMoney() != 0) {
            tvMoneyType.setText("收费");
        }
        tvCreateTime.setText("发布于" + TimeUtil.timeYMDHMinSFigure(Long.valueOf(zjfaBean.getCreate_time() + "000")));
        tvTitle.setText(zjfaBean.getContent());
        tvViewNum.setText(zjfaBean.getView_num() + "");
        Glide.with(this)
                .load(zjfaBean.getUserinfo().getAvatar())
                .error(R.mipmap.icon_contact_avatar_default)
                .placeholder(R.mipmap.icon_contact_avatar_default)
                .into(imgAvatar);
        tvName.setText(zjfaBean.getUserinfo().getNickname());
        tvJob.setText(zjfaBean.getUserinfo().getOccupation());
        tvBo.setText(zjfaBean.getRoundType());
        tvBlue.setText("近" + zjfaBean.getUserinfo().getFrequency() + "红" + zjfaBean.getUserinfo().getFrequency_red());
        tvRed.setText(zjfaBean.getUserinfo().getContinuity_red() + "连红");
        if (zjfaBean.getIs_follow() == 1) {
            Drawable drawable = getResources().getDrawable(R.drawable.bg_circle_bcbcbc_30_stork);
            imgAdd.setVisibility(View.GONE);
            tvFollow.setText("已关注");
            tvFollow.setTextColor(getResources().getColor(R.color.color_bcbcbc));
            llFollow.setBackground(drawable);
        } else {
            Drawable drawable = getResources().getDrawable(R.drawable.bg_circle_white_30_stork);
            imgAdd.setVisibility(View.VISIBLE);
            imgAdd.setImageResource(R.mipmap.gift_add);
            tvFollow.setText("关注");
            tvFollow.setTextColor(getResources().getColor(R.color.colorAccent));
            llFollow.setBackground(drawable);
        }
        for (ZjfaBean.EventsBean eventsBean : zjfaBean.getEvents()) {
            if (zjfaBean.getBetinfo().getEid() == eventsBean.getEid()) {
                zjfaBean.getBetinfo().setName(eventsBean.getName());
                zjfaBean.getBetinfo().setG_name(eventsBean.getG_name());
            }
        }
        tvFaTitle.setText(zjfaBean.getTitle());
        LogUtils.d(TAG,zjfaBean.getBetinfo().getG_name() + " " + zjfaBean.getBetinfo().getName());
        tvFxmf.setText(zjfaBean.getBetinfo().getG_name() + " 推荐 " + zjfaBean.getBetinfo().getName() + "@" + String.format("%.2f", zjfaBean.getBetinfo().getOdds() / 1000));
        tvStartName.setText(zjfaBean.getItem1_name());
        Glide.with(this)
                .load(zjfaBean.getItem1_logo())
                .error(R.mipmap.icon_contact_avatar_default)
                .placeholder(R.mipmap.icon_contact_avatar_default)
                .into(imgStart);
        tvEndName.setText(zjfaBean.getItem2_name());
        tvScore.setText(zjfaBean.getRaceResult().getScore());
        Glide.with(this)
                .load(zjfaBean.getItem2_logo())
                .error(R.mipmap.icon_contact_avatar_default)
                .placeholder(R.mipmap.icon_contact_avatar_default)
                .into(imgEnd);
        tvReason.setText(zjfaBean.getReason());
        if (zjfaBean.getResult().equals(getString(R.string.red))) {
            tvResult.setText(R.string.red);
            Drawable red = getDrawable(R.drawable.bg_textview_padding4_solidf74d21_corners50_red);
            tvResult.setBackground(red);
            tvResult.setVisibility(View.VISIBLE);
        } else if (zjfaBean.getResult().equals(getString(R.string.black))) {
            tvResult.setText(R.string.black);
            Drawable black = getDrawable(R.drawable.bg_textview_padding4_solidf74d21_corners50_black);
            tvResult.setBackground(black);
            tvResult.setVisibility(View.VISIBLE);
        }
        tvPayMoney.setText("需支付:" + zjfaBean.getMoney() + "椰糖");
    }

    /**
     * 关注取消关注专家
     */
    public void setFollow() {
        EasyHttp.get(this)
                .api("api/Forum/setFollow?uid=" + AppConfig.getLoginBean().getId() + "&eid=" + zjfaBean.getUserinfo().getId())
                .request(new HttpCallback<HttpData<String>>(this) {
                    @Override
                    public void onSucceed(HttpData<String> result) {
                        super.onSucceed(result);
                        if (result.getCode() == 1) {
                            if (zjfaBean.getIs_follow() == 1) {
                                zjfaBean.setIs_follow(0);
                            } else {
                                zjfaBean.setIs_follow(1);
                            }
                            setData();
                        }
                    }
                });
    }

    /**
     * 购买方案
     */
    private void buyFa() {
        EasyHttp.get(this)
                .api("api/Forum/buyForumDetail?uid=" + AppConfig.getLoginBean().getId() + "&forumid=" + zjfaBean.getId())
                .request(new HttpCallback<HttpData<String>>(this) {
                    @Override
                    public void onSucceed(HttpData<String> result) {
                        super.onSucceed(result);
                        if (result.getCode() == 1) {
                            getForumDetail();
                        }
                        toast(result.getMessage());
                    }
                });
    }
}