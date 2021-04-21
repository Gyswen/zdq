package com.sameal.dd.http.response;

public class RechargeBean {

    /**
     * id : 1
     * money : 50
     * coin : 50
     * status : 1
     */

    private int id;
    private int money;
    private int coin;
    private int status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getCoin() {
        return coin;
    }

    public void setCoin(int coin) {
        this.coin = coin;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
