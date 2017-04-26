package com.example.gregor.kantor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class CantorsViewActivity extends AppCompatActivity {

    private Button buttonDisplay;
    private TextView textViewInternetowyBuy;
    private TextView textViewInternetowySell;
    private TextView textViewCinkciarzBuy;
    private TextView textViewCinkciarzSell;
    private TextView textViewTrejdooBuy;
    private TextView textViewTrejdooSell;
    private Spinner spinnerCurrency;
    private InternetowyKantor internetowyKantor;
    private ArrayList<OnlineExchangeOffice> offices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cantors_view);

        buttonDisplay = (Button)findViewById(R.id.buttonDisplay);
        buttonDisplay.setOnClickListener(buttonDisplayListener);

        textViewInternetowyBuy = (TextView)findViewById(R.id.textViewInternetowyKantorBuy);
        textViewInternetowySell = (TextView)findViewById(R.id.textViewInternetowyKantorSell);
        textViewCinkciarzBuy = (TextView)findViewById(R.id.textViewCinkciarzBuy);
        textViewCinkciarzSell = (TextView)findViewById(R.id.textViewCinkciarzSell);
        textViewTrejdooBuy = (TextView)findViewById(R.id.textViewTrejdooBuy);
        textViewTrejdooSell = (TextView)findViewById(R.id.textViewTrejdooSell);
        spinnerCurrency = (Spinner) findViewById(R.id.spinnerCurrency);
        setCurrencySpinner();

        internetowyKantor = new InternetowyKantor(getApplicationContext());
        loadExchanges();
    }

    private View.OnClickListener buttonDisplayListener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            //Log.v("KANTOR", spinnerCurrency.toString());
        showExchanges(spinnerCurrency.getSelectedItem().toString());
        }
    };

    private void showExchanges(String currency){
        //loadInternetowyKantor(currency);
        showInternetowyKantor(currency);
        showCinkciarz(currency);
        showTrejdoo(currency);
    }

    private void loadExchanges(){
        offices = new ArrayList<>();
        loadInternetowyKantor();
        loadCinkciarz();
        loadTrejdoo();
    }

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

    private void XloadInternetowyKantor(String currency){
        internetowyKantor.search(currency);
        textViewInternetowyBuy.setText(internetowyKantor.searchBuyValue());
        textViewInternetowySell.setText(internetowyKantor.searchSellValue());
    }

    private void loadInternetowyKantor(){
        String regexSell = "kurs kurs_sprzedazy.*[0-9],[0-9]{4}.*kurs kurs_kupna.*([0-9],[0-9]{4})";
        String regexBuy = "kurs kurs_sprzedazy.*([0-9],[0-9]{4}).*kurs kurs_kupna.*[0-9],[0-9]{4}";
        ArrayList<String> urls = new ArrayList<>();
        urls.add("https://internetowykantor.pl/kurs-euro/");
        urls.add("https://internetowykantor.pl/kurs-dolara/");
        urls.add("https://internetowykantor.pl/kurs-funta/");
        urls.add("https://internetowykantor.pl/kurs-franka/");
        OnlineExchangeOffice office = new OnlineExchangeOffice(getApplicationContext(), regexBuy, regexSell, urls);
        offices.add(office);
    }

    private void showInternetowyKantor(String currency){
        OnlineExchangeOffice office = offices.get(0);
        office.search(currency);
        textViewInternetowyBuy.setText(office.getBuyValue());
        textViewInternetowySell.setText(office.getSellValue());
    }

    private void loadCinkciarz(){
        String regexBuy = "Dla 1,00.*\\n.*Kupno.*\\n.*Sprzedaż.*\\n.*\\n.*\\n.*([0-9],[0-9]{4}).*\\n.*\\n.*\\n.*\\n.*[0-9],[0-9]{4}";
        String regexSell = "Dla 1,00.*\\n.*Kupno.*\\n.*Sprzedaż.*\\n.*\\n.*\\n.*[0-9],[0-9]{4}.*\\n.*\\n.*\\n.*\\n.*([0-9],[0-9]{4})";
        ArrayList<String> urls = new ArrayList<>();
        urls.add("https://cinkciarz.pl/kantor/kursy-walut-cinkciarz-pl/eur");
        urls.add("https://cinkciarz.pl/kantor/kursy-walut-cinkciarz-pl/usd");
        urls.add("https://cinkciarz.pl/kantor/kursy-walut-cinkciarz-pl/gbp");
        urls.add("https://cinkciarz.pl/kantor/kursy-walut-cinkciarz-pl/chf");
        OnlineExchangeOffice office = new OnlineExchangeOffice(getApplicationContext(), regexBuy, regexSell, urls);
        offices.add(office);
    }

    private void showCinkciarz(String currency){
        OnlineExchangeOffice office = offices.get(1);
        office.search(currency);
        textViewCinkciarzBuy.setText(office.getBuyValue());
        textViewCinkciarzSell.setText(office.getSellValue());
    }

    private void loadTrejdoo(){
        String regexSell = "Sprzedaż.*\\n.*\\n.*[0-9],[0-9]{4}.*\\n.*\\n.*\\n.*\\n.*Kupno.*\\n.*\\n.*([0-9],[0-9]{4})";
        String regexBuy = "Sprzedaż.*\\n.*\\n.*([0-9],[0-9]{4}).*\\n.*\\n.*\\n.*\\n.*Kupno.*\\n.*\\n.*[0-9],[0-9]{4}";
        ArrayList<String> urls = new ArrayList<>();
        urls.add("http://www.trejdoo.com/analizy/kursy-walut/eur-pln/");
        urls.add("http://www.trejdoo.com/analizy/kursy-walut/usd-pln/");
        urls.add("http://www.trejdoo.com/analizy/kursy-walut/gbp-pln/");
        urls.add("http://www.trejdoo.com/analizy/kursy-walut/chf-pln/");
        OnlineExchangeOffice office = new OnlineExchangeOffice(getApplicationContext(), regexBuy, regexSell, urls);
        offices.add(office);
    }

    private void showTrejdoo(String currency){
        OnlineExchangeOffice office = offices.get(2);
        office.search(currency);
        textViewTrejdooBuy.setText(office.getBuyValue());
        textViewTrejdooSell.setText(office.getSellValue());
    }
}
