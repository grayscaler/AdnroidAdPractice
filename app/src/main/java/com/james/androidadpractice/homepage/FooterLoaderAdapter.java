package com.james.androidadpractice.homepage;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.james.androidadpractice.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * form http://www.jayrambhia.com/blog/footer-loader
 */

public abstract class FooterLoaderAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final String TAG = FooterLoaderAdapter.class.getSimpleName();

    private Context mContext;
    protected boolean showLoader;
    private static final int VIEW_TYPE_LOADER = -1;

    protected List<T> mItems = new ArrayList<>();

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        mContext = viewGroup.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        if (viewType == VIEW_TYPE_LOADER) {

            // Your Loader XML view here
            View view = layoutInflater.inflate(R.layout.loader_item, viewGroup, false);

            // Your LoaderViewHolder class
            return new LoaderViewHolder(view);

        } else {
            return getYourItemViewHolder(viewGroup);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        // Loader ViewHolder
        if (viewHolder instanceof LoaderViewHolder) {
            LoaderViewHolder loaderViewHolder = (LoaderViewHolder) viewHolder;

            if (showLoader) {
                loaderViewHolder.progressBar.setVisibility(View.VISIBLE);
            } else {
                loaderViewHolder.progressBar.setVisibility(View.GONE);
            }
            return;
        }

        bindYourViewHolder(viewHolder, position);
    }

    @Override
    public int getItemCount() {

        // If no items are present, there's no need for loader
        if (mItems == null || mItems.size() == 0) {
            return 0;
        }

        // +1 for loader
        return mItems.size() + 1;
    }

    @Override
    public long getItemId(int position) {

        // loader can't be at position 0
        // loader can only be at the last position
        if (position != 0 && position == getItemCount() - 1) {

            // id of loader is considered as -1 here
            return -1;
        }
        return getYourItemId(position);
    }

    @Override
    public int getItemViewType(int position) {

        // loader can't be at position 0
        // loader can only be at the last position
        if (position != 0 && position == getItemCount() - 1) {
            return VIEW_TYPE_LOADER;
        }

        return getYourItemViewType(position);
    }

    public void showLoading(boolean status) {
        showLoader = status;
    }

    public static class LoaderViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.progressbar)
        ProgressBar progressBar;

        public LoaderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public abstract long getYourItemId(int position);

    public abstract int getYourItemViewType(int position);

    public abstract RecyclerView.ViewHolder getYourItemViewHolder(ViewGroup parent);

    public abstract void bindYourViewHolder(RecyclerView.ViewHolder holder, int position);

}
