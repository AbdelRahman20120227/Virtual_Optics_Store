package com.example.ahmed.garrab.controllers;

import android.os.AsyncTask;

import com.example.ahmed.garrab.fragments.StoreFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by ESC on 6/24/2016.
 */
public class BrandController {

    private static ArrayList<String> brandsList = new ArrayList<>();

    public BrandController() {

    }

    public static ArrayList<String> getBrands() {
        new OpenConnection().execute(UserController.baseURL + "store/admin/getBrands");
        return brandsList;
    }

    static private class OpenConnection extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            URL url;
            String res = "";
            HttpURLConnection connection;
            try {
                url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                byte[] stream = new byte[99];
                InputStream input = connection.getInputStream();
                int count = -1;
                while ((count = input.read(stream)) != -1) {
                    int i = 0;
                    while (i < count) res += (char) stream[i++];
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {
                brandsList = new ArrayList<>();

                JSONObject json = new JSONObject(result);
                JSONArray brands = json.getJSONArray("names");

                for (int i = 0; i < brands.length(); i++) {
                    brandsList.add(brands.get(i).toString());
                }

                StoreFragment.brandsList = brandsList;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
