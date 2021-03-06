package com.allanakshay.donboscoyouth_eventclient;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Register_New extends AppCompatActivity {

    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");
    private DatabaseReference ref_canteen = FirebaseDatabase.getInstance().getReference().child("Canteen");
    private DatabaseReference ref_games = FirebaseDatabase.getInstance().getReference().child("Games");
    private DatabaseReference ref_income = FirebaseDatabase.getInstance().getReference().child("Total Income");
    private Query query;
    private EditText name;
    private EditText phone_number;
    private EditText unique_id;
    private EditText initial_recharge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register__new);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        name = (EditText)findViewById(R.id.register_user_name);
        phone_number = (EditText)findViewById(R.id.register_user_phone_number);
        unique_id = (EditText)findViewById(R.id.register_user_unique_id);
        initial_recharge = (EditText)findViewById(R.id.register_user_initial_recharge);
    }

    public void Register(View view)
    {
        if(!(name.getText().toString().isEmpty() || phone_number.getText().toString().isEmpty() || unique_id.getText().toString().isEmpty() || initial_recharge.getText().toString().trim().isEmpty()))
        {
            query = ref.orderByKey().equalTo(unique_id.getText().toString().trim());
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try {
                        if (dataSnapshot.getValue() == null) {
                            ref = ref.child(unique_id.getText().toString().trim());
                            ref.child("Name").setValue(name.getText().toString().trim());
                            ref.child("Phone Number").setValue(phone_number.getText().toString().trim());
                            ref.child("Balance").setValue(initial_recharge.getText().toString().trim());
                            ref.child("Overall Points").setValue("0");
                            ref = FirebaseDatabase.getInstance().getReference().child("Users");
                            Toast.makeText(Register_New.this, "User Successfully Registered", Toast.LENGTH_SHORT).show();
                            ref_canteen.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for(DataSnapshot snapshot : dataSnapshot.getChildren())
                                    {
                                        ref.child(unique_id.getText().toString().trim()).child("Items Bought").child(snapshot.getKey()).setValue("0");
                                    }
                                    ref_canteen.removeEventListener(this);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                            ref_games.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for(DataSnapshot snapshot : dataSnapshot.getChildren())
                                    {
                                        ref.child(unique_id.getText().toString().trim()).child("Games Played").child(snapshot.getKey()).setValue("0");
                                    }
                                    ref_games.removeEventListener(this);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                            ref_income.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    try {
                                        ref_income.child("Cashier").setValue(String.valueOf(Double.parseDouble(dataSnapshot.child("Cashier").getValue().toString()) + Double.parseDouble(initial_recharge.getText().toString().trim())));
                                        ref_income.removeEventListener(this);
                                    } catch (Exception e)
                                    {
                                        Toast.makeText(Register_New.this, "Error in adding total income", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                        } else {
                            Toast.makeText(Register_New.this, "Unique Code already exists. Please use another Unique Code.", Toast.LENGTH_SHORT).show();
                        }
                        query.removeEventListener(this);
                    } catch(Exception e)
                    {
                        Toast.makeText(Register_New.this, "Error in Registering User", Toast.LENGTH_SHORT).show();
                        ref = FirebaseDatabase.getInstance().getReference().child("Users");
                        query.removeEventListener(this);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }

            });
        }
        else
        {
            Toast.makeText(Register_New.this, "Please Fill all the Fields.", Toast.LENGTH_SHORT).show();
        }
    }


}
