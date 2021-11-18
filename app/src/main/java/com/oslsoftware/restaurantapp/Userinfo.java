package com.oslsoftware.restaurantapp;

public class Userinfo {
    String phone_number;
    String card_number;
    String user_name;
    String user_email;
    String user_password;

    public String getUser_name() {
        return user_name;
    }
public Userinfo()
{

}
    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_password() {
        return user_password;
    }

    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }

    public Userinfo(String phone_number, String card_number) {
        this.phone_number = phone_number;
        this.card_number = card_number;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getCard_number() {
        return card_number;
    }

    public void setCard_number(String card_number) {
        this.card_number = card_number;
    }
}
