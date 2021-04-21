package com.sameal.dd.http.response;

public class WealthBean {

    /**
     * uid : 1
     * username : 13513805787
     * nickname : 大海
     * mobile : 13513805787
     * money : 50232.00
     * avatar : https://dong.zdqyl.com/uploads/20210304/4862e81dfc739b06ecd8d9b0cc3ea6b7.jpg
     * gender : 0
     * bio : 面向大海春暖花开。
     */

    private int uid;
    private String username;
    private String nickname;
    private String mobile;
    private String money;
    private String avatar;
    private int gender;
    private String bio;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
}
