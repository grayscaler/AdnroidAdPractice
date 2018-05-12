package com.james.androidadpractice.homepage;

import com.james.androidadpractice.BasePresenter;
import com.james.androidadpractice.BaseView;
import com.james.androidadpractice.client.model.Content;

import java.util.List;

public class HomePageContract {

    interface View extends BaseView<Presenter> {

        void setContentToAdapter(List<Content> contents);

        void addContentToAdapter(List<Content> contents);

        void setSwipeRefreshLayoutRefreshing(boolean refreshing);

        void setAdapterLoading(boolean loading);

        void initEndlessRecyclerOnScrollListenerVariable();
    }

    interface Presenter extends BasePresenter {

        void loadData();

        void loadMoreData();

        void clearCached();
    }
}
