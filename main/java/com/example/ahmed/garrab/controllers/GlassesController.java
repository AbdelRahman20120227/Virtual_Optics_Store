package com.example.ahmed.garrab.controllers;

import android.content.Context;
import android.os.AsyncTask;

import com.example.ahmed.garrab.model.Glasses;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static com.example.ahmed.garrab.fragments.StoreFragment.rows;

/**
 * Created by ESC on 6/20/2016.
 */
public class GlassesController {
    private static Context context;

    public GlassesController(Context context) {
        this.context = context;
    }

    public static void getGlasses() {
        new OpenConnection().execute(UserController.baseURL + "store/glasses/getGlasses", "getGlasses");
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
                byte[] buffer = new byte[99];
                InputStream input = connection.getInputStream();
                int count = -1;
                while ((count = input.read(buffer)) != -1) {
                    int i = 0;
                    while (i < count) res += (char) buffer[i++];
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
                JSONObject json = new JSONObject(result);

                JSONArray ids = json.getJSONArray("ids");
                JSONArray brands = json.getJSONArray("brands");
                JSONArray modelNames = json.getJSONArray("modelNames");
                JSONArray paths = json.getJSONArray("paths");
                JSONArray prices = json.getJSONArray("prices");

                ArrayList<Glasses> glasses = new ArrayList<>();

                for (int i = 0; i < brands.length(); i++) {
                    Glasses storeRow = new Glasses(
                            Integer.parseInt(ids.get(i).toString()),
                            UserController.baseURL + paths.get(i),
                            modelNames.get(i).toString(),
                            Double.parseDouble(prices.get(i).toString()),
                            brands.get(i).toString());

                    glasses.add(storeRow);
                }

                rows = glasses;

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
