package com.planetpeopleplatform.mybakingapp.retrofit.util;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Hammedopejin on 5/24/2018.
 */

public class Client {
    private final static String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/";

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if ( retrofit == null ) {
            retrofit = new Retrofit.Builder()
                    .baseUrl( BASE_URL )
                    .addConverterFactory( GsonConverterFactory.create() )
                    .build();
        }
        return retrofit;
    }
}
