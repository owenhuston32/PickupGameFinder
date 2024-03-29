package com.example.pickupgamefinder.Singletons;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class InternetManager {

    private Context context;
    private static volatile InternetManager INSTANCE = null;
    private InternetManager(){};

    public static InternetManager getInstance() {
        if(INSTANCE == null) {
            synchronized (InternetManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new InternetManager();
                }
            }
        }
        return INSTANCE;
    }

    public void setContext(Context context)
    {
        this.context = context.getApplicationContext();
    }


    public boolean checkWifi() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
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
