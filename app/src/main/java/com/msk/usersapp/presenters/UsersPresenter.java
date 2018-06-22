package com.msk.usersapp.presenters;

import android.content.Context;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANRequest;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.msk.usersapp.mvp.UserMVP;
import com.msk.usersapp.utils.AppConstants;
import com.msk.usersapp.utils.ConnectivityReceiver;
import com.msk.usersapp.utils.UserModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class UsersPresenter implements UserMVP.Presenter {
    private Context mContext;
    private UserMVP.View mUserView;
    private String TAG = "error";

    public UsersPresenter(Context mContext, UserMVP.View mUserView) {
        this.mContext = mContext;
        this.mUserView = mUserView;
    }

    @Override
    public void loadData(String url) {
        if (ConnectivityReceiver.isConnected("loadData")) {
            ANRequest request = AndroidNetworking.get(url)
                    .setPriority(Priority.HIGH)
                    .build();
            Log.w("Success", "URL::: " + request.getUrl());

            request.getAsJSONObject(new JSONObjectRequestListener() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        List<UserModel> dataList = new ArrayList<>();
                        if (response != null) {
                            JSONArray dataJsonArray = response.getJSONArray(AppConstants.DATA_KEY);
                            Log.w("Success", "Response Data::: " + response.toString());
                            if (dataJsonArray.length() > 0) {
                                for (int i = 0; i < dataJsonArray.length(); i++) {
                                    UserModel model = new UserModel(AppConstants.ADAPTER_TYPE);
                                    JSONObject jsonObject = dataJsonArray.getJSONObject(i);
                                    model.setId(jsonObject.getInt(AppConstants.ID_KEY));
                                    model.setFirstName(jsonObject.getString(AppConstants.FIRST_NAME_KEY));
                                    model.setLastName(jsonObject.getString(AppConstants.LAST_NAME_KEY));
                                    model.setImageURL(jsonObject.getString(AppConstants.IMAGE_KEY));
                                    model.setTotalPage(response.getInt(AppConstants.TOTAL_PAGE_KEY));
                                    dataList.add(model);
                                }
                                mUserView.updateAdapter(dataList);
                            } else {
                                mUserView.updateAdapter(dataList);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onError(ANError error) {
                    if (error.getErrorCode() != 0) {
                        Log.d(TAG, "onError errorCode : " + error.getErrorCode());
                        Log.d(TAG, "onError errorBody : " + error.getErrorBody());
                        Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());
                        mUserView.showError(error.getErrorCode(), error.getErrorBody());
                    } else {
                        Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());
                    }
                }
            });
        } else {
            mUserView.showError(0, AppConstants.INTERNET_NOT_AVAILABLE_STR);
        }
    }
}
