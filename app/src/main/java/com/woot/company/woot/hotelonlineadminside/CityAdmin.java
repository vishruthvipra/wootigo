package com.woot.company.woot.hotelonlineadminside;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

import com.woot.company.woot.adapter.AdaptorCity;
import com.woot.company.woot.adapter.AdaptorRoomList;
import com.woot.company.woot.hotelonline.Hotels;
import com.woot.company.woot.WootCabsActivity;
import com.woot.company.woot.R;
import com.woot.company.woot.universal.ConstantObjects;
import com.woot.company.woot.universal.Utils;

public class CityAdmin extends Activity {
    ListView list;
    ImageButton add, refresh;
    AdaptorCity adaptor;
    Utils utils = new Utils();
    Context context;
    String cityId;
    TextView logout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.city_admin);
        context = getApplicationContext();
        InitializerView();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddCity.class);
                startActivity(intent);
            }
        });
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RefreshList();
            }
        });

//        logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ParseUser.logOut();
////                ParseUser currentUser = ParseUser.getCurrentUser();
//            }
//        });

        if (Utils.City.isEmpty()){
            DataFromParse();
        }else {
            adaptor=new AdaptorCity(context, utils.City);
            list.setAdapter(adaptor);
        }
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String objectId = utils.City.get(position).getCityId();
                String cityName = utils.City.get(position).getCityName();
                String cityImage = utils.City.get(position).getCityImage();
                String nuHotels = utils.City.get(position).getHotelsNu();

                Utils.savePreferences("City_Id", objectId, context);
                Utils.savePreferences("nu_hotel", nuHotels, context);

                Intent intent = new Intent(context, HotelsAdmin.class);
                intent.putExtra("objectId", objectId);
                intent.putExtra("hotel_name", cityName);
                intent.putExtra("hotel_image", cityImage);
                startActivity(intent);
            }
        });
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final String objectId = utils.City.get(position).getCityId();
                final String cityName = utils.City.get(position).getCityName();
                final String cityImage = utils.City.get(position).getCityImage();
                Utils.savePreferences("IdC", objectId, context);
                Utils.savePreferences("nameC", cityName, context);
                Utils.savePreferences("imageC", cityImage, context);

                cityId = Utils.City.get(position).getObjectId();
                AlertDialog.Builder alert = new AlertDialog.Builder(
                        CityAdmin.this);
                alert.setTitle("Alert!!");
                alert.setMessage("Are you sure you want to delete this city");
                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        DeleteCity();
                    }
                });
                alert.setNeutralButton("Edit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(CityAdmin.this, AddCity.class);
                        intent.putExtra("IdC", objectId);
                        intent.putExtra("nameC", cityName);
                        intent.putExtra("imageC", cityImage);
                        startActivity(intent);
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

    public void DeleteCity(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("City");
        query.whereEqualTo("objectId", cityId);
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
        ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Hotel");
        query2.whereEqualTo("city_id", cityId);
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
        ParseQuery<ParseObject> query3 = ParseQuery.getQuery("Rooms");
        query3.whereEqualTo("city_id", cityId);
        query3.findInBackground(new FindCallback<ParseObject>() {
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
        list = (ListView)findViewById(R.id.idListViewCityAdmin);
        add = (ImageButton)findViewById(R.id.idBtnAddNewCity);
        refresh = (ImageButton)findViewById(R.id.idRefreshCity);
        logout = (TextView) findViewById(R.id.idTextLogoutAdmin);
    }
    public void RefreshList(){
        Utils.City.clear();
        final ParseQuery<ParseObject> query = ParseQuery.getQuery("City");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> categoryList, ParseException e) {
                if (e == null) {
                    for (int i=0;i<categoryList.size();i++){
                        String objId=categoryList.get(i).getObjectId();
                        String nameCity=categoryList.get(i).get("city").toString();
                        String nuHotels = categoryList.get(i).get("nu_hotels").toString();
                        ParseFile imageCity = categoryList.get(i).getParseFile("city_image");
                        String imageUrl="";
                        if (imageCity != null) {
                            imageUrl = imageCity.getUrl();
                        }
                        ConstantObjects cOb=new ConstantObjects(objId, nameCity, imageUrl, nuHotels);
                        utils.City.add(cOb);
                        adaptor=new AdaptorCity(context, utils.City);
                        list.setAdapter(adaptor);
                    }
                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });
    }
    public void DataFromParse(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("City");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> categoryList, ParseException e) {
                if (e == null) {
                    for (int i=0;i<categoryList.size();i++){
                        String objId=categoryList.get(i).getObjectId();
                        String nameCity=categoryList.get(i).get("city").toString();

                        ParseFile imageCity = categoryList.get(i).getParseFile("city_image");
                        String imageUrl="";
                        if (imageCity != null) {
                            imageUrl = imageCity.getUrl();
                        }
                        String nuHotels = categoryList.get(i).get("nu_hotels").toString();

                        ConstantObjects cOb=new ConstantObjects(objId, nameCity, imageUrl, nuHotels);
                        utils.City.add(cOb);
                        adaptor=new AdaptorCity(context, utils.City);
                        list.setAdapter(adaptor);
                    }
                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });
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
        }
        return super.onOptionsItemSelected(item);
    }
    private void LogOut() {
        ParseUser.logOut();

        Utils.savePreferences("Login", "", context);
        Intent signin=new Intent(context, WootCabsActivity.class);
        startActivity(signin);
        finish();
        overridePendingTransition(0, 0);
    }
}
