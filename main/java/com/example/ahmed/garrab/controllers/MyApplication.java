package com.example.ahmed.garrab.controllers;

import android.app.Application;
import android.content.Context;

import com.example.ahmed.garrab.model.Customer;

/**
 * Created by ESC on 6/18/2016.
 */
public class MyApplication extends Application {

    private static UserController userController;
    private static Context CurrentContext;

    private static String email;
    private static String password;
    private static Customer customer;

    public static Customer getCustomer() {
        return customer;
    }

    public static void setCustomer(Customer customer) {
        MyApplication.customer = customer;
    }

    public static String getEmail() {
        return email;
    }

    public static String getPassword() {
        return password;
    }

    public static void setEmail(String email) {
        MyApplication.email = email;
    }

    public static void setPassword(String password) {
        MyApplication.password = password;
    }

    public static UserController getUserController() {
        return MyApplication.userController;
    }

    public static Context getCurrentContext() {
        return CurrentContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MyApplication.CurrentContext = getApplicationContext();
    }
}
