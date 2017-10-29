package com.example.ahmed.garrab.controllers;

import android.os.AsyncTask;

import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by ESC on 7/1/2016.
 */
public class TryController  {
    public TryController(){

    }
    public static void addTry(String email,String model){
        new OpenConnection().execute(UserController.baseURL +"store/attempts/addTry",email,model);
    }

    static private class OpenConnection extends AsyncTask<String,String,String> {
        @Override
        protected String doInBackground(String... params) {
            URL url;
            String res = "";
            String Parameters = "email=" + params[1] + "&modelName=" + params[2];
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
        }
    }
}
