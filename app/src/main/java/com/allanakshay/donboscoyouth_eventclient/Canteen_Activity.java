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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Canteen_Activity extends AppCompatActivity {
    EditText unique_id;
    ListView canteen_list;
    public static Button checkout;
    TextView go;
    private DatabaseReference ref_canteen = FirebaseDatabase.getInstance().getReference().child("Canteen");
    private DatabaseReference ref_users = FirebaseDatabase.getInstance().getReference().child("Users");
    public static String unique_id_string;
    private Query query;
    public static String name;
    public static String phone_number;
    public static String balance;
    public static Double cost = 0.0;
    private Canteen_Adapter canteen_adapter;
    private ArrayList<String> item = new ArrayList<String>();
    private ArrayList<String> price = new ArrayList<String>();
    private AlertDialog.Builder canteen_builder;
    private TextView name_text;
    private TextView phone_number_text;
    private TextView balance_text_text;
    private TextView cost_text;
    private Button checkout_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canteen_);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        unique_id = (EditText)findViewById(R.id.canteen_unique_id);
        canteen_list = (ListView)findViewById(R.id.canteen_list_view);
        checkout = (Button) findViewById(R.id.canteen_checkout);
        go = (TextView)findViewById(R.id.canteen_go);
        canteen_list.setEnabled(false);
        checkout.setEnabled(false);
        LayoutInflater inflater = getLayoutInflater();
        final View dialog = inflater.inflate(R.layout.canteen_builder, null);
        canteen_builder = new AlertDialog.Builder(this);
        canteen_builder.setView(dialog);
        canteen_builder.setCancelable(true);
        name_text = (TextView)dialog.findViewById(R.id.canteen_builder_name);
        phone_number_text = (TextView)dialog.findViewById(R.id.canteen_builder_phone_number);
        balance_text_text = (TextView)dialog.findViewById(R.id.canteen_builder_balance);
        cost_text = (TextView)dialog.findViewById(R.id.canteen_builder_cost);
        name_text = (TextView)dialog.findViewById(R.id.canteen_builder_name);
        checkout_text = (Button) dialog.findViewById(R.id.canteen_builder_button);
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!unique_id.getText().toString().isEmpty())
                {
                    query=ref_users.orderByKey().equalTo(unique_id.getText().toString().trim());
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            try{
                                if(dataSnapshot.getValue()!=null)
                                {
                                    unique_id_string = unique_id.getText().toString().trim();
                                    ref_users.child(unique_id_string).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            try {
                                                name = dataSnapshot.child("Name").getValue().toString();
                                                phone_number = dataSnapshot.child("Phone Number").getValue().toString();
                                                balance = dataSnapshot.child("Balance").getValue().toString();
                                                name_text.setText("Name : " + name);
                                                phone_number_text.setText("Phone Number : " + phone_number);
                                                balance_text_text.setText("Balance : Rs. " + balance);
                                                cost_text.setText("Cost : Rs. " + cost);
                                                canteen_list.setEnabled(true);
                                                checkout.setEnabled(true);
                                                checkout_text.setEnabled(true);
                                                Toast.makeText(Canteen_Activity.this, "User found", Toast.LENGTH_SHORT).show();
                                            } catch (Exception e)
                                            {
                                                unique_id_string = "";
                                                Toast.makeText(Canteen_Activity.this, "Error in Finding User", Toast.LENGTH_SHORT).show();
                                                canteen_list.setEnabled(false);
                                                checkout.setEnabled(false);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                                }
                                else {
                                    unique_id_string = "";
                                    Toast.makeText(Canteen_Activity.this, "User not found", Toast.LENGTH_SHORT).show();
                                    canteen_list.setEnabled(false);
                                    checkout.setEnabled(false);
                                }

                                query.removeEventListener(this);
                            } catch(Exception e)
                            {
                                unique_id_string = "";
                                Toast.makeText(Canteen_Activity.this, "Error in Finding User", Toast.LENGTH_SHORT).show();
                                query.removeEventListener(this);
                                canteen_list.setEnabled(false);
                                checkout.setEnabled(false);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                else
                    Toast.makeText(Canteen_Activity.this, "Please Enter the Unique ID", Toast.LENGTH_SHORT).show();
            }
        });

        ref_canteen.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try{
                    if(item!=null && price!=null)
                    {
                        item.clear();
                        price.clear();
                    }
                    for(DataSnapshot snapshot : dataSnapshot.getChildren())
                    {
                        item.add(snapshot.getKey().toString());
                        price.add(snapshot.child("Price").getValue().toString().trim());

                    }
                canteen_adapter.notifyDataSetChanged();
                } catch (Exception e)
                {
                    Toast.makeText(Canteen_Activity.this, "Error in Canteen Adapter", Toast.LENGTH_SHORT).show();
                    if(item!=null && price!=null) {
                        item.clear();
                        price.clear();
                    }
                    canteen_adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        canteen_adapter = new Canteen_Adapter(item, price, this);
        canteen_list.setAdapter(canteen_adapter);

        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dialog.getParent()!=null)
                {
                    ((ViewGroup)dialog.getParent()).removeView(dialog);
                }
                cost_text.setText("Cost : " + cost);
                canteen_builder.show();
            }
        });

        checkout_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((Double.parseDouble(balance) - cost) >=0)
                {
                    ref_users.child(unique_id_string).child("Balance").setValue(String.valueOf(Double.parseDouble(balance) - cost));
                    Toast.makeText(Canteen_Activity.this, "Items successfully paid for", Toast.LENGTH_SHORT).show();
                    canteen_list.setEnabled(false);
                    checkout.setEnabled(false);
                    checkout_text.setEnabled(false);
                    balance_text_text.setText("Balance : Rs. " + (Double.parseDouble(balance) - cost) );
                }
                else
                {
                    Toast.makeText(Canteen_Activity.this, "Insufficient Balance. Please recharge the account and try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        cost = 0.0;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        cost = 0.0;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
