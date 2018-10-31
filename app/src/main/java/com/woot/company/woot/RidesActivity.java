package com.woot.company.woot;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
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

public class RidesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rides);
        final TextView textView = (TextView)findViewById(R.id.rides);
        SharedPreferences prefs;
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        final String email = prefs.getString("email", null);
        email.toString();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String result = "";
        try {
            URL url = new URL("http://techpraja.com/Rides_booked.php");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream OS = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
            String data = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8");
            bufferedWriter.write(data);
            bufferedWriter.flush();
            bufferedWriter.close();
            OS.close();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            StringBuilder stringBuilder = new StringBuilder();
            String line = "";
            String response = "";
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            inputStream.close();
            result = stringBuilder.toString();
        } catch (Exception e) {
            Log.e("problem!", "Error" + e);
        }
        try {
            String s = "";
            JSONArray jsonArray = new JSONArray(result);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                s = s + "From: " + jsonObject.getString("from1") + "\n" +
                        "To: " + jsonObject.getString("to1") + "\n" +
                        "Fare: " + jsonObject.getString("fare") + "\n" +
                        "Distance: " + jsonObject.getString("distance") + "\n*************************\n";
            }
            textView.setText(s);
        } catch (Exception e) {
            Log.e("Error", "error in rides "+e);
        }
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
