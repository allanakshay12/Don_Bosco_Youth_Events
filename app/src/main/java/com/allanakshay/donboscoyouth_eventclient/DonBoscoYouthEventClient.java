package com.allanakshay.donboscoyouth_eventclient;

import android.support.multidex.MultiDexApplication;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Allan Akshay on 04-09-2017.
 */

public class DonBoscoYouthEventClient extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(false);
    }
}
