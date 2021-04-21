package com.sameal.dd.http.response;

public class MoneyDetailBean {


    /**
     * id : 2
     * uid : 4
     * code : 3
     * money : 80.00
     * sp_id : 5
     * order_num : 202101271736015716
     * type : 2
     * addtime : 1611740161
     */

    private int id;
    private int uid;
    private int code;
    private String money;
    private int sp_id;
    private String order_num;
    private int type;
    private int addtime;

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

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMoney() {
        return money == null ? "" : money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public int getSp_id() {
        return sp_id;
    }

    public void setSp_id(int sp_id) {
        this.sp_id = sp_id;
    }

    public String getOrder_num() {
        return order_num == null ? "" : order_num;
    }

    public void setOrder_num(String order_num) {
        this.order_num = order_num;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getAddtime() {
        return addtime;
    }

    public void setAddtime(int addtime) {
        this.addtime = addtime;
    }
}
