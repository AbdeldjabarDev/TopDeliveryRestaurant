package com.oslsoftware.restaurantapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class content extends AppCompatActivity {
    Retrofit retrofit;
    okhttp3.OkHttpClient client;
    //Moshi moshi;
    okhttp3.OkHttpClient.Builder builder;
    OrdersFragment ordersFragment;
    EarningsFragment earningsFragment;
    AccountFragment accountFragment;
    BottomNavigationView bottomNavView;
MainActivity.RestaurantApi restaurantApi;
FragmentManager mFragmentManager;
ProgressDialog progressDialog;
Call<String> resIdCall;
String token ;
String restaurantId;
    FirebaseFirestore firestore;
Map<String,ArrayList<FoodItem2>> foodItemList;
List<String> categories;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Waiting for food items");
        progressDialog.setCancelable(true);
        firestore = FirebaseFirestore.getInstance();
        foodItemList = new HashMap<>();
        getSupportActionBar().hide();

        DocumentReference myRestaurantRef = firestore.collection("/Restaurants").document("PIZZERIA ZOOM");
        categories =  Arrays.asList("pizzas","sandwiches","burgers","plats","drinks");
        AtomicReference<Integer> depth = new AtomicReference<>(5);
        for(String category : categories)
        {
            myRestaurantRef.collection(category).get().addOnCompleteListener(task -> {

                ArrayList<FoodItem2> buffer = new ArrayList<>();
                if (task.isSuccessful()) {
                    Log.d("contentActivity","Task completed successfully for category " + category);
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        Log.d("contentActivity","item" + documentSnapshot.toObject(FoodItem.class).getName() + "in category " + category);
                     buffer.add(new FoodItem2(documentSnapshot.toObject(FoodItem.class),documentSnapshot.getId()));
                    }
                    depth.getAndSet(depth.get() - 1);
                    foodItemList.put(category,buffer);
                    if(depth.get() == 0) // doesn't work !!
                    {

                        Log.d("contentActivity","depth is 0");
                        progressDialog.dismiss();

                    }

                } else
                {
                    Toast.makeText(getApplicationContext(), "Failed to fetch Restaurant list: " + task.getException().toString(), Toast.LENGTH_SHORT).show();
                    Log.d("contentActivity","Failed to fetch Restaurant list: " + task.getException().toString());
                }
            });

        }

        builder = new OkHttpClient.Builder();
        builder.readTimeout(14, TimeUnit.HOURS);
        builder.connectTimeout(10,TimeUnit.SECONDS);
        client = builder.build();
        retrofit = new Retrofit.Builder().baseUrl("http://192.168.43.247:10000").addConverterFactory(GsonConverterFactory.create()).client(client).build();
        restaurantApi = retrofit.create(MainActivity.RestaurantApi.class);
        bottomNavView = findViewById(R.id.bottom_nav);
        mFragmentManager = getSupportFragmentManager();
        ordersFragment = new OrdersFragment();
     ordersFragment.setRetrofit(retrofit);
        earningsFragment = new EarningsFragment();
        earningsFragment.setRetrofit(retrofit);
        earningsFragment.setCategories(categories);
        earningsFragment.setFoodItemList(foodItemList);
   progressDialog.show();
        accountFragment = new AccountFragment();
        bottomNavView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.orders:
                        mFragmentManager.beginTransaction().replace(R.id.content_container,ordersFragment).commit();
                        return  true;
                    case R.id.earnings :

                        mFragmentManager.beginTransaction().replace(R.id.content_container,earningsFragment).commit();
                        return true;
                    case R.id.account:
                        mFragmentManager.beginTransaction().replace(R.id.content_container,accountFragment).commit();

                        return true;
                }
                return false;
            }
        });
        /*token = getIntent().getStringExtra("token");
        if(token != null)
        resIdCall  = restaurantApi.getRestaurantId(token);
        else
         Toast.makeText(this, "warning token is null", Toast.LENGTH_SHORT).show();
        try {
            restaurantId = resIdCall.execute().body();
            ordersFragment.setRestaurantId(restaurantId);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "couldn't get restaurant ID : " + e.toString(), Toast.LENGTH_SHORT).show();
        }*/
        ordersFragment.setRestaurantId("1234567891011");
        earningsFragment.setRestaurantId("1234567891011");
        mFragmentManager.beginTransaction().replace(R.id.content_container,ordersFragment).commit();
    }
}