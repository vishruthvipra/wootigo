package com.woot.company.woot.hotelonlineadminside;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

import com.woot.company.woot.adapter.AdaptorGallery;
import com.woot.company.woot.adapter.AdaptorRoomList;
import com.woot.company.woot.R;
import com.woot.company.woot.universal.ConstantObjects;
import com.woot.company.woot.universal.Utils;

public class F3GalleryAdmin extends Fragment {
    ImageButton addNew, refresh;
    GridView gridView;
    View view;
    Context context;
    String hotelId;
    String HId;
    AdaptorGallery adaptorGallery;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.admin_tab3_view, container, false);
        InitializeView();
        context = getActivity();
        hotelId = Utils.getPreferences("Hotel_Id", context);
        HId = Utils.getPreferences("hotel", context);
        if (Utils.Gallery.isEmpty()){
            Utils.savePreferences("hotel", hotelId, context);
            PhotoGallery();
        }else if (HId.equalsIgnoreCase(hotelId)){
            adaptorGallery = new AdaptorGallery(context, Utils.Gallery);
            gridView.setAdapter(adaptorGallery);
        }else {
            Utils.savePreferences("hotel", hotelId, context);
            Utils.Gallery.clear();
            PhotoGallery();
        }
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RefreshList();
            }
        });

        addNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddPhoto.class);
                startActivity(intent);
            }
        });

        return view;
    }
    private void RefreshList(){
        Utils.Gallery.clear();
        final ParseQuery<ParseObject> query = ParseQuery.getQuery("Gallery");
        query.whereEqualTo("hotel_id", hotelId);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> photoList, ParseException e) {
                if (e == null) {
                    for (int i = 0; i < photoList.size(); i++) {
                        String objectId = photoList.get(i).getObjectId();
                        String name = photoList.get(i).get("title").toString();
                        ParseFile image = photoList.get(i).getParseFile("photo");
                        String imageUrl = "";
                        if (image != null) {
                            imageUrl = image.getUrl();
                        }
                        ConstantObjects constantOb = new ConstantObjects(objectId, name, imageUrl);
                        Utils.Gallery.add(constantOb);
                        adaptorGallery = new AdaptorGallery(context, Utils.Gallery);
                        gridView.setAdapter(adaptorGallery);
                    }
                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });
    }
    public void PhotoGallery(){
        final ParseQuery<ParseObject> query = ParseQuery.getQuery("Gallery");
        query.whereEqualTo("hotel_id", hotelId);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> gridViewGallery, ParseException e) {
                if (e == null) {
                    for (int i = 0; i < gridViewGallery.size(); i++) {
                        String objectId = gridViewGallery.get(i).getObjectId();
                        String title = gridViewGallery.get(i).get("title").toString();
                        ParseFile image = gridViewGallery.get(i).getParseFile("photo");
                        String imageUrl = "";
                        if (image != null) {
                            imageUrl = image.getUrl();
                        }
                        ConstantObjects constantOb = new ConstantObjects(objectId, title, imageUrl);
                        Utils.Gallery.add(constantOb);
                        adaptorGallery = new AdaptorGallery(context, Utils.Gallery);
                        gridView.setAdapter(adaptorGallery);
                    }
                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });
    }
    private void InitializeView(){
        addNew = (ImageButton)view.findViewById(R.id.idAddNewPhoto);
        refresh = (ImageButton)view.findViewById(R.id.idRefreshPhotoList);
        gridView = (GridView)view.findViewById(R.id.gridViewAdmin);
    }


}
