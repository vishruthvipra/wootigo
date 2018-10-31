package com.woot.company.woot.detail;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.lang.ref.SoftReference;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.woot.company.woot.R;
import com.woot.company.woot.universal.Utils;

public class BookRoom extends Activity implements View.OnClickListener{
    TextView roomName, price, cancel;
    EditText totalPeople, checkIn, checkOut, fName, lName, email, phone;
    Button btnCalculate, btnBookNow;
    String RoomTitle, Price;
    Spinner spinner;
    Calendar sDate;
    Calendar eDate;
    Context context;
    String[] roomType = { "Single bed", "Double bed", };
    String type;
    int a;

    private DatePickerDialog inDatePickerDialog;
    private DatePickerDialog outDatePickerDialog;
    private SimpleDateFormat dateFormatter;
    String user_id,userName,result_email,HotelID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.room_book);
        context = getApplicationContext();
        InitializeView();/*
        HotelID = Utils.getPreferences("h_ID", context);
        user_id = Utils.getPreferences("user_id", context);
        userName = Utils.getPreferences("user_name1", context);
        result_email = Utils.getPreferences("result_email", context);*/
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        userName = prefs.getString("name", null);
        result_email = prefs.getString("email", null);
        String phoned = prefs.getString("phone", null);
        ExtrasCollection();
        setDateTimeField();
        fName.setText(userName);
        email.setText(result_email);
        phone.setText(phoned);
        cancel.setOnClickListener(this);
        btnBookNow.setOnClickListener(this);
        btnCalculate.setOnClickListener(this);


        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item, roomType);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinner.setAdapter(aa);

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

//        roomName.setText(RoomTitle);
//        price.setText(Price);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                a = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void CalculatePrice(){
        String totalP = totalPeople.getText().toString();
        String startDate = checkIn.getText().toString();
        String endDate = checkOut.getText().toString();


        if (totalP.isEmpty() && startDate.isEmpty() && endDate.isEmpty()){
            price.setText(Price);
        }else {

            Integer pric = Integer.valueOf(Price);
            if (a == 0){
                pric = pric*1;
            }else {
                pric = pric*2;
            }

            final int d = Days.daysBetween(new DateTime(sDate), new DateTime(eDate)).getDays();
            int p = Integer.valueOf(totalP);
            int t = d*p*pric;
            String pr = String.valueOf(t);
            price.setText(pr);
        }
    }
    private void BookNow(){
        String sDate = checkIn.getText().toString();
        String eDate = checkOut.getText().toString();
        String people = totalPeople.getText().toString();
        String price1 = price.getText().toString();
        String nameF = fName.getText().toString();
//        String nameL = lName.getText().toString();
        String email1 = email.getText().toString();
        String Ph = phone.getText().toString();
        String emailHotel = Utils.getPreferences("email", context);

        if (!sDate.isEmpty() && !eDate.isEmpty() && !people.isEmpty() && !price1.isEmpty()
                && !nameF.isEmpty() && !email1.isEmpty() && !Ph.isEmpty()){
            Uri uri = Uri.parse("mailto:"+emailHotel);
            Intent it = new Intent(Intent.ACTION_SENDTO, uri);
            it.putExtra(Intent.EXTRA_SUBJECT, "Booking request for: "+RoomTitle);
            it.putExtra(Intent.EXTRA_TEXT, "Hello, \nI would like to book a "+RoomTitle+
                    "\nFrom: "+sDate+
                    "\nTo: "+ eDate +
                    "\nFor "+people+" people"+
                    "\n\nFirst Name: "+nameF+
//                    "\nLast Name: "+nameL+
                    "\nEmail: "+email1+
                    "\nPhone: "+Ph+
                    "\n\nPlease let us know payment methods and availability of this room" +
                    "\nThank you, \nRegards.");
            startActivity(it);
            BookRoom.this.finish();

        }else {
            Toast.makeText(context, "Please fill it completely", Toast.LENGTH_SHORT).show();
        }
    }
    private void setDateTimeField() {
        checkIn.setOnClickListener(this);
        checkOut.setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();
        inDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                sDate = Calendar.getInstance();
                sDate.set(year, monthOfYear, dayOfMonth);
                checkIn.setText(dateFormatter.format(sDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        outDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                eDate = Calendar.getInstance();
                eDate.set(year, monthOfYear, dayOfMonth);
                checkOut.setText(dateFormatter.format(eDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }
    private void ExtrasCollection() {
        Bundle extras = getIntent().getExtras();
        RoomTitle = extras.getString("rName");
        Price = extras.getString("rPrice");
    }
    public void InitializeView(){
//        roomName = (TextView)findViewById(R.id.idTVNameRoom);
        totalPeople = (EditText)findViewById(R.id.idTvPeopleCount);
        checkIn = (EditText)findViewById(R.id.idEditTextCheckIn);
        checkOut = (EditText)findViewById(R.id.idEditTextCheckOut);
        fName = (EditText)findViewById(R.id.idEdittextFName);
//        lName = (EditText)findViewById(R.id.idEditTextLName);
        email = (EditText)findViewById(R.id.idEditTextEmail);
        phone = (EditText)findViewById(R.id.idEditTextPhone);
        btnCalculate = (Button)findViewById(R.id.idBtnCalculatePrice);
        btnBookNow = (Button)findViewById(R.id.idBtnBookNow);
        price = (TextView)findViewById(R.id.idTextPriceTotal);
        cancel = (TextView)findViewById(R.id.idCancel);
        spinner = (Spinner)findViewById(R.id.idSpinnerRoomType);
    }

    @Override
    public void onClick(View view) {
        if(view == checkIn) {
            inDatePickerDialog.show();
        } else if(view == checkOut) {
            outDatePickerDialog.show();
        }else if (view == cancel){
            BookRoom.this.finish();
        }else if (view == btnBookNow){
            BookNow();
        }else if (view == btnCalculate){
            CalculatePrice();
        }
    }

}
