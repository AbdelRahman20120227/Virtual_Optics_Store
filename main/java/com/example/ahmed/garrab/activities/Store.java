package com.example.ahmed.garrab.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.ahmed.R;
import com.example.ahmed.garrab.model.Glasses;

import java.util.ArrayList;

public class Store extends AppCompatActivity {

    public static ArrayList<String> BrandsList = new ArrayList<>();
    public static ArrayList<Glasses> rows = new ArrayList<>();
    public static ArrayList<String> paths = new ArrayList<>();
    public static ArrayList<String> brands = new ArrayList<>();
    public static ArrayList<String> names = new ArrayList<>();
    public static ArrayList<Double> price = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

}
