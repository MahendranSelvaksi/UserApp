package com.msk.usersapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.msk.usersapp.R;
import com.msk.usersapp.utils.AppConstants;
import com.msk.usersapp.utils.RecyclerViewBaseAdapter;
import com.msk.usersapp.utils.UserModel;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<UserModel> mData;
    private boolean isLoading = false;

    private OnLoadMoreListener onLoadMoreListener;
    private boolean moreDataAvailable = false;

    public UsersAdapter(Context mContext, List<UserModel> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }


    public void setLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        onLoadMoreListener = loadMoreListener;
    }


    public void setMoreDataAvailable(boolean moreDataAvailable) {
        this.moreDataAvailable = moreDataAvailable;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position >= getItemCount() - 1 && moreDataAvailable && !isLoading && onLoadMoreListener != null) {
            isLoading = true;
            onLoadMoreListener.onLoadMore();
        }
        if (holder instanceof UserHolder) {
            ((UserHolder) holder).firstNameTV.setText(mData.get(position).getFirstName().trim());
            ((UserHolder) holder).lastNameTV.setText(mData.get(position).getLastName().trim());
            Glide.with(mContext)
                    .load(mData.get(position).getImageURL())
                    .fitCenter()
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            //holder.progressBar.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(((UserHolder) holder).profileImageView);
        }
    }


    public void notifyDataChanged() {
        isLoading = false;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        if (viewType == 1) {
            return new LoadingHolder(inflater.inflate(R.layout.bottom_loading, parent, false));
        } else {
            return new UserHolder(inflater.inflate(R.layout.user_adapter, parent, false));
        }
    }

    @Override
    public int getItemViewType(int position) {
        super.getItemViewType(position);
        if (mData.get(position).getAdapterType().equalsIgnoreCase(AppConstants.ADAPTER_TYPE)) {
            return 0;
        }
        return 1;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    private class UserHolder extends RecyclerView.ViewHolder {

        ImageView profileImageView;
        private ProgressBar user_image_prog;
        private TextView firstNameTV, lastNameTV;

        UserHolder(@NonNull View itemView) {
            super(itemView);
            profileImageView = itemView.findViewById(R.id.user_image);
            user_image_prog = itemView.findViewById(R.id.user_image_prog);
            firstNameTV = itemView.findViewById(R.id.firstNameTV);
            lastNameTV = itemView.findViewById(R.id.lastNameTV);
        }
    }

    private static class LoadingHolder extends RecyclerView.ViewHolder {
        LoadingHolder(View itemView) {
            super(itemView);
        }
    }
    public interface OnLoadMoreListener {
        void onLoadMore();
    }

}
