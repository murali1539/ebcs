package com.example.myqrscanapplication.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yatra on 23/1/16.
 */
public class Item implements Parcelable{
    public boolean isCategory = false;
    public String name;

    public Item(boolean isCategory, String name) {
        this.isCategory = isCategory;
        this.name = name;
    }

    protected Item(Parcel in) {
        isCategory = in.readByte() != 0;
        name = in.readString();
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
        dest.writeByte((byte) (isCategory ? 1 : 0));
        dest.writeString(name);
    }
}
