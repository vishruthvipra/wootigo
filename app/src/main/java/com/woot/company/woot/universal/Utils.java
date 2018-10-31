package com.woot.company.woot.universal;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.util.ArrayList;

public class Utils {
    public static final String REGISTER_URL = "http://techpraja.com/hotel/api.php";
    public static final String Hotel_Back_Image_URL = "http://techpraja.com/hotel/uploads/hotels/back_images/";
    public static final String Hotel_Image_URL = "http://techpraja.com/hotel/uploads/hotels/icons/";
    public static final String Image_URL = "http://techpraja.com/hotel/uploads/city_images/";
    public static final String CITY_URL = REGISTER_URL+"?city=city";
    public static String Banner="ca-app-pub-3940256099942544/6300978111";
    public static String Interstitial="ca-app-pub-3940256099942544/1033173712";

    public static ArrayList<ConstantObjects> Rooms=new ArrayList<ConstantObjects>();
    public static ArrayList<ConstantObjects> Hotels = new ArrayList<ConstantObjects>();
    public static ArrayList<ConstantObjects> City = new ArrayList<ConstantObjects>();
    public static ArrayList<ConstantObjects> HotelDetail = new ArrayList<ConstantObjects>();
    public static ArrayList<ConstantObjects> Gallery = new ArrayList<ConstantObjects>();
    public static ArrayList<ConstantObjects> Comments = new ArrayList<ConstantObjects>();

    public static void CopyStream(InputStream is, OutputStream os)
    {
        final int buffer_size=1024;
        try
        {
            byte[] bytes=new byte[buffer_size];
            for(;;)
            {
                int count=is.read(bytes, 0, buffer_size);
                if(count==-1)
                    break;
                os.write(bytes, 0, count);
            }
        }
        catch(Exception ex){}
    }
    public static String getPreferences(String key, Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String userName = sharedPreferences.getString(key, "");
        return userName;
    }

    public static boolean savePreferences(String key, String value,
                                          Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
        return true;
    }

}
