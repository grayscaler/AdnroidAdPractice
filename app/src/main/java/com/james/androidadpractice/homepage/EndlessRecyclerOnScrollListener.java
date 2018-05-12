package com.james.androidadpractice.homepage;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;

/**
 * from https://gist.github.com/ssinss/e06f12ef66c51252563e
 * https://gist.github.com/nesquena/8a976dd3d6f866518db2cfe7f9cb0db7
 **/

public abstract class EndlessRecyclerOnScrollListener extends RecyclerView.OnScrollListener {
    public static String TAG = EndlessRecyclerOnScrollListener.class.getSimpleName();

    private boolean loading = true; // True if we are still waiting for the last set of data to load.

    private int startingPageIndex = 0;
    private int currentPage = 1;
    private int previousTotal = 0; // The total number of items in the dataset after the last load
    private int visibleThreshold = 5; // The minimum amount of items to have below your current scroll position before loading more.

    private RecyclerView.LayoutManager mLayoutManager;

    public EndlessRecyclerOnScrollListener() {
    }

    public void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        mLayoutManager = layoutManager;
    }

    public void initVariable() {
        this.startingPageIndex = 1;
        this.currentPage = 1;
        this.previousTotal = 0;
        this.visibleThreshold = 5;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getLastVisibleItem(int[] lastVisibleItemPositions) {
        int maxSize = 0;
        for (int i = 0; i < lastVisibleItemPositions.length; i++) {
            if (i == 0) {
                maxSize = lastVisibleItemPositions[i];
            } else if (lastVisibleItemPositions[i] > maxSize) {
                maxSize = lastVisibleItemPositions[i];
            }
        }
        return maxSize;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        int lastVisibleItemPosition = 0;
        int totalItemCount = mLayoutManager.getItemCount();

        if (mLayoutManager instanceof StaggeredGridLayoutManager) {
            int[] lastVisibleItemPositions = ((StaggeredGridLayoutManager) mLayoutManager).findLastVisibleItemPositions(null);
            // get maximum element within the list
            lastVisibleItemPosition = getLastVisibleItem(lastVisibleItemPositions);
        } else if (mLayoutManager instanceof LinearLayoutManager) {
            lastVisibleItemPosition = ((LinearLayoutManager) mLayoutManager).findLastVisibleItemPosition();
        } else if (mLayoutManager instanceof GridLayoutManager) {
            lastVisibleItemPosition = ((GridLayoutManager) mLayoutManager).findLastVisibleItemPosition();
        }

        Log.d(TAG, "onScrolled: loading:" + loading + " totalItemCount:" + totalItemCount + " previousTotal:" + previousTotal);

        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false;
                previousTotal = totalItemCount;
            }
        }

        Log.d(TAG, "onScrolled: loading:" + loading + " totalItemCount:" + totalItemCount + " lastVisibleItemPosition:" + lastVisibleItemPosition + " visibleThreshold:" + visibleThreshold);
        if (!loading && (totalItemCount) <= (lastVisibleItemPosition + visibleThreshold)) {
            // End has been reached

            // Do something
            currentPage++;

            onLoadMore(currentPage);

            loading = true;
        }
    }

    public abstract void onLoadMore(int current_page);
}