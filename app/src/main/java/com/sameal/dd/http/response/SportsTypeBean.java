package com.sameal.dd.http.response;

import java.util.ArrayList;
import java.util.List;

public class SportsTypeBean {

    /**
     * id : 1
     * title : 电竞
     * father_id : 0
     * image :
     * gid : 0
     * status : 1
     * soncount : 6
     * son : [{"id":5,"title":"王者荣耀","father_id":1,"image":"https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=1511169512,2556521568&fm=26&gp=0.jpg","gid":4,"status":1,"list_count":0},{"id":6,"title":"LOL","father_id":1,"image":"https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=3342289208,2364997743&fm=26&gp=0.jpg","gid":1,"status":1,"list_count":14},{"id":9,"title":"DATA2","father_id":1,"image":"https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=3342289208,2364997743&fm=26&gp=0.jpg","gid":2,"status":1,"list_count":0},{"id":10,"title":"CSGO","father_id":1,"image":"https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=3342289208,2364997743&fm=26&gp=0.jpg","gid":3,"status":1,"list_count":0},{"id":11,"title":"星际争霸","father_id":1,"image":"https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=3342289208,2364997743&fm=26&gp=0.jpg","gid":5,"status":1,"list_count":0},{"id":12,"title":"守望先锋","father_id":1,"image":"https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=3342289208,2364997743&fm=26&gp=0.jpg","gid":6,"status":1,"list_count":0}]
     */

    private int id;
    private String title;
    private int father_id;
    private String image;
    private int gid;
    private int status;
    private int soncount;
    private List<SonBean> son;
    private boolean isCheck;

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

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

    public int getFather_id() {
        return father_id;
    }

    public void setFather_id(int father_id) {
        this.father_id = father_id;
    }

    public String getImage() {
        return image == null ? "" : image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getGid() {
        return gid;
    }

    public void setGid(int gid) {
        this.gid = gid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getSoncount() {
        return soncount;
    }

    public void setSoncount(int soncount) {
        this.soncount = soncount;
    }

    public List<SonBean> getSon() {
        if (son == null) {
            return new ArrayList<>();
        }
        return son;
    }

    public void setSon(List<SonBean> son) {
        this.son = son;
    }

    public static class SonBean {
        /**
         * id : 5
         * title : 王者荣耀
         * father_id : 1
         * image : https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=1511169512,2556521568&fm=26&gp=0.jpg
         * gid : 4
         * status : 1
         * list_count : 0
         */

        private int id;
        private String title;
        private int father_id;
        private String image;
        private int gid;
        private int status;
        private int list_count;

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

        public int getFather_id() {
            return father_id;
        }

        public void setFather_id(int father_id) {
            this.father_id = father_id;
        }

        public String getImage() {
            return image == null ? "" : image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public int getGid() {
            return gid;
        }

        public void setGid(int gid) {
            this.gid = gid;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getList_count() {
            return list_count;
        }

        public void setList_count(int list_count) {
            this.list_count = list_count;
        }
    }
}
