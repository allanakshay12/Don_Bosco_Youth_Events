package com.allanakshay.donboscoyouth_eventclient;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Allan Akshay on 05-10-2017.
 */

public class Games_Adapter extends BaseAdapter implements ListAdapter {

    ArrayList<String> games = new ArrayList<String>();
    ArrayList<String> price = new ArrayList<String>();
    Context context;
    String from_activity;

    public Games_Adapter(ArrayList<String> games, ArrayList<String> price, Context context, String from_activity)
    {
        this.games = games;
        this.price = price;
        this.context = context;
        this.from_activity = from_activity;
    }
    @Override
    public int getCount() {
        return games.size();
    }

    @Override
    public Object getItem(int position) {
        return games.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(view==null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.games_list, null);
        }
        TextView game = (TextView) view.findViewById(R.id.game_list_item);
        TextView prices = (TextView) view.findViewById(R.id.game_list_price);
        game.setText(games.get(position));
        if(from_activity.equals("Game"))
        prices.setText("Rs. "+price.get(position));
        else
            prices.setText(price.get(position));
        RelativeLayout click = (RelativeLayout) view.findViewById(R.id.game_list_click);
        if(from_activity.equals("Game")) {
            click.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, Game_Selected.class);
                    intent.putExtra("Game Name", games.get(position));
                    context.startActivity(intent);
                }
            });
        }
        return view;
    }
}
