package com.woot.company.woot;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

public class SplashScreen extends Activity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private final int SPLASH_DISPLAY_LENGTH = 500;
    Boolean isInternetPresent = false;
    ConnectionDetector cd;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
            //new LaunchScreen().execute("Exception");

            cd = new ConnectionDetector(getApplicationContext());
            isInternetPresent = cd.isConnectingToInternet();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                        if (isInternetPresent) {
                            String name = null;
                            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(SplashScreen.this);
                            name = prefs.getString("name", null);

                            if (name != null) {
                                Intent intent = new Intent(SplashScreen.this, WootCabsActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                intent = new Intent(SplashScreen.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        } else {
                            intent = new Intent(SplashScreen.this, OfflineActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }

            }, SPLASH_DISPLAY_LENGTH);

    }





    /*private class LaunchScreen extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            for (int i = 0; i < 5; i++)
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.interrupted();

                }
            return "Exception";
        }

        @Override
        protected void onPostExecute(String s) {
            Intent getNameScreenIntent = new Intent(getBaseContext(), LoginActivity.class);
            startActivity(getNameScreenIntent);
            SplashScreen.this.finishAffinity();
        }
    }*/
}