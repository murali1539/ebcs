package com.example.myqrscanapplication.cart;

import com.google.gson.annotations.SerializedName;

/**
 * Created by yatra on 21/1/16.
 */
public class CartItem {
    /**
     * {"name": "Basmati", "priceUnit": "Rs", "priceValue": 1500, "quantType": "kg", "quantValue": 25, "id": 1}
     */

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public float getPrice() {
        return price;
    }

    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("priceValue")
    private float price;
    @SerializedName("priceUnit")
    private String priceUnit;
    @SerializedName("quantType")
    private String qunatType;
    @SerializedName("quantValue")
    private int quantValue;

    public String getQuantUnit() {
        return quantValue + " " + qunatType;
    }

    private int quant = 1;
    public int getQuant() {
        return quant;
    }

    public void setQuant(int quant) {
        this.quant = quant;
    }

    public CartItem(String _id, String _name, float _price, int _quant) {
        id = _id;
        name = _name;
        price = _price;
        quantValue = _quant;
    }



}
