package com.sameal.dd.http.response;

import com.samluys.filtertab.base.BaseFilterBean;

import java.util.List;

public class ClassifyEntry extends BaseFilterBean {


    private int id;

    private String name;

    private int selected;

    private List<ClassifyChildEntry> childEntries;

    public ClassifyEntry(int id, String name, int selected, List<ClassifyChildEntry> childEntries) {
        this.id = id;
        this.name = name;
        this.selected = selected;
        this.childEntries = childEntries;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }

    public List<ClassifyChildEntry> getChildEntries() {
        return childEntries;
    }

    public void setChildEntries(List<ClassifyChildEntry> childEntries) {
        this.childEntries = childEntries;
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

    @Override
    public List getChildList() {
        return childEntries;
    }
}
