package com.example.androidtdd.ui.base;

/**
 * Created by quanlt on 19/01/2017.
 */

public interface Presenter<V extends MvpView> {
    void attachView(V v);

    void detachView();
}
