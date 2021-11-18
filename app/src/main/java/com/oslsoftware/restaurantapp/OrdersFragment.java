package com.oslsoftware.restaurantapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.SystemClock;
import android.provider.AlarmClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;


public class OrdersFragment extends Fragment {
   private Retrofit retrofit;
    RecyclerView mainRecView;
    OrdersAdapter adapter;
TextView noOrderstext;
Context mContext;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    private String restaurantId;

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    View view;
    MainActivity.RestaurantApi restaurantApi;
    public void setRetrofit(Retrofit retrofit) {
        this.retrofit = retrofit;
    }


    public OrdersFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     //    view = getActivity().getLayoutInflater().inflate(R.layout.order_item,null);
        adapter = new OrdersAdapter(getContext());
        adapter.setRestaurantId(restaurantId);
    }

    @Override
    public void onStart() {
        super.onStart();
        Callback<List<Order>> getOrdersCallback = new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                Call<List<Order>> getOrders = call.clone();
               getOrders.enqueue(this);
                Toast.makeText(mContext, "call enqueued and response got ", Toast.LENGTH_SHORT).show();
               List<Order> orders = response.body();
               adapter.getOffesets().add(SystemClock.elapsedRealtime());
               adapter.getOrderList().addAll(orders);
               adapter.notifyDataSetChanged();
              /* for(Order order:orders)
               {

                 CardView parent = view.findViewById(R.id.parentCardView);
                 LinearLayout itemsLinLay = view.findViewById(R.id.itemsLinLay);
                   for(String item:order.items)
                   {
                       TextView textView = new TextView(getActivity());
                       String[] arr = item.split("/*");
                       textView.setText(arr[0] + "Qty : " + arr[1] );
                       itemsLinLay.addView(textView);
                   }
                   /*TimerTask task = new TimerTask() {
                       @Override
                       public void run() {

                       }
                   };
                   Timer timer = new Timer();
                   timer.schedule(task,1000);
                   TextView txtView = new TextView();

                   mainLinLay.addView(parent);
               }*/
            }
            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
               // Toast.makeText(getContext(), "failed due to : " + t.toString(), Toast.LENGTH_SHORT).show();
                Log.d("OF","failed due to " + t.toString());
                AlertDialog dialog = new AlertDialog.Builder(mContext).create();
                dialog.setMessage("Error getting earnings ");
                Callback<List<Order>> thisObject = this;
                dialog.setButton(AlertDialog.BUTTON_POSITIVE, "Retry", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        call.clone().enqueue(thisObject);
                    }
                });
                dialog.show();
            }
        };
        restaurantApi = retrofit.create(MainActivity.RestaurantApi.class);
        //Call<List<Order>> getOrders = restaurantApi.getOrders(restaurantId);
        //getOrders.enqueue(getOrdersCallback);
        adapter.setRestaurantApi(restaurantApi);
        mainRecView.setAdapter(adapter);
        mainRecView.setLayoutManager(new LinearLayoutManager(mContext));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_orders, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mainRecView = view.findViewById(R.id.MainRecView);
        noOrderstext = view.findViewById(R.id.txtNoOrders);
        if(adapter.getOrderList().isEmpty())
        {
            noOrderstext.setVisibility(View.GONE);
        }
     }

}