package com.allanakshay.donboscoyouth_eventclient;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class Admin_Access_Class extends AppCompatActivity {

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__access__class);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void Admin_Games(View v)
    {
        intent = new Intent(Admin_Access_Class.this, Superuser_Games.class);
        startActivity(intent);
    }
    public void Admin_Canteen(View v)
    {
        intent = new Intent(Admin_Access_Class.this, Superuser_Canteen.class);
        startActivity(intent);
    }
    public void Admin_Users(View v)
    {
        intent = new Intent(Admin_Access_Class.this, Superuser_Users.class);
        startActivity(intent);
    }

}
