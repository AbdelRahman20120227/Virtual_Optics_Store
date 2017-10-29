package com.example.ahmed.garrab.controllers;

import com.example.ahmed.garrab.activities.Store;
import com.example.ahmed.garrab.fragments.StoreFragment;
import com.example.ahmed.garrab.model.Glasses;

import java.util.ArrayList;

/**
 * Created by ESC on 6/24/2016.
 */
public class Filter {

    public static ArrayList<Glasses> filterGlasses(String brand, Double low, Double high) {
        ArrayList<Glasses> copy = new ArrayList<>();
        for (int i = 0; i < StoreFragment.rows.size(); i++) {

            Boolean lowCond = true, highCond = true, brandCond = true;

            if (low != null && StoreFragment.rows.get(i).getPrice() < low)
                lowCond = false;
            else if (high != null && StoreFragment.rows.get(i).getPrice() > high)
                highCond = false;
            else if (brand != null && !brand.equals(StoreFragment.rows.get(i).getBrand()))
                brandCond = false;

            if (lowCond && highCond && brandCond)
                copy.add(StoreFragment.rows.get(i));
        }
        return copy;
    }

    public static ArrayList<Glasses> getGlassesByBrand(String brand) {
        ArrayList<Glasses> copy = new ArrayList<>();
        for (int i = 0; i < Store.rows.size(); i++) {
            if (brand.equals(Store.rows.get(i).getBrand()))
                copy.add(Store.rows.get(i));
        }
        return copy;
    }

    public static ArrayList<Glasses> getGlassesByPrice(double low, double high) {
        ArrayList<Glasses> copy = new ArrayList<>();
        for (int i = 0; i < Store.rows.size(); i++) {
            if (Store.rows.get(i).getPrice() >= low && Store.rows.get(i).getPrice() <= high)
                copy.add(Store.rows.get(i));
        }
        return copy;
    }

    public static ArrayList<Glasses> getGlassesByBoth(String brand, double low, double high) {
        ArrayList<Glasses> copy = new ArrayList<>();
        for (int i = 0; i < Store.rows.size(); i++) {
            if (brand.equals(Store.rows.get(i).getBrand()) &&
                    Store.rows.get(i).getPrice() >= low && Store.rows.get(i).getPrice() <= high)
                copy.add(Store.rows.get(i));
        }
        return copy;
    }
}
