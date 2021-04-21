package com.sameal.dd.http.response;

import java.util.List;

public class ExchangeShopBean {

    /**
     * id : 1
     * title : 劳力士手表
     * pics : ["http://dong.weifangtianxia.com/uploads/20210111/c4d4da1fe6d55c1cb46d079861f7f5da.jpg","http://dong.weifangtianxia.com/uploads/20210111/878c82b6053e33ba2a0af4963535a9c9.jpg"]
     * content : <p>很贵的表</p>
     * money : 100.00
     * status : 1
     * addtime : 0
     */

    private int id;
    private String title;
    private String content;
    private String money;
    private int status;
    private int addtime;
    private List<String> pics;

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

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getAddtime() {
        return addtime;
    }

    public void setAddtime(int addtime) {
        this.addtime = addtime;
    }

    public List<String> getPics() {
        return pics;
    }

    public void setPics(List<String> pics) {
        this.pics = pics;
    }
}
