package com.sameal.dd.http.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserSignBean {

    /**
     * sign_set : [[{"id":5,"uid":1,"addtime":"1609149969","is_sign":0,"is_today":1}]]
     * continue : 0
     */

    @SerializedName("continue")
    private int continueX;
    private List<List<SignSetBean>> sign_set;

    public int getContinueX() {
        return continueX;
    }

    public void setContinueX(int continueX) {
        this.continueX = continueX;
    }

    public List<List<SignSetBean>> getSign_set() {
        return sign_set;
    }

    public void setSign_set(List<List<SignSetBean>> sign_set) {
        this.sign_set = sign_set;
    }

    public static class SignSetBean {
        /**
         * id : 5
         * uid : 1
         * addtime : 1609149969
         * is_sign : 0
         * is_today : 1
         */

        private int id;
        private int uid;
        private String addtime;
        private int is_sign;
        private int is_today;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        public int getIs_sign() {
            return is_sign;
        }

        public void setIs_sign(int is_sign) {
            this.is_sign = is_sign;
        }

        public int getIs_today() {
            return is_today;
        }

        public void setIs_today(int is_today) {
            this.is_today = is_today;
        }
    }
}
