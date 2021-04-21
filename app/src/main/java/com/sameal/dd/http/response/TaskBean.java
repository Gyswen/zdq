package com.sameal.dd.http.response;

public class TaskBean {

    /**
     * id : 1
     * title : 简单
     * describes : 每日最少投注1笔>100椰糖的订单
     * reward : 18
     * grade : 1
     * addtime :
     * is_making : 1
     */

    private int id;
    private String title;
    private String describes;
    private int reward;
    private int grade;
    private String addtime;
    private int is_making;

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

    public String getDescribes() {
        return describes;
    }

    public void setDescribes(String describes) {
        this.describes = describes;
    }

    public int getReward() {
        return reward;
    }

    public void setReward(int reward) {
        this.reward = reward;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public int getIs_making() {
        return is_making;
    }

    public void setIs_making(int is_making) {
        this.is_making = is_making;
    }
}
