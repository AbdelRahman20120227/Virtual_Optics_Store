package com.example.ahmed.garrab.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.ahmed.R;
import com.example.ahmed.garrab.adapters.GlassesAdapter;
import com.example.ahmed.garrab.controllers.BrandController;
import com.example.ahmed.garrab.controllers.Filter;
import com.example.ahmed.garrab.model.Glasses;

import java.util.ArrayList;

public class StoreFragment extends Fragment implements AdapterView.OnItemClickListener {

    public static ArrayList<String> brandsList = new ArrayList<>();
    public static ArrayList<Glasses> rows = new ArrayList<>();

    private Spinner spinner_brands;
    private Button btn_filter;
    private String brand;
    private EditText low;
    private EditText high;
    private ListView glasses;

    private Context context;


    public StoreFragment() {
        // Required empty public constructor
    }

    public static Fragment getInstance() {
        return new StoreFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_store, container, false);

//        brandsList = new BrandController(getContext()).getBrands();
        context = getContext();
        attachViewIDs(view);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        brandsList = new BrandController().getBrands();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.store_glasses) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            menu.setHeaderTitle("Options");
//            String[] menuItems = getResources().getStringArray(R.array.brands);
            menu.add("Try NOW!");
            menu.add("Add to favorites");
            menu.add("View Details");
//            for (int i = 0; i < menuItems.length; i++) {
//                menu.add(Menu.NONE, i, i, menuItems[i]);
//            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        return super.onContextItemSelected(item);
    }


    public void attachViewIDs(View view) {
        glasses = (ListView) view.findViewById(R.id.store_glasses);
        glasses.setAdapter(new GlassesAdapter(context, rows));

        glasses.setOnItemClickListener(this);
        registerForContextMenu(glasses);

        spinner_brands = (Spinner) view.findViewById(R.id.spinner_brands);
        btn_filter = (Button) view.findViewById(R.id.btn_filterAction);
        low = (EditText) view.findViewById(R.id.txt_low);
        high = (EditText) view.findViewById(R.id.txt_high);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_dropdown_item_1line,
                brandsList);
        spinner_brands.setAdapter(adapter);
        spinner_brands.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView text = (TextView) view;
                brand = text.getText().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Double lower = tryParse(low.getText().toString());
                Double upper = tryParse(high.getText().toString());
                ArrayList<Glasses> glasses1 = Filter.filterGlasses(brand, lower, upper);

                Log.v("heresize", "" + glasses1.size());
                glasses.setAdapter(new GlassesAdapter(context, glasses1));
            }
        });

    }

    public Double tryParse(String value) {
        Double retVal;
        try {
            retVal = Double.parseDouble(value);
        } catch (NumberFormatException nfe) {
            retVal = null; // or null if that is your preference
        }
        return retVal;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent("com.example.ahmed.garrab.activities.GlassesDetails");
        intent.putExtra("glass", rows.get(position));
        startActivity(intent);
    }
}
