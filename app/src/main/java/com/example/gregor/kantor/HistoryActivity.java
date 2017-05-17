package com.example.gregor.kantor;

/**
 * Created by x on 04.05.2017.
 */
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class HistoryActivity extends Activity {
        ListView list;
        ArrayList<String> listItems = new ArrayList<String>();
        ArrayAdapter<String> adapter;
        int clickCounter = 0;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_history);
            list = (ListView) findViewById(R.id.list);
            adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, listItems);
            list.setAdapter(adapter);

            list.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    String item = list.getItemAtPosition(position).toString();
                    Log.i("MainActivity", "Selected = " + item);
                }
            });
        }

        public void addItems(View v) {
            listItems.add("Clicked : " + clickCounter++);
            adapter.notifyDataSetChanged();
        }
    }

