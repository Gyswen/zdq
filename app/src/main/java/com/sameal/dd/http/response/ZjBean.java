package com.sameal.dd.http.response;

public class ZjBean {

    /**
     * id : 4
     * uid : 16
     * occupation : 足球分析师
     * fans : 0
     * describes : 足球爱好者，NBA赛事实力分析，二十年竞猜经验。
     * frequency : 0
     * frequency_red : 0
     * continuity_red : 0
     * hit_rate : 0
     * grade : 1
     * addtime : 0
     * nickname : 13153039998
     * avatar : http://dong.weifangtianxia.com/uploads/20201101/8c6863b65e818e9ab04cba410f5a7bc3.jpg
     * is_follow : 0
     */

    private int id;
    private int uid;
    private String occupation;
    private int fans;
    private String describes;
    private int frequency;
    private int frequency_red;
    private String continuity_red;
    private String hit_rate;
    private int grade;
    private int addtime;
    private String nickname;
    private String avatar;
    private int is_follow;

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

    public String getOccupation() {
        return occupation == null ? "" : occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public int getFans() {
        return fans;
    }

    public void setFans(int fans) {
        this.fans = fans;
    }

    public String getDescribes() {
        return describes == null ? "" : describes;
    }

    public void setDescribes(String describes) {
        this.describes = describes;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public int getFrequency_red() {
        return frequency_red;
    }

    public void setFrequency_red(int frequency_red) {
        this.frequency_red = frequency_red;
    }

    public String getContinuity_red() {
        return continuity_red == null ? "" : continuity_red;
    }

    public void setContinuity_red(String continuity_red) {
        this.continuity_red = continuity_red;
    }

    public String getHit_rate() {
        return hit_rate == null ? "0" : hit_rate;
    }

    public void setHit_rate(String hit_rate) {
        this.hit_rate = hit_rate;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public int getAddtime() {
        return addtime;
    }

    public void setAddtime(int addtime) {
        this.addtime = addtime;
    }

    public String getNickname() {
        return nickname == null ? "" : nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar == null ? "" : avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getIs_follow() {
        return is_follow;
    }

    public void setIs_follow(int is_follow) {
        this.is_follow = is_follow;
    }
}
