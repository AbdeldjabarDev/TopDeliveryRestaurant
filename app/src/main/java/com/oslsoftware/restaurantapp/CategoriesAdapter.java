package com.oslsoftware.restaurantapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder> {
    Map<String, ArrayList<FoodItem2>> foodItemList;
    List<String> categories;
    Context mContext;
    String restaurantId;
MainActivity.RestaurantApi restaurantApi;
    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public void setFoodItemList(Map<String, ArrayList<FoodItem2>> foodItemList) {
        this.foodItemList = foodItemList;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public CategoriesAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_item_row,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
       holder.categoryName.setText(categories.get(position));
       FoodItemsAdapter adapter = new FoodItemsAdapter(mContext);
       adapter.setCategory(categories.get(position));
       adapter.setFoodItems(foodItemList.get(categories.get(position)));
       adapter.setRestaurantId(restaurantId);
       adapter.setRestaurantApi(restaurantApi);
       holder.categoryRecView.setAdapter(adapter);
       holder.categoryRecView.setLayoutManager(new LinearLayoutManager(mContext));


    }

    public void setRestaurantApi(MainActivity.RestaurantApi restaurantApi) {
        this.restaurantApi = restaurantApi;
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        RecyclerView categoryRecView;
        TextView categoryName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryRecView = itemView.findViewById(R.id.categoryRecView);
            categoryName = itemView.findViewById(R.id.txtCategory);

        }
    }
}
