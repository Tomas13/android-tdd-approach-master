package com.example.androidtdd.data.local;

import com.example.androidtdd.data.model.Comment;
import com.example.androidtdd.data.model.Photo;

import java.util.List;

import rx.Observable;

/**
 * Created by quanlt on 19/01/2017.
 */

public interface LocalDataSource {
    void saveComment(List<Comment> comments, int userId);

    void savePhotos(List<Photo> photos, int userId);

    Observable<List<Comment>> getComments(int userId);

    Observable<List<Photo>> getPhotos(int userId);
}
