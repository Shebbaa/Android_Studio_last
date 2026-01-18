package com.example.folomeev.ui;

import static com.example.folomeev.utils.Utils.APIKEY;
import static com.example.folomeev.utils.Utils.BASE_URL;
import static com.example.folomeev.utils.Utils.TOKEN;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.chaos.view.PinView;
import com.example.folomeev.R;
import com.example.folomeev.controller.API;
import com.example.folomeev.data.ChangePasswordToken;
import com.example.folomeev.data.Email;
import com.example.folomeev.data.ResponseUser;
import com.google.android.material.button.MaterialButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OTPVerificationActivity extends AppCompatActivity {

    TextView resendText;
    boolean runningTimer;
    MaterialButton verifyButton;
    CountDownTimer resetCountDownTimer;

    PinView pinView;
    Retrofit retrofit;
    String verificationType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpverification);

        verifyButton = findViewById(R.id.SetNewPass);
        resendText = findViewById(R.id.resendAfter);
        pinView = findViewById(R.id.pinView);


        String emailPass = getIntent().getStringExtra("email");
        verificationType = getIntent().getStringExtra("type");

        if (verificationType == null) verificationType = "signup";
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        API api = retrofit.create(API.class);


        resetCountDownTimer = new CountDownTimer(59000, 1000) {
            @Override
            public void onTick(long l) {
                resendText.setText(String.format("resend after 0:%02d", (l / 1000)));
            }

            @Override
            public void onFinish() {
                resendText.setText("resend");
                resendText.setTextColor(Color.parseColor("#006CEC"));
                runningTimer = false;
                resendText.setOnClickListener(v -> {
                    ResetTimer();
                    Email emailObj = new Email(emailPass);
                    api.sendCode(APIKEY, emailObj).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(OTPVerificationActivity.this, "Code Resent", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Toast.makeText(OTPVerificationActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                });
            }
        }.start();

        verifyButton.setOnClickListener(v -> {
            String code = pinView.getText().toString();

            if (code.length() == 6) {
                ChangePasswordToken changePasswordToken = new ChangePasswordToken(
                        verificationType,
                        emailPass,
                        code
                );

                api.verifyCode(APIKEY, changePasswordToken).enqueue(new Callback<ResponseUser>() {
                    @Override
                    public void onResponse(Call<ResponseUser> call, Response<ResponseUser> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            TOKEN = response.body().accessToken;

                            if (verificationType.equals("recovery")) {
                                Intent intent = new Intent(OTPVerificationActivity.this, NewPasswordActivity.class);
                                intent.putExtra("passedEmail", emailPass);
                                intent.putExtra("newToken", TOKEN);
                                startActivity(intent);
                            } else {
                                Toast.makeText(OTPVerificationActivity.this, "Success Registration!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(OTPVerificationActivity.this, HomeActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                            finish();
                        } else {
                            pinView.setLineColor(ResourcesCompat.getColor(getResources(), R.color.red, getTheme()));
                            Toast.makeText(OTPVerificationActivity.this, "Invalid Code", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseUser> call, Throwable t) {
                        Toast.makeText(OTPVerificationActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(OTPVerificationActivity.this, "Enter 6-digit code", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void ResetTimer() {
        if (!runningTimer) {
            runningTimer = true;
            resendText.setOnClickListener(null);
            resendText.setTextColor(Color.parseColor("#A7A7A7"));
            resetCountDownTimer.start();
        }
    }
}