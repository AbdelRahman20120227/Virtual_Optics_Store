package com.example.ahmed.garrab.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ahmed.R;
import com.example.ahmed.garrab.controllers.UserController;

public class Register extends AppCompatActivity {

    Button register;
    EditText fname;
    EditText lname;
    EditText email;
    EditText password;
    EditText address;
    EditText phone;
    EditText gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        clickRegister();
    }

    public void clickRegister() {
        register = (Button) findViewById(R.id.registerButton);
        fname = (EditText) findViewById(R.id.fname);
        lname = (EditText) findViewById(R.id.lname);
        email = (EditText) findViewById(R.id.email2);
        password = (EditText) findViewById(R.id.password2);
        address = (EditText) findViewById(R.id.address);
        phone = (EditText) findViewById(R.id.phone);
        gender = (EditText) findViewById(R.id.gender);
        final UserController userController = new UserController(this);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fname.getText().toString().equals(""))
                    Toast.makeText(getApplicationContext(), "Enter valid name", Toast.LENGTH_SHORT).show();
                else if (fname.getText().toString().equals(""))
                    Toast.makeText(getApplicationContext(), "Enter valid name", Toast.LENGTH_SHORT).show();
                else if (fname.getText().toString().equals(""))
                    Toast.makeText(getApplicationContext(), "Enter valid Email", Toast.LENGTH_SHORT).show();
                else if (fname.getText().toString().equals(""))
                    Toast.makeText(getApplicationContext(), "Enter valid password", Toast.LENGTH_SHORT).show();
                else {
                    //call register service
                    userController.register(fname.getText().toString(), lname.getText().toString(), email.getText().toString()
                            , password.getText().toString(), address.getText().toString(), phone.getText().toString()
                            , gender.getText().toString(), "register");
                }
            }
        });
    }
}
