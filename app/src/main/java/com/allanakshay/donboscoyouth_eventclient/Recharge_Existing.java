package com.allanakshay.donboscoyouth_eventclient;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Recharge_Existing extends AppCompatActivity {
    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");
    private DatabaseReference ref2;
    private Query query;
    EditText unique_id;
    TextView name;
    TextView phone_number;
    TextView balance;
    EditText recharge_amount;
    TextView points;
    TextView go;
    Button recharge;
    String unique_id_string;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge__existing);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        unique_id = (EditText)findViewById(R.id.recharge_existing_unique_id);
        recharge_amount = (EditText)findViewById(R.id.recharge_existing_recharge_amount);
        name = (TextView) findViewById(R.id.recharge_existing_name);
        phone_number = (TextView) findViewById(R.id.recharge_existing_phone_number);
        balance = (TextView) findViewById(R.id.recharge_existing_balance_amount);
        points = (TextView) findViewById(R.id.recharge_existing_overall_points);
        go = (TextView) findViewById(R.id.recharge_existing_go);
        recharge = (Button)findViewById(R.id.recharge_existing_recharge_button);
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!unique_id.getText().toString().isEmpty())
                {
                    query=ref.orderByKey().equalTo(unique_id.getText().toString().trim());
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            try{
                                if(dataSnapshot.getValue()!=null)
                                {
                                    unique_id_string = unique_id.getText().toString().trim();
                                    ref.child(unique_id_string).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            try {
                                                name.setText("Name : " + dataSnapshot.child("Name").getValue().toString());
                                                name.setEnabled(true);
                                                phone_number.setText("Phone number : " + dataSnapshot.child("Phone Number").getValue().toString());
                                                phone_number.setEnabled(true);
                                                balance.setText("Balance Amount : " + dataSnapshot.child("Balance").getValue().toString());
                                                balance.setEnabled(true);
                                                points.setText("Overall Points : " + dataSnapshot.child("Overall Points").getValue().toString());
                                                points.setEnabled(true);
                                                recharge_amount.setEnabled(true);
                                                recharge.setEnabled(true);
                                            } catch (Exception e)
                                            {
                                                unique_id_string = "";
                                                Toast.makeText(Recharge_Existing.this, "Error in Finding User", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                                }
                                else {
                                    unique_id_string = "";
                                    Toast.makeText(Recharge_Existing.this, "User not found", Toast.LENGTH_SHORT).show();
                                    name.setEnabled(false);
                                    phone_number.setEnabled(false);
                                    balance.setEnabled(false);
                                    recharge_amount.setEnabled(false);
                                    recharge.setEnabled(false);
                                    points.setEnabled(false);
                                }

                                query.removeEventListener(this);
                            } catch(Exception e)
                            {
                                unique_id_string = "";
                                Toast.makeText(Recharge_Existing.this, "Error in Finding User", Toast.LENGTH_SHORT).show();
                                query.removeEventListener(this);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                else
                    Toast.makeText(Recharge_Existing.this, "Please Enter the Unique ID", Toast.LENGTH_SHORT).show();
            }
        });

        recharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!recharge_amount.getText().toString().trim().isEmpty())
                {
                    ref2 = ref.child(unique_id_string);
                    ref2.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            try {
                                ref2.child("Balance").setValue(String.valueOf(Double.parseDouble(dataSnapshot.child("Balance").getValue().toString()) + Double.parseDouble(recharge_amount.getText().toString().trim())));
                                balance.setText("Balance Amount : " + String.valueOf(Double.parseDouble(dataSnapshot.child("Balance").getValue().toString()) + Double.parseDouble(recharge_amount.getText().toString().trim())));
                                Toast.makeText(Recharge_Existing.this, "Amount Successfully Recharged", Toast.LENGTH_SHORT).show();
                                ref2.removeEventListener(this);

                            } catch ( Exception e )
                            {
                                Toast.makeText(Recharge_Existing.this, "Error in Recharging the Amount", Toast.LENGTH_SHORT).show();
                                ref2.removeEventListener(this);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    name.setEnabled(false);
                    phone_number.setEnabled(false);
                    points.setEnabled(false);
                    recharge.setEnabled(false);
                    recharge_amount.setEnabled(false);
                    balance.setEnabled(false);

                }
                else
                {
                    Toast.makeText(Recharge_Existing.this, "Please Enter a Valid Recharge Amount", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
