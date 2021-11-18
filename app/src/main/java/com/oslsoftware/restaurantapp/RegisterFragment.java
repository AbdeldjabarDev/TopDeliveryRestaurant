package com.oslsoftware.restaurantapp;

import android.accounts.NetworkErrorException;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApiNotAvailableException;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
public class RegisterFragment extends Fragment {
private Retrofit retrofit;

    public void setRetrofit(Retrofit retrofit) {
        this.retrofit = retrofit;
    }

    FirebaseAuth mAuth;
public void DisplayMessageInDialog(AlertDialog dialog,String message)
{
    dialog.setMessage(message);
    Thread th= new Thread(new Runnable() {
        @Override
        public void run() {
            dialog.show();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            dialog.dismiss();
        }
    });
    th.start();
}
    public RegisterFragment()
    {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       mAuth = FirebaseAuth.getInstance();
    }
    public void showToastMessage(String text)
    {
        Toast.makeText(getContext(),text,Toast.LENGTH_SHORT).show();
    }

/*public interface RestaurantApi
{
    @POST("/Register")
    Call<callStatus> Register(@Body Userinfo info);
    @POST("/set-Order-state/{orderId}")
    Call<callStatus> setOrderState(@Body int state);
    @GET("/Verify-Token/{token}")
    Call<callStatus> verifyToken(@Path("token") String token);
    @GET("/get-Orders/{restaurantId}")
    Call<List<Order>> getOrders(@Path("restaurantId") String restaurantId);

}*/
    EditText email,password,confirmPassword;
    Button register_button;

    @Override
    public void onStart() {
        super.onStart();
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Registering ...");
        Callback<callStatus> registerCallback = new Callback<callStatus>() {
            @Override
            public void onResponse(Call<callStatus> call, Response<callStatus> response) {
                progressDialog.dismiss();
                AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
                if(response.body().getStatus() == 0)
                {
                    /*mAuth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                         DisplayMessageInDialog(dialog,"You have been logged in successfully");
                        }
                    });*/


                }
                if(response.body().getStatus() == -1)
                {
                    //TODO : verify if the message contains "email used" and inform user
                    progressDialog.dismiss();
                    dialog.setButton(AlertDialog.BUTTON_POSITIVE, "Retry", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                         

                        }
                    });
                   DisplayMessageInDialog(dialog,"Something went wrong !" + response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<callStatus> call, Throwable t) {
                AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
                progressDialog.dismiss();
               DisplayMessageInDialog(dialog,"Something went wrong " + t.getMessage());

            }
        };
        Callback<callStatus> setOrderStateCallback = new Callback<callStatus>() {
            @Override
            public void onResponse(Call<callStatus> call, Response<callStatus> response) {
                if(response.body().getStatus() == 0)
                {

                }
                else
                {

                }
            }

            @Override
            public void onFailure(Call<callStatus> call, Throwable t) {

            }
        };
        Callback<List<Order>> getOrdersCallback = new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {

            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {

            }
        };
        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pass = password.getText().toString();
                String confpass = confirmPassword.getText().toString();
                String em = email.getText().toString();
                if(pass.equals("") || em.equals("")  || confpass.equals(""))
                {
                    showToastMessage("There are Empty Fields Please fill them ");
                }
                if(!pass.equals(confpass))
                {
                    showToastMessage("Passwords don't match");
                }
                else
                {
                    progressDialog.show();
                    Userinfo info = new Userinfo();
                    info.setUser_email(em);
                    info.setUser_password(pass);
                    MainActivity.RestaurantApi restaurantApi = retrofit.create(MainActivity.RestaurantApi.class);
                    Call<callStatus> register = restaurantApi.Register(info);
                    //Call<callStatus> setOrderStateCall = restaurantApi.setOrderState()
                    register.enqueue(registerCallback);
  /*                  mAuth.createUserWithEmailAndPassword(em,pass).addOnCompleteListener(getActivity(),new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                Userinfo userInfo = new Userinfo(phone,card);
                                db.collection("Clients").document(mAuth.getCurrentUser().getUid()).set(userInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful())
                                        {
                                            Intent intent = new Intent(getActivity(),content.class);
                                            startActivity(intent);
                                        }
                                        else
                                        {
                                            //TODO : error handling
                                        }
                                    }
                                });

                            }
                            else
                            {
                                Exception exception = task.getException();
                                if(exception instanceof FirebaseAuthInvalidCredentialsException)
                                {

                                    showToastMessage(exception.toString());

                                }
                                if(exception instanceof FirebaseAuthException)
                                {
                                    showToastMessage("Firebase Auth Exception");
                                }
                                if(exception instanceof FirebaseAuthWeakPasswordException)
                                {
                                    showToastMessage("Weak password exception");
                                }



                            }
                        }
                    });
*/
                }
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_register, container, false);
        email = root.findViewById(R.id.inputUserName);
        password = root.findViewById(R.id.inputPassword);
        confirmPassword = root.findViewById(R.id.inputConfirmPassword);
        register_button = root.findViewById(R.id.register_button);
        return root;
    }

}