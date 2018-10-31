package com.woot.company.woot.hotelonlineadminside;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

import com.woot.company.woot.adapter.AdaptorCity;
import com.woot.company.woot.adapter.AdaptorHotels;
import com.woot.company.woot.R;
import com.woot.company.woot.universal.ConstantObjects;
import com.woot.company.woot.universal.Utils;

public class HotelsAdmin extends Activity {
    ListView list;
    ImageButton addNew, refresh;
    AdaptorHotels adaptor;
    Utils utils = new Utils();
    Context context;
    String CityId;
    String cityId;
    String hID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hotels_admin);
        context = getApplicationContext();
        InitializerView();
        cityId =Utils.getPreferences("CityID",context);

        Bundle extras = getIntent().getExtras();
        CityId = extras.getString("objectId");

        if (Utils.Hotels.isEmpty()){
            Utils.savePreferences("CityID", CityId,context) ;
            DataFromParse();
        }else
        if(cityId.equalsIgnoreCase(CityId)) {
            adaptor=new AdaptorHotels(context, utils.Hotels);
            list.setAdapter(adaptor);
        }else {
            Utils.savePreferences("CityID", CityId,context) ;
            Utils.Hotels.clear();
            DataFromParse();
        }
        addNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RefreshList();
                Intent intent = new Intent(context, AddHotel.class);
                startActivity(intent);
            }
        });
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RefreshList();
            }
        });
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                RefreshList();
            String objectId =   utils.Hotels.get(position).getHotelId();
            String hotelName =  utils.Hotels.get(position).getHotelName();
            String hotelDes =   utils.Hotels.get(position).getHotelDescription();
            String hotelImage = utils.Hotels.get(position).getHotelImage();
            String hotelAdd =   utils.Hotels.get(position).getAddress();
            String hotelEmail = utils.Hotels.get(position).getEmail();
            String hotelPhone = utils.Hotels.get(position).getPhone();
            String hotelWeb =   utils.Hotels.get(position).getWebsite();
////                int pos = position;
            Utils.savePreferences("Hotel_Id", objectId, context);
            Utils.savePreferences("name", hotelName, context);
            Utils.savePreferences("des", hotelDes, context);
            Utils.savePreferences("back", hotelImage, context);
            Utils.savePreferences("add", hotelAdd, context);
            Utils.savePreferences("email", hotelEmail, context);
            Utils.savePreferences("ph", hotelPhone, context);
            Utils.savePreferences("web", hotelWeb, context);


            Intent intent = new Intent(context, EditHotel.class);
            intent.putExtra("objectId", objectId);
//            intent.putExtra("city_ID", CityId);
//            intent.putExtra("hotel_name", hotelName);
//            intent.putExtra("hotel_image", hotelImage);
//                intent.putExtra("position", pos);

            startActivity(intent);
            }
        });
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            hID = Utils.Hotels.get(position).getObjectId();
            AlertDialog.Builder alert = new AlertDialog.Builder(
                    HotelsAdmin.this);
            alert.setTitle("Alert!!");
            alert.setMessage("Are you sure you want to delete this city");
            alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
//                        DeleteCity();
                }
            });
            alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialog.dismiss();
                }
            });

            alert.show();
            return false;
            }
        });
    }
    public void DeleteHotel(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Hotel");
        query.whereEqualTo("objectId", hID);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> invites, ParseException e) {
            if (e == null) {
                for (ParseObject invite : invites) {
                    invite.deleteInBackground();
                }
            } else {
            }
            }
        });
        ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Rooms");
        query2.whereEqualTo("hotel_id", hID);
        query2.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> invites, ParseException e) {
            if (e == null) {
                for (ParseObject invite : invites) {
                    invite.deleteInBackground();
                }
            } else {
            }
            }
        });
    }
    public void InitializerView(){
        list = (ListView)findViewById(R.id.idListViewAddHotels);
        addNew = (ImageButton) findViewById(R.id.idBtnAddNewHotel);
        refresh = (ImageButton) findViewById(R.id.idRefreshHotel);
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
    public void RefreshList(){
        Utils.Hotels.clear();
        final ParseQuery<ParseObject> query = ParseQuery.getQuery("Hotel");
        query.whereEqualTo("city_id", CityId);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> categoryList, ParseException e) {
            if (e == null) {
                for (int i=0;i<categoryList.size();i++){
                    String objId=categoryList.get(i).getObjectId();
                    String nameHotel=categoryList.get(i).get("name").toString();
                    String address = categoryList.get(i).get("address").toString();
                    String price = categoryList.get(i).get("price").toString();
                    String priceOld = categoryList.get(i).get("old_price").toString();
                    String des = categoryList.get(i).get("description").toString();
                    String rating = categoryList.get(i).get("rating").toString();
                    ParseFile imageHotel = categoryList.get(i).getParseFile("background");
                    String imageUrl="";
                    if (imageHotel != null) {
                        imageUrl = imageHotel.getUrl();
                    }
                    ParseFile imageIcon = categoryList.get(i).getParseFile("icon");
                    String imageIconUrl="";
                    if (imageIcon != null) {
                        imageIconUrl = imageIcon.getUrl();
                    }
                    String email = categoryList.get(i).get("email").toString();
                    String phone = categoryList.get(i).get("phone").toString();
                    String website = categoryList.get(i).get("website").toString();

                    ConstantObjects cOb=new ConstantObjects(objId, nameHotel, address, price, priceOld, des, imageUrl, imageIconUrl,rating,
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
}
