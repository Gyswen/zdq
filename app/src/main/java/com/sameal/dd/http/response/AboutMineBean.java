package com.sameal.dd.http.response;

public class AboutMineBean {

    /**
     * id : 7
     * title : 版本说明
     * content : http://www.baidu.coom
     * type : 1
     * addtime : 0
     */

    private int id;
    private String title;
    private String content;
    private int type;
    private int addtime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title == null ? "" : title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content == null ? "" : content;
    }

    public void setContent(String content) {
        this.content = content;
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
