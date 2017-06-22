package com.example.gregor.kantor.api;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Klasa tworzaca obiekt biblioteki RetroFit w celu komuniacji z API
 */

public class RestClient {
    public static final String BASE_URL = "http://kantor-api.azurewebsites.net/";

    /**
     * Przygotowanie obiektu laczacago sie z API poprzez okereslenie zrodlowego adresu URL, ustawienia konwertera danych na GSON oraz uruchomienie wyswietlanai komunikatow (logow)
     * @return utworzony obiekt Retrofit do laczenia sie z API
     */
    public static Retrofit getClient(){
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS);
        httpClient.addInterceptor(logging);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        return retrofit;
    }
}
