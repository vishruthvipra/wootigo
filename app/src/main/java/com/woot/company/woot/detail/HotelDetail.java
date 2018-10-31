package com.woot.company.woot.detail;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.woot.company.woot.adapter.AdaptorCity;
import com.woot.company.woot.adapter.AdaptorFeedBack;
import com.woot.company.woot.City;
import com.woot.company.woot.R;
import com.woot.company.woot.universal.AppController;
import com.woot.company.woot.universal.ConstantObjects;
import com.woot.company.woot.universal.ImageLoader;
import com.woot.company.woot.universal.Utils;

public class HotelDetail extends Activity implements View.OnClickListener {
    TextView hotelName, address, price, description;

    ListView lvFeedBack;
    RatingBar ratingBar, ratingHotel;
    Button btnBook, btnSubmit;
    EditText comments;
    ImageView  call, email, website;
    NetworkImageView image;

    Context context;
    String HotelTitle, Address, Price, Detail, Image, rating, Phone, Email, Website,icon;
    String hotelID;
    Utils utils = new Utils();
    AdaptorFeedBack adaptor;
    ScrollView scrollView, scrollView2;
    Float ratF, a;
    String user_id,userName,HotelID,cmt;
    private com.android.volley.toolbox.ImageLoader imageLoader;
    String feedback_user_id,feedback_hotel_id,feedback_rating,feedback,feedback_user_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hotel_detail);
        context = getApplicationContext();
        ExtrasCollection();
        utils.Comments.clear();

         user_id = Utils.getPreferences("user_id", context);
         userName = Utils.getPreferences("user_name1", context);
//        Feedback();
        InitializeView();
        call.setOnClickListener(this);
        email.setOnClickListener(this);
        website.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        btnBook.setOnClickListener(this);
        getbackVolley();

        lvFeedBack.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                scrollView.requestDisallowInterceptTouchEvent(true);

                int action = event.getActionMasked();

                switch (action) {
                    case MotionEvent.ACTION_UP:
                        scrollView.requestDisallowInterceptTouchEvent(true);
                        break;
                    case MotionEvent.ACTION_DOWN:
                        scrollView.requestDisallowInterceptTouchEvent(false);
                        break;
                }
                v.onTouchEvent(event);

                return true;
            }
        });

        scrollView.setOnTouchListener(new View.OnTouchListener()
        {
            public boolean onTouch(View p_v, MotionEvent p_event)
            {
                scrollView2.getParent().requestDisallowInterceptTouchEvent(false);
                //  We will have to follow above for all scrollable contents
                return false;
            }
        });
        scrollView2.setOnTouchListener(new View.OnTouchListener()
        {
            public boolean onTouch(View p_v, MotionEvent p_event)
            {
                // this will disallow the touch request for parent scroll on touch of child view
                p_v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

//        scrollView2.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//
//                scrollView.requestDisallowInterceptTouchEvent(true);
//                int action = event.getActionMasked();
//
//                switch (action) {
//                    case MotionEvent.ACTION_UP:
//                        scrollView.requestDisallowInterceptTouchEvent(false);
//                        break;
//                    case MotionEvent.ACTION_DOWN:
//                        scrollView.requestDisallowInterceptTouchEvent(true);
//                        break;
//                }
//                v.onTouchEvent(event);
//                return true;
//            }
//        });

        scrollView.fullScroll(View.FOCUS_UP);
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
//                Feedback();
//                getbackVolley();
                lvFeedBack.bringToFront();
                return false;
            }
        });

//        imageLoader = new ImageLoader(context);

//        btnBook.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context, BookRoom.class);
//                intent.putExtra("rName", HotelTitle);
//                intent.putExtra("rPrice", Price);
//
//                startActivity(intent);
//            }
//        });

        HotelTitle = Utils.getPreferences("name", context);
        Address = Utils.getPreferences("address", context);
        Price = Utils.getPreferences("pri", context);
        Detail = Utils.getPreferences("des", context);
        Image = Utils.getPreferences("back", context);
        rating = Utils.getPreferences("rat", context);
        Phone = Utils.getPreferences("ph", context);
        Email = Utils.getPreferences("email", context);
        Website = Utils.getPreferences("web", context);
        icon = Utils.getPreferences("icon", context);


//        float r = Float.valueOf(rating);
//        ratingHotel.setRating(r);
//        ratingHotel.bringToFront();

        hotelName.setText(HotelTitle);
        price.setText("$"+Price+"/");
        description.setText(Detail);
        address.setText(Address);
        imageLoader = AppController.getInstance().getImageLoader();


//            worker_name.setText(emp_name + "ph:"+emp_phone);

//        mViewHolder.cityImage.setImageUrl(emp_imgUrl, imageLoader);
        String emp_imgUrl = Utils.Image_URL + Image;
//        String emp_imgUrl_icon = Utils.Image_URL + hotelIcon;
//            worker_name.setText(emp_name + "ph:"+emp_phone);

        image.setImageUrl(emp_imgUrl, imageLoader);
//        mViewHolder.iconHotel.setImageUrl(emp_imgUrl_icon, imageLoader);


//        LayerDrawable stars = (LayerDrawable) ratingHotel.getProgressDrawable();
//        stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
//        stars.getDrawable(1).setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
//        stars.getDrawable(0).setColorFilter(Color.rgb(50, 50, 50), PorterDuff.Mode.SRC_ATOP);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        getbackVolley();
    }

    public void Feedback(){

        hotelID = Utils.getPreferences("h_ID", context);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Comments");
        query.whereEqualTo("hotel_id", hotelID);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> categoryList, ParseException e) {
                if (e == null) {
                    for (int i=0;i<categoryList.size();i++){
                        String objId=categoryList.get(i).getObjectId();
                        String name=categoryList.get(i).get("name").toString();
                        String commnt = categoryList.get(i).get("feedbacks").toString();
                        String rat = categoryList.get(i).get("rating").toString();

                        ConstantObjects cOb=new ConstantObjects(name, commnt, rat);
                        utils.Comments.add(cOb);
                        adaptor=new AdaptorFeedBack(context, utils.Comments);
                        lvFeedBack.setAdapter(adaptor);

                    }
                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });
//        if (!utils.Comments.isEmpty()){
//            lvFeedBack.setMinimumHeight(200);
//        }
    }
    private void BookRoom(){
        Intent intent = new Intent(context, BookRoom.class);
        intent.putExtra("rName", HotelTitle);
        intent.putExtra("rPrice", Price);

        startActivity(intent);
    }
    private void SubmitComments(){
        ExtrasCollection();
      cmt = comments.getText().toString();


        a = ratingBar.getRating();
        String r = String.valueOf(a);

//        LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
//        stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
//        stars.getDrawable(1).setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
//        stars.getDrawable(0).setColorFilter(Color.rgb(50, 50, 50), PorterDuff.Mode.SRC_ATOP);


        if (!r.isEmpty() && !cmt.isEmpty()){
//            ParseQuery<ParseObject> query = ParseQuery.getQuery("Hotel");
//            query.whereEqualTo("objectId", hotelID);
//            query.findInBackground(new FindCallback<ParseObject>() {
//                public void done(List<ParseObject> categoryList, ParseException e) {
//                    if (e == null) {
//                        for (int i = 0; i < categoryList.size(); i++) {
//                            String rat = categoryList.get(i).get("rating").toString();
//                            float rInt = Float.valueOf(rat);
//                            ratF = (rInt + a) / 5;
//
//                        }
//                    } else {
//                        Log.d("score", "Error: " + e.getMessage());
//                    }
//                }
//            });



           feedbackVolley();

            Toast.makeText(context, "Thanks for your feedback", Toast.LENGTH_SHORT).show();
            comments.setText("");
            ratingBar.setRating(0);



        }else {
            Toast.makeText(context, "Please give comments and rating", Toast.LENGTH_SHORT).show();
        }


    }

    private void Call() {
        String phone_no = Phone.replaceAll("-", "");
        Uri uri = Uri.parse("tel:" + Phone);
        Intent callIntent = new Intent(Intent.ACTION_CALL, uri);
//        callIntent.setData(Uri.parse("tel:" + phone_no));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivity(callIntent);
    }
    private void Email(){
        Uri uri = Uri.parse("mailto:" + Email);
        Intent it = new Intent(Intent.ACTION_SENDTO, uri);
        it.putExtra(Intent.EXTRA_SUBJECT, "Booking request for: Room");
        startActivity(it);
    }
    private void Website(){
        Uri uri = Uri.parse("http://"+Website); // missing 'http://' will cause crashed
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    private void ExtrasCollection() {
        Bundle extras = getIntent().getExtras();
         HotelID = extras.getString("objectId");
        Utils.savePreferences("h_ID", HotelID, context);
    }

    public void InitializeView(){
        hotelName = (TextView)findViewById(R.id.idHotelNameDetail);
        price = (TextView)findViewById(R.id.idCurrent);
        address = (TextView)findViewById(R.id.idAddressHotelD);
        description = (TextView)findViewById(R.id.idDescription);
        btnBook = (Button)findViewById(R.id.idBtnBookRoomH);
        image = (NetworkImageView) findViewById(R.id.idBackHotelDetailImage);
        call = (ImageView)findViewById(R.id.idIconCall);
        email = (ImageView)findViewById(R.id.idIconEmail);
        website = (ImageView)findViewById(R.id.idIconWeb);
        lvFeedBack = (ListView) findViewById(R.id.idListFeed);
        btnSubmit = (Button) findViewById(R.id.idBtnSubmitFeed);
        comments = (EditText) findViewById(R.id.idEditFeedEnter);
        ratingBar = (RatingBar)findViewById(R.id.idSubmitRating);
        scrollView = (ScrollView)findViewById(R.id.idScrollFirst);
        scrollView2 = (ScrollView)findViewById(R.id.idScrollSecond);
//        ratingHotel = (RatingBar)findViewById(R.id.idRatingBarDetail);
    }

    @Override
    public void onClick(View v) {
        if (v == call){
            Call();
        }else if (v == email){
            Email();
        }else if (v == website){
            Website();
        }else if (v == btnSubmit){
            SubmitComments();
        }else if (v == btnBook){
            BookRoom();
        }
    }
    private void feedbackVolley() {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String result = response.toString();



                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
                    }
                })


        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
              /*  userName; remainng*/

                params.put("user_id", user_id);
                params.put("hotel_id", HotelID);
                params.put("rating", rating);
                params.put("status", "1");
                params.put("feedback", cmt);
                params.put("post_comment", "true");

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }


    private void getbackVolley() {

        utils.Comments.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String result = response.toString();
                        try {
                            JSONObject object=new JSONObject(result);

                            JSONArray ja=object.getJSONArray("data");
                            for(int i=0;i<ja.length();i++){
                                JSONObject sec_object=ja.getJSONObject(i);
                                feedback_user_name=sec_object.getString("user_name");
                                feedback=sec_object.getString("feedback");
                                feedback_rating=sec_object.getString("rating");

//                                feedback_user_id=sec_object.getString("user_id");
//                                feedback_hotel_id=sec_object.getString("hotel_id");
//


                                ConstantObjects cOb=new ConstantObjects(feedback_user_name, feedback, feedback_rating);
                                utils.Comments.add(cOb);



                            }
                            adaptor=new AdaptorFeedBack(context, utils.Comments);
                            lvFeedBack.setAdapter(adaptor);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
                    }
                })


        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
              /*  userName; remainng*/

                params.put("user_id", user_id);
                params.put("hotel_id", HotelID);

                params.put("get_comment", "true");

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
}
