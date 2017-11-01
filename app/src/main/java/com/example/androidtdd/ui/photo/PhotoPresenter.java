package com.example.androidtdd.ui.photo;

import com.example.androidtdd.data.PlaceHolderRepository;
import com.example.androidtdd.data.model.User;
import com.example.androidtdd.ui.base.BasePresenter;
import com.example.androidtdd.util.EspressoIdlingResource;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by quanlt on 23/01/2017.
 */

public class PhotoPresenter extends BasePresenter<PhotoMvpView> {
    private PlaceHolderRepository mPlaceHolderRepository;
    private Subscription mSubscription;

    @Inject
    public PhotoPresenter(PlaceHolderRepository mPlaceHolderRepository) {
        this.mPlaceHolderRepository = mPlaceHolderRepository;
    }

    public void getPhotos(User user) {
        getMvpView().showLoading();
        EspressoIdlingResource.increment();
        mSubscription = mPlaceHolderRepository.getPhotos(user.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(photos -> {
                    EspressoIdlingResource.decrement();
                    getMvpView().hideLoading();
                    if (photos == null || photos.size() == 0) {
                        getMvpView().showEmpty();
                    } else {
                        getMvpView().showPhotos(photos);
                    }
                }, error -> {
                    EspressoIdlingResource.decrement();
                    getMvpView().hideLoading();
                    getMvpView().showEmpty();
                    getMvpView().showError(error.getMessage());
                });
    }

    @Override
    public void detachView() {
        if (mSubscription != null) {
            mSubscription.unsubscribe();
        }
        super.detachView();
    }
}
