package com.woot.company.woot.hotelonlineadminside;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.ByteArrayOutputStream;

import com.woot.company.woot.R;
import com.woot.company.woot.universal.ImageLoader;
import com.woot.company.woot.universal.Utils;

public class AddRoom extends Activity {
    EditText roomTitle, roomDescription, roomPrice;
    ImageView roomImage;
    Button save;
    ImageButton btnBack;
    Context context;
    ImageLoader imageLoader;
    String hotelId, cityId;
    String roomN, roomD, roomP, roomI, roomId;
    String title, description, price, picturePath;

    private static int RESULT_LOAD_IMAGE = 1;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            Utils.savePreferences("picturePath", picturePath, context);
            cursor.close();
            roomImage.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_room);
        InitializeView();
        context = getApplicationContext();
        imageLoader = new ImageLoader(context);
        hotelId = Utils.getPreferences("hotel_ID", context);
        cityId = Utils.getPreferences("city_ID", context);

        final String roomID = Utils.getPreferences("idR", context);
        if (!roomID.isEmpty()){
            Bundle extras = getIntent().getExtras();
            roomId = extras.getString("object_id");
            roomN = extras.getString("room_name");
            roomD = extras.getString("room_detail");
            roomP = extras.getString("room_price");
            roomI = extras.getString("room_image");

            roomTitle.setText(roomN);
            roomDescription.setText(roomD);
            roomPrice.setText(roomP);
            imageLoader.DisplayImage(roomI, roomImage);

        }else {
            roomTitle.setText("");
        }
        roomImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddRoom.this.finish();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picturePath = Utils.getPreferences("picturePath", context);
                title = roomTitle.getText().toString();
                final String nameAlready = Utils.getPreferences("roomN", context);
                description = roomDescription.getText().toString();
                price = roomPrice.getText().toString();
                if (title.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Enter Title First", Toast.LENGTH_SHORT).show();
                } else if (!title.equals(nameAlready)) {
                    if (picturePath.isEmpty()) {
                        NewRoomWithoutPic();
                    } else {
                        NewRoomWithPic();
                    }
                } else {
                    if (picturePath.isEmpty()) {
                        UpdateRoomWithoutPic();
                    } else {
                        UpdateRoomWithPic();
                    }
                }
            }
        });
    }
    public void InitializeView(){
        roomTitle = (EditText)findViewById(R.id.idTitleRoomNew);
        roomDescription = (EditText)findViewById(R.id.idDescriptionRoomNew);
        roomPrice = (EditText)findViewById(R.id.idPriceNew);
        roomImage = (ImageView)findViewById(R.id.idImageRoomNew);
        save = (Button)findViewById(R.id.idBtnSaveNew);
        btnBack = (ImageButton)findViewById(R.id.idBackButton);
    }
    public void NewRoomWithoutPic(){
        ParseObject addRoomToList = new ParseObject("Rooms");
        addRoomToList.put("room_title", title);
        addRoomToList.put("hotel_id", hotelId);
        addRoomToList.put("city_id", cityId);
        addRoomToList.put("room_detail", description);
        addRoomToList.put("price", price);
//                    addRoomToList.put("room_image", file);
        addRoomToList.saveInBackground();
        Toast.makeText(getApplicationContext(), "New Room has been added", Toast.LENGTH_SHORT).show();
        AddRoom.this.finish();
    }
    public void NewRoomWithPic(){
        Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] image = stream.toByteArray();
        ParseFile file = new ParseFile(title, image);
        file.saveInBackground();

        ParseObject addRoomToList = new ParseObject("Rooms");
        addRoomToList.put("room_title", title);
        addRoomToList.put("hotel_id", hotelId);
        addRoomToList.put("city_id", cityId);
        addRoomToList.put("room_detail", description);
        addRoomToList.put("price", price);
        addRoomToList.put("room_image", file);
        addRoomToList.saveInBackground();
        Toast.makeText(getApplicationContext(), "New Room has been added", Toast.LENGTH_SHORT).show();
        AddRoom.this.finish();
    }
    public void UpdateRoomWithoutPic(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Rooms");
        query.getInBackground(roomN, new GetCallback<ParseObject>() {
            public void done(ParseObject addRoomToList, ParseException e) {
                if (e == null) {
                    // Now let's update it with some new data. In this case, only cheatMode and score
                    // will get sent to the Parse Cloud. playerName hasn't changed.
                    addRoomToList.put("room_title", title);
                    addRoomToList.put("hotel_id", hotelId);
                    addRoomToList.put("city_id", cityId);
                    addRoomToList.put("room_detail", description);
                    addRoomToList.put("price", price);
                    addRoomToList.saveInBackground();
                    Toast.makeText(getApplicationContext(), "Room detail updated", Toast.LENGTH_SHORT).show();
                    AddRoom.this.finish();
                }
            }
        });
    }
    public void UpdateRoomWithPic(){
        Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] image = stream.toByteArray();
        final ParseFile file = new ParseFile(title, image);
        file.saveInBackground();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Rooms");
        query.getInBackground(roomN, new GetCallback<ParseObject>() {
            public void done(ParseObject addRoomToList, ParseException e) {
                if (e == null) {
                    // Now let's update it with some new data. In this case, only cheatMode and score
                    // will get sent to the Parse Cloud. playerName hasn't changed.
                    addRoomToList.put("room_title", title);
                    addRoomToList.put("hotel_id", hotelId);
                    addRoomToList.put("city_id", cityId);
                    addRoomToList.put("room_detail", description);
                    addRoomToList.put("price", price);
                    addRoomToList.put("room_image", file);
                    addRoomToList.saveInBackground();
                    Toast.makeText(getApplicationContext(), "Room detail updated", Toast.LENGTH_SHORT).show();
                    AddRoom.this.finish();
                }
            }
        });
    }

}
