package com.example.gregor.kantor;

import android.content.Context;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

/**
 * Created by Gregor on 20.04.2017.
 */

public class InternetowyKantor {

    private Context appContext;
    private String htmlSource;
    private String htmlCode;

    InternetowyKantor(Context context){
        appContext = context;
    }

    public void search(String currency){
        switch (currency){
            case "EUR":
                htmlSource = "https://internetowykantor.pl/kurs-euro/";
                break;
            case "USD":
                htmlSource = "https://internetowykantor.pl/kurs-dolara/";
                break;
            case "GBP":
                htmlSource = "https://internetowykantor.pl/kurs-funta/";
                break;
            case "CHF":
                htmlSource = "https://internetowykantor.pl/kurs-franka/";
                break;
        }
        getHTML();
    }

    public void getHTML() {
        Ion.with(appContext)
                .load(htmlSource)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        htmlCode = result.trim().replaceAll(" +"," ");
                    }
                });
    }

//    private void writeToFile(String data,Context context) {
//        try {
//            File f = new File(DATA_PATH+"result.txt");
//            FileOutputStream fos = new FileOutputStream(f);
//            fos.write(data.getBytes());
//            Log.v(TAG, "Completed.");
//            fos.flush();
//            fos.close();
//        }
//        catch (IOException e) {
//            Log.e("Exception", "File write failed: " + e.toString());
//        }
//    }

    public String searchBuyValue(){
        if(htmlCode == null){
            return "";
        }
        String tagBuy = "kurs kurs_sprzedazy";
        return getValueByTag(htmlCode, tagBuy);
    }

    public String searchSellValue(){
        if(htmlCode == null){
            return "";
        }
        String tagSell = "kurs kurs_kupna";
        return getValueByTag(htmlCode, tagSell);
    }

    private String getValueByTag(String source, String tag ){
        int valueLenght = 6;
        int offset = 2;
        int idx = source.indexOf(tag)+tag.length()+offset;

        return source.substring(idx, idx+valueLenght);
    }
}
