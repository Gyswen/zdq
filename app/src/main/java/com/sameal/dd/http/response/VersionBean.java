package com.sameal.dd.http.response;

public class VersionBean {

    /**
     * android_version : 1.0.0
     * android_title : 版本更新
     * android_href : http://www.baidu.com
     * android_content : 更新已知bug,     		制作bug的程序员已祭天
     * android_isforce : 1
     * ios_version : 1.0.0
     * ios_title : 版本更新
     * ios_href : http://www.baidu.com
     * ios_content : 更新已知bug,     		制作bug的程序员已祭天
     * ios_isforce : 1
     */

    private String android_version;
    private String android_title;
    private String android_href;
    private String android_content;
    private String android_isforce;
    private String ios_version;
    private String ios_title;
    private String ios_href;
    private String ios_content;
    private String ios_isforce;

    public String getAndroid_version() {
        return android_version == null ? "" : android_version;
    }

    public void setAndroid_version(String android_version) {
        this.android_version = android_version;
    }

    public String getAndroid_title() {
        return android_title == null ? "" : android_title;
    }

    public void setAndroid_title(String android_title) {
        this.android_title = android_title;
    }

    public String getAndroid_href() {
        return android_href == null ? "" : android_href;
    }

    public void setAndroid_href(String android_href) {
        this.android_href = android_href;
    }

    public String getAndroid_content() {
        return android_content == null ? "" : android_content;
    }

    public void setAndroid_content(String android_content) {
        this.android_content = android_content;
    }

    public String getAndroid_isforce() {
        return android_isforce == null ? "" : android_isforce;
    }

    public void setAndroid_isforce(String android_isforce) {
        this.android_isforce = android_isforce;
    }

    public String getIos_version() {
        return ios_version == null ? "" : ios_version;
    }

    public void setIos_version(String ios_version) {
        this.ios_version = ios_version;
    }

    public String getIos_title() {
        return ios_title == null ? "" : ios_title;
    }

    public void setIos_title(String ios_title) {
        this.ios_title = ios_title;
    }

    public String getIos_href() {
        return ios_href == null ? "" : ios_href;
    }

    public void setIos_href(String ios_href) {
        this.ios_href = ios_href;
    }

    public String getIos_content() {
        return ios_content == null ? "" : ios_content;
    }

    public void setIos_content(String ios_content) {
        this.ios_content = ios_content;
    }

    public String getIos_isforce() {
        return ios_isforce == null ? "" : ios_isforce;
    }

    public void setIos_isforce(String ios_isforce) {
        this.ios_isforce = ios_isforce;
    }
}
