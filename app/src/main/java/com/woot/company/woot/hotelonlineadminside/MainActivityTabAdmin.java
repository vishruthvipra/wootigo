package com.woot.company.woot.hotelonlineadminside;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.woot.company.woot.hotelonline.F1Home;
import com.woot.company.woot.hotelonline.F2Room;
import com.woot.company.woot.hotelonline.F3Gallery;
import com.woot.company.woot.hotelonline.F4FeedBack;
import com.woot.company.woot.hotelonline.F5Contact;
import com.woot.company.woot.R;
import com.woot.company.woot.universal.Utils;

public class MainActivityTabAdmin extends FragmentActivity {
    private FragmentTabHost mTabHost;
    Context context;
    String Obj;
    String cityId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bottom_tabs);
        context = getApplicationContext();
        Bundle extras = getIntent().getExtras();
        Obj = extras.getString("objectId");
        cityId = extras.getString("city_ID");
        Utils.savePreferences("hotel_ID", Obj, context);
        Utils.savePreferences("city_ID", cityId, context);

        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
//testing with custom view implementation to add icon
        TabHost.TabSpec spec = mTabHost.newTabSpec("tab" + 1);
        View tabIndicator =
                LayoutInflater.from(this).inflate(R.layout.tab1_icon, null, false);
        TextView title = (TextView) tabIndicator.findViewById(R.id.title);
        title.setText("Hotels");
        title.setTextSize(10);
        ImageView icon = (ImageView) tabIndicator.findViewById(R.id.icon);
        Drawable myIcon = getResources().getDrawable( R.drawable.image_info );
        icon.setImageDrawable(myIcon);
        icon.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        spec.setIndicator(tabIndicator);
        mTabHost.addTab(spec,
                F1HomeAdmin.class, null);

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
        mTabHost.addTab(spec1 , F2RoomAdmin.class, null);

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
        mTabHost.addTab(spec3 , F3GalleryAdmin.class, null);

    }
}
