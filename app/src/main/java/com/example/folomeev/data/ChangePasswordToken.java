package com.example.folomeev.data;

import com.google.gson.annotations.SerializedName;

public class ChangePasswordToken {

    @SerializedName("type")
    public String type;

    @SerializedName("email")
    public String email;

    @SerializedName("token")
    public String token;

    public ChangePasswordToken(String type, String email, String token) {
        this.type = type;
        this.email = email;
        this.token = token;
    }

    public String getType() { return type; }

    public void setType(String type) { this.type = type;}

    public String getEmail() {return email; }

    public void setEmail(String email) {this.email = email;}

    public String getToken() { return token; }

    public void setToken(String token) {this.token = token;}

}
