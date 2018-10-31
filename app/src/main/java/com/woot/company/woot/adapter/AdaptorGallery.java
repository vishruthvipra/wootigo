package com.woot.company.woot.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import com.woot.company.woot.R;
import com.woot.company.woot.universal.ConstantObjects;
import com.woot.company.woot.universal.ImageLoader;

public class AdaptorGallery extends BaseAdapter {

    ArrayList<ConstantObjects> myList = new ArrayList<ConstantObjects>();
    LayoutInflater inflater;
    Context context;
    ImageLoader imageLoader;


    public AdaptorGallery(Context context, ArrayList<ConstantObjects> myList) {
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
            convertView = inflater.inflate(R.layout.adapter_grid_row_admin, parent, false);
            mViewHolder = new MyViewHolder();
            mViewHolder.imageName = (TextView)convertView.findViewById(R.id.idTextViewNameImage);
            mViewHolder.image =(ImageView)convertView.findViewById(R.id.idImageBack);

            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (MyViewHolder) convertView.getTag();
        }
        final String imageName = myList.get(position).getHotelName();
        final String image =myList.get(position).getHotelImage();

        mViewHolder.imageName.setText(imageName);
        imageLoader.DisplayImage(image,mViewHolder.image);

        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    private class MyViewHolder {
        TextView imageName;
        ImageView image;
    }
}