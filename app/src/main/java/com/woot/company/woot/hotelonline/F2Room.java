package com.woot.company.woot.hotelonline;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;
import com.woot.company.woot.R;
import com.woot.company.woot.adapter.AdaptorHotels;
import com.woot.company.woot.adapter.AdaptorRoomList;
import com.woot.company.woot.detail.RoomDetail;
import com.woot.company.woot.universal.ConstantObjects;
import com.woot.company.woot.universal.Utils;

public class F2Room extends Fragment {
    ListView lvDetail;
    ArrayList<ConstantObjects> myList = new ArrayList<ConstantObjects>();
    AdaptorRoomList adaptor;
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.tab2_view, container, false);
        lvDetail = (ListView)rootView.findViewById(R.id.idlistviewRooms);
        context = getActivity();

        adaptor = new AdaptorRoomList(getActivity(), Utils.Rooms);
        lvDetail.setAdapter(adaptor);


//        Utils.Rooms.clear();
//        ParseQuery<ParseObject> query = ParseQuery.getQuery("Rooms");
////        query.whereEqualTo("category_id", CategoryId);
//        query.findInBackground(new FindCallback<ParseObject>() {
//            public void done(List<ParseObject> roomList, ParseException e) {
//                if (e == null) {
//                    for (int i = 0; i < roomList.size(); i++) {
//                        String objectId = roomList.get(i).getObjectId();
//                        String name = roomList.get(i).get("room_title").toString();
//                        String detail = roomList.get(i).get("room_detail").toString();
//                        String price = roomList.get(i).get("price").toString();
//
//                        ParseFile image = roomList.get(i).getParseFile("room_image");
////                        ParseObject parseObject = productList.get(i).getParseObject("room_image");
//                        String imageurl = "";
//                        if (image != null) {
//                            imageurl = image.getUrl();
//                        }
//                        ConstantObjects constantOb = new ConstantObjects(objectId, name, detail, price, imageurl);
//                        Utils.Rooms.add(constantOb);
//                        adaptor = new AdaptorRoomList(getActivity(), Utils.Rooms);
//                        lvDetail.setAdapter(adaptor);
//                    }
//                } else {
//                    Log.d("score", "Error: " + e.getMessage());
//                }
//            }
//        });
        lvDetail.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String rName = Utils.Rooms.get(position).getRoomName();
                String rDetail = Utils.Rooms.get(position).getRoomDetail();
                String rPrice = Utils.Rooms.get(position).getRoomPrice();
                String rImage = Utils.Rooms.get(position).getRoomImage();

                Intent intent = new Intent(context, RoomDetail.class);
                intent.putExtra("roomName", rName);
                intent.putExtra("roomDetail",rDetail);
                intent.putExtra("roomPrice", rPrice);
                intent.putExtra("roomImage", rImage);

                context.startActivity(intent);

            }
        });
        return rootView;
    }
}