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

public class Superuser_Users extends AppCompatActivity {
    private ArrayList<String> name_list = new ArrayList<>();
    private ArrayList<String> details = new ArrayList<>();
    private DatabaseReference ref_users = FirebaseDatabase.getInstance().getReference().child("Users");
    private ListView user_list;
    private Games_Adapter adapter;
    StringBuilder stringBuilder = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_superuser__users);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        user_list = (ListView) findViewById(R.id.superuser_users_listview);
        adapter = new Games_Adapter(name_list, details, this, "");
        user_list.setAdapter(adapter);
        ref_users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //try {
                    name_list.clear();
                    details.clear();
                    for(DataSnapshot snapshot : dataSnapshot.getChildren())
                    {
                        name_list.add(snapshot.getKey());
                        stringBuilder.append("\t\tName : " + snapshot.child("Name").getValue().toString()+"\n\n");
                        stringBuilder.append("\t\tPhone Number : " + snapshot.child("Phone Number").getValue().toString()+"\n\n");
                        stringBuilder.append("\t\tOverall Points : " + snapshot.child("Overall Points").getValue().toString()+"\n\n");
                        stringBuilder.append("\t\tBalance : Rs. " + snapshot.child("Balance").getValue().toString()+"\n\n");
                        stringBuilder.append("\t\tGames Played :\n");
                        for(DataSnapshot newsnapshot : snapshot.child("Games Played").getChildren())
                            stringBuilder.append("\t\t\t\t" + newsnapshot.getKey() + " : " + newsnapshot.getValue().toString() +"\n");
                        stringBuilder.append("\n\t\tItems Bought :\n");
                        for(DataSnapshot newsnapshot : snapshot.child("Items Bought").getChildren())
                            stringBuilder.append("\t\t\t\t" + newsnapshot.getKey() + " : " + newsnapshot.getValue().toString()+ "\n");
                        details.add(stringBuilder.toString());
                        stringBuilder.delete(0, stringBuilder.length()-1);
                    }
                    adapter.notifyDataSetChanged();
              /* } catch (Exception e)
                {
                    Toast.makeText(Superuser_Users.this, "Error in fetching Users List", Toast.LENGTH_SHORT).show();
                } */
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
