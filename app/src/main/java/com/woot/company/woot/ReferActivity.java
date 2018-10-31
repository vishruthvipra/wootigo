package com.woot.company.woot;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class ReferActivity extends AppCompatActivity {
    EditText couponcode;
    String coupon, wb, method, email;
    Button apply;
    TextView wallet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refer);

        SharedPreferences prefs;

        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        wallet = (TextView)findViewById(R.id.textView33);
        couponcode = (EditText)findViewById(R.id.textView34);
        apply = (Button)findViewById(R.id.button5);
        email = prefs.getString("email", null);
        email.toString();

        method = "oncreate";
        BackgroundTask9 backgroundTask9 = new BackgroundTask9(this);
        backgroundTask9.execute(method, email, "anything");
       // finish();

        apply.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                coupon = String.valueOf(couponcode.getText());
                method = "couponadded";
                BackgroundTask9 backgroundTask9 = new BackgroundTask9(ReferActivity.this);
                backgroundTask9.execute(method, email, coupon);
            }
        });
    }

    public class BackgroundTask9 extends AsyncTask<String, Void, String> {
        Context ctx;
        String wallet_balance, email, method, coups;

        BackgroundTask9(Context ctx) {
            this.ctx = ctx;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... params) {
            String book_url = "http://techpraja.com/Wallet_Balance.php";
            String coupon_url = "http://techpraja.com/Apply_Coupon.php";
            String result = "";
            method = params[0];
            email = params[1];
            coups = params[2];
            try {
                if (method.equals("oncreate")) {
                    URL url = new URL(book_url);
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
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                    StringBuilder stringBuilder = new StringBuilder();
                    String response = "";
                    String line = "";
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                    inputStream.close();
                    result = stringBuilder.toString();
                    JSONArray jsonArray = new JSONArray(result);
                    wallet_balance = "";
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    wallet_balance = jsonObject.getString("Wallet_Balance");
                    return wallet_balance;
                }
                if (method.equals("couponadded")) {
                    URL url = new URL(coupon_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream OS = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                    String data = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8") + "&" +
                            URLEncoder.encode("couponcode", "UTF-8") + "=" + URLEncoder.encode(coups, "UTF-8");
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
                    JSONArray jsonArray = new JSONArray(result);
                    wallet_balance = "";
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    wallet_balance = jsonObject.getString("Wallet_Balance");
                    return wallet_balance;
                }
            } catch (Exception e) {
                Log.e("error", "booking error" + e);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            if (!result.isEmpty()) {
                wb = result;
                wallet.setText("₹" + wb + "/-");
            } else {
                Toast.makeText(ctx, "Error, please try again", Toast.LENGTH_LONG).show();
            }
        }
    }
}
