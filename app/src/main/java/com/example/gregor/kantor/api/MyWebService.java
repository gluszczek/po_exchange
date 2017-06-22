package com.example.gregor.kantor.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Klasa definiujaca konkretne zapytania do API w celu pobrania danych z bazy danych
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
