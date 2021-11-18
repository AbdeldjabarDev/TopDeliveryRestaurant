package com.oslsoftware.restaurantapp;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;

public class orderItem  {

    String name;
    int price;
    int quantity;

    public String getName() {
        return name;
    }
    public int getPrice() {
        return price;
    }
    public int getQuantity() {
        return quantity;
    }

}
