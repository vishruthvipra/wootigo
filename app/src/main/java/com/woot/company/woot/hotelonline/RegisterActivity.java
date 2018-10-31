package com.woot.company.woot.hotelonline;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.woot.company.woot.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import com.woot.company.woot.universal.Utils;

public class RegisterActivity extends Activity {
    EditText name, email, password;
    RadioGroup radioGroup;
    RadioButton admin, user;
    Button register;
    RadioButton selectRadio;
    String opinion;
    Context context;
    String type = "";
    String nameU,emailU,passwordU;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
        context = getApplicationContext();
        InitializeView();



        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                 nameU = name.getText().toString();
                 emailU = email.getText().toString();
                 passwordU = password.getText().toString();
                signupVolley();

            }
        });

    }
    public void InitializeView(){
        name = (EditText)findViewById(R.id.idEditName);
        email = (EditText)findViewById(R.id.idEditEmail);
        password = (EditText)findViewById(R.id.idEditPass);
        register = (Button)findViewById(R.id.idBtnRegister);
    }
    private void signupVolley() {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String result = response.toString();
                        JSONObject jsonObject = null;


                        Toast.makeText(context,""+result, Toast.LENGTH_LONG).show();



                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
                    }
                })


        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();


                params.put("user_name", nameU);
                params.put("email", emailU);
                params.put("password", passwordU);
                params.put("signup", "true");

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
}
