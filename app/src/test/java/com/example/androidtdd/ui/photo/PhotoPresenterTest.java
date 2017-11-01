package com.example.androidtdd.ui.photo;

import android.net.Uri;

import com.example.androidtdd.MockHelper;
import com.example.androidtdd.data.PlaceHolderRepository;
import com.example.androidtdd.data.model.Photo;
import com.example.androidtdd.data.model.User;
import com.example.androidtdd.util.RxSchedulerOverrideRule;
import com.google.gson.Gson;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.List;

import rx.Observable;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by quanlt on 23/01/2017.
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest(Uri.class)
public class PhotoPresenterTest {
    @Mock
    PlaceHolderRepository mMockPlaceHolderRepository;
    @Mock
    PhotoMvpView mMockPhotoMvpView;

    @Rule
    public RxSchedulerOverrideRule rule = new RxSchedulerOverrideRule();

    private PhotoPresenter mPhotoPresenter;
    private final int USER_ID = 10;

    @Before
    public void setUp() throws Exception {
        mPhotoPresenter = new PhotoPresenter(mMockPlaceHolderRepository);
        mPhotoPresenter.attachView(mMockPhotoMvpView);
        PowerMockito.mockStatic(Uri.class);
        Uri uri = mock(Uri.class);
        PowerMockito.when(Uri.class, "parse", anyString()).thenReturn(uri);
    }

    @Test
    public void testGetCommentSuccess() throws Exception {
        String photosString = MockHelper.readPhotosString();
        List<Photo> photos = MockHelper.getPhotos(photosString, new Gson());
        User user = MockHelper.makeUser(USER_ID);
        when(mMockPlaceHolderRepository.getPhotos(USER_ID))
                .thenReturn(Observable.just(photos));
        mPhotoPresenter.getPhotos(user);
        verify(mMockPhotoMvpView).showLoading();
        verify(mMockPhotoMvpView).hideLoading();
        verify(mMockPhotoMvpView).showPhotos(photos);
        verify(mMockPhotoMvpView, times(0)).showError(anyString());
        verify(mMockPhotoMvpView, times(0)).showEmpty();
    }

    @Test
    public void testGetCommentFailed() throws Exception {
        String message = "Unknown error";
        User user = MockHelper.makeUser(USER_ID);
        when(mMockPlaceHolderRepository.getPhotos(USER_ID))
                .thenReturn(Observable.error(new Throwable(message)));
        mPhotoPresenter.getPhotos(user);
        verify(mMockPhotoMvpView).showLoading();
        verify(mMockPhotoMvpView).hideLoading();
        verify(mMockPhotoMvpView, times(0)).showPhotos(any(List.class));
        verify(mMockPhotoMvpView, times(1)).showError(message);
        verify(mMockPhotoMvpView, times((1))).showEmpty();
    }

    @Test
    public void testGetPhotosReturnNull() throws Exception {
        when(mMockPlaceHolderRepository.getPhotos(USER_ID))
                .thenReturn(Observable.just(null));
        User user = MockHelper.makeUser(USER_ID);
        mPhotoPresenter.getPhotos(user);
        verify(mMockPhotoMvpView).showLoading();
        verify(mMockPhotoMvpView).hideLoading();
        verify(mMockPhotoMvpView, times(0)).showPhotos(any(List.class));
        verify(mMockPhotoMvpView, times(0)).showError(anyString());
        verify(mMockPhotoMvpView, times((1))).showEmpty();

    }
    @Test
    public void testGetPhotosReturnEmptyList() throws Exception {
        when(mMockPlaceHolderRepository.getPhotos(USER_ID))
                .thenReturn(Observable.just(mock(List.class)));
        User user = MockHelper.makeUser(USER_ID);
        mPhotoPresenter.getPhotos(user);
        verify(mMockPhotoMvpView).showLoading();
        verify(mMockPhotoMvpView).hideLoading();
        verify(mMockPhotoMvpView, times(0)).showPhotos(any(List.class));
        verify(mMockPhotoMvpView, times(0)).showError(anyString());
        verify(mMockPhotoMvpView, times((1))).showEmpty();

    }

    @After
    public void tearDown() throws Exception {
        mPhotoPresenter.detachView();
    }
}
