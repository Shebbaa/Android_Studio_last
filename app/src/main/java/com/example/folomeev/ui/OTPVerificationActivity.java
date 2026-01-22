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
    MaterialButton newPassButton;
    CountDownTimer resetCountDownTimer;

    PinView pinView;
    Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpverification);

        newPassButton = findViewById(R.id.SetNewPass);
        resendText = findViewById(R.id.resendAfter);
        pinView = findViewById(R.id.pinView);
        String emailPass = getIntent().getStringExtra("email");
        String emailRepeat = getIntent().getStringExtra("emailRepeat");
        Email email = new Email(emailRepeat);
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        API api = retrofit.create(API.class);

        resetCountDownTimer = new CountDownTimer(10000,1000){

            @Override
            public void onTick(long l){resendText.setText("resend after 0:" + (l/1000+1));}

            @Override
            public void onFinish(){
                resendText.setText("resend");
                resendText.setTextColor(Color.parseColor("#006CEC"));
                runningTimer = false;
                resendText.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        ResetTimer();
                        Call<Void> call = api.sendCode(APIKEY, email);
                        call.enqueue(new Callback<Void>(){
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response){
                                if (response.isSuccessful()){
                                    Toast.makeText(OTPVerificationActivity.this, "Code Resended", Toast.LENGTH_SHORT).show();
                                }
                            }
                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Toast.makeText(OTPVerificationActivity.this, t.getMessage().toString(),Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        }.start();

        newPassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                if(pinView.getText().toString().length() == 6){
                    ChangePasswordToken changePasswordToken =
                            new ChangePasswordToken("email",emailPass, pinView.getText().toString());
                    Call<ResponseUser> call = api.verifyCode(APIKEY, changePasswordToken);
                    call.enqueue(new Callback<ResponseUser>(){
                        @Override
                        public void onResponse(Call<ResponseUser> call, Response<ResponseUser> response){
                            if(response.isSuccessful()){
                                if(response.body() != null){
                                    TOKEN = response.body().accessToken;
                                    Toast.makeText(OTPVerificationActivity.this, TOKEN + "", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(OTPVerificationActivity.this, NewPasswordActivity.class);
                                    intent.putExtra("passedEmail",emailPass);
                                    intent.putExtra("newToken", TOKEN);
                                    startActivity(intent);
                                }
                            }
                            else {
                                pinView.setLineColor(ResourcesCompat.getColor(getResources(),R.color.red, getTheme()));
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseUser> call, Throwable throwable){
                        }
                    });
                } else {
                    Toast.makeText(OTPVerificationActivity.this, "Введите текст", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void ResetTimer(){
        if(runningTimer == false){
            runningTimer = true;
            resendText.setTextColor(Color.parseColor("#A7A7A7"));
            resetCountDownTimer.start();
        }
    }
}
