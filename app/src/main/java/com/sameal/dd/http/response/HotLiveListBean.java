package com.sameal.dd.http.response;

public class HotLiveListBean {

    /**
     * id : 4
     * fid : 4
     * image : http://dong.weifangtianxia.com/uploads/20200821/ae5bad15200aac88af07e578c2d895ac.jpeg
     * author : 孙悟空4
     * title : 虹猫蓝兔
     * describes : 烤了虹猫蓝兔
     * play_stream : http://vfx.mtime.cn/Video/2019/03/19/mp4/190319222227698228.mp4
     * addtime : 1609000988
     * is_hs : 横屏
     * is_zb : 直播
     * status : 0
     */

    private int id;
    private int type;
    private int fid;
    private String rid;
    private String image;
    private String author;
    private String title;
    private String describes;
    private String play_stream;
    private String addtime;
    private String live_address;
    private String is_hs;
    private String is_zb;
    private int status;

    public String getRid() {
        return rid == null ? "" : rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


    public String getLive_address() {
        return live_address == null ? "" : live_address;
    }

    public void setLive_address(String live_address) {
        this.live_address = live_address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFid() {
        return fid;
    }

    public void setFid(int fid) {
        this.fid = fid;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
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

    public String getPlay_stream() {
        return play_stream;
    }

    public void setPlay_stream(String play_stream) {
        this.play_stream = play_stream;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public String getIs_hs() {
        return is_hs;
    }

    public void setIs_hs(String is_hs) {
        this.is_hs = is_hs;
    }

    public String getIs_zb() {
        return is_zb;
    }

    public void setIs_zb(String is_zb) {
        this.is_zb = is_zb;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
