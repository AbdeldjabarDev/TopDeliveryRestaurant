package com.oslsoftware.restaurantapp;

import java.util.List;

public class Order
{

    int totalValue;
    String location;
    String orderId;
    int status;
    String restaurant;
    List<String> items;
    public String getOrderId() {
        return orderId;
    }
    public int getTotalValue() {
        return totalValue;
    }
    public String getLocation() {
        return location;
    }
    public int getStatus() {
        return status;
    }
    public String getRestaurant() {
        return restaurant;
    }
    public List<String> getItems() {
        return items;
    }

}
