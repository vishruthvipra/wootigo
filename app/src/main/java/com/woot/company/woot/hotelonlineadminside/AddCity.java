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

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.ByteArrayOutputStream;

import com.woot.company.woot.R;
import com.woot.company.woot.universal.ImageLoader;
import com.woot.company.woot.universal.Utils;

public class AddCity extends Activity {
    EditText cityName, imageName;
    ImageView cityImage;
    Button save, upload;
    ImageButton btnBack;
    ImageLoader imageLoader;
    String IdC;
    Context context;
    String title;
    String nameAlready;
    String picturePath;


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
            final String picturePath = cursor.getString(columnIndex);
            Utils.savePreferences("pic", picturePath, context);
            cursor.close();
            cityImage.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            String resName = String.valueOf(picturePath);
            imageName.setText(resName);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_city);
        InitializeView();
        context = getApplicationContext();
        imageLoader = new ImageLoader(context);

        IdC= Utils.getPreferences("IdC", context);

        if (!IdC.isEmpty()){
            Utils.savePreferences("IdC", "", context);
            String name1 = Utils.getPreferences("nameC", context);
            String back = Utils.getPreferences("imageC", context);



            imageLoader.DisplayImage(back, cityImage);

            cityName.setText(name1);

            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    title = cityName.getText().toString();
                    picturePath = Utils.getPreferences("pic", context);

                    if (picturePath.isEmpty()){
                        ParseQuery<ParseObject> query = ParseQuery.getQuery("City");
                        query.getInBackground(IdC, new GetCallback<ParseObject>() {
                            public void done(ParseObject gameScore, ParseException e) {
                                if (e == null) {
                                    // Now let's update it with some new data. In this case, only cheatMode and score
                                    // will get sent to the Parse Cloud. playerName hasn't changed.
                                    gameScore.put("city", title);
                                    gameScore.saveInBackground();
                                    AddCity.this.finish();
                                }
                            }
                        });
                        Toast.makeText(getApplicationContext(), "city has been updated", Toast.LENGTH_SHORT).show();
                    }else if (!picturePath.isEmpty()&&!title.isEmpty()){
                        Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        byte[] image = stream.toByteArray();
                        final ParseFile file = new ParseFile(title, image);
                        file.saveInBackground();
                        Toast.makeText(context, "city has been updated", Toast.LENGTH_SHORT).show();

                        ParseQuery<ParseObject> query = ParseQuery.getQuery("City");
                        query.getInBackground(IdC, new GetCallback<ParseObject>() {
                            public void done(ParseObject gameScore, ParseException e) {
                                if (e == null) {
                                    // Now let's update it with some new data. In this case, only cheatMode and score
                                    // will get sent to the Parse Cloud. playerName hasn't changed.
                                    gameScore.put("city", title);
                                    gameScore.put("city_image", file);
                                    gameScore.saveInBackground();
                                    AddCity.this.finish();
                                }
                            }
                        });
                    }else {
                        Toast.makeText(context, "Enter Name of City", Toast.LENGTH_SHORT).show();
                    }
                }
            });


        }else {
            cityName.setText("");

            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    title = cityName.getText().toString();
                    picturePath = Utils.getPreferences("pic", context);
//                    String resName = getResources().getResourceEntryName(R.id.idImageCityNew);
//                    imageName.setText(resName);

                    if (picturePath.isEmpty()){
                        ParseObject addCityToList = new ParseObject("City");
                        addCityToList.put("city", title);
                        addCityToList.put("nu_hotels", "0");
                        addCityToList.saveInBackground();
                        AddCity.this.finish();
                    }else if (!picturePath.isEmpty()&&!title.isEmpty()){
                        Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        byte[] image = stream.toByteArray();
                        ParseFile file = new ParseFile(title, image);
                        file.saveInBackground();
                        Toast.makeText(context, "New city has been added", Toast.LENGTH_SHORT).show();

                        ParseObject addCityToList = new ParseObject("City");
                        addCityToList.put("city", title);
                        addCityToList.put("city_image", file);
                        addCityToList.put("nu_hotels", "0");
                        addCityToList.saveInBackground();
                        AddCity.this.finish();
                    }else {
                        Toast.makeText(context, "Enter Name of City", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }


        upload.setOnClickListener(new View.OnClickListener() {
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
                AddCity.this.finish();
            }
        });
//        save.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                title = cityName.getText().toString();
//                nameAlready = Utils.getPreferences("nameC", context);
//                picturePath = Utils.getPreferences("pic", getApplicationContext());
//                if (title.isEmpty()){
//                    Toast.makeText(getApplicationContext(), "Enter City Name First", Toast.LENGTH_SHORT).show();
//                }else if (title.equals(nameAlready)){
//                    if (picturePath.isEmpty()){
//                        ParseQuery<ParseObject> query = ParseQuery.getQuery("City");
//                        query.getInBackground(IdC, new GetCallback<ParseObject>() {
//                            public void done(ParseObject gameScore, ParseException e) {
//                                if (e == null) {
//                                    // Now let's update it with some new data. In this case, only cheatMode and score
//                                    // will get sent to the Parse Cloud. playerName hasn't changed.
//                                    gameScore.put("city", title);
//                                    gameScore.saveInBackground();
//                                    AddCity.this.finish();
//                                }
//                            }
//                        });
//                        Toast.makeText(getApplicationContext(), "city has been updated", Toast.LENGTH_SHORT).show();
//                    }else {
//                        Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
//                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
//                        byte[] image = stream.toByteArray();
//                        final ParseFile file = new ParseFile(title, image);
//                        file.saveInBackground();
//                        Toast.makeText(getApplicationContext(), "city has been updated", Toast.LENGTH_SHORT).show();
//
//                        ParseQuery<ParseObject> query = ParseQuery.getQuery("City");
//                        query.getInBackground(IdC, new GetCallback<ParseObject>() {
//                            public void done(ParseObject gameScore, ParseException e) {
//                                if (e == null) {
//                                    // Now let's update it with some new data. In this case, only cheatMode and score
//                                    // will get sent to the Parse Cloud. playerName hasn't changed.
//                                    gameScore.put("city", title);
//                                    gameScore.put("city_image", file);
//                                    gameScore.saveInBackground();
//                                    AddCity.this.finish();
//                                }
//                            }
//                        });
//                    }
//                }else {
//                    if (picturePath.isEmpty()){
//                        ParseObject addCityToList = new ParseObject("City");
//                        addCityToList.put("city", title);
//                        addCityToList.saveInBackground();
//                        AddCity.this.finish();
//                    }else {
//                        Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
//                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
//                        byte[] image = stream.toByteArray();
//                        ParseFile file = new ParseFile(title, image);
//                        file.saveInBackground();
//                        Toast.makeText(getApplicationContext(), "New city has been added", Toast.LENGTH_SHORT).show();
//
//                        ParseObject addCityToList = new ParseObject("City");
//                        addCityToList.put("city", title);
//                        addCityToList.put("city_image", file);
//                        addCityToList.saveInBackground();
//                        AddCity.this.finish();
//                    }
//                }
//            }
//        });
    }
    public void InitializeView(){
        cityName = (EditText)findViewById(R.id.idTitleCityNew);
        cityImage = (ImageView)findViewById(R.id.idImageCityNew);
        save = (Button)findViewById(R.id.idBtnSaveNewCity);
        btnBack = (ImageButton)findViewById(R.id.idBackButtonCityDetail);
        imageName = (EditText) findViewById(R.id.idEditImageName);
        upload = (Button) findViewById(R.id.idBtnUpload);
    }
}
