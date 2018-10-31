package com.woot.company.woot;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.DatePicker;
import android.widget.Toast;

/**
 * Created by Vishruth K on 02-06-2016.
 */
public class DateSettings implements DatePickerDialog.OnDateSetListener {
    Context context;

    public DateSettings(Context context) {
        this.context = context;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Toast.makeText(context, "Selected date:" + dayOfMonth + "/ " + monthOfYear + " /" + year, Toast.LENGTH_LONG).show();
    }
}