package com.allanakshay.donboscoyouth_eventclient;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

public class MainPage extends AppCompatActivity {

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void Games(View view)
    {
        intent = new Intent(MainPage.this, Games_Activity.class);
        startActivity(intent);
    }
    public void Canteen(View view)
    {
        intent = new Intent(MainPage.this, Canteen_Activity.class);
        startActivity(intent);
    }
    public void Cashier(View view)
    {
        intent = new Intent(MainPage.this, Cashier_activity.class);
        startActivity(intent);
    }

    public void General_Info(View view)
    {
        intent = new Intent(MainPage.this, General.class);
        startActivity(intent);
    }
    public void Admin_Access (View view)
    {
        intent = new Intent(MainPage.this, Admin_Access_Class.class);
        startActivity(intent);
    }
}
