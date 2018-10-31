package com.woot.company.woot.hotelonline;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.woot.company.woot.R;

public class F4FeedBack extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
// TODO Auto-generated method stub
        View v =
                LayoutInflater.from(getActivity()).inflate(R.layout.tab4_view, null);
        return v;
    }
}
