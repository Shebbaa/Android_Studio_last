package com.example.folomeev.ui;

import static com.example.folomeev.utils.Utils.APIKEY;
import static com.example.folomeev.utils.Utils.BASE_URL;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.folomeev.R;
import com.example.folomeev.controller.API;
import com.example.folomeev.data.User;
import com.example.folomeev.utils.Utils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignUpActivity extends AppCompatActivity { //активность обработки запроса на регистрацию пользователя

    TextInputLayout name, phone, email, pswrd, pswrdConfirm;
    MaterialButton button;
    Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        name = findViewById(R.id.fullname);
        phone = findViewById(R.id.phoneNumber);
        email = findViewById(R.id.emailAddress);
        pswrd = findViewById(R.id.passwordTextInputLayout);
        pswrdConfirm = findViewById(R.id.confirmPasswordTextInputLayout);
        button = findViewById(R.id.button_signup);

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        API api = retrofit.create(API.class);

        button.setOnClickListener(new View.OnClickListener() {
            //регистрация пользователя на сервере
            @Override
            public void onClick(View view) {
                //создание объекта пользователь
                Utils.user = new User(
                        String.valueOf(name.getEditText().getText()),
                        String.valueOf(phone.getEditText().getText()),
                        String.valueOf(email.getEditText().getText()),
                        String.valueOf(password.getEditText().getText())
                );
                //запрос к базе данных на регистрацию пользователя
                Call<Void> call = api.registration(APIKEY, Utils.user);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(SignUpActivity.this, "sign up ok", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SignUpActivity.this, LogInActivity.class));
                        } else {
                            Toast.makeText(SignUpActivity.this, response.code() + "", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(SignUpActivity.this, t.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    public void signin(View view) {
        startActivity(new Intent(SignUpActivity.this, LogInActivity.class));
    }
}