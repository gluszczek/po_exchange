package com.example.gregor.kantor;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by x on 04.05.2017.
 */

public class Adapter extends BaseAdapter{

    Context context;
    ArrayList<String> officeName;
    ArrayList<String> buyCurrency;
    ArrayList<String> sellCurrency;
    int index = 1;
    private static LayoutInflater inflater = null;

    public Adapter(Context context, String officeName, String buyCurrency, String sellCurrency) {
        // TODO Auto-generated constructor stub
        this.officeName = new ArrayList<String>();
        this.buyCurrency = new ArrayList<String>();
        this.sellCurrency = new ArrayList<String>();

        this.context = context;
        this.officeName.add(officeName);
        this.buyCurrency.add(buyCurrency);
        this.sellCurrency.add(sellCurrency);

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return officeName.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return officeName.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public void add(String name, String bcurrency, String scurrency) {

        this.officeName.add(name);
        this.buyCurrency.add(bcurrency);
        this.sellCurrency.add(scurrency);
        notifyDataSetChanged();
    }

    public void clear(){
        this.officeName.clear();
        this.buyCurrency.clear();
        this.sellCurrency.clear();
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View vi = convertView;
        if (vi == null)
            vi = inflater.inflate(R.layout.row_cantors_view, null);
        TextView name = (TextView) vi.findViewById(R.id.exchangeOfficeName);
        name.setText(officeName.get(position));
        TextView bcurrency = (TextView) vi.findViewById(R.id.buyCurrency);
        bcurrency.setText(buyCurrency.get(position));
        TextView scurrency = (TextView) vi.findViewById(R.id.sellCurrency);
        scurrency.setText(sellCurrency.get(position));
        return vi;
    }
}



