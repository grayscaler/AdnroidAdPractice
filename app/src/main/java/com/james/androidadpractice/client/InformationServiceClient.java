package com.james.androidadpractice.client;


import com.james.androidadpractice.client.model.ContentResponse;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.james.androidadpractice.Constants.BASE_URL;

public class InformationServiceClient {

    private InformationServiceApi mApi;

    public InformationServiceClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        mApi = retrofit.create(InformationServiceApi.class);
    }

    public Observable<ContentResponse> rxGetContents(String userName) {
        return mApi.rxGetContents(userName);
    }
}
