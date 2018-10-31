package com.woot.company.woot.hotelonlineadminside;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.method.HideReturnsTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

import com.woot.company.woot.adapter.AdaptorRoomList;
import com.woot.company.woot.R;
import com.woot.company.woot.universal.ConstantObjects;
import com.woot.company.woot.universal.Utils;

public class F2RoomAdmin extends Fragment {
    ImageButton addNew, refresh;
    View view;
    ListView listView;
    AdaptorRoomList adaptorRoomList;
    Context context;
    String hotelId;
    String HId;
    String objId;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.admin_tab2_view, container, false);

        InitializeView();
        context = getActivity();
        HId = Utils.getPreferences("hotel", context);

        hotelId = Utils.getPreferences("hotel_ID", context);


        if (Utils.Rooms.isEmpty()){
            Utils.savePreferences("hotel", hotelId, context);
            RoomList();

        }else if (HId.equalsIgnoreCase(hotelId)){
            adaptorRoomList = new AdaptorRoomList(context, Utils.Rooms);
            listView.setAdapter(adaptorRoomList);
        }else {
            Utils.savePreferences("hotel", hotelId, context);
            Utils.Rooms.clear();
            RoomList();
        }

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RefreshList();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String roomId = Utils.Rooms.get(position).getObjectId();
                String roomName = Utils.Rooms.get(position).getRoomName();
                String roomDetail = Utils.Rooms.get(position).getRoomDetail();
                String roomPrice = Utils.Rooms.get(position).getRoomPrice();
                String roomImage = Utils.Rooms.get(position).getRoomImage();
                Utils.savePreferences("idR", roomId, context);
                Utils.savePreferences("roomN", roomName, context);

                Intent intent = new Intent(context, AddRoom.class);
                intent.putExtra("object_id", roomId);
                intent.putExtra("room_name", roomName);
                intent.putExtra("room_detail", roomDetail);
                intent.putExtra("room_price", roomPrice);
                intent.putExtra("room_image", roomImage);
                startActivity(intent);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                objId = Utils.Rooms.get(position).getObjectId();
                        AlertDialog.Builder alert = new AlertDialog.Builder(
                        F2RoomAdmin.this.context);
                alert.setTitle("Alert!!");
                alert.setMessage("Are you sure to delete record");
                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ParseQuery<ParseObject> query = ParseQuery.getQuery("Rooms");
                        query.whereEqualTo("objectId", objId);
                        query.findInBackground(new FindCallback<ParseObject>() {
                            public void done(List<ParseObject> invites, ParseException e) {
                                if (e == null) {
                                    // iterate over all messages and delete them
                                    for(ParseObject invite : invites)
                                    {
                                        invite.deleteInBackground();
                                        Utils.Rooms.remove(objId);
                                    }
                                } else {
                                    //Handle condition here
                                }
                            }
                        });
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
        addNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.savePreferences("idR", "", context);
                Utils.savePreferences("roomN", "", context);
                Intent intent = new Intent(context, AddRoom.class);
                startActivity(intent);
            }
        });
        return view;
    }
    public void RefreshList(){
        Utils.Rooms.clear();
        final ParseQuery<ParseObject> query = ParseQuery.getQuery("Rooms");
        query.whereEqualTo("hotel_id", hotelId);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> roomList, ParseException e) {
                if (e == null) {
                    for (int i = 0; i < roomList.size(); i++) {
                        String objectId = roomList.get(i).getObjectId();
                        String name = roomList.get(i).get("room_title").toString();
                        String detail = roomList.get(i).get("room_detail").toString();
                        String price = roomList.get(i).get("price").toString();
                        ParseFile image = roomList.get(i).getParseFile("room_image");
                        String imageurl = "";
                        if (image != null) {
                            imageurl = image.getUrl();
                        }
                        ConstantObjects constantOb = new ConstantObjects(objectId, name, detail, price, imageurl);
                        Utils.Rooms.add(constantOb);
                        adaptorRoomList = new AdaptorRoomList(context, Utils.Rooms);
                        listView.setAdapter(adaptorRoomList);
                    }
                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });
    }

    public void EditRoomsDetail(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Rooms");

// Retrieve the object by id
        query.getInBackground(objId, new GetCallback<ParseObject>() {
            public void done(ParseObject roomDetail, ParseException e) {
                if (e == null) {
                    // Now let's update it with some new data. In this case, only cheatMode and score
                    // will get sent to the Parse Cloud. playerName hasn't changed.
                    roomDetail.put("room_title", "changed");
                    roomDetail.put("room_detail", "changed");
                    roomDetail.put("price", "0");
//                    roomDetail.put("room_image", "");
                    roomDetail.saveInBackground();
                }
            }
        });
    }
    public void RoomList(){
        final ParseQuery<ParseObject> query = ParseQuery.getQuery("Rooms");
        query.whereEqualTo("hotel_id", hotelId);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> roomList, ParseException e) {
                if (e == null) {
                    for (int i = 0; i < roomList.size(); i++) {
                        String objectId = roomList.get(i).getObjectId();
                        String name = roomList.get(i).get("room_title").toString();
                        String detail = roomList.get(i).get("room_detail").toString();
                        String price = roomList.get(i).get("price").toString();

                        ParseFile image = roomList.get(i).getParseFile("room_image");
//                        ParseObject parseObject = productList.get(i).getParseObject("room_image");
                        String imageurl = "";
                        if (image != null) {
                            imageurl = image.getUrl();
                        }
                        ConstantObjects constantOb = new ConstantObjects(objectId, name, detail, price, imageurl);
                        Utils.Rooms.add(constantOb);
                        adaptorRoomList = new AdaptorRoomList(context, Utils.Rooms);
                        listView.setAdapter(adaptorRoomList);
                    }
                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });
    }

    private void InitializeView(){
        addNew = (ImageButton)view.findViewById(R.id.idAddNew);
        refresh = (ImageButton)view.findViewById(R.id.idRefresh);
        listView = (ListView)view.findViewById(R.id.idListViewRoomsAdmin);
    }
}
