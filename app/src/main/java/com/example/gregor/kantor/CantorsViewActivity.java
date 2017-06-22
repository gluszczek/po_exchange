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

/**
 * Widok, w ktorym wyswietlane sa kursy walut z poszeczgolnych kantorow internetowych.
 */
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
        setTheme(MainActivity.theme ? R.style.AppTheme_Adriana : R.style.AppTheme);
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

    /**
     * Obsluga przycisku odpowiadajacego za uruchomienie procedury wczytywania kursow walut z kantorow internetowych.
     */
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

    /**
     * Funkcja okreslajaca zachowanie po zaladowaniu bazy danych.
     * Wyswietla liste kantorow w listView.
     */
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

    /**
     * Funkcja sprawdzajaca czy kursy walut zostaly zadowane.
     * Wykorzystywana do zakonczenia ladowania w przypadku udanego zaladowania.
     *
     * @return true - jesli wszystkie kursy zostaly zaladowane.
     */
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

    /**
     * Resetowanie wartosci przypisanych do kantorow. Wszystkie dotychczasowo pobrane kursy zostaja wyczyszczone.
     */
    private void resetValues() {
        Log.e("OFFICES SIZE: ", Integer.toString(offices.size()));
        for (OnlineExchangeOffice office :
                offices) {
            office.resetValues();
        }
    }

    /**
     * Funkcja rozpoczynajaca pobieranie kodu zrodlowego strony internetowego kantoru w zaleznosci od podanej waluty
     *
     * @param currency waluta dla jakies pobierany jest kod zrodlowy
     */
    private void downloadExchangesHTML(String currency) {
        for (OnlineExchangeOffice office :
                offices) {
            office.search(currency);
        }
    }

    /**
     * Funkcja rozpoczynajaca przeszukiwanie kodu zrodlowego w celu znalezienia odpowiednich kursow walut.
     */
    private void searchExchangesValues() {
        for (OnlineExchangeOffice office :
                offices) {
            office.loadBuyValue();
            office.loadSellValue();
        }
    }

    /**
     * Ustawienie spinnera sluzacego do wybrania waluty, ktora ma zostac znaleziona
     */
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

    /**
     * Dodanie nazwy i wartosci kupna i sprzedazy waluty do listView dla podanego kantoru
     *
     * @param office kantor, ktorego atrybuty maja zostac dodane
     */
    private void addExchangeOffice(OnlineExchangeOffice office) {
        listAdapter.add(office.getName(), Double.toString(office.getBuyValue()),
                Double.toString(office.getSellValue()));
    }

    /**
     * Wyswietlenie kantorow na liscie poprzez dodanie wszystkich kantorow do listView.
     */
    private void showExchanges() {
        listAdapter.clear();
        for (OnlineExchangeOffice office :
                offices) {
            addExchangeOffice(office);
        }
    }

    /**
     * Konfiguracja widoku listy.
     * Przygotowanie okna dialogowego sluzacego do przekierowania na strone internetowa kantoru.
     */
    private void prepareListView() {
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

    /**
     * Otworzenie strony internetowej kantoru dla konkretnej waluty w przegladarce
     * @param officeName nazwa kantoru, ktory ma zostac otworzony
     */
    private void goToWebsite(String officeName) {
        String targetUrl = "";
        for (OnlineExchangeOffice office :
                offices) {
            if (office.getName() == officeName) {
                targetUrl = office.getHtmlSource();
                break;
            }
        }
        Log.d("html", targetUrl);
        openBrowser(targetUrl);
    }

    /**
     * Otworzenie przekazanego adresu http w przegladarce
     * @param http adres, ktory ma zostac otworzony
     */
    private void openBrowser(String http) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(http));
        startActivity(browserIntent);
    }

    DialogInterface.OnClickListener browserDialogClickListener = (dialog, which) -> {
        switch (which) {
            case DialogInterface.BUTTON_POSITIVE:
                goToWebsite(lastClickedOffice);
                break;

            case DialogInterface.BUTTON_NEGATIVE:
                //No button clicked
                break;
        }
    };
}