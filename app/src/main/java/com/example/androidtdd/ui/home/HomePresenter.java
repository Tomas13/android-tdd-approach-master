package com.example.androidtdd.ui.home;

import com.example.androidtdd.data.PlaceHolderRepository;
import com.example.androidtdd.data.model.User;
import com.example.androidtdd.ui.base.BasePresenter;
import com.example.androidtdd.util.EspressoIdlingResource;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by quanlt on 19/01/2017.
 */

public class HomePresenter extends BasePresenter<HomeMvpView> {
    private PlaceHolderRepository mPlaceHolderRepository;
    private Subscription mSubscription;

    @Inject
    public HomePresenter(PlaceHolderRepository placeHolderRepository) {
        this.mPlaceHolderRepository = placeHolderRepository;
    }

    public void getUsers() {
        getMvpView().showLoading();
        EspressoIdlingResource.increment();
        mSubscription = mPlaceHolderRepository.getUsers()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(users -> {
                    EspressoIdlingResource.decrement();
                    getMvpView().hideLoading();
                    if (users == null || users.size() == 0) {
                        getMvpView().showEmpty();
                    } else {
                        getMvpView().showUsers(users);
                    }
                }, error -> {
                    EspressoIdlingResource.decrement();
                    getMvpView().hideLoading();
                    getMvpView().showEmpty();
                    getMvpView().showError(error.getMessage());
                });
    }

    public void showDetail(User user) {
        getMvpView().viewDetail(user);
    }

    @Override
    public void detachView() {
        if (mSubscription != null)
            mSubscription.unsubscribe();
    }
}
