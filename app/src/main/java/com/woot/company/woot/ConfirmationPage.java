package com.woot.company.woot;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

public class ConfirmationPage extends FragmentActivity {

    TextView textView, textView2, textView3, textView4, textView5, textView6, textView7, textView8;
    Button button, button2, button3;
    String date, time;
    int bookcount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation_page);

        Bundle vehicle = getIntent().getExtras();
        Bundle route = getIntent().getExtras();
        Bundle globaldistance = getIntent().getExtras();
        Bundle destination = getIntent().getExtras();
        Bundle origin = getIntent().getExtras();

        if (vehicle == null || route == null || globaldistance == null)
            return;

        final String vehicle1 = vehicle.getString("Vehicle");
        final String route1 = route.getString("Route");
        final String distance = globaldistance.getString("Distance");
        final String from = origin.getString("Origins");
        final String to = destination.getString("Destination");

        String fare = distance.replaceAll("[^\\d.]", "");
        float calc = 0;
        try {
            if (route1.equals("OneWay")) {
                if ((from.contains("Mysuru") || from.contains("Mysore")) && (to.contains("Bengaluru") || to.contains("Bangalore"))) {
                    if (vehicle1.equals("Hatchback"))
                        calc = Float.valueOf(fare) * 10;
                    else if (vehicle1.equals("Sedan"))
                        calc = Float.valueOf(fare) * 12;
                    else if (vehicle1.equals("SUV"))
                        calc = Float.valueOf(fare)  * 13;
                    else if (vehicle1.equals("TempoTraveller"))
                        calc = Float.valueOf(fare)  * 15;
                } else if ((from.contains("Bengaluru") || from.contains("Bangalore")) && (to.contains("Mysore") || to.contains("Mysuru"))) {
                    if (vehicle1.equals("Hatchback"))
                        calc = Float.valueOf(fare)  * 10;
                    else if (vehicle1.equals("Sedan"))
                        calc = Float.valueOf(fare)  * 12;
                    else if (vehicle1.equals("SUV"))
                        calc = Float.valueOf(fare)  * 13;
                    else if (vehicle1.equals("TempoTraveller"))
                        calc = Float.valueOf(fare)  * 15;
                } else {
                    if (vehicle1.equals("Hatchback"))
                        calc = Float.valueOf(fare)  * 10 * 2;
                    else if (vehicle1.equals("Sedan"))
                        calc = Float.valueOf(fare)  * 12 * 2;
                    else if (vehicle1.equals("SUV"))
                        calc = Float.valueOf(fare)  * 13 * 2;
                    else if (vehicle1.equals("TempoTraveller"))
                        calc = Float.valueOf(fare)  * 15 * 2;
                }

            } else {
                if (vehicle1.equals("Hatchback"))
                    calc = (Float.valueOf(fare)  * 10 * 2) - 100;
                else if (vehicle1.equals("Sedan"))
                    calc = (Float.valueOf(fare)  * 12 * 2) - 100;
                else if (vehicle1.equals("SUV"))
                    calc = (Float.valueOf(fare)  * 13 * 2) - 100;
                else if (vehicle1.equals("TempoTraveller"))
                    calc = (Float.valueOf(fare)  * 15 * 2) - 100;
            }
            String rounded = String.format("%.02f", calc);
            fare = String.valueOf(rounded);
        }
        catch (Exception e)
        {
            Log.e("Problem", "Error is " + e);
        }
        button = (Button) findViewById(R.id.button3);
        button2 = (Button) findViewById(R.id.ridenow);
        button3 = (Button) findViewById(R.id.ridelater);
        textView = (TextView) findViewById(R.id.textView8);
        textView2 = (TextView) findViewById(R.id.textView10);
        textView3 = (TextView) findViewById(R.id.textView12);
        textView4 = (TextView) findViewById(R.id.textView14);
        textView5 = (TextView) findViewById(R.id.textView16);
        textView6 = (TextView) findViewById(R.id.textView18);
        textView7 = (TextView) findViewById(R.id.textView20);
        textView8 = (TextView) findViewById(R.id.textView22);


        textView.setText(from);
        textView2.setText(to);
        textView3.setText(distance);
        textView4.setText(vehicle1);
        textView5.setText(route1);
        textView6.setText("₹" + fare + "/-");

        final String name, phone_number, email;
        SharedPreferences prefs;
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        name = prefs.getString("name", null);
        name.toString();
        phone_number = prefs.getString("phone", null);
        phone_number.toString();
        email = prefs.getString("email", null);
        email.toString();

        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                textView7.setText("Earliest available");
                textView8.setText("Earliest available");
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DialogFragment timePickerFragment = new TimePickerFragment();
                timePickerFragment.show(getFragmentManager(), "timePicker");
                DialogFragment datePickerFragment = new DatePickerFragment();
                datePickerFragment.show(getFragmentManager(), "datePicker");

            }
        });

        final String finalFare = fare;
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(bookcount == 0) {
                    String sdate = (String) textView7.getText();
                    String stime = (String) textView8.getText();
                    if (!(sdate.equals("Select date") || stime.equals("Select time"))) {
                        time = textView7.getText().toString();
                        date = textView8.getText().toString();
                        BackgroundTask2 backgroundTask2 = new BackgroundTask2(ConfirmationPage.this);
                        backgroundTask2.execute(from, to, vehicle1, route1, distance, finalFare, name, email, phone_number, time, date);

                    } else {
                        Toast.makeText(ConfirmationPage.this, "Select date and time", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                    Toast.makeText(ConfirmationPage.this, "You have already booked a cab", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public class BackgroundTask2 extends AsyncTask<String, Void, String> {
        AlertDialog alertDialog;
        Context ctx;
        String user, phone_number, email;

        BackgroundTask2(Context ctx) {
            this.ctx = ctx;
        }

        @Override
        protected void onPreExecute() {
            alertDialog = new AlertDialog.Builder(ctx).create();
            alertDialog.setTitle("Booking Information....");
        }

        @Override
        protected String doInBackground(String... params) {
            String book_url = "http://techpraja.com/Booking_list.php";
            String result = "";
            String from = params[0];
            String to = params[1];
            String vehicle1 = params[2];
            String route1 = params[3];
            String distance = params[4];
            String fare = params[5];
            user = params[6];
            phone_number = params[8];
            email = params[7];
            String time = params[9];
            String date = params[10];
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
                        URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8") + "&" +
                        URLEncoder.encode("date", "UTF-8") + "=" + URLEncoder.encode(date, "UTF-8") + "&" +
                        URLEncoder.encode("time", "UTF-8") + "=" + URLEncoder.encode(time, "UTF-8");

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
                    bookcount++;

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