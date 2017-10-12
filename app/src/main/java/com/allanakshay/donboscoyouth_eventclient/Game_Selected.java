package com.allanakshay.donboscoyouth_eventclient;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Game_Selected extends AppCompatActivity {
    private TextView game_selected;
    private TextView go;
    private TextView name;
    private TextView phone_number;
    private TextView balance;
    private TextView played_previously;
    private Button qualify_user;
    private TextView qualified;
    private Button charge_user;
    private EditText unique_id;
    private Query query;
    private String unique_id_string;
    private String name_string;
    private String phone_number_string;
    private String balance_string;
    private String game_selected_string;
    private String game_price;
    private String overall_points;
    private DatabaseReference ref_users = FirebaseDatabase.getInstance().getReference().child("Users");
    private DatabaseReference ref_games = FirebaseDatabase.getInstance().getReference().child("Games");
    private DatabaseReference ref_income = FirebaseDatabase.getInstance().getReference().child("Total Income");
    private DatabaseReference ref_points = FirebaseDatabase.getInstance().getReference().child("Points");
    private AlertDialog.Builder qualify_builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game__selected);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        LayoutInflater inflater = getLayoutInflater();
        final View view = inflater.inflate(R.layout.qualify_user_builder,null);
        qualify_builder = new AlertDialog.Builder(this);
        qualify_builder.setView(view);
        qualify_builder.setCancelable(true);
        game_selected_string = getIntent().getStringExtra("Game Name");
        game_selected = (TextView)findViewById(R.id.game_selected_game_selected);
        game_selected.setText(game_selected_string);
        go=(TextView)findViewById(R.id.game_selected_go);
        name=(TextView)findViewById(R.id.game_selected_name);
        phone_number=(TextView)findViewById(R.id.game_selected_phone_number);
        balance=(TextView)findViewById(R.id.game_selected_balance);
        qualify_user=(Button)findViewById(R.id.game_selected_qualify_user);
        qualified=(TextView)findViewById(R.id.game_selected_qualified);
        played_previously=(TextView)findViewById(R.id.game_selected_played_previously);
        charge_user=(Button) findViewById(R.id.game_selected_charge_user);
        unique_id = (EditText) findViewById(R.id.game_selected_unique_id);

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
                                                name_string = dataSnapshot.child("Name").getValue().toString();
                                                phone_number_string = dataSnapshot.child("Phone Number").getValue().toString();
                                                balance_string = dataSnapshot.child("Balance").getValue().toString();
                                                overall_points = dataSnapshot.child("Overall Points").getValue().toString();
                                                name.setText("Name : " + name_string);
                                                phone_number.setText("Phone Number : " + phone_number_string);
                                                balance.setText("Balance : Rs. " + balance_string);
                                                if(dataSnapshot.child("Games Played").child(game_selected_string).getValue().toString().equals("0"))
                                                    played_previously.setText("Played Previously : No");
                                                else
                                                    played_previously.setText("Played Previously : Yes");
                                                ref_games.child(game_selected_string).child("Qualified Users").child(unique_id_string).addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        if(dataSnapshot.getValue()!=null)
                                                        {
                                                            qualified.setText("Qualified : Yes");
                                                            charge_user.setEnabled(false);
                                                            qualify_user.setEnabled(true);
                                                        }
                                                        else {
                                                            qualified.setText("Qualified : No");
                                                            qualify_user.setEnabled(true);
                                                            charge_user.setEnabled(true);
                                                        }

                                                        ref_games.child(game_selected_string).child("Qualified Users").child(unique_id_string).removeEventListener(this);
                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

                                                    }
                                                });
                                                name.setEnabled(true);
                                                phone_number.setEnabled(true);
                                                balance.setEnabled(true);
                                                played_previously.setEnabled(true);
                                                qualified.setEnabled(true);
                                                Toast.makeText(Game_Selected.this, "User found", Toast.LENGTH_SHORT).show();

                                            } catch (Exception e)
                                            {
                                                unique_id_string = "";
                                                Toast.makeText(Game_Selected.this, "Error in Finding User", Toast.LENGTH_SHORT).show();
                                                charge_user.setEnabled(false);
                                                qualify_user.setEnabled(false);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                                }
                                else {
                                    unique_id_string = "";
                                    Toast.makeText(Game_Selected.this, "User not found", Toast.LENGTH_SHORT).show();
                                    charge_user.setEnabled(false);
                                    qualify_user.setEnabled(false);
                                }

                                query.removeEventListener(this);
                            } catch(Exception e)
                            {
                                unique_id_string = "";
                                Toast.makeText(Game_Selected.this, "Error in Finding User", Toast.LENGTH_SHORT).show();
                                query.removeEventListener(this);
                                charge_user.setEnabled(false);
                                qualify_user.setEnabled(false);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                else
                    Toast.makeText(Game_Selected.this, "Please Enter the Unique ID", Toast.LENGTH_SHORT).show();

            }
        });

        charge_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ref_games.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        try{
                            if((Double.parseDouble(balance_string) - Double.parseDouble(dataSnapshot.child(game_selected_string).child("Price").getValue().toString())) >= 0) {
                                ref_users.child(unique_id_string).child("Balance").setValue(String.valueOf(Double.parseDouble(balance_string) - Double.parseDouble(dataSnapshot.child(game_selected_string).child("Price").getValue().toString())));
                                balance.setText("Balance : Rs. " + (String.valueOf(Double.parseDouble(balance_string) - Double.parseDouble(dataSnapshot.child(game_selected_string).child("Price").getValue().toString()))));
                                game_price = dataSnapshot.child(game_selected_string).child("Price").getValue().toString();
                                ref_income.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        try {
                                            ref_income.child("Games").setValue(String.valueOf(Double.parseDouble(dataSnapshot.child("Games").getValue().toString()) + Double.parseDouble(game_price)));
                                            ref_income.removeEventListener(this);
                                        } catch (Exception e) {
                                            Toast.makeText(Game_Selected.this, "Error in adding total income", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                                ref_users.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        ref_users.child(unique_id_string).child("Games Played").child(game_selected_string).setValue(String.valueOf(Integer.parseInt(dataSnapshot.child(unique_id_string).child("Games Played").child(game_selected_string).getValue().toString()) + 1));
                                        ref_users.removeEventListener(this);
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                                ref_points.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        ref_users.child(unique_id_string).child("Overall Points").setValue(String.valueOf(Integer.parseInt(overall_points) + Integer.parseInt(dataSnapshot.child("P").getValue().toString())));
                                        ref_points.removeEventListener(this);
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                                charge_user.setEnabled(false);
                                qualify_user.setEnabled(true);
                                Toast.makeText(Game_Selected.this, "User successfully Charged", Toast.LENGTH_SHORT).show();
                                ref_games.removeEventListener(this);
                            }
                            else {
                                Toast.makeText(Game_Selected.this, "Insufficient Balance", Toast.LENGTH_SHORT).show();
                                qualify_user.setEnabled(false);
                            }
                        } catch (Exception e)
                        {
                            Toast.makeText(Game_Selected.this, "Error in charging user", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        qualify_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(view.getParent()!=null)
                {
                    ((ViewGroup)view.getParent()).removeView(view);
                }
                qualify_builder.show();
            }
        });

        TextView qualify_1st = (TextView) view.findViewById(R.id.qualify_user_1st);
        TextView qualify_2nd = (TextView) view.findViewById(R.id.qualify_user_2nd);
        TextView qualify_3rd = (TextView) view.findViewById(R.id.qualify_user_3rd);
        TextView qualify_eliminate = (TextView) view.findViewById(R.id.qualify_user_eliminate);

        qualify_1st.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    ref_points.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(qualified.getText().toString().equals("Qualified : Yes"))
                                ref_users.child(unique_id_string).child("Overall Points").setValue(String.valueOf(Integer.parseInt(overall_points) + Integer.parseInt(dataSnapshot.child("1").getValue().toString())));
                            else
                                ref_users.child(unique_id_string).child("Overall Points").setValue(String.valueOf(Integer.parseInt(overall_points) + Integer.parseInt(dataSnapshot.child("1").getValue().toString()) - Integer.parseInt(dataSnapshot.child("P").getValue().toString())));
                            qualified.setText("Qualified : Yes");
                            ((ViewGroup)view.getParent()).removeView(view);

                            qualify_user.setEnabled(false);
                            ref_points.removeEventListener(this);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                ref_games.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ref_games.child(game_selected_string).child("Qualified Users").child(unique_id_string).setValue("1");
                        ref_games.removeEventListener(this);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                charge_user.setEnabled(false);
            }
        });

        qualify_2nd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ref_points.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(qualified.getText().toString().equals("Qualified : Yes"))
                            ref_users.child(unique_id_string).child("Overall Points").setValue(String.valueOf(Integer.parseInt(overall_points) + Integer.parseInt(dataSnapshot.child("2").getValue().toString())));
                        else
                            ref_users.child(unique_id_string).child("Overall Points").setValue(String.valueOf(Integer.parseInt(overall_points) + Integer.parseInt(dataSnapshot.child("2").getValue().toString()) - Integer.parseInt(dataSnapshot.child("P").getValue().toString())));
                        qualified.setText("Qualified : Yes");
                        ((ViewGroup)view.getParent()).removeView(view);

                        qualify_user.setEnabled(false);
                        ref_points.removeEventListener(this);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                ref_games.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ref_games.child(game_selected_string).child("Qualified Users").child(unique_id_string).setValue("2");
                        ref_games.removeEventListener(this);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                charge_user.setEnabled(false);
            }
        });

        qualify_3rd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ref_points.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(qualified.getText().toString().equals("Qualified : Yes"))
                            ref_users.child(unique_id_string).child("Overall Points").setValue(String.valueOf(Integer.parseInt(overall_points) + Integer.parseInt(dataSnapshot.child("3").getValue().toString())));
                        else
                            ref_users.child(unique_id_string).child("Overall Points").setValue(String.valueOf(Integer.parseInt(overall_points) + Integer.parseInt(dataSnapshot.child("3").getValue().toString()) - Integer.parseInt(dataSnapshot.child("P").getValue().toString())));
                        qualified.setText("Qualified : Yes");
                        ((ViewGroup)view.getParent()).removeView(view);

                        qualify_user.setEnabled(false);
                        ref_points.removeEventListener(this);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                ref_games.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ref_games.child(game_selected_string).child("Qualified Users").child(unique_id_string).setValue("3");
                        ref_games.removeEventListener(this);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                charge_user.setEnabled(false);
            }
        });

        qualify_eliminate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ref_games.child(game_selected_string).child("Qualified Users").child(unique_id_string).removeValue();
                    qualified.setText("Qualified : No");
                    ((ViewGroup)view.getParent()).removeView(view);

                    qualify_user.setEnabled(false);

                } catch(Exception e)
                {
                    Toast.makeText(Game_Selected.this, "User not qualified. No need to eliminate", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}
