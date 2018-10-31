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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.ByteArrayOutputStream;
import java.util.List;

import com.woot.company.woot.R;
import com.woot.company.woot.universal.ImageLoader;
import com.woot.company.woot.universal.Utils;

public class EditHotel extends Activity {
    TextView save, logout;
    ImageButton back;
    EditText hotelName, description, address, email, number, facebook, twitter, adviser, website;
    ImageView backImage, logoImage;
    View view;
    ParseQuery<ParseObject> query = ParseQuery.getQuery("Hotel");
    ImageLoader imageLoader;
    Context context;
    private static int RESULT_LOAD_IMAGE = 1;
    private static int RESULT_LOAD_IMAGE2 = 2;
    String hotelId;
    String cityId;
    String title;
    String desc;
    String add;
    String num;
    String emai;
    String web;
    String picturePath;
    String picturePath2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_hotel);
        InitializeView();
        context = getApplication();
        imageLoader = new ImageLoader(context);
        //from MainActivityTabAdmin shared preferences
        hotelId= Utils.getPreferences("Hotel_Id", context);
        cityId = Utils.getPreferences("City_Id", context);
        if (!hotelId.isEmpty()){

            Utils.savePreferences("Hotel_Id", "", context);
            String name1 = Utils.getPreferences("name", context);
            String des = Utils.getPreferences("des", context);
            String add = Utils.getPreferences("add", context);
            String email1 = Utils.getPreferences("email", context);
            String phone = Utils.getPreferences("ph", context);
            String web = Utils.getPreferences("web", context);
            String back = Utils.getPreferences("back", context);

            hotelName.setText(name1);
            address.setText(add);
            description.setText(des);
            email.setText(email1);
            number.setText(phone);
            website.setText(web);
            imageLoader.DisplayImage(back, backImage);

        }else {
            hotelName.setText("");
        }
        logoImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE2);
            }
        });
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });

        String name1 = PreferenceManager.getDefaultSharedPreferences(context).getString("name", "");
        String des1 = PreferenceManager.getDefaultSharedPreferences(context).getString("des", "");
        String address1 = PreferenceManager.getDefaultSharedPreferences(context).getString("add", "");
        String email1 = PreferenceManager.getDefaultSharedPreferences(context).getString("email", "");
        String phone1 = PreferenceManager.getDefaultSharedPreferences(context).getString("ph", "");
        String website1 = PreferenceManager.getDefaultSharedPreferences(context).getString("web", "");
        String twitter1 = PreferenceManager.getDefaultSharedPreferences(context).getString("tw", "");
        String facebook1 = PreferenceManager.getDefaultSharedPreferences(context).getString("fb", "");
        String adviser1 = PreferenceManager.getDefaultSharedPreferences(context).getString("ad", "");
        String backUrl = PreferenceManager.getDefaultSharedPreferences(context).getString("backUrl", "");
        String logoUrl = PreferenceManager.getDefaultSharedPreferences(context).getString("logoUrl", "");

//        String name1 = hotelName.getText().toString();
//        if (name1.isEmpty()|| des1.isEmpty() || address1.isEmpty() || email1.isEmpty() ||
//                phone1.isEmpty() || website1.isEmpty() || twitter1.isEmpty() || facebook1.isEmpty()
//                || adviser1.isEmpty() || backUrl.isEmpty() || logoUrl.isEmpty()){
//
////            ShowDetail();
//        }else {
//            hotelName.setText(name1);
//            description.setText(des1);
//            address.setText(address1);
//            email.setText(email1);
//            number.setText(phone1);
//            website.setText(website1);
//            twitter.setText(twitter1);
//            facebook.setText(facebook1);
//            adviser.setText(adviser1);
//            imageLoader.DisplayImage(backUrl, backImage);
//            imageLoader.DisplayImage(logoUrl, logoImage);
//        }


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                title = hotelName.getText().toString();
                desc  = description.getText().toString();
                add   = address.getText().toString();
                num   = number.getText().toString();
                emai  = email.getText().toString();
                web   = website.getText().toString();
                picturePath = Utils.getPreferences("picturePath", context);
//                final String fb = facebook.getText().toString();
//                final String tw = twitter.getText().toString();
//                final String ad = adviser.getText().toString();

                if (picturePath.isEmpty()){
                    query.findInBackground(new FindCallback<ParseObject>() {
                        public void done(List<ParseObject> list, ParseException e) {
                            if (e == null) {
                                ParseObject person = new ParseObject("Hotel");
                                person.put("name", title);
                                person.put("description", desc);
                                person.put("address", add);
                                person.put("phone", num);
                                person.put("email", emai);
                                person.put("website", web);
                                person.put("city_id", cityId);
//                                person.put("facebook", fb);
//                                person.put("twitter", tw);
//                                person.put("adviser", ad);
                                person.saveInBackground();

                                Toast.makeText(context, "New Status has been saved", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.d("score", "Error: " + e.getMessage());
                            }
                        }
                    });
                }else {
                    Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] image = stream.toByteArray();
                    final ParseFile file = new ParseFile(title, image);
                    file.saveInBackground();

//                    Bitmap bitmap2 = BitmapFactory.decodeFile(picturePath2);
//                    ByteArrayOutputStream stream2 = new ByteArrayOutputStream();
//                    bitmap2.compress(Bitmap.CompressFormat.PNG, 100, stream2);
//                    byte[] image2 = stream2.toByteArray();
//                    final ParseFile file2 = new ParseFile(title, image2);
//                    file2.saveInBackground();

                    query.findInBackground(new FindCallback<ParseObject>() {
                        public void done(List<ParseObject> list, ParseException e) {
                            if (e == null) {
                                ParseObject person = new ParseObject("Hotel");
                                person.put("name", title);
                                person.put("description", desc);
                                person.put("address", add);
                                person.put("phone", num);
                                person.put("email", emai);
                                person.put("website", web);
                                person.put("city_id", cityId);
//                                person.put("facebook", fb);
//                                person.put("twitter", tw);
//                                person.put("adviser", ad);
                                person.put("background", file);
//                                person.put("logo", file2);
                                person.saveInBackground();

                                Toast.makeText(context, "New Status has been saved", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.d("score", "Error: " + e.getMessage());
                            }
                        }
                    });
                }
            }
        });
    }

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
            Utils.savePreferences("picturePath", picturePath, context);
            cursor.close();
            backImage.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        }else if (requestCode == RESULT_LOAD_IMAGE2 && resultCode == -1 && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = context.getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath2 = cursor.getString(columnIndex);
            PreferenceManager.getDefaultSharedPreferences(context).edit().putString("picturePath2", picturePath2).commit();
            cursor.close();
            logoImage.setImageBitmap(BitmapFactory.decodeFile(picturePath2));
        }
    }
    public void InitializeView(){
        save = (TextView)findViewById(R.id.idSaveEdit);
//        logout = (TextView)findViewById(R.id.idLogout);
        hotelName = (EditText)findViewById(R.id.idHotelNameAdmin);
        description = (EditText)findViewById(R.id.idDescriptionChange);
        address = (EditText)findViewById(R.id.idHotelAddress);
        email = (EditText)findViewById(R.id.idEmailHotel);
        number = (EditText)findViewById(R.id.idPhoneHOtel);
        facebook = (EditText)findViewById(R.id.idFacebookLink);
        twitter = (EditText)findViewById(R.id.idTwitterLink);
        adviser = (EditText)findViewById(R.id.idAdviserLink);
        back = (ImageButton)findViewById(R.id.idbackEditor);
        website = (EditText)findViewById(R.id.idwebsiteAdmin);
        backImage = (ImageView)findViewById(R.id.idBackImageAdmin);
        logoImage = (ImageView)findViewById(R.id.idImageLogoAdmin);
    }

}
