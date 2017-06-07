package com.example.gregor.kantor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Created by x on 27.04.2017.
 */

public class SettingsActivity extends AppCompatActivity {

    private Spinner spinnerMotive;
    private CheckBox secretOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        spinnerMotive = (Spinner) findViewById(R.id.spinnerMotive);
        secretOption = (CheckBox) findViewById(R.id.checkboxSecretOption);
        secretOption.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {

                if(isChecked){


                }
            }
        }
        );

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
