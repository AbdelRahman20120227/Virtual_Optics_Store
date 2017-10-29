package com.example.ahmed.garrab.controllers;

import android.content.Context;
import android.os.AsyncTask;

import com.example.ahmed.garrab.model.Glasses;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static com.example.ahmed.garrab.fragments.RecommendationsFragment.rows;

/**
 * Created by ESC on 6/25/2016.
 */
public class RecommendationController {

    public static Context context;

    public RecommendationController() {

    }

    public void getRecommendations(String email, Context context) {
        new OpenConnection().execute(UserController.baseURL + "store/attempts/getRecommendations", email, "getRecommendations");
        this.context = context;
    }

    static private class OpenConnection extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            URL url;
            String res = "";
            String Parameters = "email=" + params[1];
            HttpURLConnection connection;
            try {
                url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
                writer.write(Parameters);
                writer.flush();
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

                ArrayList<Glasses> userRecommendations = new ArrayList<>();

                for (int i = 0; i < brands.length(); i++) {
                    Glasses glasses = new Glasses(
                            Integer.parseInt(ids.get(i).toString()),
                            UserController.baseURL + paths.get(i),
                            modelNames.get(i).toString(),
                            Double.parseDouble(prices.get(i).toString()),
                            brands.get(i).toString());

                    userRecommendations.add(glasses);
                }

                rows = userRecommendations;

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
