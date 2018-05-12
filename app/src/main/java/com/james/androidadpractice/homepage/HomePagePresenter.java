package com.james.androidadpractice.homepage;

import static com.google.common.base.Preconditions.checkNotNull;

public class HomePagePresenter implements HomePageContract.Presenter {

    private HomePageContract.View mHomePageView;

    public HomePagePresenter(HomePageContract.View homePageView) {
        mHomePageView = checkNotNull(homePageView, "homePageView can't be null");

        mHomePageView.setPresenter(this);
    }

    @Override
    public void start() {

    }
}
