package com.example.pickupgamefinder.Singletons;

import android.content.Context;

import com.example.pickupgamefinder.R;

public class LoadingScreen {

    private Context context;
    private static volatile LoadingScreen INSTANCE = null;
    private LoadingScreen(){};

    public static LoadingScreen getInstance() {
        if(INSTANCE == null) {
            synchronized (LoadingScreen.class) {
                if (INSTANCE == null) {
                    INSTANCE = new LoadingScreen();
                }
            }
        }
        return INSTANCE;
    }

    public void setContext(Context context)
    {
        this.context = context.getApplicationContext();
    }

    public void showLoadingScreen()
    {
        com.saksham.customloadingdialog.LoaderKt.showDialog(context, false, R.raw.loading_animation);
    }
    public void hideLoadingScreen()
    {
        com.saksham.customloadingdialog.LoaderKt.hideDialog();
    }

}
