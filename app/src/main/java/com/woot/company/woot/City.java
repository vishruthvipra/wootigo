package com.woot.company.woot;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.woot.company.woot.adapter.AdaptorCity;
import com.woot.company.woot.adapter.AdaptorHotels;
import com.woot.company.woot.hotelonline.Hotels;
import com.woot.company.woot.universal.AppController;
import com.woot.company.woot.universal.ConstantObjects;
import com.woot.company.woot.universal.Utils;
import com.woot.company.woot.paassword.Change_password;

public class City extends ActionBarActivity {

    ListView list;
    AdaptorCity adaptor;
    Utils utils = new Utils();
    Context context;
    String objectId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.city);
        context = getApplicationContext();
        InitializerView();
        if (Utils.City.isEmpty()){
            makeJsonObjforCategory();
        }else {
            adaptor=new AdaptorCity(context, utils.City);
            list.setAdapter(adaptor);
        }
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                objectId = utils.City.get(position).getCity_id();
                String cityName = utils.City.get(position).getCity_name();
                String cityImage = utils.City.get(position).getCity_image();
                String nu_hotels = utils.City.get(position).getNumber_of_hotels();

                Intent intent = new Intent(context, Hotels.class);
                intent.putExtra("objectId", objectId);
                intent.putExtra("hotel_name", cityName);
                intent.putExtra("hotel_image", cityImage);

                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_settings was selected
            case R.id.action_logout:
                // this is where you put your own code to do what you want.
                LogOut();
                break;
            default:
                break;
            case R.id.change_password:
                // this is where you put your own code to do what you want.
                Intent intent = new Intent(context, Change_password.class);
                startActivity(intent);
                break;

        }
        return super.onOptionsItemSelected(item);
    }
    private void LogOut() {
        Utils.savePreferences("Login", "false", getApplicationContext());
        Intent signin=new Intent(getApplicationContext(), WootCabsActivity.class);
        startActivity(signin);
        finish();
        overridePendingTransition(0, 0);
    }

    public void InitializerView(){
        list = (ListView)findViewById(R.id.idListViewCity);
    }
    public void DataFromParse(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("City");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> categoryList, ParseException e) {
                if (e == null) {
                    for (int i=0;i<categoryList.size();i++){
                        String objId=categoryList.get(i).getObjectId();
                        String name=categoryList.get(i).get("city").toString();
                        String nu = categoryList.get(i).get("nu_hotels").toString();
                        ParseFile image = categoryList.get(i).getParseFile("city_image");
                        String imageUrl="";
                        if (image != null) {
                            imageUrl = image.getUrl();
                        }
                        ConstantObjects cOb=new ConstantObjects(objId, name, imageUrl, nu);
                        utils.City.add(cOb);
                        adaptor=new AdaptorCity(context, utils.City);
                        list.setAdapter(adaptor);
                    }
                } else {
                    Log.e("score", "Error: " + e.getMessage());
                }
            }
        });
    }
    private void makeJsonObjforCategory() {
        String url = Utils.CITY_URL;
        JsonObjectRequest jsonobject = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray JA = response.getJSONArray("data");
                            for (int i = 0; i < JA.length(); i++) {
                                JSONObject secondobj = JA.getJSONObject(i);
                                String city_id = secondobj.getString("id");
                                String city_name = secondobj.getString("name");
                                String city_image = secondobj.getString("image");
                                String number_of_hotels = secondobj.getString("number_of_hotels");

                                ConstantObjects cOb = new ConstantObjects(city_id, city_name, city_image, number_of_hotels, "", "");
                                utils.City.add(cOb);
                                adaptor = new AdaptorCity(context, utils.City);
                                list.setAdapter(adaptor);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
        };
        AppController.getInstance().addToRequestQueue(jsonobject);
    }

}
