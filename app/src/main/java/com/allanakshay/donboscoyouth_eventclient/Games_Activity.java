package com.allanakshay.donboscoyouth_eventclient;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Games_Activity extends AppCompatActivity {

    private DatabaseReference ref_games = FirebaseDatabase.getInstance().getReference().child("Games");
    ArrayList<String> games = new ArrayList<String>();
    ArrayList<String> prices = new ArrayList<String>();
    Games_Adapter games_adapter;
    ListView games_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games_);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        String from_activity = "Game";
        setSupportActionBar(toolbar);
        games_list = (ListView)findViewById(R.id.games_list_view);
        games_adapter = new Games_Adapter(games, prices, this, from_activity);
        games_list.setAdapter(games_adapter);
        ref_games.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    games.clear();
                    prices.clear();
                    for(DataSnapshot snapshot : dataSnapshot.getChildren())
                    {
                        games.add(snapshot.getKey().toString());
                        prices.add(snapshot.child("Price").getValue().toString().trim());
                    }
                    games_adapter.notifyDataSetChanged();
                }
                catch(Exception e)
                {
                    games.clear();
                    prices.clear();
                    Toast.makeText(Games_Activity.this, "Error in fetching the games list", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

}
