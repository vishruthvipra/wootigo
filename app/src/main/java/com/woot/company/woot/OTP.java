package com.woot.company.woot;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.digits.sdk.android.AuthCallback;
import com.digits.sdk.android.Digits;
import com.digits.sdk.android.DigitsAuthButton;
import com.digits.sdk.android.DigitsException;
import com.digits.sdk.android.DigitsSession;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import io.fabric.sdk.android.Fabric;

public class OTP extends AppCompatActivity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "EmFtbFa4Ju2wiltEc1IW4TRBr";
    private static final String TWITTER_SECRET = "gm0enTIgT9EeNs7OjQSCwxHbzC7HBYWpLDgA4j2LaWWyRQuaTK";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new TwitterCore(authConfig), new Digits());
        DigitsAuthButton digitsButton = (DigitsAuthButton) findViewById(R.id.auth_button);
        digitsButton.setCallback(new AuthCallback() {
            @Override
            public void success(DigitsSession session, String phoneNumber) {
                // TODO: associate the session userID with your user model
                Toast.makeText(getApplicationContext(), "Authentication successful for "
                        + phoneNumber, Toast.LENGTH_LONG).show();
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(OTP.this);
                prefs.edit().putString("phone", phoneNumber).commit();
                Intent intent = new Intent(OTP.this, RegisterActivity.class);
                startActivity(intent);
            }

            @Override
            public void failure(DigitsException exception) {
                Log.d("Digits", "Sign in with Digits failure", exception);
            }
        });
        Digits.getSessionManager().clearActiveSession();

    }
}
/*
FragmentManager fragmentManager =  getSupportFragmentManager();
        if (id == R.id.nav_fragment_promo) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new PromoFragment()).commit();
        } else if (id == R.id.nav_fragment_refer) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new ReferFragment()).commit();
        } else if (id == R.id.nav_fragment_settings) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new SettingsFragment()).commit();
        }




                // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_refer, container, false);
        button = (Button) view.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //read promo here
            }
        });
        return view;

    }

 */