package com.woot.company.woot;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class OfflineActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    EditText from, to, phone;
    String message;
    int bookcount = 0;
    String origin, destination, contact, mylocation = "no_location_provided";
    Double lat, lon;
    private static final int REQUEST_SMS = 0;
    private View mLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline);

        from = (EditText)findViewById(R.id.editText2);
        to = (EditText)findViewById(R.id.editText);
        phone = (EditText)findViewById(R.id.editText3);


            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            final Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
            lat = location.getLatitude();
            lon = location.getLongitude();
            if (lat!=null && lon!=null)
                mylocation = lat.toString() + "," + lon.toString();



        Button btnSendSMS = (Button)findViewById(R.id.button);
        btnSendSMS.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(OfflineActivity.this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                    requestSMSPermission();
                } else {
                    another(v);
                }
            }
        });
    }

    public void another(View view)
    {

        origin = String.valueOf(from.getText());
        destination = String.valueOf(to.getText());
        contact = String.valueOf(phone.getText());
        if (bookcount == 0) {
            if (!(origin.isEmpty() || destination.isEmpty() || contact.isEmpty())) {
                android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(OfflineActivity.this);
                alertDialog.setTitle("Offline Booking...");
                alertDialog.setMessage("Click OK to book a cab?");
                alertDialog.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                message = "A cab booked!\n From:" + origin + "\nTo: " + destination + "\nPhone: " + contact + " " + mylocation + " ";
                                sendSMS("9448987298", message);
                                Toast.makeText(OfflineActivity.this, "Sending SMS...", Toast.LENGTH_SHORT).show();
                                bookcount++;
                            }
                        });
                alertDialog.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                alertDialog.show();

            }
            else if (origin.isEmpty())
                Toast.makeText(OfflineActivity.this, "Enter origin", Toast.LENGTH_SHORT).show();
            else if (destination.isEmpty())
                Toast.makeText(OfflineActivity.this, "Enter destination", Toast.LENGTH_SHORT).show();
            else if (contact.isEmpty())
                Toast.makeText(OfflineActivity.this, "Enter contact", Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(OfflineActivity.this, "You have already booked a cab", Toast.LENGTH_SHORT).show();
    }

    private void sendSMS(String phoneNumber, String message) {
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, null, null);
    }

    private void requestSMSPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS)) {
            Snackbar.make(mLayout, "Permission is needed to send SMS for offline booking", Snackbar.LENGTH_INDEFINITE)
                    .setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ActivityCompat.requestPermissions(OfflineActivity.this,
                                    new String[]{Manifest.permission.SEND_SMS}, REQUEST_SMS);
                        }
                    }).show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, REQUEST_SMS);
        }
    }


}
