package com.example.androidtdd.ui.detail;

import com.example.androidtdd.data.model.User;
import com.example.androidtdd.ui.base.BaseMvpView;

/**
 * Created by quanlt on 20/01/2017.
 */

public interface DetailMvpView extends BaseMvpView {
    void showPhotos(User user);

    void showComments(User user);
}
