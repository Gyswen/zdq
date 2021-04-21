package com.sameal.dd.http.response;

import java.util.List;

public class TgUserBean {

    /**
     * info : [{"id":2,"nickname":"13333828929","coin":5},{"id":5,"nickname":"18953159673","coin":5},{"id":7,"nickname":"好用不","coin":5}]
     * count : 3
     * sumcoin : 15
     */

    private int count;
    private int sumcoin;
    private List<InfoBean> info;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getSumcoin() {
        return sumcoin;
    }

    public void setSumcoin(int sumcoin) {
        this.sumcoin = sumcoin;
    }

    public List<InfoBean> getInfo() {
        return info;
    }

    public void setInfo(List<InfoBean> info) {
        this.info = info;
    }

    public static class InfoBean {
        /**
         * id : 2
         * nickname : 13333828929
         * coin : 5
         */

        private int id;
        private String nickname;
        private int coin;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public int getCoin() {
            return coin;
        }

        public void setCoin(int coin) {
            this.coin = coin;
        }
    }
}
