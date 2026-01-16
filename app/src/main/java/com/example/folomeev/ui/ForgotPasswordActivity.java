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
import com.example.folomeev.data.Email;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ForgotPasswordActivity extends AppCompatActivity {

    TextInputLayout emailField;
    MaterialButton button;
    Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        emailField = findViewById(R.id.forgotEmailAddress);
        button = findViewById(R.id.button_forgot);

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        API api = retrofit.create(API.class);


        Email email = new Email(String.valueOf(emailField.getEditText().getText()));

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<Void> call = api.sendCode(APIKEY, email);


                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Toast.makeText(ForgotPasswordActivity.this, "Send OK", Toast.LENGTH_SHORT).show();


                        if (isEmailValid(String.valueOf(emailField.getEditText().getText()))) {
                            String emailText = emailField.getEditText().getText().toString();


                            Intent intent = new Intent(ForgotPasswordActivity.this, OTPVerificationActivity.class);
                            intent.putExtra("email", emailText);
                            intent.putExtra("emailRepeat", String.valueOf(emailField.getEditText().getText()));
                            startActivityForResult(intent, 0);
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {

                        Toast.makeText(ForgotPasswordActivity.this, t.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    public void signin2(View view) {
        startActivity(new Intent(ForgotPasswordActivity.this, LogInActivity.class));
    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}