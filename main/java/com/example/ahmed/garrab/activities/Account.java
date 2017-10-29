package com.example.ahmed.garrab.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.ahmed.R;
import com.example.ahmed.garrab.controllers.MyApplication;
import com.example.ahmed.garrab.controllers.UserController;

public class Account extends AppCompatActivity {

    EditText fname;
    EditText lname;
    EditText email;
    EditText password;
    EditText address;
    EditText phone;
    Button btn_update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fname = (EditText) findViewById(R.id.fnameu);
        lname = (EditText) findViewById(R.id.lnameu);
        email = (EditText) findViewById(R.id.email2u);
        password = (EditText) findViewById(R.id.password2u);
        address = (EditText) findViewById(R.id.addressu);
        phone = (EditText) findViewById(R.id.phoneu);
        btn_update = (Button) findViewById(R.id.UPDATE);

        fname.setText(MyApplication.getCustomer().getfName());
        lname.setText(MyApplication.getCustomer().getlName());
        email.setText(MyApplication.getCustomer().getEmail());
        password.setText(MyApplication.getCustomer().getPassword());
        address.setText(MyApplication.getCustomer().getAddress());
        phone.setText(MyApplication.getCustomer().getPhone());

        final UserController userController = new UserController(this);

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userController.update(fname.getText().toString(), lname.getText().toString(), email.getText().toString()
                        , password.getText().toString(), address.getText().toString(), phone.getText().toString()
                        , MyApplication.getCustomer().getGender(), "update");
            }
        });
    }

}
