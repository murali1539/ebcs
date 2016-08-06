package com.example.myqrscanapplication.data;

import android.os.Parcel;

import java.util.List;

/**
 * Created by yatra on 23/1/16.
 */
public class Category extends Item {

    public List<Item> items;

    public Category(boolean isCategory,String name, List<Item> items) {
        super(isCategory,name);
        this.items = items;
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest,flags);
        dest.writeTypedList(items);
    }

}
