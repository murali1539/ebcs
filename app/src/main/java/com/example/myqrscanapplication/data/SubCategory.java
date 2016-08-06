package com.example.myqrscanapplication.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by yatra on 24/1/16.
 */
public class SubCategory {
    private String name;
    @SerializedName("image_url")
    private String imageUrl;
    @SerializedName("id")
    private String prodID;
    /**
     * 0 - category
     * 1 - product
     */
    @SerializedName("item_type")
    private int type;

    public static final String categoryUrl = "http://demo8249346.mockable.io/category?id=%s";
    public static final String productUrl = "";

    public SubCategory(String name, String imageUrl, String prodID) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.prodID = prodID;
    }

    public String getUrl() {
        if(type == 0)
            return String.format(categoryUrl,prodID);
        return String.format(productUrl,prodID);
    }

    public String getName(){
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getProdID() {
        return prodID;
    }

    public int getType() {
        return type;
    }

}
