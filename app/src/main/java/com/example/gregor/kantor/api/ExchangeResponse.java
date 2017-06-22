package com.example.gregor.kantor.api;

import com.google.gson.annotations.SerializedName;

/**
 * Klasa reprezentujaca cialo internetowego kantoru sluzaca do otrzymywania danych z API
 */

public class ExchangeResponse {
    @SerializedName("idexchange")
    public String id;
    @SerializedName("name")
    public String name;
    @SerializedName("regex_buy")
    public String regexBuy;
    @SerializedName("regex_sell")
    public String regexSell;
}
