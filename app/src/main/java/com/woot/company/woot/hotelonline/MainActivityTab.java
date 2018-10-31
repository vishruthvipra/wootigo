package com.woot.company.woot.hotelonline;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.woot.company.woot.R;

import java.util.List;

import com.woot.company.woot.adapter.AdaptorHotels;
import com.woot.company.woot.universal.ConstantObjects;
import com.woot.company.woot.universal.Utils;

public class MainActivityTab extends FragmentActivity {
    private FragmentTabHost mTabHost;
    Context context;
    ParseQuery<ParseObject> query = ParseQuery.getQuery("Rooms");
    String object_id;
    String Obj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bottom_tabs);
        context = getApplicationContext();
        object_id = Utils.getPreferences("HotelId", getApplicationContext());

        Bundle extras = getIntent().getExtras();
        Obj = extras.getString("objectId");

        if (Utils.Rooms.isEmpty()){
            Utils.savePreferences("HotelId",Obj,getApplicationContext()) ;
            RoomsList();
        }else
        if(object_id.equalsIgnoreCase(Obj)) {
//            adaptor=new AdaptorHotels(getApplicationContext(), utils.Hotels);
//            list.setAdapter(adaptor);
        }else {
            Utils.savePreferences("HotelId",Obj,getApplicationContext()) ;
            Utils.Rooms.clear();
            RoomsList();
        }




        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
//testing with custom view implementation to add icon
        TabHost.TabSpec spec = mTabHost.newTabSpec("tab" + 1);
        View tabIndicator =
                LayoutInflater.from(this).inflate(R.layout.tab1_icon, null, false);
        TextView title = (TextView) tabIndicator.findViewById(R.id.title);
        title.setText("Home");
        title.setTextSize(10);
        ImageView icon = (ImageView) tabIndicator.findViewById(R.id.icon);
        Drawable myIcon = getResources().getDrawable( R.drawable.image_info );
        icon.setImageDrawable(myIcon);
        icon.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        spec.setIndicator(tabIndicator);
        mTabHost.addTab(spec,
                F1Home.class, null);

        TabHost.TabSpec spec1 = mTabHost.newTabSpec("tab" + 2);
        View tabIndicator1 =
                LayoutInflater.from(this).inflate(R.layout.tab1_icon, null, false);
        TextView title1 = (TextView) tabIndicator1.findViewById(R.id.title);
        title1.setText("Rooms");
        title1.setTextSize(10);
        ImageView icon1 = (ImageView) tabIndicator1.findViewById(R.id.icon);
        Drawable myIcon1 = getResources().getDrawable( R.drawable.image_info );
        icon1.setImageDrawable(myIcon1);
        icon1.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        spec1.setIndicator(tabIndicator1);
        mTabHost.addTab(spec1 , F2Room.class, null);

        TabHost.TabSpec spec3 = mTabHost.newTabSpec("tab" + 3);
        View tabIndicator3 =
                LayoutInflater.from(this).inflate(R.layout.tab1_icon, null, false);
        TextView title3 = (TextView) tabIndicator3.findViewById(R.id.title);
        title3.setText("Gallery");
        title3.setTextSize(10);
        ImageView icon3 = (ImageView) tabIndicator3.findViewById(R.id.icon);
        Drawable myIcon3 = getResources().getDrawable( R.drawable.image_info );
        icon3.setImageDrawable(myIcon3);
        icon3.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        spec3.setIndicator(tabIndicator3);
        mTabHost.addTab(spec3 , F3Gallery.class, null);

        TabHost.TabSpec spec4 = mTabHost.newTabSpec("tab" + 4);
        View tabIndicator4 =
                LayoutInflater.from(this).inflate(R.layout.tab1_icon, null, false);
        TextView title4 = (TextView) tabIndicator4.findViewById(R.id.title);
        title4.setText("FeedBack");
        title4.setTextSize(10);
        ImageView icon4 = (ImageView) tabIndicator4.findViewById(R.id.icon);
        Drawable myIcon4 = getResources().getDrawable( R.drawable.image_info );
        icon4.setImageDrawable(myIcon4);
        icon4.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        spec4.setIndicator(tabIndicator4);
        mTabHost.addTab(spec4 , F4FeedBack.class, null);

        TabHost.TabSpec spec5 = mTabHost.newTabSpec("tab" + 5);
        View tabIndicator5 =
                LayoutInflater.from(this).inflate(R.layout.tab1_icon, null, false);
        TextView title5 = (TextView) tabIndicator5.findViewById(R.id.title);
        title5.setText("Contact");
        title5.setTextSize(10);
        ImageView icon5 = (ImageView) tabIndicator5.findViewById(R.id.icon);
        Drawable myIcon5 = getResources().getDrawable( R.drawable.image_info );
        icon5.setImageDrawable(myIcon5);
        icon5.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        spec5.setIndicator(tabIndicator5);
        mTabHost.addTab(spec5 , F5Contact.class, null);
    }
    public void RoomsList(){
        query.whereEqualTo("hotel_id", Obj);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    for (int i = 0; i<list.size(); i++) {
                        String objectId = list.get(i).getObjectId();
                        String RTitle = list.get(i).get("room_title").toString();
                        String RDetail = list.get(i).get("room_detail").toString();
                        String RPrice = list.get(i).get("price").toString();
                        ParseFile image = list.get(i).getParseFile("room_image");
                        String imageUrl = "";
                        if (image != null) {
                            imageUrl = image.getUrl();
                        }
                        ConstantObjects constantOb = new ConstantObjects(objectId, RTitle, RDetail, RPrice, imageUrl);
                        Utils.Rooms.add(constantOb);
                    }
                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });
    }
}
