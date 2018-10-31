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
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.ByteArrayOutputStream;
import java.util.List;

import com.woot.company.woot.R;
import com.woot.company.woot.universal.ImageLoader;
import com.woot.company.woot.universal.Utils;

public class AddHotel extends Activity {
    TextView save, logout;
    TextView uploadBack, uploadLogo;
    EditText backImageName, logoImageName;

    EditText hotelName, description, address, email, number, facebook, twitter, adviser, website, oldPrice, currentPrice;
    ImageView backImage, logoImage;
    View view;
    ParseQuery<ParseObject> query = ParseQuery.getQuery("Hotel");
    ImageLoader imageLoader;
    Context context;
    private static int RESULT_LOAD_IMAGE = 1;
    private static int RESULT_LOAD_IMAGE2 = 2;
    String hotelId, cityId;
    String nuHotels, title, desc, add, num;
    String emai, web, picturePath, picturePath2;
    String oldPr, currentPr;
    int nu_hotels;
    String nu_Hotels;
    Spinner spinner;
    String[] roomType = { "Single bed", "Double bed", };
    int a;
    String nameBack, nameLogo;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == -1 && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = context.getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            Utils.savePreferences("PicturePath", picturePath, context);
            cursor.close();
            backImage.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            nameBack = String.valueOf(picturePath);
            backImageName.setText(nameBack);
        }else if (requestCode == RESULT_LOAD_IMAGE2 && resultCode == -1 && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = context.getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath2 = cursor.getString(columnIndex);
            Utils.savePreferences("picturePath2", picturePath2, context);
            cursor.close();
            logoImage.setImageBitmap(BitmapFactory.decodeFile(picturePath2));
            nameLogo = String.valueOf(picturePath2);
            logoImageName.setText(nameLogo);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_hotel);
        InitializeView();
        context = getApplicationContext();
        imageLoader = new ImageLoader(context);

        cityId = Utils.getPreferences("City_Id", context);
        nuHotels = Utils.getPreferences("nu_hotel", context);
        nu_hotels = Integer.valueOf(nuHotels);

        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item, roomType);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinner.setAdapter(aa);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                a = position+1;
                Toast.makeText(context, ""+a, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        uploadLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE2);
            }
        });
        uploadBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                title = hotelName.getText().toString();
                desc  = description.getText().toString();
                add   = address.getText().toString();
                num   = number.getText().toString();
                emai  = email.getText().toString();
                web   = website.getText().toString();
//                picturePath = Utils.getPreferences("picturePath", context);
//                final String fb = facebook.getText().toString();
//                final String tw = twitter.getText().toString();
//                final String ad = adviser.getText().toString();
                currentPr = currentPrice.getText().toString();
                oldPr = oldPrice.getText().toString();
                nu_Hotels = String.valueOf(nu_hotels+1);

                if (picturePath.isEmpty()){
                    query.findInBackground(new FindCallback<ParseObject>() {
                        public void done(List<ParseObject> list, ParseException e) {
                            if (e == null) {
                                ParseQuery<ParseObject> query = ParseQuery.getQuery("City");
                                query.getInBackground(cityId, new GetCallback<ParseObject>() {
                                    public void done(ParseObject gameScore, ParseException e) {
                                        if (e == null) {
                                            // Now let's update it with some new data. In this case, only cheatMode and score
                                            // will get sent to the Parse Cloud. playerName hasn't changed.
                                            gameScore.put("nu_hotels", nu_Hotels);
                                            Utils.savePreferences("nu_hotel", nu_Hotels, context);
                                            gameScore.saveInBackground();
                                        }
                                    }
                                });
                                ParseObject person = new ParseObject("Hotel");
                                person.put("name", title);
                                person.put("description", desc);
                                person.put("address", add);
                                person.put("phone", num);
                                person.put("email", emai);
                                person.put("website", web);
                                person.put("city_id", cityId);
                                person.put("rating", "0");
                                person.put("price", currentPr);
                                person.put("old_price", oldPr);
                                person.put("bed_type", ""+a);

//                                person.put("facebook", fb);
//                                person.put("twitter", tw);
//                                person.put("adviser", ad);
                                person.saveInBackground();

                                Toast.makeText(context, "New Hotel has been added", Toast.LENGTH_SHORT).show();
                                AddHotel.this.finish();
                            } else {
                                Log.d("score", "Error: " + e.getMessage());
                            }
                        }
                    });
                }else if (!picturePath.isEmpty() && !picturePath2.isEmpty() && !title.isEmpty()){
                    Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] image = stream.toByteArray();
                    final ParseFile file = new ParseFile(title+".jpg", image);
                    file.saveInBackground();

                    Bitmap bitmap2 = BitmapFactory.decodeFile(picturePath2);
                    ByteArrayOutputStream stream2 = new ByteArrayOutputStream();
                    bitmap2.compress(Bitmap.CompressFormat.PNG, 100, stream2);
                    byte[] image2 = stream2.toByteArray();
                    final ParseFile file2 = new ParseFile(title+"_logo.jpg", image2);
                    file2.saveInBackground();

                    query.findInBackground(new FindCallback<ParseObject>() {
                        public void done(List<ParseObject> list, ParseException e) {
                            if (e == null) {
                                ParseQuery<ParseObject> query = ParseQuery.getQuery("City");
                                query.getInBackground(cityId, new GetCallback<ParseObject>() {
                                    public void done(ParseObject gameScore, ParseException e) {
                                        if (e == null) {
                                            // Now let's update it with some new data. In this case, only cheatMode and score
                                            // will get sent to the Parse Cloud. playerName hasn't changed.
                                            gameScore.put("nu_hotels", nu_Hotels);
                                            Utils.savePreferences("nu_hotel", nu_Hotels, context);
                                            gameScore.saveInBackground();
                                        }
                                    }
                                });

                                ParseObject person = new ParseObject("Hotel");
                                person.put("name", title);
                                person.put("description", desc);
                                person.put("address", add);
                                person.put("phone", num);
                                person.put("email", emai);
                                person.put("website", web);
                                person.put("city_id", cityId);
                                person.put("rating", "0");
                                person.put("price", currentPr);
                                person.put("old_price", oldPr);
                                person.put("bed_type", ""+a);
//                                person.put("facebook", fb);
//                                person.put("twitter", tw);
//                                person.put("adviser", ad);
                                person.put("background", file);
                                person.put("icon", file2);
                                person.saveInBackground();

                                Toast.makeText(context, "New Hotel has been added", Toast.LENGTH_SHORT).show();
                                AddHotel.this.finish();
                            } else {
                                Log.d("score", "Error: " + e.getMessage());
                            }
                        }
                    });
                }else{
                    Toast.makeText(context, "Enter name of hotels", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void InitializeView(){
        save = (TextView)findViewById(R.id.idSaveEditHotel);
        hotelName = (EditText)findViewById(R.id.idHotelNameAdminNew);
        description = (EditText)findViewById(R.id.idDescriptionChangeNew);
        address = (EditText)findViewById(R.id.idHotelAddressNew);
        email = (EditText)findViewById(R.id.idEmailHotelNew);
        number = (EditText)findViewById(R.id.idPhoneHOtelNew);
        facebook = (EditText)findViewById(R.id.idFacebookLinkNew);
        twitter = (EditText)findViewById(R.id.idTwitterLinkNew);
        adviser = (EditText)findViewById(R.id.idAdviserLinkNew);
        website = (EditText)findViewById(R.id.idwebsiteAdminNew);
        backImage = (ImageView)findViewById(R.id.idBackImageAdminNew);
        logoImage = (ImageView)findViewById(R.id.idImageLogoAdminNew);
        oldPrice = (EditText)findViewById(R.id.idOldPrice);
        currentPrice = (EditText)findViewById(R.id.idPrice);
        spinner = (Spinner)findViewById(R.id.idSpinnerRoomTypeAdmin);
        uploadBack = (TextView) findViewById(R.id.idBtnUploadH);
        backImageName = (EditText) findViewById(R.id.idEditHomeImageHotel);
        uploadLogo = (TextView) findViewById(R.id.idBtnUploadLogoH);
        logoImageName = (EditText) findViewById(R.id.idEditLogoNameH);
    }
}
