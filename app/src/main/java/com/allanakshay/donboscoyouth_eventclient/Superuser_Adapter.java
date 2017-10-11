package com.allanakshay.donboscoyouth_eventclient;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by Allan Akshay on 11-10-2017.
 */

public class Superuser_Adapter extends BaseAdapter implements ListAdapter {

    private ArrayList<String> canteen_games_list = new ArrayList<>();
    private ArrayList<String> price_list = new ArrayList<>();
    private Context context;
    private DatabaseReference ref_games = FirebaseDatabase.getInstance().getReference().child("Games");
    private DatabaseReference ref_canteen = FirebaseDatabase.getInstance().getReference().child("Canteen");
    private String from_activity;

    public Superuser_Adapter(ArrayList<String> canteen_games_list, ArrayList<String> price_list, Context context, String from_activity)
    {
        this.canteen_games_list = canteen_games_list;
        this.price_list = price_list;
        this.context = context;
        this.from_activity = from_activity;
    }
    @Override
    public int getCount() {
        return canteen_games_list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.superuser_games_canteen_list, null);
        }
        TextView item = (TextView) view.findViewById(R.id.superuser_game_canteen);
        item.setText(canteen_games_list.get(position));
        final EditText price = (EditText) view.findViewById(R.id.superuser_price);
        price.setText(price_list.get(position));
        Button set_price = (Button) view.findViewById(R.id.superuser_set_price_button);
        set_price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!price.getText().toString().trim().equals(""))
                {
                    if(from_activity.equals("Games"))
                    ref_games.child(canteen_games_list.get(position)).child("Price").setValue(price.getText().toString().trim());
                    else if(from_activity.equals("Canteen"))
                        ref_canteen.child(canteen_games_list.get(position)).child("Price").setValue(price.getText().toString().trim());
                    Toast.makeText(context, "Price successfully updated", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(context, "Please fill in the price field", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
}
