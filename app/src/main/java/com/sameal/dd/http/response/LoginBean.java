package com.sameal.dd.http.response;

/**
 * author : Android 轮子哥
 * github : https://github.com/getActivity/AndroidProject
 * time   : 2019/12/07
 * desc   : 登录返回
 */
public final class LoginBean {


    /**
     * id : 1
     * pid : 10
     * group_id : 1
     * username : admin
     * nickname : admin
     * password : c13f62012fd6a8fdf06b3452a94430e5
     * mobile : 13888888888
     * salt : rpR6Bv
     * avatar : /uploads/20200924/d2b8745973a79d9dbdecfd44e8187a86.jpeg
     * gender : 0
     * money : 1973.40
     * sign_num : 1
     * sign_time : 1609149969
     * tixian_percentage : 0.00
     * score : 0
     * real_name :
     * id_card :
     * bank_name :
     * bank_deposit :
     * bank_num :
     * successions : 1
     * maxsuccessions : 1
     * loginfailure : 0
     * token :
     * level_status : 0
     * status : normal
     * verification :
     * is_shiming : 0
     * joinip : 127.0.0.1
     * jointime : 1491461418
     * logintime : 1602842862
     * vip_end_time : 2020-09-27
     * prevtime : 1601340763
     * last_time :
     * create_time : 2020-10-18 02:30:38
     * update_time : 2020-10-18 02:30:38
     * vipcode : 1
     * sm_code : 0
     * f_class :
     */

    private int id;
    private int pid;
    private int group_id;
    private String username;
    private String nickname;
    private String password;
    private String mobile;
    private String salt;
    private String avatar;
    private int gender;
    private String money;
    private String is_ex_money;
    private int sign_num;
    private int sign_time;
    private String tixian_percentage;
    private int score;
    private String real_name;
    private String id_card;
    private String bank_name;
    private String bank_deposit;
    private String bank_num;
    private int successions;
    private int maxsuccessions;
    private int loginfailure;
    private String token;
    private int level_status;
    private String status;
    private String verification;
    private int is_shiming;
    private String joinip;
    private int jointime;
    private int logintime;
    private String vip_end_time;
    private int prevtime;
    private String last_time;
    private String create_time;
    private String update_time;
    private int vipcode;
    private int sm_code;
    private String f_class;
    private String ex_money;
    private int real_status;
    private String bio;//个性签名
    private int is_test;//是否为模拟用户
    private int action;

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getGroup_id() {
        return group_id;
    }

    public void setGroup_id(int group_id) {
        this.group_id = group_id;
    }

    public String getUsername() {
        return username == null ? "" : username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname == null ? "" : nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password == null ? "" : password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobile() {
        return mobile == null ? "" : mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getSalt() {
        return salt == null ? "" : salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getAvatar() {
        return avatar == null ? "" : avatar;
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

    public String getMoney() {
        return money == null ? "0" : money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getIs_ex_money() {
        return is_ex_money;
    }

    public void setIs_ex_money(String is_ex_money) {
        this.is_ex_money = is_ex_money;
    }

    public int getSign_num() {
        return sign_num;
    }

    public void setSign_num(int sign_num) {
        this.sign_num = sign_num;
    }

    public int getSign_time() {
        return sign_time;
    }

    public void setSign_time(int sign_time) {
        this.sign_time = sign_time;
    }

    public String getTixian_percentage() {
        return tixian_percentage == null ? "" : tixian_percentage;
    }

    public void setTixian_percentage(String tixian_percentage) {
        this.tixian_percentage = tixian_percentage;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getReal_name() {
        return real_name == null ? "" : real_name;
    }

    public void setReal_name(String real_name) {
        this.real_name = real_name;
    }

    public String getId_card() {
        return id_card == null ? "" : id_card;
    }

    public void setId_card(String id_card) {
        this.id_card = id_card;
    }

    public String getBank_name() {
        return bank_name == null ? "" : bank_name;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }

    public String getBank_deposit() {
        return bank_deposit == null ? "" : bank_deposit;
    }

    public void setBank_deposit(String bank_deposit) {
        this.bank_deposit = bank_deposit;
    }

    public String getBank_num() {
        return bank_num == null ? "" : bank_num;
    }

    public void setBank_num(String bank_num) {
        this.bank_num = bank_num;
    }

    public int getSuccessions() {
        return successions;
    }

    public void setSuccessions(int successions) {
        this.successions = successions;
    }

    public int getMaxsuccessions() {
        return maxsuccessions;
    }

    public void setMaxsuccessions(int maxsuccessions) {
        this.maxsuccessions = maxsuccessions;
    }

    public int getLoginfailure() {
        return loginfailure;
    }

    public void setLoginfailure(int loginfailure) {
        this.loginfailure = loginfailure;
    }

    public String getToken() {
        return token == null ? "" : token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getLevel_status() {
        return level_status;
    }

    public void setLevel_status(int level_status) {
        this.level_status = level_status;
    }

    public String getStatus() {
        return status == null ? "" : status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVerification() {
        return verification == null ? "" : verification;
    }

    public void setVerification(String verification) {
        this.verification = verification;
    }

    public int getIs_shiming() {
        return is_shiming;
    }

    public void setIs_shiming(int is_shiming) {
        this.is_shiming = is_shiming;
    }

    public String getJoinip() {
        return joinip == null ? "" : joinip;
    }

    public void setJoinip(String joinip) {
        this.joinip = joinip;
    }

    public int getJointime() {
        return jointime;
    }

    public void setJointime(int jointime) {
        this.jointime = jointime;
    }

    public int getLogintime() {
        return logintime;
    }

    public void setLogintime(int logintime) {
        this.logintime = logintime;
    }

    public String getVip_end_time() {
        return vip_end_time == null ? "" : vip_end_time;
    }

    public void setVip_end_time(String vip_end_time) {
        this.vip_end_time = vip_end_time;
    }

    public int getPrevtime() {
        return prevtime;
    }

    public void setPrevtime(int prevtime) {
        this.prevtime = prevtime;
    }

    public String getLast_time() {
        return last_time == null ? "" : last_time;
    }

    public void setLast_time(String last_time) {
        this.last_time = last_time;
    }

    public String getCreate_time() {
        return create_time == null ? "" : create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getUpdate_time() {
        return update_time == null ? "" : update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public int getVipcode() {
        return vipcode;
    }

    public void setVipcode(int vipcode) {
        this.vipcode = vipcode;
    }

    public int getSm_code() {
        return sm_code;
    }

    public void setSm_code(int sm_code) {
        this.sm_code = sm_code;
    }

    public String getF_class() {
        return f_class == null ? "" : f_class;
    }

    public void setF_class(String f_class) {
        this.f_class = f_class;
    }

    public String getEx_money() {
        return ex_money == null ? "" : ex_money;
    }

    public void setEx_money(String ex_money) {
        this.ex_money = ex_money;
    }

    public int getReal_status() {
        return real_status;
    }

    public void setReal_status(int real_status) {
        this.real_status = real_status;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public int getIs_test() {
        return is_test;
    }

    public void setIs_test(int is_test) {
        this.is_test = is_test;
    }
}