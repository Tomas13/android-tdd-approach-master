package com.example.androidtdd.ui.photo;

import com.example.androidtdd.data.model.Photo;
import com.example.androidtdd.ui.base.BaseMvpView;

import java.util.List;

/**
 * Created by quanlt on 23/01/2017.
 */

public interface PhotoMvpView extends BaseMvpView{
    void showPhotos(List<Photo> photos);
}
