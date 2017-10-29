package com.example.ahmed.garrab.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.ahmed.R;
import com.example.ahmed.garrab.adapters.GlassesAdapter;
import com.example.ahmed.garrab.db.GlassesDBHelper;
import com.example.ahmed.garrab.model.Glasses;

import java.util.ArrayList;

public class FavoritesFragment extends Fragment {

    private ArrayList<Glasses> rows = new ArrayList<>();
    private Context context;
    private ListView glasses;
    private GlassesDBHelper glassesDBHelper;

    public FavoritesFragment() {
        // Required empty public constructor
    }

    public static Fragment getInstance() {
        return new FavoritesFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);

        context = getContext();
        glassesDBHelper = new GlassesDBHelper(context);
        attachViewIDs(view);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        glasses.setAdapter(new GlassesAdapter(context, rows));
        rows = glassesDBHelper.getFavoriteGlasses();
    }

    public void attachViewIDs(View view) {
        glasses = (ListView) view.findViewById(R.id.list_favorites);
        glasses.setAdapter(new GlassesAdapter(context, rows));

//        glasses.setOnItemClickListener(this);
//        registerForContextMenu(glasses);
    }


}
