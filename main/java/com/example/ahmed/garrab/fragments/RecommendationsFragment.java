package com.example.ahmed.garrab.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.ahmed.R;
import com.example.ahmed.garrab.adapters.GlassesAdapter;
import com.example.ahmed.garrab.model.Glasses;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecommendationsFragment
        extends Fragment
        implements AdapterView.OnItemClickListener {

    public static ArrayList<Glasses> rows;
    private Context context;
    private ListView glasses;

    public static RecommendationsFragment getInstance() {
        return new RecommendationsFragment();
    }

    public RecommendationsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recommendations, container, false);

        context = getContext();

        attachViewIDs(view);

//        onGlassesSelected();
//        onClickStore();
//        onClickAccount();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        glasses.setAdapter(new GlassesAdapter(context, rows));
    }

    public void attachViewIDs(View view) {
        glasses = (ListView) view.findViewById(R.id.recommendList);
        glasses.setAdapter(new GlassesAdapter(context, rows));

        glasses.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent("com.example.ahmed.garrab.activities.GlassesDetails");
        intent.putExtra("glass", rows.get(position));
        startActivity(intent);
    }

//    public void onGlassesSelected(View view) {
//        gridView = (ListView) view.findViewById(R.id.recommendeList);
//        rows = new ArrayList<>();
//        for (int i = 0; i < price.size(); i++) {
//            rows.add(new Glasses(UserController.baseURL + paths.get(i), names.get(i), price.get(i), brands.get(i)));
//        }
//        gridView.setAdapter(new GlassesAdapter(this, rows));
//        gridView.setOnItemClickListener(this);
//    }


}
