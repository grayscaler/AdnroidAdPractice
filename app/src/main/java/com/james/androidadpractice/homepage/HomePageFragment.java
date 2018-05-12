package com.james.androidadpractice.homepage;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.james.androidadpractice.R;
import com.james.androidadpractice.client.model.Content;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.google.common.base.Preconditions.checkNotNull;

public class HomePageFragment extends Fragment implements HomePageContract.View {

    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;

    private HomePageContract.Presenter mPresenter;

    private EndlessRecyclerOnScrollListener mEndlessRecyclerOnScrollListener;

    public static HomePageFragment newInstance() {
        return new HomePageFragment();
    }

    @Override
    public void setPresenter(HomePageContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.home_page_frag, container, false);

        ButterKnife.bind(this, root);

        initView();

        return root;
    }

    private void initView() {
        initSwipeRefreshLayout();
        initRecyclerView();
    }

    private void initSwipeRefreshLayout() {
        mSwipeRefreshLayout.setRefreshing(true);

        SwipeRefreshLayout.OnRefreshListener refreshListener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.clearCached();
                mPresenter.loadData();
            }
        };
        mSwipeRefreshLayout.setOnRefreshListener(refreshListener);
    }

    private void initRecyclerView() {
        mRecyclerView.setAdapter(new ContentAdapter());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        EndlessRecyclerOnScrollListener endlessRecyclerOnScrollListener = getEndlessRecyclerOnScrollListener();
        endlessRecyclerOnScrollListener.setLayoutManager(mRecyclerView.getLayoutManager());
        mRecyclerView.addOnScrollListener(endlessRecyclerOnScrollListener);
    }

    @NonNull
    private EndlessRecyclerOnScrollListener getEndlessRecyclerOnScrollListener() {
        if (mEndlessRecyclerOnScrollListener == null) {
            mEndlessRecyclerOnScrollListener = new EndlessRecyclerOnScrollListener() {
                @Override
                public void onLoadMore(int currentPage) { // when we have reached end of RecyclerView this event fired
                    Log.d(TAG, "onLoadMore: ");
                    // FIXME: 2018/4/2 timing issue
                    if (mPresenter != null) {
                        mPresenter.loadMoreData();
                    }
                }
            };
        }
        return mEndlessRecyclerOnScrollListener;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void setContentToAdapter(List<Content> contents) {
        getAdapterFromRecyclerView().setContents(contents);
    }

    @Override
    public void addContentToAdapter(List<Content> contents) {
        getAdapterFromRecyclerView().addContents(contents);
    }

    private ContentAdapter getAdapterFromRecyclerView() {
        return (ContentAdapter) mRecyclerView.getAdapter();
    }

    @Override
    public void setAdapterLoading(boolean loading) {
        getAdapterFromRecyclerView().showLoading(loading);
    }

    @Override
    public void initEndlessRecyclerOnScrollListenerVariable() {
        mEndlessRecyclerOnScrollListener.initVariable();
    }

    @Override
    public void setSwipeRefreshLayoutRefreshing(boolean refreshing) {
        mSwipeRefreshLayout.setRefreshing(refreshing);
    }
}
