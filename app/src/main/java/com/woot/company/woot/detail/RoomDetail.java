package com.woot.company.woot.detail;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.woot.company.woot.R;
import com.woot.company.woot.universal.ImageLoader;

public class RoomDetail extends Activity {
    TextView roomTitle, price, detail, services;
    Button btnBook;
    ImageView image;
    ImageLoader imageLoader;
    Context context;
    String RoomTitle, Price, Detail, Services, Image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.room_detail);
        context = getApplicationContext();
        InitializeView();
        ExtrasCollection();

        imageLoader=new ImageLoader(context);

        btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BookRoom.class);
                    intent.putExtra("rName", RoomTitle);
                    intent.putExtra("rPrice", Price);

                startActivity(intent);
            }
        });
        roomTitle.setText(RoomTitle);
        price.setText(Price);
        detail.setText(Detail);
        imageLoader.DisplayImage(Image, image);
    }
    private void ExtrasCollection() {
        Bundle extras = getIntent().getExtras();
        RoomTitle = extras.getString("roomName");
        Price = extras.getString("roomPrice");
        Detail = extras.getString("roomDetail");
//        Services = extras.getString("");
        Image = extras.getString("roomImage");

    }
    public void InitializeView(){
        roomTitle = (TextView)findViewById(R.id.idTitle);
        price = (TextView)findViewById(R.id.idPriceRoom);
        detail = (TextView)findViewById(R.id.idDetail);
        services = (TextView)findViewById(R.id.idServices);
        btnBook = (Button)findViewById(R.id.idBtnBookRoom);
//        image = (ImageView)findViewById(R.id.idBackRoomDetailImage);
    }
}
