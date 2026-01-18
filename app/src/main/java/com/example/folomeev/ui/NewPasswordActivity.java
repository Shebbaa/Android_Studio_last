package com.example.folomeev.ui;

import static com.example.folomeev.utils.Utils.APIKEY;
import static com.example.folomeev.utils.Utils.BASE_URL;
import static com.example.folomeev.utils.Utils.GRANT_TYPE;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.folomeev.R;
import com.example.folomeev.controller.API;
import com.example.folomeev.data.ResponseUser;
import com.example.folomeev.data.User;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewPasswordActivity extends AppCompatActivity {

    Retrofit retrofit;
    MaterialButton newPassButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);
        newPassButton = findViewById(R.id.button_NewPassSignIn);

        String newToken = getIntent().getStringExtra("newToken");
        String EmailForPassChange = getIntent().getStringExtra("passedEmail");

        TextInputEditText newPassConfirm = findViewById(R.id.newPasswordConfirmField);
        TextInputEditText newPassField = findViewById(R.id.newPasswordField);

        String pass1 = newPassConfirm.getText().toString();
        String pass2 = newPassField.getText().toString();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        API api = retrofit.create(API.class);

        newPassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!pass1.equals(pass2)) {
                    Toast.makeText(NewPasswordActivity.this, "Password's doesn't match", Toast.LENGTH_SHORT).show();
                } else {
                    User user = new User(EmailForPassChange, newPassConfirm.getEditableText().toString());
                    Call<Void> call = api.updatePassword(APIKEY, "Bearer " + newToken, user);
                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(NewPasswordActivity.this, "Password changed", Toast.LENGTH_SHORT).show();
                                Call<ResponseUser> logInCall = api.login(GRANT_TYPE, APIKEY, user);
                                logInCall.enqueue(new Callback<ResponseUser>() {
                                    @Override
                                    public void onResponse(Call<ResponseUser> call, Response<ResponseUser> response) {
                                        if (response.isSuccessful()) {
                                            startActivity(new Intent(NewPasswordActivity.this, HomeActivity.class));
                                        } else {
                                            Toast.makeText(NewPasswordActivity.this, response.code() + "", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseUser> call, Throwable t) {
                                        Toast.makeText(NewPasswordActivity.this, t.getMessage().toString(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable throwable) {
                            Toast.makeText(NewPasswordActivity.this, throwable.getMessage().toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}