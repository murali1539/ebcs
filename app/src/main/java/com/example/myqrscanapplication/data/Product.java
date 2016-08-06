package com.example.myqrscanapplication.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by yatra on 23/1/16.
 */

/*{"item_type": 1, "stock_status": [true],
        "image_url": "http://www.bigbasket.com/media/uploads/p/l/10000454_3-bb-royal-sona-masoori-boiled-rice.jpg",
                "name": "Boiled", "price_value": [50], "quantity_type": ["kg"], "price_type": ["Rs"],
        "quantity_value": [1], "id": 1, "description": "Boiled_desc"}*/

public class Product {

    private String id;
    private String name;
    private String description;
    @SerializedName("parent_category_id")
    private String parentCategoryId;
    @SerializedName("image_url")
    private String imageUrl;
    @SerializedName("quantity_type")
    private String[] quantTypes;
    @SerializedName("quantity_value")
    private int[] quantityValues;
    @SerializedName("price_type")
    private String[] currency;
    @SerializedName("price_value")
    private float[] priceValues;
    @SerializedName("stock_status")
    private boolean[] inStock;

    public Product(){

    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getParentCategoryId() {
        return parentCategoryId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int[] getQuantityValues() {
        return quantityValues;
    }

    public String[] getCurrency() {
        return currency;
    }

    public float[] getPriceValues() {
        return priceValues;
    }

    public boolean[] isInStock() {
        return inStock;
    }

    public String[] getQuantTypes() {
        return quantTypes;
    }
}
