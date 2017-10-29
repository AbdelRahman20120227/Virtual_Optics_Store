package com.example.ahmed.garrab.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.ahmed.R;
import com.example.ahmed.garrab.controllers.BrandController;
import com.example.ahmed.garrab.controllers.GlassesController;
import com.example.ahmed.garrab.controllers.UserController;

public class Login extends AppCompatActivity {

    Button register;
    Button store;
    Button login;
    EditText email;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Virtual optics store");

        // load glasses in store
        GlassesController.getGlasses();

        // load brands
        BrandController.getBrands();
        clickLogin();
        onClickRegister();
        onClickStore();
    }

    public void clickLogin() {
        login = (Button) findViewById(R.id.login);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        final UserController userController = new UserController(this);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //call login service
                userController.login(email.getText().toString(), password.getText().toString(), "login");
            }
        });
    }

    public void onClickRegister() {
        register = (Button) findViewById(R.id.register1);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inten = new Intent("com.example.ahmed.garrab.activities.Register");
                startActivity(inten);
            }
        });
    }

    public void onClickStore() {
        store = (Button) findViewById(R.id.store1);
        store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inten = new Intent("com.example.ahmed.garrab.activities.Store");
                startActivity(inten);
            }
        });
    }

}
