package com.sameal.dd.http.response;

public class UpLoadBean {

    /**
     * url : /uploads/20210111/c4d4da1fe6d55c1cb46d079861f7f5da.jpg
     */

    private String url;
    private String adpic;
    private String share;
    private String zuo;
    private String you;
    private String myex;

    public String getShare() {
        return share == null ? "" : share;
    }

    public void setShare(String share) {
        this.share = share;
    }

    public String getZuo() {
        return zuo == null ? "" : zuo;
    }

    public void setZuo(String zuo) {
        this.zuo = zuo;
    }

    public String getYou() {
        return you == null ? "" : you;
    }

    public void setYou(String you) {
        this.you = you;
    }

    public String getMyex() {
        return myex == null ? "" : myex;
    }

    public void setMyex(String myex) {
        this.myex = myex;
    }

    public String getAdpic() {
        return adpic == null ? "" : adpic;
    }

    public void setAdpic(String adpic) {
        this.adpic = adpic;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
