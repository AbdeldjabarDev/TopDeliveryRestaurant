package com.oslsoftware.restaurantapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.DataSetObserver;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.math.MathUtils;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class EarningsFragment extends Fragment {

Spinner periodSpinner;
TextView totalOrders,totalEarnings;
Call<OrderStats> getAllOrdersCall;
Callback<OrderStats> getAllOrdersCallback;
Retrofit retrofit;
String restaurantId;
Context mContext;
OrderStats stats;
RecyclerView recyclerView;
CategoriesAdapter categoriesAdapter;
List<String> categories;

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public void setFoodItemList(Map<String, ArrayList<FoodItem2>> foodItemList) {
        this.foodItemList = foodItemList;
    }

    Map<String,ArrayList<FoodItem2>> foodItemList;
String getMonthName(int month)
{
    switch (month)
    {
        case 1 : return "January";
        case 2: return "February";
        case 3: return "March";
        case 4:return "April";
        case 5: return "May";
        case 6:return "June";
        case 7:return "July";
        case 8:return "August";
        case 9:return "September";
        case 10:return "October";
        case 11:return "November";
        case 12:return "December";
    }
    return null;
}
    String getMonthName(OrderStats stats,int month)
    {
        switch (month)
        {
            case 1 : return stats.getJanuary();
            case 2: return stats.getFebruary();
            case 3: return stats.getMarch();
            case 4:return stats.getApril();
            case 5: return stats.getMay();
            case 6:return stats.getJune();
            case 7:return stats.getJuly();
            case 8:return stats.getAugust();
            case 9:return stats.getSeptember();
            case 10:return stats.getOctober();
            case 11:return stats.getNovember();
            case 12:return stats.getDecember();
        }
        return null;
    }
    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public void setRetrofit(Retrofit retrofit) {
        this.retrofit = retrofit;
    }

    public EarningsFragment() {
        // Required empty public constructor
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_earnings, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        periodSpinner = view.findViewById(R.id.periodSpinner);
        totalOrders = view.findViewById(R.id.txtTotalOrders);
        totalEarnings = view.findViewById(R.id.txtTotalEarnings);
        recyclerView = view.findViewById(R.id.ItemsStateRecView);
        categoriesAdapter = new CategoriesAdapter(mContext);

    }
    @Override
    public   void onStart() {
        super.onStart();

        getAllOrdersCall =  retrofit.create(MainActivity.RestaurantApi.class).getAllOrders(restaurantId);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity().getApplicationContext(),
                R.array.periods, android.R.layout.simple_spinner_item);

// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        periodSpinner.setAdapter(adapter);
        categoriesAdapter.setRestaurantApi(retrofit.create(MainActivity.RestaurantApi.class));
        categoriesAdapter.setRestaurantId(restaurantId);
       categoriesAdapter.setCategories(categories);
       categoriesAdapter.setFoodItemList(foodItemList);
       recyclerView.setAdapter(categoriesAdapter);
       recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
       for(String key : foodItemList.keySet())
       {
           for(FoodItem2 items : foodItemList.get(key))
           {
               Log.d("EarningsFragment","category : " + key + "item : " + items);
           }

       }
        getAllOrdersCallback = new Callback<OrderStats>() {
            @Override
            public void onResponse(Call<OrderStats> call, Response<OrderStats> response) {
                if(response.body() != null)
                {
                 stats = response.body();

                }
                else
                {
                    Toast.makeText(mContext, "Warning got empty response for all orders", Toast.LENGTH_LONG).show();
                    Log.d("EarningsFragment","Warning got empty response for all orders");
                }
            }
            @Override
            public void onFailure(Call<OrderStats> call, Throwable t) {
                AlertDialog dialog = new AlertDialog.Builder(mContext).create();
                dialog.setMessage("Error getting earnings " + t.toString());

                dialog.setButton(AlertDialog.BUTTON_POSITIVE, "Retry", (dialog1, which) -> call.clone().enqueue(this));
                dialog.setButton(AlertDialog.BUTTON_NEGATIVE,"Give up", (dialog12, which) -> {
                    dialog12.cancel();

                });
                dialog.show();
            }
        };
       getAllOrdersCall.enqueue(getAllOrdersCallback);
        periodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Date date = new Date();

switch (position)
{
    case 0:
        if(stats != null)
        {
            String arr[] = stats.getToday().split(",");
            totalEarnings.setText(arr[0]);
            totalOrders.setText(arr[1] + " DZD");
        }
        else
        {
            Toast.makeText(mContext, "An Error occured while getting your stats try again later", Toast.LENGTH_SHORT).show();
        }
        break;
    case 1:
if(stats != null)
{
    String orderValue[] = getMonthName(stats,date.getMonth()).split(",");
    totalEarnings.setText(orderValue[0]);
    totalOrders.setText(orderValue[1] + " DZD");
}
else
{
    Toast.makeText(mContext, "An Error occured while getting your stats try again later", Toast.LENGTH_SHORT).show();
}

        break;

    case 2:
if(stats != null)
{
    if(date.getMonth() != 1)
    {
        String orderValue1[] = getMonthName(stats,date.getMonth()-1).split(",");
        totalEarnings.setText(orderValue1[0]);
        totalOrders.setText(orderValue1[1] + " DZD");
    }

    else
    {
        Toast.makeText(mContext, "This is the first month in the year", Toast.LENGTH_SHORT).show();
        totalEarnings.setText("0");
        totalOrders.setText("0 DZD");
    }
}
else
{
    Toast.makeText(mContext, "An Error occured while getting your stats try again later", Toast.LENGTH_SHORT).show();
}

        break;
}
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                getAllOrdersCall.
                        enqueue(getAllOrdersCallback);
            }

        });


    }
}