package com.woot.company.woot.hotelonline;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.woot.company.woot.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.woot.company.woot.adapter.AdaptorHotels;
import com.woot.company.woot.detail.HotelDetail;
import com.woot.company.woot.universal.ConstantObjects;
import com.woot.company.woot.universal.Utils;

public class Hotels extends ActionBarActivity {

    ListView list;
    AdaptorHotels adaptor;
    Utils utils = new Utils();
    String CityId;
    String cityId;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hotels);
        context = getApplicationContext();
        InitializerView();
        cityId=Utils.getPreferences("CityID",context);

        Bundle extras = getIntent().getExtras();
        CityId = extras.getString("objectId");


        if (Utils.Hotels.isEmpty()){
            Utils.savePreferences("CityID",CityId,context) ;
           getHotelsFromCity();
        }else
        if(cityId.equalsIgnoreCase(CityId)) {
            adaptor=new AdaptorHotels(context, utils.Hotels);
            list.setAdapter(adaptor);
        }else {
            Utils.savePreferences("CityID",CityId,context) ;
            Utils.Hotels.clear();
            getHotelsFromCity();
        }
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String objectId =   utils.Hotels.get(position).getHotel_id();
            String hotelName =  utils.Hotels.get(position).getHotel_name();
            String hotelDes =   utils.Hotels.get(position).getDescription();
            String hotelPrice = utils.Hotels.get(position).getNew_price();
            String hotelImage = utils.Hotels.get(position).getBackground_image();

            String hotelAdd =   utils.Hotels.get(position).getAddress();
            String hotelEmail = utils.Hotels.get(position).getEmail();
            String hotelPhone = utils.Hotels.get(position).getPhone();
            String hotelWeb =   utils.Hotels.get(position).getWebsite();
            String rating =     utils.Hotels.get(position).getRating();
                String icon =     utils.Hotels.get(position).getIcon();
                String address =     utils.Hotels.get(position).getAddress();
//                int pos = position;
                Utils.savePreferences("address",  address,  context);
            Utils.savePreferences("name",  hotelName,  context);
            Utils.savePreferences("des",   hotelDes,   context);
            Utils.savePreferences("pri",   hotelPrice, context);
            Utils.savePreferences("back",  hotelImage, context);
            Utils.savePreferences("add",   hotelAdd,   context);
            Utils.savePreferences("email", hotelEmail, context);
            Utils.savePreferences("ph",    hotelPhone, context);
            Utils.savePreferences("web",   hotelWeb,   context);
            Utils.savePreferences("rat",   rating,     context);
                Utils.savePreferences("icon",   icon,     context);


            Intent intent = new Intent(context, HotelDetail.class);
            intent.putExtra("objectId", objectId);
            intent.putExtra("hotel_name", hotelName);
            intent.putExtra("hotel_image", hotelImage);
            intent.putExtra("hotel_email", hotelEmail);

            startActivity(intent);
            }
        });

    }

    public void InitializerView(){
        list = (ListView)findViewById(R.id.idListViewHotels);
    }
    public void DataFromParse(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Hotel");
        query.whereEqualTo("city_id", CityId);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> categoryList, ParseException e) {
            if (e == null) {
                for (int i=0;i<categoryList.size();i++){
                    String objId=categoryList.get(i).getObjectId();
                    String name=categoryList.get(i).get("name").toString();
                    String address = categoryList.get(i).get("address").toString();
                    String price = categoryList.get(i).get("price").toString();
                    String priceOld = categoryList.get(i).get("old_price").toString();
                    String des = categoryList.get(i).get("description").toString();
                    String rating = categoryList.get(i).get("rating").toString();
                    ParseFile image = categoryList.get(i).getParseFile("background");
                    String imageUrl="";
                    if (image != null) {
                        imageUrl = image.getUrl();
                    }
                    ParseFile imageIcon = categoryList.get(i).getParseFile("icon");
                    String imageIconUrl="";
                    if (imageIcon != null) {
                        imageIconUrl = imageIcon.getUrl();
                    }

                    String email = categoryList.get(i).get("email").toString();
                    String phone = categoryList.get(i).get("phone").toString();
                    String website = categoryList.get(i).get("website").toString();

                    ConstantObjects cOb=new ConstantObjects(objId, name, address, price, priceOld, des, imageUrl, imageIconUrl,rating,
                            email, phone, website);
                    utils.Hotels.add(cOb);
                    adaptor=new AdaptorHotels(context, utils.Hotels);
                    list.setAdapter(adaptor);
                }
            } else {
                Log.d("score", "Error: " + e.getMessage());
            }
            }
        });
    }
    private void getHotelsFromCity() {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String result = response.toString();
                        try {

                            JSONObject object=new JSONObject(result);
                            JSONArray JA = object.getJSONArray("data");
                            for(int i=0;i<JA.length(); i++){
                                JSONObject secondobj = JA.getJSONObject(i);
                                String hotel_id = secondobj.getString("id");
                                String hotel_name = secondobj.getString("hotel_name");
                                String phone = secondobj.getString("phone");
                                String address = secondobj.getString("address");
                                String old_price = secondobj.getString("old_price");
                                String new_price = secondobj.getString("new_price");
                                String description = secondobj.getString("description");
                                String background_image = secondobj.getString("background_image");
                                String icon = secondobj.getString("icon");
                                String website = secondobj.getString("website");
                                String email = secondobj.getString("email");
                                String rating = secondobj.getString("rating");
                                ConstantObjects cOb=new ConstantObjects(hotel_id, hotel_name, phone, address, old_price, new_price, description, background_image,icon,
                                        website, email, rating,"");
                                utils.Hotels.add(cOb);
                                adaptor=new AdaptorHotels(context, utils.Hotels);
                                list.setAdapter(adaptor);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


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


                params.put("city_id", cityId);

                params.put("hotelsByCityId", "true");

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
}
