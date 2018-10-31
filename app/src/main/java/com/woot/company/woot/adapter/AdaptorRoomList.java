package com.woot.company.woot.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import com.woot.company.woot.detail.RoomDetail;
import com.woot.company.woot.R;
import com.woot.company.woot.universal.ConstantObjects;
import com.woot.company.woot.universal.ImageLoader;

public class AdaptorRoomList extends BaseAdapter {

    ArrayList<ConstantObjects> myList = new ArrayList<ConstantObjects>();
    LayoutInflater inflater;
    Context context;
    ImageLoader imageLoader;


    public AdaptorRoomList(Context context, ArrayList<ConstantObjects> myList) {
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
            convertView = inflater.inflate(R.layout.adapter_room_list, parent, false);
            mViewHolder = new MyViewHolder();
            mViewHolder.roomName = (TextView)convertView.findViewById(R.id.idTvTitleRoom);
            mViewHolder.roomDetail = (TextView)convertView.findViewById(R.id.textView);
            mViewHolder.roomPrice = (TextView)convertView.findViewById(R.id.idTvPriceRm);
            mViewHolder.roomImage =(ImageView)convertView.findViewById(R.id.imageView4);
            mViewHolder.relativeLayout = (RelativeLayout)convertView.findViewById(R.id.relativeLayout);

            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (MyViewHolder) convertView.getTag();
        }
        final String roomName = myList.get(position).getRoomName();
        final String roomDetail = myList.get(position).getRoomDetail();
        final String roomPrice = myList.get(position).getRoomPrice();
        final String roomImage =myList.get(position).getRoomImage();


        mViewHolder.roomName.setText(roomName);
        mViewHolder.roomPrice.setText(roomPrice);
        mViewHolder.roomDetail.setText(roomDetail);
        imageLoader.DisplayImage(roomImage,mViewHolder.roomImage);

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
        TextView roomName, roomDetail, roomPrice;
        ImageView roomImage;
        RelativeLayout relativeLayout;
    }
}