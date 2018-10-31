package com.woot.company.woot.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

import com.woot.company.woot.R;
import com.woot.company.woot.universal.AppController;
import com.woot.company.woot.universal.CircularNetworkImageView;
import com.woot.company.woot.universal.ConstantObjects;
import com.woot.company.woot.universal.ImageLoader;
import com.woot.company.woot.universal.Utils;

public class AdaptorHotels extends BaseAdapter {
    private com.android.volley.toolbox.ImageLoader imageLoader;

    ArrayList<ConstantObjects> myList = new ArrayList<ConstantObjects>();
    LayoutInflater inflater;
    Context context;
//    ImageLoader imageLoader;


    public AdaptorHotels(Context context, ArrayList<ConstantObjects> myList) {
        this.myList = myList;
        this.context = context;
        inflater = LayoutInflater.from(this.context);
//        imageLoader=new ImageLoader(context);
    }

    @Override
    public int getCount() {
        return myList.size();
    }

    @Override
    public ConstantObjects getItem(int position) {
        return myList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        MyViewHolder mViewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.adapter_hotels, parent, false);
            mViewHolder = new MyViewHolder();
            mViewHolder.hotelName      = (TextView)convertView.findViewById(R.id.idTvNameHotel);
            mViewHolder.hotelAddress   = (TextView)convertView.findViewById(R.id.idAddressHotel);
            mViewHolder.price          = (TextView)convertView.findViewById(R.id.idCurrent);
            mViewHolder.oldPrice       = (TextView)convertView.findViewById(R.id.idPreviousPrice);
            mViewHolder.hotelImage     = (NetworkImageView) convertView.findViewById(R.id.idBackImage);
            mViewHolder.relativeLayout = (RelativeLayout)convertView.findViewById(R.id.Relative_layout);
            mViewHolder.iconHotel      = (CircularNetworkImageView)convertView.findViewById(R.id.idCircleIconHotel);
//            mViewHolder.ratingBar      = (RatingBar)convertView.findViewById(R.id.idRatingBar);

            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (MyViewHolder) convertView.getTag();
        }
        String hotelName = myList.get(position).getHotel_name();
        String address = myList.get(position).getAddress();
        String price = myList.get(position).getNew_price();
        String priceOld = myList.get(position).getOld_price();
        String hotelImage =myList.get(position).getBackground_image();
        String hotelIcon = myList.get(position).getIcon();
        String rating = myList.get(position).getRating();
//            int d = Integer.valueOf(rating);


//        LayerDrawable stars = (LayerDrawable) mViewHolder.ratingBar.getProgressDrawable();
//        stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
//        stars.getDrawable(1).setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
//        stars.getDrawable(0).setColorFilter(Color.rgb(50,50,50), PorterDuff.Mode.SRC_ATOP);


        mViewHolder.hotelName.setText(hotelName);
        mViewHolder.hotelAddress.setText(address);
        mViewHolder.price.setText("$"+price+"/");
        mViewHolder.oldPrice.setText("$"+priceOld);
        imageLoader = AppController.getInstance().getImageLoader();


        String emp_imgUrl = Utils.Hotel_Back_Image_URL + hotelImage;
        String emp_imgUrl_icon = Utils.Hotel_Image_URL + hotelIcon;
//            worker_name.setText(emp_name + "ph:"+emp_phone);

        mViewHolder.hotelImage.setImageUrl(emp_imgUrl, imageLoader);
        mViewHolder.iconHotel.setImageUrl(emp_imgUrl_icon, imageLoader);


//        mViewHolder.ratingBar.setRating(d);
        mViewHolder.iconHotel.bringToFront();
//        mViewHolder.ratingBar.bringToFront();

//        mViewHolder.relativeLayout.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                    Intent intent = new Intent(context, RoomDetail.class);
//                    intent.putExtra("roomName",   roomName);
//                    intent.putExtra("roomDetail", roomDetail);
//                    intent.putExtra("roomPrice", roomPrice);
//                    intent.putExtra("roomImage", roomImage);
//
//                    context.startActivity(intent);
//                }
//            });
        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    private class MyViewHolder {
        TextView hotelName, hotelAddress, price, oldPrice;
        CircularNetworkImageView iconHotel;
        NetworkImageView hotelImage;
        RatingBar ratingBar;
        RelativeLayout relativeLayout;
    }
}