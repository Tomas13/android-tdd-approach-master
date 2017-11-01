package com.example.androidtdd.data.remote;

import com.example.androidtdd.data.model.Comment;
import com.example.androidtdd.data.model.Photo;
import com.example.androidtdd.data.model.User;

import java.util.List;

import rx.Observable;

/**
 * Created by quanlt on 19/01/2017.
 */

public class RemoteDataSourceImpl implements RemoteDataSource {
    private ApiService mApiService;

    public RemoteDataSourceImpl(ApiService mApiService) {
        this.mApiService = mApiService;
    }

    @Override
    public Observable<List<User>> getUsers() {
        return mApiService.getUsers();
    }

    @Override
    public Observable<List<Comment>> getComments(int userId) {
        return mApiService.getComments(userId);
    }

    @Override
    public Observable<List<Photo>> getPhotos(int userId) {
        return mApiService.getPhotos(userId);
    }

}
