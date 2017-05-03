package com.example.gregor.kantor;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import java.util.ArrayList;

import static java.lang.Thread.sleep;

public class CantorsViewActivity extends AppCompatActivity {

    private Button buttonDisplay;
    private TextView textViewInternetowyBuy;
    private TextView textViewInternetowySell;
    private TextView textViewCinkciarzBuy;
    private TextView textViewCinkciarzSell;
    private TextView textViewTrejdooBuy;
    private TextView textViewTrejdooSell;
    private TextView textViewLiderBuy;
    private TextView textViewLiderSell;
    private TextView textViewDobryBuy;
    private TextView textViewDobrySell;
    private TextView textViewKantorPlBuy;
    private TextView textViewKantorPlSell;
    private Spinner spinnerCurrency;
    private ArrayList<OnlineExchangeOffice> offices;
    private ProgressDialog progressDialog;
    private Handler progressHandler;

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
        textViewLiderBuy = (TextView)findViewById(R.id.textViewLiderBuy);
        textViewLiderSell = (TextView)findViewById(R.id.textViewLiderSell);
        textViewDobryBuy = (TextView)findViewById(R.id.textViewDobryBuy);
        textViewDobrySell = (TextView)findViewById(R.id.textViewDobrySell);
        textViewKantorPlBuy = (TextView)findViewById(R.id.textViewKantorPlBuy);
        textViewKantorPlSell = (TextView)findViewById(R.id.textViewKantorPlSell);
        spinnerCurrency = (Spinner) findViewById(R.id.spinnerCurrency);
        setCurrencySpinner();
        setProgressHandler();
        loadExchanges();
    }

    private View.OnClickListener buttonDisplayListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            progressDialog= ProgressDialog.show(CantorsViewActivity.this, "",
                "Wczytywanie, proszę czekać...", true);
            resetValues();
            downloadExchangesHTML(spinnerCurrency.getSelectedItem().toString());
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (!isLoaded()) {
                        try {
                            sleep(1000);
                            searchExchangesValues();
                        } catch(Exception e) {
                            Log.e("threadmessage",e.getMessage());
                        }
                    }
                    progressHandler.sendEmptyMessage(0);
                }
            }).start();
        }
    };

    private void setProgressHandler(){
        progressHandler = new Handler() {
            @Override
            public void handleMessage(Message msg){
                showExchanges();
                progressDialog.dismiss();
            }
        };
    }

    private boolean isLoaded(){
        for (OnlineExchangeOffice office :
                offices) {
            Log.e(office.getName()+": ", "Sell:" + Double.toString(office.getSellValue())+" Buy:"+Double.toString(office.getBuyValue()));
            if (office.getBuyValue()==-1 || office.getSellValue()==-1) {
                return false;
            }
        }
        return true;
    }

    private void resetValues(){
        Log.e("OFFICES SIZE: ", Integer.toString(offices.size()));
        for (OnlineExchangeOffice office :
                offices) {
            office.resetValues();
        }
    }

    private void downloadExchangesHTML(String currency){
        for (OnlineExchangeOffice office :
                offices) {
            office.search(currency);
        }
    }

    private void searchExchangesValues(){
        for (OnlineExchangeOffice office :
                offices) {
            office.loadBuyValue();
            office.loadSellValue();
        }
    }

    private void loadExchanges(){
        offices = new ArrayList<>();
        loadInternetowyKantor();
        loadCinkciarz();
        loadTrejdoo();
        loadLiderWalut();
        loadDobryKantor();
        loadKantorPl();
    }

    private void showExchanges(){
        showInternetowyKantor();
        showCinkciarz();
        showTrejdoo();
        showLiderWalut();
        showDobryKantor();
        showKantorPl();
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

    private void loadInternetowyKantor(){
        String regexSell = "kurs kurs_sprzedazy.*[0-9],[0-9]{4}.*kurs kurs_kupna.*([0-9],[0-9]{4})";
        String regexBuy = "kurs kurs_sprzedazy.*([0-9],[0-9]{4}).*kurs kurs_kupna.*[0-9],[0-9]{4}";
        ArrayList<String> urls = new ArrayList<>();
        urls.add("https://internetowykantor.pl/kurs-euro/");
        urls.add("https://internetowykantor.pl/kurs-dolara/");
        urls.add("https://internetowykantor.pl/kurs-funta/");
        urls.add("https://internetowykantor.pl/kurs-franka/");
        OnlineExchangeOffice office = new OnlineExchangeOffice(getApplicationContext(), "InternetowyKantor.pl", regexBuy, regexSell, urls);
        offices.add(office);
    }

    private void showInternetowyKantor(){
        for (OnlineExchangeOffice office :
                offices) {
            if(office.getName().equals("InternetowyKantor.pl")){
                textViewInternetowyBuy.setText(Double.toString(office.getBuyValue()));
                textViewInternetowySell.setText(Double.toString(office.getSellValue()));
                return;
            }
        }
    }

    private void loadCinkciarz(){
        String regexBuy = "Dla 1,00.*\\n.*Kupno.*\\n.*Sprzedaż.*\\n.*\\n.*\\n.*([0-9],[0-9]{4}).*\\n.*\\n.*\\n.*\\n.*[0-9],[0-9]{4}";
        String regexSell = "Dla 1,00.*\\n.*Kupno.*\\n.*Sprzedaż.*\\n.*\\n.*\\n.*[0-9],[0-9]{4}.*\\n.*\\n.*\\n.*\\n.*([0-9],[0-9]{4})";
        ArrayList<String> urls = new ArrayList<>();
        urls.add("https://cinkciarz.pl/kantor/kursy-walut-cinkciarz-pl/eur");
        urls.add("https://cinkciarz.pl/kantor/kursy-walut-cinkciarz-pl/usd");
        urls.add("https://cinkciarz.pl/kantor/kursy-walut-cinkciarz-pl/gbp");
        urls.add("https://cinkciarz.pl/kantor/kursy-walut-cinkciarz-pl/chf");
        OnlineExchangeOffice office = new OnlineExchangeOffice(getApplicationContext(), "Cinkciarz.pl", regexBuy, regexSell, urls);
        offices.add(office);
    }

    private void showCinkciarz(){
        for (OnlineExchangeOffice office :
                offices) {
            if(office.getName().equals("Cinkciarz.pl")){
                textViewCinkciarzBuy.setText(Double.toString(office.getBuyValue()));
                textViewCinkciarzSell.setText(Double.toString(office.getSellValue()));
                return;
            }
        }
    }

    private void loadTrejdoo(){
        String regexSell = "Sprzedaż.*(?:\\n.*)*[0-9],[0-9]{4}.*\\n.*\\n.*\\n.*\\n.*Kupno.*\\n.*\\n.*([0-9],[0-9]{4})";
        String regexBuy = "Sprzedaż.*\\n.*\\n.*([0-9],[0-9]{4}).*\\n.*\\n.*\\n.*\\n.*Kupno.*\\n.*\\n.*[0-9],[0-9]{4}";
        ArrayList<String> urls = new ArrayList<>();
        urls.add("http://www.trejdoo.com/analizy/kursy-walut/eur-pln/");
        urls.add("http://www.trejdoo.com/analizy/kursy-walut/usd-pln/");
        urls.add("http://www.trejdoo.com/analizy/kursy-walut/gbp-pln/");
        urls.add("http://www.trejdoo.com/analizy/kursy-walut/chf-pln/");
        OnlineExchangeOffice office = new OnlineExchangeOffice(getApplicationContext(), "Trejdoo.pl", regexBuy, regexSell, urls);
        offices.add(office);
    }

    private void showTrejdoo(){
        for (OnlineExchangeOffice office :
                offices) {
            if(office.getName().equals("Trejdoo.pl")){
                textViewTrejdooBuy.setText(Double.toString(office.getBuyValue()));
                textViewTrejdooSell.setText(Double.toString(office.getSellValue()));
                return;
            }
        }
    }

    private void loadLiderWalut(){
        String regexSell = "ask\".*([0-9].[0-9]{4})<\\/span>";
        //String regexSell = "Kurs zakupu.*(?:\\n.*)*[0-9].[0-9]{4}.*(?:\\n.*)*Kurs sprzedaży.*(?:\\n.*)*([0-9].[0-9]{4})";
        String regexBuy = "bid\".*([0-9].[0-9]{4})<\\/span>";
        //String regexBuy = "Kurs zakupu.*\\n.*\\n.*\\n.*\\n.*\\n.*[0-9]\\.[0-9]{4}.*\\n.*\\n.*\\n.*\\n.*\\n.*\\n.*\\n.*\\n.*\\n.*\\n.*Kurs sprzed.*\\n.*\\n.*\\n.*\\n.*\\n.*([0-9]\\.[0-9]{4})";
        ArrayList<String> urls = new ArrayList<>();
        urls.add("https://liderwalut.pl/kursy-walut/kurs-euro");
        urls.add("https://liderwalut.pl/kursy-walut/kurs-dolara");
        urls.add("https://liderwalut.pl/kursy-walut/kurs-funta");
        urls.add("https://liderwalut.pl/kursy-walut/kurs-franka");
        OnlineExchangeOffice office = new OnlineExchangeOffice(getApplicationContext(), "LiderWalut.pl", regexBuy, regexSell, urls);
        offices.add(office);
    }

    private void showLiderWalut(){
        for (OnlineExchangeOffice office :
                offices) {
            if(office.getName().equals("LiderWalut.pl")){
                textViewLiderBuy.setText(Double.toString(office.getBuyValue()));
                textViewLiderSell.setText(Double.toString(office.getSellValue()));
                return;
            }
        }
    }

    private void loadDobryKantor(){
        String regexSell = "Sprzedaj.*Kup.*[0-9].[0-9]{4}.*([0-9].[0-9]{4})";
        String regexBuy = "Sprzedaj.*Kup.*([0-9].[0-9]{4}).*[0-9].[0-9]{4}";
        ArrayList<String> urls = new ArrayList<>();
        urls.add("https://www.dobrykantor.pl/kurs-euro.html");
        urls.add("https://www.dobrykantor.pl/kurs-usd.html");
        urls.add("https://www.dobrykantor.pl/kurs-gbp.html");
        urls.add("https://www.dobrykantor.pl/kurs-chf.html");
        OnlineExchangeOffice office = new OnlineExchangeOffice(getApplicationContext(), "DobryKantor.pl", regexBuy, regexSell, urls);
        offices.add(office);
    }

    private void showDobryKantor(){
        for (OnlineExchangeOffice office :
                offices) {
            if(office.getName().equals("DobryKantor.pl")){
                textViewDobryBuy.setText(Double.toString(office.getBuyValue()));
                textViewDobrySell.setText(Double.toString(office.getSellValue()));
                return;
            }
        }
    }

    private void loadKantorPl(){
        //String regexSell = "Kurs.*w Kantor.pl.*\\n.*\\n.*\\n.*\\n.*\\n.*\\n.*\\n.*\\n.*[0-9].[0-9]{4}.*\\n.*([0-9].[0-9]{4})";
        String regexSell = "cer-5.*([0-9].[0-9]{4})";
        //String regexBuy = "Kurs.*w Kantor.pl.*(?:\\n.*){8}[0-9].[0-9]{4}.*\\n.*([0-9].[0-9]{4})";
        String regexBuy = "cer-4.*([0-9].[0-9]{4})";
        ArrayList<String> urls = new ArrayList<>();
        urls.add("http://www.kantor.pl/euro-kurs-waluty");
        urls.add("http://www.kantor.pl/dolar-amerykanski-kurs-waluty");
        urls.add("http://www.kantor.pl/funt-brytyjski-kurs-waluty");
        urls.add("http://www.kantor.pl/frank-szwajcarski-kurs-waluty");
        OnlineExchangeOffice office = new OnlineExchangeOffice(getApplicationContext(), "Kantor.pl", regexBuy, regexSell, urls);
        offices.add(office);
    }

    private void showKantorPl(){
        for (OnlineExchangeOffice office :
                offices) {
            if(office.getName().equals("Kantor.pl")){
                textViewKantorPlBuy.setText(Double.toString(office.getBuyValue()));
                textViewKantorPlSell.setText(Double.toString(office.getSellValue()));
                return;
            }
        }
    }
}
