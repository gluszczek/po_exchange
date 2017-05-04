package com.example.gregor.kantor;

/**
 * Created by aaaa on 25.04.2017.
 */

public class Currency {
    String currencyName;
    int scaler;
    String currencyCode;
    Float exchangeRate;

    Currency(String currencyName, String scaler, String currencyCode, String exchangeRate){
        this.currencyName = currencyName;
        this.scaler = Integer.parseInt(scaler);
        this.currencyCode = currencyCode;
        this.exchangeRate = Float.parseFloat(exchangeRate);
    }

    public String toString(){
        return "Nazwa waluty: " + currencyName + " , przelicznik: " + scaler + " , kod waluty: " + currencyCode + " , wartosc srednia: " + exchangeRate;
    }

    public String getScaler() {
        return Integer.toString(scaler);
    }

    public void setScaler(int scaler) {
        this.scaler = scaler;
    }

    public String getExchangeRate() {
        return exchangeRate.toString();
    }

    public void setExchangeRate(float exchangeRate) {
        this.exchangeRate = exchangeRate;
    }



    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }



    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }
}
