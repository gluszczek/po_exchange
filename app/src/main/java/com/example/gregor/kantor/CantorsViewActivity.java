package com.example.gregor.kantor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class CantorsViewActivity extends AppCompatActivity {

    private Button buttonDisplay;
    private TextView textViewInternetowyBuy;
    private TextView textViewInternetowySell;
    private Spinner spinnerCurrency;
    private InternetowyKantor internetowyKantor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cantors_view);

        buttonDisplay = (Button)findViewById(R.id.buttonDisplay);
        buttonDisplay.setOnClickListener(buttonDisplayListener);

        textViewInternetowyBuy = (TextView)findViewById(R.id.textViewInternetowyKantorBuy);
        textViewInternetowySell = (TextView)findViewById(R.id.textViewInternetowyKantorSell);
        spinnerCurrency = (Spinner) findViewById(R.id.spinnerCurrency);
        setCurrencySpinner();

        internetowyKantor = new InternetowyKantor(getApplicationContext());
    }

    private View.OnClickListener buttonDisplayListener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            //Log.v("KANTOR", spinnerCurrency.toString());
            internetowyKantor.search(spinnerCurrency.getSelectedItem().toString());
            textViewInternetowyBuy.setText(internetowyKantor.searchBuyValue());
            textViewInternetowySell.setText(internetowyKantor.searchSellValue());
        }
    };

    private void setCurrencySpinner(){
        String array_spinner[] = new String[4];
        array_spinner[0]="EUR";
        array_spinner[1]="USD";
        array_spinner[2]="GBP";
        array_spinner[3]="CHF";

        ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, array_spinner);
        spinnerCurrency.setAdapter(adapter);
    }
}
