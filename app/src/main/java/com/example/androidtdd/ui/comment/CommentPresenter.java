package com.example.androidtdd.ui.comment;

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

public class CommentPresenter extends BasePresenter<CommentMvpView> {
    private PlaceHolderRepository mPlaceHolderRepository;
    private Subscription mSubscription;

    @Inject
    public CommentPresenter(PlaceHolderRepository mPlaceHolderRepository) {
        this.mPlaceHolderRepository = mPlaceHolderRepository;
    }

    public void getComments(User user) {
        getMvpView().showLoading();
        EspressoIdlingResource.increment();
        mSubscription = mPlaceHolderRepository.getComments(user.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(comments -> {
                    EspressoIdlingResource.decrement();
                    getMvpView().hideLoading();
                    if (comments == null || comments.size() == 0) {
                        getMvpView().showEmpty();
                    } else {
                        getMvpView().showComments(comments);
                    }
                }, error -> {
                    EspressoIdlingResource.decrement();
                    getMvpView().hideLoading();
                    getMvpView().showError(error.getMessage());
                    getMvpView().showEmpty();
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
