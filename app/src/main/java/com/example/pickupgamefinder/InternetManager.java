package com.example.pickupgamefinder;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.example.pickupgamefinder.Singletons.ErrorUIHandler;

public class InternetManager {

    private Context mContext;

    public InternetManager(Context context)
    {
        mContext = context;
    }

    public boolean checkWifi() {
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();

        boolean success = ni != null && (ni.getType() == ConnectivityManager.TYPE_WIFI
                || ni.getType() == ConnectivityManager.TYPE_ETHERNET
                || ni.getType() == ConnectivityManager.TYPE_MOBILE
                || ni.getType() == ConnectivityManager.TYPE_VPN
                || ni.getType() == ConnectivityManager.TYPE_BLUETOOTH);

        if(!success)
            ErrorUIHandler.getInstance().showError("Unable To Connect To The Internet");


        // return whether we are connected to wifi or not
        return success;

    }

}
