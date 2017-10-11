package com.allanakshay.donboscoyouth_eventclient;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Superuser_Canteen extends AppCompatActivity {
    private AlertDialog.Builder builder;
    private EditText price;
    private EditText item;
    private Button set_price;
    private View view;
    private DatabaseReference ref_games = FirebaseDatabase.getInstance().getReference().child("Canteen");
    private DatabaseReference ref_users = FirebaseDatabase.getInstance().getReference().child("Users");
    private ListView games_list;
    private Superuser_Adapter adapter;
    private ArrayList<String> item_list = new ArrayList<>();
    private ArrayList<String> price_list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_superuser__canteen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        games_list = (ListView) findViewById(R.id.superuser_canteen_listview);
        adapter = new Superuser_Adapter(item_list, price_list, this, "Canteen");
        games_list.setAdapter(adapter);
        builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        view = inflater.inflate(R.layout.games_canteen_add_builder, null);
        builder.setView(view);
        builder.setCancelable(true);
        price = (EditText) view.findViewById(R.id.game_canteen_price);
        item = (EditText) view.findViewById(R.id.game_canteen_item_name);
        set_price = (Button) view.findViewById(R.id.game_canteen_add_item);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.superuser_canteen_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view.getParent()!=null)
                {
                    ((ViewGroup)view.getParent()).removeView(view);
                }
                builder.show();
            }
        });
        set_price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(price.getText().toString().trim().equals("") || item.getText().toString().trim().equals("")))
                {
                    final Query query = ref_games.orderByKey().equalTo(item.getText().toString().trim());
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.getValue() == null)
                            {
                                ref_games.child(item.getText().toString().trim()).child("Price").setValue(price.getText().toString().trim());
                                ref_users.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for(DataSnapshot snapshot : dataSnapshot.getChildren())
                                            ref_users.child(snapshot.getKey()).child("Items Bought").child(item.getText().toString().trim()).setValue("0");
                                        ref_users.removeEventListener(this);
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                                Toast.makeText(Superuser_Canteen.this, "Item successfully added", Toast.LENGTH_SHORT).show();
                                ((ViewGroup)view.getParent()).removeView(view);
                            }
                            else
                                Toast.makeText(Superuser_Canteen.this, "Item already exists", Toast.LENGTH_SHORT).show();
                            query.removeEventListener(this);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }
                else
                    Toast.makeText(Superuser_Canteen.this, "Please Enter all the fields", Toast.LENGTH_SHORT).show();
            }
        });

        ref_games.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    item_list.clear();
                    price_list.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        item_list.add(snapshot.getKey());
                        price_list.add(snapshot.child("Price").getValue().toString());
                    }
                    adapter.notifyDataSetChanged();
                } catch (Exception e)
                {
                    Toast.makeText(Superuser_Canteen.this, "Error in fetching games list", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
