package com.allanakshay.donboscoyouth_eventclient;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class Cashier_activity extends AppCompatActivity {
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cashier_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


    }

    public void Register_New_User(View view)
    {
        intent = new Intent(Cashier_activity.this, Register_New.class);
        startActivity(intent);
    }
    public void Recharge_Existing_Account(View view)
    {
        intent = new Intent(Cashier_activity.this, Recharge_Existing.class);
        startActivity(intent);
    }

}
