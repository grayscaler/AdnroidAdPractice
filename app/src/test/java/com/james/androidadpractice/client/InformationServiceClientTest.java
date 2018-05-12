package com.james.androidadpractice.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.james.androidadpractice.client.model.ContentResponse;

import org.junit.Test;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.plugins.RxJavaPlugins;

import static com.james.androidadpractice.Constants.CONTENT_TYPE_NEWS;
import static org.junit.Assert.assertEquals;

public class InformationServiceClientTest {

    @Test
    public void rxGetContents() throws Exception {
        RxJavaPlugins.setIoSchedulerHandler(new Function<Scheduler, Scheduler>() {
            @Override
            public Scheduler apply(Scheduler scheduler) throws Exception {
                return AndroidSchedulers.mainThread();
            }
        });

        final ContentResponse[] response = new ContentResponse[1];
        InformationServiceClient informationServiceClient = new InformationServiceClient();

        informationServiceClient.rxGetContents(CONTENT_TYPE_NEWS).subscribe(new Consumer<ContentResponse>() {
            @Override
            public void accept(ContentResponse contentResponse) throws Exception {
                response[0] = contentResponse;
            }
        });
        assertEquals(CONTENT_TYPE_NEWS, response[0].getType());

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        System.out.println(gson.toJson(response[0]));
    }
}