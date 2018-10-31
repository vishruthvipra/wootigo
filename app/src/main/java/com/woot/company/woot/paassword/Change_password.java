package com.woot.company.woot.paassword;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import com.woot.company.woot.R;

public class Change_password extends Activity {
    Button btn_reset_pass;
    EditText et_old_pass, et_new_pass, et_confirm_new_pass;
    String old_pass, new_pass, confirm_new_pass, user_id;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password);
        context = getApplicationContext();
        user_id = com.woot.company.woot.universal.Utils.getPreferences("user_id", context);
        et_old_pass = (EditText) findViewById(R.id.et_old_pass);
        et_new_pass = (EditText) findViewById(R.id.et_new_pass);
        et_confirm_new_pass = (EditText) findViewById(R.id.et_confirm_new_pass);
        btn_reset_pass = (Button) findViewById(R.id.idBtnRegister);
        btn_reset_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePass();
            }
        });
    }

    public void changePass() {

        old_pass = et_old_pass.getText().toString();
        new_pass = et_new_pass.getText().toString();
        confirm_new_pass = et_confirm_new_pass.getText().toString();
        if (TextUtils.isEmpty(old_pass)) {
            Toast.makeText(Change_password.this, "Enter Old Password", Toast.LENGTH_SHORT).show();

        } else if (TextUtils.isEmpty(new_pass)) {
            Toast.makeText(Change_password.this, "Enter New Password", Toast.LENGTH_SHORT).show();

        } else if (TextUtils.isEmpty(confirm_new_pass)) {
            Toast.makeText(Change_password.this, "Confirm New Password", Toast.LENGTH_SHORT).show();

        } else if (!new_pass.equalsIgnoreCase(confirm_new_pass)) {
            Toast.makeText(Change_password.this, "Password does not mach", Toast.LENGTH_SHORT).show();
            et_old_pass.setText("");
            et_new_pass.setText("");
            et_confirm_new_pass.setText("");

        } else {
            changePassword();
        }
    }

    private void changePassword() {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, com.woot.company.woot.universal.Utils.REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String result = response.toString();
                        Toast.makeText(context, result, Toast.LENGTH_LONG).show();
                        Intent intent=new Intent(Change_password.this, com.woot.company.woot.WootCabsActivity.class);
                        startActivity(intent);

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


                params.put("new_psw", new_pass);
                params.put("old_psw", old_pass);
                params.put("id", user_id);
                params.put("change_password", "true");

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

}
