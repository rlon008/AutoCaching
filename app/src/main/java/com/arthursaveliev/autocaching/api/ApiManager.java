package com.arthursaveliev.autocaching.api;

import com.arthursaveliev.autocaching.api.cache.converter.GsonConverterListener;
import com.arthursaveliev.autocaching.api.cache.converter.GsonCustomConverterFactory;
import com.google.gson.TypeAdapter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;


class ApiManager implements GsonConverterListener {

    private static final String URL = "https://jsonplaceholder.typicode.com/";
    private static ApiManager apiManager = new ApiManager();

    private ApiService apiService;

    static ApiService getService(){
        if (apiManager.apiService == null) {
            apiManager.apiService = apiManager.createRetrofit().create(ApiService.class);
        }
        return apiManager.apiService;
    }

    private Retrofit createRetrofit(){
        return new Retrofit.Builder()
                .baseUrl(URL)
                .client(createClient())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonCustomConverterFactory.create(this))
                .build();

    }

    private OkHttpClient createClient(){
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(logging);
        return builder.build();
    }


    @Override
    public void onResponseBody(TypeAdapter typeAdapter, Object responseBody) {
    }
}