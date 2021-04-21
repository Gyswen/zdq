package com.sameal.dd.http.response;

public class WsAddressBean {

    /**
     * code : 200
     * data : {"ip":"data.302v.cn","port":3102,"sign":"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJjaWQiOiI1ZmYyYzYwNjU1MWZmZjM1ODRhYTM4OTAiLCJpcCI6IjExOC4xNzguOTEuMjE3IiwiZXhwIjoxNjEyMDk4ODMwfQ.zybL2B_Mat2tZTG2CFVCpzzxKGcn_kaJv0dWGjC8B2c"}
     */

    private int code;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * ip : data.302v.cn
         * port : 3102
         * sign : eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJjaWQiOiI1ZmYyYzYwNjU1MWZmZjM1ODRhYTM4OTAiLCJpcCI6IjExOC4xNzguOTEuMjE3IiwiZXhwIjoxNjEyMDk4ODMwfQ.zybL2B_Mat2tZTG2CFVCpzzxKGcn_kaJv0dWGjC8B2c
         */

        private String ip;
        private int port;
        private String sign;

        public String getIp() {
            return ip == null ? "" : ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public String getSign() {
            return sign == null ? "" : sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }
    }
}
