package com.example.gregor.kantor.api;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Gregor on 07.06.2017.
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
