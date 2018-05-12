package com.james.androidadpractice.homepage;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.james.androidadpractice.R;
import com.james.androidadpractice.client.model.Content;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ContentAdapter extends FooterLoaderAdapter<Content> {

    public static final String TAG = ContentAdapter.class.getSimpleName();
    private Context mContext;

    @Override
    public long getYourItemId(int position) {
        return getItemId(position);
    }

    @Override
    public int getYourItemViewType(int position) {
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder getYourItemViewHolder(ViewGroup parent) {
        mContext = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);

        View view = layoutInflater.inflate(R.layout.list_item, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void bindYourViewHolder(RecyclerView.ViewHolder holder, int position) {
        Content content = mItems.get(position);

        final ListViewHolder listViewHolder = (ListViewHolder) holder;

        Glide.with(mContext)
                .load(content.getImageUrl())
                .apply(new RequestOptions().centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL))
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        listViewHolder.progressBar.setVisibility(View.VISIBLE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        listViewHolder.progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(listViewHolder.imageView);

        listViewHolder.title.setText(content.getTitle());
        listViewHolder.description.setText(content.getDescription());
    }

    class ListViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.image)
        ImageView imageView;
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.description)
        TextView description;
        @BindView(R.id.progress)
        ProgressBar progressBar;

        public ListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void setContents(List<Content> contents) {
        mItems.clear();
        mItems.addAll(contents);
        notifyDataSetChanged();
    }

    public void addContents(List<Content> contents) {
        mItems.addAll(contents);
        notifyDataSetChanged();
    }
}
