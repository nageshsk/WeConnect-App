package com.projects.sharathnagendra.weconnect;

import android.widget.EditText;

/**
 * Created by Sharath Nagendra on 10/22/2016.
 */

public class SupporterGetterSetter {

    private String name,email,phone,time,money,days;


    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public void setDays(String days) {
        this.days = days;
    }



///////////////////////////////

    public String getName(){
        return name;
    }

    public String getEmail(){
        return email;
    }

    public String getPhone(){
        return phone;
    }
    public String getTime(){
        return time;
    }
    public String getMoney(){
        return money;
    }
    public String getDays(){
        return days;
    }




}