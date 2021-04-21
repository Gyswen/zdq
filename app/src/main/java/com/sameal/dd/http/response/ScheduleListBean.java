package com.sameal.dd.http.response;

public class ScheduleListBean {

    /**
     * id : 1
     * sp_id : 7
     * title : 澳洲足球甲级联赛
     * vs_one : 西部联队FC
     * vs_two : 阿德莱德联
     * one_logo : http://img4.imgtn.bdimg.com/it/u=2667735368,748417772&fm=26&gp=0.jpg
     * two_logo : http://img1.imgtn.bdimg.com/it/u=3275246730,2177627945&fm=15&gp=0.jpg
     * play_rolu : 1
     * start_time : 1609348609
     * sessions : 17265514
     * relust :
     * create_time : 1609176713
     */

    private int id;
    private int sp_id;
    private String title;
    private String vs_one;
    private String vs_two;
    private String one_logo;
    private String two_logo;
    private int play_rolu;
    private int start_time;
    private String sessions;
    private String relust;
    private String create_time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSp_id() {
        return sp_id;
    }

    public void setSp_id(int sp_id) {
        this.sp_id = sp_id;
    }

    public String getTitle() {
        return title == null ? "" : title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVs_one() {
        return vs_one == null ? "" : vs_one;
    }

    public void setVs_one(String vs_one) {
        this.vs_one = vs_one;
    }

    public String getVs_two() {
        return vs_two == null ? "" : vs_two;
    }

    public void setVs_two(String vs_two) {
        this.vs_two = vs_two;
    }

    public String getOne_logo() {
        return one_logo == null ? "" : one_logo;
    }

    public void setOne_logo(String one_logo) {
        this.one_logo = one_logo;
    }

    public String getTwo_logo() {
        return two_logo == null ? "" : two_logo;
    }

    public void setTwo_logo(String two_logo) {
        this.two_logo = two_logo;
    }

    public int getPlay_rolu() {
        return play_rolu;
    }

    public void setPlay_rolu(int play_rolu) {
        this.play_rolu = play_rolu;
    }

    public int getStart_time() {
        return start_time;
    }

    public void setStart_time(int start_time) {
        this.start_time = start_time;
    }

    public String getSessions() {
        return sessions == null ? "" : sessions;
    }

    public void setSessions(String sessions) {
        this.sessions = sessions;
    }

    public String getRelust() {
        return relust == null ? "" : relust;
    }

    public void setRelust(String relust) {
        this.relust = relust;
    }

    public String getCreate_time() {
        return create_time == null ? "" : create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }
}
