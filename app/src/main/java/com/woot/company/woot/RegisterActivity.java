package com.woot.company.woot;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class RegisterActivity extends Activity {
    EditText ET_NAME,ET_USER_NAME,ET_USER_PASS,ET_CON_PASS,ET_PHONE;
    String name,user_name,user_pass,confirm_pass,phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ET_NAME = (EditText)findViewById(R.id.editText7);
        ET_USER_NAME= (EditText)findViewById(R.id.editText6);
        ET_USER_PASS = (EditText)findViewById(R.id.editText4);
        ET_CON_PASS = (EditText)findViewById(R.id.editText5);

        Button proceed = (Button)findViewById(R.id.proceed);
        proceed.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                userReg(v);
            }
        });

    }
    public void userReg(View view) {
        name = ET_NAME.getText().toString();
        user_name = ET_USER_NAME.getText().toString();
        user_pass = ET_USER_PASS.getText().toString();
        SharedPreferences prefs;
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        phone = prefs.getString("phone", null);
        confirm_pass = ET_CON_PASS.getText().toString();
        if (phone != null) {
            if (user_pass.equals(confirm_pass)) {
                String method = "register";
                BackgroundTask backgroundTask = new BackgroundTask(this);
                backgroundTask.execute(method, name, user_name, user_pass, phone);
                //finish();
            } else
                Toast.makeText(this, "password not matching", Toast.LENGTH_SHORT).show();
        }
    }

}
