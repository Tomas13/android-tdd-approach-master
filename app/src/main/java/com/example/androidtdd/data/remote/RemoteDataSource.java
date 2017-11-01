package com.example.androidtdd.data.remote;

import com.example.androidtdd.data.model.Comment;
import com.example.androidtdd.data.model.Photo;
import com.example.androidtdd.data.model.User;

import java.util.List;

import rx.Observable;

/**
 * Created by quanlt on 19/01/2017.
 */

public interface RemoteDataSource {

    Observable<List<User>> getUsers();
    
    Observable<List<Comment>> getComments(int userId);

    Observable<List<Photo>> getPhotos(int userId);
}
