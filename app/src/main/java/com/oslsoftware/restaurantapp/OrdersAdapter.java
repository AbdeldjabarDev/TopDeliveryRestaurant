package com.oslsoftware.restaurantapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.SystemClock;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ViewHolder> {
    List<Order> orderList;
    List<Long> offesets;
    Context mContext;
    Call<callStatus> orderStateCall;
MainActivity.RestaurantApi restaurantApi;
String restaurantId;
RecyclerView parentRecView;

    public void setParentRecView(RecyclerView parentRecView) {
        this.parentRecView = parentRecView;
    }

    public List<Order> getOrderList() {
        return orderList;
    }

    Callback<callStatus> orderStateCallback = new Callback<callStatus>() {
    @Override
    public void onResponse(Call<callStatus> call, Response<callStatus> response) {
        Toast.makeText(mContext, "Order dispatched", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFailure(Call<callStatus> call, Throwable t) {
        Toast.makeText(mContext, "Failed to dispatch " + t.toString(), Toast.LENGTH_SHORT).show();
    }
};
    public OrdersAdapter(Context mContext) {
        this.mContext = mContext;
        orderList = new ArrayList<>();
        offesets = new ArrayList<>();
    }

    public List<Long> getOffesets() {
        return offesets;
    }

    public void setRestaurantApi(MainActivity.RestaurantApi restaurantApi) {
        this.restaurantApi = restaurantApi;

    }

    public void setOrderList(List<Order> orderList) {
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item,parent,false);
        return new ViewHolder(itemView);
    }


    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
if(!orderList.isEmpty())
{

    Order order = orderList.get(position);
    for(String item: order.getItems())
    {
        TextView textView = new TextView(mContext);
        TextView textViewQty = new TextView(mContext);
        textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        String[] arr = item.split("\\*");
        LinearLayout itemsLL = new LinearLayout(mContext);
        textView.setText(arr[0] );
        textView.setTextSize(20);
        textView.setTypeface(Typeface.SANS_SERIF);
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) textView.getLayoutParams();
        lp.leftMargin = 10;
        lp.topMargin = 10;
        lp.bottomMargin = 10;
        textView.setLayoutParams(lp);
        textViewQty.setText("Qty : " + arr[1]);
        itemsLL.addView(textView);
        itemsLL.addView(textViewQty);
        holder.itemsLinLay.addView(itemsLL);
    }
    RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) holder.itemsLinLay.getLayoutParams();
    lp.bottomMargin = 20;
    holder.itemsLinLay.setLayoutParams(lp);
    holder.confirmOrderBt.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            orderStateCall = restaurantApi.setOrderState(restaurantId,orderList.get(position).getOrderId(),1);
            orderStateCall.clone().enqueue(orderStateCallback);

        }
    });
    if(!holder.running)
    {
        holder.clock.setBase(offesets.get(position));
        holder.clock.start();
        holder.running = true;
    }



}


    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder {
        CardView parent;
        Button confirmOrderBt;
        Timer timer;
      Chronometer clock ;
        LinearLayout itemsLinLay;
        boolean running ;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            parent = itemView.findViewById(R.id.parentCardView);
            confirmOrderBt = itemView.findViewById(R.id.bt_confirm_order);
            timer = new Timer();
            clock = itemView.findViewById(R.id.orderChrono);
           itemsLinLay = itemView.findViewById(R.id.itemsLinLay);
           running = false;


        }
    }

}
