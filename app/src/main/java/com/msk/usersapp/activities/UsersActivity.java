package com.msk.usersapp.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.msk.usersapp.R;
import com.msk.usersapp.adapters.UsersAdapter;
import com.msk.usersapp.mvp.UserMVP;
import com.msk.usersapp.presenters.UsersPresenter;
import com.msk.usersapp.utils.AppConstants;
import com.msk.usersapp.utils.UserModel;

import java.util.ArrayList;
import java.util.List;

public class UsersActivity extends AppCompatActivity implements UserMVP.View {

    RecyclerView mUserRecyclerView;
    TextView mNoDataFoundTV;
    private UsersAdapter mUsersAdapter;
    private List<UserModel> mUserData;
    private Context mContext;
    private ProgressDialog pd;
    private Activity mActivity;
    private UsersPresenter mUsersPresenter;
    private int index = 0;
    private int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        mActivity = UsersActivity.this;
        mContext = getApplicationContext();
        mUserData = new ArrayList<>();
        pd = new ProgressDialog(mActivity);
        pd.setCancelable(false);
        mUsersPresenter = new UsersPresenter(mContext, this);

        mUserRecyclerView = findViewById(R.id.commonRecyclerView);
        mNoDataFoundTV = findViewById(R.id.commonNoRecordsTV);
        mUserRecyclerView.setHasFixedSize(true);
        mUserRecyclerView.setLayoutManager(new LinearLayoutManager(UsersActivity.this));

        mUsersAdapter = new UsersAdapter(mContext, mUserData);
        mUserRecyclerView.setAdapter(mUsersAdapter);

        mUsersAdapter.setLoadMoreListener(new UsersAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                mUserRecyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        index = mUserData.size();
                        mUserData.add(new UserModel(AppConstants.LOADING_TYPE));
                        mUsersAdapter.notifyItemChanged(mUserData.size() - 1);
                        page = page + 1;
                        Log.w("Success", "Total Page::::" + mUserData.get(0).getTotalPage());
                        if (page <= mUserData.get(0).getTotalPage()) {
                            mUsersAdapter.setMoreDataAvailable(true);
                            mUsersPresenter.loadData(AppConstants.URL.concat(String.valueOf(page)));
                        } else {
                            updateAdapter(new ArrayList<UserModel>());
                        }
                    }
                });
            }
        });
        showProgressDialog(getString(R.string.processDialogString));
        mUsersPresenter.loadData(AppConstants.URL.concat(String.valueOf(page)));

    }

    @Override
    public void updateAdapter(List<UserModel> usersData) {
        closeProgressDialog();
        if (usersData.size() > 0) {
            if (index > 0) {
                mUserData.remove(index);
            }
            mUserData.addAll(usersData);
            mUsersAdapter.notifyDataChanged();
            mUsersAdapter.setMoreDataAvailable(true);
            mUserRecyclerView.setVisibility(View.VISIBLE);
            mNoDataFoundTV.setVisibility(View.GONE);
        } else {
            if (index > 0) {
                mUserData.remove(index);
            }
            Toast.makeText(mActivity, "No more data found...", Toast.LENGTH_SHORT).show();
            mUsersAdapter.setMoreDataAvailable(false);
        }
        mUserRecyclerView.setVisibility(mUserData.size() > 0 ? View.VISIBLE : View.GONE);
        mNoDataFoundTV.setVisibility(mUserData.size() > 0 ? View.GONE : View.VISIBLE);
    }

    @Override
    public void showError(int code, String message) {
        closeProgressDialog();
    }

    private void closeProgressDialog() {
        if (pd != null && pd.isShowing()) {
            pd.dismiss();
        }
    }

    private void showProgressDialog(String message) {
        pd.setMessage(message);
        if (pd != null)
            pd.show();
    }
}
