package com.woot.company.woot.hotelonlineadminside;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class F1HomeAdmin extends Fragment {
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.edit_hotel, container, false);
        InitializeView();
        context = getActivity();
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

                                Toast.makeText(getActivity(), "New Status has been saved", Toast.LENGTH_SHORT).show();
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

                                Toast.makeText(getActivity(), "New Status has been saved", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.d("score", "Error: " + e.getMessage());
                            }
                        }
                    });
                }
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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

//    public void ShowDetail(){
//        query.findInBackground(new FindCallback<ParseObject>() {
//            public void done(List<ParseObject> list, ParseException e) {
//                if (e == null) {
//                    if (list.size() == 0) {
//                        Toast.makeText(getActivity(), "No data found in Parse", Toast.LENGTH_SHORT).show();
//                        NewEntry();
//                    } else {
//                        String Name = list.get(0).get("hotel_name").toString();
//                        String Description = list.get(0).get("description").toString();
//                        String Address = list.get(0).get("address").toString();
//                        String Email = list.get(0).get("email").toString();
//                        String Website = list.get(0).get("website").toString();
//                        String Phone = list.get(0).get("phone").toString();
//                        String Facebook = list.get(0).get("facebook").toString();
//                        String Twitter = list.get(0).get("twitter").toString();
//                        String Adviser = list.get(0).get("adviser").toString();
//
//                        ParseFile image = list.get(0).getParseFile("back_image");
//                        String imageUrl = "";
//                        if (image != null) {
//                            imageUrl = image.getUrl();
//                        }
//                        String imageUrlOfline = imageUrl;
//                        imageLoader.DisplayImage(imageUrlOfline, backImage);
//
//                        ParseFile image2 = list.get(0).getParseFile("logo");
//                        String imageUrl2 = "";
//                        if (image2 != null) {
//                            imageUrl2 = image2.getUrl();
//                        }
//                        String imageUrlLogo = imageUrl2;
//                        imageLoader.DisplayImage(imageUrlLogo, logoImage);
//
//                        PreferenceManager.getDefaultSharedPreferences(context).edit().putString("name", Name).commit();
//                        PreferenceManager.getDefaultSharedPreferences(context).edit().putString("des", Description).commit();
//                        PreferenceManager.getDefaultSharedPreferences(context).edit().putString("add", Address).commit();
//                        PreferenceManager.getDefaultSharedPreferences(context).edit().putString("email", Email).commit();
//                        PreferenceManager.getDefaultSharedPreferences(context).edit().putString("web", Website).commit();
//                        PreferenceManager.getDefaultSharedPreferences(context).edit().putString("ph", Phone).commit();
//                        PreferenceManager.getDefaultSharedPreferences(context).edit().putString("fb", Facebook).commit();
//                        PreferenceManager.getDefaultSharedPreferences(context).edit().putString("tw", Twitter).commit();
//                        PreferenceManager.getDefaultSharedPreferences(context).edit().putString("ad", Adviser).commit();
//                        PreferenceManager.getDefaultSharedPreferences(context).edit().putString("backUrl", imageUrlOfline).commit();
//                        PreferenceManager.getDefaultSharedPreferences(context).edit().putString("logoUrl", imageUrlLogo).commit();
//
////                    String BackImage = list.get(0).get("").toString();
////                    String Logo = list.get(0).get("").toString();
//                        hotelName.setText(Name);
//                        description.setText(Description);
//                        address.setText(Address);
//                        email.setText(Email);
//                        website.setText(Website);
//                        number.setText(Phone);
//                        facebook.setText(Facebook);
//                        twitter.setText(Twitter);
//                        adviser.setText(Adviser);
//                    }
//
//                } else {
//                    Log.d("score", "Error: " + e.getMessage());
//                }
//            }
//        });
//    }
//    public void NewEntry(){
//        ParseObject newEntry = new ParseObject("Hotel_detail");
//        newEntry.put("hotel_name", "Name");
//        newEntry.put("description", "Des");
//        newEntry.put("address", "Add");
//        newEntry.put("email", "Email");
//        newEntry.put("website", "Web");
//        newEntry.put("phone", "Phone");
//        newEntry.put("facebook", "Facebook");
//        newEntry.put("twitter", "Twitter");
//        newEntry.put("adviser", "Adviser");
//        newEntry.saveInBackground();
//    }
    public void InitializeView(){
        save = (TextView)view.findViewById(R.id.idSaveEdit);
//        logout = (TextView)view.findViewById(R.id.idLogout);
        hotelName = (EditText)view.findViewById(R.id.idHotelNameAdmin);
        description = (EditText)view.findViewById(R.id.idDescriptionChange);
        address = (EditText)view.findViewById(R.id.idHotelAddress);
        email = (EditText)view.findViewById(R.id.idEmailHotel);
        number = (EditText)view.findViewById(R.id.idPhoneHOtel);
        facebook = (EditText)view.findViewById(R.id.idFacebookLink);
        twitter = (EditText)view.findViewById(R.id.idTwitterLink);
        adviser = (EditText)view.findViewById(R.id.idAdviserLink);
        back = (ImageButton)view.findViewById(R.id.idbackEditor);
        website = (EditText)view.findViewById(R.id.idwebsiteAdmin);
        backImage = (ImageView)view.findViewById(R.id.idBackImageAdmin);
        logoImage = (ImageView)view.findViewById(R.id.idImageLogoAdmin);
    }
}
