package com.oslsoftware.restaurantapp;

public class FoodItem2 {
    FoodItem foodItem;
    String docId;

    public FoodItem2(FoodItem foodItem, String docId) {
        this.foodItem = foodItem;
        this.docId = docId;
    }

    public FoodItem getFoodItem() {
        return foodItem;
    }

    public void setFoodItem(FoodItem foodItem) {
        this.foodItem = foodItem;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }
}
