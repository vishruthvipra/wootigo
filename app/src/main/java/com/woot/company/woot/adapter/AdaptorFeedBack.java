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

import java.util.ArrayList;

import com.woot.company.woot.R;
import com.woot.company.woot.universal.ConstantObjects;
import com.woot.company.woot.universal.ImageLoader;

public class AdaptorFeedBack extends BaseAdapter {

    ArrayList<ConstantObjects> myList = new ArrayList<ConstantObjects>();
    LayoutInflater inflater;
    Context context;
    ImageLoader imageLoader;


    public AdaptorFeedBack(Context context, ArrayList<ConstantObjects> myList) {
        this.myList = myList;
        this.context = context;
        inflater = LayoutInflater.from(this.context);
        imageLoader=new ImageLoader(context);
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
            convertView = inflater.inflate(R.layout.adapter_feedback, parent, false);
            mViewHolder = new MyViewHolder();

            mViewHolder.nameF = (TextView)convertView.findViewById(R.id.idTvBy);
//            mViewHolder.cityImage =(ImageView)convertView.findViewById(R.id.idCityImage);
//            mViewHolder.relativeLayoutCity = (RelativeLayout)convertView.findViewById(R.id.Relative_layout_city);
            mViewHolder.commentF = (TextView)convertView.findViewById(R.id.idTvComments);
            mViewHolder.ratingBar = (RatingBar) convertView.findViewById(R.id.ratingBar);

            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (MyViewHolder) convertView.getTag();
        }
        final String name = myList.get(position).getHotelId();
        final String comments = myList.get(position).getHotelName();
        final String rat = myList.get(position).getHotelImage();
        float a = Float.valueOf(rat);

        mViewHolder.nameF.setText(name);
        mViewHolder.commentF.setText(comments);
        mViewHolder.ratingBar.setRating(a);

        LayerDrawable stars = (LayerDrawable) mViewHolder.ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(1).setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(0).setColorFilter(Color.rgb(50, 50, 50), PorterDuff.Mode.SRC_ATOP);

//        imageLoader.DisplayImage(cityImage,mViewHolder.cityImage);
//        mViewHolder.nuHotels.setText(nu_hotels);

        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    private class MyViewHolder {
        TextView nameF, commentF;
        RatingBar ratingBar;
//        ImageView cityImage;
    }
}