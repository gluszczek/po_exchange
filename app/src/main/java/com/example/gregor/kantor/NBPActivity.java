package com.example.gregor.kantor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class NBPActivity extends AppCompatActivity {

    private Button buttonDisplay;
    private Spinner spinnerCurrency;
    private ParserXml parserXml;
    private TextView currencyName;
    private TextView scaler;
    private TextView currencyCode;
    private TextView avgCurrency;
    private TextView buyCurrency;
    private TextView sellCurrency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        parserXml = new ParserXml();

        parserXml.parseXml();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nbp);

        buttonDisplay = (Button)findViewById(R.id.buttonDisplay);
        buttonDisplay.setOnClickListener(buttonDisplayListener);

        currencyName = (TextView)findViewById(R.id.currencyName);
        scaler = (TextView)findViewById(R.id.scaler);
        currencyCode = (TextView)findViewById(R.id.currencyCode);
        avgCurrency = (TextView)findViewById(R.id.avgCurrency);
        buyCurrency = (TextView)findViewById(R.id.buyNBP);
        sellCurrency = (TextView)findViewById(R.id.sellNBP);

        spinnerCurrency = (Spinner) findViewById(R.id.spinnerCurrency);
        setCurrencySpinner();

    }



    private View.OnClickListener buttonDisplayListener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {

            Currency c = parserXml.find(spinnerCurrency.getSelectedItem().toString());
            currencyName.setText(c.getCurrencyName());
            scaler.setText(c.getScaler());
            currencyCode.setText(c.getCurrencyCode());
            avgCurrency.setText(c.getExchangeRate());
            buyCurrency.setText(c.getBuyRate());
            sellCurrency.setText(c.getSellRate());
        }

    };

    private void setCurrencySpinner(){

        String[] spinner = new String[35];

        spinner[0] = "bat (Tajlandia)";
        spinner[1] = "dolar amerykański";
        spinner[2] = "dolar australijski";
        spinner[3] = "dolar Hongkongu";
        spinner[4] = "dolar kanadyjski";
        spinner[5] = "dolar nowozelandzki";
        spinner[6] = "dolar singapurski";
        spinner[7] = "euro";
        spinner[8] = "forint (Węgry)";
        spinner[9] = "frank szwajcarski";
        spinner[10] = "funt szterling";
        spinner[11] = "hrywna (Ukraina)";
        spinner[12] = "jen (Japonia)";
        spinner[13] = "korona czeska";
        spinner[14] = "korona duńska";
        spinner[15] = "korona islandzka";
        spinner[16] = "korona norweska";
        spinner[17] = "korona szwedzka";
        spinner[18] = "kuna (Chorwacja)";
        spinner[19] = "lej rumuński";
        spinner[20] = "lew (Bułgaria)";
        spinner[21] = "lira turecka";
        spinner[22] = "nowy izraelski szekel";
        spinner[23] = "peso chilijskie";
        spinner[24] = "peso filipińskie";
        spinner[25] = "peso meksykańskie";
        spinner[26] = "rand (Republika Południowej Afryki)";
        spinner[27] = "real (Brazylia)";
        spinner[28] = "ringgit (Malezja)";
        spinner[29] = "rubel rosyjski";
        spinner[30] = "rupia indonezyjska";
        spinner[31] = "rupia indyjska";
        spinner[32] = "won południowokoreański";
        spinner[33] = "yuan renminbi (Chiny)";
        spinner[34] = "SDR (MFW)";

        ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, spinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCurrency.setAdapter(adapter);
    }

}
