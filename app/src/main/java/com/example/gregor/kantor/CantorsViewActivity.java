package com.example.gregor.kantor;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gregor.kantor.api.OfficesLoader;

import java.util.ArrayList;

import static java.lang.Thread.sleep;

public class CantorsViewActivity extends AppCompatActivity {

    private ListView list;
    private Adapter listAdapter;
    private Button buttonDisplay;
    private Spinner spinnerCurrency;
    private ArrayList<OnlineExchangeOffice> offices;
    private ProgressDialog progressDialog;
    private Handler progressHandler;
    private String lastClickedOffice = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cantors_view);
        buttonDisplay = (Button) findViewById(R.id.buttonDisplay);
        buttonDisplay.setOnClickListener(buttonDisplayListener);
        spinnerCurrency = (Spinner) findViewById(R.id.spinnerCurrency);
        setCurrencySpinner();
        setProgressHandler();
        OfficesLoader officesLoader = new OfficesLoader(CantorsViewActivity.this);
        offices = officesLoader.loadOffices();
        prepareListView();
    }

    private View.OnClickListener buttonDisplayListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            progressDialog = ProgressDialog.show(CantorsViewActivity.this, "",
                    "Wczytywanie, proszę czekać...", true);
            resetValues();
            downloadExchangesHTML(spinnerCurrency.getSelectedItem().toString());
            new Thread(new Runnable() {
                @Override
                public void run() {
                    int counter = 5;
                    while (!isLoaded() && counter > 0) {
                        try {
                            sleep(1000);
                            searchExchangesValues();
                            counter--;
                        } catch (Exception e) {
                            Log.e("threadmessage", e.getMessage());
                        }
                    }
                    progressHandler.sendEmptyMessage(0);
                }
            }).start();
        }
    };

    private void setProgressHandler() {
        progressHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                showExchanges();
                list.setAdapter(listAdapter);
                progressDialog.dismiss();
            }
        };
    }

    private boolean isLoaded() {
        for (OnlineExchangeOffice office :
                offices) {
            Log.e(office.getName() + ": ", "Sell:" + Double.toString(office.getSellValue()) +
                                            " Buy:" + Double.toString(office.getBuyValue()));

            if (office.getBuyValue() == -1 || office.getSellValue() == -1) {
                return false;
            }
        }
        return true;
    }

    private void resetValues() {
        Log.e("OFFICES SIZE: ", Integer.toString(offices.size()));
        for (OnlineExchangeOffice office :
                offices) {
            office.resetValues();
        }
    }

    private void downloadExchangesHTML(String currency) {
        for (OnlineExchangeOffice office :
                offices) {
            office.search(currency);
        }
    }

    private void searchExchangesValues() {
        for (OnlineExchangeOffice office :
                offices) {
            office.loadBuyValue();
            office.loadSellValue();
        }
    }

    private void setCurrencySpinner() {
        String array_spinner[] = new String[4];
        array_spinner[0] = "EUR";
        array_spinner[1] = "USD";
        array_spinner[2] = "GBP";
        array_spinner[3] = "CHF";

        ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, array_spinner);
        spinnerCurrency.setAdapter(adapter);
    }

    private void addExchangeOffice(OnlineExchangeOffice office) {
        listAdapter.add(office.getName(), Double.toString(office.getBuyValue()),
                                            Double.toString(office.getSellValue()));
    }

    private void showExchanges() {
        listAdapter.clear();
        for (OnlineExchangeOffice office :
                offices) {
            addExchangeOffice(office);
        }
    }

    private void prepareListView(){
        /* ListView handling */
        list = (ListView) findViewById(R.id.listExchangeOffice);
        listAdapter = new Adapter(this, "", "", "");
        list.setAdapter(listAdapter);
        list.setOnItemClickListener((parent, view, position, id) -> {
            lastClickedOffice = list.getItemAtPosition(position).toString();
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            builder.setMessage("Otworzyc kantor " + lastClickedOffice + " w przegladarce?").setPositiveButton("Tak", browserDialogClickListener)
                    .setNegativeButton("Nie", browserDialogClickListener).show();
            Log.i("HistoryActivity", "Selected = " + lastClickedOffice);
        });
    }

    private void goToWebsite(String officeName){
        String targetUrl = "";
        for (OnlineExchangeOffice office :
                offices) {
            if (office.getName() == officeName){
                targetUrl = office.getHtmlSource();
                break;
            }
        }
        Log.d("html", targetUrl);
        openBrowser(targetUrl);
    }

    private void openBrowser(String http){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(http));
        startActivity(browserIntent);
    }

    DialogInterface.OnClickListener browserDialogClickListener = (dialog, which) -> {
        switch (which){
            case DialogInterface.BUTTON_POSITIVE:
                goToWebsite(lastClickedOffice);
                break;

            case DialogInterface.BUTTON_NEGATIVE:
                //No button clicked
                break;
        }
    };
}