package com.sameal.dd.ui.ddActivity;

import android.text.method.LinkMovementMethod;
import android.widget.TextView;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.sameal.dd.R;
import com.sameal.dd.common.MyActivity;
import com.sameal.dd.http.model.HttpData;
import com.sameal.dd.other.IntentKey;
import com.zzhoujay.richtext.RichText;

/**
 * @author zhangj
 * @date 2021/1/7 13:50
 * @desc 问题详情
 */
public class CustomerDetailActivity extends MyActivity {

    TextView tvContent;

    private String title;
    private int id;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_customer_detail;
    }

    @Override
    protected void initView() {
        tvContent = (TextView) findViewById(R.id.tv_content);
    }

    @Override
    protected void initData() {
        title = getString(IntentKey.TITLE);
        id = getInt(IntentKey.ID);
        setTitle(title);
        getCustomerDetail();
    }

    /**
     * 获取问题详情
     */
    private void getCustomerDetail() {
        EasyHttp.get(this)
                .api("api/User/customerDetail?id=" + id)
                .request(new HttpCallback<HttpData<CustomerDetailBean>>(this) {
                    @Override
                    public void onSucceed(HttpData<CustomerDetailBean> result) {
                        super.onSucceed(result);
                        if (result.getCode() == 1) {
                            tvContent.setMovementMethod(LinkMovementMethod.getInstance());
                            RichText.initCacheDir(CustomerDetailActivity.this);
                            RichText.fromHtml(result.getData().getProblem().getContent()).into(tvContent);
//                            tvContent.setText(Html.fromHtml(result.getData().getProblem().getContent()));
                        }
                    }
                });
    }

    public static class CustomerDetailBean {

        /**
         * problem : {"id":1,"title":"电竞玩法","content":"0"}
         */

        private ProblemBean problem;

        public ProblemBean getProblem() {
            return problem;
        }

        public void setProblem(ProblemBean problem) {
            this.problem = problem;
        }

        public static class ProblemBean {
            /**
             * id : 1
             * title : 电竞玩法
             * content : 0
             */

            private int id;
            private String title;
            private String content;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }
        }
    }
}