package com.sameal.dd.http.response;

import com.google.gson.annotations.SerializedName;

public class BannerListBean {

    /**
     * id : 28
     * image : http://dong.weifangtianxia.com/uploads/20201030/df8b7d2635670652d29440c57b49e088.jpeg
     * enum : 4
     * links : 22
     */

    private int id;
    private String image;
    @SerializedName("enum")
    private String enumX;
    private String links;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getEnumX() {
        return enumX;
    }

    public void setEnumX(String enumX) {
        this.enumX = enumX;
    }

    public String getLinks() {
        return links;
    }

    public void setLinks(String links) {
        this.links = links;
    }
}
