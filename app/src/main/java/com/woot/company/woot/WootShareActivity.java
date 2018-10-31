package com.woot.company.woot;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
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
import java.util.ArrayList;
import java.util.List;

public class WootShareActivity extends AppCompatActivity {

    Spinner from, to, timeslot, dateslot;
    String[] city;
    String fromlocation = null, tolocation = null, timechosen = null, datechosen = null, price;
    final List<String>list1 = new ArrayList<String>();
    String share_from = "http://techpraja.com/Share_from.php", share_to = "http://techpraja.com/Share_to.php",
            share_date = "http://techpraja.com/Share_date.php", share_time = "http://techpraja.com/Share_time.php",
            share_price = "http://techpraja.com/Share_Price.php", share_book = "http://techpraja.com/Share_Booking.php";
    TextView textView;
    int bookcountshared = 0;
    URL url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_woot_share);

        from = (Spinner) findViewById(R.id.from);
        to = (Spinner) findViewById(R.id.to);
        dateslot = (Spinner) findViewById(R.id.dateslot);
        timeslot = (Spinner) findViewById(R.id.timeslot);
        textView = (TextView)findViewById(R.id.price);

        android.app.ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        BackgroundTask3 backgroundTask3 = new BackgroundTask3(WootShareActivity.this);
        backgroundTask3.execute();

        from.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                fromlocation = from.getItemAtPosition(position).toString();
                BackgroundTask4 backgroundTask4 = new BackgroundTask4(WootShareActivity.this);
                backgroundTask4.execute();

                to.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                        tolocation = to.getItemAtPosition(position).toString();
                        BackgroundTask5 backgroundTask5 = new BackgroundTask5(WootShareActivity.this);
                        backgroundTask5.execute();

                        dateslot.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                                datechosen = dateslot.getItemAtPosition(position).toString();
                                BackgroundTask6 backgroundTask6 = new BackgroundTask6(WootShareActivity.this);
                                backgroundTask6.execute();

                                timeslot.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                                        timechosen = timeslot.getItemAtPosition(position).toString();
                                        BackgroundTask7 backgroundTask7 = new BackgroundTask7(WootShareActivity.this);
                                        backgroundTask7.execute();
                                    }

                                    public void onNothingSelected(AdapterView<?> adapterView) {
                                    }
                                });
                            }
                            public void onNothingSelected(AdapterView<?> adapterView) {
                            }
                        });
                    }
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });
            }
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        final String name, phone_number, email;
        SharedPreferences prefs;
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        name = prefs.getString("name", null);
        name.toString();
        phone_number = prefs.getString("phone", null);
        phone_number.toString();
        email = prefs.getString("email", null);
        email.toString();

        Button button = (Button)findViewById(R.id.button4);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(bookcountshared == 0) {

                    BackgroundTask8 backgroundTask8 = new BackgroundTask8(WootShareActivity.this);
                    backgroundTask8.execute(name, phone_number, email);

                }
                else
                    Toast.makeText(WootShareActivity.this, "You have already booked a cab", Toast.LENGTH_SHORT).show();
            }
        });

    }


    public class BackgroundTask3 extends AsyncTask<String, Void, String> {
        Context ctx;
        BackgroundTask3(Context ctx) {
            this.ctx = ctx;
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... params) {
            String result = "";
            try {
                url = new URL(share_from);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
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
                JSONObject json;
                city = new String[jsonArray.length()];
                for (int i = 0; i < jsonArray.length(); i++) {
                    json = jsonArray.getJSONObject(i);
                    city[i] = json.getString("from1");
                }
                for (int i = 0; i < city.length; i++) {
                    list1.add(city[i]);
                }
            } catch (Exception e) {
                Log.e("error", "problem at from.php... " + e);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {

            ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(WootShareActivity.this, android.R.layout.simple_spinner_item, city);
            dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            from.setAdapter(dataAdapter1);
        }

    }

    public class BackgroundTask4 extends AsyncTask<String, Void, String> {
        Context ctx;
        BackgroundTask4(Context ctx) {
            this.ctx = ctx;
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... params) {
            String result = "";
            try {
                url = new URL(share_to);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream OS = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                String data = URLEncoder.encode("from1", "UTF-8") + "=" + URLEncoder.encode(fromlocation, "UTF-8");
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
                JSONObject json;
                city = new String[jsonArray.length()];
                for (int i = 0; i < jsonArray.length(); i++) {
                    json = jsonArray.getJSONObject(i);
                    city[i] = json.getString("to1");
                }
                for (int i = 0; i < city.length; i++) {
                    list1.add(city[i]);
                }
            } catch (Exception e) {
                Log.e("error", "problem at to.php... " + e);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(WootShareActivity.this, android.R.layout.simple_spinner_item, city);
            dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            to.setAdapter(dataAdapter1);
        }
    }

    public class BackgroundTask5 extends AsyncTask<String, Void, String> {
        Context ctx;
        BackgroundTask5(Context ctx) {
            this.ctx = ctx;
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... params) {
            String result = "";
            try {
                url = new URL(share_date);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream OS = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                String data = URLEncoder.encode("from1", "UTF-8") + "=" + URLEncoder.encode(fromlocation, "UTF-8") + "&" +
                        URLEncoder.encode("to1", "UTF-8") + "=" + URLEncoder.encode(tolocation, "UTF-8");
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
                JSONObject json;
                city = new String[jsonArray.length()];
                for (int i = 0; i < jsonArray.length(); i++) {
                    json = jsonArray.getJSONObject(i);
                    city[i] = json.getString("date1");
                }
                for (int i = 0; i < city.length; i++) {
                    list1.add(city[i]);
                }
            } catch (Exception e) {
                Log.e("error", "problem at date.php... " + e);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(WootShareActivity.this, android.R.layout.simple_spinner_item, city);
            dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dateslot.setAdapter(dataAdapter1);
        }
    }

    public class BackgroundTask6 extends AsyncTask<String, Void, String> {
        Context ctx;
        BackgroundTask6(Context ctx) {
            this.ctx = ctx;
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... params) {
            String result = "";
            try {
                url = new URL(share_time);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream OS = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                String data = URLEncoder.encode("from1", "UTF-8") + "=" + URLEncoder.encode(fromlocation, "UTF-8") + "&" +
                        URLEncoder.encode("to1", "UTF-8") + "=" + URLEncoder.encode(tolocation, "UTF-8") + "&" +
                        URLEncoder.encode("date1", "UTF-8") + "=" + URLEncoder.encode(datechosen, "UTF-8");
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
                JSONObject json;
                city = new String[jsonArray.length()];
                for (int i = 0; i < jsonArray.length(); i++) {
                    json = jsonArray.getJSONObject(i);
                    city[i] = json.getString("time1");
                }
                for (int i = 0; i < city.length; i++) {
                    list1.add(city[i]);
                }
            } catch (Exception e) {
                Log.e("error", "problem at time.php... " + e);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(WootShareActivity.this, android.R.layout.simple_spinner_item, city);
            dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            timeslot.setAdapter(dataAdapter1);
        }
    }

    public class BackgroundTask7 extends AsyncTask<String, Void, String> {
        Context ctx;
        BackgroundTask7(Context ctx) {
            this.ctx = ctx;
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... params) {
            String result = "";
            try {
                url = new URL(share_price);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream OS = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                String data = URLEncoder.encode("from1", "UTF-8") + "=" + URLEncoder.encode(fromlocation, "UTF-8") + "&" +
                        URLEncoder.encode("to1", "UTF-8") + "=" + URLEncoder.encode(tolocation, "UTF-8") + "&" +
                        URLEncoder.encode("date1", "UTF-8") + "=" + URLEncoder.encode(datechosen, "UTF-8") + "&" +
                        URLEncoder.encode("time1", "UTF-8") + "=" + URLEncoder.encode(timechosen, "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                StringBuilder stringBuilder = new StringBuilder();
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
                String response = "";
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                response = jsonObject.getString("price");
                return response;
            } catch (Exception e) {
                Log.e("error", "problem at price.php... " + e);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            price = result;
            textView.setText("₹" + price + "/-");
        }
    }

    public class BackgroundTask8 extends AsyncTask<String, Void, String> {
        Context ctx;
        AlertDialog alertDialog;
        String user, phone_number, email;
        BackgroundTask8(Context ctx) {
            this.ctx = ctx;
        }

        @Override
        protected void onPreExecute() {
            alertDialog = new AlertDialog.Builder(ctx).create();
            alertDialog.setTitle("Booking Information....");
        }

        @Override
        protected String doInBackground(String... params) {
            String result = "";
            user = params[0];
            phone_number = params[1];
            email = params[2];
            try {
                url = new URL(share_book);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream OS = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                String data = URLEncoder.encode("from1", "UTF-8") + "=" + URLEncoder.encode(fromlocation, "UTF-8") + "&" +
                        URLEncoder.encode("to1", "UTF-8") + "=" + URLEncoder.encode(tolocation, "UTF-8") + "&" +
                        URLEncoder.encode("date1", "UTF-8") + "=" + URLEncoder.encode(datechosen, "UTF-8") + "&" +
                        URLEncoder.encode("time1", "UTF-8") + "=" + URLEncoder.encode(timechosen, "UTF-8") + "&" +
                        URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(user, "UTF-8") + "&" +
                        URLEncoder.encode("phone_number", "UTF-8") + "=" + URLEncoder.encode(phone_number, "UTF-8") + "&" +
                        URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8") + "&" +
                        URLEncoder.encode("price", "UTF-8") + "=" + URLEncoder.encode(price, "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                StringBuilder stringBuilder = new StringBuilder();
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
                    alertDialog.setMessage("Cab is booked");
                    alertDialog.show();
                    bookcountshared++;
                } else {
                    Toast.makeText(ctx, "Error, please try again", Toast.LENGTH_LONG).show();
                }

            } catch (Exception e) {
                result = "Error!";
                alertDialog.setMessage(result);
                alertDialog.show();
                Log.e("Problem!", "at " + e);
            }
        }
    }
}