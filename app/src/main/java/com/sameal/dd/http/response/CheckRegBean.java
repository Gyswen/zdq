package com.sameal.dd.http.response;

import java.io.Serializable;

public class CheckRegBean implements Serializable {

    /**
     * action : 1
     * is_reg : 1
     */

    private int action;
    private int is_reg;

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public int getIs_reg() {
        return is_reg;
    }

    public void setIs_reg(int is_reg) {
        this.is_reg = is_reg;
    }
}
