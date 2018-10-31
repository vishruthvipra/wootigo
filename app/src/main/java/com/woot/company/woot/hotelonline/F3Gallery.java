package com.woot.company.woot.hotelonline;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.woot.company.woot.R;

public class F3Gallery extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
// TODO Auto-generated method stub
        View v =
                LayoutInflater.from(getActivity()).inflate(R.layout.tab3_view, null);
        return v;
    }
}