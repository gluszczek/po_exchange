package com.example.gregor.kantor.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Gregor on 07.06.2017.
 */

public interface MyWebService {
    @GET("exchanges/{id}")
    Call<List<ExchangeResponse>> getExchange(@Path("id") int exchangeId);

    @GET("exchanges")
    Call<List<ExchangeResponse>> getExchangeList();

    @GET("url/{idexchange}/{idcurrency}")
    Call<List<UrlResponse>> getUrl(@Path("idexchange") int exchangeId,
                             @Path("idcurrency") int currencyId);

}
