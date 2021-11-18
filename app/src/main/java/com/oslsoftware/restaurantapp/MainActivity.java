package com.oslsoftware.restaurantapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public class MainActivity extends AppCompatActivity {
FirebaseAuth auth;

    Retrofit retrofit;
    okhttp3.OkHttpClient client;
    //Moshi moshi;
    okhttp3.OkHttpClient.Builder builder;
    Login login;
    //TODO : make something like access token so the user doesn't have to type the secure token each time
    public interface RestaurantApi
    {
        @POST("/Register")
        Call<callStatus> Register(@Body Userinfo info);
        @POST("/set-Order-state/{restaurantId}/{orderId}/{state}")
        Call<callStatus> setOrderState(@Path("restaurantId") String restaurantId,@Path("orderId") String orderId,@Path("state") int state);
        @POST("/set-state/{restaurantId}/{state}")
        Call<callStatus> setState(@Path("restaurantId") String restaurantId,@Path("state") boolean state);
        @GET("/Verify-Token/{token}")
        Call<callStatus> verifyToken(@Path("token") String token);
        @GET("/get-Orders/{restaurantId}")
        Call<List<Order>> getOrders(@Path("restaurantId") String restaurantId);
        @GET("/get-AllOrders/{restaurantId}")
        Call<OrderStats> getAllOrders(@Path("restaurantId") String restaurantId);//TODO : make something like cache
        @GET("/get-restaurantId/{token}")
        Call<String> getRestaurantId(@Path("token") String token);
        @POST("/set-item-state/{restaurantId}/{category}/{item}/{state}")
        Call<callStatus> setItemState(@Path("restaurantId") String restaurantId,@Path("category") String category,
                                      @Path("item") String itemName,@Path("state") Integer state);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
FirebaseApp.initializeApp(getApplicationContext());
auth = FirebaseAuth.getInstance();

        builder = new OkHttpClient.Builder();
        builder.readTimeout(14, TimeUnit.HOURS);
        builder.connectTimeout(10,TimeUnit.SECONDS);
        client = builder.build();
        retrofit = new Retrofit.Builder().baseUrl("http://192.168.43.247:10000").addConverterFactory(GsonConverterFactory.create()).client(client).build();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(auth.getCurrentUser() == null)
        {
            Intent intent = new Intent(this,content.class);
            startActivity(intent);
        }
        else
        {
            login = new Login();
            login.setRetrofit(retrofit);
            getSupportFragmentManager().beginTransaction().replace(R.id.MainActContainer,login).commit();
        }
    }
}