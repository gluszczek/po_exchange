package com.example.gregor.kantor;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Created by x on 27.04.2017.
 */

public class SettingsActivity extends AppCompatActivity {

    private Spinner spinnerMotive;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        spinnerMotive = (Spinner) findViewById(R.id.spinnerMotive);
        setMotiveSpinner();
    }
    private void setMotiveSpinner(){
        String array_spinner[] = new String[4];
        array_spinner[0]="classic";
        array_spinner[1]="dark sky";
        array_spinner[2]="doom";
        array_spinner[3]="this other motive";

        ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, array_spinner);
        spinnerMotive.setAdapter(adapter);
    }

}
