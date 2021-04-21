package com.sameal.dd.http.response;

public class WsMatchBean {

    /**
     * type : match
     * data : {"rid":"37336535","status":4,"uTime":1612101623}
     */

    private String type;
    private DataBean data;

    public String getType() {
        return type == null ? "" : type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * rid : 37336535
         * status : 4
         * uTime : 1612101623
         */

        private String rid;
        private int status;
        private int uTime;

        public String getRid() {
            return rid == null ? "" : rid;
        }

        public void setRid(String rid) {
            this.rid = rid;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getuTime() {
            return uTime;
        }

        public void setuTime(int uTime) {
            this.uTime = uTime;
        }
    }
}
