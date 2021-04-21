package com.sameal.dd.http.response;

import java.util.ArrayList;
import java.util.List;

public class WsPingBean {

    /**
     * type : odds
     * data : [{"eid":40320847,"rid":37339550,"odd":1850,"uTime":1612099184,"status":1}]
     */

    private String type;
    private List<DataBean> data;

    public String getType() {
        return type == null ? "" : type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<DataBean> getData() {
        if (data == null) {
            return new ArrayList<>();
        }
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * eid : 40320847
         * rid : 37339550
         * odd : 1850
         * uTime : 1612099184
         * status : 1
         */

        private int eid;
        private int rid;
        private int odd;
        private int uTime;
        private int status;

        public int getEid() {
            return eid;
        }

        public void setEid(int eid) {
            this.eid = eid;
        }

        public int getRid() {
            return rid;
        }

        public void setRid(int rid) {
            this.rid = rid;
        }

        public int getOdd() {
            return odd;
        }

        public void setOdd(int odd) {
            this.odd = odd;
        }

        public int getuTime() {
            return uTime;
        }

        public void setuTime(int uTime) {
            this.uTime = uTime;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}
