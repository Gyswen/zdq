package com.sameal.dd.http.response;

public class HotActivityBean {

    /**
     * pic : http://qn.tuzijie888.com/2.png
     * title : 满周有礼
     * msg : 2有礼
     */

    private String pic;
    private String title;
    private String msg;
    private String type;

    public String getType() {
        return type == null ? "" : type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
