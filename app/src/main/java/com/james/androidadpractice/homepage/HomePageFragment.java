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
import com.mopub.nativeads.MediaViewBinder;
import com.mopub.nativeads.MoPubNativeAdPositioning;
import com.mopub.nativeads.MoPubRecyclerAdapter;
import com.mopub.nativeads.MoPubStaticNativeAdRenderer;
import com.mopub.nativeads.MoPubVideoNativeAdRenderer;
import com.mopub.nativeads.RequestParameters;
import com.mopub.nativeads.ViewBinder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.james.androidadpractice.Constants.MOPUB_TEST_UNIT_ID;

public class HomePageFragment extends Fragment implements HomePageContract.View {

    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;

    private HomePageContract.Presenter mPresenter;

    private EndlessRecyclerOnScrollListener mEndlessRecyclerOnScrollListener;
    private ContentAdapter mContentAdapter;

    private MoPubRecyclerAdapter mRecyclerAdapter;
    private MoPubSampleAdUnit mAdConfiguration;
    private RequestParameters mRequestParameters;

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

        initMopub();

        initView();

        return root;
    }

    private void initMopub() {
//        mAdConfiguration = MoPubSampleAdUnit.fromBundle(getArguments());

        mContentAdapter = new ContentAdapter();

        MoPubNativeAdPositioning.MoPubClientPositioning adPositioning = MoPubNativeAdPositioning.clientPositioning();

        adPositioning.addFixedPosition(5);
        adPositioning.enableRepeatingPositions(5);

        mRecyclerAdapter = new MoPubRecyclerAdapter(getActivity(), mContentAdapter, adPositioning);

        MoPubStaticNativeAdRenderer moPubStaticNativeAdRenderer = new MoPubStaticNativeAdRenderer(
                new ViewBinder.Builder(R.layout.native_ad_list_item)
                        .titleId(R.id.native_title)
                        .textId(R.id.native_text)
                        .mainImageId(R.id.native_main_image)
                        .iconImageId(R.id.native_icon_image)
                        .callToActionId(R.id.native_cta)
                        .privacyInformationIconImageId(R.id.native_privacy_information_icon_image)
                        .build()
        );

        // Set up a renderer for a video native ad.
        MoPubVideoNativeAdRenderer moPubVideoNativeAdRenderer = new MoPubVideoNativeAdRenderer(
                new MediaViewBinder.Builder(R.layout.video_ad_list_item)
                        .titleId(R.id.native_title)
                        .textId(R.id.native_text)
                        .mediaLayoutId(R.id.native_media_layout)
                        .iconImageId(R.id.native_icon_image)
                        .callToActionId(R.id.native_cta)
                        .privacyInformationIconImageId(R.id.native_privacy_information_icon_image)
                        .build());

        mRecyclerAdapter.registerAdRenderer(moPubStaticNativeAdRenderer);
        mRecyclerAdapter.registerAdRenderer(moPubVideoNativeAdRenderer);

        mRecyclerAdapter.loadAds(MOPUB_TEST_UNIT_ID);
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
                mRecyclerAdapter.refreshAds(MOPUB_TEST_UNIT_ID);
            }
        };
        mSwipeRefreshLayout.setOnRefreshListener(refreshListener);
    }

    private void initRecyclerView() {
        mRecyclerView.setAdapter(mRecyclerAdapter);

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
        mContentAdapter.setContents(contents);
    }

    @Override
    public void addContentToAdapter(List<Content> contents) {
        mContentAdapter.addContents(contents);
    }

//    private ContentAdapter getAdapterFromRecyclerView() {
//        return (ContentAdapter) mRecyclerView.getAdapter();
//    }

    @Override
    public void setAdapterLoading(boolean loading) {
        mContentAdapter.showLoading(loading);
    }

    @Override
    public void initEndlessRecyclerOnScrollListenerVariable() {
        mEndlessRecyclerOnScrollListener.initVariable();
    }

    @Override
    public void setSwipeRefreshLayoutRefreshing(boolean refreshing) {
        mSwipeRefreshLayout.setRefreshing(refreshing);
    }

    @Override
    public void onDestroyView() {
        // You must call this or the ad adapter may cause a memory leak.
        mRecyclerAdapter.destroy();
        super.onDestroyView();
    }
}
