package com.example.folomeev.ui;

import static com.example.folomeev.utils.Utils.APIKEY;
import static com.example.folomeev.utils.Utils.BASE_URL;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;

import com.example.folomeev.R;
import com.example.folomeev.controller.API;
import com.example.folomeev.data.User;
import com.example.folomeev.data.ResponseUser;
import com.example.folomeev.utils.Utils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignUpActivity extends AppCompatActivity {

    AppCompatCheckBox checkBox;
    MaterialButton button;
    TextView signIntext;
    TextInputLayout email, password, passwordConfirm;
    Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        checkBox = findViewById(R.id.signupCheckBox);
        button = findViewById(R.id.button_signup);
        signIntext = findViewById(R.id.textView17);
        email = findViewById(R.id.emailAddress);
        password = findViewById(R.id.passwordTextInputLayout);
        passwordConfirm = findViewById(R.id.confirmPasswordTextInputLayout);

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        API api = retrofit.create(API.class);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String e = String.valueOf(email.getEditText().getText());
                String p1 = String.valueOf(password.getEditText().getText());
                String p2 = String.valueOf(passwordConfirm.getEditText().getText());

                if (checkBox.isChecked()
                        && isEmailValid(e)
                        && p1.equals(p2)
                        && e.equals(e.toLowerCase())) {


                    User user = new User(e, p1);

                    Call<ResponseUser> call = api.signUpByEmailAndPassword(APIKEY, user);
                    call.enqueue(new Callback<ResponseUser>() {
                        @Override
                        public void onResponse(Call<ResponseUser> call, Response<ResponseUser> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(SignUpActivity.this, "User create", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(SignUpActivity.this, HomeActivity.class));
                            } else {
                                Toast.makeText(SignUpActivity.this, response.code() + "", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseUser> call, Throwable t) {
                            Toast.makeText(SignUpActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

    }



    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


    public void signin(View view) {
        startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
    }

}
