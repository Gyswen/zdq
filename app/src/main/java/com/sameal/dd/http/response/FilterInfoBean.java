package com.sameal.dd.http.response;

import com.samluys.filtertab.base.BaseFilterBean;

import java.util.ArrayList;
import java.util.List;

public class FilterInfoBean extends BaseFilterBean {

    private String name;
    private List<FilterInfoBean> list;
    private int select = 0;

    public int getSelect() {
        return select;
    }

    public void setSelect(int select) {
        this.select = select;
    }

    public List<FilterInfoBean> getList() {
        if (list == null) {
            return new ArrayList<>();
        }
        return list;
    }

    public void setList(List<FilterInfoBean> list) {
        this.list = list;
    }

    public String getName() {
        return name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getItemName() {
        return name;
    }

    @Override
    public int getId() {
        return 0;
    }

    @Override
    public int getSelecteStatus() {
        return select;
    }

    @Override
    public void setSelecteStatus(int status) {

    }

    @Override
    public List getChildList() {
        return list;
    }


}
