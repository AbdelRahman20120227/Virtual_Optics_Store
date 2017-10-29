package com.example.ahmed.garrab.controllers;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.ahmed.garrab.model.Customer;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by ESC on 6/18/2016.
 */
public class UserController {
    //private static UserController userController;
    public final static String baseURL = "http://virtual-merged.rhcloud.com/VirtualOpticsStore/";
    public Context context;

    public UserController(Context context) {
        this.context = context;
    }

    /*public static UserController getInstance(){
        if(userController == null)
             userController = new UserController();
        return userController;
    }*/

    // Call searchCustomer service
    public void login(String email, String password, String serviceType) {
        RecommendationController recommend = new RecommendationController();
        recommend.getRecommendations(email, context);
        new OpenConnection(context).execute(baseURL + "store/customer/login", email, password, serviceType);
    }

    public void update(String fname, String lname, String email, String password, String address, String phone, String gender, String serviceType) {
        new OpenConnection(context).execute(baseURL + "store/customer/update", fname, lname, email, password, address, phone, gender, serviceType);
    }

    // call addCustomer service
    public void register(String fname, String lname, String email, String password, String address, String phone, String gender, String serviceType) {
        new OpenConnection(context).execute(baseURL + "store/customer/signup", fname, lname, email, password, address, phone, gender, serviceType);
    }

    // Inner class to call service(handle long operations in background in new thread)
    static private class OpenConnection extends AsyncTask<String, String, String> {
        String service;
        String email, pass;
        private ProgressDialog dialog;
        private Context context;

        public OpenConnection(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... params) {
            URL url;
            String res = "";
            String Parameters;
            service = params[params.length - 1];
            email = params[1];
            pass = params[2];
            if (service.equals("login"))
                Parameters = "email=" + params[1] + "&password=" + params[2];
            else
                Parameters = "fname=" + params[1] + "&lname=" + params[2] + "&email=" + params[3] + "&password=" + params[4] + "&address=" + params[5]
                        + "&phone=" + params[6] + "&gender=" + params[7];
            HttpURLConnection connection;
            try {
                url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setInstanceFollowRedirects(false);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
                OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
                writer.write(Parameters);
                writer.flush();
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
        protected void onPreExecute() {
            dialog = new ProgressDialog(context);
            this.dialog.setMessage("Please Wait :))");
            this.dialog.show();
        }

        @Override
        protected void onPostExecute(String result) {
            Log.v("UserController response", result);
            super.onPostExecute(result);

            if (dialog.isShowing())
                dialog.dismiss();

            if (service.equals("register") && result.equals("OK")) {
                Intent intent = new Intent("com.example.ahmed.garrab.activities.Login");
                context.startActivity(intent);
            } else if (service.equals("update") && result.equals("success")) {
                Toast.makeText(context, "Your data updated SUCCESSFULLY ;)", Toast.LENGTH_SHORT).show();
            } else if (service.equals("login")) {
                if (result.equals(Globals.USER_NOT_EXIST)) {
                    Toast.makeText(context, "Wrong Email or Password", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject json = new JSONObject(result);
                        Customer customer = new Customer(
                                json.getInt("ID"),
                                json.getString("fname"),
                                json.getString("lname"),
                                json.getString("password"),
                                json.getString("phone"),
                                json.getString("address"),
                                json.getString("gender"),
                                json.getString("email"));
                        MyApplication.setCustomer(customer);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    MyApplication.setEmail(email);
                    MyApplication.setPassword(pass);
                    Intent intent = new Intent("com.example.ahmed.garrab.activities.HomePage");
                    context.startActivity(intent);
                }
            } else
                Toast.makeText(context, "Error occured!!", Toast.LENGTH_SHORT).show();
        }
    }
}
