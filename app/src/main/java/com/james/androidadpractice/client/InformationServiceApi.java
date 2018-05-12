package com.james.androidadpractice.client;

import com.james.androidadpractice.client.model.ContentResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface InformationServiceApi {

    @GET("grayscaler/codingtest/{contentType}")
    Observable<ContentResponse> rxGetContents(@Path("contentType") String contentType);
}
