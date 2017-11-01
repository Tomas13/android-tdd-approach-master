package com.example.androidtdd.ui.detail;

import com.example.androidtdd.data.model.User;
import com.example.androidtdd.ui.base.BasePresenter;

import javax.inject.Inject;

/**
 * Created by quanlt on 23/01/2017.
 */

public class DetailPresenter extends BasePresenter<DetailMvpView> {


    @Inject
    public DetailPresenter() {

    }

    public void showPhotos(User user) {
        getMvpView().showPhotos(user);
    }

    public void showComments(User user) {
        getMvpView().showComments(user);
    }
}
