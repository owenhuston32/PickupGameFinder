package com.example.pickupgamefinder;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

public class InternetManager {

    private Context mContext;

    public InternetManager(Context context)
    {
        mContext = context;
    }

    public boolean checkWifi() {
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();

        // return whether we are connected to wifi or not
        return ni != null && ni.getType() == ConnectivityManager.TYPE_WIFI;

    }

}
