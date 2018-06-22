package com.msk.usersapp.utils;

import android.support.v7.widget.RecyclerView;


public abstract class RecyclerViewBaseAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    private OnLoadMoreListener onLoadMoreListener;
    private boolean isLoading = false;
    private boolean moreDataAvailable = false;

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public void setLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        onLoadMoreListener = loadMoreListener;
    }

    public void setMoreDataAvailable(boolean moreDataAvailable) {
        this.moreDataAvailable = moreDataAvailable;
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        if (position >= getItemCount() - 1 && moreDataAvailable && !isLoading && onLoadMoreListener != null) {
            isLoading = true;
            onLoadMoreListener.onLoadMore();
        }
    }

    public void notifyDataChanged() {
        isLoading = false;
        notifyDataSetChanged();
    }
}
