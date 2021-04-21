package com.sameal.dd.http.response;

public class MessageEvent {

    private int type;
    private String message;
    private int posi;

    public MessageEvent(int type, String message) {
        this.type = type;
        this.message = message;
    }

    public MessageEvent(int type, int posi) {
        this.type = type;
        this.posi = posi;
    }

    public int getPosi() {
        return posi;
    }

    public void setPosi(int posi) {
        this.posi = posi;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMessage() {
        return message == null ? "" : message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
