package com.woot.company.woot.hotelonline;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.woot.company.woot.R;

import java.util.List;

import com.woot.company.woot.universal.Utils;

public class F5Contact extends Fragment {
    TextView address, email, phone, web;
    ImageView facebook, twitter, adviser, location;
    View view;
    String address1;
    String email1;
    String phone1;
    String website1;
    Context context;
    ParseQuery<ParseObject> query = ParseQuery.getQuery("Hotel_detail");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = LayoutInflater.from(getActivity()).inflate(R.layout.tab5_view, null);
        context = getActivity();
        InitializeView();

        address1 = Utils.getPreferences("add", getActivity());
        email1  = Utils.getPreferences("email", getActivity());
        phone1  = Utils.getPreferences("ph", getActivity());
        website1 = Utils.getPreferences("web", getActivity());

        address.setText(address1);
        email.setText(email1);
        phone.setText(phone1);
        web.setText(website1);

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("mailto:" + email1);
                Intent it = new Intent(Intent.ACTION_SENDTO, uri);
                it.putExtra(Intent.EXTRA_SUBJECT, "Booking request for: Room");
                startActivity(it);
            }
        });
        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone_no= phone.getText().toString().replaceAll("-", "");
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + phone_no));
                startActivity(callIntent);
            }
        });
        web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("http://www.google.com"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("geo:38.899533,-77.036476");
                Intent it = new Intent(Intent.ACTION_VIEW,uri);
                context.startActivity(it);
            }
        });
        return view;
    }
    public void InitializeView(){
        address = (TextView)view.findViewById(R.id.idTvAddress);
        email = (TextView)view.findViewById(R.id.idEmail);
        phone = (TextView)view.findViewById(R.id.idPhone);
        web = (TextView)view.findViewById(R.id.idWeb);
        facebook = (ImageView)view.findViewById(R.id.fb);
        twitter = (ImageView)view.findViewById(R.id.twitter);
        adviser = (ImageView)view.findViewById(R.id.idAdviser);
        location = (ImageView)view.findViewById(R.id.idImageViewMap);
    }
}