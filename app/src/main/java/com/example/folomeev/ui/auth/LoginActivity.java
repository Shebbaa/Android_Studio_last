package com.example.folomeev.ui.auth;

import static com.example.folomeev.utils.Utils.APIKEY;
import static com.example.folomeev.utils.Utils.BASE_URL;
import static com.example.folomeev.utils.Utils.GRANT_TYPE;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.folomeev.R;
import com.example.folomeev.controller.API;
import com.example.folomeev.data.ResponseUser;
import com.example.folomeev.data.User;
import com.example.folomeev.ui.HomeActivity;
import com.example.folomeev.utils.Utils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    TextInputLayout email, password;
    MaterialButton button;
    Retrofit retrofit;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        email = findViewById(R.id.logInEmailAddress);
        password = findViewById(R.id.LoginPasswordTextInputLayout);
        button = findViewById(R.id.button_signin);


        email.getEditText().setText(Utils.user.getEmail());
        password.getEditText().setText(Utils.user.getPassword());

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        API api = retrofit.create(API.class);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = new User(String.valueOf(email.getEditText().getText()), String.valueOf(password.getEditText().getText()));
                //запрос к базе данных на авторизацию пользователя
                Call<ResponseUser> call = api.login(GRANT_TYPE, APIKEY, user);
                call.enqueue(new Callback<ResponseUser>() {
                    @Override
                    public void onResponse(Call<ResponseUser> call, Response<ResponseUser> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "sign in ok", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                        } else {
                            Toast.makeText(LoginActivity.this, response.code() + "", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseUser> call, Throwable t) {

                        Toast.makeText(LoginActivity.this, t.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    public void signup(View view) {
        startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
    }

    public void forgot(View view) {
        startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
    }
}