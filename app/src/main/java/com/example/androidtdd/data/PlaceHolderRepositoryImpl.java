package com.example.androidtdd.data;

import com.example.androidtdd.data.local.LocalDataSource;
import com.example.androidtdd.data.model.Comment;
import com.example.androidtdd.data.model.Photo;
import com.example.androidtdd.data.model.User;
import com.example.androidtdd.data.remote.RemoteDataSource;

import java.util.List;

import rx.Observable;

/**
 * Created by quanlt on 20/01/2017.
 */

public class PlaceHolderRepositoryImpl implements PlaceHolderRepository {
    private LocalDataSource mLocalDataSource;
    private RemoteDataSource mRemoteDataSource;

    public PlaceHolderRepositoryImpl(LocalDataSource mLocalDataSource, RemoteDataSource mRemoteDataSource) {
        this.mLocalDataSource = mLocalDataSource;
        this.mRemoteDataSource = mRemoteDataSource;
    }

    @Override
    public Observable<List<User>> getUsers() {
        return mRemoteDataSource.getUsers();
    }

    @Override
    public Observable<List<Comment>> getComments(int userId) {
        return Observable.concat(mLocalDataSource.getComments(userId)
                        .onErrorReturn(error -> null),
                mRemoteDataSource.getComments(userId)
                        .doOnNext(comments -> mLocalDataSource.saveComment(comments, userId)))
                .first(comments -> comments != null && comments.size() > 0);
    }

    @Override
    public Observable<List<Photo>> getPhotos(int userId) {
        return Observable.concat(mLocalDataSource.getPhotos(userId)
                        .onErrorReturn(error -> null),
                mRemoteDataSource.getPhotos(userId)
                        .doOnNext(photos -> mLocalDataSource.savePhotos(photos, userId)))
                .first(photos -> photos != null && photos.size() > 0);
    }
}
