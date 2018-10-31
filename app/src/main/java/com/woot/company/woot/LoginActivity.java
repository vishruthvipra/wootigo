package com.woot.company.woot;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends Activity {
    EditText ET_NAME,ET_PASS;
    String login_name,login_pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ET_NAME = (EditText) findViewById(R.id.email);
        ET_PASS = (EditText) findViewById(R.id.password);

        Button register = (Button) findViewById(R.id.register_button);
        Button login = (Button) findViewById(R.id.email_sign_in_button);

        register.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                userReg(v);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                userLogin(v);

            }
        });
    }
    public void userReg(View view) {
        startActivity(new Intent(this, OTP.class));
    }

    public void userLogin(View view) {
        login_name = ET_NAME.getText().toString();
        login_pass = ET_PASS.getText().toString();
        String method = "login";
        BackgroundTask backgroundTask = new BackgroundTask(this);
        backgroundTask.execute(method, login_name, login_pass);

    }

}