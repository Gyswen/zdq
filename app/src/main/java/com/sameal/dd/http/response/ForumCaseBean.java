package com.sameal.dd.http.response;

import java.util.List;

public class ForumCaseBean {

    /**
     * id : 1
     * title : 电竞
     * father_id : 0
     * addtime : 0
     * son : [{"id":3,"title":"KOG","father_id":1,"addtime":"0"},{"id":4,"title":"LOL","father_id":1,"addtime":"0"},{"id":5,"title":"DOTA2","father_id":1,"addtime":"0"},{"id":6,"title":"CSGO","father_id":1,"addtime":"0"}]
     */

    private int id;
    private String title;
    private int father_id;
    private String addtime;
    private List<SonBean> son;

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

    public int getFather_id() {
        return father_id;
    }

    public void setFather_id(int father_id) {
        this.father_id = father_id;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public List<SonBean> getSon() {
        return son;
    }

    public void setSon(List<SonBean> son) {
        this.son = son;
    }

    public static class SonBean {
        /**
         * id : 3
         * title : KOG
         * father_id : 1
         * addtime : 0
         */

        private int id;
        private String title;
        private int father_id;
        private String addtime;

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

        public int getFather_id() {
            return father_id;
        }

        public void setFather_id(int father_id) {
            this.father_id = father_id;
        }

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }
    }
}
