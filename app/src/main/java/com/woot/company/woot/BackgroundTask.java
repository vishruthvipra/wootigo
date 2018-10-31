package com.woot.company.woot;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Vishruth K on 16-05-2016.
 */
public class BackgroundTask extends AsyncTask<String,Void,String> {
    AlertDialog alertDialog;
    Context ctx;
    String user, phone_number, email;
    BackgroundTask(Context ctx)
    {
        this.ctx =ctx;
    }
    @Override
    protected void onPreExecute() {
        alertDialog = new AlertDialog.Builder(ctx).create();
        alertDialog.setTitle("Login/Register Information....");
    }
    @Override
    protected String doInBackground(String... params) {
        String reg_url = "http://techpraja.com/Register_user.php";
        String login_url = "http://techpraja.com/User_login.php";
        String book_url = "http://techpraja.com/Booking_list.php";
        String method = params[0];
        if (method.equals("register")) {
            String result = "";
            String name = params[1];
            String user_name = params[2];
            String user_pass = params[3];
            String phone = params[4];
            String status = "OK";
            try {
                URL url = new URL(reg_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream OS = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                String data = URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8") + "&" +
                        URLEncoder.encode("phone_number", "UTF-8") + "=" + URLEncoder.encode(phone, "UTF-8") + "&" +
                        URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(user_pass, "UTF-8") + "&" +
                        URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(user_name, "UTF-8") + "&" +
                        URLEncoder.encode("status", "UTF-8") + "=" + URLEncoder.encode(status, "UTF-8");

                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                StringBuilder stringBuilder = new StringBuilder();
                String response = "";
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                inputStream.close();
                result = stringBuilder.toString();

            } catch (Exception e) {
                Log.e("problem!", "Error" + e);
            }
            try {
                JSONArray jsonArray = new JSONArray(result);
                user = "";
                phone_number = "";
                email = "";

                JSONObject jsonObject = jsonArray.getJSONObject(0);
                user = jsonObject.getString("name");
                phone_number = jsonObject.getString("phone_number");
                email = jsonObject.getString("email");
                return user;
            } catch (Exception e) {
                Log.e("error", "error parsing data"+e);
            }
        }
        else if(method.equals("login"))
        {
            String result = "";
            String login_name = params[1];
            String login_pass = params[2];
            try {
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(login_name, "UTF-8") + "&" +
                        URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(login_pass, "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                StringBuilder stringBuilder = new StringBuilder();
                String response = "";
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                inputStream.close();
                result = stringBuilder.toString();

            } catch (Exception e) {
                Log.e("problem!", "Error" + e);
            }
            try {
                JSONArray jsonArray = new JSONArray(result);
                user = "";
                phone_number = "";
                email = "";

                JSONObject jsonObject = jsonArray.getJSONObject(0);
                user = jsonObject.getString("name");
                phone_number = jsonObject.getString("phone_number");
                email = jsonObject.getString("email");
                return user;
            } catch (Exception e) {
                Log.e("error", "error parsing data"+e);
            }
        }
        else if (method.equals("book")) {
            String result = "";
            String from = params[1];
            String to = params[2];
            String vehicle1 = params[3];
            String route1 = params[4];
            String distance = params[5];
            String fare = params[6];
            user = params[7];
            phone_number = params[9];
            email = params[8];
            try {
                URL url = new URL(book_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream OS = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                String data = URLEncoder.encode("from1", "UTF-8") + "=" + URLEncoder.encode(from, "UTF-8") + "&" +
                        URLEncoder.encode("to1", "UTF-8") + "=" + URLEncoder.encode(to, "UTF-8") + "&" +
                        URLEncoder.encode("vehicle", "UTF-8") + "=" + URLEncoder.encode(vehicle1, "UTF-8") + "&" +
                        URLEncoder.encode("route", "UTF-8") + "=" + URLEncoder.encode(route1, "UTF-8") + "&" +
                        URLEncoder.encode("distance", "UTF-8") + "=" + URLEncoder.encode(distance, "UTF-8") + "&" +
                        URLEncoder.encode("fare", "UTF-8") + "=" + URLEncoder.encode(fare, "UTF-8") + "&" +
                        URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(user, "UTF-8") + "&" +
                        URLEncoder.encode("phone_number", "UTF-8") + "=" + URLEncoder.encode(phone_number, "UTF-8") + "&" +
                        URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8");

                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                StringBuilder stringBuilder = new StringBuilder();
                String response = "";
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                inputStream.close();
                result = stringBuilder.toString();
                } catch (Exception e) {
                Log.e("problem!", "Error" + e);
            }
            try {
                JSONArray jsonArray = new JSONArray(result);
                phone_number = "";
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                phone_number = jsonObject.getString("phone_number");
                return phone_number;
            } catch (Exception e) {
                Log.e("error", "booking error" + e);
            }
        }
        return null;
    }
    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
    @Override
    protected void onPostExecute(String result) {
        try {
            if (!result.isEmpty()) {
                if(result.contains("+")){
                    alertDialog.setMessage("Cab is booked");
                    alertDialog.show();
                    Intent intent = new Intent(ctx, WootCabsActivity.class);
                    ctx.startActivity(intent);
                }
                else {
                    String name = result;
                    String phone = phone_number;
                    String emailid = email;
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
                    prefs.edit().putString("name", name).commit();
                    prefs.edit().putString("phone", phone).commit();
                    prefs.edit().putString("email", emailid).commit();
                    Intent intent = new Intent(ctx, WootCabsActivity.class);
                    ctx.startActivity(intent);
                }
            }
            else
            {
                Toast.makeText(ctx, "Error, please try again", Toast.LENGTH_LONG).show();
            }
        }
        catch (Exception e)
        {
            result = "Error!";
            alertDialog.setMessage(result);
            alertDialog.show();
            Log.e("Problem!", "at " +e);
        }
    }
}