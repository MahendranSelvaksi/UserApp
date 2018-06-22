package com.msk.usersapp.mvp;

import com.msk.usersapp.utils.UserModel;

import java.util.List;

public interface UserMVP {

    interface View{
        void updateAdapter(List<UserModel> usersData);
        void showError(int code,String message);
    }

    interface Presenter{
        void loadData(String url);
    }
}
