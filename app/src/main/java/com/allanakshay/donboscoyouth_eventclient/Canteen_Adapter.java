package com.allanakshay.donboscoyouth_eventclient;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Allan Akshay on 05-10-2017.
 */

public class Canteen_Adapter extends BaseAdapter implements ListAdapter {
    private ArrayList<String> item;
    private ArrayList<String> price;
    private Context context;

    public Canteen_Adapter(ArrayList<String> item, ArrayList<String> price, Context context)
    {
        this.item = item;
        this.price = price;
        this.context = context;
    }
    @Override
    public int getCount() {
        if(item!=null)
        return item.size();
        else
            return 0;
    }

    @Override
    public Object getItem(int position) {
        if(item!=null)
        return item.get(position);
        else
            return 0;
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
            view = inflater.inflate(R.layout.canteen_list, null);
        }
        TextView canteen_item = (TextView)view.findViewById(R.id.canteen_list_item);
        TextView canteen_price = (TextView)view.findViewById(R.id.canteen_list_price);
        CheckBox checkbox = (CheckBox) view.findViewById(R.id.canteen_list_checkbox);
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    Canteen_Activity.cost = Canteen_Activity.cost + Double.parseDouble(price.get(position));
                }
                else
                    Canteen_Activity.cost = Canteen_Activity.cost - Double.parseDouble(price.get(position));

                Canteen_Activity.checkout.setText("Checkout ( Rs. " + Canteen_Activity.cost + " )");
            }
        });
        canteen_item.setText(item.get(position));
        canteen_price.setText("Rs. " + price.get(position));
        return view;
    }
}
