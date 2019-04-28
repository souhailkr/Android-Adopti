package com.esprit.souhaikr.adopt.controllers;

import com.esprit.souhaikr.adopt.utils.BottomNavigationActivity;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by SouhaiKr on 26/11/2018.
 */

public class APIClient {
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {



        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
      //OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        OkHttpClient client = new OkHttpClient.Builder()
        .addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();

                Request request = original.newBuilder()
                        .header("Accept", "application/pyur.v1")
                        .header("Authorization","Bearer "+BottomNavigationActivity.token)
                        .header("Content-Type", "application/json")
                        .method(original.method(),original.body())
                        .build();
                return chain.proceed(request);
            }
        }).build();


        retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.5:3000")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();



        return retrofit;
    }

}
