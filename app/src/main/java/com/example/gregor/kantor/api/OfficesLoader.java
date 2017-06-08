package com.example.gregor.kantor.api;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.gregor.kantor.OnlineExchangeOffice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * Created by Gregor on 07.06.2017.
 */

public class OfficesLoader {
    private AppCompatActivity activityReference;
    private MyWebService myWebService;
    private ArrayList<OnlineExchangeOffice> onlineExchangeOffices;
    private ProgressDialog progress;
    private Thread progressThread;

    public OfficesLoader(AppCompatActivity activity){
        activityReference = activity;
        myWebService = RestClient.getClient().create(MyWebService.class);
    }

    public ArrayList<OnlineExchangeOffice> loadOffices(){
        onlineExchangeOffices = new ArrayList<>();
        runProgressDialog();
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<ExchangeResponse> exchangeResponses = downloadExchangesList();
                for (ExchangeResponse exchangeResponse :
                        exchangeResponses) {
                    onlineExchangeOffices.add(mergeExchangeWithCurrencies(exchangeResponse));
                }
                progress.dismiss();
            }
        }).start();
        return onlineExchangeOffices;
    }

    public OnlineExchangeOffice mergeExchangeWithCurrencies(ExchangeResponse exchange){
        ArrayList<String> htmlSources = new ArrayList<>();
        for(int i=1; i<=4; i++){
            htmlSources.add(loadUrl(Integer.parseInt(exchange.id), i));
        }
        OnlineExchangeOffice office = new OnlineExchangeOffice(
                activityReference.getApplicationContext(),
                exchange.name,
                exchange.regexBuy,
                exchange.regexSell,
                htmlSources);

        return office;
    }

    public List<ExchangeResponse> downloadExchangesList(){
        Call<List<ExchangeResponse>> call = myWebService.getExchangeList();
        List<ExchangeResponse> exchangeResponses = null;
        try {
            exchangeResponses =  call.execute().body();
        }catch( IOException e){
            e.printStackTrace();
        }
        return  exchangeResponses;
    }

    public String loadUrl(int exchangeId, int currencyId){
        Call<List<UrlResponse>> call = myWebService.getUrl(exchangeId, currencyId);
        try {
            UrlResponse urlResponse = call.execute().body().get(0);
            return urlResponse.url;
        }catch( IOException e){
            e.printStackTrace();
        }

        return "";
    }

    public void runProgressDialog(){
        progress = new ProgressDialog(activityReference);
        progress.setMessage("Pobieranie bazy danych...");
        progress.setCancelable(false);
        progress.show();
    }
}
