package com.james.androidadpractice.homepage;

import android.support.annotation.NonNull;

import com.james.androidadpractice.client.InformationServiceClient;
import com.james.androidadpractice.client.model.Content;
import com.james.androidadpractice.client.model.ContentResponse;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.james.androidadpractice.Constants.CONTENT_TYPE_NEWS;

public class HomePagePresenter implements HomePageContract.Presenter {

    private HomePageContract.View mHomePageView;
    private InformationServiceClient mInformationServiceClient;

    private static List<Content> mCached;

    private boolean firstLaunch = true;

    public HomePagePresenter(HomePageContract.View homePageView, InformationServiceClient informationServiceClient) {
        mHomePageView = checkNotNull(homePageView, "homePageView can't be null");
        mInformationServiceClient = informationServiceClient;

        mHomePageView.setPresenter(this);
    }

    @Override
    public void start() {
        loadData();
    }

    @Override
    public void loadData() {

        if (firstLaunch) {
            mHomePageView.setContentToAdapter(getFakeHits());
            firstLaunch = false;
        }

        //check cached
        if (mCached != null && !mCached.isEmpty()) {
            mHomePageView.setSwipeRefreshLayoutRefreshing(false);
            mHomePageView.setContentToAdapter(mCached);
            return;
        }

        mHomePageView.setSwipeRefreshLayoutRefreshing(true);
        mHomePageView.initEndlessRecyclerOnScrollListenerVariable();

        mInformationServiceClient.rxGetContents(CONTENT_TYPE_NEWS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ContentResponse>() {
                               @Override
                               public void accept(ContentResponse contentResponse) throws Exception {
                                   mHomePageView.setSwipeRefreshLayoutRefreshing(false);
                                   mHomePageView.setContentToAdapter(contentResponse.getData().getContents());
                                   addAllToCached(contentResponse.getData().getContents());
                               }
                           }, new Consumer<Throwable>() {
                               @Override
                               public void accept(Throwable throwable) throws Exception {
                                   mHomePageView.setSwipeRefreshLayoutRefreshing(false);
                               }
                           }
                );
    }

    @Override
    public void loadMoreData() {

        mHomePageView.setAdapterLoading(true);

        mInformationServiceClient.rxGetContents(CONTENT_TYPE_NEWS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ContentResponse>() {
                               @Override
                               public void accept(ContentResponse contentResponse) throws Exception {
                                   mHomePageView.setAdapterLoading(false);
                                   mHomePageView.addContentToAdapter(contentResponse.getData().getContents());
                                   addToCached(contentResponse.getData().getContents());
                               }
                           }, new Consumer<Throwable>() {
                               @Override
                               public void accept(Throwable throwable) throws Exception {
                                   mHomePageView.setAdapterLoading(false);
                               }
                           }
                );
    }

    @NonNull
    private List<Content> getFakeHits() {
        List<Content> mFakeData = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            mFakeData.add(new Content());
        }
        return mFakeData;
    }

    @Override
    public void clearCached() {
        if (mCached != null) {
            mCached.clear();
        }
    }

    public void addAllToCached(List<Content> contents) {
        if (mCached == null) {
            mCached = new ArrayList<>();
        }
        mCached.clear();
        mCached.addAll(contents);
    }

    private void addToCached(List<Content> contents) {
        if (mCached == null) {
            mCached = new ArrayList<>();
        }
        mCached.addAll(contents);
    }
}
