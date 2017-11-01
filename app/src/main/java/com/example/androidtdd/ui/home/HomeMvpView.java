package com.example.androidtdd.ui.home;

import com.example.androidtdd.data.model.User;
import com.example.androidtdd.ui.base.BaseMvpView;
import com.example.androidtdd.ui.base.MvpView;

import java.util.List;


/**
 * Created by quanlt on 19/01/2017.
 */

public interface HomeMvpView extends BaseMvpView{
    void showUsers(List<User> users);
    void viewDetail(User user);
}
