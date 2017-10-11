package com.allanakshay.donboscoyouth_eventclient;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class General extends AppCompatActivity {
    private DatabaseReference ref_users = FirebaseDatabase.getInstance().getReference().child("Users");
    private DatabaseReference ref_games = FirebaseDatabase.getInstance().getReference().child("Games");
    private DatabaseReference ref_income = FirebaseDatabase.getInstance().getReference().child("Total Income");
    private TextView total_income;
    private ListView overall_winners_listview;
    private ListView qualified_users_listview;
    private Query query;
    ArrayList<String> winners_name_list = new ArrayList<>();
    ArrayList<String> scores_list = new ArrayList<>();
    ArrayList<String> qualified_name_list = new ArrayList<>();
    ArrayList<String> game_list = new ArrayList<>();
    Games_Adapter overall_adapter;
    Games_Adapter qualified_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        total_income = (TextView) findViewById(R.id.general_total_income);
        overall_winners_listview = (ListView) findViewById(R.id.general_overall_ranking_listview);
        qualified_users_listview = (ListView) findViewById(R.id.general_qualified_users_listview);
        overall_adapter = new Games_Adapter(winners_name_list, scores_list, this, "");
        qualified_adapter = new Games_Adapter(qualified_name_list, game_list, this, "");
        overall_winners_listview.setAdapter(overall_adapter);
        qualified_users_listview.setAdapter(qualified_adapter);
        ref_income.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                total_income.setText("\t\tRs. " + dataSnapshot.child("Cashier").getValue().toString());
                ref_income.removeEventListener(this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        query = ref_users.orderByChild("Overall Points").limitToLast(4);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try
                {
                    winners_name_list.clear();
                    scores_list.clear();
                    for(DataSnapshot snapshot : dataSnapshot.getChildren())
                    {
                        winners_name_list.add(snapshot.child("Name").getValue().toString());
                        scores_list.add("Points : "+snapshot.child("Overall Points").getValue().toString());
                    }
                    overall_adapter.notifyDataSetChanged();
                } catch (Exception e)
                {
                    //Toast.makeText(General.this, "Error in processing Overall Winners", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ref_games.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                try{
                    qualified_name_list.clear();
                    game_list.clear();
                    for(final DataSnapshot snapshot : dataSnapshot.getChildren())
                    {
                        ref_users.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot newdataSnapshot) {
                                try{
                                    for(DataSnapshot qualified : snapshot.child("Qualified Users").getChildren()) {
                                        game_list.add("Game : "+snapshot.getKey());
                                        qualified_name_list.add(newdataSnapshot.child(qualified.getKey()).child("Name").getValue().toString());

                                    }
                                    qualified_adapter.notifyDataSetChanged();
                                    ref_users.removeEventListener(this);
                                } catch (Exception e)
                                {
                                    Toast.makeText(General.this, "Error in getting user list", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                    qualified_adapter.notifyDataSetChanged();

                } catch(Exception e)
                {
                    Toast.makeText(General.this, "Error in fetching qualified users list", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

}
