package com.oslsoftware.restaurantapp;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;


public class Login extends Fragment {
    private FirebaseAuth mAuth;
    private Retrofit retrofit;
    View root;
    TextView RegisterTextView;
    EditText loginUserName, loginPassword,loginSecureToken;
    Button loginBt;
    RegisterFragment Register ;

    public void setRetrofit(Retrofit retrofit) {
        this.retrofit = retrofit;
    }

    public Login() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Register = new RegisterFragment();
        mAuth = FirebaseAuth.getInstance();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_login, container, false);
        InitViews();
        return root;
    }

    public void InitViews() {
        RegisterTextView = root.findViewById(R.id.TextRegister);
        loginUserName = root.findViewById(R.id.loginUserName);
        loginPassword = root.findViewById(R.id.loginPassword);
        loginSecureToken = root.findViewById(R.id.login_SecureToken);
        loginBt = root.findViewById(R.id.loginButton);
        RegisterTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Register.setRetrofit(retrofit);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.MainActContainer, Register).commit();
            }
        });
        loginBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String UserName = loginUserName.getText().toString();
                String PassWord = loginPassword.getText().toString();
                //String loginToken = loginSecureToken.getText().toString();
                String loginToken = "6f2d5c8e9a6b6a6e6c5b2f7f5aff";
                //TODO : pack data and make api call to athenticate

               /*Intent intent = new Intent (getActivity(),content.class);
               startActivity(intent);*/
                //mAuth.getCurrentUser().getUid().hashCode();
                mAuth.signInWithEmailAndPassword(UserName,PassWord)
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful())
                                {
                                    //TODO : make a modal dialog to show progress
                                    Toast.makeText(getContext(),"Signed  in  Successfully",Toast.LENGTH_SHORT);
                                   MainActivity.RestaurantApi restaurantApi = retrofit.create(MainActivity.RestaurantApi.class);
                                    Call<callStatus> verifyTokenCall = restaurantApi.verifyToken(loginToken);
                                    Callback<callStatus> verifyTokenCallback = new Callback<callStatus>() {
                                        @Override
                                        public void onResponse(Call<callStatus> call, Response<callStatus> response) {
                                            if(response.body().getStatus() == 0)
                                            {
                                                Intent intent = new Intent (getActivity(),content.class);
                                                intent.putExtra("token",loginToken);
                                                startActivity(intent);

                                            }
                                            if(response.body().getStatus() == -1)
                                            {
                                                AlertDialog dialog = new AlertDialog.Builder(getContext()).create();
                                                dialog.setMessage("The Secure token is not valid verify it or Contact us");
                                                dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                    }
                                                });
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<callStatus> call, Throwable t) {
                                            AlertDialog dialog = new AlertDialog.Builder(getContext()).create();
                                            dialog.setMessage("Something Wen wrong");
                                            dialog.setButton(AlertDialog.BUTTON_POSITIVE, "Retry", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    getActivity().recreate();
                                                }
                                            });

                                        }
                                    };
                                    verifyTokenCall.enqueue(verifyTokenCallback);
                                    //Intent intnet = new Intent(getActivity(),)
                                    //startActivity(intent);
                                }
                                else
                                {
                                    Toast.makeText(getContext(),"Signing in Failed",Toast.LENGTH_SHORT);
                                    Exception e = task.getException();
                                    Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                                    //if(e == 1st exception )
                                    //if(e ==  2nd exception)
                                }
                            }
                        });
            }
        });
    }
}