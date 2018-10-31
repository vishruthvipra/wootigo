package com.woot.company.woot.hotelonlineadminside;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.parse.ParseFile;
import com.parse.ParseObject;

import java.io.ByteArrayOutputStream;

import com.woot.company.woot.R;
import com.woot.company.woot.universal.Utils;

public class AddPhoto extends Activity {
    EditText title;
    ImageView image;
    Button save;
    ImageButton btnBack;
    Context context;
    String hotelId, cityId;

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
            PreferenceManager.getDefaultSharedPreferences(this).edit().putString("newPhoto", picturePath).commit();
            cursor.close();
            image.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_photo);
        InitializeView();
        context = getApplicationContext();
        hotelId = Utils.getPreferences("Hotel_Id", context);
        cityId = Utils.getPreferences("City_Id", context);


        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddPhoto.this.finish();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String picturePath = PreferenceManager.getDefaultSharedPreferences(context).getString("newPhoto", "");
                String title1 = title.getText().toString();
                if (title1.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Enter Title First", Toast.LENGTH_SHORT).show();
                }else if (picturePath.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Select image from gallery", Toast.LENGTH_SHORT).show();
                }else {
                    Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] image = stream.toByteArray();
                    ParseFile file = new ParseFile(title1, image);
                    file.saveInBackground();

                    ParseObject addRoomToList = new ParseObject("Gallery");
                    addRoomToList.put("title", title1);
                    addRoomToList.put("hotel_id", hotelId);
                    addRoomToList.put("city_id", cityId);
                    addRoomToList.put("photo", file);
                    addRoomToList.saveInBackground();
                    Toast.makeText(getApplicationContext(), "New Photo has been added", Toast.LENGTH_SHORT).show();
                    AddPhoto.this.finish();
                }
            }
        });


    }
    public void InitializeView(){
        title = (EditText)findViewById(R.id.idTitleNewPhoto);
        image = (ImageView)findViewById(R.id.idNewPhoto);
        save = (Button)findViewById(R.id.idBtnSaveNewPhoto);
        btnBack = (ImageButton)findViewById(R.id.idBackButtonAddPhoto);
    }
}
