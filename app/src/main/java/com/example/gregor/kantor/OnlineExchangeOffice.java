package com.example.gregor.kantor;

import android.content.Context;
import android.util.Log;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.ContentValues.TAG;
import static com.example.gregor.kantor.MainActivity.DATA_PATH;

/**
 * Created by Gregor on 26.04.2017.
 */

public class OnlineExchangeOffice {
    private Context appContext;
    private String htmlSource;
    private ArrayList<String> htmlSources;
    private String htmlCode;
    private String regexSell;
    private String regexBuy;

    public OnlineExchangeOffice(Context context, String buyRegex, String sellRegex, ArrayList<String> htmls){
        appContext = context;
        regexSell = sellRegex;
        regexBuy = buyRegex;
        htmlSources = htmls;
    }

    public void search(String currency){
        switch (currency){
            case "EUR":
                htmlSource = htmlSources.get(0);
                break;
            case "USD":
                htmlSource = htmlSources.get(1);
                break;
            case "GBP":
                htmlSource = htmlSources.get(2);
                break;
            case "CHF":
                htmlSource = htmlSources.get(3);
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
                        Log.v(TAG, result);
                        //htmlCode = result;
                        writeToFile(htmlCode);
                    }
                });
    }

    public String getBuyValue(){
        if(htmlCode == null){
            return "";
        }
        String buyValue = getValueByRegex(regexBuy);
        return buyValue;
    }

    public String getSellValue(){
        if(htmlCode == null){
            return "";
        }
        String sellValue = getValueByRegex(regexSell);
        return sellValue;
    }

    private String getValueByRegex(String regex){
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(htmlCode);
        String value = "";
        if (matcher.find())
        {
            value = matcher.group(1);
        }
        return value;
    }

    private void writeToFile(String data) {
    try {
        File f = new File(DATA_PATH+"result.txt");
        FileOutputStream fos = new FileOutputStream(f);
        fos.write(data.getBytes());
        Log.v(TAG, "Completed.");
        fos.flush();
        fos.close();
    }
    catch (IOException e) {
        Log.e("Exception", "File write failed: " + e.toString());
    }
    }
}
