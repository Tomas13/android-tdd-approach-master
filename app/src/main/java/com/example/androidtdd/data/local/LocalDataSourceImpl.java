package com.example.androidtdd.data.local;

import com.example.androidtdd.data.model.Comment;
import com.example.androidtdd.data.model.Photo;

import java.util.List;

import io.realm.Realm;
import rx.Observable;

/**
 * Created by quanlt on 19/01/2017.
 */

public class LocalDataSourceImpl implements LocalDataSource {

    public LocalDataSourceImpl() {
    }

    @Override
    public void saveComment(List<Comment> comments, int userId) {
        for (Comment comment : comments) {
            comment.setUserId(userId);
        }
        Realm bgRealm = Realm.getDefaultInstance();
        bgRealm.executeTransaction(realm -> realm.copyToRealmOrUpdate(comments));
        bgRealm.close();
    }

    @Override
    public void savePhotos(List<Photo> photos, int userId) {
        Realm bgRealm = Realm.getDefaultInstance();
        for (Photo photo : photos) {
            photo.setUserId(userId);
        }
        bgRealm.executeTransaction(realm -> realm.copyToRealmOrUpdate(photos));
        bgRealm.close();

    }

    @Override
    public Observable<List<Comment>> getComments(int userId) {
        Realm bgRealm = Realm.getDefaultInstance();
        List<Comment> comments = bgRealm
                .where(Comment.class)
                .equalTo("userId", userId)
                .findAll();
        if (comments != null) {
            comments = bgRealm.copyFromRealm(comments);
        }
        bgRealm.close();
        return Observable.just(comments);
    }

    @Override
    public Observable<List<Photo>> getPhotos(int userId) {
        Realm bgRealm = Realm.getDefaultInstance();
        List<Photo> photos = bgRealm
                .where(Photo.class)
                .equalTo("userId", userId)
                .findAll();
        if (photos != null) {
            photos = bgRealm.copyFromRealm(photos);
        }
        bgRealm.close();
        return Observable.just(photos);
    }
}
