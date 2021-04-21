package com.sameal.dd.http.response;

import java.util.List;

public class CustomerMainBean {

    private List<CustomerBean> customer;
    private List<ProblemBean> problem;

    public List<CustomerBean> getCustomer() {
        return customer;
    }

    public void setCustomer(List<CustomerBean> customer) {
        this.customer = customer;
    }

    public List<ProblemBean> getProblem() {
        return problem;
    }

    public void setProblem(List<ProblemBean> problem) {
        this.problem = problem;
    }

    public static class CustomerBean {
        /**
         * phone : 17076012299
         * type : 1
         * descr : 客服热线
         */

        private String phone;
        private int type;
        private String descr;

        public String getPhone() {
            return phone == null ? "" : phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getDescr() {
            return descr == null ? "" : descr;
        }

        public void setDescr(String descr) {
            this.descr = descr;
        }
    }

    public static class ProblemBean {
        /**
         * id : 1
         * title : 电竞玩法
         */

        private int id;
        private String title;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title == null ? "" : title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
