package com.woot.company.woot.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;


import com.woot.company.woot.R;
import com.woot.company.woot.universal.AppController;
import com.woot.company.woot.universal.ConstantObjects;
import com.woot.company.woot.universal.ImageLoader;
import com.woot.company.woot.universal.Utils;

public class AdaptorCity extends BaseAdapter {
    private com.android.volley.toolbox.ImageLoader imageLoader;
    ArrayList<ConstantObjects> myList = new ArrayList<ConstantObjects>();
    LayoutInflater inflater;
    Context context;
//    ImageLoader imageLoader;


    public AdaptorCity(Context context, ArrayList<ConstantObjects> myList) {
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
            convertView = inflater.inflate(R.layout.adapter_city, parent, false);
            mViewHolder = new MyViewHolder();
            mViewHolder.cityName = (TextView)convertView.findViewById(R.id.idTvCity);
            mViewHolder.cityImage =(NetworkImageView)convertView.findViewById(R.id.idCityImage);
            mViewHolder.relativeLayoutCity = (RelativeLayout)convertView.findViewById(R.id.Relative_layout_city);
            mViewHolder.nuHotels = (TextView)convertView.findViewById(R.id.idTvTotalHotel);

            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (MyViewHolder) convertView.getTag();
        }
        final String cityName = myList.get(position).getCity_name();
        final String cityImage =myList.get(position).getCity_image();
        final String nu_hotels = myList.get(position).getNumber_of_hotels();

        mViewHolder.cityName.setText(cityName);
        imageLoader = AppController.getInstance().getImageLoader();


        String emp_imgUrl = Utils.Image_URL + cityImage;
//            worker_name.setText(emp_name + "ph:"+emp_phone);

        mViewHolder.cityImage.setImageUrl(emp_imgUrl, imageLoader);

//        imageLoader.DisplayImage(cityImage,mViewHolder.cityImage);
        mViewHolder.nuHotels.setText(nu_hotels);

        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    private class MyViewHolder {
        TextView cityName, nuHotels;
        NetworkImageView cityImage;
        RelativeLayout relativeLayoutCity;
    }
}