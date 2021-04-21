package com.sameal.dd.http.response;

import com.samluys.filtertab.base.BaseFilterBean;

public class ClassifyChildEntry extends BaseFilterBean {

    /**
     * 街道ID
     */
    private int id;
    /**
     * 街道名称
     */
    private String name;
    /**
     * 选择状态
     */
    private int selected;

    public ClassifyChildEntry(int id, String name, int selected) {
        this.id = id;
        this.name = name;
        this.selected = selected;
    }

    @Override
    public String getItemName() {
        return name;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public int getSelecteStatus() {
        return selected;
    }

    @Override
    public void setSelecteStatus(int status) {
        this.selected = status;
    }
}
