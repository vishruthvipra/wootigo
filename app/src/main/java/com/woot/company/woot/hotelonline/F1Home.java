package com.woot.company.woot.hotelonline;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.woot.company.woot.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

import com.woot.company.woot.universal.ImageLoader;
import com.woot.company.woot.universal.Utils;


public class F1Home extends Fragment {
	TextView hotelName, welcome;
	ImageView facebook, twitter, tripAdviser, backHotel, circleIcon;
    View view;
    Context context;
    ImageLoader imageLoader;
//    ParseQuery<ParseObject> query = ParseQuery.getQuery("Hotel_detail");

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.tab1_view, container, false);
        InitializeView();
        context = getActivity();
        imageLoader = new ImageLoader(context);

        String name = Utils.getPreferences("name", getActivity());
        String description = Utils.getPreferences("des", getActivity());
        String backImage = Utils.getPreferences("back", getActivity());

        imageLoader.DisplayImage(backImage, backHotel);
        circleIcon.bringToFront();

        hotelName.setText(name);
        welcome.setText(description);

//        query.findInBackground(new FindCallback<ParseObject>() {
//            public void done(List<ParseObject> list, ParseException e) {
//                if (e == null) {
//
//                        String Name = list.get(0).get("hotel_name").toString();
//                        String Description = list.get(0).get("description").toString();
//
////                    String BackImage = list.get(0).get("").toString();
////                    String Logo = list.get(0).get("").toString();
//
////                        welcome.setText(Description);
//                } else {
//                    Log.d("score", "Error: " + e.getMessage());
//                }
//            }
//        });

        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("http://www.facebook.com"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("http://www.twitter.com"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        tripAdviser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("http://www.tripadvisor.com/"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });


		return view;
	}

    public void InitializeView(){
        hotelName = (TextView)view.findViewById(R.id.textView2);
        welcome = (TextView)view.findViewById(R.id.idTextDetail);
        facebook = (ImageView)view.findViewById(R.id.facebookLink);
        twitter = (ImageView)view.findViewById(R.id.twitterLink);
        tripAdviser = (ImageView)view.findViewById(R.id.visiterLink);
        backHotel = (ImageView)view.findViewById(R.id.idBackImage);
        circleIcon = (ImageView)view.findViewById(R.id.idCircleImage);
    }
}
