package com.example.androidtdd.data;

import android.net.Uri;

import com.example.androidtdd.MockHelper;
import com.example.androidtdd.data.local.LocalDataSource;
import com.example.androidtdd.data.model.Comment;
import com.example.androidtdd.data.model.Photo;
import com.example.androidtdd.data.model.User;
import com.example.androidtdd.data.remote.RemoteDataSource;
import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.observers.TestSubscriber;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by quanlt on 20/01/2017.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(Uri.class)
public class PlaceHolderRepositoryImplTest {
    @Mock
    LocalDataSource mMockLocalDataSource;
    @Mock
    RemoteDataSource mMockRemoteDataSource;
    private PlaceHolderRepository mPlaceHolderRepository;
    private int USER_ID = 10;

    private TestSubscriber<List<Comment>> commentsTestSubscriber = new TestSubscriber<>();
    private TestSubscriber<List<Photo>> photosTestSubscriber = new TestSubscriber<>();
    private TestSubscriber<List<User>> usersTestSubscriber = new TestSubscriber<>();

    @Before
    public void setUp() throws Exception {
        mPlaceHolderRepository = new PlaceHolderRepositoryImpl(mMockLocalDataSource, mMockRemoteDataSource);
        PowerMockito.mockStatic(Uri.class);
        Uri uri = mock(Uri.class);
        PowerMockito.when(Uri.class, "parse", anyString()).thenReturn(uri);
    }

    @Test
    public void testGetUsers() throws Exception {
        when(mPlaceHolderRepository.getUsers())
                .thenReturn(Observable.just(MockHelper.getUsers()));
        mPlaceHolderRepository.getUsers().subscribe(usersTestSubscriber);
        verify(mMockRemoteDataSource, times(1)).getUsers();
        usersTestSubscriber.assertCompleted();
        usersTestSubscriber.assertValue(MockHelper.getUsers());
        usersTestSubscriber.assertNoErrors();
    }

    @Test
    public void testGetUsersFail() {
        Throwable error = new Throwable("Error");
        when(mMockRemoteDataSource.getUsers())
                .thenReturn(Observable.error(error));
        mPlaceHolderRepository.getUsers().subscribe(usersTestSubscriber);
        usersTestSubscriber.assertError(error);
    }

    @Test
    public void testGetCommentsFromServer() throws Exception {
        String commentsString = MockHelper.readCommentsString();
        List<Comment> comments = MockHelper.getComments(commentsString, new Gson());
        when(mMockLocalDataSource.getComments(USER_ID))
                .thenReturn(Observable.error(new Throwable()));
        when(mMockRemoteDataSource.getComments(USER_ID))
                .thenReturn(Observable.just(comments));
        mPlaceHolderRepository.getComments(USER_ID).subscribe(commentsTestSubscriber);
        verify(mMockRemoteDataSource).getComments(USER_ID);
        commentsTestSubscriber.assertNoErrors();
        commentsTestSubscriber.assertCompleted();
        commentsTestSubscriber.assertValue(comments);
    }

    @Test
    public void testGetCommentsFromLocal() throws Exception {
        String commentsString = MockHelper.readCommentsString();
        List<Comment> comments = MockHelper.getComments(commentsString, new Gson());
        when(mMockLocalDataSource.getComments(USER_ID))
                .thenReturn(Observable.just(comments));
        when(mMockRemoteDataSource.getComments(USER_ID))
                .thenReturn(Observable.just(new ArrayList<>()));
        mPlaceHolderRepository.getComments(USER_ID).subscribe(commentsTestSubscriber);
        verify(mMockLocalDataSource).getComments(USER_ID);
        commentsTestSubscriber.assertNoErrors();
        commentsTestSubscriber.assertCompleted();
        commentsTestSubscriber.assertValue(comments);
    }

    @Test
    public void testSaveCommentsComeFromServer() throws Exception {
        String commentsString = MockHelper.readCommentsString();
        List<Comment> comments = MockHelper.getComments(commentsString, new Gson());
        when(mMockLocalDataSource.getComments(USER_ID))
                .thenReturn(Observable.error(new Throwable()));
        when(mMockRemoteDataSource.getComments(USER_ID))
                .thenReturn(Observable.just(comments));
        mPlaceHolderRepository.getComments(USER_ID).subscribe(commentsTestSubscriber);
        verify(mMockRemoteDataSource).getComments(USER_ID);
        verify(mMockLocalDataSource).saveComment(comments, USER_ID);
        commentsTestSubscriber.assertNoErrors();
        commentsTestSubscriber.assertCompleted();
        commentsTestSubscriber.assertValue(comments);
    }

    @Test
    public void testGetPhotosFromServer() throws Exception {
        String photosString = MockHelper.readPhotosString();
        List<Photo> photos = MockHelper.getPhotos(photosString, new Gson());
        when(mMockLocalDataSource.getPhotos(USER_ID))
                .thenReturn(Observable.error(new Throwable()));
        when(mMockRemoteDataSource.getPhotos(USER_ID))
                .thenReturn(Observable.just(photos));
        mPlaceHolderRepository.getPhotos(USER_ID).subscribe(photosTestSubscriber);
        verify(mMockRemoteDataSource).getPhotos(USER_ID);
        photosTestSubscriber.assertNoErrors();
        photosTestSubscriber.assertCompleted();
        photosTestSubscriber.assertValue(photos);
    }

    @Test
    public void testGetPhotosFromLocal() throws Exception {
        String photosString = MockHelper.readPhotosString();
        List<Photo> photos = MockHelper.getPhotos(photosString, new Gson());
        when(mMockLocalDataSource.getPhotos(USER_ID))
                .thenReturn(Observable.just(photos));
        when(mMockRemoteDataSource.getPhotos(USER_ID))
                .thenReturn(Observable.empty());
        mPlaceHolderRepository.getPhotos(USER_ID).subscribe(photosTestSubscriber);
        verify(mMockLocalDataSource).getPhotos(USER_ID);
        photosTestSubscriber.assertNoErrors();
        photosTestSubscriber.assertCompleted();
        photosTestSubscriber.assertValue(photos);
    }

    @Test
    public void testSavePhotosComeFromServer() throws Exception {
        String photosString = MockHelper.readPhotosString();
        List<Photo> photos = MockHelper.getPhotos(photosString, new Gson());
        when(mMockLocalDataSource.getPhotos(USER_ID))
                .thenReturn(Observable.error(new Throwable()));
        when(mMockRemoteDataSource.getPhotos(USER_ID))
                .thenReturn(Observable.just(photos));
        mPlaceHolderRepository.getPhotos(USER_ID).subscribe(photosTestSubscriber);
        verify(mMockRemoteDataSource).getPhotos(USER_ID);
        verify(mMockLocalDataSource).savePhotos(photos, USER_ID);
        photosTestSubscriber.assertNoErrors();
        photosTestSubscriber.assertCompleted();
        photosTestSubscriber.assertValue(photos);
    }
}
