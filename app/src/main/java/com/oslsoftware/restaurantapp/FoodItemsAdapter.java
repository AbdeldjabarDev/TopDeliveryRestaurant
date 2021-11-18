package com.oslsoftware.restaurantapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import retrofit2.Retrofit;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FoodItemsAdapter extends RecyclerView.Adapter<FoodItemsAdapter.ViewHolder> {
ArrayList<FoodItem2> foodItems;
Context mContext;
String category;
String restaurantId;

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        Log.d("FoodItemAdapter","onAttached to recycler view called");
    }

    @Override
    public void onViewAttachedToWindow(@NonNull ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        Log.d("FoodItemAdapter","on view Attached to window  called");
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    MainActivity.RestaurantApi restaurantApi;

    public void setRestaurantApi(MainActivity.RestaurantApi restaurantApi) {
        this.restaurantApi = restaurantApi;
    }



    public FoodItemsAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setFoodItems(ArrayList<FoodItem2> foodItems) {
        this.foodItems = foodItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("FoodItemAdapter","On create view holder called for category " + category);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_item_state,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d("FoodItemAdapter","On bind view holder called for category " + category);
holder.itemName.setText(foodItems.get(position).getFoodItem().getName());
holder.itemState.setChecked(foodItems.get(position).getFoodItem().getAvailable() == 0 ? false:true);
holder.itemState.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        retrofit2.Callback<callStatus> setItemCallCallback = new Callback<callStatus>() {
            @Override
            public void onResponse(Call<callStatus> call, Response<callStatus> response) {
                  if(response.body().getStatus() == 0)
                  {
                      Toast.makeText(mContext, "Item state set successfully", Toast.LENGTH_SHORT).show();
                      FoodItem dummy = foodItems.get(position).getFoodItem();
                      dummy.setAvailable(isChecked ? 1:0);
                      foodItems.get(position).setFoodItem(dummy);
                  }
                  else
                  {
                      Toast.makeText(mContext, "item state set failed please try later", Toast.LENGTH_SHORT).show();

                  }
            }

            @Override
            public void onFailure(Call<callStatus> call, Throwable t) {
                Toast.makeText(mContext, "Failed : " + t.toString(), Toast.LENGTH_SHORT).show();
                holder.itemState.setChecked(!isChecked);
            }
        };
        Call<callStatus> setItemStateCall =  restaurantApi.setItemState(restaurantId,category,foodItems.get(position).getDocId(),isChecked == true ? 1:0);
        setItemStateCall.enqueue(setItemCallCallback);
    }
});
    }

    @Override
    public int getItemCount() {
        Log.d("FoodItemAdapter","items count : " + foodItems.size());
        return foodItems.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemName;
        Switch itemState;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.txtItemName);
            itemState = itemView.findViewById(R.id.switchAvailable);
        }
    }
}
